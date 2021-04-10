package android.support.v4.media;

import android.view.*;

interface TransportMediatorCallback
{
    long getPlaybackPosition();
    
    void handleAudioFocusChange(final int p0);
    
    void handleKey(final KeyEvent p0);
    
    void playbackPositionUpdate(final long p0);
}
