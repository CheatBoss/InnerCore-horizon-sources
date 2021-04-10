package android.support.v4.media;

import android.service.media.*;
import android.media.browse.*;
import android.os.*;
import android.util.*;

class MediaBrowserServiceCompatApi23 extends MediaBrowserServiceCompatApi21
{
    private static final String TAG = "MediaBrowserServiceCompatApi21";
    
    public static Object createService() {
        return new MediaBrowserServiceAdaptorApi23();
    }
    
    public static void onCreate(final Object o, final ServiceImplApi23 serviceImplApi23) {
        ((MediaBrowserServiceAdaptorApi23)o).onCreate(serviceImplApi23);
    }
    
    public interface ItemCallback
    {
        void onItemLoaded(final int p0, final Bundle p1, final Parcel p2);
    }
    
    static class MediaBrowserServiceAdaptorApi23 extends MediaBrowserServiceAdaptorApi21
    {
        public void onCreate(final ServiceImplApi23 serviceImplApi23) {
            this.mBinder = (ServiceBinderProxyApi21)new ServiceBinderProxyApi23(serviceImplApi23);
        }
        
        private static class ServiceBinderProxyApi23 extends ServiceBinderProxyApi21
        {
            ServiceImplApi23 mServiceImpl;
            
            ServiceBinderProxyApi23(final ServiceImplApi23 mServiceImpl) {
                super(mServiceImpl);
                this.mServiceImpl = mServiceImpl;
            }
            
            @Override
            public void getMediaItem(final String s, final ResultReceiver resultReceiver) {
                try {
                    this.mServiceImpl.getMediaItem(s, new ItemCallback() {
                        final /* synthetic */ String val$KEY_MEDIA_ITEM = (String)MediaBrowserService.class.getDeclaredField("KEY_MEDIA_ITEM").get(null);
                        
                        @Override
                        public void onItemLoaded(final int n, final Bundle bundle, final Parcel parcel) {
                            if (parcel != null) {
                                parcel.setDataPosition(0);
                                bundle.putParcelable(this.val$KEY_MEDIA_ITEM, (Parcelable)MediaBrowser$MediaItem.CREATOR.createFromParcel(parcel));
                                parcel.recycle();
                            }
                            resultReceiver.send(n, bundle);
                        }
                    });
                }
                catch (IllegalAccessException | NoSuchFieldException ex) {
                    final Throwable t;
                    Log.i("MediaBrowserServiceCompatApi21", "Failed to get KEY_MEDIA_ITEM via reflection", t);
                }
            }
        }
    }
    
    public interface ServiceImplApi23 extends ServiceImplApi21
    {
        void getMediaItem(final String p0, final ItemCallback p1);
    }
}
