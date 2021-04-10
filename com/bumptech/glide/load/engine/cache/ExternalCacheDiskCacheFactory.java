package com.bumptech.glide.load.engine.cache;

import android.content.*;
import java.io.*;

public final class ExternalCacheDiskCacheFactory extends DiskLruCacheFactory
{
    public ExternalCacheDiskCacheFactory(final Context context) {
        this(context, "image_manager_disk_cache", 262144000);
    }
    
    public ExternalCacheDiskCacheFactory(final Context context, final int n) {
        this(context, "image_manager_disk_cache", n);
    }
    
    public ExternalCacheDiskCacheFactory(final Context context, final String s, final int n) {
        super((CacheDirectoryGetter)new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                final File externalCacheDir = context.getExternalCacheDir();
                if (externalCacheDir == null) {
                    return null;
                }
                if (s != null) {
                    return new File(externalCacheDir, s);
                }
                return externalCacheDir;
            }
        }, n);
    }
}
