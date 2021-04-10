package com.google.android.gms.common.util;

import android.os.*;

public class ThreadUtils
{
    public static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }
}
