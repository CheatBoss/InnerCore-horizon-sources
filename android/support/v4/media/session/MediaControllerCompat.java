package android.support.v4.media.session;

import android.content.*;
import android.view.*;
import android.app.*;
import android.text.*;
import java.util.*;
import android.util.*;
import android.net.*;
import android.support.v4.media.*;
import android.os.*;

public final class MediaControllerCompat
{
    private static final String TAG = "MediaControllerCompat";
    private final MediaControllerImpl mImpl;
    private final MediaSessionCompat.Token mToken;
    
    public MediaControllerCompat(final Context context, final MediaSessionCompat.Token mToken) throws RemoteException {
        if (mToken == null) {
            throw new IllegalArgumentException("sessionToken must not be null");
        }
        this.mToken = mToken;
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi23(context, mToken);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi21(context, mToken);
            return;
        }
        this.mImpl = (MediaControllerImpl)new MediaControllerImplBase(this.mToken);
    }
    
    public MediaControllerCompat(final Context context, final MediaSessionCompat mediaSessionCompat) {
        if (mediaSessionCompat == null) {
            throw new IllegalArgumentException("session must not be null");
        }
        this.mToken = mediaSessionCompat.getSessionToken();
        if (Build$VERSION.SDK_INT >= 23) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi23(context, mediaSessionCompat);
            return;
        }
        if (Build$VERSION.SDK_INT >= 21) {
            this.mImpl = (MediaControllerImpl)new MediaControllerImplApi21(context, mediaSessionCompat);
            return;
        }
        this.mImpl = (MediaControllerImpl)new MediaControllerImplBase(this.mToken);
    }
    
    public void adjustVolume(final int n, final int n2) {
        this.mImpl.adjustVolume(n, n2);
    }
    
    public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
        if (keyEvent == null) {
            throw new IllegalArgumentException("KeyEvent may not be null");
        }
        return this.mImpl.dispatchMediaButtonEvent(keyEvent);
    }
    
    public Bundle getExtras() {
        return this.mImpl.getExtras();
    }
    
    public long getFlags() {
        return this.mImpl.getFlags();
    }
    
    public Object getMediaController() {
        return this.mImpl.getMediaController();
    }
    
    public MediaMetadataCompat getMetadata() {
        return this.mImpl.getMetadata();
    }
    
    public String getPackageName() {
        return this.mImpl.getPackageName();
    }
    
    public PlaybackInfo getPlaybackInfo() {
        return this.mImpl.getPlaybackInfo();
    }
    
    public PlaybackStateCompat getPlaybackState() {
        return this.mImpl.getPlaybackState();
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
        return this.mImpl.getQueue();
    }
    
    public CharSequence getQueueTitle() {
        return this.mImpl.getQueueTitle();
    }
    
    public int getRatingType() {
        return this.mImpl.getRatingType();
    }
    
    public PendingIntent getSessionActivity() {
        return this.mImpl.getSessionActivity();
    }
    
    public MediaSessionCompat.Token getSessionToken() {
        return this.mToken;
    }
    
    public TransportControls getTransportControls() {
        return this.mImpl.getTransportControls();
    }
    
    public void registerCallback(final Callback callback) {
        this.registerCallback(callback, null);
    }
    
    public void registerCallback(final Callback callback, final Handler handler) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        Handler handler2;
        if ((handler2 = handler) == null) {
            handler2 = new Handler();
        }
        this.mImpl.registerCallback(callback, handler2);
    }
    
    public void sendCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            throw new IllegalArgumentException("command cannot be null or empty");
        }
        this.mImpl.sendCommand(s, bundle, resultReceiver);
    }
    
    public void setVolumeTo(final int n, final int n2) {
        this.mImpl.setVolumeTo(n, n2);
    }
    
    public void unregisterCallback(final Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("callback cannot be null");
        }
        this.mImpl.unregisterCallback(callback);
    }
    
    public abstract static class Callback implements IBinder$DeathRecipient
    {
        private final Object mCallbackObj;
        private MessageHandler mHandler;
        private boolean mRegistered;
        
        public Callback() {
            this.mRegistered = false;
            Object callback;
            if (Build$VERSION.SDK_INT >= 21) {
                callback = MediaControllerCompatApi21.createCallback((MediaControllerCompatApi21.Callback)new StubApi21());
            }
            else {
                callback = new StubCompat();
            }
            this.mCallbackObj = callback;
        }
        
        private void setHandler(final Handler handler) {
            this.mHandler = new MessageHandler(this, handler.getLooper());
        }
        
        public void binderDied() {
            this.onSessionDestroyed();
        }
        
        public void onAudioInfoChanged(final PlaybackInfo playbackInfo) {
        }
        
        public void onExtrasChanged(final Bundle bundle) {
        }
        
        public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) {
        }
        
        public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) {
        }
        
        public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) {
        }
        
        public void onQueueTitleChanged(final CharSequence charSequence) {
        }
        
        public void onSessionDestroyed() {
        }
        
        public void onSessionEvent(final String s, final Bundle bundle) {
        }
        
        private class StubApi21 implements MediaControllerCompatApi21.Callback
        {
            @Override
            public void onMetadataChanged(final Object o) {
                MediaControllerCompat.Callback.this.onMetadataChanged(MediaMetadataCompat.fromMediaMetadata(o));
            }
            
            @Override
            public void onPlaybackStateChanged(final Object o) {
                MediaControllerCompat.Callback.this.onPlaybackStateChanged(PlaybackStateCompat.fromPlaybackState(o));
            }
            
            @Override
            public void onSessionDestroyed() {
                MediaControllerCompat.Callback.this.onSessionDestroyed();
            }
            
            @Override
            public void onSessionEvent(final String s, final Bundle bundle) {
                MediaControllerCompat.Callback.this.onSessionEvent(s, bundle);
            }
        }
        
        private class StubCompat extends Stub
        {
            public void onEvent(final String s, final Bundle bundle) throws RemoteException {
                Callback.this.mHandler.post(1, s, bundle);
            }
            
            public void onExtrasChanged(final Bundle bundle) throws RemoteException {
                Callback.this.mHandler.post(7, bundle, null);
            }
            
            public void onMetadataChanged(final MediaMetadataCompat mediaMetadataCompat) throws RemoteException {
                Callback.this.mHandler.post(3, mediaMetadataCompat, null);
            }
            
            public void onPlaybackStateChanged(final PlaybackStateCompat playbackStateCompat) throws RemoteException {
                Callback.this.mHandler.post(2, playbackStateCompat, null);
            }
            
            public void onQueueChanged(final List<MediaSessionCompat.QueueItem> list) throws RemoteException {
                Callback.this.mHandler.post(5, list, null);
            }
            
            public void onQueueTitleChanged(final CharSequence charSequence) throws RemoteException {
                Callback.this.mHandler.post(6, charSequence, null);
            }
            
            public void onSessionDestroyed() throws RemoteException {
                Callback.this.mHandler.post(8, null, null);
            }
            
            public void onVolumeInfoChanged(final ParcelableVolumeInfo parcelableVolumeInfo) throws RemoteException {
                PlaybackInfo playbackInfo;
                if (parcelableVolumeInfo != null) {
                    playbackInfo = new PlaybackInfo(parcelableVolumeInfo.volumeType, parcelableVolumeInfo.audioStream, parcelableVolumeInfo.controlType, parcelableVolumeInfo.maxVolume, parcelableVolumeInfo.currentVolume);
                }
                else {
                    playbackInfo = null;
                }
                Callback.this.mHandler.post(4, playbackInfo, null);
            }
        }
    }
    
    private class MessageHandler extends Handler
    {
        private static final int MSG_DESTROYED = 8;
        private static final int MSG_EVENT = 1;
        private static final int MSG_UPDATE_EXTRAS = 7;
        private static final int MSG_UPDATE_METADATA = 3;
        private static final int MSG_UPDATE_PLAYBACK_STATE = 2;
        private static final int MSG_UPDATE_QUEUE = 5;
        private static final int MSG_UPDATE_QUEUE_TITLE = 6;
        private static final int MSG_UPDATE_VOLUME = 4;
        final /* synthetic */ Callback this$0;
        
        public MessageHandler(final Callback this$0, final Looper looper) {
            this.this$0 = this$0;
            super(looper);
        }
        
        public void handleMessage(final Message message) {
            if (!this.this$0.mRegistered) {
                return;
            }
            switch (message.what) {
                default: {}
                case 8: {
                    this.this$0.onSessionDestroyed();
                }
                case 7: {
                    this.this$0.onExtrasChanged((Bundle)message.obj);
                }
                case 6: {
                    this.this$0.onQueueTitleChanged((CharSequence)message.obj);
                }
                case 5: {
                    this.this$0.onQueueChanged((List<MediaSessionCompat.QueueItem>)message.obj);
                }
                case 4: {
                    this.this$0.onAudioInfoChanged((PlaybackInfo)message.obj);
                }
                case 3: {
                    this.this$0.onMetadataChanged((MediaMetadataCompat)message.obj);
                }
                case 2: {
                    this.this$0.onPlaybackStateChanged((PlaybackStateCompat)message.obj);
                }
                case 1: {
                    this.this$0.onSessionEvent((String)message.obj, message.getData());
                }
            }
        }
        
        public void post(final int n, final Object o, final Bundle data) {
            final Message obtainMessage = this.obtainMessage(n, o);
            obtainMessage.setData(data);
            obtainMessage.sendToTarget();
        }
    }
    
    interface MediaControllerImpl
    {
        void adjustVolume(final int p0, final int p1);
        
        boolean dispatchMediaButtonEvent(final KeyEvent p0);
        
        Bundle getExtras();
        
        long getFlags();
        
        Object getMediaController();
        
        MediaMetadataCompat getMetadata();
        
        String getPackageName();
        
        PlaybackInfo getPlaybackInfo();
        
        PlaybackStateCompat getPlaybackState();
        
        List<MediaSessionCompat.QueueItem> getQueue();
        
        CharSequence getQueueTitle();
        
        int getRatingType();
        
        PendingIntent getSessionActivity();
        
        TransportControls getTransportControls();
        
        void registerCallback(final Callback p0, final Handler p1);
        
        void sendCommand(final String p0, final Bundle p1, final ResultReceiver p2);
        
        void setVolumeTo(final int p0, final int p1);
        
        void unregisterCallback(final Callback p0);
    }
    
    static class MediaControllerImplApi21 implements MediaControllerImpl
    {
        protected final Object mControllerObj;
        
        public MediaControllerImplApi21(final Context context, final MediaSessionCompat.Token token) throws RemoteException {
            this.mControllerObj = MediaControllerCompatApi21.fromToken(context, token.getToken());
            if (this.mControllerObj == null) {
                throw new RemoteException();
            }
        }
        
        public MediaControllerImplApi21(final Context context, final MediaSessionCompat mediaSessionCompat) {
            this.mControllerObj = MediaControllerCompatApi21.fromToken(context, mediaSessionCompat.getSessionToken().getToken());
        }
        
        @Override
        public void adjustVolume(final int n, final int n2) {
            MediaControllerCompatApi21.adjustVolume(this.mControllerObj, n, n2);
        }
        
        @Override
        public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
            return MediaControllerCompatApi21.dispatchMediaButtonEvent(this.mControllerObj, keyEvent);
        }
        
        @Override
        public Bundle getExtras() {
            return MediaControllerCompatApi21.getExtras(this.mControllerObj);
        }
        
        @Override
        public long getFlags() {
            return MediaControllerCompatApi21.getFlags(this.mControllerObj);
        }
        
        @Override
        public Object getMediaController() {
            return this.mControllerObj;
        }
        
        @Override
        public MediaMetadataCompat getMetadata() {
            final Object metadata = MediaControllerCompatApi21.getMetadata(this.mControllerObj);
            if (metadata != null) {
                return MediaMetadataCompat.fromMediaMetadata(metadata);
            }
            return null;
        }
        
        @Override
        public String getPackageName() {
            return MediaControllerCompatApi21.getPackageName(this.mControllerObj);
        }
        
        @Override
        public PlaybackInfo getPlaybackInfo() {
            final Object playbackInfo = MediaControllerCompatApi21.getPlaybackInfo(this.mControllerObj);
            if (playbackInfo != null) {
                return new PlaybackInfo(MediaControllerCompatApi21.PlaybackInfo.getPlaybackType(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getLegacyAudioStream(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getVolumeControl(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getMaxVolume(playbackInfo), MediaControllerCompatApi21.PlaybackInfo.getCurrentVolume(playbackInfo));
            }
            return null;
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            final Object playbackState = MediaControllerCompatApi21.getPlaybackState(this.mControllerObj);
            if (playbackState != null) {
                return PlaybackStateCompat.fromPlaybackState(playbackState);
            }
            return null;
        }
        
        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            final List<Object> queue = MediaControllerCompatApi21.getQueue(this.mControllerObj);
            if (queue == null) {
                return null;
            }
            final ArrayList<MediaSessionCompat.QueueItem> list = new ArrayList<MediaSessionCompat.QueueItem>();
            final Iterator<Object> iterator = queue.iterator();
            while (iterator.hasNext()) {
                list.add(MediaSessionCompat.QueueItem.obtain(iterator.next()));
            }
            return list;
        }
        
        @Override
        public CharSequence getQueueTitle() {
            return MediaControllerCompatApi21.getQueueTitle(this.mControllerObj);
        }
        
        @Override
        public int getRatingType() {
            return MediaControllerCompatApi21.getRatingType(this.mControllerObj);
        }
        
        @Override
        public PendingIntent getSessionActivity() {
            return MediaControllerCompatApi21.getSessionActivity(this.mControllerObj);
        }
        
        @Override
        public TransportControls getTransportControls() {
            final Object transportControls = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (transportControls != null) {
                return new TransportControlsApi21(transportControls);
            }
            return null;
        }
        
        @Override
        public void registerCallback(final Callback callback, final Handler handler) {
            MediaControllerCompatApi21.registerCallback(this.mControllerObj, callback.mCallbackObj, handler);
        }
        
        @Override
        public void sendCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
            MediaControllerCompatApi21.sendCommand(this.mControllerObj, s, bundle, resultReceiver);
        }
        
        @Override
        public void setVolumeTo(final int n, final int n2) {
            MediaControllerCompatApi21.setVolumeTo(this.mControllerObj, n, n2);
        }
        
        @Override
        public void unregisterCallback(final Callback callback) {
            MediaControllerCompatApi21.unregisterCallback(this.mControllerObj, callback.mCallbackObj);
        }
    }
    
    static class MediaControllerImplApi23 extends MediaControllerImplApi21
    {
        public MediaControllerImplApi23(final Context context, final MediaSessionCompat.Token token) throws RemoteException {
            super(context, token);
        }
        
        public MediaControllerImplApi23(final Context context, final MediaSessionCompat mediaSessionCompat) {
            super(context, mediaSessionCompat);
        }
        
        @Override
        public TransportControls getTransportControls() {
            final Object transportControls = MediaControllerCompatApi21.getTransportControls(this.mControllerObj);
            if (transportControls != null) {
                return new TransportControlsApi23(transportControls);
            }
            return null;
        }
    }
    
    static class MediaControllerImplBase implements MediaControllerImpl
    {
        private IMediaSession mBinder;
        private MediaSessionCompat.Token mToken;
        private TransportControls mTransportControls;
        
        public MediaControllerImplBase(final MediaSessionCompat.Token mToken) {
            this.mToken = mToken;
            this.mBinder = IMediaSession.Stub.asInterface((IBinder)mToken.getToken());
        }
        
        @Override
        public void adjustVolume(final int n, final int n2) {
            try {
                this.mBinder.adjustVolume(n, n2, null);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in adjustVolume. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public boolean dispatchMediaButtonEvent(final KeyEvent keyEvent) {
            if (keyEvent == null) {
                throw new IllegalArgumentException("event may not be null.");
            }
            try {
                this.mBinder.sendMediaButton(keyEvent);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in dispatchMediaButtonEvent. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
            return false;
        }
        
        @Override
        public Bundle getExtras() {
            try {
                return this.mBinder.getExtras();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getExtras. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public long getFlags() {
            try {
                return this.mBinder.getFlags();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getFlags. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return 0L;
            }
        }
        
        @Override
        public Object getMediaController() {
            return null;
        }
        
        @Override
        public MediaMetadataCompat getMetadata() {
            try {
                return this.mBinder.getMetadata();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getMetadata. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public String getPackageName() {
            try {
                return this.mBinder.getPackageName();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getPackageName. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public PlaybackInfo getPlaybackInfo() {
            try {
                final ParcelableVolumeInfo volumeAttributes = this.mBinder.getVolumeAttributes();
                return new PlaybackInfo(volumeAttributes.volumeType, volumeAttributes.audioStream, volumeAttributes.controlType, volumeAttributes.maxVolume, volumeAttributes.currentVolume);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getPlaybackInfo. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public PlaybackStateCompat getPlaybackState() {
            try {
                return this.mBinder.getPlaybackState();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getPlaybackState. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public List<MediaSessionCompat.QueueItem> getQueue() {
            try {
                return this.mBinder.getQueue();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getQueue. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public CharSequence getQueueTitle() {
            try {
                return this.mBinder.getQueueTitle();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getQueueTitle. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public int getRatingType() {
            try {
                return this.mBinder.getRatingType();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getRatingType. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return 0;
            }
        }
        
        @Override
        public PendingIntent getSessionActivity() {
            try {
                return this.mBinder.getLaunchPendingIntent();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in getSessionActivity. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                return null;
            }
        }
        
        @Override
        public TransportControls getTransportControls() {
            if (this.mTransportControls == null) {
                this.mTransportControls = new TransportControlsBase(this.mBinder);
            }
            return this.mTransportControls;
        }
        
        @Override
        public void registerCallback(final Callback callback, final Handler handler) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.asBinder().linkToDeath((IBinder$DeathRecipient)callback, 0);
                this.mBinder.registerCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                callback.setHandler(handler);
                callback.mRegistered = true;
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in registerCallback. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
                callback.onSessionDestroyed();
            }
        }
        
        @Override
        public void sendCommand(final String s, final Bundle bundle, final ResultReceiver resultReceiver) {
            try {
                this.mBinder.sendCommand(s, bundle, new MediaSessionCompat.ResultReceiverWrapper(resultReceiver));
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in sendCommand. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void setVolumeTo(final int n, final int n2) {
            try {
                this.mBinder.setVolumeTo(n, n2, null);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in setVolumeTo. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void unregisterCallback(final Callback callback) {
            if (callback == null) {
                throw new IllegalArgumentException("callback may not be null.");
            }
            try {
                this.mBinder.unregisterCallbackListener((IMediaControllerCallback)callback.mCallbackObj);
                this.mBinder.asBinder().unlinkToDeath((IBinder$DeathRecipient)callback, 0);
                callback.mRegistered = false;
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in unregisterCallback. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
    }
    
    public static final class PlaybackInfo
    {
        public static final int PLAYBACK_TYPE_LOCAL = 1;
        public static final int PLAYBACK_TYPE_REMOTE = 2;
        private final int mAudioStream;
        private final int mCurrentVolume;
        private final int mMaxVolume;
        private final int mPlaybackType;
        private final int mVolumeControl;
        
        PlaybackInfo(final int mPlaybackType, final int mAudioStream, final int mVolumeControl, final int mMaxVolume, final int mCurrentVolume) {
            this.mPlaybackType = mPlaybackType;
            this.mAudioStream = mAudioStream;
            this.mVolumeControl = mVolumeControl;
            this.mMaxVolume = mMaxVolume;
            this.mCurrentVolume = mCurrentVolume;
        }
        
        public int getAudioStream() {
            return this.mAudioStream;
        }
        
        public int getCurrentVolume() {
            return this.mCurrentVolume;
        }
        
        public int getMaxVolume() {
            return this.mMaxVolume;
        }
        
        public int getPlaybackType() {
            return this.mPlaybackType;
        }
        
        public int getVolumeControl() {
            return this.mVolumeControl;
        }
    }
    
    public abstract static class TransportControls
    {
        TransportControls() {
        }
        
        public abstract void fastForward();
        
        public abstract void pause();
        
        public abstract void play();
        
        public abstract void playFromMediaId(final String p0, final Bundle p1);
        
        public abstract void playFromSearch(final String p0, final Bundle p1);
        
        public abstract void playFromUri(final Uri p0, final Bundle p1);
        
        public abstract void rewind();
        
        public abstract void seekTo(final long p0);
        
        public abstract void sendCustomAction(final PlaybackStateCompat.CustomAction p0, final Bundle p1);
        
        public abstract void sendCustomAction(final String p0, final Bundle p1);
        
        public abstract void setRating(final RatingCompat p0);
        
        public abstract void skipToNext();
        
        public abstract void skipToPrevious();
        
        public abstract void skipToQueueItem(final long p0);
        
        public abstract void stop();
    }
    
    static class TransportControlsApi21 extends TransportControls
    {
        protected final Object mControlsObj;
        
        public TransportControlsApi21(final Object mControlsObj) {
            this.mControlsObj = mControlsObj;
        }
        
        @Override
        public void fastForward() {
            MediaControllerCompatApi21.TransportControls.fastForward(this.mControlsObj);
        }
        
        @Override
        public void pause() {
            MediaControllerCompatApi21.TransportControls.pause(this.mControlsObj);
        }
        
        @Override
        public void play() {
            MediaControllerCompatApi21.TransportControls.play(this.mControlsObj);
        }
        
        @Override
        public void playFromMediaId(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromMediaId(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void playFromSearch(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.playFromSearch(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            if (uri != null && !Uri.EMPTY.equals((Object)uri)) {
                final Bundle bundle2 = new Bundle();
                bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_URI", (Parcelable)uri);
                bundle2.putParcelable("android.support.v4.media.session.action.ARGUMENT_EXTRAS", (Parcelable)bundle);
                this.sendCustomAction("android.support.v4.media.session.action.PLAY_FROM_URI", bundle2);
                return;
            }
            throw new IllegalArgumentException("You must specify a non-empty Uri for playFromUri.");
        }
        
        @Override
        public void rewind() {
            MediaControllerCompatApi21.TransportControls.rewind(this.mControlsObj);
        }
        
        @Override
        public void seekTo(final long n) {
            MediaControllerCompatApi21.TransportControls.seekTo(this.mControlsObj, n);
        }
        
        @Override
        public void sendCustomAction(final PlaybackStateCompat.CustomAction customAction, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, customAction.getAction(), bundle);
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle) {
            MediaControllerCompatApi21.TransportControls.sendCustomAction(this.mControlsObj, s, bundle);
        }
        
        @Override
        public void setRating(final RatingCompat ratingCompat) {
            final Object mControlsObj = this.mControlsObj;
            Object rating;
            if (ratingCompat != null) {
                rating = ratingCompat.getRating();
            }
            else {
                rating = null;
            }
            MediaControllerCompatApi21.TransportControls.setRating(mControlsObj, rating);
        }
        
        @Override
        public void skipToNext() {
            MediaControllerCompatApi21.TransportControls.skipToNext(this.mControlsObj);
        }
        
        @Override
        public void skipToPrevious() {
            MediaControllerCompatApi21.TransportControls.skipToPrevious(this.mControlsObj);
        }
        
        @Override
        public void skipToQueueItem(final long n) {
            MediaControllerCompatApi21.TransportControls.skipToQueueItem(this.mControlsObj, n);
        }
        
        @Override
        public void stop() {
            MediaControllerCompatApi21.TransportControls.stop(this.mControlsObj);
        }
    }
    
    static class TransportControlsApi23 extends TransportControlsApi21
    {
        public TransportControlsApi23(final Object o) {
            super(o);
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            MediaControllerCompatApi23.TransportControls.playFromUri(this.mControlsObj, uri, bundle);
        }
    }
    
    static class TransportControlsBase extends TransportControls
    {
        private IMediaSession mBinder;
        
        public TransportControlsBase(final IMediaSession mBinder) {
            this.mBinder = mBinder;
        }
        
        @Override
        public void fastForward() {
            try {
                this.mBinder.fastForward();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in fastForward. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void pause() {
            try {
                this.mBinder.pause();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in pause. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void play() {
            try {
                this.mBinder.play();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in play. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void playFromMediaId(final String s, final Bundle bundle) {
            try {
                this.mBinder.playFromMediaId(s, bundle);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in playFromMediaId. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void playFromSearch(final String s, final Bundle bundle) {
            try {
                this.mBinder.playFromSearch(s, bundle);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in playFromSearch. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void playFromUri(final Uri uri, final Bundle bundle) {
            try {
                this.mBinder.playFromUri(uri, bundle);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in playFromUri. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void rewind() {
            try {
                this.mBinder.rewind();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in rewind. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void seekTo(final long n) {
            try {
                this.mBinder.seekTo(n);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in seekTo. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void sendCustomAction(final PlaybackStateCompat.CustomAction customAction, final Bundle bundle) {
            this.sendCustomAction(customAction.getAction(), bundle);
        }
        
        @Override
        public void sendCustomAction(final String s, final Bundle bundle) {
            try {
                this.mBinder.sendCustomAction(s, bundle);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in sendCustomAction. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void setRating(final RatingCompat ratingCompat) {
            try {
                this.mBinder.rate(ratingCompat);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in setRating. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void skipToNext() {
            try {
                this.mBinder.next();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in skipToNext. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void skipToPrevious() {
            try {
                this.mBinder.previous();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in skipToPrevious. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void skipToQueueItem(final long n) {
            try {
                this.mBinder.skipToQueueItem(n);
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in skipToQueueItem. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
        
        @Override
        public void stop() {
            try {
                this.mBinder.stop();
            }
            catch (RemoteException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Dead object in stop. ");
                sb.append(ex);
                Log.e("MediaControllerCompat", sb.toString());
            }
        }
    }
}
