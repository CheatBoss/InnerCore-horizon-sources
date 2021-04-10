package com.google.android.vending.expansion.downloader;

import android.app.*;
import com.google.android.vending.expansion.downloader.impl.*;
import android.content.pm.*;
import android.content.*;
import android.os.*;

public class DownloaderClientMarshaller
{
    public static final int DOWNLOAD_REQUIRED = 2;
    public static final int LVL_CHECK_REQUIRED = 1;
    public static final int MSG_ONDOWNLOADPROGRESS = 11;
    public static final int MSG_ONDOWNLOADSTATE_CHANGED = 10;
    public static final int MSG_ONSERVICECONNECTED = 12;
    public static final int NO_DOWNLOAD_REQUIRED = 0;
    public static final String PARAM_MESSENGER = "EMH";
    public static final String PARAM_NEW_STATE = "newState";
    public static final String PARAM_PROGRESS = "progress";
    
    public static IDownloaderClient CreateProxy(final Messenger messenger) {
        return new Proxy(messenger);
    }
    
    public static IStub CreateStub(final IDownloaderClient downloaderClient, final Class<?> clazz) {
        return new Stub(downloaderClient, clazz);
    }
    
    public static int startDownloadServiceIfRequired(final Context context, final PendingIntent pendingIntent, final Class<?> clazz) throws PackageManager$NameNotFoundException {
        return DownloaderService.startDownloadServiceIfRequired(context, pendingIntent, clazz);
    }
    
    public static int startDownloadServiceIfRequired(final Context context, final Intent intent, final Class<?> clazz) throws PackageManager$NameNotFoundException {
        return DownloaderService.startDownloadServiceIfRequired(context, intent, clazz);
    }
    
    private static class Proxy implements IDownloaderClient
    {
        private Messenger mServiceMessenger;
        
        public Proxy(final Messenger mServiceMessenger) {
            this.mServiceMessenger = mServiceMessenger;
        }
        
        private void send(final int n, final Bundle data) {
            final Message obtain = Message.obtain((Handler)null, n);
            obtain.setData(data);
            try {
                this.mServiceMessenger.send(obtain);
            }
            catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        
        @Override
        public void onDownloadProgress(final DownloadProgressInfo downloadProgressInfo) {
            final Bundle bundle = new Bundle(1);
            bundle.putParcelable("progress", (Parcelable)downloadProgressInfo);
            this.send(11, bundle);
        }
        
        @Override
        public void onDownloadStateChanged(final int n) {
            final Bundle bundle = new Bundle(1);
            bundle.putInt("newState", n);
            this.send(10, bundle);
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger) {
        }
    }
    
    private static class Stub implements IStub
    {
        private boolean mBound;
        private ServiceConnection mConnection;
        private Context mContext;
        private Class<?> mDownloaderServiceClass;
        private IDownloaderClient mItf;
        final Messenger mMessenger;
        private Messenger mServiceMessenger;
        
        public Stub(final IDownloaderClient mItf, final Class<?> mDownloaderServiceClass) {
            this.mItf = null;
            this.mMessenger = new Messenger((Handler)new Handler() {
                public void handleMessage(final Message message) {
                    switch (message.what) {
                        default: {}
                        case 12: {
                            Stub.this.mItf.onServiceConnected((Messenger)message.getData().getParcelable("EMH"));
                        }
                        case 11: {
                            final Bundle data = message.getData();
                            if (Stub.this.mContext != null) {
                                data.setClassLoader(Stub.this.mContext.getClassLoader());
                                Stub.this.mItf.onDownloadProgress((DownloadProgressInfo)message.getData().getParcelable("progress"));
                                return;
                            }
                            break;
                        }
                        case 10: {
                            Stub.this.mItf.onDownloadStateChanged(message.getData().getInt("newState"));
                            break;
                        }
                    }
                }
            });
            this.mConnection = (ServiceConnection)new ServiceConnection() {
                public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                    Stub.this.mServiceMessenger = new Messenger(binder);
                    Stub.this.mItf.onServiceConnected(Stub.this.mServiceMessenger);
                }
                
                public void onServiceDisconnected(final ComponentName componentName) {
                    Stub.this.mServiceMessenger = null;
                }
            };
            this.mItf = mItf;
            this.mDownloaderServiceClass = mDownloaderServiceClass;
        }
        
        @Override
        public void connect(final Context mContext) {
            this.mContext = mContext;
            final Intent intent = new Intent(mContext, (Class)this.mDownloaderServiceClass);
            intent.putExtra("EMH", (Parcelable)this.mMessenger);
            if (!mContext.bindService(intent, this.mConnection, 2)) {
                return;
            }
            this.mBound = true;
        }
        
        @Override
        public void disconnect(final Context context) {
            if (this.mBound) {
                context.unbindService(this.mConnection);
                this.mBound = false;
            }
            this.mContext = null;
        }
        
        @Override
        public Messenger getMessenger() {
            return this.mMessenger;
        }
    }
}
