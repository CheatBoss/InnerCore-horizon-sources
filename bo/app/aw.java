package bo.app;

public class aw extends Exception
{
    public aw(final String s) {
        super(s);
    }
    
    public aw(final String s, final Throwable t) {
        super(s, t);
    }
}
