package android.support.v4.media;

import android.support.annotation.*;
import android.support.v4.media.session.*;
import java.lang.ref.*;
import android.util.*;
import android.support.v4.os.*;
import android.support.v4.util.*;
import android.text.*;
import android.support.v4.app.*;
import android.content.*;
import android.os.*;
import java.lang.annotation.*;
import java.util.*;

public final class MediaBrowserCompat
{
    public static final String EXTRA_PAGE = "android.media.browse.extra.PAGE";
    public static final String EXTRA_PAGE_SIZE = "android.media.browse.extra.PAGE_SIZE";
    private static final String TAG = "MediaBrowserCompat";
    private final MediaBrowserImpl mImpl;
    
    public MediaBrowserCompat(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
        MediaBrowserImpl mImpl;
        if (Build$VERSION.SDK_INT >= 23) {
            mImpl = new MediaBrowserImplApi23(context, componentName, connectionCallback, bundle);
        }
        else if (Build$VERSION.SDK_INT >= 21) {
            mImpl = new MediaBrowserImplApi21(context, componentName, connectionCallback, bundle);
        }
        else {
            mImpl = new MediaBrowserServiceImplBase(context, componentName, connectionCallback, bundle);
        }
        this.mImpl = mImpl;
    }
    
    public void connect() {
        this.mImpl.connect();
    }
    
    public void disconnect() {
        this.mImpl.disconnect();
    }
    
    @Nullable
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }
    
    public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
        this.mImpl.getItem(s, itemCallback);
    }
    
    @NonNull
    public String getRoot() {
        return this.mImpl.getRoot();
    }
    
    @NonNull
    public ComponentName getServiceComponent() {
        return this.mImpl.getServiceComponent();
    }
    
    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }
    
    public boolean isConnected() {
        return this.mImpl.isConnected();
    }
    
    public void subscribe(@NonNull final String s, @NonNull final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
        if (bundle == null) {
            throw new IllegalArgumentException("options are null");
        }
        this.mImpl.subscribe(s, bundle, subscriptionCallback);
    }
    
    public void subscribe(@NonNull final String s, @NonNull final SubscriptionCallback subscriptionCallback) {
        this.mImpl.subscribe(s, null, subscriptionCallback);
    }
    
    public void unsubscribe(@NonNull final String s) {
        this.mImpl.unsubscribe(s, null);
    }
    
    public void unsubscribe(@NonNull final String s, @NonNull final Bundle bundle) {
        if (bundle == null) {
            throw new IllegalArgumentException("options are null");
        }
        this.mImpl.unsubscribe(s, bundle);
    }
    
    private static class CallbackHandler extends Handler
    {
        private final MediaBrowserServiceCallbackImpl mCallbackImpl;
        private WeakReference<Messenger> mCallbacksMessengerRef;
        
        CallbackHandler(final MediaBrowserServiceCallbackImpl mCallbackImpl) {
            this.mCallbackImpl = mCallbackImpl;
        }
        
        public void handleMessage(final Message message) {
            if (this.mCallbacksMessengerRef == null) {
                return;
            }
            final Bundle data = message.getData();
            data.setClassLoader(MediaSessionCompat.class.getClassLoader());
            switch (message.what) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unhandled message: ");
                    sb.append(message);
                    sb.append("\n  Client version: ");
                    sb.append(1);
                    sb.append("\n  Service version: ");
                    sb.append(message.arg1);
                    Log.w("MediaBrowserCompat", sb.toString());
                }
                case 3: {
                    this.mCallbackImpl.onLoadChildren(this.mCallbacksMessengerRef.get(), data.getString("data_media_item_id"), data.getParcelableArrayList("data_media_item_list"), data.getBundle("data_options"));
                }
                case 2: {
                    this.mCallbackImpl.onConnectionFailed(this.mCallbacksMessengerRef.get());
                }
                case 1: {
                    this.mCallbackImpl.onServiceConnected(this.mCallbacksMessengerRef.get(), data.getString("data_media_item_id"), (MediaSessionCompat.Token)data.getParcelable("data_media_session_token"), data.getBundle("data_root_hints"));
                }
            }
        }
        
        void setCallbacksMessenger(final Messenger messenger) {
            this.mCallbacksMessengerRef = new WeakReference<Messenger>(messenger);
        }
    }
    
    public static class ConnectionCallback
    {
        private ConnectionCallbackInternal mConnectionCallbackInternal;
        final Object mConnectionCallbackObj;
        
        public ConnectionCallback() {
            if (Build$VERSION.SDK_INT >= 21) {
                this.mConnectionCallbackObj = MediaBrowserCompatApi21.createConnectionCallback((MediaBrowserCompatApi21.ConnectionCallback)new StubApi21());
                return;
            }
            this.mConnectionCallbackObj = null;
        }
        
        public void onConnected() {
        }
        
        public void onConnectionFailed() {
        }
        
        public void onConnectionSuspended() {
        }
        
        void setInternalConnectionCallback(final ConnectionCallbackInternal mConnectionCallbackInternal) {
            this.mConnectionCallbackInternal = mConnectionCallbackInternal;
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.ConnectionCallback
        {
            @Override
            public void onConnected() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnected();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnected();
            }
            
            @Override
            public void onConnectionFailed() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionFailed();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionFailed();
            }
            
            @Override
            public void onConnectionSuspended() {
                if (MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal != null) {
                    MediaBrowserCompat.ConnectionCallback.this.mConnectionCallbackInternal.onConnectionSuspended();
                }
                MediaBrowserCompat.ConnectionCallback.this.onConnectionSuspended();
            }
        }
    }
    
    interface ConnectionCallbackInternal
    {
        void onConnected();
        
        void onConnectionFailed();
        
        void onConnectionSuspended();
    }
    
    public abstract static class ItemCallback
    {
        final Object mItemCallbackObj;
        
        public ItemCallback() {
            if (Build$VERSION.SDK_INT >= 23) {
                this.mItemCallbackObj = MediaBrowserCompatApi23.createItemCallback((MediaBrowserCompatApi23.ItemCallback)new StubApi23());
                return;
            }
            this.mItemCallbackObj = null;
        }
        
        public void onError(@NonNull final String s) {
        }
        
        public void onItemLoaded(final MediaItem mediaItem) {
        }
        
        private class StubApi23 implements MediaBrowserCompatApi23.ItemCallback
        {
            @Override
            public void onError(@NonNull final String s) {
                MediaBrowserCompat.ItemCallback.this.onError(s);
            }
            
            @Override
            public void onItemLoaded(final Parcel parcel) {
                parcel.setDataPosition(0);
                final MediaItem mediaItem = (MediaItem)MediaItem.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                MediaBrowserCompat.ItemCallback.this.onItemLoaded(mediaItem);
            }
        }
    }
    
    private static class ItemReceiver extends ResultReceiver
    {
        private final ItemCallback mCallback;
        private final String mMediaId;
        
        ItemReceiver(final String mMediaId, final ItemCallback mCallback, final Handler handler) {
            super(handler);
            this.mMediaId = mMediaId;
            this.mCallback = mCallback;
        }
        
        @Override
        protected void onReceiveResult(final int n, final Bundle bundle) {
            bundle.setClassLoader(MediaBrowserCompat.class.getClassLoader());
            if (n != 0 || bundle == null || !bundle.containsKey("media_item")) {
                this.mCallback.onError(this.mMediaId);
                return;
            }
            final Parcelable parcelable = bundle.getParcelable("media_item");
            if (parcelable instanceof MediaItem) {
                this.mCallback.onItemLoaded((MediaItem)parcelable);
                return;
            }
            this.mCallback.onError(this.mMediaId);
        }
    }
    
    interface MediaBrowserImpl
    {
        void connect();
        
        void disconnect();
        
        @Nullable
        Bundle getExtras();
        
        void getItem(@NonNull final String p0, @NonNull final ItemCallback p1);
        
        @NonNull
        String getRoot();
        
        ComponentName getServiceComponent();
        
        @NonNull
        MediaSessionCompat.Token getSessionToken();
        
        boolean isConnected();
        
        void subscribe(@NonNull final String p0, final Bundle p1, @NonNull final SubscriptionCallback p2);
        
        void unsubscribe(@NonNull final String p0, final Bundle p1);
    }
    
    static class MediaBrowserImplApi21 implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl, ConnectionCallbackInternal
    {
        private static final boolean DBG = false;
        protected Object mBrowserObj;
        private Messenger mCallbacksMessenger;
        private final CallbackHandler mHandler;
        private ServiceBinderWrapper mServiceBinderWrapper;
        private final ComponentName mServiceComponent;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        public MediaBrowserImplApi21(final Context context, final ComponentName mServiceComponent, final ConnectionCallback connectionCallback, final Bundle bundle) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            this.mServiceComponent = mServiceComponent;
            connectionCallback.setInternalConnectionCallback((ConnectionCallbackInternal)this);
            this.mBrowserObj = MediaBrowserCompatApi21.createBrowser(context, mServiceComponent, connectionCallback.mConnectionCallbackObj, bundle);
        }
        
        @Override
        public void connect() {
            MediaBrowserCompatApi21.connect(this.mBrowserObj);
        }
        
        @Override
        public void disconnect() {
            MediaBrowserCompatApi21.disconnect(this.mBrowserObj);
        }
        
        @Nullable
        @Override
        public Bundle getExtras() {
            return MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("mediaId is empty.");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null.");
            }
            if (!MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
                return;
            }
            if (this.mServiceBinderWrapper == null) {
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onItemLoaded(null);
                    }
                });
                return;
            }
            final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
            try {
                this.mServiceBinderWrapper.getMediaItem(s, itemReceiver);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Remote error getting media item: ");
                sb.append(s);
                Log.i("MediaBrowserCompat", sb.toString());
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                });
            }
        }
        
        @NonNull
        @Override
        public String getRoot() {
            return MediaBrowserCompatApi21.getRoot(this.mBrowserObj);
        }
        
        @Override
        public ComponentName getServiceComponent() {
            return MediaBrowserCompatApi21.getServiceComponent(this.mBrowserObj);
        }
        
        @NonNull
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            return MediaSessionCompat.Token.fromToken(MediaBrowserCompatApi21.getSessionToken(this.mBrowserObj));
        }
        
        @Override
        public boolean isConnected() {
            return MediaBrowserCompatApi21.isConnected(this.mBrowserObj);
        }
        
        @Override
        public void onConnected() {
            final Bundle extras = MediaBrowserCompatApi21.getExtras(this.mBrowserObj);
            if (extras == null) {
                return;
            }
            final IBinder binder = BundleCompat.getBinder(extras, "extra_messenger");
            if (binder != null) {
                this.mServiceBinderWrapper = new ServiceBinderWrapper(binder);
                this.mCallbacksMessenger = new Messenger((Handler)this.mHandler);
                this.mHandler.setCallbacksMessenger(this.mCallbacksMessenger);
                try {
                    this.mServiceBinderWrapper.registerCallbackMessenger(this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    Log.i("MediaBrowserCompat", "Remote error registering client messenger.");
                }
                this.onServiceConnected(this.mCallbacksMessenger, null, null, null);
            }
        }
        
        @Override
        public void onConnectionFailed() {
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
        }
        
        @Override
        public void onConnectionSuspended() {
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, @NonNull final Bundle bundle) {
            if (this.mCallbacksMessenger != messenger) {
                return;
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null) {
                subscription.getCallback(bundle).onChildrenLoaded(s, list, bundle);
            }
        }
        
        @Override
        public void onServiceConnected(Messenger iterator, String s, MediaSessionCompat.Token optionsList, Bundle callbacks) {
            iterator = (Messenger)this.mSubscriptions.entrySet().iterator();
            while (((Iterator)iterator).hasNext()) {
                final Map.Entry<String, V> entry = ((Iterator<Map.Entry<String, V>>)iterator).next();
                s = entry.getKey();
                final Subscription subscription = (Subscription)entry.getValue();
                optionsList = (MediaSessionCompat.Token)subscription.getOptionsList();
                callbacks = (Bundle)subscription.getCallbacks();
                for (int i = 0; i < ((List)optionsList).size(); ++i) {
                    if (((List<Object>)optionsList).get(i) == null) {
                        MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, ((SubscriptionCallbackApi21)((List<Object>)callbacks).get(i)).mSubscriptionCallbackObj);
                    }
                    else {
                        try {
                            this.mServiceBinderWrapper.addSubscription(s, (Bundle)((List<Object>)optionsList).get(i), this.mCallbacksMessenger);
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("addSubscription failed with RemoteException parentId=");
                            sb.append(s);
                            Log.d("MediaBrowserCompat", sb.toString());
                        }
                    }
                }
            }
        }
        
        @Override
        public void subscribe(@NonNull final String s, final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
            final SubscriptionCallbackApi21 subscriptionCallbackApi21 = new SubscriptionCallbackApi21(subscriptionCallback, bundle);
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            subscription.setCallbackForOptions(subscriptionCallbackApi21, bundle);
            if (MediaBrowserCompatApi21.isConnected(this.mBrowserObj)) {
                if (bundle != null) {
                    if (this.mServiceBinderWrapper != null) {
                        try {
                            this.mServiceBinderWrapper.addSubscription(s, bundle, this.mCallbacksMessenger);
                            return;
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Remote error subscribing media item: ");
                            sb.append(s);
                            Log.i("MediaBrowserCompat", sb.toString());
                            return;
                        }
                    }
                }
                MediaBrowserCompatApi21.subscribe(this.mBrowserObj, s, subscriptionCallbackApi21.mSubscriptionCallbackObj);
            }
        }
        
        @Override
        public void unsubscribe(@NonNull final String s, final Bundle bundle) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("parentId is empty.");
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null && subscription.remove(bundle)) {
                if (bundle != null && this.mServiceBinderWrapper != null) {
                    if (this.mServiceBinderWrapper == null) {
                        try {
                            this.mServiceBinderWrapper.removeSubscription(s, bundle, this.mCallbacksMessenger);
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("removeSubscription failed with RemoteException parentId=");
                            sb.append(s);
                            Log.d("MediaBrowserCompat", sb.toString());
                        }
                    }
                }
                else if (this.mServiceBinderWrapper != null || subscription.isEmpty()) {
                    MediaBrowserCompatApi21.unsubscribe(this.mBrowserObj, s);
                }
            }
            if (subscription != null && subscription.isEmpty()) {
                this.mSubscriptions.remove(s);
            }
        }
    }
    
    static class MediaBrowserImplApi23 extends MediaBrowserImplApi21
    {
        public MediaBrowserImplApi23(final Context context, final ComponentName componentName, final ConnectionCallback connectionCallback, final Bundle bundle) {
            super(context, componentName, connectionCallback, bundle);
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            MediaBrowserCompatApi23.getItem(this.mBrowserObj, s, itemCallback.mItemCallbackObj);
        }
    }
    
    interface MediaBrowserServiceCallbackImpl
    {
        void onConnectionFailed(final Messenger p0);
        
        void onLoadChildren(final Messenger p0, final String p1, final List p2, final Bundle p3);
        
        void onServiceConnected(final Messenger p0, final String p1, final MediaSessionCompat.Token p2, final Bundle p3);
    }
    
    static class MediaBrowserServiceImplBase implements MediaBrowserImpl, MediaBrowserServiceCallbackImpl
    {
        private static final int CONNECT_STATE_CONNECTED = 2;
        private static final int CONNECT_STATE_CONNECTING = 1;
        private static final int CONNECT_STATE_DISCONNECTED = 0;
        private static final int CONNECT_STATE_SUSPENDED = 3;
        private static final boolean DBG = false;
        private final ConnectionCallback mCallback;
        private Messenger mCallbacksMessenger;
        private final Context mContext;
        private Bundle mExtras;
        private final CallbackHandler mHandler;
        private MediaSessionCompat.Token mMediaSessionToken;
        private final Bundle mRootHints;
        private String mRootId;
        private ServiceBinderWrapper mServiceBinderWrapper;
        private final ComponentName mServiceComponent;
        private MediaServiceConnection mServiceConnection;
        private int mState;
        private final ArrayMap<String, Subscription> mSubscriptions;
        
        public MediaBrowserServiceImplBase(final Context mContext, final ComponentName mServiceComponent, final ConnectionCallback mCallback, final Bundle mRootHints) {
            this.mHandler = new CallbackHandler(this);
            this.mSubscriptions = new ArrayMap<String, Subscription>();
            this.mState = 0;
            if (mContext == null) {
                throw new IllegalArgumentException("context must not be null");
            }
            if (mServiceComponent == null) {
                throw new IllegalArgumentException("service component must not be null");
            }
            if (mCallback == null) {
                throw new IllegalArgumentException("connection callback must not be null");
            }
            this.mContext = mContext;
            this.mServiceComponent = mServiceComponent;
            this.mCallback = mCallback;
            this.mRootHints = mRootHints;
        }
        
        private void forceCloseConnection() {
            if (this.mServiceConnection != null) {
                this.mContext.unbindService((ServiceConnection)this.mServiceConnection);
            }
            this.mState = 0;
            this.mServiceConnection = null;
            this.mServiceBinderWrapper = null;
            this.mCallbacksMessenger = null;
            this.mRootId = null;
            this.mMediaSessionToken = null;
        }
        
        private static String getStateLabel(final int n) {
            switch (n) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("UNKNOWN/");
                    sb.append(n);
                    return sb.toString();
                }
                case 3: {
                    return "CONNECT_STATE_SUSPENDED";
                }
                case 2: {
                    return "CONNECT_STATE_CONNECTED";
                }
                case 1: {
                    return "CONNECT_STATE_CONNECTING";
                }
                case 0: {
                    return "CONNECT_STATE_DISCONNECTED";
                }
            }
        }
        
        private boolean isCurrent(final Messenger messenger, final String s) {
            if (this.mCallbacksMessenger != messenger) {
                if (this.mState != 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(" for ");
                    sb.append(this.mServiceComponent);
                    sb.append(" with mCallbacksMessenger=");
                    sb.append(this.mCallbacksMessenger);
                    sb.append(" this=");
                    sb.append(this);
                    Log.i("MediaBrowserCompat", sb.toString());
                }
                return false;
            }
            return true;
        }
        
        @Override
        public void connect() {
            if (this.mState != 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append("connect() called while not disconnected (state=");
                sb.append(getStateLabel(this.mState));
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            if (this.mServiceBinderWrapper != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("mServiceBinderWrapper should be null. Instead it is ");
                sb2.append(this.mServiceBinderWrapper);
                throw new RuntimeException(sb2.toString());
            }
            if (this.mCallbacksMessenger != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("mCallbacksMessenger should be null. Instead it is ");
                sb3.append(this.mCallbacksMessenger);
                throw new RuntimeException(sb3.toString());
            }
            this.mState = 1;
            final Intent intent = new Intent("android.media.browse.MediaBrowserService");
            intent.setComponent(this.mServiceComponent);
            final MediaServiceConnection mServiceConnection = new MediaServiceConnection();
            this.mServiceConnection = mServiceConnection;
            boolean bindService;
            try {
                bindService = this.mContext.bindService(intent, (ServiceConnection)this.mServiceConnection, 1);
            }
            catch (Exception ex) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Failed binding to service ");
                sb4.append(this.mServiceComponent);
                Log.e("MediaBrowserCompat", sb4.toString());
                bindService = false;
            }
            if (!bindService) {
                this.mHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (mServiceConnection == MediaBrowserServiceImplBase.this.mServiceConnection) {
                            MediaBrowserServiceImplBase.this.forceCloseConnection();
                            MediaBrowserServiceImplBase.this.mCallback.onConnectionFailed();
                        }
                    }
                });
            }
        }
        
        @Override
        public void disconnect() {
            if (this.mCallbacksMessenger != null) {
                try {
                    this.mServiceBinderWrapper.disconnect(this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("RemoteException during connect for ");
                    sb.append(this.mServiceComponent);
                    Log.w("MediaBrowserCompat", sb.toString());
                }
            }
            this.forceCloseConnection();
        }
        
        void dump() {
            Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
            final StringBuilder sb = new StringBuilder();
            sb.append("  mServiceComponent=");
            sb.append(this.mServiceComponent);
            Log.d("MediaBrowserCompat", sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  mCallback=");
            sb2.append(this.mCallback);
            Log.d("MediaBrowserCompat", sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  mRootHints=");
            sb3.append(this.mRootHints);
            Log.d("MediaBrowserCompat", sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  mState=");
            sb4.append(getStateLabel(this.mState));
            Log.d("MediaBrowserCompat", sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  mServiceConnection=");
            sb5.append(this.mServiceConnection);
            Log.d("MediaBrowserCompat", sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("  mServiceBinderWrapper=");
            sb6.append(this.mServiceBinderWrapper);
            Log.d("MediaBrowserCompat", sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("  mCallbacksMessenger=");
            sb7.append(this.mCallbacksMessenger);
            Log.d("MediaBrowserCompat", sb7.toString());
            final StringBuilder sb8 = new StringBuilder();
            sb8.append("  mRootId=");
            sb8.append(this.mRootId);
            Log.d("MediaBrowserCompat", sb8.toString());
            final StringBuilder sb9 = new StringBuilder();
            sb9.append("  mMediaSessionToken=");
            sb9.append(this.mMediaSessionToken);
            Log.d("MediaBrowserCompat", sb9.toString());
        }
        
        @Nullable
        @Override
        public Bundle getExtras() {
            if (!this.isConnected()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getExtras() called while not connected (state=");
                sb.append(getStateLabel(this.mState));
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            return this.mExtras;
        }
        
        @Override
        public void getItem(@NonNull final String s, @NonNull final ItemCallback itemCallback) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("mediaId is empty.");
            }
            if (itemCallback == null) {
                throw new IllegalArgumentException("cb is null.");
            }
            Runnable runnable;
            CallbackHandler callbackHandler;
            if (this.mState != 2) {
                Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
                final CallbackHandler mHandler = this.mHandler;
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        itemCallback.onError(s);
                    }
                };
                callbackHandler = mHandler;
            }
            else {
                final ItemReceiver itemReceiver = new ItemReceiver(s, itemCallback, this.mHandler);
                try {
                    this.mServiceBinderWrapper.getMediaItem(s, itemReceiver);
                    return;
                }
                catch (RemoteException ex) {
                    Log.i("MediaBrowserCompat", "Remote error getting media item.");
                    final CallbackHandler mHandler2 = this.mHandler;
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            itemCallback.onError(s);
                        }
                    };
                    callbackHandler = mHandler2;
                }
            }
            callbackHandler.post(runnable);
        }
        
        @NonNull
        @Override
        public String getRoot() {
            if (!this.isConnected()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getRoot() called while not connected(state=");
                sb.append(getStateLabel(this.mState));
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            return this.mRootId;
        }
        
        @NonNull
        @Override
        public ComponentName getServiceComponent() {
            if (!this.isConnected()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getServiceComponent() called while not connected (state=");
                sb.append(this.mState);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            return this.mServiceComponent;
        }
        
        @NonNull
        @Override
        public MediaSessionCompat.Token getSessionToken() {
            if (!this.isConnected()) {
                final StringBuilder sb = new StringBuilder();
                sb.append("getSessionToken() called while not connected(state=");
                sb.append(this.mState);
                sb.append(")");
                throw new IllegalStateException(sb.toString());
            }
            return this.mMediaSessionToken;
        }
        
        @Override
        public boolean isConnected() {
            return this.mState == 2;
        }
        
        @Override
        public void onConnectionFailed(final Messenger messenger) {
            final StringBuilder sb = new StringBuilder();
            sb.append("onConnectFailed for ");
            sb.append(this.mServiceComponent);
            Log.e("MediaBrowserCompat", sb.toString());
            if (!this.isCurrent(messenger, "onConnectFailed")) {
                return;
            }
            if (this.mState != 1) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("onConnect from service while mState=");
                sb2.append(getStateLabel(this.mState));
                sb2.append("... ignoring");
                Log.w("MediaBrowserCompat", sb2.toString());
                return;
            }
            this.forceCloseConnection();
            this.mCallback.onConnectionFailed();
        }
        
        @Override
        public void onLoadChildren(final Messenger messenger, final String s, final List list, final Bundle bundle) {
            if (!this.isCurrent(messenger, "onLoadChildren")) {
                return;
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null) {
                final SubscriptionCallback callback = subscription.getCallback(bundle);
                if (callback != null) {
                    if (bundle == null) {
                        callback.onChildrenLoaded(s, list);
                        return;
                    }
                    callback.onChildrenLoaded(s, list, bundle);
                }
            }
        }
        
        @Override
        public void onServiceConnected(final Messenger messenger, String mRootId, final MediaSessionCompat.Token mMediaSessionToken, Bundle mExtras) {
            if (!this.isCurrent(messenger, "onConnect")) {
                return;
            }
            if (this.mState != 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("onConnect from service while mState=");
                sb.append(getStateLabel(this.mState));
                sb.append("... ignoring");
                Log.w("MediaBrowserCompat", sb.toString());
                return;
            }
            this.mRootId = mRootId;
            this.mMediaSessionToken = mMediaSessionToken;
            this.mExtras = mExtras;
            this.mState = 2;
            this.mCallback.onConnected();
            try {
                for (final Map.Entry<String, Subscription> entry : this.mSubscriptions.entrySet()) {
                    mRootId = entry.getKey();
                    final Iterator<Bundle> iterator2 = entry.getValue().getOptionsList().iterator();
                    while (iterator2.hasNext()) {
                        mExtras = iterator2.next();
                        this.mServiceBinderWrapper.addSubscription(mRootId, mExtras, this.mCallbacksMessenger);
                    }
                }
            }
            catch (RemoteException ex) {
                Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException.");
            }
        }
        
        @Override
        public void subscribe(@NonNull final String s, final Bundle bundle, @NonNull final SubscriptionCallback subscriptionCallback) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("parentId is empty.");
            }
            if (subscriptionCallback == null) {
                throw new IllegalArgumentException("callback is null");
            }
            Subscription subscription;
            if ((subscription = this.mSubscriptions.get(s)) == null) {
                subscription = new Subscription();
                this.mSubscriptions.put(s, subscription);
            }
            subscription.setCallbackForOptions(subscriptionCallback, bundle);
            if (this.mState == 2) {
                try {
                    this.mServiceBinderWrapper.addSubscription(s, bundle, this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("addSubscription failed with RemoteException parentId=");
                    sb.append(s);
                    Log.d("MediaBrowserCompat", sb.toString());
                }
            }
        }
        
        @Override
        public void unsubscribe(@NonNull final String s, final Bundle bundle) {
            if (TextUtils.isEmpty((CharSequence)s)) {
                throw new IllegalArgumentException("parentId is empty.");
            }
            final Subscription subscription = this.mSubscriptions.get(s);
            if (subscription != null && subscription.remove(bundle) && this.mState == 2) {
                try {
                    this.mServiceBinderWrapper.removeSubscription(s, bundle, this.mCallbacksMessenger);
                }
                catch (RemoteException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("removeSubscription failed with RemoteException parentId=");
                    sb.append(s);
                    Log.d("MediaBrowserCompat", sb.toString());
                }
            }
            if (subscription != null && subscription.isEmpty()) {
                this.mSubscriptions.remove(s);
            }
        }
        
        private class MediaServiceConnection implements ServiceConnection
        {
            private boolean isCurrent(final String s) {
                if (MediaBrowserServiceImplBase.this.mServiceConnection != this) {
                    if (MediaBrowserServiceImplBase.this.mState != 0) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append(s);
                        sb.append(" for ");
                        sb.append(MediaBrowserServiceImplBase.this.mServiceComponent);
                        sb.append(" with mServiceConnection=");
                        sb.append(MediaBrowserServiceImplBase.this.mServiceConnection);
                        sb.append(" this=");
                        sb.append(this);
                        Log.i("MediaBrowserCompat", sb.toString());
                    }
                    return false;
                }
                return true;
            }
            
            private void postOrRun(final Runnable runnable) {
                if (Thread.currentThread() == MediaBrowserServiceImplBase.this.mHandler.getLooper().getThread()) {
                    runnable.run();
                    return;
                }
                MediaBrowserServiceImplBase.this.mHandler.post(runnable);
            }
            
            public void onServiceConnected(final ComponentName componentName, final IBinder binder) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (!MediaServiceConnection.this.isCurrent("onServiceConnected")) {
                            return;
                        }
                        MediaBrowserServiceImplBase.this.mServiceBinderWrapper = new ServiceBinderWrapper(binder);
                        MediaBrowserServiceImplBase.this.mCallbacksMessenger = new Messenger((Handler)MediaBrowserServiceImplBase.this.mHandler);
                        MediaBrowserServiceImplBase.this.mHandler.setCallbacksMessenger(MediaBrowserServiceImplBase.this.mCallbacksMessenger);
                        MediaBrowserServiceImplBase.this.mState = 1;
                        try {
                            MediaBrowserServiceImplBase.this.mServiceBinderWrapper.connect(MediaBrowserServiceImplBase.this.mContext, MediaBrowserServiceImplBase.this.mRootHints, MediaBrowserServiceImplBase.this.mCallbacksMessenger);
                        }
                        catch (RemoteException ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("RemoteException during connect for ");
                            sb.append(MediaBrowserServiceImplBase.this.mServiceComponent);
                            Log.w("MediaBrowserCompat", sb.toString());
                        }
                    }
                });
            }
            
            public void onServiceDisconnected(final ComponentName componentName) {
                this.postOrRun(new Runnable() {
                    @Override
                    public void run() {
                        if (!MediaServiceConnection.this.isCurrent("onServiceDisconnected")) {
                            return;
                        }
                        MediaBrowserServiceImplBase.this.mServiceBinderWrapper = null;
                        MediaBrowserServiceImplBase.this.mCallbacksMessenger = null;
                        MediaBrowserServiceImplBase.this.mHandler.setCallbacksMessenger(null);
                        MediaBrowserServiceImplBase.this.mState = 3;
                        MediaBrowserServiceImplBase.this.mCallback.onConnectionSuspended();
                    }
                });
            }
        }
    }
    
    public static class MediaItem implements Parcelable
    {
        public static final Parcelable$Creator<MediaItem> CREATOR;
        public static final int FLAG_BROWSABLE = 1;
        public static final int FLAG_PLAYABLE = 2;
        private final MediaDescriptionCompat mDescription;
        private final int mFlags;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<MediaItem>() {
                public MediaItem createFromParcel(final Parcel parcel) {
                    return new MediaItem(parcel);
                }
                
                public MediaItem[] newArray(final int n) {
                    return new MediaItem[n];
                }
            };
        }
        
        private MediaItem(final Parcel parcel) {
            this.mFlags = parcel.readInt();
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
        }
        
        public MediaItem(@NonNull final MediaDescriptionCompat mDescription, final int mFlags) {
            if (mDescription == null) {
                throw new IllegalArgumentException("description cannot be null");
            }
            if (TextUtils.isEmpty((CharSequence)mDescription.getMediaId())) {
                throw new IllegalArgumentException("description must have a non-empty media id");
            }
            this.mFlags = mFlags;
            this.mDescription = mDescription;
        }
        
        public int describeContents() {
            return 0;
        }
        
        @NonNull
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }
        
        public int getFlags() {
            return this.mFlags;
        }
        
        @NonNull
        public String getMediaId() {
            return this.mDescription.getMediaId();
        }
        
        public boolean isBrowsable() {
            return (this.mFlags & 0x1) != 0x0;
        }
        
        public boolean isPlayable() {
            return (this.mFlags & 0x2) != 0x0;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MediaItem{");
            sb.append("mFlags=");
            sb.append(this.mFlags);
            sb.append(", mDescription=");
            sb.append(this.mDescription);
            sb.append('}');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeInt(this.mFlags);
            this.mDescription.writeToParcel(parcel, n);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }
    
    private static class ServiceBinderWrapper
    {
        private Messenger mMessenger;
        
        public ServiceBinderWrapper(final IBinder binder) {
            this.mMessenger = new Messenger(binder);
        }
        
        private void sendRequest(final int what, final Bundle data, final Messenger replyTo) throws RemoteException {
            final Message obtain = Message.obtain();
            obtain.what = what;
            obtain.arg1 = 1;
            obtain.setData(data);
            obtain.replyTo = replyTo;
            this.mMessenger.send(obtain);
        }
        
        void addSubscription(final String s, final Bundle bundle, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", s);
            bundle2.putBundle("data_options", bundle);
            this.sendRequest(3, bundle2, messenger);
        }
        
        void connect(final Context context, final Bundle bundle, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_package_name", context.getPackageName());
            bundle2.putBundle("data_root_hints", bundle);
            this.sendRequest(1, bundle2, messenger);
        }
        
        void disconnect(final Messenger messenger) throws RemoteException {
            this.sendRequest(2, null, messenger);
        }
        
        void getMediaItem(final String s, final ResultReceiver resultReceiver) throws RemoteException {
            final Bundle bundle = new Bundle();
            bundle.putString("data_media_item_id", s);
            bundle.putParcelable("data_result_receiver", (Parcelable)resultReceiver);
            this.sendRequest(5, bundle, null);
        }
        
        void registerCallbackMessenger(final Messenger messenger) throws RemoteException {
            this.sendRequest(6, null, messenger);
        }
        
        void removeSubscription(final String s, final Bundle bundle, final Messenger messenger) throws RemoteException {
            final Bundle bundle2 = new Bundle();
            bundle2.putString("data_media_item_id", s);
            bundle2.putBundle("data_options", bundle);
            this.sendRequest(4, bundle2, messenger);
        }
    }
    
    private static class Subscription
    {
        private final List<SubscriptionCallback> mCallbacks;
        private final List<Bundle> mOptionsList;
        
        public Subscription() {
            this.mCallbacks = new ArrayList<SubscriptionCallback>();
            this.mOptionsList = new ArrayList<Bundle>();
        }
        
        public SubscriptionCallback getCallback(final Bundle bundle) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    return this.mCallbacks.get(i);
                }
            }
            return null;
        }
        
        public List<SubscriptionCallback> getCallbacks() {
            return this.mCallbacks;
        }
        
        public List<Bundle> getOptionsList() {
            return this.mOptionsList;
        }
        
        public boolean isEmpty() {
            return this.mCallbacks.isEmpty();
        }
        
        public boolean remove(final Bundle bundle) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    this.mCallbacks.remove(i);
                    this.mOptionsList.remove(i);
                    return true;
                }
            }
            return false;
        }
        
        public void setCallbackForOptions(final SubscriptionCallback subscriptionCallback, final Bundle bundle) {
            for (int i = 0; i < this.mOptionsList.size(); ++i) {
                if (MediaBrowserCompatUtils.areSameOptions(this.mOptionsList.get(i), bundle)) {
                    this.mCallbacks.set(i, subscriptionCallback);
                    return;
                }
            }
            this.mCallbacks.add(subscriptionCallback);
            this.mOptionsList.add(bundle);
        }
    }
    
    public abstract static class SubscriptionCallback
    {
        public void onChildrenLoaded(@NonNull final String s, final List<MediaItem> list) {
        }
        
        public void onChildrenLoaded(@NonNull final String s, final List<MediaItem> list, @NonNull final Bundle bundle) {
        }
        
        public void onError(@NonNull final String s) {
        }
        
        public void onError(@NonNull final String s, @NonNull final Bundle bundle) {
        }
    }
    
    static class SubscriptionCallbackApi21 extends SubscriptionCallback
    {
        private Bundle mOptions;
        SubscriptionCallback mSubscriptionCallback;
        private final Object mSubscriptionCallbackObj;
        
        public SubscriptionCallbackApi21(final SubscriptionCallback mSubscriptionCallback, final Bundle mOptions) {
            this.mSubscriptionCallback = mSubscriptionCallback;
            this.mOptions = mOptions;
            this.mSubscriptionCallbackObj = MediaBrowserCompatApi21.createSubscriptionCallback((MediaBrowserCompatApi21.SubscriptionCallback)new StubApi21());
        }
        
        @Override
        public void onChildrenLoaded(@NonNull final String s, final List<MediaItem> list) {
            this.mSubscriptionCallback.onChildrenLoaded(s, list);
        }
        
        @Override
        public void onChildrenLoaded(@NonNull final String s, final List<MediaItem> list, @NonNull final Bundle bundle) {
            this.mSubscriptionCallback.onChildrenLoaded(s, list, bundle);
        }
        
        @Override
        public void onError(@NonNull final String s) {
            this.mSubscriptionCallback.onError(s);
        }
        
        @Override
        public void onError(@NonNull final String s, @NonNull final Bundle bundle) {
            this.mSubscriptionCallback.onError(s, bundle);
        }
        
        private class StubApi21 implements MediaBrowserCompatApi21.SubscriptionCallback
        {
            @Override
            public void onChildrenLoaded(@NonNull final String s, final List<Parcel> list) {
                ArrayList<MediaItem> list3;
                if (list != null) {
                    final ArrayList<MediaItem> list2 = new ArrayList<MediaItem>();
                    final Iterator<Parcel> iterator = list.iterator();
                    while (true) {
                        list3 = list2;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final Parcel parcel = iterator.next();
                        parcel.setDataPosition(0);
                        list2.add((MediaItem)MediaItem.CREATOR.createFromParcel(parcel));
                        parcel.recycle();
                    }
                }
                else {
                    list3 = null;
                }
                if (SubscriptionCallbackApi21.this.mOptions != null) {
                    SubscriptionCallbackApi21.this.onChildrenLoaded(s, MediaBrowserCompatUtils.applyOptions(list3, SubscriptionCallbackApi21.this.mOptions), SubscriptionCallbackApi21.this.mOptions);
                    return;
                }
                SubscriptionCallbackApi21.this.onChildrenLoaded(s, list3);
            }
            
            @Override
            public void onError(@NonNull final String s) {
                if (SubscriptionCallbackApi21.this.mOptions != null) {
                    SubscriptionCallbackApi21.this.onError(s, SubscriptionCallbackApi21.this.mOptions);
                    return;
                }
                SubscriptionCallbackApi21.this.onError(s);
            }
        }
    }
}
