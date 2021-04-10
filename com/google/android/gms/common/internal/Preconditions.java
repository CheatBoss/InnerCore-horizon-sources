package com.google.android.gms.common.internal;

import android.os.*;
import com.google.android.gms.common.util.*;
import android.text.*;

public final class Preconditions
{
    public static void checkArgument(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public static void checkArgument(final boolean b, final Object o) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(o));
    }
    
    public static void checkHandlerThread(final Handler handler) {
        if (Looper.myLooper() == handler.getLooper()) {
            return;
        }
        throw new IllegalStateException("Must be called on the handler thread");
    }
    
    public static void checkMainThread(final String s) {
        if (ThreadUtils.isMainThread()) {
            return;
        }
        throw new IllegalStateException(s);
    }
    
    public static String checkNotEmpty(final String s) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        throw new IllegalArgumentException("Given String is empty or null");
    }
    
    public static String checkNotEmpty(final String s, final Object o) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return s;
        }
        throw new IllegalArgumentException(String.valueOf(o));
    }
    
    public static void checkNotMainThread() {
        checkNotMainThread("Must not be called on the main application thread");
    }
    
    public static void checkNotMainThread(final String s) {
        if (!ThreadUtils.isMainThread()) {
            return;
        }
        throw new IllegalStateException(s);
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null reference");
    }
    
    public static <T> T checkNotNull(final T t, final Object o) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(o));
    }
    
    public static int checkNotZero(final int n) {
        if (n != 0) {
            return n;
        }
        throw new IllegalArgumentException("Given Integer is zero");
    }
    
    public static void checkState(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalStateException();
    }
    
    public static void checkState(final boolean b, final Object o) {
        if (b) {
            return;
        }
        throw new IllegalStateException(String.valueOf(o));
    }
}
