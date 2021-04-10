package com.bumptech.glide.load.model.stream;

import java.io.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class StreamStringLoader extends StringLoader<InputStream> implements StreamModelLoader<String>
{
    public StreamStringLoader(final Context context) {
        this(Glide.buildStreamModelLoader(Uri.class, context));
    }
    
    public StreamStringLoader(final ModelLoader<Uri, InputStream> modelLoader) {
        super(modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<String, InputStream>
    {
        @Override
        public ModelLoader<String, InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new StreamStringLoader(genericLoaderFactory.buildModelLoader(Uri.class, InputStream.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
