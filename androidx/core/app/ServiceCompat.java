package androidx.core.app;

import android.app.*;
import android.os.*;
import java.lang.annotation.*;
import androidx.annotation.*;

public final class ServiceCompat
{
    public static final int START_STICKY = 1;
    public static final int STOP_FOREGROUND_DETACH = 2;
    public static final int STOP_FOREGROUND_REMOVE = 1;
    
    private ServiceCompat() {
    }
    
    public static void stopForeground(@NonNull final Service service, final int n) {
        if (Build$VERSION.SDK_INT >= 24) {
            service.stopForeground(n);
            return;
        }
        service.stopForeground((n & 0x1) != 0x0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public @interface StopForegroundFlags {
    }
}
