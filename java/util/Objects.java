package java.util;

import java.util.function.*;

public final class Objects
{
    private Objects() {
        throw new AssertionError((Object)"No java.util.Objects instances for you!");
    }
    
    public static boolean equals(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static boolean deepEquals(final Object o, final Object o2) {
        return o == o2 || (o != null && o2 != null && Arrays.deepEquals0(o, o2));
    }
    
    public static int hashCode(final Object o) {
        return (o != null) ? o.hashCode() : 0;
    }
    
    public static int hash(final Object... array) {
        return Arrays.hashCode(array);
    }
    
    public static String toString(final Object o) {
        return String.valueOf(o);
    }
    
    public static String toString(final Object o, final String s) {
        return (o != null) ? o.toString() : s;
    }
    
    public static <T> int compare(final T t, final T t2, final Comparator<? super T> comparator) {
        return (t == t2) ? 0 : comparator.compare(t, t2);
    }
    
    public static <T> T requireNonNull(final T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        return t;
    }
    
    public static <T> T requireNonNull(final T t, final String s) {
        if (t == null) {
            throw new NullPointerException(s);
        }
        return t;
    }
    
    public static boolean isNull(final Object o) {
        return o == null;
    }
    
    public static boolean nonNull(final Object o) {
        return o != null;
    }
    
    public static <T> T requireNonNull(final T t, final Supplier<String> supplier) {
        if (t == null) {
            throw new NullPointerException(supplier.get());
        }
        return t;
    }
}
