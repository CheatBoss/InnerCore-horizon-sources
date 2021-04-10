package java.util;

import java.util.function.*;

public final class Spliterator-CC
{
    public static void $default$forEachRemaining(final Spliterator p0, final Consumer<?> p2) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static Comparator<?> $default$getComparator(final Spliterator spliterator) {
        throw new IllegalStateException();
    }
    
    public static long $default$getExactSizeIfKnown(final Spliterator spliterator) {
        if ((spliterator.characteristics() & 0x40) == 0x0) {
            return -1L;
        }
        return spliterator.estimateSize();
    }
    
    public static boolean $default$hasCharacteristics(final Spliterator spliterator, final int n) {
        return (spliterator.characteristics() & n) == n;
    }
}
