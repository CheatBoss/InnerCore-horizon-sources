package org.apache.http.impl.entity;

import org.apache.http.entity.*;
import org.apache.http.*;

@Deprecated
public class LaxContentLengthStrategy implements ContentLengthStrategy
{
    public LaxContentLengthStrategy() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public long determineLength(final HttpMessage httpMessage) throws HttpException {
        throw new RuntimeException("Stub!");
    }
}
