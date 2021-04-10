package java.util.concurrent.atomic;

import java.util.function.*;

public class DesugarAtomicReference
{
    private DesugarAtomicReference() {
    }
    
    public static <V> V accumulateAndGet(final AtomicReference<V> atomicReference, final V v, final BinaryOperator<V> binaryOperator) {
        V value;
        Object apply;
        do {
            value = atomicReference.get();
            apply = binaryOperator.apply(value, v);
        } while (!atomicReference.compareAndSet(value, (V)apply));
        return (V)apply;
    }
    
    public static <V> V getAndAccumulate(final AtomicReference<V> atomicReference, final V v, final BinaryOperator<V> binaryOperator) {
        V value;
        do {
            value = atomicReference.get();
        } while (!atomicReference.compareAndSet(value, (V)binaryOperator.apply(value, v)));
        return value;
    }
    
    public static <V> V getAndUpdate(final AtomicReference<V> atomicReference, final UnaryOperator<V> unaryOperator) {
        V value;
        do {
            value = atomicReference.get();
        } while (!atomicReference.compareAndSet(value, (V)unaryOperator.apply(value)));
        return value;
    }
    
    public static <V> V updateAndGet(final AtomicReference<V> atomicReference, final UnaryOperator<V> unaryOperator) {
        V value;
        Object apply;
        do {
            value = atomicReference.get();
            apply = unaryOperator.apply(value);
        } while (!atomicReference.compareAndSet(value, (V)apply));
        return (V)apply;
    }
}
