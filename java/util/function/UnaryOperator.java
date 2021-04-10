package java.util.function;

@FunctionalInterface
public interface UnaryOperator<T> extends Function<T, T>
{
    default <T> UnaryOperator<T> identity() {
        return (UnaryOperator<T>)(o -> o);
    }
}
