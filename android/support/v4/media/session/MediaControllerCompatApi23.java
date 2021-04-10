package android.support.v4.media.session;

import android.net.*;
import android.os.*;
import android.media.session.*;

class MediaControllerCompatApi23
{
    public static class TransportControls extends MediaControllerCompatApi21.TransportControls
    {
        public static void playFromUri(final Object o, final Uri uri, final Bundle bundle) {
            ((MediaController$TransportControls)o).playFromUri(uri, bundle);
        }
    }
}
