package org.apache.http;

@Deprecated
public interface HeaderElement
{
    String getName();
    
    NameValuePair getParameter(final int p0);
    
    NameValuePair getParameterByName(final String p0);
    
    int getParameterCount();
    
    NameValuePair[] getParameters();
    
    String getValue();
}
