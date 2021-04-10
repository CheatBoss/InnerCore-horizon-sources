package java.util.function;

public final class -$$Lambda$DoublePredicate$01E7YsTWsjaQSI72YV852C1Uqco implements DoublePredicate
{
    @Override
    public DoublePredicate and(final DoublePredicate doublePredicate) {
        return DoublePredicate-CC.$default$and(this, doublePredicate);
    }
    
    @Override
    public DoublePredicate negate() {
        return DoublePredicate-CC.$default$negate(this);
    }
    
    @Override
    public DoublePredicate or(final DoublePredicate doublePredicate) {
        return DoublePredicate-CC.$default$or(this, doublePredicate);
    }
    
    @Override
    public final boolean test(final double n) {
        return DoublePredicate-CC.lambda$negate$1(this.f$0, n);
    }
}
