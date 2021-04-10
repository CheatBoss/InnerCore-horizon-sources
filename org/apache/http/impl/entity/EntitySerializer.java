package org.apache.http.impl.entity;

import org.apache.http.entity.*;
import org.apache.http.io.*;
import java.io.*;
import org.apache.http.*;

@Deprecated
public class EntitySerializer
{
    public EntitySerializer(final ContentLengthStrategy contentLengthStrategy) {
        throw new RuntimeException("Stub!");
    }
    
    protected OutputStream doSerialize(final SessionOutputBuffer sessionOutputBuffer, final HttpMessage httpMessage) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
    
    public void serialize(final SessionOutputBuffer sessionOutputBuffer, final HttpMessage httpMessage, final HttpEntity httpEntity) throws HttpException, IOException {
        throw new RuntimeException("Stub!");
    }
}
