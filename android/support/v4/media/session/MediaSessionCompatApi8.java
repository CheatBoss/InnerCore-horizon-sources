package android.support.v4.media.session;

import android.content.*;
import android.media.*;

class MediaSessionCompatApi8
{
    public static void registerMediaButtonEventReceiver(final Context context, final ComponentName componentName) {
        ((AudioManager)context.getSystemService("audio")).registerMediaButtonEventReceiver(componentName);
    }
    
    public static void unregisterMediaButtonEventReceiver(final Context context, final ComponentName componentName) {
        ((AudioManager)context.getSystemService("audio")).unregisterMediaButtonEventReceiver(componentName);
    }
}
