package java.util.concurrent.atomic;

import java.util.function.*;

public class DesugarAtomicLong
{
    private DesugarAtomicLong() {
    }
    
    public static long accumulateAndGet(final AtomicLong atomicLong, final long n, final LongBinaryOperator longBinaryOperator) {
        long value;
        long applyAsLong;
        do {
            value = atomicLong.get();
            applyAsLong = longBinaryOperator.applyAsLong(value, n);
        } while (!atomicLong.compareAndSet(value, applyAsLong));
        return applyAsLong;
    }
    
    public static long getAndAccumulate(final AtomicLong atomicLong, final long n, final LongBinaryOperator longBinaryOperator) {
        long value;
        do {
            value = atomicLong.get();
        } while (!atomicLong.compareAndSet(value, longBinaryOperator.applyAsLong(value, n)));
        return value;
    }
    
    public static long getAndUpdate(final AtomicLong atomicLong, final LongUnaryOperator longUnaryOperator) {
        long value;
        do {
            value = atomicLong.get();
        } while (!atomicLong.compareAndSet(value, longUnaryOperator.applyAsLong(value)));
        return value;
    }
    
    public static long updateAndGet(final AtomicLong atomicLong, final LongUnaryOperator longUnaryOperator) {
        long value;
        long applyAsLong;
        do {
            value = atomicLong.get();
            applyAsLong = longUnaryOperator.applyAsLong(value);
        } while (!atomicLong.compareAndSet(value, applyAsLong));
        return applyAsLong;
    }
}
