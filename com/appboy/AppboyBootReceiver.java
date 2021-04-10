package com.appboy;

import com.appboy.support.*;
import android.content.*;
import bo.app.*;

public class AppboyBootReceiver extends BroadcastReceiver
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(AppboyBootReceiver.class);
    }
    
    boolean a(final Context context, final Intent intent) {
        String s;
        String string;
        if (intent == null) {
            s = AppboyBootReceiver.a;
            string = "Null intent received. Doing nothing.";
        }
        else {
            String s2;
            StringBuilder sb2;
            if (context == null) {
                s = AppboyBootReceiver.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Null context received for intent ");
                sb.append(intent.toString());
                s2 = ". Doing nothing.";
                sb2 = sb;
            }
            else {
                final String a = AppboyBootReceiver.a;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Received broadcast message. Message: ");
                sb3.append(intent.toString());
                AppboyLogger.i(a, sb3.toString());
                if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
                    AppboyLogger.i(AppboyBootReceiver.a, "Boot complete intent received. Initializing.");
                    dx.a(context);
                    Appboy.getInstance(context);
                    return true;
                }
                s = AppboyBootReceiver.a;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Unknown intent ");
                sb4.append(intent.toString());
                s2 = " received. Doing nothing.";
                sb2 = sb4;
            }
            sb2.append(s2);
            string = sb2.toString();
        }
        AppboyLogger.w(s, string);
        return false;
    }
    
    public void onReceive(final Context context, final Intent intent) {
        this.a(context, intent);
    }
}
