package org.apache.james.mime4j.field;

import org.apache.james.mime4j.*;

public class ParseException extends MimeException
{
    private static final long serialVersionUID = 1L;
    
    protected ParseException(final String s) {
        super(s);
    }
    
    protected ParseException(final String s, final Throwable t) {
        super(s, t);
    }
    
    protected ParseException(final Throwable t) {
        super(t);
    }
}
