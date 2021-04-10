package com.zhekasmirnov.horizon.launcher.env;

import java.io.*;
import android.content.*;
import android.content.pm.*;

public class LibraryDirectoryPackageManager extends WrappedPackageManager
{
    public final File libraryDir;
    
    public LibraryDirectoryPackageManager(final PackageManager mBase, final File dir) {
        super(mBase);
        this.libraryDir = dir;
    }
    
    @Override
    public ActivityInfo getActivityInfo(final ComponentName component, final int flags) throws PackageManager.NameNotFoundException {
        final ActivityInfo info = super.getActivityInfo(component, flags);
        info.applicationInfo.nativeLibraryDir = this.libraryDir.getAbsolutePath();
        return info;
    }
}
