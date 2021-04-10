package com.bumptech.glide.load.model.file_descriptor;

import android.os.*;
import java.io.*;
import android.content.*;
import android.net.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.model.*;

public class FileDescriptorFileLoader extends FileLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<File>
{
    public FileDescriptorFileLoader(final Context context) {
        this(Glide.buildFileDescriptorModelLoader(Uri.class, context));
    }
    
    public FileDescriptorFileLoader(final ModelLoader<Uri, ParcelFileDescriptor> modelLoader) {
        super(modelLoader);
    }
    
    public static class Factory implements ModelLoaderFactory<File, ParcelFileDescriptor>
    {
        @Override
        public ModelLoader<File, ParcelFileDescriptor> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new FileDescriptorFileLoader(genericLoaderFactory.buildModelLoader(Uri.class, ParcelFileDescriptor.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
