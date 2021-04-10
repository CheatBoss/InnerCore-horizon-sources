package com.google.android.vending.expansion.downloader.impl;

import com.google.android.vending.expansion.downloader.*;
import android.util.*;

public class DownloadInfo
{
    public int mControl;
    public long mCurrentBytes;
    public String mETag;
    public final String mFileName;
    public int mFuzz;
    public final int mIndex;
    boolean mInitialized;
    public long mLastMod;
    public int mNumFailed;
    public int mRedirectCount;
    public int mRetryAfter;
    public int mStatus;
    public long mTotalBytes;
    public String mUri;
    
    public DownloadInfo(final int mIndex, final String mFileName, final String s) {
        this.mFuzz = Helpers.sRandom.nextInt(1001);
        this.mFileName = mFileName;
        this.mIndex = mIndex;
    }
    
    public void logVerboseInfo() {
        Log.v("LVLDL", "Service adding new entry");
        final StringBuilder sb = new StringBuilder();
        sb.append("FILENAME: ");
        sb.append(this.mFileName);
        Log.v("LVLDL", sb.toString());
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("URI     : ");
        sb2.append(this.mUri);
        Log.v("LVLDL", sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("FILENAME: ");
        sb3.append(this.mFileName);
        Log.v("LVLDL", sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("CONTROL : ");
        sb4.append(this.mControl);
        Log.v("LVLDL", sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("STATUS  : ");
        sb5.append(this.mStatus);
        Log.v("LVLDL", sb5.toString());
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("FAILED_C: ");
        sb6.append(this.mNumFailed);
        Log.v("LVLDL", sb6.toString());
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("RETRY_AF: ");
        sb7.append(this.mRetryAfter);
        Log.v("LVLDL", sb7.toString());
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("REDIRECT: ");
        sb8.append(this.mRedirectCount);
        Log.v("LVLDL", sb8.toString());
        final StringBuilder sb9 = new StringBuilder();
        sb9.append("LAST_MOD: ");
        sb9.append(this.mLastMod);
        Log.v("LVLDL", sb9.toString());
        final StringBuilder sb10 = new StringBuilder();
        sb10.append("TOTAL   : ");
        sb10.append(this.mTotalBytes);
        Log.v("LVLDL", sb10.toString());
        final StringBuilder sb11 = new StringBuilder();
        sb11.append("CURRENT : ");
        sb11.append(this.mCurrentBytes);
        Log.v("LVLDL", sb11.toString());
        final StringBuilder sb12 = new StringBuilder();
        sb12.append("ETAG    : ");
        sb12.append(this.mETag);
        Log.v("LVLDL", sb12.toString());
    }
    
    public void resetDownload() {
        this.mCurrentBytes = 0L;
        this.mETag = "";
        this.mLastMod = 0L;
        this.mStatus = 0;
        this.mControl = 0;
        this.mNumFailed = 0;
        this.mRetryAfter = 0;
        this.mRedirectCount = 0;
    }
    
    public long restartTime(final long n) {
        final int mNumFailed = this.mNumFailed;
        if (mNumFailed == 0) {
            return n;
        }
        final int mRetryAfter = this.mRetryAfter;
        if (mRetryAfter > 0) {
            return this.mLastMod + mRetryAfter;
        }
        return this.mLastMod + (this.mFuzz + 1000) * 30 * (1 << mNumFailed - 1);
    }
}
