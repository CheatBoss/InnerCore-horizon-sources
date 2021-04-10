package java.util.function;

public final class -$$Lambda$DoubleUnaryOperator$EzzlhUGRoL66wVBCG-_euZgC-CA implements DoubleUnaryOperator
{
    @Override
    public DoubleUnaryOperator andThen(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$andThen(this, doubleUnaryOperator);
    }
    
    @Override
    public final double applyAsDouble(final double n) {
        return DoubleUnaryOperator-CC.lambda$andThen$1(this.f$0, this.f$1, n);
    }
    
    @Override
    public DoubleUnaryOperator compose(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$compose(this, doubleUnaryOperator);
    }
}
