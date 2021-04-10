package androidx.core.app;

import androidx.annotation.*;
import android.os.*;
import android.app.*;

public final class AlarmManagerCompat
{
    private AlarmManagerCompat() {
    }
    
    public static void setAlarmClock(@NonNull final AlarmManager alarmManager, final long n, @NonNull final PendingIntent pendingIntent, @NonNull final PendingIntent pendingIntent2) {
        if (Build$VERSION.SDK_INT >= 21) {
            alarmManager.setAlarmClock(new AlarmManager$AlarmClockInfo(n, pendingIntent), pendingIntent2);
            return;
        }
        setExact(alarmManager, 0, n, pendingIntent2);
    }
    
    public static void setAndAllowWhileIdle(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 23) {
            alarmManager.setAndAllowWhileIdle(n, n2, pendingIntent);
            return;
        }
        alarmManager.set(n, n2, pendingIntent);
    }
    
    public static void setExact(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 19) {
            alarmManager.setExact(n, n2, pendingIntent);
            return;
        }
        alarmManager.set(n, n2, pendingIntent);
    }
    
    public static void setExactAndAllowWhileIdle(@NonNull final AlarmManager alarmManager, final int n, final long n2, @NonNull final PendingIntent pendingIntent) {
        if (Build$VERSION.SDK_INT >= 23) {
            alarmManager.setExactAndAllowWhileIdle(n, n2, pendingIntent);
            return;
        }
        setExact(alarmManager, n, n2, pendingIntent);
    }
}
