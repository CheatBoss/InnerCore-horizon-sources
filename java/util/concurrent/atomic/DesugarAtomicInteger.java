package java.util.concurrent.atomic;

import java.util.function.*;

public class DesugarAtomicInteger
{
    private DesugarAtomicInteger() {
    }
    
    public static int accumulateAndGet(final AtomicInteger atomicInteger, final int n, final IntBinaryOperator intBinaryOperator) {
        int value;
        int applyAsInt;
        do {
            value = atomicInteger.get();
            applyAsInt = intBinaryOperator.applyAsInt(value, n);
        } while (!atomicInteger.compareAndSet(value, applyAsInt));
        return applyAsInt;
    }
    
    public static int getAndAccumulate(final AtomicInteger atomicInteger, final int n, final IntBinaryOperator intBinaryOperator) {
        int value;
        do {
            value = atomicInteger.get();
        } while (!atomicInteger.compareAndSet(value, intBinaryOperator.applyAsInt(value, n)));
        return value;
    }
    
    public static int getAndUpdate(final AtomicInteger atomicInteger, final IntUnaryOperator intUnaryOperator) {
        int value;
        do {
            value = atomicInteger.get();
        } while (!atomicInteger.compareAndSet(value, intUnaryOperator.applyAsInt(value)));
        return value;
    }
    
    public static int updateAndGet(final AtomicInteger atomicInteger, final IntUnaryOperator intUnaryOperator) {
        int value;
        int applyAsInt;
        do {
            value = atomicInteger.get();
            applyAsInt = intUnaryOperator.applyAsInt(value);
        } while (!atomicInteger.compareAndSet(value, applyAsInt));
        return applyAsInt;
    }
}
