package com.zhekasmirnov.horizon.launcher.env;

import java.util.*;
import android.graphics.drawable.*;
import android.os.*;
import android.graphics.*;
import android.content.res.*;
import android.content.*;
import android.content.pm.*;

public class WrappedPackageManager extends PackageManager
{
    public final PackageManager mBase;
    
    public WrappedPackageManager(final PackageManager mBase) {
        this.mBase = mBase;
    }
    
    public PackageInfo getPackageInfo(final String packageName, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getPackageInfo(packageName, flags);
    }
    
    public PackageInfo getPackageInfo(final VersionedPackage versionedPackage, final int flags) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mBase.getPackageInfo(versionedPackage, flags);
        }
        return null;
    }
    
    public String[] currentToCanonicalPackageNames(final String[] names) {
        return this.mBase.currentToCanonicalPackageNames(names);
    }
    
    public String[] canonicalToCurrentPackageNames(final String[] names) {
        return this.mBase.canonicalToCurrentPackageNames(names);
    }
    
    public Intent getLaunchIntentForPackage(final String packageName) {
        return this.mBase.getLaunchIntentForPackage(packageName);
    }
    
    public Intent getLeanbackLaunchIntentForPackage(final String packageName) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mBase.getLeanbackLaunchIntentForPackage(packageName);
        }
        return null;
    }
    
    public int[] getPackageGids(final String packageName) throws PackageManager.NameNotFoundException {
        return this.mBase.getPackageGids(packageName);
    }
    
    public int[] getPackageGids(final String packageName, final int flags) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mBase.getPackageGids(packageName, flags);
        }
        return null;
    }
    
    public int getPackageUid(final String packageName, final int flags) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 24) {
            return this.mBase.getPackageUid(packageName, flags);
        }
        return 0;
    }
    
    public PermissionInfo getPermissionInfo(final String name, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getPermissionInfo(name, flags);
    }
    
    public List<PermissionInfo> queryPermissionsByGroup(final String group, final int flags) throws PackageManager.NameNotFoundException {
        return (List<PermissionInfo>)this.mBase.queryPermissionsByGroup(group, flags);
    }
    
    public PermissionGroupInfo getPermissionGroupInfo(final String name, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getPermissionGroupInfo(name, flags);
    }
    
    public List<PermissionGroupInfo> getAllPermissionGroups(final int flags) {
        return (List<PermissionGroupInfo>)this.mBase.getAllPermissionGroups(flags);
    }
    
    public ApplicationInfo getApplicationInfo(final String packageName, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getApplicationInfo(packageName, flags);
    }
    
    public ActivityInfo getActivityInfo(final ComponentName component, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getActivityInfo(component, flags);
    }
    
    public ActivityInfo getReceiverInfo(final ComponentName component, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getReceiverInfo(component, flags);
    }
    
    public ServiceInfo getServiceInfo(final ComponentName component, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getServiceInfo(component, flags);
    }
    
    public ProviderInfo getProviderInfo(final ComponentName component, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getProviderInfo(component, flags);
    }
    
    public List<PackageInfo> getInstalledPackages(final int flags) {
        return (List<PackageInfo>)this.mBase.getInstalledPackages(flags);
    }
    
    public List<PackageInfo> getPackagesHoldingPermissions(final String[] permissions, final int flags) {
        if (Build.VERSION.SDK_INT >= 18) {
            return (List<PackageInfo>)this.mBase.getPackagesHoldingPermissions(permissions, flags);
        }
        return null;
    }
    
    public int checkPermission(final String permName, final String pkgName) {
        return this.mBase.checkPermission(permName, pkgName);
    }
    
    public boolean isPermissionRevokedByPolicy(final String permName, final String pkgName) {
        return Build.VERSION.SDK_INT >= 23 && this.mBase.isPermissionRevokedByPolicy(permName, pkgName);
    }
    
    public boolean addPermission(final PermissionInfo info) {
        return this.mBase.addPermission(info);
    }
    
    public boolean addPermissionAsync(final PermissionInfo info) {
        return this.mBase.addPermissionAsync(info);
    }
    
    public void removePermission(final String name) {
        this.mBase.removePermission(name);
    }
    
    public int checkSignatures(final String pkg1, final String pkg2) {
        return this.mBase.checkSignatures(pkg1, pkg2);
    }
    
    public int checkSignatures(final int uid1, final int uid2) {
        return this.mBase.checkSignatures(uid1, uid2);
    }
    
    public String[] getPackagesForUid(final int uid) {
        return this.mBase.getPackagesForUid(uid);
    }
    
    public String getNameForUid(final int uid) {
        return this.mBase.getNameForUid(uid);
    }
    
    public List<ApplicationInfo> getInstalledApplications(final int flags) {
        return (List<ApplicationInfo>)this.mBase.getInstalledApplications(flags);
    }
    
    public boolean isInstantApp() {
        return Build.VERSION.SDK_INT >= 26 && this.mBase.isInstantApp();
    }
    
    public boolean isInstantApp(final String packageName) {
        return Build.VERSION.SDK_INT >= 26 && this.mBase.isInstantApp(packageName);
    }
    
    public int getInstantAppCookieMaxBytes() {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mBase.getInstantAppCookieMaxBytes();
        }
        return 0;
    }
    
    public byte[] getInstantAppCookie() {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mBase.getInstantAppCookie();
        }
        return null;
    }
    
    public void clearInstantAppCookie() {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBase.clearInstantAppCookie();
        }
    }
    
    public void updateInstantAppCookie(final byte[] cookie) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBase.updateInstantAppCookie(cookie);
        }
    }
    
    public String[] getSystemSharedLibraryNames() {
        return this.mBase.getSystemSharedLibraryNames();
    }
    
    public List<SharedLibraryInfo> getSharedLibraries(final int flags) {
        if (Build.VERSION.SDK_INT >= 26) {
            return (List<SharedLibraryInfo>)this.mBase.getSharedLibraries(flags);
        }
        return null;
    }
    
    public ChangedPackages getChangedPackages(final int sequenceNumber) {
        if (Build.VERSION.SDK_INT >= 26) {
            return this.mBase.getChangedPackages(sequenceNumber);
        }
        return null;
    }
    
    public FeatureInfo[] getSystemAvailableFeatures() {
        return this.mBase.getSystemAvailableFeatures();
    }
    
    public boolean hasSystemFeature(final String name) {
        return this.mBase.hasSystemFeature(name);
    }
    
    public boolean hasSystemFeature(final String name, final int version) {
        return Build.VERSION.SDK_INT >= 24 && this.mBase.hasSystemFeature(name, version);
    }
    
    public ResolveInfo resolveActivity(final Intent intent, final int flags) {
        return this.mBase.resolveActivity(intent, flags);
    }
    
    public List<ResolveInfo> queryIntentActivities(final Intent intent, final int flags) {
        return (List<ResolveInfo>)this.mBase.queryIntentActivities(intent, flags);
    }
    
    public List<ResolveInfo> queryIntentActivityOptions(final ComponentName caller, final Intent[] specifics, final Intent intent, final int flags) {
        return (List<ResolveInfo>)this.mBase.queryIntentActivityOptions(caller, specifics, intent, flags);
    }
    
    public List<ResolveInfo> queryBroadcastReceivers(final Intent intent, final int flags) {
        return (List<ResolveInfo>)this.mBase.queryBroadcastReceivers(intent, flags);
    }
    
    public ResolveInfo resolveService(final Intent intent, final int flags) {
        return this.mBase.resolveService(intent, flags);
    }
    
    public List<ResolveInfo> queryIntentServices(final Intent intent, final int flags) {
        return (List<ResolveInfo>)this.mBase.queryIntentServices(intent, flags);
    }
    
    public List<ResolveInfo> queryIntentContentProviders(final Intent intent, final int flags) {
        if (Build.VERSION.SDK_INT >= 19) {
            return (List<ResolveInfo>)this.mBase.queryIntentContentProviders(intent, flags);
        }
        return null;
    }
    
    public ProviderInfo resolveContentProvider(final String name, final int flags) {
        return this.mBase.resolveContentProvider(name, flags);
    }
    
    public List<ProviderInfo> queryContentProviders(final String processName, final int uid, final int flags) {
        return (List<ProviderInfo>)this.mBase.queryContentProviders(processName, uid, flags);
    }
    
    public InstrumentationInfo getInstrumentationInfo(final ComponentName className, final int flags) throws PackageManager.NameNotFoundException {
        return this.mBase.getInstrumentationInfo(className, flags);
    }
    
    public List<InstrumentationInfo> queryInstrumentation(final String targetPackage, final int flags) {
        return (List<InstrumentationInfo>)this.mBase.queryInstrumentation(targetPackage, flags);
    }
    
    public Drawable getDrawable(final String packageName, final int resid, final ApplicationInfo appInfo) {
        return this.mBase.getDrawable(packageName, resid, appInfo);
    }
    
    public Drawable getActivityIcon(final ComponentName activityName) throws PackageManager.NameNotFoundException {
        return this.mBase.getActivityIcon(activityName);
    }
    
    public Drawable getActivityIcon(final Intent intent) throws PackageManager.NameNotFoundException {
        return this.mBase.getActivityIcon(intent);
    }
    
    public Drawable getActivityBanner(final ComponentName activityName) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 20) {
            return this.mBase.getActivityBanner(activityName);
        }
        return null;
    }
    
    public Drawable getActivityBanner(final Intent intent) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 20) {
            return this.mBase.getActivityBanner(intent);
        }
        return null;
    }
    
    public Drawable getDefaultActivityIcon() {
        return this.mBase.getDefaultActivityIcon();
    }
    
    public Drawable getApplicationIcon(final ApplicationInfo info) {
        return this.mBase.getApplicationIcon(info);
    }
    
    public Drawable getApplicationIcon(final String packageName) throws PackageManager.NameNotFoundException {
        return this.mBase.getApplicationIcon(packageName);
    }
    
    public Drawable getApplicationBanner(final ApplicationInfo info) {
        if (Build.VERSION.SDK_INT >= 20) {
            return this.mBase.getApplicationBanner(info);
        }
        return null;
    }
    
    public Drawable getApplicationBanner(final String packageName) throws PackageManager.NameNotFoundException {
        if (Build.VERSION.SDK_INT >= 20) {
            return this.mBase.getApplicationBanner(packageName);
        }
        return null;
    }
    
    public Drawable getActivityLogo(final ComponentName activityName) throws PackageManager.NameNotFoundException {
        return this.mBase.getActivityLogo(activityName);
    }
    
    public Drawable getActivityLogo(final Intent intent) throws PackageManager.NameNotFoundException {
        return this.mBase.getActivityLogo(intent);
    }
    
    public Drawable getApplicationLogo(final ApplicationInfo info) {
        return this.mBase.getApplicationLogo(info);
    }
    
    public Drawable getApplicationLogo(final String packageName) throws PackageManager.NameNotFoundException {
        return this.mBase.getApplicationLogo(packageName);
    }
    
    public Drawable getUserBadgedIcon(final Drawable icon, final UserHandle user) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mBase.getUserBadgedIcon(icon, user);
        }
        return null;
    }
    
    public Drawable getUserBadgedDrawableForDensity(final Drawable drawable, final UserHandle user, final Rect badgeLocation, final int badgeDensity) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mBase.getUserBadgedDrawableForDensity(drawable, user, badgeLocation, badgeDensity);
        }
        return null;
    }
    
    public CharSequence getUserBadgedLabel(final CharSequence label, final UserHandle user) {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mBase.getUserBadgedLabel(label, user);
        }
        return null;
    }
    
    public CharSequence getText(final String packageName, final int resid, final ApplicationInfo appInfo) {
        return this.mBase.getText(packageName, resid, appInfo);
    }
    
    public XmlResourceParser getXml(final String packageName, final int resid, final ApplicationInfo appInfo) {
        return this.mBase.getXml(packageName, resid, appInfo);
    }
    
    public CharSequence getApplicationLabel(final ApplicationInfo info) {
        return this.mBase.getApplicationLabel(info);
    }
    
    public Resources getResourcesForActivity(final ComponentName activityName) throws PackageManager.NameNotFoundException {
        return this.mBase.getResourcesForActivity(activityName);
    }
    
    public Resources getResourcesForApplication(final ApplicationInfo app) throws PackageManager.NameNotFoundException {
        return this.mBase.getResourcesForApplication(app);
    }
    
    public Resources getResourcesForApplication(final String appPackageName) throws PackageManager.NameNotFoundException {
        return this.mBase.getResourcesForApplication(appPackageName);
    }
    
    public void verifyPendingInstall(final int id, final int verificationCode) {
        this.mBase.verifyPendingInstall(id, verificationCode);
    }
    
    public void extendVerificationTimeout(final int id, final int verificationCodeAtTimeout, final long millisecondsToDelay) {
        this.mBase.extendVerificationTimeout(id, verificationCodeAtTimeout, millisecondsToDelay);
    }
    
    public void setInstallerPackageName(final String targetPackage, final String installerPackageName) {
        this.mBase.setInstallerPackageName(targetPackage, installerPackageName);
    }
    
    public String getInstallerPackageName(final String packageName) {
        return this.mBase.getInstallerPackageName(packageName);
    }
    
    public void addPackageToPreferred(final String packageName) {
        this.mBase.addPackageToPreferred(packageName);
    }
    
    public void removePackageFromPreferred(final String packageName) {
        this.mBase.removePackageFromPreferred(packageName);
    }
    
    public List<PackageInfo> getPreferredPackages(final int flags) {
        return (List<PackageInfo>)this.mBase.getPreferredPackages(flags);
    }
    
    public void addPreferredActivity(final IntentFilter filter, final int match, final ComponentName[] set, final ComponentName activity) {
        this.mBase.addPreferredActivity(filter, match, set, activity);
    }
    
    public void clearPackagePreferredActivities(final String packageName) {
        this.mBase.clearPackagePreferredActivities(packageName);
    }
    
    public int getPreferredActivities(final List<IntentFilter> outFilters, final List<ComponentName> outActivities, final String packageName) {
        return this.mBase.getPreferredActivities((List)outFilters, (List)outActivities, packageName);
    }
    
    public void setComponentEnabledSetting(final ComponentName componentName, final int newState, final int flags) {
        this.mBase.setComponentEnabledSetting(componentName, newState, flags);
    }
    
    public int getComponentEnabledSetting(final ComponentName componentName) {
        return this.mBase.getComponentEnabledSetting(componentName);
    }
    
    public void setApplicationEnabledSetting(final String packageName, final int newState, final int flags) {
        this.mBase.setApplicationEnabledSetting(packageName, newState, flags);
    }
    
    public int getApplicationEnabledSetting(final String packageName) {
        return this.mBase.getApplicationEnabledSetting(packageName);
    }
    
    public boolean isSafeMode() {
        return this.mBase.isSafeMode();
    }
    
    public void setApplicationCategoryHint(final String packageName, final int categoryHint) {
        if (Build.VERSION.SDK_INT >= 26) {
            this.mBase.setApplicationCategoryHint(packageName, categoryHint);
        }
    }
    
    public PackageInstaller getPackageInstaller() {
        if (Build.VERSION.SDK_INT >= 21) {
            return this.mBase.getPackageInstaller();
        }
        return null;
    }
    
    public boolean canRequestPackageInstalls() {
        return Build.VERSION.SDK_INT >= 26 && this.mBase.canRequestPackageInstalls();
    }
}
