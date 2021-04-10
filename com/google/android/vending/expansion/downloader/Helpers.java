package com.google.android.vending.expansion.downloader;

import android.content.*;
import java.io.*;
import android.util.*;
import com.mojang.minecraftpe.packagesource.*;
import android.os.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

public class Helpers
{
    private static final Pattern CONTENT_DISPOSITION_PATTERN;
    public static final int FS_CANNOT_READ = 2;
    public static final int FS_DOES_NOT_EXIST = 1;
    public static final int FS_READABLE = 0;
    public static Random sRandom;
    
    static {
        Helpers.sRandom = new Random(SystemClock.uptimeMillis());
        CONTENT_DISPOSITION_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");
    }
    
    private Helpers() {
    }
    
    public static boolean canWriteOBBFile(final Context context) {
        final File file = new File(getSaveFilePath(context));
        if (file.exists()) {
            return file.isDirectory() && file.canWrite();
        }
        return file.mkdirs();
    }
    
    static void deleteFile(final String s) {
        try {
            new File(s).delete();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("file: '");
            sb.append(s);
            sb.append("' couldn't be deleted");
            Log.w("LVLDL", sb.toString(), (Throwable)ex);
        }
    }
    
    public static boolean doesFileExist(final Context context, final String s, final long n, final boolean b) {
        final File file = new File(generateSaveFileName(context, s));
        if (file.exists()) {
            if (file.length() == n) {
                return true;
            }
            if (b) {
                file.delete();
            }
        }
        return false;
    }
    
    public static String generateSaveFileName(final Context context, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(getSaveFilePath(context));
        sb.append(File.separator);
        sb.append(s);
        return sb.toString();
    }
    
    public static long getAvailableBytes(final File file) {
        final StatFs statFs = new StatFs(file.getPath());
        return statFs.getBlockSize() * (statFs.getAvailableBlocks() - 4L);
    }
    
    public static String getDownloadProgressPercent(final long n, final long n2) {
        if (n2 == 0L) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(n * 100L / n2));
        sb.append("%");
        return sb.toString();
    }
    
    public static String getDownloadProgressString(final long n, final long n2) {
        if (n2 == 0L) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(String.format("%.2f", n / 1048576.0f));
        sb.append("MB /");
        sb.append(String.format("%.2f", n2 / 1048576.0f));
        sb.append("MB");
        return sb.toString();
    }
    
    public static String getDownloadProgressStringNotification(final long n, final long n2) {
        if (n2 == 0L) {
            return "";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(getDownloadProgressString(n, n2));
        sb.append(" (");
        sb.append(getDownloadProgressPercent(n, n2));
        sb.append(")");
        return sb.toString();
    }
    
    public static PackageSource.StringResourceId getDownloaderStringResourceIDFromPlaystoreState(final int n) {
        switch (n) {
            default: {
                return PackageSource.StringResourceId.STATE_UNKNOWN;
            }
            case 18: {
                return PackageSource.StringResourceId.STATE_FAILED_CANCELLED;
            }
            case 17: {
                return PackageSource.StringResourceId.STATE_FAILED_SDCARD_FULL;
            }
            case 16: {
                return PackageSource.StringResourceId.STATE_FAILED_FETCHING_URL;
            }
            case 15: {
                return PackageSource.StringResourceId.STATE_FAILED_UNLICENSED;
            }
            case 14: {
                return PackageSource.StringResourceId.STATE_PAUSED_SDCARD_UNAVAILABLE;
            }
            case 13: {
                return PackageSource.StringResourceId.STATE_PAUSED_NETWORK_SETUP_FAILURE;
            }
            case 12: {
                return PackageSource.StringResourceId.STATE_PAUSED_ROAMING;
            }
            case 11: {
                return PackageSource.StringResourceId.STATE_PAUSED_WIFI_UNAVAILABLE;
            }
            case 10: {
                return PackageSource.StringResourceId.STATE_PAUSED_WIFI_DISABLED;
            }
            case 9: {
                return PackageSource.StringResourceId.STATE_PAUSED_WIFI_UNAVAILABLE;
            }
            case 8: {
                return PackageSource.StringResourceId.STATE_PAUSED_WIFI_DISABLED;
            }
            case 7: {
                return PackageSource.StringResourceId.STATE_PAUSED_BY_REQUEST;
            }
            case 6: {
                return PackageSource.StringResourceId.STATE_PAUSED_NETWORK_UNAVAILABLE;
            }
            case 5: {
                return PackageSource.StringResourceId.STATE_COMPLETED;
            }
            case 4: {
                return PackageSource.StringResourceId.STATE_DOWNLOADING;
            }
            case 3: {
                return PackageSource.StringResourceId.STATE_CONNECTING;
            }
            case 2: {
                return PackageSource.StringResourceId.STATE_FETCHING_URL;
            }
            case 1: {
                return PackageSource.StringResourceId.STATE_IDLE;
            }
        }
    }
    
    public static String getExpansionAPKFileName(final Context context, final boolean b, final int n) {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (b) {
            s = "main.";
        }
        else {
            s = "patch.";
        }
        sb.append(s);
        sb.append(n);
        sb.append(".");
        sb.append(context.getPackageName());
        sb.append(".obb");
        return sb.toString();
    }
    
    public static int getFileStatus(final Context context, final String s) {
        final File file = new File(generateSaveFileName(context, s));
        if (!file.exists()) {
            return 1;
        }
        if (file.canRead()) {
            return 0;
        }
        return 2;
    }
    
    public static File getFilesystemRoot(final String s) {
        final File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
        if (s.startsWith(downloadCacheDirectory.getPath())) {
            return downloadCacheDirectory;
        }
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (s.startsWith(externalStorageDirectory.getPath())) {
            return externalStorageDirectory;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot determine filesystem root for ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static String getSaveFilePath(final Context context) {
        if (Build$VERSION.SDK_INT >= 19) {
            return context.getObbDir().toString();
        }
        final File externalStorageDirectory = Environment.getExternalStorageDirectory();
        final StringBuilder sb = new StringBuilder();
        sb.append(externalStorageDirectory.toString());
        sb.append(Constants.EXP_PATH);
        sb.append(context.getPackageName());
        return sb.toString();
    }
    
    public static String getSpeedString(final float n) {
        return String.format("%.2f", n * 1000.0f / 1024.0f);
    }
    
    public static String getTimeRemaining(final long n) {
        SimpleDateFormat simpleDateFormat;
        if (n > 3600000L) {
            simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        }
        else {
            simpleDateFormat = new SimpleDateFormat("mm:ss", Locale.getDefault());
        }
        return simpleDateFormat.format(new Date(n - TimeZone.getDefault().getRawOffset()));
    }
    
    public static boolean isExternalMediaMounted() {
        return Environment.getExternalStorageState().equals("mounted");
    }
    
    public static boolean isFilenameValid(String replaceFirst) {
        replaceFirst = replaceFirst.replaceFirst("/+", "/");
        return replaceFirst.startsWith(Environment.getDownloadCacheDirectory().toString()) || replaceFirst.startsWith(Environment.getExternalStorageDirectory().toString());
    }
    
    static String parseContentDisposition(String group) {
        try {
            final Matcher matcher = Helpers.CONTENT_DISPOSITION_PATTERN.matcher(group);
            if (matcher.find()) {
                group = matcher.group(1);
                return group;
            }
        }
        catch (IllegalStateException ex) {}
        return null;
    }
}
