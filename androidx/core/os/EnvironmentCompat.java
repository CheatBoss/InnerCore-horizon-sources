package androidx.core.os;

import androidx.annotation.*;
import android.os.*;
import android.util.*;
import java.io.*;

public final class EnvironmentCompat
{
    public static final String MEDIA_UNKNOWN = "unknown";
    private static final String TAG = "EnvironmentCompat";
    
    private EnvironmentCompat() {
    }
    
    public static String getStorageState(@NonNull final File file) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Environment.getStorageState(file);
        }
        try {
            if (file.getCanonicalPath().startsWith(Environment.getExternalStorageDirectory().getCanonicalPath())) {
                return Environment.getExternalStorageState();
            }
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to resolve canonical path: ");
            sb.append(ex);
            Log.w("EnvironmentCompat", sb.toString());
        }
        return "unknown";
    }
}
