package org.apache.http.impl.conn;

import org.apache.http.impl.io.*;
import org.apache.http.io.*;
import org.apache.http.message.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public class DefaultResponseParser extends AbstractMessageParser
{
    public DefaultResponseParser(final SessionInputBuffer sessionInputBuffer, final LineParser lineParser, final HttpResponseFactory httpResponseFactory, final HttpParams httpParams) {
        super(null, null, null);
        throw new RuntimeException("Stub!");
    }
    
    @Override
    protected HttpMessage parseHead(final SessionInputBuffer sessionInputBuffer) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
}
