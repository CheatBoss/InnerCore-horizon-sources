package com.bumptech.glide.load.model.file_descriptor;

import android.os.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class FileDescriptorResourceLoader extends ResourceLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<Integer>
{
    public FileDescriptorResourceLoader(final Context context) {
        this(context, Glide.buildFileDescriptorModelLoader(Uri.class, context));
    }
    
    public FileDescriptorResourceLoader(final Context context, final ModelLoader<Uri, ParcelFileDescriptor> modelLoader) {
        super(context, modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<Integer, ParcelFileDescriptor>
    {
        @Override
        public ModelLoader<Integer, ParcelFileDescriptor> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new FileDescriptorResourceLoader(context, genericLoaderFactory.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
