package java.time.temporal;

import java.time.*;

public class UnsupportedTemporalTypeException extends DateTimeException
{
    private static final long serialVersionUID = -6158898438688206006L;
    
    public UnsupportedTemporalTypeException(final String s) {
        super(s);
    }
    
    public UnsupportedTemporalTypeException(final String s, final Throwable t) {
        super(s, t);
    }
}
