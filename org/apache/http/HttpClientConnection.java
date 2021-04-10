package org.apache.http;

import java.io.*;

@Deprecated
public interface HttpClientConnection extends HttpConnection
{
    void flush() throws IOException;
    
    boolean isResponseAvailable(final int p0) throws IOException;
    
    void receiveResponseEntity(final HttpResponse p0) throws HttpException, IOException;
    
    HttpResponse receiveResponseHeader() throws HttpException, IOException;
    
    void sendRequestEntity(final HttpEntityEnclosingRequest p0) throws HttpException, IOException;
    
    void sendRequestHeader(final HttpRequest p0) throws HttpException, IOException;
}
