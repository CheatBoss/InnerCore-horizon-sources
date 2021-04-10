package androidx.core.os;

import androidx.annotation.*;
import android.os.*;

public final class TraceCompat
{
    private TraceCompat() {
    }
    
    public static void beginSection(@NonNull final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.beginSection(s);
        }
    }
    
    public static void endSection() {
        if (Build$VERSION.SDK_INT >= 18) {
            Trace.endSection();
        }
    }
}
