package android.support.customtabs;

import android.content.*;
import android.os.*;

public abstract class CustomTabsServiceConnection implements ServiceConnection
{
    public abstract void onCustomTabsServiceConnected(final ComponentName p0, final CustomTabsClient p1);
    
    public final void onServiceConnected(final ComponentName componentName, final IBinder binder) {
        this.onCustomTabsServiceConnected(componentName, new CustomTabsClient(ICustomTabsService.Stub.asInterface(binder), componentName) {});
    }
}
