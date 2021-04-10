package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.data.*;
import com.bumptech.glide.provider.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.resource.transcode.*;
import com.bumptech.glide.util.*;
import com.bumptech.glide.load.engine.cache.*;
import android.util.*;
import com.bumptech.glide.load.*;
import java.io.*;

class DecodeJob<A, T, Z>
{
    private static final FileOpener DEFAULT_FILE_OPENER;
    private static final String TAG = "DecodeJob";
    private final DiskCacheProvider diskCacheProvider;
    private final DiskCacheStrategy diskCacheStrategy;
    private final DataFetcher<A> fetcher;
    private final FileOpener fileOpener;
    private final int height;
    private volatile boolean isCancelled;
    private final DataLoadProvider<A, T> loadProvider;
    private final Priority priority;
    private final EngineKey resultKey;
    private final ResourceTranscoder<T, Z> transcoder;
    private final Transformation<T> transformation;
    private final int width;
    
    static {
        DEFAULT_FILE_OPENER = new FileOpener();
    }
    
    public DecodeJob(final EngineKey engineKey, final int n, final int n2, final DataFetcher<A> dataFetcher, final DataLoadProvider<A, T> dataLoadProvider, final Transformation<T> transformation, final ResourceTranscoder<T, Z> resourceTranscoder, final DiskCacheProvider diskCacheProvider, final DiskCacheStrategy diskCacheStrategy, final Priority priority) {
        this(engineKey, n, n2, dataFetcher, dataLoadProvider, transformation, resourceTranscoder, diskCacheProvider, diskCacheStrategy, priority, DecodeJob.DEFAULT_FILE_OPENER);
    }
    
    DecodeJob(final EngineKey resultKey, final int width, final int height, final DataFetcher<A> fetcher, final DataLoadProvider<A, T> loadProvider, final Transformation<T> transformation, final ResourceTranscoder<T, Z> transcoder, final DiskCacheProvider diskCacheProvider, final DiskCacheStrategy diskCacheStrategy, final Priority priority, final FileOpener fileOpener) {
        this.resultKey = resultKey;
        this.width = width;
        this.height = height;
        this.fetcher = fetcher;
        this.loadProvider = loadProvider;
        this.transformation = transformation;
        this.transcoder = transcoder;
        this.diskCacheProvider = diskCacheProvider;
        this.diskCacheStrategy = diskCacheStrategy;
        this.priority = priority;
        this.fileOpener = fileOpener;
    }
    
    private Resource<T> cacheAndDecodeSourceData(final A a) throws IOException {
        final long logTime = LogTime.getLogTime();
        this.diskCacheProvider.getDiskCache().put(this.resultKey.getOriginalKey(), (DiskCache.Writer)new SourceWriter((Encoder<Object>)this.loadProvider.getSourceEncoder(), a));
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Wrote source to cache", logTime);
        }
        final long logTime2 = LogTime.getLogTime();
        final Resource<T> loadFromCache = this.loadFromCache(this.resultKey.getOriginalKey());
        if (Log.isLoggable("DecodeJob", 2) && loadFromCache != null) {
            this.logWithTimeAndKey("Decoded source from cache", logTime2);
        }
        return loadFromCache;
    }
    
    private Resource<T> decodeFromSourceData(final A a) throws IOException {
        if (this.diskCacheStrategy.cacheSource()) {
            return this.cacheAndDecodeSourceData(a);
        }
        final long logTime = LogTime.getLogTime();
        final Resource<T> decode = this.loadProvider.getSourceDecoder().decode(a, this.width, this.height);
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded from source", logTime);
        }
        return decode;
    }
    
    private Resource<T> decodeSource() throws Exception {
        try {
            final long logTime = LogTime.getLogTime();
            final A loadData = this.fetcher.loadData(this.priority);
            if (Log.isLoggable("DecodeJob", 2)) {
                this.logWithTimeAndKey("Fetched data", logTime);
            }
            if (this.isCancelled) {
                return null;
            }
            return this.decodeFromSourceData(loadData);
        }
        finally {
            this.fetcher.cleanup();
        }
    }
    
    private Resource<T> loadFromCache(final Key key) throws IOException {
        final File value = this.diskCacheProvider.getDiskCache().get(key);
        if (value == null) {
            return null;
        }
        try {
            final Resource<T> decode = this.loadProvider.getCacheDecoder().decode(value, this.width, this.height);
            if (decode == null) {
                this.diskCacheProvider.getDiskCache().delete(key);
            }
            return decode;
        }
        finally {
            if (!false) {
                this.diskCacheProvider.getDiskCache().delete(key);
            }
        }
    }
    
    private void logWithTimeAndKey(final String s, final long n) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" in ");
        sb.append(LogTime.getElapsedMillis(n));
        sb.append(", key: ");
        sb.append(this.resultKey);
        Log.v("DecodeJob", sb.toString());
    }
    
    private Resource<Z> transcode(final Resource<T> resource) {
        if (resource == null) {
            return null;
        }
        return this.transcoder.transcode(resource);
    }
    
    private Resource<T> transform(final Resource<T> resource) {
        if (resource == null) {
            return null;
        }
        final Resource<T> transform = this.transformation.transform(resource, this.width, this.height);
        if (!resource.equals(transform)) {
            resource.recycle();
        }
        return transform;
    }
    
    private Resource<Z> transformEncodeAndTranscode(final Resource<T> resource) {
        final long logTime = LogTime.getLogTime();
        final Resource<T> transform = this.transform(resource);
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Transformed resource from source", logTime);
        }
        this.writeTransformedToCache(transform);
        final long logTime2 = LogTime.getLogTime();
        final Resource<Z> transcode = this.transcode(transform);
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Transcoded transformed from source", logTime2);
        }
        return transcode;
    }
    
    private void writeTransformedToCache(final Resource<T> resource) {
        if (resource == null) {
            return;
        }
        if (!this.diskCacheStrategy.cacheResult()) {
            return;
        }
        final long logTime = LogTime.getLogTime();
        this.diskCacheProvider.getDiskCache().put(this.resultKey, (DiskCache.Writer)new SourceWriter((Encoder<Object>)this.loadProvider.getEncoder(), resource));
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Wrote transformed from source to cache", logTime);
        }
    }
    
    public void cancel() {
        this.isCancelled = true;
        this.fetcher.cancel();
    }
    
    public Resource<Z> decodeFromSource() throws Exception {
        return this.transformEncodeAndTranscode(this.decodeSource());
    }
    
    public Resource<Z> decodeResultFromCache() throws Exception {
        if (!this.diskCacheStrategy.cacheResult()) {
            return null;
        }
        final long logTime = LogTime.getLogTime();
        final Resource<T> loadFromCache = this.loadFromCache(this.resultKey);
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded transformed from cache", logTime);
        }
        final long logTime2 = LogTime.getLogTime();
        final Resource<Z> transcode = this.transcode(loadFromCache);
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Transcoded transformed from cache", logTime2);
        }
        return transcode;
    }
    
    public Resource<Z> decodeSourceFromCache() throws Exception {
        if (!this.diskCacheStrategy.cacheSource()) {
            return null;
        }
        final long logTime = LogTime.getLogTime();
        final Resource<T> loadFromCache = this.loadFromCache(this.resultKey.getOriginalKey());
        if (Log.isLoggable("DecodeJob", 2)) {
            this.logWithTimeAndKey("Decoded source from cache", logTime);
        }
        return this.transformEncodeAndTranscode(loadFromCache);
    }
    
    interface DiskCacheProvider
    {
        DiskCache getDiskCache();
    }
    
    static class FileOpener
    {
        public OutputStream open(final File file) throws FileNotFoundException {
            return new BufferedOutputStream(new FileOutputStream(file));
        }
    }
    
    class SourceWriter<DataType> implements Writer
    {
        private final DataType data;
        private final Encoder<DataType> encoder;
        
        public SourceWriter(final Encoder<DataType> encoder, final DataType data) {
            this.encoder = encoder;
            this.data = data;
        }
        
        @Override
        public boolean write(final File file) {
            final boolean b = false;
            boolean b2 = false;
            OutputStream outputStream = null;
            OutputStream open = null;
            while (true) {
                try {
                    try {
                        final OutputStream outputStream2 = outputStream = (open = DecodeJob.this.fileOpener.open(file));
                        final boolean encode;
                        b2 = (encode = this.encoder.encode(this.data, outputStream2));
                        if (outputStream2 == null) {
                            return encode;
                        }
                        final boolean b3 = b2;
                        try {
                            outputStream2.close();
                            return b2;
                        }
                        catch (IOException ex2) {
                            return b3;
                        }
                    }
                    finally {
                        if (open != null) {
                            final OutputStream outputStream3 = open;
                            outputStream3.close();
                        }
                        final FileNotFoundException ex;
                        Log.d("DecodeJob", "Failed to find file to write to disk cache", (Throwable)ex);
                        final boolean encode = b;
                        // iftrue(Label_0111:, outputStream == null)
                        Block_12: {
                            break Block_12;
                            Label_0111: {
                                return encode;
                            }
                        }
                        outputStream.close();
                        return false;
                    }
                }
                catch (FileNotFoundException ex3) {}
                try {
                    final OutputStream outputStream3 = open;
                    outputStream3.close();
                    continue;
                }
                catch (IOException ex4) {}
                break;
            }
        }
    }
}
