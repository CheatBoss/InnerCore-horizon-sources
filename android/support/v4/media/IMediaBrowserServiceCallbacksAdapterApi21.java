package android.support.v4.media;

import android.media.session.*;
import java.lang.reflect.*;
import android.os.*;

class IMediaBrowserServiceCallbacksAdapterApi21
{
    private Method mAsBinderMethod;
    Object mCallbackObject;
    private Method mOnConnectFailedMethod;
    private Method mOnConnectMethod;
    private Method mOnLoadChildrenMethod;
    
    IMediaBrowserServiceCallbacksAdapterApi21(final Object mCallbackObject) {
        this.mCallbackObject = mCallbackObject;
        try {
            final Class<?> forName = Class.forName("android.service.media.IMediaBrowserServiceCallbacks");
            final Class<?> forName2 = Class.forName("android.content.pm.ParceledListSlice");
            this.mAsBinderMethod = forName.getMethod("asBinder", (Class<?>[])new Class[0]);
            this.mOnConnectMethod = forName.getMethod("onConnect", String.class, MediaSession$Token.class, Bundle.class);
            this.mOnConnectFailedMethod = forName.getMethod("onConnectFailed", (Class<?>[])new Class[0]);
            this.mOnLoadChildrenMethod = forName.getMethod("onLoadChildren", String.class, forName2);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    IBinder asBinder() {
        try {
            return (IBinder)this.mAsBinderMethod.invoke(this.mCallbackObject, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
            return null;
        }
    }
    
    void onConnect(final String s, final Object o, final Bundle bundle) throws RemoteException {
        try {
            this.mOnConnectMethod.invoke(this.mCallbackObject, s, o, bundle);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    void onConnectFailed() throws RemoteException {
        try {
            this.mOnConnectFailedMethod.invoke(this.mCallbackObject, new Object[0]);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    void onLoadChildren(final String s, final Object o) throws RemoteException {
        try {
            this.mOnLoadChildrenMethod.invoke(this.mCallbackObject, s, o);
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    static class Stub
    {
        static Method sAsInterfaceMethod;
        
        static {
            try {
                Stub.sAsInterfaceMethod = Class.forName("android.service.media.IMediaBrowserServiceCallbacks$Stub").getMethod("asInterface", IBinder.class);
            }
            catch (ClassNotFoundException | NoSuchMethodException ex) {
                final Throwable t;
                t.printStackTrace();
            }
        }
        
        static Object asInterface(final IBinder binder) {
            try {
                return Stub.sAsInterfaceMethod.invoke(null, binder);
            }
            catch (IllegalAccessException | InvocationTargetException ex) {
                final Throwable t;
                t.printStackTrace();
                return null;
            }
        }
    }
}
