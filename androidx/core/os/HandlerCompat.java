package androidx.core.os;

import java.lang.reflect.*;
import android.util.*;
import androidx.annotation.*;
import android.os.*;

public final class HandlerCompat
{
    private static final String TAG = "HandlerCompat";
    
    private HandlerCompat() {
    }
    
    @NonNull
    public static Handler createAsync(@NonNull final Looper looper) {
        if (Build$VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            try {
                return Handler.class.getDeclaredConstructor(Looper.class, Handler$Callback.class, Boolean.TYPE).newInstance(looper, null, true);
            }
            catch (InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
                if (cause instanceof Error) {
                    throw (Error)cause;
                }
                throw new RuntimeException(cause);
            }
            catch (NoSuchMethodException ex2) {}
            catch (InstantiationException ex3) {}
            catch (IllegalAccessException ex4) {}
            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper);
    }
    
    @NonNull
    public static Handler createAsync(@NonNull final Looper looper, @NonNull final Handler$Callback handler$Callback) {
        if (Build$VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper, handler$Callback);
        }
        if (Build$VERSION.SDK_INT >= 16) {
            try {
                return Handler.class.getDeclaredConstructor(Looper.class, Handler$Callback.class, Boolean.TYPE).newInstance(looper, handler$Callback, true);
            }
            catch (InvocationTargetException ex) {
                final Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
                if (cause instanceof Error) {
                    throw (Error)cause;
                }
                throw new RuntimeException(cause);
            }
            catch (NoSuchMethodException ex2) {}
            catch (InstantiationException ex3) {}
            catch (IllegalAccessException ex4) {}
            Log.v("HandlerCompat", "Unable to invoke Handler(Looper, Callback, boolean) constructor");
        }
        return new Handler(looper, handler$Callback);
    }
    
    public static boolean postDelayed(@NonNull final Handler handler, @NonNull final Runnable runnable, @Nullable final Object obj, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return handler.postDelayed(runnable, obj, n);
        }
        final Message obtain = Message.obtain(handler, runnable);
        obtain.obj = obj;
        return handler.sendMessageDelayed(obtain, n);
    }
}
