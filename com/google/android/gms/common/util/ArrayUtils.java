package com.google.android.gms.common.util;

import java.lang.reflect.*;
import java.util.*;
import com.google.android.gms.common.internal.*;

public final class ArrayUtils
{
    public static <T> T[] concat(final T[]... array) {
        if (array.length == 0) {
            return (T[])Array.newInstance(array.getClass(), 0);
        }
        int i = 0;
        int n = 0;
        while (i < array.length) {
            n += array[i].length;
            ++i;
        }
        final T[] copy = Arrays.copyOf(array[0], n);
        int length = array[0].length;
        for (int j = 1; j < array.length; ++j) {
            final T[] array2 = array[j];
            System.arraycopy(array2, 0, copy, length, array2.length);
            length += array2.length;
        }
        return copy;
    }
    
    public static <T> boolean contains(final T[] array, final T t) {
        return indexOf(array, t) >= 0;
    }
    
    public static <T> int indexOf(final T[] array, final T t) {
        int i = 0;
        int length;
        if (array != null) {
            length = array.length;
        }
        else {
            length = 0;
        }
        while (i < length) {
            if (Objects.equal(array[i], t)) {
                return i;
            }
            ++i;
        }
        return -1;
    }
    
    public static <T> T[] removeAll(final T[] array, final T... array2) {
        if (array == null) {
            return null;
        }
        if (array2 != null && array2.length != 0) {
            final Object[] array3 = (Object[])Array.newInstance(array2.getClass().getComponentType(), array.length);
            final int length = array2.length;
            int n = 0;
            int n4;
            if (length == 1) {
                final int length2 = array.length;
                int n2 = 0;
                int n3 = 0;
                while (true) {
                    n4 = n2;
                    if (n3 >= length2) {
                        break;
                    }
                    final T t = array[n3];
                    int n5 = n2;
                    if (!Objects.equal(array2[0], t)) {
                        array3[n2] = t;
                        n5 = n2 + 1;
                    }
                    ++n3;
                    n2 = n5;
                }
            }
            else {
                final int length3 = array.length;
                int n6 = 0;
                while (true) {
                    n4 = n6;
                    if (n >= length3) {
                        break;
                    }
                    final T t2 = array[n];
                    int n7 = n6;
                    if (!contains(array2, t2)) {
                        array3[n6] = t2;
                        n7 = n6 + 1;
                    }
                    ++n;
                    n6 = n7;
                }
            }
            return resize(array3, n4);
        }
        return Arrays.copyOf(array, array.length);
    }
    
    public static <T> T[] resize(final T[] array, final int n) {
        if (array == null) {
            return null;
        }
        T[] copy = array;
        if (n != array.length) {
            copy = Arrays.copyOf(array, n);
        }
        return copy;
    }
}
