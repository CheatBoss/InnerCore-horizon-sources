package java.util.function;

public final class -$$Lambda$IntPredicate$6LuiLiTSEVs3MpquRl2gnnnEIxg implements IntPredicate
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
        return IntPredicate-CC.lambda$negate$1(this.f$0, n);
    }
}
