package com.google.android.vending.expansion.downloader.impl;

import android.support.v4.app.*;
import android.app.*;
import android.content.*;
import com.mojang.minecraftpe.packagesource.*;
import com.google.android.vending.expansion.downloader.*;
import android.os.*;

public class DownloadNotification implements IDownloaderClient
{
    static final String LOGTAG = "DownloadNotification";
    static final int NOTIFICATION_ID = -908767821;
    private NotificationCompat.Builder mActiveDownloadBuilder;
    private NotificationCompat.Builder mBuilder;
    private String mChannelId;
    private IDownloaderClient mClientProxy;
    private PendingIntent mContentIntent;
    private NotificationCompat.Builder mCurrentBuilder;
    private String mCurrentText;
    private CharSequence mLabel;
    private Notification mLatestNotification;
    private final NotificationManager mNotificationManager;
    private DownloadProgressInfo mProgressInfo;
    private final Service mService;
    private int mState;
    
    DownloadNotification(final Service mService, final String channelId, final CharSequence mLabel) {
        this.mState = -1;
        this.mService = mService;
        this.mChannelId = channelId;
        this.mLabel = mLabel;
        this.mNotificationManager = (NotificationManager)mService.getSystemService("notification");
        this.mActiveDownloadBuilder = new NotificationCompat.Builder((Context)this.mService);
        this.mBuilder = new NotificationCompat.Builder((Context)this.mService);
        this.mActiveDownloadBuilder.setPriority(-1);
        this.mActiveDownloadBuilder.setCategory("progress");
        this.mBuilder.setChannelId(channelId);
        this.mActiveDownloadBuilder.setChannelId(channelId);
        this.mBuilder.setPriority(-1);
        this.mBuilder.setCategory("progress");
        this.mCurrentBuilder = this.mBuilder;
        this.mCurrentText = PackageSource.getStringResource(PackageSource.StringResourceId.STATE_UNKNOWN);
    }
    
    public PendingIntent getClientIntent() {
        return this.mContentIntent;
    }
    
    @Override
    public void onDownloadProgress(final DownloadProgressInfo mProgressInfo) {
        this.mProgressInfo = mProgressInfo;
        final IDownloaderClient mClientProxy = this.mClientProxy;
        if (mClientProxy != null) {
            mClientProxy.onDownloadProgress(mProgressInfo);
        }
        NotificationCompat.Builder mCurrentBuilder;
        if (mProgressInfo.mOverallTotal <= 0L) {
            this.mBuilder.setTicker(this.mLabel);
            this.mBuilder.setSmallIcon(17301633);
            this.mBuilder.setContentTitle(this.mLabel);
            this.mBuilder.setContentText(this.mCurrentText);
            mCurrentBuilder = this.mBuilder;
        }
        else {
            this.mActiveDownloadBuilder.setProgress((int)mProgressInfo.mOverallTotal, (int)mProgressInfo.mOverallProgress, false);
            this.mActiveDownloadBuilder.setContentText(Helpers.getDownloadProgressString(mProgressInfo.mOverallProgress, mProgressInfo.mOverallTotal));
            this.mActiveDownloadBuilder.setSmallIcon(17301633);
            final NotificationCompat.Builder mActiveDownloadBuilder = this.mActiveDownloadBuilder;
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.mLabel);
            sb.append(": ");
            sb.append(this.mCurrentText);
            mActiveDownloadBuilder.setTicker(sb.toString());
            this.mActiveDownloadBuilder.setContentTitle(this.mLabel);
            this.mActiveDownloadBuilder.setContentInfo(String.format(PackageSource.getStringResource(PackageSource.StringResourceId.TIME_REMAINING_NOTIFICATION), Helpers.getTimeRemaining(mProgressInfo.mTimeRemaining)));
            mCurrentBuilder = this.mActiveDownloadBuilder;
        }
        this.mCurrentBuilder = mCurrentBuilder;
        final Notification build = this.mCurrentBuilder.build();
        this.mLatestNotification = build;
        this.mNotificationManager.notify(DownloadNotification.NOTIFICATION_ID, build);
    }
    
    @Override
    public void onDownloadStateChanged(int n) {
        final IDownloaderClient mClientProxy = this.mClientProxy;
        if (mClientProxy != null) {
            mClientProxy.onDownloadStateChanged(n);
        }
        if (n != this.mState && (this.mState = n) != 1) {
            if (this.mContentIntent == null) {
                return;
            }
            final int n2 = 17301634;
            Enum<PackageSource.StringResourceId> enum1 = null;
            boolean b2 = false;
            Label_0176: {
                Label_0174: {
                    Label_0171: {
                        if (n != 0) {
                            Label_0155: {
                                if (n != 7) {
                                    int n3 = n2;
                                    Label_0146: {
                                        if (n != 2) {
                                            n3 = n2;
                                            if (n != 3) {
                                                if (n != 4) {
                                                    if (n == 5) {
                                                        break Label_0155;
                                                    }
                                                    switch (n) {
                                                        default: {
                                                            enum1 = Helpers.getDownloaderStringResourceIDFromPlaystoreState(n);
                                                            n3 = 17301642;
                                                            break Label_0146;
                                                        }
                                                        case 15:
                                                        case 16:
                                                        case 17:
                                                        case 18:
                                                        case 19: {
                                                            enum1 = Helpers.getDownloaderStringResourceIDFromPlaystoreState(n);
                                                            break Label_0171;
                                                        }
                                                    }
                                                }
                                                else {
                                                    n3 = 17301633;
                                                }
                                            }
                                        }
                                        enum1 = Helpers.getDownloaderStringResourceIDFromPlaystoreState(n);
                                    }
                                    final boolean b = true;
                                    n = n3;
                                    b2 = b;
                                    break Label_0176;
                                }
                            }
                            enum1 = Helpers.getDownloaderStringResourceIDFromPlaystoreState(n);
                            n = n2;
                            break Label_0174;
                        }
                        enum1 = PackageSource.StringResourceId.STATE_UNKNOWN;
                    }
                    n = 17301642;
                }
                b2 = false;
            }
            this.mCurrentText = PackageSource.getStringResource((PackageSource.StringResourceId)enum1);
            final NotificationCompat.Builder mCurrentBuilder = this.mCurrentBuilder;
            final StringBuilder sb = new StringBuilder();
            sb.append((Object)this.mLabel);
            sb.append(": ");
            sb.append(this.mCurrentText);
            mCurrentBuilder.setTicker(sb.toString());
            this.mCurrentBuilder.setSmallIcon(n);
            this.mCurrentBuilder.setContentTitle(this.mLabel);
            this.mCurrentBuilder.setContentText(this.mCurrentText);
            if (b2) {
                this.mCurrentBuilder.setOngoing(true);
            }
            else {
                this.mCurrentBuilder.setOngoing(false);
                this.mCurrentBuilder.setAutoCancel(true);
            }
            final Notification build = this.mCurrentBuilder.build();
            this.mLatestNotification = build;
            this.mNotificationManager.notify(DownloadNotification.NOTIFICATION_ID, build);
        }
    }
    
    @Override
    public void onServiceConnected(final Messenger messenger) {
    }
    
    public void resendState() {
        final IDownloaderClient mClientProxy = this.mClientProxy;
        if (mClientProxy != null) {
            mClientProxy.onDownloadStateChanged(this.mState);
        }
    }
    
    public void setClientIntent(final PendingIntent mContentIntent) {
        this.mBuilder.setContentIntent(mContentIntent);
        this.mActiveDownloadBuilder.setContentIntent(mContentIntent);
        this.mContentIntent = mContentIntent;
    }
    
    public void setMessenger(final Messenger messenger) {
        final IDownloaderClient createProxy = DownloaderClientMarshaller.CreateProxy(messenger);
        this.mClientProxy = createProxy;
        final DownloadProgressInfo mProgressInfo = this.mProgressInfo;
        if (mProgressInfo != null) {
            createProxy.onDownloadProgress(mProgressInfo);
        }
        final int mState = this.mState;
        if (mState != -1) {
            this.mClientProxy.onDownloadStateChanged(mState);
        }
    }
    
    public void startForeground() {
        if (Build$VERSION.SDK_INT >= 26) {
            if (this.mLatestNotification == null) {
                this.mBuilder.setTicker(this.mLabel);
                this.mBuilder.setSmallIcon(17301633);
                this.mBuilder.setContentTitle(this.mLabel);
                this.mBuilder.setContentText(this.mCurrentText);
                this.mLatestNotification = this.mBuilder.build();
            }
            this.mService.startForeground(DownloadNotification.NOTIFICATION_ID, this.mLatestNotification);
        }
    }
}
