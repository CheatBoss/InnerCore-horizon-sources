package com.zhekasmirnov.horizon.activity.service;

import android.support.annotation.*;
import android.util.*;
import android.os.*;
import android.app.job.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.io.*;
import com.zhekasmirnov.horizon.launcher.*;
import android.support.v4.app.*;
import java.util.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import org.json.*;
import android.content.*;
import com.zhekasmirnov.horizon.*;
import android.app.*;
import com.zhekasmirnov.horizon.activity.main.*;

@RequiresApi(api = 21)
public class NotificationService extends JobService
{
    public static final String UPDATE_NOTIFICATION_CHANNEL_ID = "HORIZON_NOTIFICATION_UPDATES";
    public static final String ACTION_UPDATE_CLICK = "update_click";
    public static final String ACTION_UPDATE_REJECTED = "update_rejected";
    
    public static void schedule(final Context context) {
        final JobScheduler jobScheduler = (JobScheduler)context.getSystemService("jobscheduler");
        if (jobScheduler != null) {
            final ComponentName serviceName = new ComponentName(context, (Class)NotificationService.class);
            final JobInfo jobInfo = new JobInfo.Builder(0, serviceName).setRequiredNetworkType(1).setMinimumLatency(1800000L).setOverrideDeadline(21600000L).build();
            final int result = jobScheduler.schedule(jobInfo);
            if (result != 1) {
                Log.e("ERROR", "failed to schedule update job " + result);
            }
            else {
                Log.d("DEBUG", "scheduled update job");
            }
        }
    }
    
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            final CharSequence name = this.getString(2131624119);
            final String description = this.getString(2131624117);
            final int importance = 3;
            final NotificationChannel channel = new NotificationChannel("HORIZON_NOTIFICATION_UPDATES", name, importance);
            channel.setDescription(description);
            final NotificationManager notificationManager = (NotificationManager)this.getSystemService((Class)NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    
    public boolean onStartJob(final JobParameters params) {
        schedule((Context)this);
        this.createNotificationChannel();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final PackStorage packStorage = new PackStorage(null, new File(Environment.getExternalHorizonDirectory(), "packs"));
                final PackRepository packRepo = new ExternalPackRepository("https://gitlab.com/zhekasmirnov/horizon-cloud-config/raw/master/packs.json");
                final NotificationManagerCompat notificationManager = NotificationManagerCompat.from((Context)NotificationService.this);
                int index = 0;
                packRepo.fetch();
                packStorage.fetchLocationsFromRepo(packRepo);
                for (final PackHolder holder : packStorage.reloadAll()) {
                    holder.refreshUpdateInfoNow();
                    if (holder.isUpdateAvailable()) {
                        final PackManifest manifest = holder.getManifest();
                        final String customName = holder.packDirectory.getCustomName();
                        final String packDir = holder.packDirectory.directory.getAbsolutePath();
                        final int newestVersion = holder.packDirectory.getNewestVersionCode();
                        final String notificationKey = packDir + "#" + newestVersion;
                        if (manifest == null || !NotificationService.this.isNotificationAllowedToPass((Context)NotificationService.this, notificationKey)) {
                            continue;
                        }
                        final PackManifest localManifest = holder.packDirectory.getLocalManifest();
                        final PackManifest externalManifest = holder.packDirectory.getExternalManifest();
                        final NotificationCompat.Builder builder = new NotificationCompat.Builder((Context)NotificationService.this, "HORIZON_NOTIFICATION_UPDATES").setSmallIcon(2131165304).setContentTitle((CharSequence)((customName != null) ? customName : manifest.pack)).setContentText((CharSequence)NotificationService.this.getString(2131624120, new Object[] { (localManifest != null) ? localManifest.getPackVersionString() : "?", (externalManifest != null) ? externalManifest.getPackVersionString() : "?" })).setPriority(0).setAutoCancel(true).setGroup("com.zheka.horizon.PACK_UPDATES").setDeleteIntent(NotificationIntentReceiver.makePendingIntent((Context)NotificationService.this, NotificationIntentReceiver.makeIntent((Context)NotificationService.this, "update_rejected").putExtra("version", newestVersion).putExtra("pack_directory", packDir).putExtra("notification_key", notificationKey))).setContentIntent(NotificationIntentReceiver.makePendingIntent((Context)NotificationService.this, NotificationIntentReceiver.makeIntent((Context)NotificationService.this, "update_click").putExtra("version", newestVersion).putExtra("pack_directory", packDir).putExtra("notification_key", notificationKey)));
                        notificationManager.notify(index++, builder.build());
                    }
                }
                if (index > 0) {
                    final NotificationCompat.Builder builder2 = new NotificationCompat.Builder((Context)NotificationService.this, "HORIZON_NOTIFICATION_UPDATES").setSmallIcon(2131165304).setContentTitle((CharSequence)NotificationService.this.getString(2131624119)).setContentText((CharSequence)NotificationService.this.getString(2131624118, new Object[] { index })).setPriority(0).setAutoCancel(true).setGroup("com.zheka.horizon.PACK_UPDATES").setGroupSummary(true);
                    notificationManager.notify(256, builder2.build());
                }
            }
        }).start();
        return false;
    }
    
    public boolean onStopJob(final JobParameters params) {
        return false;
    }
    
    public boolean isNotificationAllowedToPass(final Context context, final String uniqueKey) {
        final SharedPreferences prefs = context.getSharedPreferences("notifications", 0);
        final String timingsJsonString = prefs.getString("reject_timings", "{}");
        JSONObject timingsJson = new JSONObject();
        try {
            timingsJson = new JSONObject(timingsJsonString);
        }
        catch (JSONException ex) {}
        final long time = timingsJson.optLong(uniqueKey, 0L);
        return time != -1L && (time == 0L || time < System.currentTimeMillis());
    }
    
    public static void disableNotification(final Context context, final String uniqueKey, final long period) {
        final SharedPreferences prefs = context.getSharedPreferences("notifications", 0);
        final String timingsJsonString = prefs.getString("reject_timings", "{}");
        JSONObject timingsJson = new JSONObject();
        try {
            timingsJson = new JSONObject(timingsJsonString);
        }
        catch (JSONException ex) {}
        final long time = timingsJson.optLong(uniqueKey, 0L);
        if (time != -1L) {
            try {
                if (period == -1L) {
                    timingsJson.put(uniqueKey, -1);
                }
                else if (period + System.currentTimeMillis() > time) {
                    timingsJson.put(uniqueKey, System.currentTimeMillis() + period);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        prefs.edit().putString("reject_timings", timingsJson.toString()).apply();
    }
    
    static {
        NotificationIntentReceiver.addListener("update_click", new NotificationIntentReceiver.IActionListener() {
            @Override
            public void onAction(final Context context, final Intent intent) {
                for (final Activity activity : HorizonApplication.getActivityStack()) {
                    activity.finish();
                }
                context.startActivity(new Intent(context, (Class)StartupWrapperActivity.class).addFlags(335544320).putExtra("pack_directory", intent.getStringExtra("pack_directory")));
                NotificationService.disableNotification(context, intent.getStringExtra("notification_key"), 7776000000L);
            }
        });
        NotificationIntentReceiver.addListener("update_rejected", new NotificationIntentReceiver.IActionListener() {
            @Override
            public void onAction(final Context context, final Intent intent) {
                NotificationService.disableNotification(context, intent.getStringExtra("notification_key"), 7776000000L);
            }
        });
    }
}
