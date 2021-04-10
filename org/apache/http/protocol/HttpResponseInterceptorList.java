package org.apache.http.protocol;

import org.apache.http.*;
import java.util.*;

@Deprecated
public interface HttpResponseInterceptorList
{
    void addResponseInterceptor(final HttpResponseInterceptor p0);
    
    void addResponseInterceptor(final HttpResponseInterceptor p0, final int p1);
    
    void clearResponseInterceptors();
    
    HttpResponseInterceptor getResponseInterceptor(final int p0);
    
    int getResponseInterceptorCount();
    
    void removeResponseInterceptorByClass(final Class p0);
    
    void setInterceptors(final List p0);
}
