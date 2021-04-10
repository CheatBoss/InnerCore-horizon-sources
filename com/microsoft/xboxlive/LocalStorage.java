package com.microsoft.xboxlive;

import android.content.*;

public class LocalStorage
{
    public static String getPath(final Context context) {
        return context.getFilesDir().getPath();
    }
}
