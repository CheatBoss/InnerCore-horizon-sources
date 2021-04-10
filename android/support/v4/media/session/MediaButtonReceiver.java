package android.support.v4.media.session;

import android.view.*;
import android.content.*;
import android.content.pm.*;
import java.util.*;

public class MediaButtonReceiver extends BroadcastReceiver
{
    public static KeyEvent handleIntent(final MediaSessionCompat mediaSessionCompat, final Intent intent) {
        if (mediaSessionCompat != null && intent != null && "android.intent.action.MEDIA_BUTTON".equals(intent.getAction()) && intent.hasExtra("android.intent.extra.KEY_EVENT")) {
            final KeyEvent keyEvent = (KeyEvent)intent.getParcelableExtra("android.intent.extra.KEY_EVENT");
            mediaSessionCompat.getController().dispatchMediaButtonEvent(keyEvent);
            return keyEvent;
        }
        return null;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
        intent2.setPackage(context.getPackageName());
        final PackageManager packageManager = context.getPackageManager();
        List list;
        if ((list = packageManager.queryIntentServices(intent2, 0)).isEmpty()) {
            intent2.setAction("android.media.browse.MediaBrowserService");
            list = packageManager.queryIntentServices(intent2, 0);
        }
        if (list.isEmpty()) {
            throw new IllegalStateException("Could not find any Service that handles android.intent.action.MEDIA_BUTTON or a media browser service implementation");
        }
        if (list.size() != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected 1 Service that handles ");
            sb.append(intent2.getAction());
            sb.append(", found ");
            sb.append(list.size());
            throw new IllegalStateException(sb.toString());
        }
        final ResolveInfo resolveInfo = list.get(0);
        intent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
        context.startService(intent);
    }
}
