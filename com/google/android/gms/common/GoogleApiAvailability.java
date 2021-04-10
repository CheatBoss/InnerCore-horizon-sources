package com.google.android.gms.common;

import com.google.android.gms.common.internal.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.base.*;
import android.support.v4.app.*;
import android.content.res.*;
import android.content.*;
import com.google.android.gms.common.api.internal.*;
import android.widget.*;
import android.util.*;
import android.view.*;
import android.app.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public class GoogleApiAvailability extends GoogleApiAvailabilityLight
{
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final Object mLock;
    private static final GoogleApiAvailability zzas;
    private String zzat;
    
    static {
        mLock = new Object();
        zzas = new GoogleApiAvailability();
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }
    
    GoogleApiAvailability() {
    }
    
    public static GoogleApiAvailability getInstance() {
        return GoogleApiAvailability.zzas;
    }
    
    static Dialog zza(final Context context, final int n, final DialogRedirect dialogRedirect, final DialogInterface$OnCancelListener onCancelListener) {
        AlertDialog$Builder alertDialog$Builder = null;
        if (n == 0) {
            return null;
        }
        final TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        if ("Theme.Dialog.Alert".equals(context.getResources().getResourceEntryName(typedValue.resourceId))) {
            alertDialog$Builder = new AlertDialog$Builder(context, 5);
        }
        AlertDialog$Builder alertDialog$Builder2;
        if ((alertDialog$Builder2 = alertDialog$Builder) == null) {
            alertDialog$Builder2 = new AlertDialog$Builder(context);
        }
        alertDialog$Builder2.setMessage((CharSequence)ConnectionErrorMessages.getErrorMessage(context, n));
        if (onCancelListener != null) {
            alertDialog$Builder2.setOnCancelListener(onCancelListener);
        }
        final String errorDialogButtonMessage = ConnectionErrorMessages.getErrorDialogButtonMessage(context, n);
        if (errorDialogButtonMessage != null) {
            alertDialog$Builder2.setPositiveButton((CharSequence)errorDialogButtonMessage, (DialogInterface$OnClickListener)dialogRedirect);
        }
        final String errorTitle = ConnectionErrorMessages.getErrorTitle(context, n);
        if (errorTitle != null) {
            alertDialog$Builder2.setTitle((CharSequence)errorTitle);
        }
        return (Dialog)alertDialog$Builder2.create();
    }
    
    private final String zza(final Context context, final NotificationManager notificationManager) {
        Preconditions.checkState(PlatformVersion.isAtLeastO());
        String zzb;
        if ((zzb = this.zzb()) == null) {
            zzb = "com.google.android.gms.availability";
            final NotificationChannel notificationChannel = notificationManager.getNotificationChannel("com.google.android.gms.availability");
            final String defaultNotificationChannelName = ConnectionErrorMessages.getDefaultNotificationChannelName(context);
            NotificationChannel notificationChannel2;
            if (notificationChannel == null) {
                notificationChannel2 = new NotificationChannel("com.google.android.gms.availability", (CharSequence)defaultNotificationChannelName, 4);
            }
            else {
                if (defaultNotificationChannelName.equals(notificationChannel.getName())) {
                    return zzb;
                }
                notificationChannel.setName((CharSequence)defaultNotificationChannelName);
                notificationChannel2 = notificationChannel;
            }
            notificationManager.createNotificationChannel(notificationChannel2);
            return "com.google.android.gms.availability";
        }
        return zzb;
    }
    
    static void zza(final Activity activity, final Dialog dialog, final String s, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        if (activity instanceof FragmentActivity) {
            SupportErrorDialogFragment.newInstance(dialog, dialogInterface$OnCancelListener).show(((FragmentActivity)activity).getSupportFragmentManager(), s);
            return;
        }
        ErrorDialogFragment.newInstance(dialog, dialogInterface$OnCancelListener).show(activity.getFragmentManager(), s);
    }
    
    private final void zza(final Context context, int n, final String s, final PendingIntent pendingIntent) {
        if (n == 18) {
            this.zza(context);
            return;
        }
        if (pendingIntent == null) {
            if (n == 6) {
                Log.w("GoogleApiAvailability", "Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
            }
            return;
        }
        final String errorNotificationTitle = ConnectionErrorMessages.getErrorNotificationTitle(context, n);
        final String errorNotificationMessage = ConnectionErrorMessages.getErrorNotificationMessage(context, n);
        final Resources resources = context.getResources();
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        Notification notification;
        if (DeviceProperties.isWearable(context)) {
            Preconditions.checkState(PlatformVersion.isAtLeastKitKatWatch());
            final Notification$Builder setStyle = new Notification$Builder(context).setSmallIcon(context.getApplicationInfo().icon).setPriority(2).setAutoCancel(true).setContentTitle((CharSequence)errorNotificationTitle).setStyle((Notification$Style)new Notification$BigTextStyle().bigText((CharSequence)errorNotificationMessage));
            if (DeviceProperties.isWearableWithoutPlayStore(context)) {
                setStyle.addAction(R$drawable.common_full_open_on_phone, (CharSequence)resources.getString(R$string.common_open_on_phone), pendingIntent);
            }
            else {
                setStyle.setContentIntent(pendingIntent);
            }
            if (PlatformVersion.isAtLeastO() && PlatformVersion.isAtLeastO()) {
                setStyle.setChannelId(this.zza(context, notificationManager));
            }
            notification = setStyle.build();
        }
        else {
            final NotificationCompat.Builder setStyle2 = new NotificationCompat.Builder(context).setSmallIcon(17301642).setTicker(resources.getString(R$string.common_google_play_services_notification_ticker)).setWhen(System.currentTimeMillis()).setAutoCancel(true).setContentIntent(pendingIntent).setContentTitle(errorNotificationTitle).setContentText(errorNotificationMessage).setLocalOnly(true).setStyle(new NotificationCompat.BigTextStyle().bigText(errorNotificationMessage));
            if (PlatformVersion.isAtLeastO() && PlatformVersion.isAtLeastO()) {
                setStyle2.setChannelId(this.zza(context, notificationManager));
            }
            notification = setStyle2.build();
        }
        if (n != 1 && n != 2 && n != 3) {
            n = 39789;
        }
        else {
            n = 10436;
            GooglePlayServicesUtilLight.zzbt.set(false);
        }
        if (s == null) {
            notificationManager.notify(n, notification);
            return;
        }
        notificationManager.notify(s, n, notification);
    }
    
    private final String zzb() {
        synchronized (GoogleApiAvailability.mLock) {
            return this.zzat;
        }
    }
    
    @Override
    public int getApkVersion(final Context context) {
        return super.getApkVersion(context);
    }
    
    public Dialog getErrorDialog(final Activity activity, final int n, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        return zza((Context)activity, n, DialogRedirect.getInstance(activity, this.getErrorResolutionIntent((Context)activity, n, "d"), n2), dialogInterface$OnCancelListener);
    }
    
    @Override
    public Intent getErrorResolutionIntent(final Context context, final int n, final String s) {
        return super.getErrorResolutionIntent(context, n, s);
    }
    
    @Override
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final int n, final int n2) {
        return super.getErrorResolutionPendingIntent(context, n, n2);
    }
    
    @Override
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final int n, final int n2, final String s) {
        return super.getErrorResolutionPendingIntent(context, n, n2, s);
    }
    
    public PendingIntent getErrorResolutionPendingIntent(final Context context, final ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            return connectionResult.getResolution();
        }
        return this.getErrorResolutionPendingIntent(context, connectionResult.getErrorCode(), 0);
    }
    
    @Override
    public final String getErrorString(final int n) {
        return super.getErrorString(n);
    }
    
    @Override
    public int isGooglePlayServicesAvailable(final Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }
    
    @Override
    public int isGooglePlayServicesAvailable(final Context context, final int n) {
        return super.isGooglePlayServicesAvailable(context, n);
    }
    
    @Override
    public final boolean isUserResolvableError(final int n) {
        return super.isUserResolvableError(n);
    }
    
    public GooglePlayServicesUpdatedReceiver registerCallbackOnUpdate(final Context context, final GooglePlayServicesUpdatedReceiver.Callback callback) {
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        final GooglePlayServicesUpdatedReceiver googlePlayServicesUpdatedReceiver = new GooglePlayServicesUpdatedReceiver(callback);
        context.registerReceiver((BroadcastReceiver)googlePlayServicesUpdatedReceiver, intentFilter);
        googlePlayServicesUpdatedReceiver.zzc(context);
        if (!this.isUninstalledAppPossiblyUpdating(context, "com.google.android.gms")) {
            callback.zzv();
            googlePlayServicesUpdatedReceiver.unregister();
            return null;
        }
        return googlePlayServicesUpdatedReceiver;
    }
    
    public boolean showErrorDialogFragment(final Activity activity, final int n, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        final Dialog errorDialog = this.getErrorDialog(activity, n, n2, dialogInterface$OnCancelListener);
        if (errorDialog == null) {
            return false;
        }
        zza(activity, errorDialog, "GooglePlayServicesErrorDialog", dialogInterface$OnCancelListener);
        return true;
    }
    
    public boolean showErrorDialogFragment(final Activity activity, final LifecycleFragment lifecycleFragment, final int n, final int n2, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        final Dialog zza = zza((Context)activity, n, DialogRedirect.getInstance(lifecycleFragment, this.getErrorResolutionIntent((Context)activity, n, "d"), n2), dialogInterface$OnCancelListener);
        if (zza == null) {
            return false;
        }
        zza(activity, zza, "GooglePlayServicesErrorDialog", dialogInterface$OnCancelListener);
        return true;
    }
    
    public void showErrorNotification(final Context context, final int n) {
        this.showErrorNotification(context, n, null);
    }
    
    public void showErrorNotification(final Context context, final int n, final String s) {
        this.zza(context, n, s, this.getErrorResolutionPendingIntent(context, n, 0, "n"));
    }
    
    public Dialog showUpdatingDialog(final Activity activity, final DialogInterface$OnCancelListener dialogInterface$OnCancelListener) {
        final ProgressBar view = new ProgressBar((Context)activity, (AttributeSet)null, 16842874);
        view.setIndeterminate(true);
        view.setVisibility(0);
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
        alertDialog$Builder.setView((View)view);
        alertDialog$Builder.setMessage((CharSequence)ConnectionErrorMessages.getErrorMessage((Context)activity, 18));
        alertDialog$Builder.setPositiveButton((CharSequence)"", (DialogInterface$OnClickListener)null);
        final AlertDialog create = alertDialog$Builder.create();
        zza(activity, (Dialog)create, "GooglePlayServicesUpdatingDialog", dialogInterface$OnCancelListener);
        return (Dialog)create;
    }
    
    public boolean showWrappedErrorNotification(final Context context, final ConnectionResult connectionResult, final int n) {
        final PendingIntent errorResolutionPendingIntent = this.getErrorResolutionPendingIntent(context, connectionResult);
        if (errorResolutionPendingIntent != null) {
            this.zza(context, connectionResult.getErrorCode(), null, GoogleApiActivity.zza(context, errorResolutionPendingIntent, n));
            return true;
        }
        return false;
    }
    
    final void zza(final Context context) {
        new zza(context).sendEmptyMessageDelayed(1, 120000L);
    }
    
    private final class zza extends Handler
    {
        private final Context zzau;
        
        public zza(final Context context) {
            Looper looper;
            if (Looper.myLooper() == null) {
                looper = Looper.getMainLooper();
            }
            else {
                looper = Looper.myLooper();
            }
            super(looper);
            this.zzau = context.getApplicationContext();
        }
        
        public final void handleMessage(final Message message) {
            if (message.what != 1) {
                final int what = message.what;
                final StringBuilder sb = new StringBuilder(50);
                sb.append("Don't know how to handle this message: ");
                sb.append(what);
                Log.w("GoogleApiAvailability", sb.toString());
                return;
            }
            final int googlePlayServicesAvailable = GoogleApiAvailability.this.isGooglePlayServicesAvailable(this.zzau);
            if (GoogleApiAvailability.this.isUserResolvableError(googlePlayServicesAvailable)) {
                GoogleApiAvailability.this.showErrorNotification(this.zzau, googlePlayServicesAvailable);
            }
        }
    }
}
