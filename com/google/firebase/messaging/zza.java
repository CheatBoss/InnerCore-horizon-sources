package com.google.firebase.messaging;

import java.util.concurrent.atomic.*;
import android.content.*;
import android.content.pm.*;
import android.graphics.drawable.*;
import android.util.*;
import android.text.*;
import org.json.*;
import android.content.res.*;
import android.net.*;
import android.graphics.*;
import android.support.v4.content.*;
import com.google.android.gms.common.util.*;
import android.media.*;
import android.os.*;
import com.google.firebase.iid.*;
import android.support.v4.app.*;
import java.util.*;
import android.app.*;

final class zza
{
    private static final AtomicInteger zzdk;
    private Bundle zzdl;
    private final Context zzv;
    
    static {
        zzdk = new AtomicInteger((int)SystemClock.elapsedRealtime());
    }
    
    public zza(final Context context) {
        this.zzv = context.getApplicationContext();
    }
    
    static String zza(final Bundle bundle, final String s) {
        String s2;
        if ((s2 = bundle.getString(s)) == null) {
            s2 = bundle.getString(s.replace("gcm.n.", "gcm.notification."));
        }
        return s2;
    }
    
    private static void zza(final Intent intent, final Bundle bundle) {
        for (final String s : bundle.keySet()) {
            if (s.startsWith("google.c.a.") || s.equals("from")) {
                intent.putExtra(s, bundle.getString(s));
            }
        }
    }
    
    private final Bundle zzar() {
        final Bundle zzdl = this.zzdl;
        if (zzdl != null) {
            return zzdl;
        }
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = this.zzv.getPackageManager().getApplicationInfo(this.zzv.getPackageName(), 128);
        }
        catch (PackageManager$NameNotFoundException ex) {}
        if (applicationInfo != null && applicationInfo.metaData != null) {
            return this.zzdl = applicationInfo.metaData;
        }
        return Bundle.EMPTY;
    }
    
    static String zzb(final Bundle bundle, String s) {
        s = String.valueOf(s);
        if ("_loc_key".length() != 0) {
            s = s.concat("_loc_key");
        }
        else {
            s = new String(s);
        }
        return zza(bundle, s);
    }
    
    private final boolean zzb(final int n) {
        if (Build$VERSION.SDK_INT != 26) {
            return true;
        }
        try {
            if (this.zzv.getResources().getDrawable(n, (Resources$Theme)null) instanceof AdaptiveIconDrawable) {
                final StringBuilder sb = new StringBuilder(77);
                sb.append("Adaptive icons cannot be used in notifications. Ignoring icon id: ");
                sb.append(n);
                Log.e("FirebaseMessaging", sb.toString());
                return false;
            }
            return true;
        }
        catch (Resources$NotFoundException ex) {
            return false;
        }
    }
    
    static Object[] zzc(final Bundle bundle, final String s) {
        final String value = String.valueOf(s);
        String concat;
        if ("_loc_args".length() != 0) {
            concat = value.concat("_loc_args");
        }
        else {
            concat = new String(value);
        }
        final String zza = zza(bundle, concat);
        if (TextUtils.isEmpty((CharSequence)zza)) {
            return null;
        }
        try {
            final JSONArray jsonArray = new JSONArray(zza);
            final int length = jsonArray.length();
            final String[] array = new String[length];
            for (int i = 0; i < length; ++i) {
                array[i] = (String)jsonArray.opt(i);
            }
            return array;
        }
        catch (JSONException ex) {
            final String value2 = String.valueOf(s);
            String concat2;
            if ("_loc_args".length() != 0) {
                concat2 = value2.concat("_loc_args");
            }
            else {
                concat2 = new String(value2);
            }
            final String substring = concat2.substring(6);
            final StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 41 + String.valueOf(zza).length());
            sb.append("Malformed ");
            sb.append(substring);
            sb.append(": ");
            sb.append(zza);
            sb.append("  Default value will be used.");
            Log.w("FirebaseMessaging", sb.toString());
            return null;
        }
    }
    
    private final String zzd(Bundle zzc, String string) {
        final String zza = zza(zzc, string);
        if (!TextUtils.isEmpty((CharSequence)zza)) {
            return zza;
        }
        final String zzb = zzb(zzc, string);
        if (TextUtils.isEmpty((CharSequence)zzb)) {
            return null;
        }
        final Resources resources = this.zzv.getResources();
        final int identifier = resources.getIdentifier(zzb, "string", this.zzv.getPackageName());
        if (identifier == 0) {
            final String value = String.valueOf(string);
            String concat;
            if ("_loc_key".length() != 0) {
                concat = value.concat("_loc_key");
            }
            else {
                concat = new String(value);
            }
            final String substring = concat.substring(6);
            final StringBuilder sb = new StringBuilder(String.valueOf(substring).length() + 49 + String.valueOf(zzb).length());
            sb.append(substring);
            sb.append(" resource not found: ");
            sb.append(zzb);
            sb.append(" Default value will be used.");
            Log.w("FirebaseMessaging", sb.toString());
            return null;
        }
        zzc = (Bundle)zzc(zzc, string);
        if (zzc == null) {
            return resources.getString(identifier);
        }
        try {
            string = resources.getString(identifier, (Object[])(Object)zzc);
            return string;
        }
        catch (MissingFormatArgumentException ex) {
            final String string2 = Arrays.toString((Object[])(Object)zzc);
            final StringBuilder sb2 = new StringBuilder(String.valueOf(zzb).length() + 58 + String.valueOf(string2).length());
            sb2.append("Missing format argument for ");
            sb2.append(zzb);
            sb2.append(": ");
            sb2.append(string2);
            sb2.append(" Default value will be used.");
            Log.w("FirebaseMessaging", sb2.toString(), (Throwable)ex);
            return null;
        }
    }
    
    static boolean zzf(final Bundle bundle) {
        return "1".equals(zza(bundle, "gcm.n.e")) || zza(bundle, "gcm.n.icon") != null;
    }
    
    static Uri zzg(final Bundle bundle) {
        String s;
        if (TextUtils.isEmpty((CharSequence)(s = zza(bundle, "gcm.n.link_android")))) {
            s = zza(bundle, "gcm.n.link");
        }
        if (!TextUtils.isEmpty((CharSequence)s)) {
            return Uri.parse(s);
        }
        return null;
    }
    
    static String zzi(final Bundle bundle) {
        String s;
        if (TextUtils.isEmpty((CharSequence)(s = zza(bundle, "gcm.n.sound2")))) {
            s = zza(bundle, "gcm.n.sound");
        }
        return s;
    }
    
    private final Integer zzl(final String s) {
        if (Build$VERSION.SDK_INT < 21) {
            return null;
        }
        if (!TextUtils.isEmpty((CharSequence)s)) {
            try {
                return Color.parseColor(s);
            }
            catch (IllegalArgumentException ex) {
                final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 54);
                sb.append("Color ");
                sb.append(s);
                sb.append(" not valid. Notification will use default color.");
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
        final int int1 = this.zzar().getInt("com.google.firebase.messaging.default_notification_color", 0);
        if (int1 != 0) {
            try {
                return ContextCompat.getColor(this.zzv, int1);
            }
            catch (Resources$NotFoundException ex2) {
                Log.w("FirebaseMessaging", "Cannot find the color resource referenced in AndroidManifest.");
            }
        }
        return null;
    }
    
    final boolean zzh(final Bundle bundle) {
        if ("1".equals(zza(bundle, "gcm.n.noui"))) {
            return true;
        }
        boolean b = false;
        Label_0132: {
            if (!((KeyguardManager)this.zzv.getSystemService("keyguard")).inKeyguardRestrictedInputMode()) {
                if (!PlatformVersion.isAtLeastLollipop()) {
                    SystemClock.sleep(10L);
                }
                final int myPid = Process.myPid();
                final List runningAppProcesses = ((ActivityManager)this.zzv.getSystemService("activity")).getRunningAppProcesses();
                if (runningAppProcesses != null) {
                    for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : runningAppProcesses) {
                        if (activityManager$RunningAppProcessInfo.pid == myPid) {
                            if (activityManager$RunningAppProcessInfo.importance == 100) {
                                b = true;
                                break Label_0132;
                            }
                            break;
                        }
                    }
                }
            }
            b = false;
        }
        if (b) {
            return false;
        }
        CharSequence charSequence;
        if (TextUtils.isEmpty(charSequence = this.zzd(bundle, "gcm.n.title"))) {
            charSequence = this.zzv.getApplicationInfo().loadLabel(this.zzv.getPackageManager());
        }
        final String zzd = this.zzd(bundle, "gcm.n.body");
        final String zza = zza(bundle, "gcm.n.icon");
        int n = 0;
        Label_0404: {
            if (!TextUtils.isEmpty((CharSequence)zza)) {
                final Resources resources = this.zzv.getResources();
                n = resources.getIdentifier(zza, "drawable", this.zzv.getPackageName());
                if (n != 0 && this.zzb(n)) {
                    break Label_0404;
                }
                n = resources.getIdentifier(zza, "mipmap", this.zzv.getPackageName());
                if (n != 0 && this.zzb(n)) {
                    break Label_0404;
                }
                final StringBuilder sb = new StringBuilder(String.valueOf(zza).length() + 61);
                sb.append("Icon resource ");
                sb.append(zza);
                sb.append(" not found. Notification will use default icon.");
                Log.w("FirebaseMessaging", sb.toString());
            }
            final int int1 = this.zzar().getInt("com.google.firebase.messaging.default_notification_icon", 0);
            while (true) {
                Label_0372: {
                    if (int1 == 0) {
                        break Label_0372;
                    }
                    final int icon = int1;
                    if (!this.zzb(int1)) {
                        break Label_0372;
                    }
                    final int n2 = icon;
                    if (n2 != 0) {
                        n = n2;
                        if (this.zzb(n2)) {
                            break Label_0404;
                        }
                    }
                    n = 17301651;
                    break Label_0404;
                }
                final int icon = this.zzv.getApplicationInfo().icon;
                continue;
            }
        }
        final Integer zzl = this.zzl(zza(bundle, "gcm.n.color"));
        final String zzi = zzi(bundle);
        Uri uri;
        if (TextUtils.isEmpty((CharSequence)zzi)) {
            uri = null;
        }
        else if (!"default".equals(zzi) && this.zzv.getResources().getIdentifier(zzi, "raw", this.zzv.getPackageName()) != 0) {
            final String packageName = this.zzv.getPackageName();
            final StringBuilder sb2 = new StringBuilder(String.valueOf(packageName).length() + 24 + String.valueOf(zzi).length());
            sb2.append("android.resource://");
            sb2.append(packageName);
            sb2.append("/raw/");
            sb2.append(zzi);
            uri = Uri.parse(sb2.toString());
        }
        else {
            uri = RingtoneManager.getDefaultUri(2);
        }
        final String zza2 = zza(bundle, "gcm.n.click_action");
        Intent intent;
        if (!TextUtils.isEmpty((CharSequence)zza2)) {
            intent = new Intent(zza2);
            intent.setPackage(this.zzv.getPackageName());
            intent.setFlags(268435456);
        }
        else {
            final Uri zzg = zzg(bundle);
            if (zzg != null) {
                intent = new Intent("android.intent.action.VIEW");
                intent.setPackage(this.zzv.getPackageName());
                intent.setData(zzg);
            }
            else {
                final Intent launchIntentForPackage = this.zzv.getPackageManager().getLaunchIntentForPackage(this.zzv.getPackageName());
                if ((intent = launchIntentForPackage) == null) {
                    Log.w("FirebaseMessaging", "No activity found to launch app");
                    intent = launchIntentForPackage;
                }
            }
        }
        PendingIntent activity;
        if (intent == null) {
            activity = null;
        }
        else {
            intent.addFlags(67108864);
            final Bundle bundle2 = new Bundle(bundle);
            FirebaseMessagingService.zzj(bundle2);
            intent.putExtras(bundle2);
            for (final String s : bundle2.keySet()) {
                if (s.startsWith("gcm.n.") || s.startsWith("gcm.notification.")) {
                    intent.removeExtra(s);
                }
            }
            activity = PendingIntent.getActivity(this.zzv, com.google.firebase.messaging.zza.zzdk.incrementAndGet(), intent, 1073741824);
        }
        PendingIntent zza3;
        PendingIntent zza4;
        if (bundle != null && "1".equals(bundle.getString("google.c.a.e"))) {
            final Intent intent2 = new Intent("com.google.firebase.messaging.NOTIFICATION_OPEN");
            zza(intent2, bundle);
            intent2.putExtra("pending_intent", (Parcelable)activity);
            zza3 = zzau.zza(this.zzv, com.google.firebase.messaging.zza.zzdk.incrementAndGet(), intent2, 1073741824);
            final Intent intent3 = new Intent("com.google.firebase.messaging.NOTIFICATION_DISMISS");
            zza(intent3, bundle);
            zza4 = zzau.zza(this.zzv, com.google.firebase.messaging.zza.zzdk.incrementAndGet(), intent3, 1073741824);
        }
        else {
            zza4 = null;
            zza3 = activity;
        }
        Notification notification;
        if (PlatformVersion.isAtLeastO() && this.zzv.getApplicationInfo().targetSdkVersion > 25) {
            String channelId = zza(bundle, "gcm.n.android_channel_id");
            Label_1189: {
                if (!PlatformVersion.isAtLeastO()) {
                    channelId = null;
                }
                else {
                    final NotificationManager notificationManager = (NotificationManager)this.zzv.getSystemService((Class)NotificationManager.class);
                    if (!TextUtils.isEmpty((CharSequence)channelId)) {
                        if (notificationManager.getNotificationChannel(channelId) != null) {
                            break Label_1189;
                        }
                        final StringBuilder sb3 = new StringBuilder(String.valueOf(channelId).length() + 122);
                        sb3.append("Notification Channel requested (");
                        sb3.append(channelId);
                        sb3.append(") has not been created by the app. Manifest configuration, or default, value will be used.");
                        Log.w("FirebaseMessaging", sb3.toString());
                    }
                    channelId = this.zzar().getString("com.google.firebase.messaging.default_notification_channel_id");
                    String s2;
                    if (!TextUtils.isEmpty((CharSequence)channelId)) {
                        if (notificationManager.getNotificationChannel(channelId) != null) {
                            break Label_1189;
                        }
                        s2 = "Notification Channel set in AndroidManifest.xml has not been created by the app. Default value will be used.";
                    }
                    else {
                        s2 = "Missing Default Notification Channel metadata in AndroidManifest. Default value will be used.";
                    }
                    Log.w("FirebaseMessaging", s2);
                    if (notificationManager.getNotificationChannel("fcm_fallback_notification_channel") == null) {
                        notificationManager.createNotificationChannel(new NotificationChannel("fcm_fallback_notification_channel", (CharSequence)this.zzv.getString(R$string.fcm_fallback_notification_channel_label), 3));
                    }
                    channelId = "fcm_fallback_notification_channel";
                }
            }
            final Notification$Builder setSmallIcon = new Notification$Builder(this.zzv).setAutoCancel(true).setSmallIcon(n);
            if (!TextUtils.isEmpty(charSequence)) {
                setSmallIcon.setContentTitle(charSequence);
            }
            if (!TextUtils.isEmpty((CharSequence)zzd)) {
                setSmallIcon.setContentText((CharSequence)zzd);
                setSmallIcon.setStyle((Notification$Style)new Notification$BigTextStyle().bigText((CharSequence)zzd));
            }
            if (zzl != null) {
                setSmallIcon.setColor((int)zzl);
            }
            if (uri != null) {
                setSmallIcon.setSound(uri);
            }
            if (zza3 != null) {
                setSmallIcon.setContentIntent(zza3);
            }
            if (zza4 != null) {
                setSmallIcon.setDeleteIntent(zza4);
            }
            if (channelId != null) {
                setSmallIcon.setChannelId(channelId);
            }
            notification = setSmallIcon.build();
        }
        else {
            final NotificationCompat.Builder setSmallIcon2 = new NotificationCompat.Builder(this.zzv).setAutoCancel(true).setSmallIcon(n);
            if (!TextUtils.isEmpty(charSequence)) {
                setSmallIcon2.setContentTitle(charSequence);
            }
            if (!TextUtils.isEmpty((CharSequence)zzd)) {
                setSmallIcon2.setContentText(zzd);
                setSmallIcon2.setStyle(new NotificationCompat.BigTextStyle().bigText(zzd));
            }
            if (zzl != null) {
                setSmallIcon2.setColor(zzl);
            }
            if (uri != null) {
                setSmallIcon2.setSound(uri);
            }
            if (zza3 != null) {
                setSmallIcon2.setContentIntent(zza3);
            }
            if (zza4 != null) {
                setSmallIcon2.setDeleteIntent(zza4);
            }
            notification = setSmallIcon2.build();
        }
        final String zza5 = zza(bundle, "gcm.n.tag");
        if (Log.isLoggable("FirebaseMessaging", 3)) {
            Log.d("FirebaseMessaging", "Showing notification");
        }
        final NotificationManager notificationManager2 = (NotificationManager)this.zzv.getSystemService("notification");
        String string = zza5;
        if (TextUtils.isEmpty((CharSequence)zza5)) {
            final long uptimeMillis = SystemClock.uptimeMillis();
            final StringBuilder sb4 = new StringBuilder(37);
            sb4.append("FCM-Notification:");
            sb4.append(uptimeMillis);
            string = sb4.toString();
        }
        notificationManager2.notify(string, 0, notification);
        return true;
    }
}
