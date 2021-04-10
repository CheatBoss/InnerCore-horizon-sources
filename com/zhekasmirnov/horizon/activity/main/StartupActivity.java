package com.zhekasmirnov.horizon.activity.main;

import android.support.v7.app.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import android.content.*;
import android.app.*;
import android.support.v4.content.*;
import android.support.v4.app.*;
import java.util.*;
import android.widget.*;
import android.support.annotation.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import android.os.*;
import com.zhekasmirnov.horizon.activity.service.*;

public class StartupActivity extends AppCompatActivity
{
    private final TaskManager taskManager;
    private static final String[] REQUIRED_PERMISSIONS;
    private final HashMap<String, PermissionResult> permissionResults;
    
    public StartupActivity() {
        this.taskManager = new TaskManager();
        this.permissionResults = new HashMap<String, PermissionResult>();
    }
    
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(2131427363);
        final String launchPackDirectory = this.getIntent().getStringExtra("pack_directory");
        if (HorizonApplication.getLock("startup").tryLock()) {
            this.taskManager.addTask(new Task() {
                @Override
                public Object getLock() {
                    return "initialization";
                }
                
                @Override
                public void run() {
                    StartupActivity.this.initialize();
                }
            }, new TaskWatcher.Callback() {
                @Override
                public void complete(final Task task) {
                    StartupActivity.this.startActivity(new Intent((Context)StartupActivity.this, (Class)PackSelectorActivity.class).putExtra("pack_directory", launchPackDirectory));
                    for (final Activity activity : HorizonApplication.getActivityStack()) {
                        if (!(activity instanceof PackSelectorActivity)) {
                            activity.finish();
                        }
                    }
                }
            });
            HorizonApplication.initializeFirebase((Context)this);
            HorizonApplication.sendFirebaseEvent("horizon_startup", new Bundle());
        }
    }
    
    private void initPermissionResults() {
        synchronized (this.permissionResults) {
            for (final String permission : StartupActivity.REQUIRED_PERMISSIONS) {
                this.permissionResults.put(permission, (ContextCompat.checkSelfPermission((Context)this, permission) == 0) ? PermissionResult.GRANTED : PermissionResult.DENIED);
            }
        }
    }
    
    private PermissionResult getAllPermissionsResult() {
        synchronized (this.permissionResults) {
            for (final PermissionResult result : this.permissionResults.values()) {
                if (result == PermissionResult.REJECTED) {
                    return result;
                }
                if (result == PermissionResult.DENIED) {
                    return result;
                }
            }
        }
        return PermissionResult.GRANTED;
    }
    
    private void requestDeniedPermissions() {
        final List<String> permissionsToRequest = new ArrayList<String>();
        synchronized (this.permissionResults) {
            for (final String permission : this.permissionResults.keySet()) {
                if (this.permissionResults.get(permission) != PermissionResult.GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }
            for (final String permission : permissionsToRequest) {
                this.permissionResults.put(permission, PermissionResult.DENIED);
            }
        }
        ActivityCompat.requestPermissions((Activity)this, (String[])permissionsToRequest.toArray(new String[permissionsToRequest.size()]), 0);
    }
    
    private void requestPermissions() {
        this.initPermissionResults();
        PermissionResult result = this.getAllPermissionsResult();
        if (result != PermissionResult.GRANTED) {
            this.requestDeniedPermissions();
            while ((result = this.getAllPermissionsResult()) != PermissionResult.GRANTED) {
                if (result == PermissionResult.REJECTED) {
                    this.requestDeniedPermissions();
                    this.runOnUiThread((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText((Context)StartupActivity.this, 2131623975, 1).show();
                        }
                    });
                }
                Thread.yield();
            }
        }
    }
    
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        synchronized (this.permissionResults) {
            for (int index = 0; index < permissions.length; ++index) {
                this.permissionResults.put(permissions[index], (grantResults[index] == 0) ? PermissionResult.GRANTED : PermissionResult.REJECTED);
            }
        }
    }
    
    private void exit() {
        this.finish();
    }
    
    private void initialize() {
        HorizonLibrary.include();
        LocaleUtils.updateDefaultLanguage(this.getResources());
        AdsManager.getInstance().runInitialization();
        AdsManager.getInstance().awaitAndLoadAds(1, "interstitial", "interstitial_video");
        if (Build.VERSION.SDK_INT >= 21) {
            NotificationService.schedule((Context)this);
        }
        this.requestPermissions();
    }
    
    static {
        REQUIRED_PERMISSIONS = new String[] { "android.permission.WRITE_EXTERNAL_STORAGE" };
    }
    
    private enum PermissionResult
    {
        GRANTED, 
        DENIED, 
        REJECTED;
    }
}
