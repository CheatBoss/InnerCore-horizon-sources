package org.apache.http.client.methods;

import org.apache.http.*;
import java.net.*;

@Deprecated
public interface HttpUriRequest extends HttpRequest
{
    void abort() throws UnsupportedOperationException;
    
    String getMethod();
    
    URI getURI();
    
    boolean isAborted();
}
