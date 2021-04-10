package java.util.function;

public final class -$$Lambda$IntPredicate$Gjqjw1UkLLbkSrWX6rKKkHJDvzI implements IntPredicate
{
    @Override
    public IntPredicate and(final IntPredicate intPredicate) {
        return IntPredicate-CC.$default$and(this, intPredicate);
    }
    
    @Override
    public IntPredicate negate() {
        return IntPredicate-CC.$default$negate(this);
    }
    
    @Override
    public IntPredicate or(final IntPredicate intPredicate) {
        return IntPredicate-CC.$default$or(this, intPredicate);
    }
    
    @Override
    public final boolean test(final int n) {
        return IntPredicate-CC.lambda$and$0(this.f$0, this.f$1, n);
    }
}
