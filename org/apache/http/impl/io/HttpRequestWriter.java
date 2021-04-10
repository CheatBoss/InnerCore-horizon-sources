package org.apache.http.impl.io;

import org.apache.http.io.*;
import org.apache.http.message.*;
import org.apache.http.params.*;
import org.apache.http.*;
import java.io.*;

@Deprecated
public class HttpRequestWriter extends AbstractMessageWriter
{
    public HttpRequestWriter(final SessionOutputBuffer sessionOutputBuffer, final LineFormatter lineFormatter, final HttpParams httpParams) {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected void writeHeadLine(final HttpMessage httpMessage) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
