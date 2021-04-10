package java.util.function;

public final class -$$Lambda$DoubleUnaryOperator$i7wtM_8Ous-CB32HCfZ4usZ4zaQ implements DoubleUnaryOperator
{
    @Override
    public DoubleUnaryOperator andThen(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$andThen(this, doubleUnaryOperator);
    }
    
    @Override
    public final double applyAsDouble(final double n) {
        return DoubleUnaryOperator-CC.lambda$identity$2(n);
    }
    
    @Override
    public DoubleUnaryOperator compose(final DoubleUnaryOperator doubleUnaryOperator) {
        return DoubleUnaryOperator-CC.$default$compose(this, doubleUnaryOperator);
    }
}
