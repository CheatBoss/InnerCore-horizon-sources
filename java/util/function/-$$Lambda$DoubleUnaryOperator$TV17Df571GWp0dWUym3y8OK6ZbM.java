package java.util.function;

public final class -$$Lambda$DoubleUnaryOperator$TV17Df571GWp0dWUym3y8OK6ZbM implements DoubleUnaryOperator
{
    @Override
    public DoubleUnaryOperator andThen(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$andThen(this, doubleUnaryOperator);
    }
    
    @Override
    public final double applyAsDouble(final double n) {
        return DoubleUnaryOperator-CC.lambda$compose$0(this.f$0, this.f$1, n);
    }
    
    @Override
    public DoubleUnaryOperator compose(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$compose(this, doubleUnaryOperator);
    }
}
