package android.support.v4.media;

import java.util.*;
import android.media.browse.*;
import java.lang.reflect.*;

class ParceledListSliceAdapterApi21
{
    private static Constructor sConstructor;
    
    static {
        try {
            ParceledListSliceAdapterApi21.sConstructor = Class.forName("android.content.pm.ParceledListSlice").getConstructor(List.class);
        }
        catch (ClassNotFoundException | NoSuchMethodException ex) {
            final Throwable t;
            t.printStackTrace();
        }
    }
    
    static Object newInstance(final List<MediaBrowser$MediaItem> list) {
        try {
            return ParceledListSliceAdapterApi21.sConstructor.newInstance(list);
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            t.printStackTrace();
            return null;
        }
    }
}
