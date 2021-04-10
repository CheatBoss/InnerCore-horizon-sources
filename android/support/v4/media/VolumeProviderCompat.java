package android.support.v4.media;

import android.os.*;
import java.lang.annotation.*;

public abstract class VolumeProviderCompat
{
    public static final int VOLUME_CONTROL_ABSOLUTE = 2;
    public static final int VOLUME_CONTROL_FIXED = 0;
    public static final int VOLUME_CONTROL_RELATIVE = 1;
    private Callback mCallback;
    private final int mControlType;
    private int mCurrentVolume;
    private final int mMaxVolume;
    private Object mVolumeProviderObj;
    
    public VolumeProviderCompat(final int mControlType, final int mMaxVolume, final int mCurrentVolume) {
        this.mControlType = mControlType;
        this.mMaxVolume = mMaxVolume;
        this.mCurrentVolume = mCurrentVolume;
    }
    
    public final int getCurrentVolume() {
        return this.mCurrentVolume;
    }
    
    public final int getMaxVolume() {
        return this.mMaxVolume;
    }
    
    public final int getVolumeControl() {
        return this.mControlType;
    }
    
    public Object getVolumeProvider() {
        if (this.mVolumeProviderObj == null && Build$VERSION.SDK_INT >= 21) {
            return this.mVolumeProviderObj = VolumeProviderCompatApi21.createVolumeProvider(this.mControlType, this.mMaxVolume, this.mCurrentVolume, (VolumeProviderCompatApi21.Delegate)new VolumeProviderCompatApi21.Delegate() {
                @Override
                public void onAdjustVolume(final int n) {
                    VolumeProviderCompat.this.onAdjustVolume(n);
                }
                
                @Override
                public void onSetVolumeTo(final int n) {
                    VolumeProviderCompat.this.onSetVolumeTo(n);
                }
            });
        }
        return this.mVolumeProviderObj;
    }
    
    public void onAdjustVolume(final int n) {
    }
    
    public void onSetVolumeTo(final int n) {
    }
    
    public void setCallback(final Callback mCallback) {
        this.mCallback = mCallback;
    }
    
    public final void setCurrentVolume(final int mCurrentVolume) {
        this.mCurrentVolume = mCurrentVolume;
        final Object volumeProvider = this.getVolumeProvider();
        if (volumeProvider != null) {
            VolumeProviderCompatApi21.setCurrentVolume(volumeProvider, mCurrentVolume);
        }
        if (this.mCallback != null) {
            this.mCallback.onVolumeChanged(this);
        }
    }
    
    public abstract static class Callback
    {
        public abstract void onVolumeChanged(final VolumeProviderCompat p0);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ControlType {
    }
}
