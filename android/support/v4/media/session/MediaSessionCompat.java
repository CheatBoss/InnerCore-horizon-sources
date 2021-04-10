package android.support.v4.media.session;

import android.app.*;
import android.text.*;
import android.content.*;
import android.content.pm.*;
import android.util.*;
import java.util.*;
import android.net.*;
import android.media.*;
import android.graphics.*;
import android.view.*;
import android.support.v4.media.*;
import java.lang.annotation.*;
import android.os.*;

public class MediaSessionCompat
{
    public static final String ACTION_ARGUMENT_EXTRAS = "android.support.v4.media.session.action.ARGUMENT_EXTRAS";
    public static final String ACTION_ARGUMENT_URI = "android.support.v4.media.session.action.ARGUMENT_URI";
    public static final String ACTION_PLAY_FROM_URI = "android.support.v4.media.session.action.PLAY_FROM_URI";
    public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
    public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
    private static final String TAG = "MediaSessionCompat";
    private final ArrayList<OnActiveChangeListener> mActiveListeners;
    private final MediaControllerCompat mController;
    private final MediaSessionImpl mImpl;
    
    private MediaSessionCompat(final Context context, final MediaSessionImpl mImpl) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        this.mImpl = mImpl;
        this.mController = new MediaControllerCompat(context, this);
    }
    
    public MediaSessionCompat(final Context context, final String s) {
        this(context, s, null, null);
    }
    
    public MediaSessionCompat(final Context context, final String s, final ComponentName componentName, final PendingIntent pendingIntent) {
        this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
        if (context == null) {
            throw new IllegalArgumentException("context must not be null");
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("tag must not be null or empty");
        }
        ComponentName component;
        if ((component = componentName) == null) {
            final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.setPackage(context.getPackageName());
            final List queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
            if (queryBroadcastReceivers.size() == 1) {
                final ResolveInfo resolveInfo = queryBroadcastReceivers.get(0);
                component = new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
            }
            else {
                component = componentName;
                if (queryBroadcastReceivers.size() > 1) {
                    Log.w("MediaSessionCompat", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, using null. Provide a specific ComponentName to use as this session's media button receiver");
                    component = componentName;
                }
            }
        }
        PendingIntent broadcast = pendingIntent;
        if (component != null && (broadcast = pendingIntent) == null) {
            final Intent intent2 = new Intent("android.intent.action.MEDIA_BUTTON");
            intent2.setComponent(component);
            broadcast = PendingIntent.getBroadcast(context, 0, intent2, 0);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            (this.mImpl = (MediaSessionImpl)new MediaSessionImplApi21(context, s)).setMediaButtonReceiver(broadcast);
        }
        else {
            this.mImpl = (MediaSessionImpl)new MediaSessionImplBase(context, s, component, broadcast);
        }
        this.mController = new MediaControllerCompat(context, this);
    }
    
    public static MediaSessionCompat obtain(final Context context, final Object o) {
        return new MediaSessionCompat(context, (MediaSessionImpl)new MediaSessionImplApi21(o));
    }
    
    public void addOnActiveChangeListener(final OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.add(onActiveChangeListener);
    }
    
    public MediaControllerCompat getController() {
        return this.mController;
    }
    
    public Object getMediaSession() {
        return this.mImpl.getMediaSession();
    }
    
    public Object getRemoteControlClient() {
        return this.mImpl.getRemoteControlClient();
    }
    
    public Token getSessionToken() {
        return this.mImpl.getSessionToken();
    }
    
    public boolean isActive() {
        return this.mImpl.isActive();
    }
    
    public void release() {
        this.mImpl.release();
    }
    
    public void removeOnActiveChangeListener(final OnActiveChangeListener onActiveChangeListener) {
        if (onActiveChangeListener == null) {
            throw new IllegalArgumentException("Listener may not be null");
        }
        this.mActiveListeners.remove(onActiveChangeListener);
    }
    
    public void sendSessionEvent(final String s, final Bundle bundle) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("event cannot be null or empty");
        }
        this.mImpl.sendSessionEvent(s, bundle);
    }
    
    public void setActive(final boolean active) {
        this.mImpl.setActive(active);
        final Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onActiveChanged();
        }
    }
    
    public void setCallback(final Callback callback) {
        this.setCallback(callback, null);
    }
    
    public void setCallback(final Callback callback, Handler handler) {
        final MediaSessionImpl mImpl = this.mImpl;
        if (handler == null) {
            handler = new Handler();
        }
        mImpl.setCallback(callback, handler);
    }
    
    public void setExtras(final Bundle extras) {
        this.mImpl.setExtras(extras);
    }
    
    public void setFlags(final int flags) {
        this.mImpl.setFlags(flags);
    }
    
    public void setMediaButtonReceiver(final PendingIntent mediaButtonReceiver) {
        this.mImpl.setMediaButtonReceiver(mediaButtonReceiver);
    }
    
    public void setMetadata(final MediaMetadataCompat metadata) {
        this.mImpl.setMetadata(metadata);
    }
    
    public void setPlaybackState(final PlaybackStateCompat playbackState) {
        this.mImpl.setPlaybackState(playbackState);
    }
    
    public void setPlaybackToLocal(final int playbackToLocal) {
        this.mImpl.setPlaybackToLocal(playbackToLocal);
    }
    
    public void setPlaybackToRemote(final VolumeProviderCompat playbackToRemote) {
        if (playbackToRemote == null) {
            throw new IllegalArgumentException("volumeProvider may not be null!");
        }
        this.mImpl.setPlaybackToRemote(playbackToRemote);
    }
    
    public void setQueue(final List<QueueItem> queue) {
        this.mImpl.setQueue(queue);
    }
    
    public void setQueueTitle(final CharSequence queueTitle) {
        this.mImpl.setQueueTitle(queueTitle);
    }
    
    public void setRatingType(final int ratingType) {
        this.mImpl.setRatingType(ratingType);
    }
    
    public void setSessionActivity(final PendingIntent sessionActivity) {
        this.mImpl.setSessionActivity(sessionActivity);
    }
    
    public abstract static class Callback
    {
        final Object mCallbackObj;
        
        public Callback() {
            Object mCallbackObj;
            if (Build$VERSION.SDK_INT >= 23) {
                mCallbackObj = MediaSessionCompatApi23.createCallback((MediaSessionCompatApi23.Callback)new StubApi23());
            }
            else {
                if (Build$VERSION.SDK_INT < 21) {
                    this.mCallbackObj = null;
                    return;
                }
                mCallbackObj = MediaSessionCompatApi21.createCallback((MediaSessionCompatApi21.Callback)new StubApi21());
            }
            this.mCallbackObj = mCallbackObj;
        }
        
        public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        }
        
        public void onCustomAction(final String s, final Bundle bundle) {
        }
        
        public void onFastForward() {
        }
        
        public boolean onMediaButtonEvent(final Intent intent) {
            return false;
        }
        
        public void onPause() {
        }
        
        public void onPlay() {
        }
        
        public void onPlayFromMediaId(final String s, final Bundle bundle) {
        }
        
        public void onPlayFromSearch(final String s, final Bundle bundle) {
        }
        
        public void onPlayFromUri(final Uri uri, final Bundle bundle) {
        }
        
        public void onRewind() {
        }
        
        public void onSeekTo(final long n) {
        }
        
        public void onSetRating(final RatingCompat ratingCompat) {
        }
        
        public void onSkipToNext() {
        }
        
        public void onSkipToPrevious() {
        }
        
        public void onSkipToQueueItem(final long n) {
        }
        
        public void onStop() {
        }
        
        private class StubApi21 implements MediaSessionCompatApi21.Callback
        {
            @Override
            public void onCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
                MediaSessionCompat.Callback.this.onCommand(s, bundle, resultReceiver);
            }
            
            @Override
            public void onCustomAction(final String s, Bundle bundle) {
                if (s.equals("android.support.v4.media.session.action.PLAY_FROM_URI")) {
                    final Uri uri = (Uri)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_URI");
                    bundle = (Bundle)bundle.getParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS");
                    MediaSessionCompat.Callback.this.onPlayFromUri(uri, bundle);
                    return;
                }
                MediaSessionCompat.Callback.this.onCustomAction(s, bundle);
            }
            
            @Override
            public void onFastForward() {
                MediaSessionCompat.Callback.this.onFastForward();
            }
            
            @Override
            public boolean onMediaButtonEvent(final Intent intent) {
                return MediaSessionCompat.Callback.this.onMediaButtonEvent(intent);
            }
            
            @Override
            public void onPause() {
                MediaSessionCompat.Callback.this.onPause();
            }
            
            @Override
            public void onPlay() {
                MediaSessionCompat.Callback.this.onPlay();
            }
            
            @Override
            public void onPlayFromMediaId(final String s, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onPlayFromMediaId(s, bundle);
            }
            
            @Override
            public void onPlayFromSearch(final String s, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onPlayFromSearch(s, bundle);
            }
            
            @Override
            public void onRewind() {
                MediaSessionCompat.Callback.this.onRewind();
            }
            
            @Override
            public void onSeekTo(final long n) {
                MediaSessionCompat.Callback.this.onSeekTo(n);
            }
            
            @Override
            public void onSetRating(final Object o) {
                MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(o));
            }
            
            @Override
            public void onSkipToNext() {
                MediaSessionCompat.Callback.this.onSkipToNext();
            }
            
            @Override
            public void onSkipToPrevious() {
                MediaSessionCompat.Callback.this.onSkipToPrevious();
            }
            
            @Override
            public void onSkipToQueueItem(final long n) {
                MediaSessionCompat.Callback.this.onSkipToQueueItem(n);
            }
            
            @Override
            public void onStop() {
                MediaSessionCompat.Callback.this.onStop();
            }
        }
        
        private class StubApi23 extends StubApi21 implements MediaSessionCompatApi23.Callback
        {
            @Override
            public void onPlayFromUri(final Uri uri, final Bundle bundle) {
                MediaSessionCompat.Callback.this.onPlayFromUri(uri, bundle);
            }
        }
    }
    
    interface MediaSessionImpl
    {
        Object getMediaSession();
        
        Object getRemoteControlClient();
        
        Token getSessionToken();
        
        boolean isActive();
        
        void release();
        
        void sendSessionEvent(final String p0, final Bundle p1);
        
        void setActive(final boolean p0);
        
        void setCallback(final Callback p0, final Handler p1);
        
        void setExtras(final Bundle p0);
        
        void setFlags(final int p0);
        
        void setMediaButtonReceiver(final PendingIntent p0);
        
        void setMetadata(final MediaMetadataCompat p0);
        
        void setPlaybackState(final PlaybackStateCompat p0);
        
        void setPlaybackToLocal(final int p0);
        
        void setPlaybackToRemote(final VolumeProviderCompat p0);
        
        void setQueue(final List<QueueItem> p0);
        
        void setQueueTitle(final CharSequence p0);
        
        void setRatingType(final int p0);
        
        void setSessionActivity(final PendingIntent p0);
    }
    
    static class MediaSessionImplApi21 implements MediaSessionImpl
    {
        private PendingIntent mMediaButtonIntent;
        private final Object mSessionObj;
        private final Token mToken;
        
        public MediaSessionImplApi21(final Context context, final String s) {
            this.mSessionObj = MediaSessionCompatApi21.createSession(context, s);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }
        
        public MediaSessionImplApi21(final Object o) {
            this.mSessionObj = MediaSessionCompatApi21.verifySession(o);
            this.mToken = new Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
        }
        
        @Override
        public Object getMediaSession() {
            return this.mSessionObj;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return null;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return MediaSessionCompatApi21.isActive(this.mSessionObj);
        }
        
        @Override
        public void release() {
            MediaSessionCompatApi21.release(this.mSessionObj);
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
            MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, s, bundle);
        }
        
        @Override
        public void setActive(final boolean b) {
            MediaSessionCompatApi21.setActive(this.mSessionObj, b);
        }
        
        @Override
        public void setCallback(final Callback callback, final Handler handler) {
            final Object mSessionObj = this.mSessionObj;
            Object mCallbackObj;
            if (callback == null) {
                mCallbackObj = null;
            }
            else {
                mCallbackObj = callback.mCallbackObj;
            }
            MediaSessionCompatApi21.setCallback(mSessionObj, mCallbackObj, handler);
        }
        
        @Override
        public void setExtras(final Bundle bundle) {
            MediaSessionCompatApi21.setExtras(this.mSessionObj, bundle);
        }
        
        @Override
        public void setFlags(final int n) {
            MediaSessionCompatApi21.setFlags(this.mSessionObj, n);
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent mMediaButtonIntent) {
            this.mMediaButtonIntent = mMediaButtonIntent;
            MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, mMediaButtonIntent);
        }
        
        @Override
        public void setMetadata(final MediaMetadataCompat mediaMetadataCompat) {
            final Object mSessionObj = this.mSessionObj;
            Object mediaMetadata;
            if (mediaMetadataCompat == null) {
                mediaMetadata = null;
            }
            else {
                mediaMetadata = mediaMetadataCompat.getMediaMetadata();
            }
            MediaSessionCompatApi21.setMetadata(mSessionObj, mediaMetadata);
        }
        
        @Override
        public void setPlaybackState(final PlaybackStateCompat playbackStateCompat) {
            final Object mSessionObj = this.mSessionObj;
            Object playbackState;
            if (playbackStateCompat == null) {
                playbackState = null;
            }
            else {
                playbackState = playbackStateCompat.getPlaybackState();
            }
            MediaSessionCompatApi21.setPlaybackState(mSessionObj, playbackState);
        }
        
        @Override
        public void setPlaybackToLocal(final int n) {
            MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, n);
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat volumeProviderCompat) {
            MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, volumeProviderCompat.getVolumeProvider());
        }
        
        @Override
        public void setQueue(final List<QueueItem> list) {
            ArrayList<Object> list3;
            if (list != null) {
                final ArrayList<Object> list2 = new ArrayList<Object>();
                final Iterator<QueueItem> iterator = list.iterator();
                while (true) {
                    list3 = list2;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list2.add(iterator.next().getQueueItem());
                }
            }
            else {
                list3 = null;
            }
            MediaSessionCompatApi21.setQueue(this.mSessionObj, list3);
        }
        
        @Override
        public void setQueueTitle(final CharSequence charSequence) {
            MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, charSequence);
        }
        
        @Override
        public void setRatingType(final int n) {
            if (Build$VERSION.SDK_INT < 22) {
                return;
            }
            MediaSessionCompatApi22.setRatingType(this.mSessionObj, n);
        }
        
        @Override
        public void setSessionActivity(final PendingIntent pendingIntent) {
            MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, pendingIntent);
        }
    }
    
    static class MediaSessionImplBase implements MediaSessionImpl
    {
        private final AudioManager mAudioManager;
        private volatile Callback mCallback;
        private final ComponentName mComponentName;
        private final Context mContext;
        private final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks;
        private boolean mDestroyed;
        private Bundle mExtras;
        private int mFlags;
        private MessageHandler mHandler;
        private boolean mIsActive;
        private boolean mIsMbrRegistered;
        private boolean mIsRccRegistered;
        private int mLocalStream;
        private final Object mLock;
        private final PendingIntent mMediaButtonEventReceiver;
        private MediaMetadataCompat mMetadata;
        private final String mPackageName;
        private List<QueueItem> mQueue;
        private CharSequence mQueueTitle;
        private int mRatingType;
        private final Object mRccObj;
        private PendingIntent mSessionActivity;
        private PlaybackStateCompat mState;
        private final MediaSessionStub mStub;
        private final String mTag;
        private final Token mToken;
        private VolumeProviderCompat.Callback mVolumeCallback;
        private VolumeProviderCompat mVolumeProvider;
        private int mVolumeType;
        
        public MediaSessionImplBase(final Context mContext, final String mTag, final ComponentName mComponentName, final PendingIntent mMediaButtonEventReceiver) {
            this.mLock = new Object();
            this.mControllerCallbacks = (RemoteCallbackList<IMediaControllerCallback>)new RemoteCallbackList();
            this.mDestroyed = false;
            this.mIsActive = false;
            this.mIsRccRegistered = false;
            this.mIsMbrRegistered = false;
            this.mVolumeCallback = new VolumeProviderCompat.Callback() {
                @Override
                public void onVolumeChanged(final VolumeProviderCompat volumeProviderCompat) {
                    if (MediaSessionImplBase.this.mVolumeProvider != volumeProviderCompat) {
                        return;
                    }
                    MediaSessionImplBase.this.sendVolumeInfoChanged(new ParcelableVolumeInfo(MediaSessionImplBase.this.mVolumeType, MediaSessionImplBase.this.mLocalStream, volumeProviderCompat.getVolumeControl(), volumeProviderCompat.getMaxVolume(), volumeProviderCompat.getCurrentVolume()));
                }
            };
            if (mComponentName == null) {
                throw new IllegalArgumentException("MediaButtonReceiver component may not be null.");
            }
            this.mContext = mContext;
            this.mPackageName = mContext.getPackageName();
            this.mAudioManager = (AudioManager)mContext.getSystemService("audio");
            this.mTag = mTag;
            this.mComponentName = mComponentName;
            this.mMediaButtonEventReceiver = mMediaButtonEventReceiver;
            this.mStub = new MediaSessionStub();
            this.mToken = new Token(this.mStub);
            this.mRatingType = 0;
            this.mVolumeType = 1;
            this.mLocalStream = 3;
            Object remoteControlClient;
            if (Build$VERSION.SDK_INT >= 14) {
                remoteControlClient = MediaSessionCompatApi14.createRemoteControlClient(mMediaButtonEventReceiver);
            }
            else {
                remoteControlClient = null;
            }
            this.mRccObj = remoteControlClient;
        }
        
        private void adjustVolume(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onAdjustVolume(n);
                }
                return;
            }
            this.mAudioManager.adjustStreamVolume(this.mLocalStream, n, n2);
        }
        
        private MediaMetadataCompat cloneMetadataIfNeeded(final MediaMetadataCompat mediaMetadataCompat) {
            if (mediaMetadataCompat == null) {
                return null;
            }
            if (!mediaMetadataCompat.containsKey("android.media.metadata.ART") && !mediaMetadataCompat.containsKey("android.media.metadata.ALBUM_ART")) {
                return mediaMetadataCompat;
            }
            final MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder(mediaMetadataCompat);
            final Bitmap bitmap = mediaMetadataCompat.getBitmap("android.media.metadata.ART");
            if (bitmap != null) {
                builder.putBitmap("android.media.metadata.ART", bitmap.copy(bitmap.getConfig(), false));
            }
            final Bitmap bitmap2 = mediaMetadataCompat.getBitmap("android.media.metadata.ALBUM_ART");
            if (bitmap2 != null) {
                builder.putBitmap("android.media.metadata.ALBUM_ART", bitmap2.copy(bitmap2.getConfig(), false));
            }
            return builder.build();
        }
        
        private PlaybackStateCompat getStateWithUpdatedPosition() {
            while (true) {
                Object o = this.mLock;
                while (true) {
                    synchronized (o) {
                        final PlaybackStateCompat mState = this.mState;
                        if (this.mMetadata != null && this.mMetadata.containsKey("android.media.metadata.DURATION")) {
                            long long1 = this.mMetadata.getLong("android.media.metadata.DURATION");
                            // monitorexit(o)
                            final Object o2 = o = null;
                            Label_0200: {
                                if (mState != null) {
                                    if (mState.getState() != 3 && mState.getState() != 4) {
                                        o = o2;
                                        if (mState.getState() != 5) {
                                            break Label_0200;
                                        }
                                    }
                                    final long lastPositionUpdateTime = mState.getLastPositionUpdateTime();
                                    final long elapsedRealtime = SystemClock.elapsedRealtime();
                                    o = o2;
                                    if (lastPositionUpdateTime > 0L) {
                                        final long n = (long)(mState.getPlaybackSpeed() * (elapsedRealtime - lastPositionUpdateTime)) + mState.getPosition();
                                        if (long1 < 0L || n <= long1) {
                                            if (n < 0L) {
                                                long1 = 0L;
                                            }
                                            else {
                                                long1 = n;
                                            }
                                        }
                                        o = new PlaybackStateCompat.Builder(mState);
                                        ((PlaybackStateCompat.Builder)o).setState(mState.getState(), long1, mState.getPlaybackSpeed(), elapsedRealtime);
                                        o = ((PlaybackStateCompat.Builder)o).build();
                                    }
                                }
                            }
                            if (o == null) {
                                return mState;
                            }
                            return (PlaybackStateCompat)o;
                        }
                    }
                    long long1 = -1L;
                    continue;
                }
            }
        }
        
        private void postToHandler(final int n) {
            this.postToHandler(n, null);
        }
        
        private void postToHandler(final int n, final Object o) {
            this.postToHandler(n, o, null);
        }
        
        private void postToHandler(final int n, final Object o, final Bundle bundle) {
            synchronized (this.mLock) {
                if (this.mHandler != null) {
                    this.mHandler.post(n, o, bundle);
                }
            }
        }
        
        private void sendEvent(final String s, final Bundle bundle) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onEvent(s, bundle);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void sendMetadata(final MediaMetadataCompat mediaMetadataCompat) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onMetadataChanged(mediaMetadataCompat);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void sendQueue(final List<QueueItem> list) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onQueueChanged(list);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void sendQueueTitle(final CharSequence charSequence) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onQueueTitleChanged(charSequence);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void sendSessionDestroyed() {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onSessionDestroyed();
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
            this.mControllerCallbacks.kill();
        }
        
        private void sendState(final PlaybackStateCompat playbackStateCompat) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onPlaybackStateChanged(playbackStateCompat);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void sendVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) {
            int beginBroadcast = this.mControllerCallbacks.beginBroadcast();
            while (true) {
                --beginBroadcast;
                if (beginBroadcast < 0) {
                    break;
                }
                final IMediaControllerCallback mediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(beginBroadcast);
                try {
                    mediaControllerCallback.onVolumeInfoChanged(parcelableVolumeInfo);
                }
                catch (RemoteException ex) {}
            }
            this.mControllerCallbacks.finishBroadcast();
        }
        
        private void setVolumeTo(final int n, final int n2) {
            if (this.mVolumeType == 2) {
                if (this.mVolumeProvider != null) {
                    this.mVolumeProvider.onSetVolumeTo(n);
                }
                return;
            }
            this.mAudioManager.setStreamVolume(this.mLocalStream, n, n2);
        }
        
        private boolean update() {
            if (this.mIsActive) {
                if (Build$VERSION.SDK_INT >= 8) {
                    if (!this.mIsMbrRegistered && (this.mFlags & 0x1) != 0x0) {
                        if (Build$VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                        }
                        else {
                            MediaSessionCompatApi8.registerMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = true;
                    }
                    else if (this.mIsMbrRegistered && (this.mFlags & 0x1) == 0x0) {
                        if (Build$VERSION.SDK_INT >= 18) {
                            MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                        }
                        else {
                            MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                        }
                        this.mIsMbrRegistered = false;
                    }
                }
                if (Build$VERSION.SDK_INT < 14) {
                    return false;
                }
                if (!this.mIsRccRegistered && (this.mFlags & 0x2) != 0x0) {
                    MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
                    return this.mIsRccRegistered = true;
                }
                if (!this.mIsRccRegistered || (this.mFlags & 0x2) != 0x0) {
                    return false;
                }
            }
            else {
                if (this.mIsMbrRegistered) {
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
                    }
                    else {
                        MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
                    }
                    this.mIsMbrRegistered = false;
                }
                if (!this.mIsRccRegistered) {
                    return false;
                }
            }
            MediaSessionCompatApi14.setState(this.mRccObj, 0);
            MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
            return this.mIsRccRegistered = false;
        }
        
        @Override
        public Object getMediaSession() {
            return null;
        }
        
        @Override
        public Object getRemoteControlClient() {
            return this.mRccObj;
        }
        
        @Override
        public Token getSessionToken() {
            return this.mToken;
        }
        
        @Override
        public boolean isActive() {
            return this.mIsActive;
        }
        
        @Override
        public void release() {
            this.mIsActive = false;
            this.mDestroyed = true;
            this.update();
            this.sendSessionDestroyed();
        }
        
        @Override
        public void sendSessionEvent(final String s, final Bundle bundle) {
            this.sendEvent(s, bundle);
        }
        
        @Override
        public void setActive(final boolean mIsActive) {
            if (mIsActive == this.mIsActive) {
                return;
            }
            this.mIsActive = mIsActive;
            if (this.update()) {
                this.setMetadata(this.mMetadata);
                this.setPlaybackState(this.mState);
            }
        }
        
        @Override
        public void setCallback(final Callback mCallback, Handler handler) {
            this.mCallback = mCallback;
            Handler handler2;
            if (mCallback == null) {
                if (Build$VERSION.SDK_INT >= 18) {
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null);
                }
                if (Build$VERSION.SDK_INT >= 19) {
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null);
                }
                return;
            }
            else if ((handler2 = handler) == null) {
                handler2 = new Handler();
            }
            handler = (Handler)this.mLock;
            synchronized (handler) {
                this.mHandler = new MessageHandler(handler2.getLooper());
                // monitorexit(handler)
                final MediaSessionCompatApi19.Callback callback = new MediaSessionCompatApi19.Callback() {
                    @Override
                    public void onSeekTo(final long n) {
                        MediaSessionImplBase.this.postToHandler(11, n);
                    }
                    
                    @Override
                    public void onSetRating(final Object o) {
                        MediaSessionImplBase.this.postToHandler(12, RatingCompat.fromRating(o));
                    }
                };
                if (Build$VERSION.SDK_INT >= 18) {
                    handler = (Handler)MediaSessionCompatApi18.createPlaybackPositionUpdateListener((MediaSessionCompatApi18.Callback)callback);
                    MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, handler);
                }
                if (Build$VERSION.SDK_INT >= 19) {
                    MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, MediaSessionCompatApi19.createMetadataUpdateListener((MediaSessionCompatApi19.Callback)callback));
                }
            }
        }
        
        @Override
        public void setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
        }
        
        @Override
        public void setFlags(final int mFlags) {
            synchronized (this.mLock) {
                this.mFlags = mFlags;
                // monitorexit(this.mLock)
                this.update();
            }
        }
        
        @Override
        public void setMediaButtonReceiver(final PendingIntent pendingIntent) {
        }
        
        @Override
        public void setMetadata(MediaMetadataCompat mediaMetadataCompat) {
            MediaMetadataCompat cloneMetadataIfNeeded = mediaMetadataCompat;
            if (Build$VERSION.SDK_INT >= 14 && (cloneMetadataIfNeeded = mediaMetadataCompat) != null) {
                cloneMetadataIfNeeded = this.cloneMetadataIfNeeded(mediaMetadataCompat);
            }
            mediaMetadataCompat = (MediaMetadataCompat)this.mLock;
            synchronized (mediaMetadataCompat) {
                this.mMetadata = cloneMetadataIfNeeded;
                // monitorexit(mediaMetadataCompat)
                this.sendMetadata(cloneMetadataIfNeeded);
                if (!this.mIsActive) {
                    return;
                }
                final int sdk_INT = Build$VERSION.SDK_INT;
                final MediaMetadataCompat mediaMetadataCompat2 = null;
                mediaMetadataCompat = null;
                if (sdk_INT >= 19) {
                    final Object mRccObj = this.mRccObj;
                    if (cloneMetadataIfNeeded != null) {
                        mediaMetadataCompat = (MediaMetadataCompat)cloneMetadataIfNeeded.getBundle();
                    }
                    long actions;
                    if (this.mState == null) {
                        actions = 0L;
                    }
                    else {
                        actions = this.mState.getActions();
                    }
                    MediaSessionCompatApi19.setMetadata(mRccObj, (Bundle)mediaMetadataCompat, actions);
                    return;
                }
                if (Build$VERSION.SDK_INT >= 14) {
                    final Object mRccObj2 = this.mRccObj;
                    if (cloneMetadataIfNeeded == null) {
                        mediaMetadataCompat = mediaMetadataCompat2;
                    }
                    else {
                        mediaMetadataCompat = (MediaMetadataCompat)cloneMetadataIfNeeded.getBundle();
                    }
                    MediaSessionCompatApi14.setMetadata(mRccObj2, (Bundle)mediaMetadataCompat);
                }
            }
        }
        
        @Override
        public void setPlaybackState(final PlaybackStateCompat mState) {
            synchronized (this.mLock) {
                this.mState = mState;
                // monitorexit(this.mLock)
                this.sendState(mState);
                if (!this.mIsActive) {
                    return;
                }
                if (mState == null) {
                    if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setState(this.mRccObj, 0);
                        MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0L);
                    }
                }
                else {
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.setState(this.mRccObj, mState.getState(), mState.getPosition(), mState.getPlaybackSpeed(), mState.getLastPositionUpdateTime());
                    }
                    else if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setState(this.mRccObj, mState.getState());
                    }
                    if (Build$VERSION.SDK_INT >= 19) {
                        MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, mState.getActions());
                        return;
                    }
                    if (Build$VERSION.SDK_INT >= 18) {
                        MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, mState.getActions());
                        return;
                    }
                    if (Build$VERSION.SDK_INT >= 14) {
                        MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, mState.getActions());
                    }
                }
            }
        }
        
        @Override
        public void setPlaybackToLocal(final int n) {
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 1;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
        }
        
        @Override
        public void setPlaybackToRemote(final VolumeProviderCompat mVolumeProvider) {
            if (mVolumeProvider == null) {
                throw new IllegalArgumentException("volumeProvider may not be null");
            }
            if (this.mVolumeProvider != null) {
                this.mVolumeProvider.setCallback(null);
            }
            this.mVolumeType = 2;
            this.mVolumeProvider = mVolumeProvider;
            this.sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
            mVolumeProvider.setCallback(this.mVolumeCallback);
        }
        
        @Override
        public void setQueue(final List<QueueItem> mQueue) {
            this.sendQueue(this.mQueue = mQueue);
        }
        
        @Override
        public void setQueueTitle(final CharSequence mQueueTitle) {
            this.sendQueueTitle(this.mQueueTitle = mQueueTitle);
        }
        
        @Override
        public void setRatingType(final int mRatingType) {
            this.mRatingType = mRatingType;
        }
        
        @Override
        public void setSessionActivity(final PendingIntent mSessionActivity) {
            synchronized (this.mLock) {
                this.mSessionActivity = mSessionActivity;
            }
        }
        
        private static final class Command
        {
            public final String command;
            public final Bundle extras;
            public final ResultReceiver stub;
            
            public Command(final String command, final Bundle extras, final ResultReceiver stub) {
                this.command = command;
                this.extras = extras;
                this.stub = stub;
            }
        }
        
        class MediaSessionStub extends Stub
        {
            public void adjustVolume(final int n, final int n2, final String s) {
                MediaSessionImplBase.this.adjustVolume(n, n2);
            }
            
            public void fastForward() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(9);
            }
            
            public Bundle getExtras() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mExtras;
                }
            }
            
            public long getFlags() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mFlags;
                }
            }
            
            public PendingIntent getLaunchPendingIntent() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mSessionActivity;
                }
            }
            
            public MediaMetadataCompat getMetadata() {
                return MediaSessionImplBase.this.mMetadata;
            }
            
            public String getPackageName() {
                return MediaSessionImplBase.this.mPackageName;
            }
            
            public PlaybackStateCompat getPlaybackState() {
                return MediaSessionImplBase.this.getStateWithUpdatedPosition();
            }
            
            public List<QueueItem> getQueue() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    return MediaSessionImplBase.this.mQueue;
                }
            }
            
            public CharSequence getQueueTitle() {
                return MediaSessionImplBase.this.mQueueTitle;
            }
            
            public int getRatingType() {
                return MediaSessionImplBase.this.mRatingType;
            }
            
            public String getTag() {
                return MediaSessionImplBase.this.mTag;
            }
            
            public ParcelableVolumeInfo getVolumeAttributes() {
                synchronized (MediaSessionImplBase.this.mLock) {
                    final int access$400 = MediaSessionImplBase.this.mVolumeType;
                    final int access$401 = MediaSessionImplBase.this.mLocalStream;
                    final VolumeProviderCompat access$402 = MediaSessionImplBase.this.mVolumeProvider;
                    int volumeControl;
                    int n;
                    int n2;
                    if (access$400 == 2) {
                        volumeControl = access$402.getVolumeControl();
                        n = access$402.getMaxVolume();
                        n2 = access$402.getCurrentVolume();
                    }
                    else {
                        n = MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(access$401);
                        n2 = MediaSessionImplBase.this.mAudioManager.getStreamVolume(access$401);
                        volumeControl = 2;
                    }
                    // monitorexit(MediaSessionImplBase.access$1400(this.this$0))
                    return new ParcelableVolumeInfo(access$400, access$401, volumeControl, n, n2);
                }
            }
            
            public boolean isTransportControlEnabled() {
                return (MediaSessionImplBase.this.mFlags & 0x2) != 0x0;
            }
            
            public void next() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(7);
            }
            
            public void pause() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(5);
            }
            
            public void play() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(1);
            }
            
            public void playFromMediaId(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(2, s, bundle);
            }
            
            public void playFromSearch(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(3, s, bundle);
            }
            
            public void playFromUri(final Uri uri, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(18, uri, bundle);
            }
            
            public void previous() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(8);
            }
            
            public void rate(final RatingCompat ratingCompat) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(12, ratingCompat);
            }
            
            public void registerCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                if (MediaSessionImplBase.this.mDestroyed) {
                    try {
                        mediaControllerCallback.onSessionDestroyed();
                        return;
                    }
                    catch (Exception ex) {
                        return;
                    }
                }
                MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)mediaControllerCallback);
            }
            
            public void rewind() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(10);
            }
            
            public void seekTo(final long n) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(11, n);
            }
            
            public void sendCommand(final String s, final Bundle bundle, final ResultReceiverWrapper resultReceiverWrapper) {
                MediaSessionImplBase.this.postToHandler(15, new Command(s, bundle, resultReceiverWrapper.mResultReceiver));
            }
            
            public void sendCustomAction(final String s, final Bundle bundle) throws RemoteException {
                MediaSessionImplBase.this.postToHandler(13, s, bundle);
            }
            
            public boolean sendMediaButton(final KeyEvent keyEvent) {
                final int access$900 = MediaSessionImplBase.this.mFlags;
                boolean b = true;
                if ((access$900 & 0x1) == 0x0) {
                    b = false;
                }
                if (b) {
                    MediaSessionImplBase.this.postToHandler(14, keyEvent);
                }
                return b;
            }
            
            public void setVolumeTo(final int n, final int n2, final String s) {
                MediaSessionImplBase.this.setVolumeTo(n, n2);
            }
            
            public void skipToQueueItem(final long n) {
                MediaSessionImplBase.this.postToHandler(4, n);
            }
            
            public void stop() throws RemoteException {
                MediaSessionImplBase.this.postToHandler(6);
            }
            
            public void unregisterCallbackListener(final IMediaControllerCallback mediaControllerCallback) {
                MediaSessionImplBase.this.mControllerCallbacks.unregister((IInterface)mediaControllerCallback);
            }
        }
        
        private class MessageHandler extends Handler
        {
            private static final int KEYCODE_MEDIA_PAUSE = 127;
            private static final int KEYCODE_MEDIA_PLAY = 126;
            private static final int MSG_ADJUST_VOLUME = 16;
            private static final int MSG_COMMAND = 15;
            private static final int MSG_CUSTOM_ACTION = 13;
            private static final int MSG_FAST_FORWARD = 9;
            private static final int MSG_MEDIA_BUTTON = 14;
            private static final int MSG_NEXT = 7;
            private static final int MSG_PAUSE = 5;
            private static final int MSG_PLAY = 1;
            private static final int MSG_PLAY_MEDIA_ID = 2;
            private static final int MSG_PLAY_SEARCH = 3;
            private static final int MSG_PLAY_URI = 18;
            private static final int MSG_PREVIOUS = 8;
            private static final int MSG_RATE = 12;
            private static final int MSG_REWIND = 10;
            private static final int MSG_SEEK_TO = 11;
            private static final int MSG_SET_VOLUME = 17;
            private static final int MSG_SKIP_TO_ITEM = 4;
            private static final int MSG_STOP = 6;
            
            public MessageHandler(final Looper looper) {
                super(looper);
            }
            
            private void onMediaButtonEvent(final KeyEvent keyEvent, final Callback callback) {
                if (keyEvent != null) {
                    if (keyEvent.getAction() != 0) {
                        return;
                    }
                    long actions;
                    if (MediaSessionImplBase.this.mState == null) {
                        actions = 0L;
                    }
                    else {
                        actions = MediaSessionImplBase.this.mState.getActions();
                    }
                    final int keyCode = keyEvent.getKeyCode();
                    if (keyCode != 79) {
                        switch (keyCode) {
                            default: {
                                switch (keyCode) {
                                    default: {
                                        return;
                                    }
                                    case 127: {
                                        if ((actions & 0x2L) != 0x0L) {
                                            callback.onPause();
                                        }
                                        return;
                                    }
                                    case 126: {
                                        if ((actions & 0x4L) != 0x0L) {
                                            callback.onPlay();
                                        }
                                        return;
                                    }
                                }
                                break;
                            }
                            case 90: {
                                if ((actions & 0x40L) != 0x0L) {
                                    callback.onFastForward();
                                }
                                return;
                            }
                            case 89: {
                                if ((actions & 0x8L) != 0x0L) {
                                    callback.onRewind();
                                }
                                return;
                            }
                            case 88: {
                                if ((actions & 0x10L) != 0x0L) {
                                    callback.onSkipToPrevious();
                                }
                                return;
                            }
                            case 87: {
                                if ((actions & 0x20L) != 0x0L) {
                                    callback.onSkipToNext();
                                }
                                return;
                            }
                            case 86: {
                                if ((actions & 0x1L) != 0x0L) {
                                    callback.onStop();
                                }
                                return;
                            }
                            case 85: {
                                break;
                            }
                        }
                    }
                    final PlaybackStateCompat access$2800 = MediaSessionImplBase.this.mState;
                    boolean b = false;
                    final boolean b2 = access$2800 != null && MediaSessionImplBase.this.mState.getState() == 3;
                    final boolean b3 = (actions & 0x204L) != 0x0L;
                    if ((actions & 0x202L) != 0x0L) {
                        b = true;
                    }
                    if (b2 && b) {
                        callback.onPause();
                        return;
                    }
                    if (!b2 && b3) {
                        callback.onPlay();
                    }
                }
            }
            
            public void handleMessage(final Message message) {
                final Callback access$2700 = MediaSessionImplBase.this.mCallback;
                if (access$2700 == null) {
                    return;
                }
                switch (message.what) {
                    default: {}
                    case 18: {
                        access$2700.onPlayFromUri((Uri)message.obj, message.getData());
                    }
                    case 17: {
                        MediaSessionImplBase.this.setVolumeTo((int)message.obj, 0);
                    }
                    case 16: {
                        MediaSessionImplBase.this.adjustVolume((int)message.obj, 0);
                    }
                    case 15: {
                        final Command command = (Command)message.obj;
                        access$2700.onCommand(command.command, command.extras, command.stub);
                    }
                    case 14: {
                        final KeyEvent keyEvent = (KeyEvent)message.obj;
                        final Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
                        intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
                        if (!access$2700.onMediaButtonEvent(intent)) {
                            this.onMediaButtonEvent(keyEvent, access$2700);
                        }
                    }
                    case 13: {
                        access$2700.onCustomAction((String)message.obj, message.getData());
                    }
                    case 12: {
                        access$2700.onSetRating((RatingCompat)message.obj);
                    }
                    case 11: {
                        access$2700.onSeekTo((long)message.obj);
                    }
                    case 10: {
                        access$2700.onRewind();
                    }
                    case 9: {
                        access$2700.onFastForward();
                    }
                    case 8: {
                        access$2700.onSkipToPrevious();
                    }
                    case 7: {
                        access$2700.onSkipToNext();
                    }
                    case 6: {
                        access$2700.onStop();
                    }
                    case 5: {
                        access$2700.onPause();
                    }
                    case 4: {
                        access$2700.onSkipToQueueItem((long)message.obj);
                    }
                    case 3: {
                        access$2700.onPlayFromSearch((String)message.obj, message.getData());
                    }
                    case 2: {
                        access$2700.onPlayFromMediaId((String)message.obj, message.getData());
                    }
                    case 1: {
                        access$2700.onPlay();
                    }
                }
            }
            
            public void post(final int n) {
                this.post(n, null);
            }
            
            public void post(final int n, final Object o) {
                this.obtainMessage(n, o).sendToTarget();
            }
            
            public void post(final int n, final Object o, final int n2) {
                this.obtainMessage(n, n2, 0, o).sendToTarget();
            }
            
            public void post(final int n, final Object o, final Bundle data) {
                final Message obtainMessage = this.obtainMessage(n, o);
                obtainMessage.setData(data);
                obtainMessage.sendToTarget();
            }
        }
    }
    
    public interface OnActiveChangeListener
    {
        void onActiveChanged();
    }
    
    public static final class QueueItem implements Parcelable
    {
        public static final Parcelable$Creator<QueueItem> CREATOR;
        public static final int UNKNOWN_ID = -1;
        private final MediaDescriptionCompat mDescription;
        private final long mId;
        private Object mItem;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<QueueItem>() {
                public QueueItem createFromParcel(final Parcel parcel) {
                    return new QueueItem(parcel);
                }
                
                public QueueItem[] newArray(final int n) {
                    return new QueueItem[n];
                }
            };
        }
        
        private QueueItem(final Parcel parcel) {
            this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.mId = parcel.readLong();
        }
        
        public QueueItem(final MediaDescriptionCompat mediaDescriptionCompat, final long n) {
            this(null, mediaDescriptionCompat, n);
        }
        
        private QueueItem(final Object mItem, final MediaDescriptionCompat mDescription, final long mId) {
            if (mDescription == null) {
                throw new IllegalArgumentException("Description cannot be null.");
            }
            if (mId == -1L) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.mDescription = mDescription;
            this.mId = mId;
            this.mItem = mItem;
        }
        
        public static QueueItem obtain(final Object o) {
            return new QueueItem(o, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(o)), MediaSessionCompatApi21.QueueItem.getQueueId(o));
        }
        
        public int describeContents() {
            return 0;
        }
        
        public MediaDescriptionCompat getDescription() {
            return this.mDescription;
        }
        
        public long getQueueId() {
            return this.mId;
        }
        
        public Object getQueueItem() {
            if (this.mItem == null && Build$VERSION.SDK_INT >= 21) {
                return this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
            }
            return this.mItem;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("MediaSession.QueueItem {Description=");
            sb.append(this.mDescription);
            sb.append(", Id=");
            sb.append(this.mId);
            sb.append(" }");
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mDescription.writeToParcel(parcel, n);
            parcel.writeLong(this.mId);
        }
    }
    
    static final class ResultReceiverWrapper implements Parcelable
    {
        public static final Parcelable$Creator<ResultReceiverWrapper> CREATOR;
        private ResultReceiver mResultReceiver;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<ResultReceiverWrapper>() {
                public ResultReceiverWrapper createFromParcel(final Parcel parcel) {
                    return new ResultReceiverWrapper(parcel);
                }
                
                public ResultReceiverWrapper[] newArray(final int n) {
                    return new ResultReceiverWrapper[n];
                }
            };
        }
        
        ResultReceiverWrapper(final Parcel parcel) {
            this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(parcel);
        }
        
        public ResultReceiverWrapper(final ResultReceiver mResultReceiver) {
            this.mResultReceiver = mResultReceiver;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            this.mResultReceiver.writeToParcel(parcel, n);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface SessionFlags {
    }
    
    public static final class Token implements Parcelable
    {
        public static final Parcelable$Creator<Token> CREATOR;
        private final Object mInner;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<Token>() {
                public Token createFromParcel(final Parcel parcel) {
                    Object o;
                    if (Build$VERSION.SDK_INT >= 21) {
                        o = parcel.readParcelable((ClassLoader)null);
                    }
                    else {
                        o = parcel.readStrongBinder();
                    }
                    return new Token(o);
                }
                
                public Token[] newArray(final int n) {
                    return new Token[n];
                }
            };
        }
        
        Token(final Object mInner) {
            this.mInner = mInner;
        }
        
        public static Token fromToken(final Object o) {
            if (o != null && Build$VERSION.SDK_INT >= 21) {
                return new Token(MediaSessionCompatApi21.verifyToken(o));
            }
            return null;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public Object getToken() {
            return this.mInner;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            if (Build$VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable)this.mInner, n);
                return;
            }
            parcel.writeStrongBinder((IBinder)this.mInner);
        }
    }
}
