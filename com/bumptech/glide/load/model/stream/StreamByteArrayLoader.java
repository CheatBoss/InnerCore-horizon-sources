package com.bumptech.glide.load.model.stream;

import java.io.*;
import com.bumptech.glide.load.data.*;
import android.content.*;
import com.bumptech.glide.load.model.*;

public class StreamByteArrayLoader implements StreamModelLoader<byte[]>
{
    private final String id;
    
    public StreamByteArrayLoader() {
        this("");
    }
    
    @Deprecated
    public StreamByteArrayLoader(final String id) {
        this.id = id;
    }
    
    @Override
    public DataFetcher<InputStream> getResourceFetcher(final byte[] array, final int n, final int n2) {
        return new ByteArrayFetcher(array, this.id);
    }
    
    public static class Factory implements ModelLoaderFactory<byte[], InputStream>
    {
        @Override
        public ModelLoader<byte[], InputStream> build(final Context context, final GenericLoaderFactory genericLoaderFactory) {
            return new StreamByteArrayLoader();
        }
        
        @Override
        public void teardown() {
        }
    }
}
