package android.support.v4.view;

import android.view.*;

class ViewConfigurationCompatICS
{
    static boolean hasPermanentMenuKey(final ViewConfiguration viewConfiguration) {
        return viewConfiguration.hasPermanentMenuKey();
    }
}
