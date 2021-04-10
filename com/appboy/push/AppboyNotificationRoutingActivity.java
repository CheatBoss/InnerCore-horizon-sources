package com.appboy.push;

import android.app.*;
import com.appboy.support.*;
import android.os.*;
import android.content.*;

public class AppboyNotificationRoutingActivity extends Activity
{
    private static final String TAG;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyNotificationRoutingActivity.class);
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final Intent intent = this.getIntent();
        Label_0027: {
            String s;
            String s2;
            if (intent == null) {
                s = AppboyNotificationRoutingActivity.TAG;
                s2 = "Notification routing activity received null intent. Doing nothing.";
            }
            else {
                final String action = intent.getAction();
                if (action != null) {
                    final String tag = AppboyNotificationRoutingActivity.TAG;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Notification routing activity received intent: ");
                    sb.append(intent.toString());
                    AppboyLogger.i(tag, sb.toString());
                    final Context applicationContext = this.getApplicationContext();
                    final Intent setClass = new Intent(action).setClass(applicationContext, (Class)AppboyNotificationUtils.getNotificationReceiverClass());
                    setClass.putExtras(intent.getExtras());
                    applicationContext.sendBroadcast(setClass);
                    break Label_0027;
                }
                s = AppboyNotificationRoutingActivity.TAG;
                s2 = "Notification routing activity received intent with null action. Doing nothing.";
            }
            AppboyLogger.d(s, s2);
        }
        this.finish();
    }
}
