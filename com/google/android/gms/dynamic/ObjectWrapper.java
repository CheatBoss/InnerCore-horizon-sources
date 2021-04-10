package com.google.android.gms.dynamic;

import android.os.*;
import java.lang.reflect.*;

public final class ObjectWrapper<T> extends Stub
{
    private final T zzabn;
    
    private ObjectWrapper(final T zzabn) {
        this.zzabn = zzabn;
    }
    
    public static <T> T unwrap(final IObjectWrapper objectWrapper) {
        if (objectWrapper instanceof ObjectWrapper) {
            return (T)((ObjectWrapper)objectWrapper).zzabn;
        }
        final IBinder binder = objectWrapper.asBinder();
        final Field[] declaredFields = binder.getClass().getDeclaredFields();
        final int length = declaredFields.length;
        int i = 0;
        Field field = null;
        int n = 0;
        while (i < length) {
            final Field field2 = declaredFields[i];
            int n2 = n;
            if (!field2.isSynthetic()) {
                n2 = n + 1;
                field = field2;
            }
            ++i;
            n = n2;
        }
        if (n == 1) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
                try {
                    return (T)field.get(binder);
                }
                catch (IllegalAccessException ex) {
                    throw new IllegalArgumentException("Could not access the field in remoteBinder.", ex);
                }
                catch (NullPointerException ex2) {
                    throw new IllegalArgumentException("Binder object is null.", ex2);
                }
            }
            throw new IllegalArgumentException("IObjectWrapper declared field not private!");
        }
        final int length2 = declaredFields.length;
        final StringBuilder sb = new StringBuilder(64);
        sb.append("Unexpected number of IObjectWrapper declared fields: ");
        sb.append(length2);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static <T> IObjectWrapper wrap(final T t) {
        return new ObjectWrapper<Object>(t);
    }
}
