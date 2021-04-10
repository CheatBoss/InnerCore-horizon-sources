package bo.app;

import com.appboy.configuration.*;
import android.content.pm.*;
import com.appboy.support.*;
import android.content.*;
import android.app.*;
import android.os.*;

public class bq
{
    private static final String a;
    private final Context b;
    private final bv c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bq.class);
    }
    
    public bq(final Context b, final bv c) {
        this.b = b;
        this.c = c;
    }
    
    public static boolean a(final Context context, final AppboyConfigurationProvider appboyConfigurationProvider) {
        return dy.b(context) && b(context, appboyConfigurationProvider);
    }
    
    private static boolean b(Context context, final AppboyConfigurationProvider appboyConfigurationProvider) {
        final StringBuilder sb = new StringBuilder();
        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(packageName);
        sb2.append(".permission.C2D_MESSAGE");
        final String string = sb2.toString();
        try {
            packageManager.getPermissionInfo(string, 4096);
        }
        catch (PackageManager$NameNotFoundException ex) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("The manifest does not define the ");
            sb3.append(string);
            sb3.append(" permission.");
            sb.append(sb3.toString());
        }
        Label_0191: {
            String string2;
            if (!PermissionUtils.hasPermission(context, "android.permission.INTERNET")) {
                string2 = "Missing permission. The android.permission.INTERNET permission must be set so that the Android application can send the registration ID to the 3rd party server.";
            }
            else if (!PermissionUtils.hasPermission(context, "com.google.android.c2dm.permission.RECEIVE")) {
                string2 = "Missing permission. The com.google.android.c2dm.permission.RECEIVE permission must be set so that the Android application can register and receive messages.";
            }
            else {
                if (PermissionUtils.hasPermission(context, string)) {
                    break Label_0191;
                }
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Missing permission. The ");
                sb4.append(string);
                sb4.append(" permission must be set so that ONLY this Android application can register and receive GCM messages.");
                string2 = sb4.toString();
            }
            sb.append(string2);
        }
        if (!PermissionUtils.hasPermission(context, "android.permission.WAKE_LOCK")) {
            AppboyLogger.i(bq.a, "Missing permission. The android.permission.WAKE_LOCK permission is recommended be set so that the GCM receiver can notify users by waking the phone when a message is received.");
        }
        context = (Context)new ComponentName(context, "com.appboy.AppboyGcmReceiver");
        try {
            final ActivityInfo receiverInfo = packageManager.getReceiverInfo((ComponentName)context, 2);
            if (receiverInfo == null || !receiverInfo.enabled) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("The ");
                sb5.append(((ComponentName)context).getClassName());
                sb5.append(" broadcast receiver is either not found or is disabled");
                sb.append(sb5.toString());
            }
        }
        catch (PackageManager$NameNotFoundException ex2) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("No ");
            sb6.append(((ComponentName)context).getClassName());
            sb6.append(" broadcast receiver is registered in the manifest.");
            sb.append(sb6.toString());
        }
        if (appboyConfigurationProvider.getGcmSenderId() == null) {
            sb.append("Cannot find the Google Cloud Messaging sender ID value in configuration");
        }
        if (sb.length() == 0) {
            return true;
        }
        AppboyLogger.e(bq.a, sb.toString());
        return false;
    }
    
    public void a(final String... array) {
        if (this.c.a() != null) {
            AppboyLogger.w(bq.a, "The device is already registered with the GCM server and is eligible to receive GCM messages.");
            return;
        }
        AppboyLogger.d(bq.a, "Registering the application with the GCM server.");
        final String join = StringUtils.join(array, ",");
        final Intent intent = new Intent("com.google.android.c2dm.intent.REGISTER");
        intent.setPackage("com.google.android.gsf");
        intent.putExtra("app", (Parcelable)PendingIntent.getBroadcast(this.b, 0, new Intent(), 0));
        intent.putExtra("sender", join);
        this.b.startService(intent);
    }
}
