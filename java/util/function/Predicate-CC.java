package java.util.function;

public final class Predicate-CC
{
    public static Predicate<Object> $default$and(final Predicate p0, final Predicate<?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Predicate<Object> $default$negate(final Predicate predicate) {
        return (Predicate<Object>)new -$$Lambda$Predicate$L51YwfosqnYQ8QKStSMYaDgSslA(predicate);
    }
    
    public static Predicate<Object> $default$or(final Predicate p0, final Predicate<?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static <T> Predicate<T> isEqual(final Object o) {
        if (o == null) {
            return (Predicate<T>)-$$Lambda$wLIh0GiBW9398cTP8uaTH8KoGwo.INSTANCE;
        }
        return (Predicate<T>)new -$$Lambda$Predicate$SDsDck317M7uJ9htNLy7zOBr1L8(o);
    }
}
