package java.util.stream;

public final class Sink-CC
{
    public static void $default$accept(final Sink sink, final double n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    public static void $default$accept(final Sink sink, final int n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    public static void $default$accept(final Sink sink, final long n) {
        throw new IllegalStateException("called wrong accept method");
    }
    
    public static void $default$begin(final Sink sink, final long n) {
    }
    
    public static boolean $default$cancellationRequested(final Sink sink) {
        return false;
    }
    
    public static void $default$end(final Sink sink) {
    }
}
