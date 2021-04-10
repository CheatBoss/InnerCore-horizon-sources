package com.mojang.minecraftpe.packagesource.googleplay;

import com.mojang.minecraftpe.*;
import com.mojang.minecraftpe.packagesource.*;
import java.io.*;
import android.util.*;
import android.provider.*;
import com.googleplay.licensing.*;
import android.os.storage.*;
import android.content.*;
import com.google.android.vending.expansion.downloader.*;
import android.os.*;
import android.app.*;
import android.content.pm.*;
import java.util.*;

public class ApkXDownloaderClient extends PackageSource implements IDownloaderClient, ActivityListener
{
    private static final String LOG_TAG = "ApkXDownloaderClient";
    public static final byte[] SALT;
    private static String licenseKey;
    private static String notificationChannelId;
    private MainActivity mActivity;
    private IStub mDownloaderClientStub;
    private PackageSourceListener mListener;
    private NotificationManager mNotificationManager;
    private IDownloaderService mRemoteService;
    private StorageManager mStorageManager;
    
    static {
        SALT = new byte[] { 78, -97, 80, -51, 45, -99, 108, 52, -42, 25, 48, 24, -76, -105, 9, 38, -43, 81, 6, 14 };
    }
    
    public ApkXDownloaderClient(final MainActivity mActivity, final String licenseKey, final PackageSourceListener mListener) {
        this.mListener = mListener;
        (this.mActivity = mActivity).addListener(this);
        ApkXDownloaderClient.licenseKey = licenseKey;
        ApkXDownloaderClient.notificationChannelId = String.format("%1$s_APKXDownload", this.mActivity.getCallingPackage());
        this.mNotificationManager = (NotificationManager)this.mActivity.getSystemService("notification");
        this.mStorageManager = (StorageManager)this.mActivity.getSystemService("storage");
    }
    
    public static int convertOBBStateToMountState(final int n) {
        if (n == 1) {
            return 7;
        }
        if (n == 2) {
            return 8;
        }
        switch (n) {
            default: {
                return 0;
            }
            case 25: {
                return 6;
            }
            case 24: {
                return 1;
            }
            case 23: {
                return 5;
            }
            case 22: {
                return 3;
            }
            case 21: {
                return 2;
            }
            case 20: {
                return 4;
            }
        }
    }
    
    public static int convertStateToFailedReason(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 18: {
                return 5;
            }
            case 17: {
                return 4;
            }
            case 16: {
                return 3;
            }
            case 15: {
                return 2;
            }
        }
    }
    
    public static int convertStateToPausedReason(final int n) {
        switch (n) {
            default: {
                return 0;
            }
            case 14: {
                return 9;
            }
            case 13: {
                return 8;
            }
            case 12: {
                return 7;
            }
            case 11: {
                return 6;
            }
            case 10: {
                return 5;
            }
            case 9: {
                return 4;
            }
            case 8: {
                return 3;
            }
            case 7: {
                return 2;
            }
            case 6: {
                return 1;
            }
        }
    }
    
    static ApkXDownloaderClient create(final String s, final PackageSourceListener packageSourceListener) {
        return new ApkXDownloaderClient(MainActivity.mInstance, s, packageSourceListener);
    }
    
    private void deleteObbFiles() {
        final File[] listFiles = new File(Helpers.getSaveFilePath((Context)this.mActivity)).listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            final String name = file.getName();
            if (name.endsWith(".obb")) {
                Log.i("ApkXDownloaderClient", String.format("deleteObbFiles - deleting file %s", name));
                if (!file.delete()) {
                    Log.e("ApkXDownloaderClient", String.format("deleteObbFiles - failed to delete file %s", name));
                }
            }
        }
    }
    
    public static final String getLicenseKey() {
        return ApkXDownloaderClient.licenseKey;
    }
    
    public static final String getNotificationChannelId() {
        return ApkXDownloaderClient.notificationChannelId;
    }
    
    private void launchDownloader() {
        this.mActivity.runOnUiThread((Runnable)new LaunchDownloaderTask(this));
    }
    
    @Override
    public void abortDownload() {
        final IDownloaderService mRemoteService = this.mRemoteService;
        if (mRemoteService != null) {
            mRemoteService.requestAbortDownload();
        }
    }
    
    @Override
    public void destructor() {
        Log.i("ApkXDownloaderClient", "destructor");
        this.mActivity.removeListener(this);
    }
    
    @Override
    public void downloadFiles(final String s, final long n, final boolean b, final boolean b2) {
        final APKExpansionPolicy apkExpansionPolicy = new APKExpansionPolicy((Context)this.mActivity, new AESObfuscator(ApkXDownloaderClient.SALT, this.mActivity.getPackageName(), Settings$Secure.getString(this.mActivity.getContentResolver(), "android_id")));
        apkExpansionPolicy.resetPolicy();
        new LicenseChecker((Context)this.mActivity, apkExpansionPolicy, ApkXDownloaderClient.licenseKey).checkAccess(new LicenseCheckerCallback() {
            @Override
            public void allow(int i) {
                Log.i("ApkXDownloaderClient", String.format("LicenseCheckerCallback - allow: %d.", i));
                Log.i("ApkXDownloaderClient", String.format("LicenseCheckerCallback - Expecting to find file name: '%s', size: %d.", s, n));
                int expansionURLCount;
                for (expansionURLCount = apkExpansionPolicy.getExpansionURLCount(), i = 0; i < expansionURLCount; ++i) {
                    Log.i("ApkXDownloaderClient", String.format("LicenseCheckerCallback - File name: '%s', size: %d.", apkExpansionPolicy.getExpansionFileName(i), apkExpansionPolicy.getExpansionFileSize(i)));
                }
                if (b && (expansionURLCount == 0 || !s.equalsIgnoreCase(apkExpansionPolicy.getExpansionFileName(0)))) {
                    Log.e("ApkXDownloaderClient", String.format("LicenseCheckerCallback - Verification failed. File name: '%s', found name: '%s'.", s, apkExpansionPolicy.getExpansionFileName(0)));
                    ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, false, true, 0, 6);
                    return;
                }
                if (b2 && (expansionURLCount == 0 || n != apkExpansionPolicy.getExpansionFileSize(0))) {
                    Log.e("ApkXDownloaderClient", String.format("LicenseCheckerCallback - Verification failed. File size: '%s', found size: '%s'.", n, apkExpansionPolicy.getExpansionFileSize(0)));
                    ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, false, true, 0, 6);
                    return;
                }
                if (Helpers.canWriteOBBFile((Context)ApkXDownloaderClient.this.mActivity)) {
                    ApkXDownloaderClient.this.launchDownloader();
                    return;
                }
                ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, false, true, 0, 1);
            }
            
            @Override
            public void applicationError(final int n) {
                Log.i("ApkXDownloaderClient", String.format("LicenseCheckerCallback - error: %d", n));
                ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, false, true, 0, 8);
            }
            
            @Override
            public void dontAllow(int n) {
                Log.i("ApkXDownloaderClient", String.format("LicenseCheckerCallback - dontAllow: %d", n));
                if (n != 291) {
                    if (n != 561) {
                        n = 0;
                    }
                    else {
                        n = 2;
                    }
                }
                else {
                    n = 7;
                }
                ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, false, true, 0, n);
            }
        });
    }
    
    @Override
    public String getDownloadDirectoryPath() {
        return Helpers.getSaveFilePath((Context)this.mActivity);
    }
    
    @Override
    public String getMountPath(final String s) {
        final StorageManager mStorageManager = this.mStorageManager;
        if (mStorageManager == null) {
            return null;
        }
        return mStorageManager.getMountedObbPath(s);
    }
    
    public String getOBBFilePath(final String s) {
        return Helpers.generateSaveFileName((Context)this.mActivity, s);
    }
    
    @Override
    public void mountFiles(String obbFilePath) {
        if (obbFilePath == null || obbFilePath.isEmpty()) {
            Log.e("ApkXDownloaderClient", String.format("mountFiles - filename '%s' is empty.", obbFilePath));
            return;
        }
        obbFilePath = this.getOBBFilePath(obbFilePath);
        Log.d("ApkXDownloaderClient", String.format("mountFiles - path: '%s'.", obbFilePath));
        if (!new File(obbFilePath).exists()) {
            Log.e("ApkXDownloaderClient", String.format("mountFiles - path '%s' does not exist.", obbFilePath));
            return;
        }
        this.mStorageManager.mountObb(obbFilePath, (String)null, (OnObbStateChangeListener)new OnObbStateChangeListener() {
            public void onObbStateChange(String s, int n) {
                super.onObbStateChange(s, n);
                Log.d("ApkXDownloaderClient", String.format("onObbStateChange - path: '%s', state: %d.", s, n));
                final int convertOBBStateToMountState = ApkXDownloaderClient.convertOBBStateToMountState(n);
                final boolean obbMounted = ApkXDownloaderClient.this.mStorageManager.isObbMounted(s);
                final String mountPath = ApkXDownloaderClient.this.getMountPath(s);
                Log.d("ApkXDownloaderClient", String.format("onObbStateChange - source path: '%s', new path: '%s'.", s, mountPath));
                s = mountPath;
                if (mountPath == null) {
                    s = "";
                }
                n = convertOBBStateToMountState;
                if (obbMounted) {
                    n = convertOBBStateToMountState;
                    if (s == "") {
                        n = 4;
                    }
                }
                ApkXDownloaderClient.this.mListener.onMountStateChanged(s, n);
            }
        });
    }
    
    @Override
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        Log.i("ApkXDownloaderClient", "onActivityResult");
    }
    
    @Override
    public void onDestroy() {
        Log.i("ApkXDownloaderClient", "onDestroy");
        this.mActivity.removeListener(this);
    }
    
    @Override
    public void onDownloadProgress(final DownloadProgressInfo downloadProgressInfo) {
        final long mOverallProgress = downloadProgressInfo.mOverallProgress;
        final long mOverallTotal = downloadProgressInfo.mOverallTotal;
        final float mCurrentSpeed = downloadProgressInfo.mCurrentSpeed;
        final long mTimeRemaining = downloadProgressInfo.mTimeRemaining;
        Log.i("ApkXDownloaderClient", String.format("onDownloadProgress - %d / %d", mOverallProgress, mOverallTotal));
        this.mListener.onDownloadProgress(mOverallProgress, mOverallTotal, mCurrentSpeed, mTimeRemaining);
    }
    
    @Override
    public void onDownloadStateChanged(int n) {
        final String stringResource = PackageSource.getStringResource(Helpers.getDownloaderStringResourceIDFromPlaystoreState(n));
        int convertStateToFailedReason = 0;
        boolean b3 = false;
        boolean b4 = false;
        boolean b5 = false;
        boolean b6 = false;
        boolean b7 = false;
        Label_0236: {
            boolean b8 = false;
            boolean b9 = false;
            Label_0221: {
                boolean b = false;
                boolean b2 = false;
                boolean b10 = false;
                Label_0198: {
                    Label_0195: {
                        switch (n) {
                            default: {
                                b = true;
                                b2 = true;
                                break Label_0195;
                            }
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19: {
                                convertStateToFailedReason = convertStateToFailedReason(n);
                                b3 = false;
                                b4 = false;
                                b5 = false;
                                b6 = false;
                                b7 = true;
                                n = 0;
                                break Label_0236;
                            }
                            case 6:
                            case 7:
                            case 8:
                            case 9:
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14: {
                                final int convertStateToPausedReason = convertStateToPausedReason(n);
                                b3 = (n == 8 || n == 9);
                                b4 = false;
                                b8 = true;
                                b9 = false;
                                n = convertStateToPausedReason;
                                break Label_0221;
                            }
                            case 5: {
                                b = false;
                                b2 = false;
                                b10 = true;
                                break Label_0198;
                            }
                            case 4: {
                                b = false;
                                break;
                            }
                            case 1:
                            case 2:
                            case 3: {
                                b = true;
                                break;
                            }
                        }
                        b2 = false;
                    }
                    b10 = false;
                }
                n = 0;
                final boolean b11 = b;
                final boolean b12 = false;
                b9 = b10;
                b8 = b2;
                b4 = b11;
                b3 = b12;
            }
            final boolean b13 = b8;
            b7 = false;
            convertStateToFailedReason = 0;
            b6 = b9;
            b5 = b13;
        }
        Log.i("ApkXDownloaderClient", String.format("onDownloadStateChanged - state: %s", stringResource));
        this.mListener.onDownloadStateChanged(b3, b4, b5, b6, b7, n, convertStateToFailedReason);
    }
    
    @Override
    public void onResume() {
        Log.i("ApkXDownloaderClient", "onResume");
        final IStub mDownloaderClientStub = this.mDownloaderClientStub;
        if (mDownloaderClientStub != null) {
            mDownloaderClientStub.connect((Context)this.mActivity);
        }
    }
    
    @Override
    public void onServiceConnected(final Messenger messenger) {
        Log.i("ApkXDownloaderClient", "onServiceConnected");
        (this.mRemoteService = DownloaderServiceMarshaller.CreateProxy(messenger)).onClientUpdated(this.mDownloaderClientStub.getMessenger());
    }
    
    @Override
    public void onStop() {
        Log.i("ApkXDownloaderClient", "onStop");
        final IStub mDownloaderClientStub = this.mDownloaderClientStub;
        if (mDownloaderClientStub != null) {
            mDownloaderClientStub.disconnect((Context)this.mActivity);
        }
    }
    
    @Override
    public void pauseDownload() {
        final IDownloaderService mRemoteService = this.mRemoteService;
        if (mRemoteService != null) {
            mRemoteService.requestPauseDownload();
        }
    }
    
    @Override
    public void resumeDownload() {
        final IDownloaderService mRemoteService = this.mRemoteService;
        if (mRemoteService != null) {
            mRemoteService.requestContinueDownload();
        }
    }
    
    @Override
    public void resumeDownloadOnCell() {
        final IDownloaderService mRemoteService = this.mRemoteService;
        if (mRemoteService != null) {
            mRemoteService.setDownloadFlags(1);
            this.mRemoteService.requestContinueDownload();
        }
    }
    
    @Override
    public void unmountFiles(String obbFilePath) {
        if (obbFilePath == null || obbFilePath.isEmpty()) {
            Log.w("ApkXDownloaderClient", String.format("unmountFiles - filename '%s' is empty.", obbFilePath));
            return;
        }
        obbFilePath = this.getOBBFilePath(obbFilePath);
        Log.d("ApkXDownloaderClient", String.format("unmountFiles - path: '%s'.", obbFilePath));
        if (!new File(obbFilePath).exists()) {
            Log.w("ApkXDownloaderClient", String.format("unmountFiles - path '%s' does not exist.", obbFilePath));
            return;
        }
        this.mStorageManager.unmountObb(obbFilePath, false, (OnObbStateChangeListener)null);
    }
    
    class LaunchDownloaderTask implements Runnable
    {
        IDownloaderClient client;
        
        LaunchDownloaderTask(final IDownloaderClient client) {
            this.client = client;
        }
        
        @Override
        public void run() {
            ApkXDownloaderClient.this.deleteObbFiles();
            try {
                ApkXDownloaderClient.this.mDownloaderClientStub = DownloaderClientMarshaller.CreateStub(this.client, ApkXDownloaderService.class);
                ApkXDownloaderClient.this.mDownloaderClientStub.connect((Context)ApkXDownloaderClient.this.mActivity);
                final Intent intent = ApkXDownloaderClient.this.mActivity.getIntent();
                final Intent intent2 = new Intent((Context)ApkXDownloaderClient.this.mActivity, (Class)ApkXDownloaderClient.this.mActivity.getClass());
                intent2.setFlags(335544320);
                intent2.setAction(intent.getAction());
                if (intent.getCategories() != null) {
                    final Iterator iterator = intent.getCategories().iterator();
                    while (iterator.hasNext()) {
                        intent2.addCategory((String)iterator.next());
                    }
                }
                if (Build$VERSION.SDK_INT >= 26) {
                    final String stringResource = PackageSource.getStringResource(StringResourceId.NOTIFICATIONCHANNEL_NAME);
                    final String stringResource2 = PackageSource.getStringResource(StringResourceId.NOTIFICATIONCHANNEL_DESCRIPTION);
                    final NotificationChannel notificationChannel = new NotificationChannel(ApkXDownloaderClient.getNotificationChannelId(), (CharSequence)stringResource, 2);
                    notificationChannel.setDescription(stringResource2);
                    ApkXDownloaderClient.this.mNotificationManager.createNotificationChannel(notificationChannel);
                }
                final int startDownloadServiceIfRequired = DownloaderClientMarshaller.startDownloadServiceIfRequired((Context)ApkXDownloaderClient.this.mActivity, PendingIntent.getActivity((Context)ApkXDownloaderClient.this.mActivity, 0, intent2, 134217728), ApkXDownloaderService.class);
                ApkXDownloaderClient.this.mListener.onDownloadStarted();
                Log.i("ApkXDownloaderClient", String.format("launchDownloader - startResult %d", startDownloadServiceIfRequired));
                if (startDownloadServiceIfRequired == 0) {
                    ApkXDownloaderClient.this.mListener.onDownloadStateChanged(false, false, false, true, false, 0, 0);
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                Log.e("ApkXDownloaderClient", String.format("launchDownloader - cannot find own package: %s", ex.toString()));
                ex.printStackTrace();
            }
        }
    }
}
