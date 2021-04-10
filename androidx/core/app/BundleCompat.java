package androidx.core.app;

import androidx.annotation.*;
import android.os.*;
import android.util.*;
import java.lang.reflect.*;

public final class BundleCompat
{
    private BundleCompat() {
    }
    
    @Nullable
    public static IBinder getBinder(@NonNull final Bundle bundle, @Nullable final String s) {
        if (Build$VERSION.SDK_INT >= 18) {
            return bundle.getBinder(s);
        }
        return BundleCompatBaseImpl.getBinder(bundle, s);
    }
    
    public static void putBinder(@NonNull final Bundle bundle, @Nullable final String s, @Nullable final IBinder binder) {
        if (Build$VERSION.SDK_INT >= 18) {
            bundle.putBinder(s, binder);
            return;
        }
        BundleCompatBaseImpl.putBinder(bundle, s, binder);
    }
    
    static class BundleCompatBaseImpl
    {
        private static final String TAG = "BundleCompatBaseImpl";
        private static Method sGetIBinderMethod;
        private static boolean sGetIBinderMethodFetched;
        private static Method sPutIBinderMethod;
        private static boolean sPutIBinderMethodFetched;
        
        private BundleCompatBaseImpl() {
        }
        
        public static IBinder getBinder(final Bundle bundle, final String s) {
            if (!BundleCompatBaseImpl.sGetIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sGetIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sGetIBinderMethod != null) {
                try {
                    return (IBinder)BundleCompatBaseImpl.sGetIBinderMethod.invoke(bundle, s);
                }
                catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                    final Throwable t;
                    Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", t);
                    BundleCompatBaseImpl.sGetIBinderMethod = null;
                }
            }
            return null;
        }
        
        public static void putBinder(final Bundle bundle, final String s, final IBinder binder) {
            if (!BundleCompatBaseImpl.sPutIBinderMethodFetched) {
                try {
                    (BundleCompatBaseImpl.sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class)).setAccessible(true);
                }
                catch (NoSuchMethodException ex) {
                    Log.i("BundleCompatBaseImpl", "Failed to retrieve putIBinder method", (Throwable)ex);
                }
                BundleCompatBaseImpl.sPutIBinderMethodFetched = true;
            }
            if (BundleCompatBaseImpl.sPutIBinderMethod != null) {
                try {
                    BundleCompatBaseImpl.sPutIBinderMethod.invoke(bundle, s, binder);
                }
                catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException ex2) {
                    final Throwable t;
                    Log.i("BundleCompatBaseImpl", "Failed to invoke putIBinder via reflection", t);
                    BundleCompatBaseImpl.sPutIBinderMethod = null;
                }
            }
        }
    }
}
