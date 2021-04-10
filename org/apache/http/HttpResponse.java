package org.apache.http;

import java.util.*;

@Deprecated
public interface HttpResponse extends HttpMessage
{
    HttpEntity getEntity();
    
    Locale getLocale();
    
    StatusLine getStatusLine();
    
    void setEntity(final HttpEntity p0);
    
    void setLocale(final Locale p0);
    
    void setReasonPhrase(final String p0) throws IllegalStateException;
    
    void setStatusCode(final int p0) throws IllegalStateException;
    
    void setStatusLine(final ProtocolVersion p0, final int p1);
    
    void setStatusLine(final ProtocolVersion p0, final int p1, final String p2);
    
    void setStatusLine(final StatusLine p0);
}
