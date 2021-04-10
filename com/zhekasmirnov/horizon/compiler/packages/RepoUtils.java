package com.zhekasmirnov.horizon.compiler.packages;

import android.os.*;
import java.util.*;
import android.support.annotation.*;

public class RepoUtils
{
    public static String CPU_API;
    private static String NDK_ARCH;
    private static int NDK_VERSION;
    
    public static void setVersion() {
        final int sdkVersion = Build.VERSION.SDK_INT;
        final int ndkVersion = 14;
        if (sdkVersion <= 24) {}
        String ndkArch;
        String cpuAbi;
        if (Build.CPU_ABI.startsWith("arm")) {
            ndkArch = "armel";
            cpuAbi = "armeabi-v7a";
        }
        else if (Build.CPU_ABI.startsWith("mips")) {
            ndkArch = "mipsel";
            cpuAbi = "mips";
        }
        else {
            ndkArch = "i686";
            cpuAbi = "x86";
        }
        RepoUtils.CPU_API = cpuAbi;
        RepoUtils.NDK_ARCH = ndkArch;
        RepoUtils.NDK_VERSION = ndkVersion;
    }
    
    public static boolean isContainsPackage(final List<PackageInfo> repo, final String pkg) {
        for (final PackageInfo packageInfo : repo) {
            if (packageInfo.getName().equals(pkg)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isContainsPackage(final List<PackageInfo> repo, final String pkg, final String version) {
        for (final PackageInfo packageInfo : repo) {
            if (packageInfo.getName().equals(pkg) && packageInfo.getVersion().equals(version)) {
                return true;
            }
        }
        return false;
    }
    
    @Nullable
    public static PackageInfo getPackageByName(final List<PackageInfo> repo, final String pkg) {
        for (final PackageInfo packageInfo : repo) {
            if (packageInfo.getName().equals(pkg)) {
                return packageInfo;
            }
        }
        return null;
    }
    
    public static String replaceMacro(String str) {
        if (str != null) {
            str = str.replaceAll("\\$\\{HOSTARCH\\}", RepoUtils.NDK_ARCH);
            str = str.replaceAll("\\$\\{HOSTNDKARCH\\}", RepoUtils.NDK_ARCH);
            str = str.replaceAll("\\$\\{HOSTNDKVERSION\\}", String.valueOf(RepoUtils.NDK_VERSION));
        }
        return str;
    }
    
    @NonNull
    public static List<PackageInfo> checkingForUpdates(final List<PackageInfo> availablePackages, final List<PackageInfo> installedPackages) {
        final List<PackageInfo> list = new ArrayList<PackageInfo>();
        for (final PackageInfo installedPkg : installedPackages) {
            for (final PackageInfo pkg : availablePackages) {
                if (installedPkg.getName().equals(pkg.getName())) {
                    if (!installedPkg.getVersion().equals(pkg.getVersion())) {
                        list.add(pkg);
                        break;
                    }
                    break;
                }
            }
        }
        return list;
    }
}
