package com.microsoft.xbox.service.network.managers;

import org.apache.http.client.methods.*;
import java.net.*;

public class HttpDeleteWithRequestBody extends HttpPost
{
    public HttpDeleteWithRequestBody(final URI uri) {
        super(uri);
    }
    
    public String getMethod() {
        return "DELETE";
    }
}
