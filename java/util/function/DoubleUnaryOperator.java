package java.util.function;

import java.util.*;

@FunctionalInterface
public interface DoubleUnaryOperator
{
    double applyAsDouble(final double p0);
    
    default DoubleUnaryOperator compose(final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return n -> this.applyAsDouble(doubleUnaryOperator.applyAsDouble(n));
    }
    
    default DoubleUnaryOperator andThen(final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return n -> doubleUnaryOperator.applyAsDouble(this.applyAsDouble(n));
    }
    
    default DoubleUnaryOperator identity() {
        return n -> n;
    }
}
