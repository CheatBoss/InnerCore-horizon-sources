package com.bumptech.glide.load.data;

import android.os.*;
import android.net.*;
import android.content.*;
import java.io.*;

public class FileDescriptorLocalUriFetcher extends LocalUriFetcher<ParcelFileDescriptor>
{
    public FileDescriptorLocalUriFetcher(final Context context, final Uri uri) {
        super(context, uri);
    }
    
    @Override
    protected void close(final ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        parcelFileDescriptor.close();
    }
    
    @Override
    protected ParcelFileDescriptor loadResource(final Uri uri, final ContentResolver contentResolver) throws FileNotFoundException {
        return contentResolver.openAssetFileDescriptor(uri, "r").getParcelFileDescriptor();
    }
}
