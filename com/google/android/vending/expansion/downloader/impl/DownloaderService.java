package com.google.android.vending.expansion.downloader.impl;

import android.net.wifi.*;
import android.util.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import java.io.*;
import com.google.android.vending.expansion.downloader.*;
import android.app.*;
import android.support.v4.content.*;
import android.os.*;
import android.provider.*;
import com.googleplay.licensing.*;

public abstract class DownloaderService extends CustomIntentService implements IDownloaderService
{
    public static final String ACTION_DOWNLOADS_CHANGED = "downloadsChanged";
    public static final String ACTION_DOWNLOAD_COMPLETE = "lvldownloader.intent.action.DOWNLOAD_COMPLETE";
    public static final String ACTION_DOWNLOAD_STATUS = "lvldownloader.intent.action.DOWNLOAD_STATUS";
    public static final int CONTROL_PAUSED = 1;
    public static final int CONTROL_RUN = 0;
    public static final int DOWNLOAD_REQUIRED = 2;
    public static final String EXTRA_FILE_NAME = "downloadId";
    public static final String EXTRA_IS_WIFI_REQUIRED = "isWifiRequired";
    public static final String EXTRA_MESSAGE_HANDLER = "EMH";
    public static final String EXTRA_PACKAGE_NAME = "EPN";
    public static final String EXTRA_PENDING_INTENT = "EPI";
    public static final String EXTRA_STATUS_CURRENT_FILE_SIZE = "CFS";
    public static final String EXTRA_STATUS_CURRENT_PROGRESS = "CFP";
    public static final String EXTRA_STATUS_STATE = "ESS";
    public static final String EXTRA_STATUS_TOTAL_PROGRESS = "TFP";
    public static final String EXTRA_STATUS_TOTAL_SIZE = "ETS";
    private static final String LOG_TAG = "LVLDL";
    public static final int LVL_CHECK_REQUIRED = 1;
    public static final int NETWORK_CANNOT_USE_ROAMING = 5;
    public static final int NETWORK_MOBILE = 1;
    public static final int NETWORK_NO_CONNECTION = 2;
    public static final int NETWORK_OK = 1;
    public static final int NETWORK_RECOMMENDED_UNUSABLE_DUE_TO_SIZE = 4;
    public static final int NETWORK_TYPE_DISALLOWED_BY_REQUESTOR = 6;
    public static final int NETWORK_UNUSABLE_DUE_TO_SIZE = 3;
    public static final int NETWORK_WIFI = 2;
    public static final int NO_DOWNLOAD_REQUIRED = 0;
    private static final float SMOOTHING_FACTOR = 0.005f;
    public static final int STATUS_CANCELED = 490;
    public static final int STATUS_CANNOT_RESUME = 489;
    public static final int STATUS_DEVICE_NOT_FOUND_ERROR = 499;
    public static final int STATUS_FILE_ALREADY_EXISTS_ERROR = 488;
    public static final int STATUS_FILE_DELIVERED_INCORRECTLY = 487;
    public static final int STATUS_FILE_ERROR = 492;
    public static final int STATUS_FORBIDDEN = 403;
    public static final int STATUS_HTTP_DATA_ERROR = 495;
    public static final int STATUS_HTTP_EXCEPTION = 496;
    public static final int STATUS_INSUFFICIENT_SPACE_ERROR = 498;
    public static final int STATUS_PAUSED_BY_APP = 193;
    public static final int STATUS_PENDING = 190;
    public static final int STATUS_QUEUED_FOR_WIFI = 197;
    public static final int STATUS_QUEUED_FOR_WIFI_OR_CELLULAR_PERMISSION = 196;
    public static final int STATUS_RUNNING = 192;
    public static final int STATUS_SUCCESS = 200;
    public static final int STATUS_TOO_MANY_REDIRECTS = 497;
    public static final int STATUS_UNHANDLED_HTTP_CODE = 494;
    public static final int STATUS_UNHANDLED_REDIRECT = 493;
    public static final int STATUS_UNKNOWN_ERROR = 491;
    public static final int STATUS_WAITING_FOR_NETWORK = 195;
    public static final int STATUS_WAITING_TO_RETRY = 194;
    private static final String TEMP_EXT = ".tmp";
    public static final int VISIBILITY_HIDDEN = 2;
    public static final int VISIBILITY_VISIBLE = 0;
    public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
    private static boolean sIsRunning;
    private PendingIntent mAlarmIntent;
    float mAverageDownloadSpeed;
    long mBytesAtSample;
    long mBytesSoFar;
    private Messenger mClientMessenger;
    private BroadcastReceiver mConnReceiver;
    private ConnectivityManager mConnectivityManager;
    private int mControl;
    int mFileCount;
    private boolean mIsAtLeast3G;
    private boolean mIsAtLeast4G;
    private boolean mIsCellularConnection;
    private boolean mIsConnected;
    private boolean mIsFailover;
    private boolean mIsRoaming;
    long mMillisecondsAtSample;
    private DownloadNotification mNotification;
    private PackageInfo mPackageInfo;
    private PendingIntent mPendingIntent;
    private final Messenger mServiceMessenger;
    private final IStub mServiceStub;
    private boolean mStateChanged;
    private int mStatus;
    long mTotalLength;
    private WifiManager mWifiManager;
    
    public DownloaderService() {
        super("LVLDownloadService");
        final IStub createStub = DownloaderServiceMarshaller.CreateStub(this);
        this.mServiceStub = createStub;
        this.mServiceMessenger = createStub.getMessenger();
    }
    
    private void cancelAlarms() {
        if (this.mAlarmIntent != null) {
            final AlarmManager alarmManager = (AlarmManager)this.getSystemService("alarm");
            if (alarmManager == null) {
                Log.e("LVLDL", "couldn't get alarm manager");
                return;
            }
            alarmManager.cancel(this.mAlarmIntent);
            this.mAlarmIntent = null;
        }
    }
    
    private static boolean isLVLCheckRequired(final DownloadsDB downloadsDB, final PackageInfo packageInfo) {
        return downloadsDB.mVersionCode != packageInfo.versionCode;
    }
    
    private static boolean isServiceRunning() {
        synchronized (DownloaderService.class) {
            return DownloaderService.sIsRunning;
        }
    }
    
    public static boolean isStatusClientError(final int n) {
        return n >= 400 && n < 500;
    }
    
    public static boolean isStatusCompleted(final int n) {
        return (n >= 200 && n < 300) || (n >= 400 && n < 600);
    }
    
    public static boolean isStatusError(final int n) {
        return n >= 400 && n < 600;
    }
    
    public static boolean isStatusInformational(final int n) {
        return n >= 100 && n < 200;
    }
    
    public static boolean isStatusServerError(final int n) {
        return n >= 500 && n < 600;
    }
    
    public static boolean isStatusSuccess(final int n) {
        return n >= 200 && n < 300;
    }
    
    private void scheduleAlarm(final long n) {
        final AlarmManager alarmManager = (AlarmManager)this.getSystemService("alarm");
        if (alarmManager == null) {
            Log.e("LVLDL", "couldn't get alarm manager");
            return;
        }
        final String alarmReceiverClassName = this.getAlarmReceiverClassName();
        final Intent intent = new Intent("android.intent.action.DOWNLOAD_WAKEUP");
        intent.putExtra("EPI", (Parcelable)this.mPendingIntent);
        intent.setClassName(this.getPackageName(), alarmReceiverClassName);
        this.mAlarmIntent = PendingIntent.getBroadcast((Context)this, 0, intent, 1073741824);
        alarmManager.set(0, System.currentTimeMillis() + n, this.mAlarmIntent);
    }
    
    private static void setServiceRunning(final boolean sIsRunning) {
        synchronized (DownloaderService.class) {
            DownloaderService.sIsRunning = sIsRunning;
        }
    }
    
    public static int startDownloadServiceIfRequired(final Context context, final PendingIntent pendingIntent, final Class<?> clazz) throws PackageManager$NameNotFoundException {
        return startDownloadServiceIfRequired(context, pendingIntent, context.getPackageName(), clazz.getName());
    }
    
    public static int startDownloadServiceIfRequired(final Context context, final PendingIntent pendingIntent, final String s, final String s2) throws PackageManager$NameNotFoundException {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static int startDownloadServiceIfRequired(final Context context, final Intent intent, final Class<?> clazz) throws PackageManager$NameNotFoundException {
        return startDownloadServiceIfRequired(context, (PendingIntent)intent.getParcelableExtra("EPI"), clazz);
    }
    
    private void updateNetworkState(final NetworkInfo networkInfo) {
        final boolean mIsConnected = this.mIsConnected;
        final boolean mIsFailover = this.mIsFailover;
        final boolean mIsCellularConnection = this.mIsCellularConnection;
        final boolean mIsRoaming = this.mIsRoaming;
        final boolean mIsAtLeast3G = this.mIsAtLeast3G;
        boolean mStateChanged = false;
        if (networkInfo != null) {
            this.mIsRoaming = networkInfo.isRoaming();
            this.mIsFailover = networkInfo.isFailover();
            this.mIsConnected = networkInfo.isConnected();
            this.updateNetworkType(networkInfo.getType(), networkInfo.getSubtype());
        }
        else {
            this.mIsRoaming = false;
            this.mIsFailover = false;
            this.mIsConnected = false;
            this.updateNetworkType(-1, -1);
        }
        if (this.mStateChanged || mIsConnected != this.mIsConnected || mIsFailover != this.mIsFailover || mIsCellularConnection != this.mIsCellularConnection || mIsRoaming != this.mIsRoaming || mIsAtLeast3G != this.mIsAtLeast3G) {
            mStateChanged = true;
        }
        this.mStateChanged = mStateChanged;
    }
    
    private void updateNetworkType(final int n, final int n2) {
        if (n != 0) {
            if (n != 1) {
                if (n == 6) {
                    this.mIsCellularConnection = true;
                    this.mIsAtLeast3G = true;
                    this.mIsAtLeast4G = true;
                    return;
                }
                if (n != 7 && n != 9) {
                    return;
                }
            }
            this.mIsCellularConnection = false;
            this.mIsAtLeast3G = false;
            this.mIsAtLeast4G = false;
            return;
        }
        this.mIsCellularConnection = true;
        switch (n2) {
            default: {
                this.mIsCellularConnection = false;
                this.mIsAtLeast3G = false;
                this.mIsAtLeast4G = false;
            }
            case 13:
            case 14:
            case 15: {
                this.mIsAtLeast3G = true;
                this.mIsAtLeast4G = true;
            }
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10: {
                this.mIsAtLeast3G = true;
                this.mIsAtLeast4G = false;
            }
            case 1:
            case 2:
            case 4:
            case 7:
            case 11: {
                this.mIsAtLeast3G = false;
                this.mIsAtLeast4G = false;
            }
        }
    }
    
    public String generateSaveFile(String generateTempSaveFileName, final long n) throws GenerateSaveFileError {
        generateTempSaveFileName = this.generateTempSaveFileName(generateTempSaveFileName);
        final File file = new File(generateTempSaveFileName);
        if (!Helpers.isExternalMediaMounted()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("External media not mounted: ");
            sb.append(generateTempSaveFileName);
            Log.d("LVLDL", sb.toString());
            throw new GenerateSaveFileError(499, "external media is not yet mounted");
        }
        if (file.exists()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("File already exists: ");
            sb2.append(generateTempSaveFileName);
            Log.d("LVLDL", sb2.toString());
            throw new GenerateSaveFileError(488, "requested destination file already exists");
        }
        if (Helpers.getAvailableBytes(Helpers.getFilesystemRoot(generateTempSaveFileName)) >= n) {
            return generateTempSaveFileName;
        }
        throw new GenerateSaveFileError(498, "insufficient space on external storage");
    }
    
    public String generateTempSaveFileName(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(Helpers.getSaveFilePath((Context)this));
        sb.append(File.separator);
        sb.append(s);
        sb.append(".tmp");
        return sb.toString();
    }
    
    public abstract String getAlarmReceiverClassName();
    
    public int getControl() {
        return this.mControl;
    }
    
    public String getLogMessageForNetworkError(final int n) {
        if (n == 2) {
            return "no network connection available";
        }
        if (n == 3) {
            return "download size exceeds limit for mobile network";
        }
        if (n == 4) {
            return "download size exceeds recommended limit for mobile network";
        }
        if (n == 5) {
            return "download cannot use the current network connection because it is roaming";
        }
        if (n != 6) {
            return "unknown error with network connectivity";
        }
        return "download was requested to not use the current network type";
    }
    
    public int getNetworkAvailabilityState(final DownloadsDB downloadsDB) {
        if (!this.mIsConnected) {
            return 2;
        }
        if (!this.mIsCellularConnection) {
            return 1;
        }
        final int mFlags = downloadsDB.mFlags;
        if (this.mIsRoaming) {
            return 5;
        }
        if ((mFlags & 0x1) != 0x0) {
            return 1;
        }
        return 6;
    }
    
    public abstract String getNotificationChannelId();
    
    public abstract String getPublicKey();
    
    public abstract byte[] getSALT();
    
    public int getStatus() {
        return this.mStatus;
    }
    
    public boolean handleFileUpdated(final DownloadsDB downloadsDB, final int n, final String s, final long n2) {
        final DownloadInfo downloadInfoByFileName = downloadsDB.getDownloadInfoByFileName(s);
        if (downloadInfoByFileName != null) {
            final String mFileName = downloadInfoByFileName.mFileName;
            if (mFileName != null) {
                if (s.equals(mFileName)) {
                    return false;
                }
                final File file = new File(Helpers.generateSaveFileName((Context)this, mFileName));
                if (file.exists()) {
                    file.delete();
                }
            }
        }
        return true ^ Helpers.doesFileExist((Context)this, s, n2, true);
    }
    
    public boolean isWiFi() {
        return this.mIsConnected && !this.mIsCellularConnection;
    }
    
    public void notifyUpdateBytes(final long mBytesAtSample) {
        final long uptimeMillis = SystemClock.uptimeMillis();
        final long mMillisecondsAtSample = this.mMillisecondsAtSample;
        long n2;
        if (0L != mMillisecondsAtSample) {
            final float n = (mBytesAtSample - this.mBytesAtSample) / (float)(uptimeMillis - mMillisecondsAtSample);
            final float mAverageDownloadSpeed = this.mAverageDownloadSpeed;
            float mAverageDownloadSpeed2 = n;
            if (0.0f != mAverageDownloadSpeed) {
                mAverageDownloadSpeed2 = n * 0.005f + mAverageDownloadSpeed * 0.995f;
            }
            this.mAverageDownloadSpeed = mAverageDownloadSpeed2;
            n2 = (long)((this.mTotalLength - mBytesAtSample) / this.mAverageDownloadSpeed);
        }
        else {
            n2 = -1L;
        }
        this.mMillisecondsAtSample = uptimeMillis;
        this.mBytesAtSample = mBytesAtSample;
        this.mNotification.onDownloadProgress(new DownloadProgressInfo(this.mTotalLength, mBytesAtSample, n2, this.mAverageDownloadSpeed));
    }
    
    @Override
    public IBinder onBind(final Intent intent) {
        Log.d("LVLDL", "Service Bound");
        return this.mServiceMessenger.getBinder();
    }
    
    @Override
    public void onClientUpdated(final Messenger messenger) {
        this.mClientMessenger = messenger;
        this.mNotification.setMessenger(messenger);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            this.mPackageInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            (this.mNotification = new DownloadNotification(this, this.getNotificationChannelId(), this.getPackageManager().getApplicationLabel(this.getApplicationInfo()))).startForeground();
        }
        catch (PackageManager$NameNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onDestroy() {
        final BroadcastReceiver mConnReceiver = this.mConnReceiver;
        if (mConnReceiver != null) {
            this.unregisterReceiver(mConnReceiver);
            this.mConnReceiver = null;
        }
        this.mServiceStub.disconnect((Context)this);
        super.onDestroy();
    }
    
    @Override
    protected void onHandleIntent(final Intent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //     4: aload_0        
        //     5: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloadsDB.getDB:(Landroid/content/Context;)Lcom/google/android/vending/expansion/downloader/impl/DownloadsDB;
        //     8: astore          9
        //    10: aload_1        
        //    11: ldc             "EPI"
        //    13: invokevirtual   android/content/Intent.getParcelableExtra:(Ljava/lang/String;)Landroid/os/Parcelable;
        //    16: checkcast       Landroid/app/PendingIntent;
        //    19: astore_1       
        //    20: aload_1        
        //    21: ifnull          40
        //    24: aload_0        
        //    25: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mNotification:Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;
        //    28: aload_1        
        //    29: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadNotification.setClientIntent:(Landroid/app/PendingIntent;)V
        //    32: aload_0        
        //    33: aload_1        
        //    34: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mPendingIntent:Landroid/app/PendingIntent;
        //    37: goto            58
        //    40: aload_0        
        //    41: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mPendingIntent:Landroid/app/PendingIntent;
        //    44: ifnull          599
        //    47: aload_0        
        //    48: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mNotification:Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;
        //    51: aload_0        
        //    52: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mPendingIntent:Landroid/app/PendingIntent;
        //    55: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadNotification.setClientIntent:(Landroid/app/PendingIntent;)V
        //    58: aload           9
        //    60: aload_0        
        //    61: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mPackageInfo:Landroid/content/pm/PackageInfo;
        //    64: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.isLVLCheckRequired:(Lcom/google/android/vending/expansion/downloader/impl/DownloadsDB;Landroid/content/pm/PackageInfo;)Z
        //    67: ifeq            80
        //    70: aload_0        
        //    71: aload_0        
        //    72: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloaderService.updateLVL:(Landroid/content/Context;)V
        //    75: iconst_0       
        //    76: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //    79: return         
        //    80: aload           9
        //    82: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadsDB.getDownloads:()[Lcom/google/android/vending/expansion/downloader/impl/DownloadInfo;
        //    85: astore_1       
        //    86: aload_0        
        //    87: lconst_0       
        //    88: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mBytesSoFar:J
        //    91: aload_0        
        //    92: lconst_0       
        //    93: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mTotalLength:J
        //    96: aload_0        
        //    97: aload_1        
        //    98: arraylength    
        //    99: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mFileCount:I
        //   102: aload_1        
        //   103: arraylength    
        //   104: istore_3       
        //   105: iconst_0       
        //   106: istore_2       
        //   107: iload_2        
        //   108: iload_3        
        //   109: if_icmpge       201
        //   112: aload_1        
        //   113: iload_2        
        //   114: aaload         
        //   115: astore          10
        //   117: aload           10
        //   119: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mStatus:I
        //   122: sipush          200
        //   125: if_icmpne       158
        //   128: aload_0        
        //   129: aload           10
        //   131: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mFileName:Ljava/lang/String;
        //   134: aload           10
        //   136: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mTotalBytes:J
        //   139: iconst_1       
        //   140: invokestatic    com/google/android/vending/expansion/downloader/Helpers.doesFileExist:(Landroid/content/Context;Ljava/lang/String;JZ)Z
        //   143: ifne            158
        //   146: aload           10
        //   148: iconst_0       
        //   149: putfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mStatus:I
        //   152: aload           10
        //   154: lconst_0       
        //   155: putfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mCurrentBytes:J
        //   158: aload_0        
        //   159: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mTotalLength:J
        //   162: lstore          5
        //   164: aload           10
        //   166: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mTotalBytes:J
        //   169: lstore          7
        //   171: aload_0        
        //   172: lload           5
        //   174: lload           7
        //   176: ladd           
        //   177: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mTotalLength:J
        //   180: aload_0        
        //   181: aload_0        
        //   182: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mBytesSoFar:J
        //   185: aload           10
        //   187: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mCurrentBytes:J
        //   190: ladd           
        //   191: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mBytesSoFar:J
        //   194: iload_2        
        //   195: iconst_1       
        //   196: iadd           
        //   197: istore_2       
        //   198: goto            107
        //   201: aload_0        
        //   202: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloaderService.pollNetworkState:()V
        //   205: aload_0        
        //   206: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mConnReceiver:Landroid/content/BroadcastReceiver;
        //   209: ifnonnull       256
        //   212: aload_0        
        //   213: new             Lcom/google/android/vending/expansion/downloader/impl/DownloaderService$InnerBroadcastReceiver;
        //   216: dup            
        //   217: aload_0        
        //   218: aload_0        
        //   219: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService$InnerBroadcastReceiver.<init>:(Lcom/google/android/vending/expansion/downloader/impl/DownloaderService;Landroid/app/Service;)V
        //   222: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mConnReceiver:Landroid/content/BroadcastReceiver;
        //   225: new             Landroid/content/IntentFilter;
        //   228: dup            
        //   229: ldc_w           "android.net.conn.CONNECTIVITY_CHANGE"
        //   232: invokespecial   android/content/IntentFilter.<init>:(Ljava/lang/String;)V
        //   235: astore          10
        //   237: aload           10
        //   239: ldc_w           "android.net.wifi.WIFI_STATE_CHANGED"
        //   242: invokevirtual   android/content/IntentFilter.addAction:(Ljava/lang/String;)V
        //   245: aload_0        
        //   246: aload_0        
        //   247: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mConnReceiver:Landroid/content/BroadcastReceiver;
        //   250: aload           10
        //   252: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloaderService.registerReceiver:(Landroid/content/BroadcastReceiver;Landroid/content/IntentFilter;)Landroid/content/Intent;
        //   255: pop            
        //   256: aload_1        
        //   257: arraylength    
        //   258: istore_3       
        //   259: iconst_0       
        //   260: istore_2       
        //   261: iload_2        
        //   262: iload_3        
        //   263: if_icmpge       586
        //   266: aload_1        
        //   267: iload_2        
        //   268: aaload         
        //   269: astore          10
        //   271: aload           10
        //   273: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mCurrentBytes:J
        //   276: lstore          5
        //   278: new             Ljava/lang/StringBuilder;
        //   281: dup            
        //   282: invokespecial   java/lang/StringBuilder.<init>:()V
        //   285: astore          11
        //   287: aload           11
        //   289: ldc_w           "onHandleIntent downloadInfo status: "
        //   292: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   295: pop            
        //   296: aload           11
        //   298: aload           10
        //   300: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mStatus:I
        //   303: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   306: pop            
        //   307: ldc             "LVLDL"
        //   309: aload           11
        //   311: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   314: invokestatic    android/util/Log.v:(Ljava/lang/String;Ljava/lang/String;)I
        //   317: pop            
        //   318: aload           10
        //   320: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mStatus:I
        //   323: sipush          200
        //   326: if_icmpeq       365
        //   329: new             Lcom/google/android/vending/expansion/downloader/impl/DownloadThread;
        //   332: dup            
        //   333: aload           10
        //   335: aload_0        
        //   336: aload_0        
        //   337: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mNotification:Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;
        //   340: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloadThread.<init>:(Lcom/google/android/vending/expansion/downloader/impl/DownloadInfo;Lcom/google/android/vending/expansion/downloader/impl/DownloaderService;Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;)V
        //   343: astore          11
        //   345: aload_0        
        //   346: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService.cancelAlarms:()V
        //   349: aload_0        
        //   350: ldc2_w          5000
        //   353: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService.scheduleAlarm:(J)V
        //   356: aload           11
        //   358: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadThread.run:()V
        //   361: aload_0        
        //   362: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService.cancelAlarms:()V
        //   365: aload           9
        //   367: aload           10
        //   369: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadsDB.updateFromDb:(Lcom/google/android/vending/expansion/downloader/impl/DownloadInfo;)Z
        //   372: pop            
        //   373: aload           10
        //   375: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mStatus:I
        //   378: istore          4
        //   380: iload           4
        //   382: sipush          200
        //   385: if_icmpeq       548
        //   388: iload           4
        //   390: sipush          403
        //   393: if_icmpeq       538
        //   396: iload           4
        //   398: sipush          487
        //   401: if_icmpeq       487
        //   404: iload           4
        //   406: sipush          490
        //   409: if_icmpeq       662
        //   412: iload           4
        //   414: sipush          498
        //   417: if_icmpeq       656
        //   420: iload           4
        //   422: sipush          499
        //   425: if_icmpeq       650
        //   428: iload           4
        //   430: tableswitch {
        //              386: 644
        //              387: 638
        //              388: 638
        //              389: 464
        //              390: 464
        //          default: 624
        //        }
        //   464: aload_0        
        //   465: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mWifiManager:Landroid/net/wifi/WifiManager;
        //   468: ifnull          632
        //   471: aload_0        
        //   472: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mWifiManager:Landroid/net/wifi/WifiManager;
        //   475: invokevirtual   android/net/wifi/WifiManager.isWifiEnabled:()Z
        //   478: ifne            632
        //   481: bipush          8
        //   483: istore_2       
        //   484: goto            665
        //   487: aload           10
        //   489: lconst_0       
        //   490: putfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mCurrentBytes:J
        //   493: aload           9
        //   495: aload           10
        //   497: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadsDB.updateDownload:(Lcom/google/android/vending/expansion/downloader/impl/DownloadInfo;)Z
        //   500: pop            
        //   501: bipush          13
        //   503: istore_2       
        //   504: goto            665
        //   507: iload_3        
        //   508: ifeq            521
        //   511: aload_0        
        //   512: ldc2_w          60000
        //   515: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService.scheduleAlarm:(J)V
        //   518: goto            525
        //   521: aload_0        
        //   522: invokespecial   com/google/android/vending/expansion/downloader/impl/DownloaderService.cancelAlarms:()V
        //   525: aload_0        
        //   526: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mNotification:Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;
        //   529: iload_2        
        //   530: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadNotification.onDownloadStateChanged:(I)V
        //   533: iconst_0       
        //   534: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //   537: return         
        //   538: aload_0        
        //   539: aload_0        
        //   540: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloaderService.updateLVL:(Landroid/content/Context;)V
        //   543: iconst_0       
        //   544: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //   547: return         
        //   548: aload_0        
        //   549: aload_0        
        //   550: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mBytesSoFar:J
        //   553: aload           10
        //   555: getfield        com/google/android/vending/expansion/downloader/impl/DownloadInfo.mCurrentBytes:J
        //   558: lload           5
        //   560: lsub           
        //   561: ladd           
        //   562: putfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mBytesSoFar:J
        //   565: aload           9
        //   567: aload_0        
        //   568: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mPackageInfo:Landroid/content/pm/PackageInfo;
        //   571: getfield        android/content/pm/PackageInfo.versionCode:I
        //   574: iconst_0       
        //   575: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadsDB.updateMetadata:(II)Z
        //   578: pop            
        //   579: iload_2        
        //   580: iconst_1       
        //   581: iadd           
        //   582: istore_2       
        //   583: goto            261
        //   586: aload_0        
        //   587: getfield        com/google/android/vending/expansion/downloader/impl/DownloaderService.mNotification:Lcom/google/android/vending/expansion/downloader/impl/DownloadNotification;
        //   590: iconst_5       
        //   591: invokevirtual   com/google/android/vending/expansion/downloader/impl/DownloadNotification.onDownloadStateChanged:(I)V
        //   594: iconst_0       
        //   595: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //   598: return         
        //   599: ldc             "LVLDL"
        //   601: ldc_w           "Downloader started in bad state without notification intent."
        //   604: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   607: pop            
        //   608: iconst_0       
        //   609: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //   612: return         
        //   613: astore_1       
        //   614: goto            618
        //   617: astore_1       
        //   618: iconst_0       
        //   619: invokestatic    com/google/android/vending/expansion/downloader/impl/DownloaderService.setServiceRunning:(Z)V
        //   622: aload_1        
        //   623: athrow         
        //   624: bipush          19
        //   626: istore_2       
        //   627: iconst_0       
        //   628: istore_3       
        //   629: goto            507
        //   632: bipush          9
        //   634: istore_2       
        //   635: goto            665
        //   638: bipush          6
        //   640: istore_2       
        //   641: goto            665
        //   644: bipush          7
        //   646: istore_2       
        //   647: goto            627
        //   650: bipush          14
        //   652: istore_2       
        //   653: goto            665
        //   656: bipush          17
        //   658: istore_2       
        //   659: goto            665
        //   662: bipush          18
        //   664: istore_2       
        //   665: iconst_1       
        //   666: istore_3       
        //   667: goto            507
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  4      20     617    618    Any
        //  24     37     617    618    Any
        //  40     58     617    618    Any
        //  58     75     617    618    Any
        //  80     105    617    618    Any
        //  117    158    617    618    Any
        //  158    171    617    618    Any
        //  171    194    613    617    Any
        //  201    256    613    617    Any
        //  256    259    613    617    Any
        //  271    365    613    617    Any
        //  365    380    613    617    Any
        //  464    481    613    617    Any
        //  487    501    613    617    Any
        //  511    518    613    617    Any
        //  521    525    613    617    Any
        //  525    533    613    617    Any
        //  538    543    613    617    Any
        //  548    579    613    617    Any
        //  586    594    613    617    Any
        //  599    608    613    617    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    @Override
    public int onStartCommand(final Intent intent, int onStartCommand, final int n) {
        onStartCommand = super.onStartCommand(intent, onStartCommand, n);
        final DownloadNotification mNotification = this.mNotification;
        if (mNotification != null) {
            mNotification.startForeground();
        }
        return onStartCommand;
    }
    
    void pollNetworkState() {
        if (this.mConnectivityManager == null) {
            this.mConnectivityManager = (ConnectivityManager)this.getSystemService("connectivity");
        }
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager)this.getApplicationContext().getSystemService("wifi");
        }
        final ConnectivityManager mConnectivityManager = this.mConnectivityManager;
        if (mConnectivityManager == null) {
            Log.w("LVLDL", "couldn't get connectivity manager to poll network state");
            return;
        }
        this.updateNetworkState(mConnectivityManager.getActiveNetworkInfo());
    }
    
    @Override
    public void requestAbortDownload() {
        this.mControl = 1;
        this.mStatus = 490;
    }
    
    @Override
    public void requestContinueDownload() {
        if (this.mControl == 1) {
            this.mControl = 0;
        }
        final Intent intent = new Intent((Context)this, (Class)this.getClass());
        intent.putExtra("EPI", (Parcelable)this.mPendingIntent);
        ContextCompat.startForegroundService((Context)this, intent);
    }
    
    @Override
    public void requestDownloadStatus() {
        this.mNotification.resendState();
    }
    
    @Override
    public void requestPauseDownload() {
        this.mControl = 1;
        this.mStatus = 193;
    }
    
    @Override
    public void setDownloadFlags(final int n) {
        DownloadsDB.getDB((Context)this).updateFlags(n);
    }
    
    @Override
    protected boolean shouldStop() {
        return DownloadsDB.getDB((Context)this).mStatus == 0;
    }
    
    public void updateLVL(Context applicationContext) {
        applicationContext = applicationContext.getApplicationContext();
        new Handler(applicationContext.getMainLooper()).post((Runnable)new LVLRunnable(applicationContext, this.mPendingIntent));
    }
    
    public static class GenerateSaveFileError extends Exception
    {
        private static final long serialVersionUID = 3465966015408936540L;
        String mMessage;
        int mStatus;
        
        public GenerateSaveFileError(final int mStatus, final String mMessage) {
            this.mStatus = mStatus;
            this.mMessage = mMessage;
        }
    }
    
    private class InnerBroadcastReceiver extends BroadcastReceiver
    {
        final Service mService;
        
        InnerBroadcastReceiver(final Service mService) {
            this.mService = mService;
        }
        
        public void onReceive(final Context context, Intent intent) {
            DownloaderService.this.pollNetworkState();
            if (DownloaderService.this.mStateChanged && !isServiceRunning()) {
                Log.d("LVLDL", "InnerBroadcastReceiver Called");
                intent = new Intent(context, (Class)this.mService.getClass());
                intent.putExtra("EPI", (Parcelable)DownloaderService.this.mPendingIntent);
                ContextCompat.startForegroundService(context, intent);
            }
        }
    }
    
    private class LVLRunnable implements Runnable
    {
        final Context mContext;
        
        LVLRunnable(final Context mContext, final PendingIntent pendingIntent) {
            this.mContext = mContext;
            DownloaderService.this.mPendingIntent = pendingIntent;
        }
        
        @Override
        public void run() {
            setServiceRunning(true);
            DownloaderService.this.mNotification.onDownloadStateChanged(2);
            final APKExpansionPolicy apkExpansionPolicy = new APKExpansionPolicy(this.mContext, new AESObfuscator(DownloaderService.this.getSALT(), this.mContext.getPackageName(), Settings$Secure.getString(this.mContext.getContentResolver(), "android_id")));
            apkExpansionPolicy.resetPolicy();
            new LicenseChecker(this.mContext, apkExpansionPolicy, DownloaderService.this.getPublicKey()).checkAccess(new LicenseCheckerCallback() {
                @Override
                public void allow(int startDownloadServiceIfRequired) {
                    while (true) {
                        while (true) {
                            Label_0476: {
                                try {
                                    final int expansionURLCount = apkExpansionPolicy.getExpansionURLCount();
                                    final DownloadsDB db = DownloadsDB.getDB(LVLRunnable.this.mContext);
                                    int n2;
                                    if (expansionURLCount != 0) {
                                        startDownloadServiceIfRequired = 0;
                                        int n = 0;
                                        while (true) {
                                            n2 = startDownloadServiceIfRequired;
                                            if (n >= expansionURLCount) {
                                                break;
                                            }
                                            final String expansionFileName = apkExpansionPolicy.getExpansionFileName(n);
                                            int n3 = startDownloadServiceIfRequired;
                                            if (expansionFileName != null) {
                                                final DownloadInfo downloadInfo = new DownloadInfo(n, expansionFileName, LVLRunnable.this.mContext.getPackageName());
                                                final long expansionFileSize = apkExpansionPolicy.getExpansionFileSize(n);
                                                if (DownloaderService.this.handleFileUpdated(db, n, expansionFileName, expansionFileSize)) {
                                                    startDownloadServiceIfRequired |= -1;
                                                    downloadInfo.resetDownload();
                                                    downloadInfo.mUri = apkExpansionPolicy.getExpansionURL(n);
                                                    downloadInfo.mTotalBytes = expansionFileSize;
                                                    downloadInfo.mStatus = startDownloadServiceIfRequired;
                                                    db.updateDownload(downloadInfo);
                                                    break Label_0476;
                                                }
                                                final DownloadInfo downloadInfoByFileName = db.getDownloadInfoByFileName(downloadInfo.mFileName);
                                                if (downloadInfoByFileName == null) {
                                                    final StringBuilder sb = new StringBuilder();
                                                    sb.append("file ");
                                                    sb.append(downloadInfo.mFileName);
                                                    sb.append(" found. Not downloading.");
                                                    Log.d("LVLDL", sb.toString());
                                                    downloadInfo.mStatus = 200;
                                                    downloadInfo.mTotalBytes = expansionFileSize;
                                                    downloadInfo.mCurrentBytes = expansionFileSize;
                                                    downloadInfo.mUri = apkExpansionPolicy.getExpansionURL(n);
                                                    db.updateDownload(downloadInfo);
                                                    n3 = startDownloadServiceIfRequired;
                                                }
                                                else {
                                                    n3 = startDownloadServiceIfRequired;
                                                    if (downloadInfoByFileName.mStatus != 200) {
                                                        downloadInfoByFileName.mUri = apkExpansionPolicy.getExpansionURL(n);
                                                        db.updateDownload(downloadInfoByFileName);
                                                        startDownloadServiceIfRequired |= -1;
                                                        break Label_0476;
                                                    }
                                                }
                                            }
                                            ++n;
                                            startDownloadServiceIfRequired = n3;
                                        }
                                    }
                                    else {
                                        n2 = 0;
                                    }
                                    try {
                                        db.updateMetadata(LVLRunnable.this.mContext.getPackageManager().getPackageInfo(LVLRunnable.this.mContext.getPackageName(), 0).versionCode, n2);
                                        startDownloadServiceIfRequired = DownloaderService.startDownloadServiceIfRequired(LVLRunnable.this.mContext, DownloaderService.this.mPendingIntent, DownloaderService.this.getClass());
                                        if (startDownloadServiceIfRequired != 0) {
                                            if (startDownloadServiceIfRequired == 1) {
                                                Log.e("LVLDL", "In LVL checking loop!");
                                                DownloaderService.this.mNotification.onDownloadStateChanged(15);
                                                throw new RuntimeException("Error with LVL checking and database integrity");
                                            }
                                        }
                                        else {
                                            DownloaderService.this.mNotification.onDownloadStateChanged(5);
                                        }
                                        return;
                                    }
                                    catch (PackageManager$NameNotFoundException ex) {
                                        ex.printStackTrace();
                                        throw new RuntimeException("Error with getting information from package name");
                                    }
                                }
                                finally {
                                    setServiceRunning(false);
                                }
                            }
                            int n3 = startDownloadServiceIfRequired;
                            continue;
                        }
                    }
                }
                
                @Override
                public void applicationError(final int n) {
                    try {
                        DownloaderService.this.mNotification.onDownloadStateChanged(16);
                    }
                    finally {
                        setServiceRunning(false);
                    }
                }
                
                @Override
                public void dontAllow(final int n) {
                    Label_0035: {
                        if (n == 291) {
                            break Label_0035;
                        }
                        if (n != 561) {
                            return;
                        }
                        try {
                            DownloaderService.this.mNotification.onDownloadStateChanged(15);
                            return;
                            DownloaderService.this.mNotification.onDownloadStateChanged(16);
                        }
                        finally {
                            setServiceRunning(false);
                        }
                    }
                }
            });
        }
    }
}
