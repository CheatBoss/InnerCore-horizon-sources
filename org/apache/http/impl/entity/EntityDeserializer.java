package org.apache.http.impl.entity;

import org.apache.http.io.*;
import org.apache.http.*;
import java.io.*;
import org.apache.http.entity.*;

@Deprecated
public class EntityDeserializer
{
    public EntityDeserializer(final ContentLengthStrategy contentLengthStrategy) {
        throw new RuntimeException("Stub!");
    }
    
    public HttpEntity deserialize(final SessionInputBuffer sessionInputBuffer, final HttpMessage httpMessage) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    protected BasicHttpEntity doDeserialize(final SessionInputBuffer sessionInputBuffer, final HttpMessage httpMessage) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
