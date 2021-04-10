package com.bumptech.glide.load.model;

import android.content.*;
import com.bumptech.glide.load.data.*;
import java.util.*;

public class GenericLoaderFactory
{
    private static final ModelLoader NULL_MODEL_LOADER;
    private final Map<Class, Map<Class, ModelLoader>> cachedModelLoaders;
    private final Context context;
    private final Map<Class, Map<Class, ModelLoaderFactory>> modelClassToResourceFactories;
    
    static {
        NULL_MODEL_LOADER = new ModelLoader() {
            @Override
            public DataFetcher getResourceFetcher(final Object o, final int n, final int n2) {
                throw new NoSuchMethodError("This should never be called!");
            }
            
            @Override
            public String toString() {
                return "NULL_MODEL_LOADER";
            }
        };
    }
    
    public GenericLoaderFactory(final Context context) {
        this.modelClassToResourceFactories = new HashMap<Class, Map<Class, ModelLoaderFactory>>();
        this.cachedModelLoaders = new HashMap<Class, Map<Class, ModelLoader>>();
        this.context = context.getApplicationContext();
    }
    
    private <T, Y> void cacheModelLoader(final Class<T> clazz, final Class<Y> clazz2, final ModelLoader<T, Y> modelLoader) {
        Object o;
        if ((o = this.cachedModelLoaders.get(clazz)) == null) {
            o = new HashMap<Class<Y>, ModelLoader<T, Y>>();
            this.cachedModelLoaders.put(clazz, (Map<Class, ModelLoader>)o);
        }
        ((Map<Class<Y>, ModelLoader<T, Y>>)o).put(clazz2, modelLoader);
    }
    
    private <T, Y> void cacheNullLoader(final Class<T> clazz, final Class<Y> clazz2) {
        this.cacheModelLoader(clazz, clazz2, GenericLoaderFactory.NULL_MODEL_LOADER);
    }
    
    private <T, Y> ModelLoader<T, Y> getCachedLoader(final Class<T> clazz, final Class<Y> clazz2) {
        final Map<Class, ModelLoader> map = this.cachedModelLoaders.get(clazz);
        ModelLoader<T, Y> modelLoader = null;
        if (map != null) {
            modelLoader = map.get(clazz2);
        }
        return modelLoader;
    }
    
    private <T, Y> ModelLoaderFactory<T, Y> getFactory(final Class<T> clazz, final Class<Y> clazz2) {
        final Map<Class, ModelLoaderFactory> map = this.modelClassToResourceFactories.get(clazz);
        ModelLoaderFactory<T, Y> modelLoaderFactory = null;
        if (map != null) {
            modelLoaderFactory = map.get(clazz2);
        }
        ModelLoaderFactory<T, Y> modelLoaderFactory2;
        if ((modelLoaderFactory2 = modelLoaderFactory) == null) {
            final Iterator<Class> iterator = this.modelClassToResourceFactories.keySet().iterator();
            while (true) {
                modelLoaderFactory2 = modelLoaderFactory;
                if (!iterator.hasNext()) {
                    break;
                }
                final Class clazz3 = iterator.next();
                ModelLoaderFactory<T, Y> modelLoaderFactory3 = modelLoaderFactory;
                if (clazz3.isAssignableFrom(clazz)) {
                    final Map<Class, ModelLoaderFactory> map2 = this.modelClassToResourceFactories.get(clazz3);
                    modelLoaderFactory3 = modelLoaderFactory;
                    if (map2 != null) {
                        final ModelLoaderFactory<T, Y> modelLoaderFactory4 = map2.get(clazz2);
                        if ((modelLoaderFactory3 = modelLoaderFactory4) != null) {
                            return modelLoaderFactory4;
                        }
                    }
                }
                modelLoaderFactory = modelLoaderFactory3;
            }
        }
        return modelLoaderFactory2;
    }
    
    public <T, Y> ModelLoader<T, Y> buildModelLoader(final Class<T> clazz, final Class<Y> clazz2) {
        synchronized (this) {
            final ModelLoader<T, Object> cachedLoader = this.getCachedLoader((Class<T>)clazz, (Class<Object>)clazz2);
            if (cachedLoader == null) {
                final ModelLoaderFactory<T, Y> factory = this.getFactory((Class<T>)clazz, clazz2);
                ModelLoader<T, Y> modelLoader;
                if (factory != null) {
                    final ModelLoader<T, Y> build = factory.build(this.context, this);
                    this.cacheModelLoader((Class<T>)clazz, clazz2, (ModelLoader<T, Y>)build);
                    modelLoader = (ModelLoader<T, Y>)build;
                }
                else {
                    this.cacheNullLoader((Class<T>)clazz, clazz2);
                    modelLoader = (ModelLoader<T, Y>)cachedLoader;
                }
                return modelLoader;
            }
            if (GenericLoaderFactory.NULL_MODEL_LOADER.equals(cachedLoader)) {
                return null;
            }
            return (ModelLoader<T, Y>)cachedLoader;
        }
    }
    
    @Deprecated
    public <T, Y> ModelLoader<T, Y> buildModelLoader(final Class<T> clazz, final Class<Y> clazz2, final Context context) {
        synchronized (this) {
            return this.buildModelLoader(clazz, clazz2);
        }
    }
    
    public <T, Y> ModelLoaderFactory<T, Y> register(final Class<T> clazz, final Class<Y> clazz2, final ModelLoaderFactory<T, Y> modelLoaderFactory) {
        synchronized (this) {
            this.cachedModelLoaders.clear();
            Object o;
            if ((o = this.modelClassToResourceFactories.get(clazz)) == null) {
                o = new HashMap<Class<Y>, ModelLoaderFactory<T, Y>>();
                this.modelClassToResourceFactories.put(clazz, (Map<Class, ModelLoaderFactory>)o);
            }
            final ModelLoaderFactory<T, Y> modelLoaderFactory2 = ((Map<Class<Y>, ModelLoaderFactory<T, Y>>)o).put(clazz2, modelLoaderFactory);
            ModelLoaderFactory<T, Y> modelLoaderFactory3;
            if ((modelLoaderFactory3 = modelLoaderFactory2) != null) {
                final Iterator<Map<Class, ModelLoaderFactory>> iterator = this.modelClassToResourceFactories.values().iterator();
                do {
                    modelLoaderFactory3 = modelLoaderFactory2;
                    if (iterator.hasNext()) {
                        continue;
                    }
                    return modelLoaderFactory3;
                } while (!iterator.next().containsValue(modelLoaderFactory2));
                modelLoaderFactory3 = null;
            }
            return modelLoaderFactory3;
        }
    }
    
    public <T, Y> ModelLoaderFactory<T, Y> unregister(final Class<T> clazz, final Class<Y> clazz2) {
        synchronized (this) {
            this.cachedModelLoaders.clear();
            final ModelLoaderFactory<T, Y> modelLoaderFactory = null;
            final Map<Class, ModelLoaderFactory> map = this.modelClassToResourceFactories.get(clazz);
            ModelLoaderFactory<T, Y> modelLoaderFactory2 = modelLoaderFactory;
            if (map != null) {
                modelLoaderFactory2 = map.remove(clazz2);
            }
            return modelLoaderFactory2;
        }
    }
}
