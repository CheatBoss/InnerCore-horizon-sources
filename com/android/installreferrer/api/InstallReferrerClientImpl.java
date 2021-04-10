package com.android.installreferrer.api;

import com.google.android.finsky.externalreferrer.*;
import com.android.installreferrer.commons.*;
import android.content.*;
import android.content.pm.*;
import java.util.*;
import java.lang.annotation.*;
import android.os.*;

class InstallReferrerClientImpl extends InstallReferrerClient
{
    private static final int PLAY_STORE_MIN_APP_VER = 80837300;
    private static final String SERVICE_ACTION_NAME = "com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE";
    private static final String SERVICE_NAME = "com.google.android.finsky.externalreferrer.GetInstallReferrerService";
    private static final String SERVICE_PACKAGE_NAME = "com.android.vending";
    private static final String TAG = "InstallReferrerClient";
    private final Context mApplicationContext;
    private int mClientState;
    private IGetInstallReferrerService mService;
    private ServiceConnection mServiceConnection;
    
    public InstallReferrerClientImpl(final Context context) {
        this.mClientState = 0;
        this.mApplicationContext = context.getApplicationContext();
    }
    
    private boolean isPlayStoreCompatible() {
        final PackageManager packageManager = this.mApplicationContext.getPackageManager();
        boolean b = false;
        try {
            if (packageManager.getPackageInfo("com.android.vending", 128).versionCode >= 80837300) {
                b = true;
            }
            return b;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    @Override
    public void endConnection() {
        this.mClientState = 3;
        if (this.mServiceConnection != null) {
            InstallReferrerCommons.logVerbose("InstallReferrerClient", "Unbinding from service.");
            this.mApplicationContext.unbindService(this.mServiceConnection);
            this.mServiceConnection = null;
        }
        this.mService = null;
    }
    
    @Override
    public ReferrerDetails getInstallReferrer() throws RemoteException {
        if (this.isReady()) {
            final Bundle bundle = new Bundle();
            bundle.putString("package_name", this.mApplicationContext.getPackageName());
            try {
                return new ReferrerDetails(this.mService.getInstallReferrer(bundle));
            }
            catch (RemoteException ex) {
                InstallReferrerCommons.logWarn("InstallReferrerClient", "RemoteException getting install referrer information");
                this.mClientState = 0;
                throw ex;
            }
        }
        throw new IllegalStateException("Service not connected. Please start a connection before using the service.");
    }
    
    @Override
    public boolean isReady() {
        return this.mClientState == 2 && this.mService != null && this.mServiceConnection != null;
    }
    
    @Override
    public void startConnection(final InstallReferrerStateListener installReferrerStateListener) {
        if (this.isReady()) {
            InstallReferrerCommons.logVerbose("InstallReferrerClient", "Service connection is valid. No need to re-initialize.");
            installReferrerStateListener.onInstallReferrerSetupFinished(0);
            return;
        }
        final int mClientState = this.mClientState;
        if (mClientState == 1) {
            InstallReferrerCommons.logWarn("InstallReferrerClient", "Client is already in the process of connecting to the service.");
            installReferrerStateListener.onInstallReferrerSetupFinished(3);
            return;
        }
        if (mClientState == 3) {
            InstallReferrerCommons.logWarn("InstallReferrerClient", "Client was already closed and can't be reused. Please create another instance.");
            installReferrerStateListener.onInstallReferrerSetupFinished(3);
            return;
        }
        InstallReferrerCommons.logVerbose("InstallReferrerClient", "Starting install referrer service setup.");
        this.mServiceConnection = (ServiceConnection)new InstallReferrerServiceConnection(installReferrerStateListener);
        final Intent intent = new Intent("com.google.android.finsky.BIND_GET_INSTALL_REFERRER_SERVICE");
        intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.externalreferrer.GetInstallReferrerService"));
        final List queryIntentServices = this.mApplicationContext.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices != null && !queryIntentServices.isEmpty()) {
            final ResolveInfo resolveInfo = queryIntentServices.get(0);
            if (resolveInfo.serviceInfo != null) {
                final String packageName = resolveInfo.serviceInfo.packageName;
                final String name = resolveInfo.serviceInfo.name;
                if (!"com.android.vending".equals(packageName) || name == null || !this.isPlayStoreCompatible()) {
                    InstallReferrerCommons.logWarn("InstallReferrerClient", "Play Store missing or incompatible. Version 8.3.73 or later required.");
                    this.mClientState = 0;
                    installReferrerStateListener.onInstallReferrerSetupFinished(2);
                    return;
                }
                if (this.mApplicationContext.bindService(new Intent(intent), this.mServiceConnection, 1)) {
                    InstallReferrerCommons.logVerbose("InstallReferrerClient", "Service was bonded successfully.");
                    return;
                }
                InstallReferrerCommons.logWarn("InstallReferrerClient", "Connection to service is blocked.");
                this.mClientState = 0;
                installReferrerStateListener.onInstallReferrerSetupFinished(1);
                return;
            }
        }
        this.mClientState = 0;
        InstallReferrerCommons.logVerbose("InstallReferrerClient", "Install Referrer service unavailable on device.");
        installReferrerStateListener.onInstallReferrerSetupFinished(2);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ClientState {
        public static final int CLOSED = 3;
        public static final int CONNECTED = 2;
        public static final int CONNECTING = 1;
        public static final int DISCONNECTED = 0;
    }
    
    private final class InstallReferrerServiceConnection implements ServiceConnection
    {
        private final InstallReferrerStateListener mListener;
        
        private InstallReferrerServiceConnection(final InstallReferrerStateListener mListener) {
            if (mListener != null) {
                this.mListener = mListener;
                return;
            }
            throw new RuntimeException("Please specify a listener to know when setup is done.");
        }
        
        public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
            InstallReferrerCommons.logVerbose("InstallReferrerClient", "Install Referrer service connected.");
            InstallReferrerClientImpl.this.mService = IGetInstallReferrerService.Stub.asInterface(binder);
            InstallReferrerClientImpl.this.mClientState = 2;
            this.mListener.onInstallReferrerSetupFinished(0);
        }
        
        public void onServiceDisconnected(final ComponentName componentName) {
            InstallReferrerCommons.logWarn("InstallReferrerClient", "Install Referrer service disconnected.");
            InstallReferrerClientImpl.this.mService = null;
            InstallReferrerClientImpl.this.mClientState = 0;
            this.mListener.onInstallReferrerServiceDisconnected();
        }
    }
}
