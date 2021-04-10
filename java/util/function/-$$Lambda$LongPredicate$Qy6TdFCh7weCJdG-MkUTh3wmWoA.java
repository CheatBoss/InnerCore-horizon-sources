package java.util.function;

public final class -$$Lambda$LongPredicate$Qy6TdFCh7weCJdG-MkUTh3wmWoA implements LongPredicate
{
    @Override
    public LongPredicate and(final LongPredicate longPredicate) {
        return LongPredicate-CC.$default$and(this, longPredicate);
    }
    
    @Override
    public LongPredicate negate() {
        return LongPredicate-CC.$default$negate(this);
    }
    
    @Override
    public LongPredicate or(final LongPredicate longPredicate) {
        return LongPredicate-CC.$default$or(this, longPredicate);
    }
    
    @Override
    public final boolean test(final long n) {
        return LongPredicate-CC.lambda$negate$1(this.f$0, n);
    }
}
