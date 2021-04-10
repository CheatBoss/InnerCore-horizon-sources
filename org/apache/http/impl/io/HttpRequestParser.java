package org.apache.http.impl.io;

import org.apache.http.io.*;
import org.apache.http.message.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public class HttpRequestParser extends AbstractMessageParser
{
    public HttpRequestParser(final SessionInputBuffer sessionInputBuffer, final LineParser lineParser, final HttpRequestFactory httpRequestFactory, final HttpParams httpParams) {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpMessage parseHead(final SessionInputBuffer sessionInputBuffer) throws IOException, HttpException, ParseException {
        throw new RuntimeException("Stub!");
    }
}
