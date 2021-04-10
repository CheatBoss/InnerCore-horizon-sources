package org.apache.http.impl.client;

import org.apache.http.*;
import java.io.*;
import org.apache.http.client.*;

@Deprecated
public class BasicResponseHandler implements ResponseHandler<String>
{
    public BasicResponseHandler() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String handleResponse(final HttpResponse httpResponse) throws HttpResponseException, IOException {
        throw new RuntimeException("Stub!");
    }
}
