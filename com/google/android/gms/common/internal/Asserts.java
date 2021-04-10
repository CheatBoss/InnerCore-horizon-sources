package com.google.android.gms.common.internal;

public final class Asserts
{
    public static void checkState(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalStateException();
    }
}
