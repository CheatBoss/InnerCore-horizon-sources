package android.support.v4.media;

import android.content.*;
import android.media.*;
import android.media.browse.*;
import android.os.*;
import java.util.*;

class MediaBrowserServiceCompatApi21
{
    public static Object createService() {
        return new MediaBrowserServiceAdaptorApi21();
    }
    
    public static IBinder onBind(final Object o, final Intent intent) {
        return ((MediaBrowserServiceAdaptorApi21)o).onBind(intent);
    }
    
    public static void onCreate(final Object o, final ServiceImplApi21 serviceImplApi21) {
        ((MediaBrowserServiceAdaptorApi21)o).onCreate(serviceImplApi21);
    }
    
    static class MediaBrowserServiceAdaptorApi21
    {
        ServiceBinderProxyApi21 mBinder;
        
        public IBinder onBind(final Intent intent) {
            if ("android.media.browse.MediaBrowserService".equals(intent.getAction())) {
                return (IBinder)this.mBinder;
            }
            return null;
        }
        
        public void onCreate(final ServiceImplApi21 serviceImplApi21) {
            this.mBinder = new ServiceBinderProxyApi21(serviceImplApi21);
        }
        
        static class ServiceBinderProxyApi21 extends Stub
        {
            final ServiceImplApi21 mServiceImpl;
            
            ServiceBinderProxyApi21(final ServiceImplApi21 mServiceImpl) {
                this.mServiceImpl = mServiceImpl;
            }
            
            @Override
            public void addSubscription(final String s, final Object o) {
                this.mServiceImpl.addSubscription(s, new ServiceCallbacksApi21(o));
            }
            
            @Override
            public void connect(final String s, final Bundle bundle, final Object o) {
                this.mServiceImpl.connect(s, bundle, new ServiceCallbacksApi21(o));
            }
            
            @Override
            public void disconnect(final Object o) {
                this.mServiceImpl.disconnect(new ServiceCallbacksApi21(o));
            }
            
            @Override
            public void getMediaItem(final String s, final ResultReceiver resultReceiver) {
            }
            
            @Override
            public void removeSubscription(final String s, final Object o) {
                this.mServiceImpl.removeSubscription(s, new ServiceCallbacksApi21(o));
            }
        }
    }
    
    public interface ServiceCallbacks
    {
        IBinder asBinder();
        
        void onConnect(final String p0, final Object p1, final Bundle p2) throws RemoteException;
        
        void onConnectFailed() throws RemoteException;
        
        void onLoadChildren(final String p0, final List<Parcel> p1) throws RemoteException;
    }
    
    public static class ServiceCallbacksApi21 implements ServiceCallbacks
    {
        private static Object sNullParceledListSliceObj;
        private final IMediaBrowserServiceCallbacksAdapterApi21 mCallbacks;
        
        static {
            final MediaBrowser$MediaItem mediaBrowser$MediaItem = new MediaBrowser$MediaItem(new MediaDescription$Builder().setMediaId("android.support.v4.media.MediaBrowserCompat.NULL_MEDIA_ITEM").build(), 0);
            final ArrayList<MediaBrowser$MediaItem> list = new ArrayList<MediaBrowser$MediaItem>();
            list.add(mediaBrowser$MediaItem);
            ServiceCallbacksApi21.sNullParceledListSliceObj = ParceledListSliceAdapterApi21.newInstance(list);
        }
        
        ServiceCallbacksApi21(final Object o) {
            this.mCallbacks = new IMediaBrowserServiceCallbacksAdapterApi21(o);
        }
        
        @Override
        public IBinder asBinder() {
            return this.mCallbacks.asBinder();
        }
        
        @Override
        public void onConnect(final String s, final Object o, final Bundle bundle) throws RemoteException {
            this.mCallbacks.onConnect(s, o, bundle);
        }
        
        @Override
        public void onConnectFailed() throws RemoteException {
            this.mCallbacks.onConnectFailed();
        }
        
        @Override
        public void onLoadChildren(final String s, final List<Parcel> list) throws RemoteException {
            final Object o = null;
            ArrayList<MediaBrowser$MediaItem> list3;
            if (list != null) {
                final ArrayList<MediaBrowser$MediaItem> list2 = new ArrayList<MediaBrowser$MediaItem>();
                final Iterator<Parcel> iterator = list.iterator();
                while (true) {
                    list3 = list2;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    final Parcel parcel = iterator.next();
                    parcel.setDataPosition(0);
                    list2.add((MediaBrowser$MediaItem)MediaBrowser$MediaItem.CREATOR.createFromParcel(parcel));
                    parcel.recycle();
                }
            }
            else {
                list3 = null;
            }
            Object o2;
            if (Build$VERSION.SDK_INT > 23) {
                if (list3 == null) {
                    o2 = o;
                }
                else {
                    o2 = ParceledListSliceAdapterApi21.newInstance(list3);
                }
            }
            else if (list3 == null) {
                o2 = ServiceCallbacksApi21.sNullParceledListSliceObj;
            }
            else {
                o2 = ParceledListSliceAdapterApi21.newInstance(list3);
            }
            this.mCallbacks.onLoadChildren(s, o2);
        }
    }
    
    public interface ServiceImplApi21
    {
        void addSubscription(final String p0, final ServiceCallbacks p1);
        
        void connect(final String p0, final Bundle p1, final ServiceCallbacks p2);
        
        void disconnect(final ServiceCallbacks p0);
        
        void removeSubscription(final String p0, final ServiceCallbacks p1);
    }
}
