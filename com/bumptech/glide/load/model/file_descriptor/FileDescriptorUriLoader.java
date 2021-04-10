package com.bumptech.glide.load.model.file_descriptor;

import android.os.*;
import android.net.*;
import android.content.*;
import com.bumptech.glide.*;
import com.bumptech.glide.load.data.*;
import com.bumptech.glide.load.model.*;

public class FileDescriptorUriLoader extends UriLoader<ParcelFileDescriptor> implements FileDescriptorModelLoader<Uri>
{
    public FileDescriptorUriLoader(final Context context) {
        this(context, Glide.buildFileDescriptorModelLoader(GlideUrl.class, context));
    }
    
    public FileDescriptorUriLoader(final Context context, final ModelLoader<GlideUrl, ParcelFileDescriptor> modelLoader) {
        super(context, modelLoader);
    }
    
    @Override
    protected DataFetcher<ParcelFileDescriptor> getAssetPathFetcher(final Context context, final String s) {
        return new FileDescriptorAssetPathFetcher(context.getApplicationContext().getAssets(), s);
    }
    
    @Override
    protected DataFetcher<ParcelFileDescriptor> getLocalUriFetcher(final Context context, final Uri uri) {
        return new FileDescriptorLocalUriFetcher(context, uri);
    }
    
    public static class Factory implements ModelLoaderFactory<Uri, ParcelFileDescriptor>
    {
        @Override
        public ModelLoader<Uri, ParcelFileDescriptor> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new FileDescriptorUriLoader(context, genericLoaderFactory.buildModelLoader(GlideUrl.class, ParcelFileDescriptor.class));
        }
        
        @Override
        public void teardown() {
        }
    }
}
