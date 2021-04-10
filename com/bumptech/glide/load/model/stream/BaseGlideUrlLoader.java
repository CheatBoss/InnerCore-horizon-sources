package com.bumptech.glide.load.model.stream;

import java.io.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;
import com.bumptech.glide.load.data.*;
import android.text.*;

public abstract class BaseGlideUrlLoader<T> implements StreamModelLoader<T>
{
    private final ModelLoader<GlideUrl, InputStream> concreteLoader;
    private final ModelCache<T, GlideUrl> modelCache;
    
    public BaseGlideUrlLoader(final Context context) {
        this(context, null);
    }
    
    public BaseGlideUrlLoader(final Context context, final ModelCache<T, GlideUrl> modelCache) {
        this(Glide.buildModelLoader(GlideUrl.class, InputStream.class, context), modelCache);
    }
    
    public BaseGlideUrlLoader(final ModelLoader<GlideUrl, InputStream> modelLoader) {
        this(modelLoader, null);
    }
    
    public BaseGlideUrlLoader(final ModelLoader<GlideUrl, InputStream> concreteLoader, final ModelCache<T, GlideUrl> modelCache) {
        this.concreteLoader = concreteLoader;
        this.modelCache = modelCache;
    }
    
    protected Headers getHeaders(final T t, final int n, final int n2) {
        return Headers.NONE;
    }
    
    @Override
    public DataFetcher<InputStream> getResourceFetcher(final T t, final int n, final int n2) {
        GlideUrl glideUrl = null;
        if (this.modelCache != null) {
            glideUrl = this.modelCache.get(t, n, n2);
        }
        GlideUrl glideUrl2;
        if ((glideUrl2 = glideUrl) == null) {
            final String url = this.getUrl(t, n, n2);
            if (TextUtils.isEmpty((CharSequence)url)) {
                return null;
            }
            final GlideUrl glideUrl3 = glideUrl2 = new GlideUrl(url, this.getHeaders(t, n, n2));
            if (this.modelCache != null) {
                this.modelCache.put(t, n, n2, glideUrl3);
                glideUrl2 = glideUrl3;
            }
        }
        return this.concreteLoader.getResourceFetcher(glideUrl2, n, n2);
    }
    
    protected abstract String getUrl(final T p0, final int p1, final int p2);
}
