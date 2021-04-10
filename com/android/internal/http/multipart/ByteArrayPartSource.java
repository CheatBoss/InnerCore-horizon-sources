package com.android.internal.http.multipart;

import java.io.*;

public class ByteArrayPartSource implements PartSource
{
    public ByteArrayPartSource(final String s, final byte[] array) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream createInputStream() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String getFileName() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getLength() {
        throw new RuntimeException("Stub!");
    }
}
