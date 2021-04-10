package java.util.concurrent;

import sun.misc.*;
import java.lang.reflect.*;

final class DesugarUnsafe
{
    private static final Unsafe theUnsafe;
    
    static {
        final Field field = getField();
        field.setAccessible(true);
        try {
            theUnsafe = (Unsafe)field.get(null);
        }
        catch (IllegalAccessException ex) {
            throw new Error("Couldn't get the Unsafe", ex);
        }
    }
    
    public static final int getAndAddInt(final Unsafe unsafe, final Object o, final long n, final int n2) {
        int intVolatile;
        do {
            intVolatile = unsafe.getIntVolatile(o, n);
        } while (!unsafe.compareAndSwapInt(o, n, intVolatile, intVolatile + n2));
        return intVolatile;
    }
    
    public static final long getAndAddLong(final Unsafe unsafe, final Object o, final long n, final long n2) {
        long longVolatile;
        do {
            longVolatile = unsafe.getLongVolatile(o, n);
        } while (!unsafe.compareAndSwapLong(o, n, longVolatile, longVolatile + n2));
        return longVolatile;
    }
    
    public static final int getAndSetInt(final Unsafe unsafe, final Object o, final long n, final int n2) {
        int intVolatile;
        do {
            intVolatile = unsafe.getIntVolatile(o, n);
        } while (!unsafe.compareAndSwapInt(o, n, intVolatile, n2));
        return intVolatile;
    }
    
    public static final long getAndSetLong(final Unsafe unsafe, final Object o, final long n, final long n2) {
        long longVolatile;
        do {
            longVolatile = unsafe.getLongVolatile(o, n);
        } while (!unsafe.compareAndSwapLong(o, n, longVolatile, n2));
        return longVolatile;
    }
    
    public static final Object getAndSetObject(final Unsafe unsafe, final Object o, final long n, final Object o2) {
        Object objectVolatile;
        do {
            objectVolatile = unsafe.getObjectVolatile(o, n);
        } while (!unsafe.compareAndSwapObject(o, n, objectVolatile, o2));
        return objectVolatile;
    }
    
    private static Field getField() {
        try {
            return Unsafe.class.getDeclaredField("theUnsafe");
        }
        catch (NoSuchFieldException ex) {
            final Field[] declaredFields = Unsafe.class.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (Modifier.isStatic(field.getModifiers()) && Unsafe.class.isAssignableFrom(field.getType())) {
                    return field;
                }
            }
            throw new Error("Couldn't find the Unsafe", ex);
        }
    }
    
    public static Unsafe getUnsafe() {
        return DesugarUnsafe.theUnsafe;
    }
}
