package org.apache.http.entity;

import org.apache.http.*;

@Deprecated
public interface ContentLengthStrategy
{
    public static final int CHUNKED = -2;
    public static final int IDENTITY = -1;
    
    long determineLength(final HttpMessage p0) throws HttpException;
}
