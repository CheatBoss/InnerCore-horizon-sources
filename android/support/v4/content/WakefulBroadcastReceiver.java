package android.support.v4.content;

import android.util.*;
import android.content.*;
import android.os.*;

public abstract class WakefulBroadcastReceiver extends BroadcastReceiver
{
    private static final String EXTRA_WAKE_LOCK_ID = "android.support.content.wakelockid";
    private static final SparseArray<PowerManager$WakeLock> mActiveWakeLocks;
    private static int mNextId;
    
    static {
        mActiveWakeLocks = new SparseArray();
        WakefulBroadcastReceiver.mNextId = 1;
    }
    
    public static boolean completeWakefulIntent(final Intent intent) {
        final int intExtra = intent.getIntExtra("android.support.content.wakelockid", 0);
        if (intExtra == 0) {
            return false;
        }
        synchronized (WakefulBroadcastReceiver.mActiveWakeLocks) {
            final PowerManager$WakeLock powerManager$WakeLock = (PowerManager$WakeLock)WakefulBroadcastReceiver.mActiveWakeLocks.get(intExtra);
            if (powerManager$WakeLock != null) {
                powerManager$WakeLock.release();
                WakefulBroadcastReceiver.mActiveWakeLocks.remove(intExtra);
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("No active wake lock id #");
            sb.append(intExtra);
            Log.w("WakefulBroadcastReceiver", sb.toString());
            return true;
        }
    }
    
    public static ComponentName startWakefulService(final Context context, final Intent intent) {
        synchronized (WakefulBroadcastReceiver.mActiveWakeLocks) {
            final int mNextId = WakefulBroadcastReceiver.mNextId;
            ++WakefulBroadcastReceiver.mNextId;
            if (WakefulBroadcastReceiver.mNextId <= 0) {
                WakefulBroadcastReceiver.mNextId = 1;
            }
            intent.putExtra("android.support.content.wakelockid", mNextId);
            final ComponentName startService = context.startService(intent);
            if (startService == null) {
                return null;
            }
            final PowerManager powerManager = (PowerManager)context.getSystemService("power");
            final StringBuilder sb = new StringBuilder();
            sb.append("wake:");
            sb.append(startService.flattenToShortString());
            final PowerManager$WakeLock wakeLock = powerManager.newWakeLock(1, sb.toString());
            wakeLock.setReferenceCounted(false);
            wakeLock.acquire(60000L);
            WakefulBroadcastReceiver.mActiveWakeLocks.put(mNextId, (Object)wakeLock);
            return startService;
        }
    }
}
