package com.google.android.vending.expansion.downloader;

import android.os.*;

public interface IDownloaderService
{
    public static final int FLAGS_DOWNLOAD_OVER_CELLULAR = 1;
    
    void onClientUpdated(final Messenger p0);
    
    void requestAbortDownload();
    
    void requestContinueDownload();
    
    void requestDownloadStatus();
    
    void requestPauseDownload();
    
    void setDownloadFlags(final int p0);
}
