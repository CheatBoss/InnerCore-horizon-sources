package com.appsflyer.internal;

import android.content.*;
import android.os.*;
import com.appsflyer.*;
import java.security.cert.*;
import java.security.*;
import android.content.pm.*;

public class EventDataCollector
{
    private final Context \u03b9;
    
    public EventDataCollector(final Context \u03b9) {
        this.\u03b9 = \u03b9;
    }
    
    public long bootTime() {
        return System.currentTimeMillis() - SystemClock.elapsedRealtime();
    }
    
    public String disk() {
        final StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        long n;
        long n2;
        if (Build$VERSION.SDK_INT >= 18) {
            final long blockSizeLong = statFs.getBlockSizeLong();
            n = statFs.getAvailableBlocksLong() * blockSizeLong;
            n2 = statFs.getBlockCountLong() * blockSizeLong;
        }
        else {
            final int blockSize = statFs.getBlockSize();
            n = statFs.getAvailableBlocks() * blockSize;
            n2 = statFs.getBlockCount() * blockSize;
        }
        final double pow = Math.pow(2.0, 20.0);
        final double n3 = (double)n;
        Double.isNaN(n3);
        final long n4 = (long)(n3 / pow);
        final double n5 = (double)n2;
        Double.isNaN(n5);
        final long n6 = (long)(n5 / pow);
        final StringBuilder sb = new StringBuilder();
        sb.append(n4);
        sb.append("/");
        sb.append(n6);
        return sb.toString();
    }
    
    public String signature() throws CertificateException, NoSuchAlgorithmException, PackageManager$NameNotFoundException {
        return AndroidUtils.signature(this.\u03b9.getPackageManager(), this.\u03b9.getPackageName());
    }
}
