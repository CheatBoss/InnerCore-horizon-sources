package android.support.customtabs;

import android.content.*;
import android.net.*;
import java.util.*;
import android.graphics.*;
import android.support.annotation.*;
import android.os.*;

public final class CustomTabsSession
{
    private static final String TAG = "CustomTabsSession";
    private final ICustomTabsCallback mCallback;
    private final ComponentName mComponentName;
    private final ICustomTabsService mService;
    
    CustomTabsSession(final ICustomTabsService mService, final ICustomTabsCallback mCallback, final ComponentName mComponentName) {
        this.mService = mService;
        this.mCallback = mCallback;
        this.mComponentName = mComponentName;
    }
    
    IBinder getBinder() {
        return this.mCallback.asBinder();
    }
    
    ComponentName getComponentName() {
        return this.mComponentName;
    }
    
    public boolean mayLaunchUrl(final Uri uri, final Bundle bundle, final List<Bundle> list) {
        try {
            return this.mService.mayLaunchUrl(this.mCallback, uri, bundle, list);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
    
    public boolean setActionButton(@NonNull final Bitmap bitmap, @NonNull final String s) {
        return this.setToolbarItem(0, bitmap, s);
    }
    
    public boolean setToolbarItem(final int n, @NonNull final Bitmap bitmap, @NonNull final String s) {
        final Bundle bundle = new Bundle();
        bundle.putInt("android.support.customtabs.customaction.ID", n);
        bundle.putParcelable("android.support.customtabs.customaction.ICON", (Parcelable)bitmap);
        bundle.putString("android.support.customtabs.customaction.DESCRIPTION", s);
        final Bundle bundle2 = new Bundle();
        bundle2.putBundle("android.support.customtabs.extra.ACTION_BUTTON_BUNDLE", bundle);
        try {
            return this.mService.updateVisuals(this.mCallback, bundle2);
        }
        catch (RemoteException ex) {
            return false;
        }
    }
}
