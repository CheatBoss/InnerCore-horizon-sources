package com.bumptech.glide;

import com.bumptech.glide.load.engine.bitmap_recycle.*;
import com.bumptech.glide.load.engine.*;
import com.bumptech.glide.load.engine.cache.*;
import android.content.*;
import android.graphics.*;
import com.bumptech.glide.provider.*;
import android.os.*;
import com.bumptech.glide.load.resource.gif.*;
import com.bumptech.glide.load.resource.gifbitmap.*;
import java.io.*;
import com.bumptech.glide.load.resource.file.*;
import android.net.*;
import com.bumptech.glide.load.model.file_descriptor.*;
import java.net.*;
import com.bumptech.glide.load.model.stream.*;
import com.bumptech.glide.load.resource.bitmap.*;
import com.bumptech.glide.load.resource.drawable.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.load.*;
import com.bumptech.glide.load.model.*;
import android.util.*;
import android.view.*;
import com.bumptech.glide.util.*;
import com.bumptech.glide.request.*;
import com.bumptech.glide.module.*;
import java.util.*;
import com.bumptech.glide.manager.*;
import android.app.*;
import android.annotation.*;
import android.support.v4.app.*;
import android.widget.*;
import com.bumptech.glide.load.engine.prefill.*;
import com.bumptech.glide.request.target.*;
import android.graphics.drawable.*;
import com.bumptech.glide.request.animation.*;

public class Glide
{
    private static final String TAG = "Glide";
    private static volatile Glide glide;
    private final CenterCrop bitmapCenterCrop;
    private final FitCenter bitmapFitCenter;
    private final BitmapPool bitmapPool;
    private final BitmapPreFiller bitmapPreFiller;
    private final DataLoadProviderRegistry dataLoadProviderRegistry;
    private final DecodeFormat decodeFormat;
    private final GifBitmapWrapperTransformation drawableCenterCrop;
    private final GifBitmapWrapperTransformation drawableFitCenter;
    private final Engine engine;
    private final ImageViewTargetFactory imageViewTargetFactory;
    private final GenericLoaderFactory loaderFactory;
    private final Handler mainHandler;
    private final MemoryCache memoryCache;
    private final TranscoderRegistry transcoderRegistry;
    
    Glide(final Engine engine, final MemoryCache memoryCache, final BitmapPool bitmapPool, final Context context, final DecodeFormat decodeFormat) {
        this.imageViewTargetFactory = new ImageViewTargetFactory();
        this.transcoderRegistry = new TranscoderRegistry();
        this.engine = engine;
        this.bitmapPool = bitmapPool;
        this.memoryCache = memoryCache;
        this.decodeFormat = decodeFormat;
        this.loaderFactory = new GenericLoaderFactory(context);
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.bitmapPreFiller = new BitmapPreFiller(memoryCache, bitmapPool, decodeFormat);
        this.dataLoadProviderRegistry = new DataLoadProviderRegistry();
        final StreamBitmapDataLoadProvider streamBitmapDataLoadProvider = new StreamBitmapDataLoadProvider(bitmapPool, decodeFormat);
        this.dataLoadProviderRegistry.register(InputStream.class, Bitmap.class, streamBitmapDataLoadProvider);
        final FileDescriptorBitmapDataLoadProvider fileDescriptorBitmapDataLoadProvider = new FileDescriptorBitmapDataLoadProvider(bitmapPool, decodeFormat);
        this.dataLoadProviderRegistry.register(ParcelFileDescriptor.class, Bitmap.class, fileDescriptorBitmapDataLoadProvider);
        final ImageVideoDataLoadProvider imageVideoDataLoadProvider = new ImageVideoDataLoadProvider(streamBitmapDataLoadProvider, fileDescriptorBitmapDataLoadProvider);
        this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, Bitmap.class, imageVideoDataLoadProvider);
        final GifDrawableLoadProvider gifDrawableLoadProvider = new GifDrawableLoadProvider(context, bitmapPool);
        this.dataLoadProviderRegistry.register(InputStream.class, GifDrawable.class, gifDrawableLoadProvider);
        this.dataLoadProviderRegistry.register(ImageVideoWrapper.class, GifBitmapWrapper.class, new ImageVideoGifDrawableLoadProvider(imageVideoDataLoadProvider, gifDrawableLoadProvider, bitmapPool));
        this.dataLoadProviderRegistry.register(InputStream.class, File.class, new StreamFileDataLoadProvider());
        this.register(File.class, ParcelFileDescriptor.class, new FileDescriptorFileLoader.Factory());
        this.register(File.class, InputStream.class, new StreamFileLoader.Factory());
        this.register(Integer.TYPE, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
        this.register(Integer.TYPE, InputStream.class, new StreamResourceLoader.Factory());
        this.register(Integer.class, ParcelFileDescriptor.class, new FileDescriptorResourceLoader.Factory());
        this.register(Integer.class, InputStream.class, new StreamResourceLoader.Factory());
        this.register(String.class, ParcelFileDescriptor.class, new FileDescriptorStringLoader.Factory());
        this.register(String.class, InputStream.class, new StreamStringLoader.Factory());
        this.register(Uri.class, ParcelFileDescriptor.class, new FileDescriptorUriLoader.Factory());
        this.register(Uri.class, InputStream.class, new StreamUriLoader.Factory());
        this.register(URL.class, InputStream.class, new StreamUrlLoader.Factory());
        this.register(GlideUrl.class, InputStream.class, new HttpUrlGlideUrlLoader.Factory());
        this.register(byte[].class, InputStream.class, new StreamByteArrayLoader.Factory());
        this.transcoderRegistry.register(Bitmap.class, GlideBitmapDrawable.class, new GlideBitmapDrawableTranscoder(context.getResources(), bitmapPool));
        this.transcoderRegistry.register(GifBitmapWrapper.class, GlideDrawable.class, new GifBitmapWrapperDrawableTranscoder(new GlideBitmapDrawableTranscoder(context.getResources(), bitmapPool)));
        this.bitmapCenterCrop = new CenterCrop(bitmapPool);
        this.drawableCenterCrop = new GifBitmapWrapperTransformation(bitmapPool, this.bitmapCenterCrop);
        this.bitmapFitCenter = new FitCenter(bitmapPool);
        this.drawableFitCenter = new GifBitmapWrapperTransformation(bitmapPool, this.bitmapFitCenter);
    }
    
    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(final Class<T> clazz, final Context context) {
        return buildModelLoader(clazz, ParcelFileDescriptor.class, context);
    }
    
    public static <T> ModelLoader<T, ParcelFileDescriptor> buildFileDescriptorModelLoader(final T t, final Context context) {
        return buildModelLoader(t, ParcelFileDescriptor.class, context);
    }
    
    public static <T, Y> ModelLoader<T, Y> buildModelLoader(final Class<T> clazz, final Class<Y> clazz2, final Context context) {
        if (clazz == null) {
            if (Log.isLoggable("Glide", 3)) {
                Log.d("Glide", "Unable to load null model, setting placeholder only");
            }
            return null;
        }
        return get(context).getLoaderFactory().buildModelLoader(clazz, clazz2);
    }
    
    public static <T, Y> ModelLoader<T, Y> buildModelLoader(final T t, final Class<Y> clazz, final Context context) {
        Class<?> class1;
        if (t != null) {
            class1 = t.getClass();
        }
        else {
            class1 = null;
        }
        return buildModelLoader((Class<T>)class1, clazz, context);
    }
    
    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(final Class<T> clazz, final Context context) {
        return buildModelLoader(clazz, InputStream.class, context);
    }
    
    public static <T> ModelLoader<T, InputStream> buildStreamModelLoader(final T t, final Context context) {
        return buildModelLoader(t, InputStream.class, context);
    }
    
    public static void clear(final View view) {
        clear(new ClearTarget(view));
    }
    
    public static void clear(final FutureTarget<?> futureTarget) {
        futureTarget.clear();
    }
    
    public static void clear(final Target<?> target) {
        Util.assertMainThread();
        final Request request = target.getRequest();
        if (request != null) {
            request.clear();
            target.setRequest(null);
        }
    }
    
    public static Glide get(Context applicationContext) {
        if (Glide.glide == null) {
            synchronized (Glide.class) {
                if (Glide.glide == null) {
                    applicationContext = applicationContext.getApplicationContext();
                    final List<GlideModule> parse = new ManifestParser(applicationContext).parse();
                    final GlideBuilder glideBuilder = new GlideBuilder(applicationContext);
                    final Iterator<GlideModule> iterator = parse.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().applyOptions(applicationContext, glideBuilder);
                    }
                    Glide.glide = glideBuilder.createGlide();
                    final Iterator<GlideModule> iterator2 = parse.iterator();
                    while (iterator2.hasNext()) {
                        iterator2.next().registerComponents(applicationContext, Glide.glide);
                    }
                }
            }
        }
        return Glide.glide;
    }
    
    private GenericLoaderFactory getLoaderFactory() {
        return this.loaderFactory;
    }
    
    public static File getPhotoCacheDir(final Context context) {
        return getPhotoCacheDir(context, "image_manager_disk_cache");
    }
    
    public static File getPhotoCacheDir(final Context context, final String s) {
        final File cacheDir = context.getCacheDir();
        if (cacheDir == null) {
            if (Log.isLoggable("Glide", 6)) {
                Log.e("Glide", "default disk cache dir is null");
            }
            return null;
        }
        final File file = new File(cacheDir, s);
        if (!file.mkdirs() && (!file.exists() || !file.isDirectory())) {
            return null;
        }
        return file;
    }
    
    @Deprecated
    public static boolean isSetup() {
        return Glide.glide != null;
    }
    
    @Deprecated
    public static void setup(final GlideBuilder glideBuilder) {
        if (isSetup()) {
            throw new IllegalArgumentException("Glide is already setup, check with isSetup() first");
        }
        Glide.glide = glideBuilder.createGlide();
    }
    
    static void tearDown() {
        Glide.glide = null;
    }
    
    public static RequestManager with(final Activity activity) {
        return RequestManagerRetriever.get().get(activity);
    }
    
    @TargetApi(11)
    public static RequestManager with(final Fragment fragment) {
        return RequestManagerRetriever.get().get(fragment);
    }
    
    public static RequestManager with(final Context context) {
        return RequestManagerRetriever.get().get(context);
    }
    
    public static RequestManager with(final android.support.v4.app.Fragment fragment) {
        return RequestManagerRetriever.get().get(fragment);
    }
    
    public static RequestManager with(final FragmentActivity fragmentActivity) {
        return RequestManagerRetriever.get().get(fragmentActivity);
    }
    
     <T, Z> DataLoadProvider<T, Z> buildDataProvider(final Class<T> clazz, final Class<Z> clazz2) {
        return this.dataLoadProviderRegistry.get(clazz, clazz2);
    }
    
     <R> Target<R> buildImageViewTarget(final ImageView imageView, final Class<R> clazz) {
        return this.imageViewTargetFactory.buildTarget(imageView, clazz);
    }
    
     <Z, R> ResourceTranscoder<Z, R> buildTranscoder(final Class<Z> clazz, final Class<R> clazz2) {
        return this.transcoderRegistry.get(clazz, clazz2);
    }
    
    public void clearDiskCache() {
        Util.assertBackgroundThread();
        this.getEngine().clearDiskCache();
    }
    
    public void clearMemory() {
        this.bitmapPool.clearMemory();
        this.memoryCache.clearMemory();
    }
    
    CenterCrop getBitmapCenterCrop() {
        return this.bitmapCenterCrop;
    }
    
    FitCenter getBitmapFitCenter() {
        return this.bitmapFitCenter;
    }
    
    public BitmapPool getBitmapPool() {
        return this.bitmapPool;
    }
    
    DecodeFormat getDecodeFormat() {
        return this.decodeFormat;
    }
    
    GifBitmapWrapperTransformation getDrawableCenterCrop() {
        return this.drawableCenterCrop;
    }
    
    GifBitmapWrapperTransformation getDrawableFitCenter() {
        return this.drawableFitCenter;
    }
    
    Engine getEngine() {
        return this.engine;
    }
    
    Handler getMainHandler() {
        return this.mainHandler;
    }
    
    public void preFillBitmapPool(final PreFillType.Builder... array) {
        this.bitmapPreFiller.preFill(array);
    }
    
    public <T, Y> void register(final Class<T> clazz, final Class<Y> clazz2, final ModelLoaderFactory<T, Y> modelLoaderFactory) {
        final ModelLoaderFactory<T, Y> register = this.loaderFactory.register(clazz, clazz2, modelLoaderFactory);
        if (register != null) {
            register.teardown();
        }
    }
    
    public void setMemoryCategory(final MemoryCategory memoryCategory) {
        this.memoryCache.setSizeMultiplier(memoryCategory.getMultiplier());
        this.bitmapPool.setSizeMultiplier(memoryCategory.getMultiplier());
    }
    
    public void trimMemory(final int n) {
        this.bitmapPool.trimMemory(n);
        this.memoryCache.trimMemory(n);
    }
    
    @Deprecated
    public <T, Y> void unregister(final Class<T> clazz, final Class<Y> clazz2) {
        final ModelLoaderFactory<T, Y> unregister = this.loaderFactory.unregister(clazz, clazz2);
        if (unregister != null) {
            unregister.teardown();
        }
    }
    
    private static class ClearTarget extends ViewTarget<View, Object>
    {
        public ClearTarget(final View view) {
            super(view);
        }
        
        @Override
        public void onLoadCleared(final Drawable drawable) {
        }
        
        @Override
        public void onLoadFailed(final Exception ex, final Drawable drawable) {
        }
        
        @Override
        public void onLoadStarted(final Drawable drawable) {
        }
        
        @Override
        public void onResourceReady(final Object o, final GlideAnimation<? super Object> glideAnimation) {
        }
    }
}
