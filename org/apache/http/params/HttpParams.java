package org.apache.http.params;

@Deprecated
public interface HttpParams
{
    HttpParams copy();
    
    boolean getBooleanParameter(final String p0, final boolean p1);
    
    double getDoubleParameter(final String p0, final double p1);
    
    int getIntParameter(final String p0, final int p1);
    
    long getLongParameter(final String p0, final long p1);
    
    Object getParameter(final String p0);
    
    boolean isParameterFalse(final String p0);
    
    boolean isParameterTrue(final String p0);
    
    boolean removeParameter(final String p0);
    
    HttpParams setBooleanParameter(final String p0, final boolean p1);
    
    HttpParams setDoubleParameter(final String p0, final double p1);
    
    HttpParams setIntParameter(final String p0, final int p1);
    
    HttpParams setLongParameter(final String p0, final long p1);
    
    HttpParams setParameter(final String p0, final Object p1);
}
