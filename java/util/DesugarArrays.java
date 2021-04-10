package java.util;

import com.android.tools.r8.annotations.*;
import java.util.function.*;
import java.util.stream.*;

@SynthesizedClassMap({ -$$Lambda$DesugarArrays$1C2dauI-ueMyVBLxTOfkv0V6y-I.class, -$$Lambda$DesugarArrays$51zLmcTVV7Vogf_CZnCry1CSMAE.class, -$$Lambda$DesugarArrays$DIjIbx9JdqEqQDgTSapaq8SNgFs.class, -$$Lambda$DesugarArrays$dfUm1jOa5d_m_KtQgm7GzMY8EXQ.class })
public class DesugarArrays
{
    private DesugarArrays() {
    }
    
    static boolean deepEquals0(final Object o, final Object o2) {
        if (o instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.deepEquals((Object[])o, (Object[])o2);
        }
        if (o instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[])o, (byte[])o2);
        }
        if (o instanceof short[] && o2 instanceof short[]) {
            return Arrays.equals((short[])o, (short[])o2);
        }
        if (o instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[])o, (int[])o2);
        }
        if (o instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[])o, (long[])o2);
        }
        if (o instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[])o, (char[])o2);
        }
        if (o instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[])o, (float[])o2);
        }
        if (o instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[])o, (double[])o2);
        }
        if (o instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[])o, (boolean[])o2);
        }
        return o.equals(o2);
    }
    
    public static void parallelSetAll(final double[] array, final IntToDoubleFunction intToDoubleFunction) {
        intToDoubleFunction.getClass();
        IntStream-CC.range(0, array.length).parallel().forEach(new -$$Lambda$DesugarArrays$DIjIbx9JdqEqQDgTSapaq8SNgFs(array, intToDoubleFunction));
    }
    
    public static void parallelSetAll(final int[] array, final IntUnaryOperator intUnaryOperator) {
        intUnaryOperator.getClass();
        IntStream-CC.range(0, array.length).parallel().forEach(new -$$Lambda$DesugarArrays$51zLmcTVV7Vogf_CZnCry1CSMAE(array, intUnaryOperator));
    }
    
    public static void parallelSetAll(final long[] array, final IntToLongFunction intToLongFunction) {
        intToLongFunction.getClass();
        IntStream-CC.range(0, array.length).parallel().forEach(new -$$Lambda$DesugarArrays$dfUm1jOa5d_m_KtQgm7GzMY8EXQ(array, intToLongFunction));
    }
    
    public static <T> void parallelSetAll(final T[] array, final IntFunction<? extends T> intFunction) {
        intFunction.getClass();
        IntStream-CC.range(0, array.length).parallel().forEach(new -$$Lambda$DesugarArrays$1C2dauI-ueMyVBLxTOfkv0V6y-I(array, intFunction));
    }
    
    public static void setAll(final double[] array, final IntToDoubleFunction intToDoubleFunction) {
        intToDoubleFunction.getClass();
        for (int i = 0; i < array.length; ++i) {
            array[i] = intToDoubleFunction.applyAsDouble(i);
        }
    }
    
    public static void setAll(final int[] array, final IntUnaryOperator intUnaryOperator) {
        intUnaryOperator.getClass();
        for (int i = 0; i < array.length; ++i) {
            array[i] = intUnaryOperator.applyAsInt(i);
        }
    }
    
    public static void setAll(final long[] array, final IntToLongFunction intToLongFunction) {
        intToLongFunction.getClass();
        for (int i = 0; i < array.length; ++i) {
            array[i] = intToLongFunction.applyAsLong(i);
        }
    }
    
    public static <T> void setAll(final T[] array, final IntFunction<? extends T> intFunction) {
        intFunction.getClass();
        for (int i = 0; i < array.length; ++i) {
            array[i] = (T)intFunction.apply(i);
        }
    }
    
    public static Spliterator.OfDouble spliterator(final double[] array) {
        return Spliterators.spliterator(array, 1040);
    }
    
    public static Spliterator.OfDouble spliterator(final double[] array, final int n, final int n2) {
        return Spliterators.spliterator(array, n, n2, 1040);
    }
    
    public static Spliterator.OfInt spliterator(final int[] array) {
        return Spliterators.spliterator(array, 1040);
    }
    
    public static Spliterator.OfInt spliterator(final int[] array, final int n, final int n2) {
        return Spliterators.spliterator(array, n, n2, 1040);
    }
    
    public static Spliterator.OfLong spliterator(final long[] array) {
        return Spliterators.spliterator(array, 1040);
    }
    
    public static Spliterator.OfLong spliterator(final long[] array, final int n, final int n2) {
        return Spliterators.spliterator(array, n, n2, 1040);
    }
    
    public static <T> Spliterator<T> spliterator(final T[] array) {
        return Spliterators.spliterator(array, 1040);
    }
    
    public static <T> Spliterator<T> spliterator(final T[] array, final int n, final int n2) {
        return Spliterators.spliterator(array, n, n2, 1040);
    }
    
    public static DoubleStream stream(final double[] array) {
        return stream(array, 0, array.length);
    }
    
    public static DoubleStream stream(final double[] array, final int n, final int n2) {
        return StreamSupport.doubleStream(spliterator(array, n, n2), false);
    }
    
    public static IntStream stream(final int[] array) {
        return stream(array, 0, array.length);
    }
    
    public static IntStream stream(final int[] array, final int n, final int n2) {
        return StreamSupport.intStream(spliterator(array, n, n2), false);
    }
    
    public static LongStream stream(final long[] array) {
        return stream(array, 0, array.length);
    }
    
    public static LongStream stream(final long[] array, final int n, final int n2) {
        return StreamSupport.longStream(spliterator(array, n, n2), false);
    }
    
    public static <T> Stream<T> stream(final T[] array) {
        return stream(array, 0, array.length);
    }
    
    public static <T> Stream<T> stream(final T[] array, final int n, final int n2) {
        return StreamSupport.stream((Spliterator<T>)spliterator((T[])array, n, n2), false);
    }
}
