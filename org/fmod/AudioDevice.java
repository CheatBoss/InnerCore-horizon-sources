package org.fmod;

import android.media.*;
import android.util.*;

public class AudioDevice
{
    private AudioTrack mTrack;
    
    public AudioDevice() {
        this.mTrack = null;
    }
    
    private int fetchChannelConfigFromCount(final int n) {
        if (n == 1) {
            return 2;
        }
        if (n == 2) {
            return 3;
        }
        if (n == 6) {
            return 252;
        }
        if (n == 8) {
            return 6396;
        }
        return 0;
    }
    
    public void close() {
        try {
            this.mTrack.stop();
        }
        catch (IllegalStateException ex) {
            Log.e("fmod", "AudioDevice::init : AudioTrack stop caused IllegalStateException");
        }
        this.mTrack.release();
        this.mTrack = null;
    }
    
    public boolean init(int n, final int n2, final int n3, final int n4) {
        final int fetchChannelConfigFromCount = this.fetchChannelConfigFromCount(n);
        final int minBufferSize = AudioTrack.getMinBufferSize(n2, fetchChannelConfigFromCount, 2);
        if (minBufferSize < 0) {
            Log.w("fmod", "AudioDevice::init : Couldn't query minimum buffer size, possibly unsupported sample rate or channel count");
        }
        else {
            final StringBuilder sb = new StringBuilder();
            sb.append("AudioDevice::init : Min buffer size: ");
            sb.append(minBufferSize);
            sb.append(" bytes");
            Log.i("fmod", sb.toString());
        }
        n = n3 * n4 * n * 2;
        if (n <= minBufferSize) {
            n = minBufferSize;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("AudioDevice::init : Actual buffer size: ");
        sb2.append(n);
        sb2.append(" bytes");
        Log.i("fmod", sb2.toString());
        try {
            final AudioTrack mTrack = new AudioTrack(3, n2, fetchChannelConfigFromCount, 2, n, 1);
            this.mTrack = mTrack;
            try {
                mTrack.play();
                return true;
            }
            catch (IllegalStateException ex) {
                Log.e("fmod", "AudioDevice::init : AudioTrack play caused IllegalStateException");
                this.mTrack.release();
                this.mTrack = null;
                return false;
            }
        }
        catch (IllegalArgumentException ex2) {
            Log.e("fmod", "AudioDevice::init : AudioTrack creation caused IllegalArgumentException");
            return false;
        }
    }
    
    public void write(final byte[] array, final int n) {
        this.mTrack.write(array, 0, n);
    }
}
