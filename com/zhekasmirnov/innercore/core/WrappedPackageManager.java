package com.zhekasmirnov.innercore.core;

import android.graphics.drawable.*;
import android.content.*;
import java.util.*;
import android.support.annotation.*;
import android.os.*;
import android.graphics.*;
import android.content.res.*;
import android.content.pm.*;

public class WrappedPackageManager extends PackageManager
{
    protected PackageManager wrapped;
    
    public WrappedPackageManager(final PackageManager wrapped) {
        this.wrapped = wrapped;
    }
    
    public void addPackageToPreferred(final String s) {
        this.wrapped.addPackageToPreferred(s);
    }
    
    public boolean addPermission(final PermissionInfo permissionInfo) {
        return this.wrapped.addPermission(permissionInfo);
    }
    
    public boolean addPermissionAsync(final PermissionInfo permissionInfo) {
        return this.wrapped.addPermissionAsync(permissionInfo);
    }
    
    public void addPreferredActivity(final IntentFilter intentFilter, final int n, final ComponentName[] array, final ComponentName componentName) {
        this.wrapped.addPreferredActivity(intentFilter, n, array, componentName);
    }
    
    public boolean canRequestPackageInstalls() {
        return false;
    }
    
    public String[] canonicalToCurrentPackageNames(final String[] array) {
        return this.wrapped.canonicalToCurrentPackageNames(array);
    }
    
    public int checkPermission(final String s, final String s2) {
        return this.wrapped.checkPermission(s, s2);
    }
    
    public int checkSignatures(final int n, final int n2) {
        return this.wrapped.checkSignatures(n, n2);
    }
    
    public int checkSignatures(final String s, final String s2) {
        return this.wrapped.checkSignatures(s, s2);
    }
    
    public void clearInstantAppCookie() {
    }
    
    public void clearPackagePreferredActivities(final String s) {
        this.wrapped.clearPackagePreferredActivities(s);
    }
    
    public String[] currentToCanonicalPackageNames(final String[] array) {
        return this.wrapped.currentToCanonicalPackageNames(array);
    }
    
    public void extendVerificationTimeout(final int n, final int n2, final long n3) {
        this.wrapped.extendVerificationTimeout(n, n2, n3);
    }
    
    public Drawable getActivityBanner(final ComponentName componentName) throws PackageManager$NameNotFoundException {
        if (Build$VERSION.SDK_INT >= 20) {
            return this.wrapped.getActivityBanner(componentName);
        }
        return null;
    }
    
    public Drawable getActivityBanner(final Intent intent) throws PackageManager$NameNotFoundException {
        if (Build$VERSION.SDK_INT >= 20) {
            return this.wrapped.getActivityBanner(intent);
        }
        return null;
    }
    
    public Drawable getActivityIcon(final ComponentName componentName) throws PackageManager$NameNotFoundException {
        return this.wrapped.getActivityIcon(componentName);
    }
    
    public Drawable getActivityIcon(final Intent intent) throws PackageManager$NameNotFoundException {
        return this.wrapped.getActivityIcon(intent);
    }
    
    public ActivityInfo getActivityInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getActivityInfo(componentName, n);
    }
    
    public Drawable getActivityLogo(final ComponentName componentName) throws PackageManager$NameNotFoundException {
        return this.wrapped.getActivityLogo(componentName);
    }
    
    public Drawable getActivityLogo(final Intent intent) throws PackageManager$NameNotFoundException {
        return this.wrapped.getActivityLogo(intent);
    }
    
    public List<PermissionGroupInfo> getAllPermissionGroups(final int n) {
        return (List<PermissionGroupInfo>)this.wrapped.getAllPermissionGroups(n);
    }
    
    public Drawable getApplicationBanner(final ApplicationInfo applicationInfo) {
        if (Build$VERSION.SDK_INT >= 20) {
            return this.wrapped.getApplicationBanner(applicationInfo);
        }
        return null;
    }
    
    public Drawable getApplicationBanner(final String s) throws PackageManager$NameNotFoundException {
        if (Build$VERSION.SDK_INT >= 20) {
            return this.wrapped.getApplicationBanner(s);
        }
        return null;
    }
    
    public int getApplicationEnabledSetting(final String s) {
        return this.wrapped.getApplicationEnabledSetting(s);
    }
    
    public Drawable getApplicationIcon(final ApplicationInfo applicationInfo) {
        return this.wrapped.getApplicationIcon(applicationInfo);
    }
    
    public Drawable getApplicationIcon(final String s) throws PackageManager$NameNotFoundException {
        return this.wrapped.getApplicationIcon(s);
    }
    
    public ApplicationInfo getApplicationInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getApplicationInfo(s, n);
    }
    
    public CharSequence getApplicationLabel(final ApplicationInfo applicationInfo) {
        return this.wrapped.getApplicationLabel(applicationInfo);
    }
    
    public Drawable getApplicationLogo(final ApplicationInfo applicationInfo) {
        return this.wrapped.getApplicationLogo(applicationInfo);
    }
    
    public Drawable getApplicationLogo(final String s) throws PackageManager$NameNotFoundException {
        return this.wrapped.getApplicationLogo(s);
    }
    
    @Nullable
    public ChangedPackages getChangedPackages(final int n) {
        return null;
    }
    
    public int getComponentEnabledSetting(final ComponentName componentName) {
        return this.wrapped.getComponentEnabledSetting(componentName);
    }
    
    public Drawable getDefaultActivityIcon() {
        return this.wrapped.getDefaultActivityIcon();
    }
    
    public Drawable getDrawable(final String s, final int n, final ApplicationInfo applicationInfo) {
        return this.wrapped.getDrawable(s, n, applicationInfo);
    }
    
    public List<ApplicationInfo> getInstalledApplications(final int n) {
        return (List<ApplicationInfo>)this.wrapped.getInstalledApplications(n);
    }
    
    public List<PackageInfo> getInstalledPackages(final int n) {
        return (List<PackageInfo>)this.wrapped.getInstalledPackages(n);
    }
    
    public String getInstallerPackageName(final String s) {
        return this.wrapped.getInstallerPackageName(s);
    }
    
    @NonNull
    public byte[] getInstantAppCookie() {
        return new byte[0];
    }
    
    public int getInstantAppCookieMaxBytes() {
        return 0;
    }
    
    public InstrumentationInfo getInstrumentationInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getInstrumentationInfo(componentName, n);
    }
    
    public Intent getLaunchIntentForPackage(final String s) {
        return this.wrapped.getLaunchIntentForPackage(s);
    }
    
    public Intent getLeanbackLaunchIntentForPackage(final String s) {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.wrapped.getLeanbackLaunchIntentForPackage(s);
        }
        return null;
    }
    
    public String getNameForUid(final int n) {
        return this.wrapped.getNameForUid(n);
    }
    
    public PackageInfo getPackageArchiveInfo(final String s, final int n) {
        return this.wrapped.getPackageArchiveInfo(s, n);
    }
    
    public int[] getPackageGids(final String s) throws PackageManager$NameNotFoundException {
        return this.wrapped.getPackageGids(s);
    }
    
    public int[] getPackageGids(final String s, final int n) throws PackageManager$NameNotFoundException {
        if (Build$VERSION.SDK_INT >= 24) {
            return this.wrapped.getPackageGids(s, n);
        }
        return null;
    }
    
    public PackageInfo getPackageInfo(final VersionedPackage versionedPackage, final int n) throws PackageManager$NameNotFoundException {
        return null;
    }
    
    public PackageInfo getPackageInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getPackageInfo(s, n);
    }
    
    @NonNull
    public PackageInstaller getPackageInstaller() {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.wrapped.getPackageInstaller();
        }
        return null;
    }
    
    public int getPackageUid(final String s, final int n) throws PackageManager$NameNotFoundException {
        if (Build$VERSION.SDK_INT >= 24) {
            return this.wrapped.getPackageUid(s, n);
        }
        return 0;
    }
    
    public String[] getPackagesForUid(final int n) {
        return this.wrapped.getPackagesForUid(n);
    }
    
    public List<PackageInfo> getPackagesHoldingPermissions(final String[] array, final int n) {
        if (Build$VERSION.SDK_INT >= 18) {
            return (List<PackageInfo>)this.wrapped.getPackagesHoldingPermissions(array, n);
        }
        return null;
    }
    
    public PermissionGroupInfo getPermissionGroupInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getPermissionGroupInfo(s, n);
    }
    
    public PermissionInfo getPermissionInfo(final String s, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getPermissionInfo(s, n);
    }
    
    public int getPreferredActivities(final List<IntentFilter> list, final List<ComponentName> list2, final String s) {
        return this.wrapped.getPreferredActivities((List)list, (List)list2, s);
    }
    
    public List<PackageInfo> getPreferredPackages(final int n) {
        return (List<PackageInfo>)this.wrapped.getPreferredPackages(n);
    }
    
    public ProviderInfo getProviderInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getProviderInfo(componentName, n);
    }
    
    public ActivityInfo getReceiverInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getReceiverInfo(componentName, n);
    }
    
    public Resources getResourcesForActivity(final ComponentName componentName) throws PackageManager$NameNotFoundException {
        return this.wrapped.getResourcesForActivity(componentName);
    }
    
    public Resources getResourcesForApplication(final ApplicationInfo applicationInfo) throws PackageManager$NameNotFoundException {
        return this.wrapped.getResourcesForApplication(applicationInfo);
    }
    
    public Resources getResourcesForApplication(final String s) throws PackageManager$NameNotFoundException {
        return this.wrapped.getResourcesForApplication(s);
    }
    
    public ServiceInfo getServiceInfo(final ComponentName componentName, final int n) throws PackageManager$NameNotFoundException {
        return this.wrapped.getServiceInfo(componentName, n);
    }
    
    @NonNull
    public List<SharedLibraryInfo> getSharedLibraries(final int n) {
        return null;
    }
    
    public FeatureInfo[] getSystemAvailableFeatures() {
        return this.wrapped.getSystemAvailableFeatures();
    }
    
    public String[] getSystemSharedLibraryNames() {
        return this.wrapped.getSystemSharedLibraryNames();
    }
    
    public CharSequence getText(final String s, final int n, final ApplicationInfo applicationInfo) {
        return this.wrapped.getText(s, n, applicationInfo);
    }
    
    public Drawable getUserBadgedDrawableForDensity(final Drawable drawable, final UserHandle userHandle, final Rect rect, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.wrapped.getUserBadgedDrawableForDensity(drawable, userHandle, rect, n);
        }
        return null;
    }
    
    public Drawable getUserBadgedIcon(final Drawable drawable, final UserHandle userHandle) {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.wrapped.getUserBadgedIcon(drawable, userHandle);
        }
        return null;
    }
    
    public CharSequence getUserBadgedLabel(final CharSequence charSequence, final UserHandle userHandle) {
        if (Build$VERSION.SDK_INT >= 21) {
            return this.wrapped.getUserBadgedLabel(charSequence, userHandle);
        }
        return null;
    }
    
    public XmlResourceParser getXml(final String s, final int n, final ApplicationInfo applicationInfo) {
        return this.wrapped.getXml(s, n, applicationInfo);
    }
    
    public boolean hasSystemFeature(final String s) {
        return this.wrapped.hasSystemFeature(s);
    }
    
    public boolean hasSystemFeature(final String s, final int n) {
        return Build$VERSION.SDK_INT >= 24 && this.wrapped.hasSystemFeature(s, n);
    }
    
    public boolean isInstantApp() {
        return false;
    }
    
    public boolean isInstantApp(final String s) {
        return false;
    }
    
    public boolean isPermissionRevokedByPolicy(@NonNull final String s, @NonNull final String s2) {
        return Build$VERSION.SDK_INT >= 23 && this.wrapped.isPermissionRevokedByPolicy(s, s2);
    }
    
    public boolean isSafeMode() {
        return this.wrapped.isSafeMode();
    }
    
    public List<ResolveInfo> queryBroadcastReceivers(final Intent intent, final int n) {
        return (List<ResolveInfo>)this.wrapped.queryBroadcastReceivers(intent, n);
    }
    
    public List<ProviderInfo> queryContentProviders(final String s, final int n, final int n2) {
        return (List<ProviderInfo>)this.wrapped.queryContentProviders(s, n, n2);
    }
    
    public List<InstrumentationInfo> queryInstrumentation(final String s, final int n) {
        return (List<InstrumentationInfo>)this.wrapped.queryInstrumentation(s, n);
    }
    
    public List<ResolveInfo> queryIntentActivities(final Intent intent, final int n) {
        return (List<ResolveInfo>)this.wrapped.queryIntentActivities(intent, n);
    }
    
    public List<ResolveInfo> queryIntentActivityOptions(final ComponentName componentName, final Intent[] array, final Intent intent, final int n) {
        return (List<ResolveInfo>)this.wrapped.queryIntentActivityOptions(componentName, array, intent, n);
    }
    
    public List<ResolveInfo> queryIntentContentProviders(final Intent intent, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            return (List<ResolveInfo>)this.wrapped.queryIntentContentProviders(intent, n);
        }
        return null;
    }
    
    public List<ResolveInfo> queryIntentServices(final Intent intent, final int n) {
        return (List<ResolveInfo>)this.wrapped.queryIntentServices(intent, n);
    }
    
    public List<PermissionInfo> queryPermissionsByGroup(final String s, final int n) throws PackageManager$NameNotFoundException {
        return (List<PermissionInfo>)this.wrapped.queryPermissionsByGroup(s, n);
    }
    
    public void removePackageFromPreferred(final String s) {
        this.wrapped.removePackageFromPreferred(s);
    }
    
    public void removePermission(final String s) {
        this.wrapped.removePermission(s);
    }
    
    public ResolveInfo resolveActivity(final Intent intent, final int n) {
        return this.wrapped.resolveActivity(intent, n);
    }
    
    public ProviderInfo resolveContentProvider(final String s, final int n) {
        return this.wrapped.resolveContentProvider(s, n);
    }
    
    public ResolveInfo resolveService(final Intent intent, final int n) {
        return this.wrapped.resolveService(intent, n);
    }
    
    public void setApplicationCategoryHint(@NonNull final String s, final int n) {
    }
    
    public void setApplicationEnabledSetting(final String s, final int n, final int n2) {
        this.wrapped.setApplicationEnabledSetting(s, n, n2);
    }
    
    public void setComponentEnabledSetting(final ComponentName componentName, final int n, final int n2) {
        this.wrapped.setComponentEnabledSetting(componentName, n, n2);
    }
    
    public void setInstallerPackageName(final String s, final String s2) {
        this.wrapped.setInstallerPackageName(s, s2);
    }
    
    public void updateInstantAppCookie(@Nullable final byte[] array) {
    }
    
    public void verifyPendingInstall(final int n, final int n2) {
        this.wrapped.verifyPendingInstall(n, n2);
    }
}
