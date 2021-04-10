package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import android.os.*;
import android.content.*;
import com.zhekasmirnov.horizon.activity.util.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.app.*;

public class StartupWrapperActivity extends AppCompatActivity
{
    private boolean isWaitingForPermissionOverlay;
    private boolean isPausedByPermissionOverlay;
    
    public StartupWrapperActivity() {
        this.isWaitingForPermissionOverlay = false;
        this.isPausedByPermissionOverlay = false;
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131427364);
        final Intent intent = this.getIntent();
        final boolean launchLock = LaunchUtils.receiveLock((Context)this);
        boolean askForPermission = false;
        boolean lastLaunchFailed = false;
        final SharedPreferences prefs = this.getSharedPreferences("launch_wrapper", 0);
        if (MIUIPermissionHelper.isMIUI() && !prefs.getBoolean("permissionAsked", false)) {
            askForPermission = true;
        }
        if (launchLock && !intent.getBooleanExtra("receiveLock", false)) {
            lastLaunchFailed = true;
            if (prefs.getInt("askPermissionAttempts", 0) < 5) {
                askForPermission = true;
            }
        }
        if (askForPermission) {
            final boolean _lastLaunchFailed = lastLaunchFailed;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    prefs.edit().putBoolean("permissionAsked", true).putInt("askPermissionAttempts", prefs.getInt("askPermissionAttempts", 0) + 1).apply();
                    if (DialogHelper.awaitDecision(2131624097, _lastLaunchFailed ? 2131624099 : 2131624098, 2131624019, 17039360)) {
                        StartupWrapperActivity.this.isWaitingForPermissionOverlay = true;
                        try {
                            MIUIPermissionHelper.jumpToPermissionsSettings((Context)StartupWrapperActivity.this);
                            return;
                        }
                        catch (Throwable err) {
                            err.printStackTrace();
                        }
                    }
                    StartupWrapperActivity.this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            StartupWrapperActivity.this.prepareEnvironmentAndRestartIfRequired(intent);
                        }
                    });
                }
            }).start();
        }
        else {
            this.prepareEnvironmentAndRestartIfRequired(intent);
        }
    }
    
    private boolean tryPrepareEnvironment() {
        try {
            HorizonLibrary.include();
            return true;
        }
        catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private void prepareEnvironmentAndRestartIfRequired(final Intent intent) {
        intent.putExtra("launchAttempts", intent.getIntExtra("launchAttempts", 0) + 1);
        try {
            HorizonLibrary.include();
            Logger.debug("LAUNCH-WRAPPER", "initialized horizon environment in " + intent.getIntExtra("launchAttempts", 0) + " attempts");
            this.startActivity(new Intent((Context)this, (Class)StartupActivity.class).putExtras(intent));
        }
        catch (UnsatisfiedLinkError e) {
            LaunchUtils.forceRestartIntoDifferentAbi((Activity)this, intent);
        }
        this.finish();
    }
    
    protected void onPause() {
        super.onPause();
        System.out.println("horizon launch wrapper: on pause");
        if (this.isWaitingForPermissionOverlay) {
            this.isPausedByPermissionOverlay = true;
        }
    }
    
    protected void onResume() {
        super.onResume();
        System.out.println("horizon launch wrapper: on resume");
        if (this.isPausedByPermissionOverlay) {
            this.isPausedByPermissionOverlay = false;
            this.prepareEnvironmentAndRestartIfRequired(this.getIntent());
        }
    }
}
