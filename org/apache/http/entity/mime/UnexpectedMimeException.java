package org.apache.http.entity.mime;

import org.apache.james.mime4j.*;

@Deprecated
public class UnexpectedMimeException extends RuntimeException
{
    private static final long serialVersionUID = 1316818299528463579L;
    
    public UnexpectedMimeException(final MimeException ex) {
        super(ex.getMessage(), ex);
    }
}
