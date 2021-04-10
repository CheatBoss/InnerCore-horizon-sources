package java.util;

public final class Set-CC
{
    public static Spliterator<Object> $default$spliterator(final Set set) {
        return Spliterators.spliterator((Collection<?>)set, 1);
    }
}
