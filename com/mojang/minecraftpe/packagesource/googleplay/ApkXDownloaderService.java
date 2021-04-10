package com.mojang.minecraftpe.packagesource.googleplay;

import com.google.android.vending.expansion.downloader.impl.*;

public class ApkXDownloaderService extends DownloaderService
{
    @Override
    public String getAlarmReceiverClassName() {
        return ApkXDownloaderAlarmReceiver.class.getName();
    }
    
    @Override
    public String getNotificationChannelId() {
        return ApkXDownloaderClient.getNotificationChannelId();
    }
    
    @Override
    public String getPublicKey() {
        return ApkXDownloaderClient.getLicenseKey();
    }
    
    @Override
    public byte[] getSALT() {
        return ApkXDownloaderClient.SALT;
    }
}
