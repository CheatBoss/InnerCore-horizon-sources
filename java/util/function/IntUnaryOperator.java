package java.util.function;

import java.util.*;

@FunctionalInterface
public interface IntUnaryOperator
{
    int applyAsInt(final int p0);
    
    default IntUnaryOperator compose(final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return n -> this.applyAsInt(intUnaryOperator.applyAsInt(n));
    }
    
    default IntUnaryOperator andThen(final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return n -> intUnaryOperator.applyAsInt(this.applyAsInt(n));
    }
    
    default IntUnaryOperator identity() {
        return n -> n;
    }
}
