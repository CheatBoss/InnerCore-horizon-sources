package android.support.v4.media.session;

import android.media.session.*;

class MediaSessionCompatApi22
{
    public static void setRatingType(final Object o, final int ratingType) {
        ((MediaSession)o).setRatingType(ratingType);
    }
}
