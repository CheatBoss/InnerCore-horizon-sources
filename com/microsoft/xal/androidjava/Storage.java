package com.microsoft.xal.androidjava;

import android.content.*;

public class Storage
{
    public static String getStoragePath(final Context context) {
        return context.getFilesDir().getPath();
    }
}
