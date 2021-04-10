package java.time.zone;

import java.time.*;

public class ZoneRulesException extends DateTimeException
{
    private static final long serialVersionUID = -1632418723876261839L;
    
    public ZoneRulesException(final String s) {
        super(s);
    }
    
    public ZoneRulesException(final String s, final Throwable t) {
        super(s, t);
    }
}
