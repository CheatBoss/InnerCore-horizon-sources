package org.apache.http.protocol;

import org.apache.http.*;
import java.util.*;

@Deprecated
public interface HttpRequestInterceptorList
{
    void addRequestInterceptor(final HttpRequestInterceptor p0);
    
    void addRequestInterceptor(final HttpRequestInterceptor p0, final int p1);
    
    void clearRequestInterceptors();
    
    HttpRequestInterceptor getRequestInterceptor(final int p0);
    
    int getRequestInterceptorCount();
    
    void removeRequestInterceptorByClass(final Class p0);
    
    void setInterceptors(final List p0);
}
