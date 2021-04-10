package com.bumptech.glide.load.data;

import android.content.res.*;
import java.io.*;

public class StreamAssetPathFetcher extends AssetPathFetcher<InputStream>
{
    public StreamAssetPathFetcher(final AssetManager assetManager, final String s) {
        super(assetManager, s);
    }
    
    @Override
    protected void close(final InputStream inputStream) throws IOException {
        inputStream.close();
    }
    
    @Override
    protected InputStream loadResource(final AssetManager assetManager, final String s) throws IOException {
        return assetManager.open(s);
    }
}
