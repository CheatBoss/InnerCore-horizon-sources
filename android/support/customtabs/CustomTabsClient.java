package android.support.customtabs;

import android.text.*;
import android.content.*;
import android.os.*;

public class CustomTabsClient
{
    private final ICustomTabsService mService;
    private final ComponentName mServiceComponentName;
    
    CustomTabsClient(final ICustomTabsService mService, final ComponentName mServiceComponentName) {
        this.mService = mService;
        this.mServiceComponentName = mServiceComponentName;
    }
    
    public static boolean bindCustomTabsService(final Context context, final String package1, final CustomTabsServiceConnection customTabsServiceConnection) {
        final Intent intent = new Intent("android.support.customtabs.action.CustomTabsService");
        if (!TextUtils.isEmpty((CharSequence)package1)) {
            intent.setPackage(package1);
        }
        return context.bindService(intent, (ServiceConnection)customTabsServiceConnection, 33);
    }
    
    public Bundle extraCommand(final String s, final Bundle bundle) {
        try {
            return this.mService.extraCommand(s, bundle);
        }
        catch (RemoteException ex) {
            return null;
        }
    }
    
    public CustomTabsSession newSession(final CustomTabsCallback customTabsCallback) {
        final ICustomTabsCallback.Stub stub = new ICustomTabsCallback.Stub() {
            public void extraCallback(final String s, final Bundle bundle) throws RemoteException {
                if (customTabsCallback != null) {
                    customTabsCallback.extraCallback(s, bundle);
                }
            }
            
            public void onNavigationEvent(final int n, final Bundle bundle) {
                if (customTabsCallback != null) {
                    customTabsCallback.onNavigationEvent(n, bundle);
                }
            }
        };
        try {
            if (!this.mService.newSession(stub)) {
                return null;
            }
            return new CustomTabsSession(this.mService, stub, this.mServiceComponentName);
        }
        catch (RemoteException ex) {
            return null;
        }
    }
    
    public boolean warmup(final long n) {
        try {
            return this.mService.warmup(n);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
}
