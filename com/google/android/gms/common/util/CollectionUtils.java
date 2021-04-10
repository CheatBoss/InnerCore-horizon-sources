package com.google.android.gms.common.util;

import java.util.*;

public final class CollectionUtils
{
    @Deprecated
    public static <T> List<T> listOf() {
        return Collections.emptyList();
    }
    
    @Deprecated
    public static <T> List<T> listOf(final T t) {
        return Collections.singletonList(t);
    }
    
    @Deprecated
    public static <T> List<T> listOf(final T... array) {
        final int length = array.length;
        if (length == 0) {
            return listOf();
        }
        if (length != 1) {
            return Collections.unmodifiableList((List<? extends T>)Arrays.asList((T[])array));
        }
        return listOf(array[0]);
    }
}
