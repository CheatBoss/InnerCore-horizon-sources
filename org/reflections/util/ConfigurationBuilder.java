package org.reflections.util;

import com.google.common.base.*;
import javax.annotation.*;
import java.net.*;
import org.reflections.scanners.*;
import org.reflections.*;
import java.util.*;
import com.google.common.collect.*;
import org.reflections.adapters.*;
import org.reflections.serializers.*;
import java.util.concurrent.*;

public class ConfigurationBuilder implements Configuration
{
    @Nullable
    private ClassLoader[] classLoaders;
    @Nullable
    private ExecutorService executorService;
    @Nullable
    private Predicate<String> inputsFilter;
    protected MetadataAdapter metadataAdapter;
    @Nonnull
    private Set<Scanner> scanners;
    private Serializer serializer;
    @Nonnull
    private Set<URL> urls;
    
    public ConfigurationBuilder() {
        this.scanners = (Set<Scanner>)Sets.newHashSet((Object[])new Scanner[] { new TypeAnnotationsScanner(), new SubTypesScanner() });
        this.urls = (Set<URL>)Sets.newHashSet();
    }
    
    public static ConfigurationBuilder build(@Nullable Object... array) {
        final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        final ArrayList arrayList = Lists.newArrayList();
        if (array != null) {
            for (int length = array.length, i = 0; i < length; ++i) {
                final Object o = array[i];
                if (o != null) {
                    if (((Iterable<Iterable>)o).getClass().isArray()) {
                        final Object[] array2 = (Object[])o;
                        for (int length2 = array2.length, j = 0; j < length2; ++j) {
                            final Object o2 = array2[j];
                            if (o2 != null) {
                                arrayList.add(o2);
                            }
                        }
                    }
                    else if (o instanceof Iterable) {
                        for (final Iterable next : (Iterable<Iterable>)o) {
                            if (next != null) {
                                arrayList.add(next);
                            }
                        }
                    }
                    else {
                        arrayList.add(o);
                    }
                }
            }
        }
        final ArrayList arrayList2 = Lists.newArrayList();
        for (final ClassLoader next2 : arrayList) {
            if (next2 instanceof ClassLoader) {
                arrayList2.add(next2);
            }
        }
        if (arrayList2.isEmpty()) {
            array = null;
        }
        else {
            array = arrayList2.toArray(new ClassLoader[arrayList2.size()]);
        }
        final FilterBuilder filterBuilder = new FilterBuilder();
        final ArrayList arrayList3 = Lists.newArrayList();
        for (final String next3 : arrayList) {
            if (next3 instanceof String) {
                configurationBuilder.addUrls(ClasspathHelper.forPackage(next3, (ClassLoader[])array));
                filterBuilder.includePackage(next3);
            }
            else if (next3 instanceof Class) {
                if (Scanner.class.isAssignableFrom((Class<?>)next3)) {
                    try {
                        configurationBuilder.addScanners((Scanner)((Class<?>)next3).newInstance());
                    }
                    catch (Exception ex) {}
                }
                configurationBuilder.addUrls(ClasspathHelper.forClass((Class<?>)next3, (ClassLoader[])array));
                filterBuilder.includePackage((Class<?>)next3);
            }
            else if (next3 instanceof Scanner) {
                arrayList3.add(next3);
            }
            else if (next3 instanceof URL) {
                configurationBuilder.addUrls((URL)next3);
            }
            else {
                if (next3 instanceof ClassLoader) {
                    continue;
                }
                if (next3 instanceof Predicate) {
                    filterBuilder.add((Predicate<String>)next3);
                }
                else if (next3 instanceof ExecutorService) {
                    configurationBuilder.setExecutorService((ExecutorService)next3);
                }
                else {
                    if (Reflections.log != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("could not use param ");
                        sb.append((Object)next3);
                        throw new ReflectionsException(sb.toString());
                    }
                    continue;
                }
            }
        }
        if (configurationBuilder.getUrls().isEmpty()) {
            if (array != null) {
                configurationBuilder.addUrls(ClasspathHelper.forClassLoader((ClassLoader[])array));
            }
            else {
                configurationBuilder.addUrls(ClasspathHelper.forClassLoader());
            }
        }
        configurationBuilder.filterInputsBy((Predicate<String>)filterBuilder);
        if (!arrayList3.isEmpty()) {
            configurationBuilder.setScanners((Scanner[])arrayList3.toArray(new Scanner[arrayList3.size()]));
        }
        if (!arrayList2.isEmpty()) {
            configurationBuilder.addClassLoaders(arrayList2);
        }
        return configurationBuilder;
    }
    
    public ConfigurationBuilder addClassLoader(final ClassLoader classLoader) {
        return this.addClassLoaders(classLoader);
    }
    
    public ConfigurationBuilder addClassLoaders(final Collection<ClassLoader> collection) {
        return this.addClassLoaders((ClassLoader[])collection.toArray(new ClassLoader[collection.size()]));
    }
    
    public ConfigurationBuilder addClassLoaders(ClassLoader... classLoaders) {
        if (this.classLoaders != null) {
            classLoaders = (ClassLoader[])ObjectArrays.concat((Object[])this.classLoaders, (Object[])classLoaders, (Class)ClassLoader.class);
        }
        this.classLoaders = classLoaders;
        return this;
    }
    
    public ConfigurationBuilder addScanners(final Scanner... array) {
        this.scanners.addAll(Sets.newHashSet((Object[])array));
        return this;
    }
    
    public ConfigurationBuilder addUrls(final Collection<URL> collection) {
        this.urls.addAll(collection);
        return this;
    }
    
    public ConfigurationBuilder addUrls(final URL... array) {
        this.urls.addAll(Sets.newHashSet((Object[])array));
        return this;
    }
    
    public ConfigurationBuilder filterInputsBy(final Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
        return this;
    }
    
    public ConfigurationBuilder forPackages(final String... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            this.addUrls(ClasspathHelper.forPackage(array[i], new ClassLoader[0]));
        }
        return this;
    }
    
    @Nullable
    @Override
    public ClassLoader[] getClassLoaders() {
        return this.classLoaders;
    }
    
    @Nullable
    @Override
    public ExecutorService getExecutorService() {
        return this.executorService;
    }
    
    @Nullable
    @Override
    public Predicate<String> getInputsFilter() {
        return this.inputsFilter;
    }
    
    @Override
    public MetadataAdapter getMetadataAdapter() {
        if (this.metadataAdapter != null) {
            return this.metadataAdapter;
        }
        try {
            return this.metadataAdapter = new JavassistAdapter();
        }
        catch (Throwable t) {
            if (Reflections.log != null) {
                Reflections.log.warn("could not create JavassistAdapter, using JavaReflectionAdapter", t);
            }
            return this.metadataAdapter = new JavaReflectionAdapter();
        }
    }
    
    @Nonnull
    @Override
    public Set<Scanner> getScanners() {
        return this.scanners;
    }
    
    @Override
    public Serializer getSerializer() {
        if (this.serializer != null) {
            return this.serializer;
        }
        return this.serializer = new XmlSerializer();
    }
    
    @Nonnull
    @Override
    public Set<URL> getUrls() {
        return this.urls;
    }
    
    public void setClassLoaders(@Nullable final ClassLoader[] classLoaders) {
        this.classLoaders = classLoaders;
    }
    
    public ConfigurationBuilder setExecutorService(@Nullable final ExecutorService executorService) {
        this.executorService = executorService;
        return this;
    }
    
    public void setInputsFilter(@Nullable final Predicate<String> inputsFilter) {
        this.inputsFilter = inputsFilter;
    }
    
    public ConfigurationBuilder setMetadataAdapter(final MetadataAdapter metadataAdapter) {
        this.metadataAdapter = metadataAdapter;
        return this;
    }
    
    public ConfigurationBuilder setScanners(@Nonnull final Scanner... array) {
        this.scanners.clear();
        return this.addScanners(array);
    }
    
    public ConfigurationBuilder setSerializer(final Serializer serializer) {
        this.serializer = serializer;
        return this;
    }
    
    public ConfigurationBuilder setUrls(@Nonnull final Collection<URL> collection) {
        this.urls = (Set<URL>)Sets.newHashSet((Iterable)collection);
        return this;
    }
    
    public ConfigurationBuilder setUrls(final URL... array) {
        this.urls = (Set<URL>)Sets.newHashSet((Object[])array);
        return this;
    }
    
    public ConfigurationBuilder useParallelExecutor() {
        return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
    }
    
    public ConfigurationBuilder useParallelExecutor(final int n) {
        this.setExecutorService(Executors.newFixedThreadPool(n));
        return this;
    }
}
