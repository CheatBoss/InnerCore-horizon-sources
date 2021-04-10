package java.time;

public class DateTimeException extends RuntimeException
{
    private static final long serialVersionUID = -1632418723876261839L;
    
    public DateTimeException(final String s) {
        super(s);
    }
    
    public DateTimeException(final String s, final Throwable t) {
        super(s, t);
    }
}
