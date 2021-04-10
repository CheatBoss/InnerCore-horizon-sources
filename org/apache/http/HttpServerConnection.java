package org.apache.http;

import java.io.*;

@Deprecated
public interface HttpServerConnection extends HttpConnection
{
    void flush() throws IOException;
    
    void receiveRequestEntity(final HttpEntityEnclosingRequest p0) throws HttpException, IOException;
    
    HttpRequest receiveRequestHeader() throws HttpException, IOException;
    
    void sendResponseEntity(final HttpResponse p0) throws HttpException, IOException;
    
    void sendResponseHeader(final HttpResponse p0) throws HttpException, IOException;
}
