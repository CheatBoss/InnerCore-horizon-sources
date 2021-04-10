package org.apache.http.client;

import org.apache.http.*;
import java.io.*;

@Deprecated
public interface ResponseHandler<T>
{
    T handleResponse(final HttpResponse p0) throws ClientProtocolException, IOException;
}
