package com.bumptech.glide.load.model.stream;

import java.io.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class StreamFileLoader extends FileLoader<InputStream> implements StreamModelLoader<File>
{
    public StreamFileLoader(final Context context) {
        this(Glide.buildStreamModelLoader(Uri.class, context));
    }
    
    public StreamFileLoader(final ModelLoader<Uri, InputStream> modelLoader) {
        super(modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<File, InputStream>
    {
        @Override
        public ModelLoader<File, InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new StreamFileLoader(genericLoaderFactory.buildModelLoader(Uri.class, InputStream.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
