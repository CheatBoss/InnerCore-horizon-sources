package com.google.firebase.messaging;

import android.os.*;
import java.util.*;
import android.util.*;
import android.app.*;
import android.text.*;
import com.google.firebase.iid.*;
import android.content.*;
import com.google.android.gms.tasks.*;
import java.util.concurrent.*;

public class FirebaseMessagingService extends zzb
{
    private static final Queue<String> zzdo;
    
    static {
        zzdo = new ArrayDeque<String>(10);
    }
    
    static void zzj(final Bundle bundle) {
        final Iterator<String> iterator = (Iterator<String>)bundle.keySet().iterator();
        while (iterator.hasNext()) {
            final String s = iterator.next();
            if (s != null && s.startsWith("google.c.")) {
                iterator.remove();
            }
        }
    }
    
    public void onDeletedMessages() {
    }
    
    public void onMessageReceived(final RemoteMessage remoteMessage) {
    }
    
    public void onMessageSent(final String s) {
    }
    
    public void onNewToken(final String s) {
    }
    
    public void onSendError(final String s, final Exception ex) {
    }
    
    @Override
    protected final Intent zzb(final Intent intent) {
        return zzau.zzah().zzai();
    }
    
    @Override
    public final boolean zzc(final Intent intent) {
        if ("com.google.firebase.messaging.NOTIFICATION_OPEN".equals(intent.getAction())) {
            final PendingIntent pendingIntent = (PendingIntent)intent.getParcelableExtra("pending_intent");
            if (pendingIntent != null) {
                try {
                    pendingIntent.send();
                }
                catch (PendingIntent$CanceledException ex) {
                    Log.e("FirebaseMessaging", "Notification pending intent canceled");
                }
            }
            if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                MessagingAnalytics.logNotificationOpen(intent);
            }
            return true;
        }
        return false;
    }
    
    @Override
    public final void zzd(final Intent intent) {
        final String action = intent.getAction();
        if (!"com.google.android.c2dm.intent.RECEIVE".equals(action) && !"com.google.firebase.messaging.RECEIVE_DIRECT_BOOT".equals(action)) {
            if ("com.google.firebase.messaging.NOTIFICATION_DISMISS".equals(action)) {
                if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                    MessagingAnalytics.logNotificationDismiss(intent);
                }
            }
            else {
                if ("com.google.firebase.messaging.NEW_TOKEN".equals(action)) {
                    this.onNewToken(intent.getStringExtra("token"));
                    return;
                }
                final String value = String.valueOf(intent.getAction());
                String concat;
                if (value.length() != 0) {
                    concat = "Unknown intent action: ".concat(value);
                }
                else {
                    concat = new String("Unknown intent action: ");
                }
                Log.d("FirebaseMessaging", concat);
            }
            return;
        }
        final String stringExtra = intent.getStringExtra("google.message_id");
        Object o;
        if (TextUtils.isEmpty((CharSequence)stringExtra)) {
            o = Tasks.forResult((TResult)null);
        }
        else {
            final Bundle bundle = new Bundle();
            bundle.putString("google.message_id", stringExtra);
            o = zzaa.zzc((Context)this).zza(2, bundle);
        }
        boolean b = false;
        Label_0279: {
            if (!TextUtils.isEmpty((CharSequence)stringExtra)) {
                if (FirebaseMessagingService.zzdo.contains(stringExtra)) {
                    if (Log.isLoggable("FirebaseMessaging", 3)) {
                        final String value2 = String.valueOf(stringExtra);
                        String concat2;
                        if (value2.length() != 0) {
                            concat2 = "Received duplicate message: ".concat(value2);
                        }
                        else {
                            concat2 = new String("Received duplicate message: ");
                        }
                        Log.d("FirebaseMessaging", concat2);
                    }
                    b = true;
                    break Label_0279;
                }
                if (FirebaseMessagingService.zzdo.size() >= 10) {
                    FirebaseMessagingService.zzdo.remove();
                }
                FirebaseMessagingService.zzdo.add(stringExtra);
            }
            b = false;
        }
        Label_0619: {
            if (!b) {
                String stringExtra2;
                if ((stringExtra2 = intent.getStringExtra("message_type")) == null) {
                    stringExtra2 = "gcm";
                }
                final int hashCode = stringExtra2.hashCode();
                int n = 0;
                Label_0399: {
                    if (hashCode != -2062414158) {
                        if (hashCode != 102161) {
                            if (hashCode != 814694033) {
                                if (hashCode == 814800675) {
                                    if (stringExtra2.equals("send_event")) {
                                        n = 2;
                                        break Label_0399;
                                    }
                                }
                            }
                            else if (stringExtra2.equals("send_error")) {
                                n = 3;
                                break Label_0399;
                            }
                        }
                        else if (stringExtra2.equals("gcm")) {
                            n = 0;
                            break Label_0399;
                        }
                    }
                    else if (stringExtra2.equals("deleted_messages")) {
                        n = 1;
                        break Label_0399;
                    }
                    n = -1;
                }
                if (n != 0) {
                    if (n != 1) {
                        if (n != 2) {
                            if (n != 3) {
                                final String value3 = String.valueOf(stringExtra2);
                                String concat3;
                                if (value3.length() != 0) {
                                    concat3 = "Received message with unknown type: ".concat(value3);
                                }
                                else {
                                    concat3 = new String("Received message with unknown type: ");
                                }
                                Log.w("FirebaseMessaging", concat3);
                            }
                            else {
                                String s;
                                if ((s = intent.getStringExtra("google.message_id")) == null) {
                                    s = intent.getStringExtra("message_id");
                                }
                                this.onSendError(s, new SendException(intent.getStringExtra("error")));
                            }
                        }
                        else {
                            this.onMessageSent(intent.getStringExtra("google.message_id"));
                        }
                    }
                    else {
                        this.onDeletedMessages();
                    }
                }
                else {
                    if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                        MessagingAnalytics.logNotificationReceived(intent);
                    }
                    Bundle extras;
                    if ((extras = intent.getExtras()) == null) {
                        extras = new Bundle();
                    }
                    extras.remove("android.support.content.wakelockid");
                    if (zza.zzf(extras)) {
                        if (new zza((Context)this).zzh(extras)) {
                            break Label_0619;
                        }
                        if (MessagingAnalytics.shouldUploadMetrics(intent)) {
                            MessagingAnalytics.logNotificationForeground(intent);
                        }
                    }
                    this.onMessageReceived(new RemoteMessage(extras));
                }
            }
            try {
                Tasks.await((Task<Object>)o, 1L, TimeUnit.SECONDS);
            }
            catch (ExecutionException | InterruptedException | TimeoutException ex) {
                final Object o2;
                final String value4 = String.valueOf(o2);
                final StringBuilder sb = new StringBuilder(String.valueOf(value4).length() + 20);
                sb.append("Message ack failed: ");
                sb.append(value4);
                Log.w("FirebaseMessaging", sb.toString());
            }
        }
    }
}
