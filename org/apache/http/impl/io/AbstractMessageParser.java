package org.apache.http.impl.io;

import org.apache.http.message.*;
import org.apache.http.io.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractMessageParser implements HttpMessageParser
{
    protected final LineParser lineParser;
    
    public AbstractMessageParser(final SessionInputBuffer sessionInputBuffer, final LineParser lineParser, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    public static Header[] parseHeaders(final SessionInputBuffer sessionInputBuffer, final int n, final int n2, final LineParser lineParser) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public HttpMessage parse() throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract HttpMessage parseHead(final SessionInputBuffer p0) throws IOException, HttpException, ParseException;
}
