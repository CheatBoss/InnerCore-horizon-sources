package com.microsoft.xbox.idp.util;

import java.io.*;

public class HttpCall
{
    private final long id;
    
    public HttpCall(final String s, final String s2, final String s3) {
        this.id = create(s, s2, s3, true);
    }
    
    public HttpCall(final String s, final String s2, final String s3, final boolean b) {
        this.id = create(s, s2, s3, b);
    }
    
    private static native long create(final String p0, final String p1, final String p2, final boolean p3);
    
    private static native void delete(final long p0);
    
    @Override
    protected void finalize() throws Throwable {
        delete(this.id);
        super.finalize();
    }
    
    public native void getResponseAsync(final Callback p0);
    
    public native void setContentTypeHeaderValue(final String p0);
    
    public native void setCustomHeader(final String p0, final String p1);
    
    public native void setLongHttpCall(final boolean p0);
    
    public native void setRequestBody(final String p0);
    
    public native void setRequestBody(final byte[] p0);
    
    public native void setRetryAllowed(final boolean p0);
    
    public native void setXboxContractVersionHeaderValue(final String p0);
    
    public interface Callback
    {
        void processResponse(final int p0, final InputStream p1, final HttpHeaders p2) throws Exception;
    }
}
