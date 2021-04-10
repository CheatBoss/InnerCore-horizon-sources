package com.bumptech.glide.load.data;

import com.bumptech.glide.*;
import java.io.*;

public class ByteArrayFetcher implements DataFetcher<InputStream>
{
    private final byte[] bytes;
    private final String id;
    
    public ByteArrayFetcher(final byte[] bytes, final String id) {
        this.bytes = bytes;
        this.id = id;
    }
    
    @Override
    public void cancel() {
    }
    
    @Override
    public void cleanup() {
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    @Override
    public InputStream loadData(final Priority priority) {
        return new ByteArrayInputStream(this.bytes);
    }
}
