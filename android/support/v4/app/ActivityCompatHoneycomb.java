package android.support.v4.app;

import android.app.*;
import java.io.*;

class ActivityCompatHoneycomb
{
    static void dump(final Activity activity, final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
        activity.dump(s, fileDescriptor, printWriter, array);
    }
    
    static void invalidateOptionsMenu(final Activity activity) {
        activity.invalidateOptionsMenu();
    }
}
