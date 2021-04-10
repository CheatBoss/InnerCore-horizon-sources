package org.apache.james.mime4j;

import java.io.*;

public class MimeIOException extends IOException
{
    private static final long serialVersionUID = 5393613459533735409L;
    
    public MimeIOException(final String s) {
        this(new MimeException(s));
    }
    
    public MimeIOException(final MimeException ex) {
        super(ex.getMessage());
        this.initCause(ex);
    }
}
