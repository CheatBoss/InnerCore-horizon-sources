package com.zhekasmirnov.innercore.core.handle;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.content.*;

public class CrashHandlerWrapActivity extends Activity
{
    private static Intent targetIntent;
    
    private void setCrashText(final String s) {
        this.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
            }
        });
    }
    
    public static void setTargetIntent(final Intent targetIntent) {
        CrashHandlerWrapActivity.targetIntent = targetIntent;
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (CrashHandlerWrapActivity.targetIntent != null) {
            this.startActivity(CrashHandlerWrapActivity.targetIntent);
            CrashHandlerWrapActivity.targetIntent = null;
            return;
        }
        if (CrashBacktraceFileHelper.checkLock()) {
            Toast.makeText((Context)this, (CharSequence)"Seems, that Inner Core was crashed inside native code, crash report will be collected and sent for future analysis.", 1).show();
            this.setCrashText(CrashHandler.buildAndSendLatestCrashReport());
            return;
        }
        this.finish();
    }
}
