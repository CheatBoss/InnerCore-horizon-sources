package org.apache.james.mime4j;

public class MimeException extends Exception
{
    private static final long serialVersionUID = 8352821278714188542L;
    
    public MimeException(final String s) {
        super(s);
    }
    
    public MimeException(final String s, final Throwable t) {
        super(s);
        this.initCause(t);
    }
    
    public MimeException(final Throwable t) {
        super(t);
    }
}
