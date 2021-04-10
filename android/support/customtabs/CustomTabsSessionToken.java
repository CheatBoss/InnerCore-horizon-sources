package android.support.customtabs;

import android.util.*;
import android.content.*;
import android.support.v4.app.*;
import android.os.*;

public class CustomTabsSessionToken
{
    private static final String TAG = "CustomTabsSessionToken";
    private final CustomTabsCallback mCallback;
    private final ICustomTabsCallback mCallbackBinder;
    
    CustomTabsSessionToken(final ICustomTabsCallback mCallbackBinder) {
        this.mCallbackBinder = mCallbackBinder;
        this.mCallback = new CustomTabsCallback() {
            @Override
            public void onNavigationEvent(final int n, final Bundle bundle) {
                try {
                    CustomTabsSessionToken.this.mCallbackBinder.onNavigationEvent(n, bundle);
                }
                catch (RemoteException ex) {
                    Log.e("CustomTabsSessionToken", "RemoteException during ICustomTabsCallback transaction");
                }
            }
        };
    }
    
    public static CustomTabsSessionToken getSessionTokenFromIntent(final Intent intent) {
        final IBinder binder = BundleCompat.getBinder(intent.getExtras(), "android.support.customtabs.extra.SESSION");
        if (binder == null) {
            return null;
        }
        return new CustomTabsSessionToken(ICustomTabsCallback.Stub.asInterface(binder));
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof CustomTabsSessionToken && ((CustomTabsSessionToken)o).getCallbackBinder().equals(this.mCallbackBinder.asBinder());
    }
    
    public CustomTabsCallback getCallback() {
        return this.mCallback;
    }
    
    IBinder getCallbackBinder() {
        return this.mCallbackBinder.asBinder();
    }
    
    @Override
    public int hashCode() {
        return this.getCallbackBinder().hashCode();
    }
}
