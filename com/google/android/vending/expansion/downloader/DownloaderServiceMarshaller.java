package com.google.android.vending.expansion.downloader;

import android.os.*;
import android.content.*;

public class DownloaderServiceMarshaller
{
    public static final int MSG_REQUEST_ABORT_DOWNLOAD = 1;
    public static final int MSG_REQUEST_CLIENT_UPDATE = 6;
    public static final int MSG_REQUEST_CONTINUE_DOWNLOAD = 4;
    public static final int MSG_REQUEST_DOWNLOAD_STATE = 5;
    public static final int MSG_REQUEST_PAUSE_DOWNLOAD = 2;
    public static final int MSG_SET_DOWNLOAD_FLAGS = 3;
    public static final String PARAMS_FLAGS = "flags";
    public static final String PARAM_MESSENGER = "EMH";
    
    public static IDownloaderService CreateProxy(final Messenger messenger) {
        return new Proxy(messenger);
    }
    
    public static IStub CreateStub(final IDownloaderService downloaderService) {
        return new Stub(downloaderService);
    }
    
    private static class Proxy implements IDownloaderService
    {
        private Messenger mMsg;
        
        public Proxy(final Messenger mMsg) {
            this.mMsg = mMsg;
        }
        
        private void send(final int n, final Bundle data) {
            final Message obtain = Message.obtain((Handler)null, n);
            obtain.setData(data);
            try {
                this.mMsg.send(obtain);
            }
            catch (RemoteException ex) {
                ex.printStackTrace();
            }
        }
        
        @Override
        public void onClientUpdated(final Messenger messenger) {
            final Bundle bundle = new Bundle(1);
            bundle.putParcelable("EMH", (Parcelable)messenger);
            this.send(6, bundle);
        }
        
        @Override
        public void requestAbortDownload() {
            this.send(1, new Bundle());
        }
        
        @Override
        public void requestContinueDownload() {
            this.send(4, new Bundle());
        }
        
        @Override
        public void requestDownloadStatus() {
            this.send(5, new Bundle());
        }
        
        @Override
        public void requestPauseDownload() {
            this.send(2, new Bundle());
        }
        
        @Override
        public void setDownloadFlags(final int n) {
            final Bundle bundle = new Bundle();
            bundle.putInt("flags", n);
            this.send(3, bundle);
        }
    }
    
    private static class Stub implements IStub
    {
        private IDownloaderService mItf;
        final Messenger mMessenger;
        
        public Stub(final IDownloaderService mItf) {
            this.mItf = null;
            this.mMessenger = new Messenger((Handler)new Handler() {
                public void handleMessage(final Message message) {
                    switch (message.what) {
                        default: {}
                        case 6: {
                            Stub.this.mItf.onClientUpdated((Messenger)message.getData().getParcelable("EMH"));
                        }
                        case 5: {
                            Stub.this.mItf.requestDownloadStatus();
                        }
                        case 4: {
                            Stub.this.mItf.requestContinueDownload();
                        }
                        case 3: {
                            Stub.this.mItf.setDownloadFlags(message.getData().getInt("flags"));
                        }
                        case 2: {
                            Stub.this.mItf.requestPauseDownload();
                        }
                        case 1: {
                            Stub.this.mItf.requestAbortDownload();
                        }
                    }
                }
            });
            this.mItf = mItf;
        }
        
        @Override
        public void connect(final Context context) {
        }
        
        @Override
        public void disconnect(final Context context) {
        }
        
        @Override
        public Messenger getMessenger() {
            return this.mMessenger;
        }
    }
}
