package java.util.function;

public final class -$$Lambda$LongUnaryOperator$kI3lBaNH3h6ldTmGeiEUd61CYJI implements LongUnaryOperator
{
    @Override
    public LongUnaryOperator andThen(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$andThen(this, longUnaryOperator);
    }
    
    @Override
    public final long applyAsLong(final long n) {
        return LongUnaryOperator-CC.lambda$identity$2(n);
    }
    
    @Override
    public LongUnaryOperator compose(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$compose(this, longUnaryOperator);
    }
}
