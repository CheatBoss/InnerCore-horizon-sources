package android.support.v4.content;

import android.content.*;
import java.io.*;
import android.graphics.drawable.*;

class ContextCompatApi21
{
    public static File getCodeCacheDir(final Context context) {
        return context.getCodeCacheDir();
    }
    
    public static Drawable getDrawable(final Context context, final int n) {
        return context.getDrawable(n);
    }
    
    public static File getNoBackupFilesDir(final Context context) {
        return context.getNoBackupFilesDir();
    }
}
