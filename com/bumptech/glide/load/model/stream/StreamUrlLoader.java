package com.bumptech.glide.load.model.stream;

import java.io.*;
import java.net.*;
import android.content.*;
import com.bumptech.glide.load.model.*;

public class StreamUrlLoader extends UrlLoader<InputStream>
{
    public StreamUrlLoader(final ModelLoader<GlideUrl, InputStream> modelLoader) {
        super(modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<URL, InputStream>
    {
        @Override
        public ModelLoader<URL, InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new StreamUrlLoader(genericLoaderFactory.buildModelLoader(GlideUrl.class, InputStream.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
