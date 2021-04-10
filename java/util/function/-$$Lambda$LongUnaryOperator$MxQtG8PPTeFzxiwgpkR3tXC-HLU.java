package java.util.function;

public final class -$$Lambda$LongUnaryOperator$MxQtG8PPTeFzxiwgpkR3tXC-HLU implements LongUnaryOperator
{
    @Override
    public LongUnaryOperator andThen(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$andThen(this, longUnaryOperator);
    }
    
    @Override
    public final long applyAsLong(final long n) {
        return LongUnaryOperator-CC.lambda$andThen$1(this.f$0, this.f$1, n);
    }
    
    @Override
    public LongUnaryOperator compose(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$compose(this, longUnaryOperator);
    }
}
