package android.support.v4.media.session;

import android.text.*;
import java.util.*;
import android.support.annotation.*;
import java.lang.annotation.*;
import android.os.*;

public final class PlaybackStateCompat implements Parcelable
{
    public static final long ACTION_FAST_FORWARD = 64L;
    public static final long ACTION_PAUSE = 2L;
    public static final long ACTION_PLAY = 4L;
    public static final long ACTION_PLAY_FROM_MEDIA_ID = 1024L;
    public static final long ACTION_PLAY_FROM_SEARCH = 2048L;
    public static final long ACTION_PLAY_FROM_URI = 8192L;
    public static final long ACTION_PLAY_PAUSE = 512L;
    public static final long ACTION_REWIND = 8L;
    public static final long ACTION_SEEK_TO = 256L;
    public static final long ACTION_SET_RATING = 128L;
    public static final long ACTION_SKIP_TO_NEXT = 32L;
    public static final long ACTION_SKIP_TO_PREVIOUS = 16L;
    public static final long ACTION_SKIP_TO_QUEUE_ITEM = 4096L;
    public static final long ACTION_STOP = 1L;
    public static final Parcelable$Creator<PlaybackStateCompat> CREATOR;
    public static final long PLAYBACK_POSITION_UNKNOWN = -1L;
    public static final int STATE_BUFFERING = 6;
    public static final int STATE_CONNECTING = 8;
    public static final int STATE_ERROR = 7;
    public static final int STATE_FAST_FORWARDING = 4;
    public static final int STATE_NONE = 0;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_REWINDING = 5;
    public static final int STATE_SKIPPING_TO_NEXT = 10;
    public static final int STATE_SKIPPING_TO_PREVIOUS = 9;
    public static final int STATE_SKIPPING_TO_QUEUE_ITEM = 11;
    public static final int STATE_STOPPED = 1;
    private final long mActions;
    private final long mActiveItemId;
    private final long mBufferedPosition;
    private List<CustomAction> mCustomActions;
    private final CharSequence mErrorMessage;
    private final Bundle mExtras;
    private final long mPosition;
    private final float mSpeed;
    private final int mState;
    private Object mStateObj;
    private final long mUpdateTime;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<PlaybackStateCompat>() {
            public PlaybackStateCompat createFromParcel(final Parcel parcel) {
                return new PlaybackStateCompat(parcel, null);
            }
            
            public PlaybackStateCompat[] newArray(final int n) {
                return new PlaybackStateCompat[n];
            }
        };
    }
    
    private PlaybackStateCompat(final int mState, final long mPosition, final long mBufferedPosition, final float mSpeed, final long mActions, final CharSequence mErrorMessage, final long mUpdateTime, final List<CustomAction> list, final long mActiveItemId, final Bundle mExtras) {
        this.mState = mState;
        this.mPosition = mPosition;
        this.mBufferedPosition = mBufferedPosition;
        this.mSpeed = mSpeed;
        this.mActions = mActions;
        this.mErrorMessage = mErrorMessage;
        this.mUpdateTime = mUpdateTime;
        this.mCustomActions = new ArrayList<CustomAction>(list);
        this.mActiveItemId = mActiveItemId;
        this.mExtras = mExtras;
    }
    
    private PlaybackStateCompat(final Parcel parcel) {
        this.mState = parcel.readInt();
        this.mPosition = parcel.readLong();
        this.mSpeed = parcel.readFloat();
        this.mUpdateTime = parcel.readLong();
        this.mBufferedPosition = parcel.readLong();
        this.mActions = parcel.readLong();
        this.mErrorMessage = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.mCustomActions = (List<CustomAction>)parcel.createTypedArrayList((Parcelable$Creator)CustomAction.CREATOR);
        this.mActiveItemId = parcel.readLong();
        this.mExtras = parcel.readBundle();
    }
    
    public static PlaybackStateCompat fromPlaybackState(final Object mStateObj) {
        PlaybackStateCompat playbackStateCompat = null;
        Bundle extras = null;
        if (mStateObj != null) {
            if (Build$VERSION.SDK_INT < 21) {
                return null;
            }
            final List<Object> customActions = PlaybackStateCompatApi21.getCustomActions(mStateObj);
            ArrayList<CustomAction> list;
            if (customActions != null) {
                list = new ArrayList<CustomAction>(customActions.size());
                final Iterator<Object> iterator = customActions.iterator();
                while (iterator.hasNext()) {
                    list.add(CustomAction.fromCustomAction(iterator.next()));
                }
            }
            else {
                list = null;
            }
            if (Build$VERSION.SDK_INT >= 22) {
                extras = PlaybackStateCompatApi22.getExtras(mStateObj);
            }
            playbackStateCompat = new PlaybackStateCompat(PlaybackStateCompatApi21.getState(mStateObj), PlaybackStateCompatApi21.getPosition(mStateObj), PlaybackStateCompatApi21.getBufferedPosition(mStateObj), PlaybackStateCompatApi21.getPlaybackSpeed(mStateObj), PlaybackStateCompatApi21.getActions(mStateObj), PlaybackStateCompatApi21.getErrorMessage(mStateObj), PlaybackStateCompatApi21.getLastPositionUpdateTime(mStateObj), list, PlaybackStateCompatApi21.getActiveQueueItemId(mStateObj), extras);
            playbackStateCompat.mStateObj = mStateObj;
        }
        return playbackStateCompat;
    }
    
    public int describeContents() {
        return 0;
    }
    
    public long getActions() {
        return this.mActions;
    }
    
    public long getActiveQueueItemId() {
        return this.mActiveItemId;
    }
    
    public long getBufferedPosition() {
        return this.mBufferedPosition;
    }
    
    public List<CustomAction> getCustomActions() {
        return this.mCustomActions;
    }
    
    public CharSequence getErrorMessage() {
        return this.mErrorMessage;
    }
    
    @Nullable
    public Bundle getExtras() {
        return this.mExtras;
    }
    
    public long getLastPositionUpdateTime() {
        return this.mUpdateTime;
    }
    
    public float getPlaybackSpeed() {
        return this.mSpeed;
    }
    
    public Object getPlaybackState() {
        if (this.mStateObj == null && Build$VERSION.SDK_INT >= 21) {
            List<Object> list = null;
            if (this.mCustomActions != null) {
                final ArrayList<Object> list2 = new ArrayList<Object>(this.mCustomActions.size());
                final Iterator<CustomAction> iterator = this.mCustomActions.iterator();
                while (true) {
                    list = list2;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list2.add(iterator.next().getCustomAction());
                }
            }
            Object mStateObj;
            if (Build$VERSION.SDK_INT >= 22) {
                mStateObj = PlaybackStateCompatApi22.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, list, this.mActiveItemId, this.mExtras);
            }
            else {
                mStateObj = PlaybackStateCompatApi21.newInstance(this.mState, this.mPosition, this.mBufferedPosition, this.mSpeed, this.mActions, this.mErrorMessage, this.mUpdateTime, list, this.mActiveItemId);
            }
            return this.mStateObj = mStateObj;
        }
        return this.mStateObj;
    }
    
    public long getPosition() {
        return this.mPosition;
    }
    
    public int getState() {
        return this.mState;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PlaybackState {");
        sb.append("state=");
        sb.append(this.mState);
        sb.append(", position=");
        sb.append(this.mPosition);
        sb.append(", buffered position=");
        sb.append(this.mBufferedPosition);
        sb.append(", speed=");
        sb.append(this.mSpeed);
        sb.append(", updated=");
        sb.append(this.mUpdateTime);
        sb.append(", actions=");
        sb.append(this.mActions);
        sb.append(", error=");
        sb.append(this.mErrorMessage);
        sb.append(", custom actions=");
        sb.append(this.mCustomActions);
        sb.append(", active item id=");
        sb.append(this.mActiveItemId);
        sb.append("}");
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        parcel.writeInt(this.mState);
        parcel.writeLong(this.mPosition);
        parcel.writeFloat(this.mSpeed);
        parcel.writeLong(this.mUpdateTime);
        parcel.writeLong(this.mBufferedPosition);
        parcel.writeLong(this.mActions);
        TextUtils.writeToParcel(this.mErrorMessage, parcel, n);
        parcel.writeTypedList((List)this.mCustomActions);
        parcel.writeLong(this.mActiveItemId);
        parcel.writeBundle(this.mExtras);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface Actions {
    }
    
    public static final class Builder
    {
        private long mActions;
        private long mActiveItemId;
        private long mBufferedPosition;
        private final List<CustomAction> mCustomActions;
        private CharSequence mErrorMessage;
        private Bundle mExtras;
        private long mPosition;
        private float mRate;
        private int mState;
        private long mUpdateTime;
        
        public Builder() {
            this.mCustomActions = new ArrayList<CustomAction>();
            this.mActiveItemId = -1L;
        }
        
        public Builder(final PlaybackStateCompat playbackStateCompat) {
            this.mCustomActions = new ArrayList<CustomAction>();
            this.mActiveItemId = -1L;
            this.mState = playbackStateCompat.mState;
            this.mPosition = playbackStateCompat.mPosition;
            this.mRate = playbackStateCompat.mSpeed;
            this.mUpdateTime = playbackStateCompat.mUpdateTime;
            this.mBufferedPosition = playbackStateCompat.mBufferedPosition;
            this.mActions = playbackStateCompat.mActions;
            this.mErrorMessage = playbackStateCompat.mErrorMessage;
            if (playbackStateCompat.mCustomActions != null) {
                this.mCustomActions.addAll(playbackStateCompat.mCustomActions);
            }
            this.mActiveItemId = playbackStateCompat.mActiveItemId;
            this.mExtras = playbackStateCompat.mExtras;
        }
        
        public Builder addCustomAction(final CustomAction customAction) {
            if (customAction == null) {
                throw new IllegalArgumentException("You may not add a null CustomAction to PlaybackStateCompat.");
            }
            this.mCustomActions.add(customAction);
            return this;
        }
        
        public Builder addCustomAction(final String s, final String s2, final int n) {
            return this.addCustomAction(new CustomAction(s, (CharSequence)s2, n, (Bundle)null));
        }
        
        public PlaybackStateCompat build() {
            return new PlaybackStateCompat(this.mState, this.mPosition, this.mBufferedPosition, this.mRate, this.mActions, this.mErrorMessage, this.mUpdateTime, this.mCustomActions, this.mActiveItemId, this.mExtras, null);
        }
        
        public Builder setActions(final long mActions) {
            this.mActions = mActions;
            return this;
        }
        
        public Builder setActiveQueueItemId(final long mActiveItemId) {
            this.mActiveItemId = mActiveItemId;
            return this;
        }
        
        public Builder setBufferedPosition(final long mBufferedPosition) {
            this.mBufferedPosition = mBufferedPosition;
            return this;
        }
        
        public Builder setErrorMessage(final CharSequence mErrorMessage) {
            this.mErrorMessage = mErrorMessage;
            return this;
        }
        
        public Builder setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
            return this;
        }
        
        public Builder setState(final int n, final long n2, final float n3) {
            return this.setState(n, n2, n3, SystemClock.elapsedRealtime());
        }
        
        public Builder setState(final int mState, final long mPosition, final float mRate, final long mUpdateTime) {
            this.mState = mState;
            this.mPosition = mPosition;
            this.mUpdateTime = mUpdateTime;
            this.mRate = mRate;
            return this;
        }
    }
    
    public static final class CustomAction implements Parcelable
    {
        public static final Parcelable$Creator<CustomAction> CREATOR;
        private final String mAction;
        private Object mCustomActionObj;
        private final Bundle mExtras;
        private final int mIcon;
        private final CharSequence mName;
        
        static {
            CREATOR = (Parcelable$Creator)new Parcelable$Creator<CustomAction>() {
                public CustomAction createFromParcel(final Parcel parcel) {
                    return new CustomAction(parcel);
                }
                
                public CustomAction[] newArray(final int n) {
                    return new CustomAction[n];
                }
            };
        }
        
        private CustomAction(final Parcel parcel) {
            this.mAction = parcel.readString();
            this.mName = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.mIcon = parcel.readInt();
            this.mExtras = parcel.readBundle();
        }
        
        private CustomAction(final String mAction, final CharSequence mName, final int mIcon, final Bundle mExtras) {
            this.mAction = mAction;
            this.mName = mName;
            this.mIcon = mIcon;
            this.mExtras = mExtras;
        }
        
        public static CustomAction fromCustomAction(final Object mCustomActionObj) {
            if (mCustomActionObj != null && Build$VERSION.SDK_INT >= 21) {
                final CustomAction customAction = new CustomAction(PlaybackStateCompatApi21.CustomAction.getAction(mCustomActionObj), PlaybackStateCompatApi21.CustomAction.getName(mCustomActionObj), PlaybackStateCompatApi21.CustomAction.getIcon(mCustomActionObj), PlaybackStateCompatApi21.CustomAction.getExtras(mCustomActionObj));
                customAction.mCustomActionObj = mCustomActionObj;
                return customAction;
            }
            return null;
        }
        
        public int describeContents() {
            return 0;
        }
        
        public String getAction() {
            return this.mAction;
        }
        
        public Object getCustomAction() {
            if (this.mCustomActionObj == null && Build$VERSION.SDK_INT >= 21) {
                return this.mCustomActionObj = PlaybackStateCompatApi21.CustomAction.newInstance(this.mAction, this.mName, this.mIcon, this.mExtras);
            }
            return this.mCustomActionObj;
        }
        
        public Bundle getExtras() {
            return this.mExtras;
        }
        
        public int getIcon() {
            return this.mIcon;
        }
        
        public CharSequence getName() {
            return this.mName;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Action:mName='");
            sb.append((Object)this.mName);
            sb.append(", mIcon=");
            sb.append(this.mIcon);
            sb.append(", mExtras=");
            sb.append(this.mExtras);
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            parcel.writeString(this.mAction);
            TextUtils.writeToParcel(this.mName, parcel, n);
            parcel.writeInt(this.mIcon);
            parcel.writeBundle(this.mExtras);
        }
    }
    
    public static final class Builder
    {
        private final String mAction;
        private Bundle mExtras;
        private final int mIcon;
        private final CharSequence mName;
        
        public Builder(final String mAction, final CharSequence mName, final int mIcon) {
            if (TextUtils.isEmpty((CharSequence)mAction)) {
                throw new IllegalArgumentException("You must specify an action to build a CustomAction.");
            }
            if (TextUtils.isEmpty(mName)) {
                throw new IllegalArgumentException("You must specify a name to build a CustomAction.");
            }
            if (mIcon == 0) {
                throw new IllegalArgumentException("You must specify an icon resource id to build a CustomAction.");
            }
            this.mAction = mAction;
            this.mName = mName;
            this.mIcon = mIcon;
        }
        
        public CustomAction build() {
            return new CustomAction(this.mAction, this.mName, this.mIcon, this.mExtras);
        }
        
        public Builder setExtras(final Bundle mExtras) {
            this.mExtras = mExtras;
            return this;
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
    }
}
