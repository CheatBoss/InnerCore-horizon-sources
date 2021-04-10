package com.bumptech.glide.load.model.file_descriptor;

import android.os.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class FileDescriptorStringLoader extends StringLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<String>
{
    public FileDescriptorStringLoader(final Context context) {
        this(Glide.buildFileDescriptorModelLoader(Uri.class, context));
    }
    
    public FileDescriptorStringLoader(final ModelLoader<Uri, ParcelFileDescriptor> modelLoader) {
        super(modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<String, ParcelFileDescriptor>
    {
        @Override
        public ModelLoader<String, ParcelFileDescriptor> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new FileDescriptorStringLoader(genericLoaderFactory.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
