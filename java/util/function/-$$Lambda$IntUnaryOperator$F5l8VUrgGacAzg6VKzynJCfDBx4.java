package java.util.function;

public final class -$$Lambda$IntUnaryOperator$F5l8VUrgGacAzg6VKzynJCfDBx4 implements IntUnaryOperator
{
    @Override
    public IntUnaryOperator andThen(final IntUnaryOperator intUnaryOperator) {
        return IntUnaryOperator-CC.$default$andThen(this, intUnaryOperator);
    }
    
    @Override
    public final int applyAsInt(final int n) {
        return IntUnaryOperator-CC.lambda$andThen$1(this.f$0, this.f$1, n);
    }
    
    @Override
    public IntUnaryOperator compose(final IntUnaryOperator intUnaryOperator) {
        return IntUnaryOperator-CC.$default$compose(this, intUnaryOperator);
    }
}
