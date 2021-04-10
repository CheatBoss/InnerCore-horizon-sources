package com.bumptech.glide.load.model.stream;

import java.io.*;
import com.bumptech.glide.load.data.*;
import android.content.*;
import com.bumptech.glide.load.model.*;

public class HttpUrlGlideUrlLoader implements ModelLoader<GlideUrl, InputStream>
{
    private final ModelCache<GlideUrl, GlideUrl> modelCache;
    
    public HttpUrlGlideUrlLoader() {
        this(null);
    }
    
    public HttpUrlGlideUrlLoader(final ModelCache<GlideUrl, GlideUrl> modelCache) {
        this.modelCache = modelCache;
    }
    
    @Override
    public DataFetcher<InputStream> getResourceFetcher(final GlideUrl glideUrl, final int n, final int n2) {
        GlideUrl glideUrl2 = glideUrl;
        if (this.modelCache != null && (glideUrl2 = this.modelCache.get(glideUrl, 0, 0)) == null) {
            this.modelCache.put(glideUrl, 0, 0, glideUrl);
            glideUrl2 = glideUrl;
        }
        return new HttpUrlFetcher(glideUrl2);
    }
    
    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream>
    {
        private final ModelCache<GlideUrl, GlideUrl> modelCache;
        
        public Factory() {
            this.modelCache = new ModelCache<GlideUrl, GlideUrl>(500);
        }
        
        @Override
        public ModelLoader<GlideUrl, InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new HttpUrlGlideUrlLoader(this.modelCache);
        }
        
        @Override
        public void teardown() {
        }
    }
}
