package com.android.internal.http.multipart;

import java.io.*;

public class StringPart extends PartBase
{
    public static final String DEFAULT_CHARSET = "US-ASCII";
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";
    public static final String DEFAULT_TRANSFER_ENCODING = "8bit";
    
    public StringPart(String s, final String s2) {
        s = null;
        super(s, s, s, s);
        throw new RuntimeException("Stub!");
    }
    
    public StringPart(String s, final String s2, final String s3) {
        s = null;
        super(s, s, s, s);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected long lengthOfData() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void sendData(final OutputStream outputStream) throws IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void setCharSet(final String s) {
        throw new RuntimeException("Stub!");
    }
}
