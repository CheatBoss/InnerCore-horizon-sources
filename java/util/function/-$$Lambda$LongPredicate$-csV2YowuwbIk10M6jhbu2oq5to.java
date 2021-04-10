package java.util.function;

public final class -$$Lambda$LongPredicate$-csV2YowuwbIk10M6jhbu2oq5to implements LongPredicate
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
        return LongPredicate-CC.lambda$or$2(this.f$0, this.f$1, n);
    }
}
