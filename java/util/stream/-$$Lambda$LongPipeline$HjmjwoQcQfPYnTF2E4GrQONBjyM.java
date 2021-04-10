package java.util.stream;

import java.util.function.*;

public final class -$$Lambda$LongPipeline$HjmjwoQcQfPYnTF2E4GrQONBjyM implements LongUnaryOperator
{
    @Override
    public LongUnaryOperator andThen(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$andThen(this, longUnaryOperator);
    }
    
    @Override
    public final long applyAsLong(final long n) {
        return LongPipeline.lambda$count$4(n);
    }
    
    @Override
    public LongUnaryOperator compose(final LongUnaryOperator longUnaryOperator) {
        return LongUnaryOperator-CC.$default$compose(this, longUnaryOperator);
    }
}
