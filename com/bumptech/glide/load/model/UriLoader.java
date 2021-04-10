package com.bumptech.glide.load.model;

import android.net.*;
import android.content.*;
import com.bumptech.glide.load.data.*;

public abstract class UriLoader<T> implements ModelLoader<Uri, T>
{
    private final Context context;
    private final ModelLoader<GlideUrl, T> urlLoader;
    
    public UriLoader(final Context context, final ModelLoader<GlideUrl, T> urlLoader) {
        this.context = context;
        this.urlLoader = urlLoader;
    }
    
    private static boolean isLocalUri(final String s) {
        return "file".equals(s) || "content".equals(s) || "android.resource".equals(s);
    }
    
    protected abstract DataFetcher<T> getAssetPathFetcher(final Context p0, final String p1);
    
    protected abstract DataFetcher<T> getLocalUriFetcher(final Context p0, final Uri p1);
    
    @Override
    public final DataFetcher<T> getResourceFetcher(final Uri uri, final int n, final int n2) {
        final String scheme = uri.getScheme();
        final DataFetcher<T> dataFetcher = null;
        if (!isLocalUri(scheme)) {
            DataFetcher<T> resourceFetcher = dataFetcher;
            if (this.urlLoader != null) {
                if (!"http".equals(scheme)) {
                    resourceFetcher = dataFetcher;
                    if (!"https".equals(scheme)) {
                        return resourceFetcher;
                    }
                }
                resourceFetcher = this.urlLoader.getResourceFetcher(new GlideUrl(uri.toString()), n, n2);
            }
            return resourceFetcher;
        }
        if (AssetUriParser.isAssetUri(uri)) {
            return this.getAssetPathFetcher(this.context, AssetUriParser.toAssetPath(uri));
        }
        return this.getLocalUriFetcher(this.context, uri);
    }
}
