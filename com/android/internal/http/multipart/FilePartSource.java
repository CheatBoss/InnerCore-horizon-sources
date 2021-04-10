package com.android.internal.http.multipart;

import java.io.*;

public class FilePartSource implements PartSource
{
    public FilePartSource(final File file) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }
    
    public FilePartSource(final String s, final File file) throws FileNotFoundException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public InputStream createInputStream() throws IOException {
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
