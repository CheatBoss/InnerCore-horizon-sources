package com.bumptech.glide.load.model.stream;

import java.io.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class StreamResourceLoader extends ResourceLoader<InputStream> implements StreamModelLoader<Integer>
{
    public StreamResourceLoader(final Context context) {
        this(context, Glide.buildStreamModelLoader(Uri.class, context));
    }
    
    public StreamResourceLoader(final Context context, final ModelLoader<Uri, InputStream> modelLoader) {
        super(context, modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<Integer, InputStream>
    {
        @Override
        public ModelLoader<Integer, InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new StreamResourceLoader(context, genericLoaderFactory.buildModelLoader(Uri.class, InputStream.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
