package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.model.*;
import android.net.*;
import java.io.*;
import android.content.*;
import com.bumptech.glide.load.data.*;

public class MediaStoreStreamLoader implements ModelLoader<Uri, InputStream>
{
    private final Context context;
    private final ModelLoader<Uri, InputStream> uriLoader;
    
    public MediaStoreStreamLoader(final Context context, final ModelLoader<Uri, InputStream> uriLoader) {
        this.context = context;
        this.uriLoader = uriLoader;
    }
    
    @Override
    public DataFetcher<InputStream> getResourceFetcher(final Uri uri, final int n, final int n2) {
        return new MediaStoreThumbFetcher(this.context, uri, this.uriLoader.getResourceFetcher(uri, n, n2), n, n2);
    }
}
