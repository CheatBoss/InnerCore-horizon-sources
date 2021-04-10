package com.bumptech.glide.load.data;

import android.os.*;
import android.content.res.*;
import java.io.*;

public class FileDescriptorAssetPathFetcher extends AssetPathFetcher<ParcelFileDescriptor>
{
    public FileDescriptorAssetPathFetcher(final AssetManager assetManager, final String s) {
        super(assetManager, s);
    }
    
    @Override
    protected void close(final ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        parcelFileDescriptor.close();
    }
    
    @Override
    protected ParcelFileDescriptor loadResource(final AssetManager assetManager, final String s) throws IOException {
        return assetManager.openFd(s).getParcelFileDescriptor();
    }
}
