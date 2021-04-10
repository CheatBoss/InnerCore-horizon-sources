package com.zhekasmirnov.horizon;

import android.app.*;
import android.os.*;
import android.content.*;

public class RelaunchInstrumentation extends Instrumentation
{
    public void onCreate(final Bundle arguments) {
        super.onCreate(arguments);
        if (arguments.getByte("skipLaunch") == 0) {
            this.reLaunch(arguments);
        }
    }
    
    private void reLaunch(final Bundle arguments) {
        final Context context = this.getContext();
        System.out.println("RELAUNCH INSTRUMENTATION: " + context.getPackageName() + " " + context);
        Intent launchIntent = (Intent)arguments.getParcelable("launchIntent");
        if (launchIntent == null) {
            launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        }
        LaunchUtils.createLock(context);
        launchIntent.putExtra("fromInstrumentation", true);
        launchIntent.putExtra("receiveLock", true);
        launchIntent.setPackage(context.getPackageName());
        launchIntent.addFlags(335577088);
        context.startActivity(launchIntent);
    }
}
