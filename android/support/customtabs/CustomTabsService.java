package android.support.customtabs;

import android.app.*;
import android.support.v4.util.*;
import android.net.*;
import android.os.*;
import java.util.*;
import android.content.*;

public abstract class CustomTabsService extends Service
{
    public static final String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";
    public static final String KEY_URL = "android.support.customtabs.otherurls.URL";
    private ICustomTabsService.Stub mBinder;
    private final Map<IBinder, IBinder$DeathRecipient> mDeathRecipientMap;
    
    public CustomTabsService() {
        this.mDeathRecipientMap = new ArrayMap<IBinder, IBinder$DeathRecipient>();
        this.mBinder = new ICustomTabsService.Stub() {
            public Bundle extraCommand(final String s, final Bundle bundle) {
                return CustomTabsService.this.extraCommand(s, bundle);
            }
            
            public boolean mayLaunchUrl(final ICustomTabsCallback customTabsCallback, final Uri uri, final Bundle bundle, final List<Bundle> list) {
                return CustomTabsService.this.mayLaunchUrl(new CustomTabsSessionToken(customTabsCallback), uri, bundle, list);
            }
            
            public boolean newSession(final ICustomTabsCallback customTabsCallback) {
                final CustomTabsSessionToken customTabsSessionToken = new CustomTabsSessionToken(customTabsCallback);
                try {
                    final IBinder$DeathRecipient binder$DeathRecipient = (IBinder$DeathRecipient)new IBinder$DeathRecipient() {
                        public void binderDied() {
                            CustomTabsService.this.cleanUpSession(customTabsSessionToken);
                        }
                    };
                    synchronized (CustomTabsService.this.mDeathRecipientMap) {
                        customTabsCallback.asBinder().linkToDeath((IBinder$DeathRecipient)binder$DeathRecipient, 0);
                        CustomTabsService.this.mDeathRecipientMap.put(customTabsCallback.asBinder(), binder$DeathRecipient);
                        // monitorexit(CustomTabsService.access$000(this.this$0))
                        return CustomTabsService.this.newSession(customTabsSessionToken);
                    }
                }
                catch (RemoteException ex) {
                    return false;
                }
            }
            
            public boolean updateVisuals(final ICustomTabsCallback customTabsCallback, final Bundle bundle) {
                return CustomTabsService.this.updateVisuals(new CustomTabsSessionToken(customTabsCallback), bundle);
            }
            
            public boolean warmup(final long n) {
                return CustomTabsService.this.warmup(n);
            }
        };
    }
    
    protected boolean cleanUpSession(final CustomTabsSessionToken customTabsSessionToken) {
        try {
            synchronized (this.mDeathRecipientMap) {
                final IBinder callbackBinder = customTabsSessionToken.getCallbackBinder();
                callbackBinder.unlinkToDeath((IBinder$DeathRecipient)this.mDeathRecipientMap.get(callbackBinder), 0);
                this.mDeathRecipientMap.remove(callbackBinder);
                return true;
            }
        }
        catch (NoSuchElementException ex) {
            return false;
        }
    }
    
    protected abstract Bundle extraCommand(final String p0, final Bundle p1);
    
    protected abstract boolean mayLaunchUrl(final CustomTabsSessionToken p0, final Uri p1, final Bundle p2, final List<Bundle> p3);
    
    protected abstract boolean newSession(final CustomTabsSessionToken p0);
    
    public IBinder onBind(final Intent intent) {
        return (IBinder)this.mBinder;
    }
    
    protected abstract boolean updateVisuals(final CustomTabsSessionToken p0, final Bundle p1);
    
    protected abstract boolean warmup(final long p0);
}
