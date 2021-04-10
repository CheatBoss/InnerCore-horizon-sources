package java.util.function;

public final class -$$Lambda$IntUnaryOperator$DKxG0-oyUAYjk17nXTQ5x-EXFgU implements IntUnaryOperator
{
    @Override
    public IntUnaryOperator andThen(final IntUnaryOperator intUnaryOperator) {
        return IntUnaryOperator-CC.$default$andThen(this, intUnaryOperator);
    }
    
    @Override
    public final int applyAsInt(final int n) {
        return IntUnaryOperator-CC.lambda$identity$2(n);
    }
    
    @Override
    public IntUnaryOperator compose(final IntUnaryOperator intUnaryOperator) {
        return IntUnaryOperator-CC.$default$compose(this, intUnaryOperator);
    }
}
