package com.google.android.vending.expansion.downloader;

import android.os.*;

public class DownloadProgressInfo implements Parcelable
{
    public static final Parcelable$Creator<DownloadProgressInfo> CREATOR;
    public float mCurrentSpeed;
    public long mOverallProgress;
    public long mOverallTotal;
    public long mTimeRemaining;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<DownloadProgressInfo>() {
            public DownloadProgressInfo createFromParcel(final Parcel parcel) {
                return new DownloadProgressInfo(parcel);
            }
            
            public DownloadProgressInfo[] newArray(final int n) {
                return new DownloadProgressInfo[n];
            }
        };
    }
    
    public DownloadProgressInfo(final long mOverallTotal, final long mOverallProgress, final long mTimeRemaining, final float mCurrentSpeed) {
        this.mOverallTotal = mOverallTotal;
        this.mOverallProgress = mOverallProgress;
        this.mTimeRemaining = mTimeRemaining;
        this.mCurrentSpeed = mCurrentSpeed;
    }
    
    public DownloadProgressInfo(final Parcel parcel) {
        this.mOverallTotal = parcel.readLong();
        this.mOverallProgress = parcel.readLong();
        this.mTimeRemaining = parcel.readLong();
        this.mCurrentSpeed = parcel.readFloat();
    }
    
    public int describeContents() {
        return 0;
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeLong(this.mOverallTotal);
        parcel.writeLong(this.mOverallProgress);
        parcel.writeLong(this.mTimeRemaining);
        parcel.writeFloat(this.mCurrentSpeed);
    }
}
