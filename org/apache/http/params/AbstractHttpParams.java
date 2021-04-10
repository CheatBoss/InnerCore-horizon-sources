package org.apache.http.params;

@Deprecated
public abstract class AbstractHttpParams implements HttpParams
{
    protected AbstractHttpParams() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean getBooleanParameter(final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public double getDoubleParameter(final String s, final double n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public int getIntParameter(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long getLongParameter(final String s, final long n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isParameterFalse(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public boolean isParameterTrue(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setBooleanParameter(final String s, final boolean b) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setDoubleParameter(final String s, final double n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setIntParameter(final String s, final int n) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpParams setLongParameter(final String s, final long n) {
        throw new RuntimeException("Stub!");
    }
}
