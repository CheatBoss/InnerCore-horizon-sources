package com.zhekasmirnov.innercore.core;

import android.content.*;
import android.content.pm.*;

public class RedirectPackageManager extends WrappedPackageManager
{
    protected String nativeLibraryDir;
    
    public RedirectPackageManager(final PackageManager packageManager, final String nativeLibraryDir) {
        super(packageManager);
        this.nativeLibraryDir = nativeLibraryDir;
    }
    
    @Override
    public ActivityInfo getActivityInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        final ActivityInfo activityInfo = this.wrapped.getActivityInfo(componentName, n);
        activityInfo.applicationInfo.nativeLibraryDir = this.nativeLibraryDir;
        return activityInfo;
    }
}
