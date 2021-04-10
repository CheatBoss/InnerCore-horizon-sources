package org.apache.http.impl.io;

import org.apache.http.util.*;
import org.apache.http.message.*;
import org.apache.http.io.*;
import org.apache.http.params.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public abstract class AbstractMessageWriter implements HttpMessageWriter
{
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;
    protected final SessionOutputBuffer sessionBuffer;
    
    public AbstractMessageWriter(final SessionOutputBuffer sessionOutputBuffer, final LineFormatter lineFormatter, final HttpParams httpParams) {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public void write(final HttpMessage httpMessage) throws IOException, HttpException {
        throw new RuntimeException("Stub!");
    }
    
    protected abstract void writeHeadLine(final HttpMessage p0) throws IOException;
}
