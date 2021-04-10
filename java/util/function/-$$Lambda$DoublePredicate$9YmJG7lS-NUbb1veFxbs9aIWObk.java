package java.util.function;

public final class -$$Lambda$DoublePredicate$9YmJG7lS-NUbb1veFxbs9aIWObk implements DoublePredicate
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
        return DoublePredicate-CC.lambda$or$2(this.f$0, this.f$1, n);
    }
}
