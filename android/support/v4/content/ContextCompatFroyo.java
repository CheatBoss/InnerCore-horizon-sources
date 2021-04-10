package android.support.v4.content;

import android.content.*;
import java.io.*;

class ContextCompatFroyo
{
    public static File getExternalCacheDir(final Context context) {
        return context.getExternalCacheDir();
    }
    
    public static File getExternalFilesDir(final Context context, final String s) {
        return context.getExternalFilesDir(s);
    }
}
