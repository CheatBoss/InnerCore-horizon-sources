package android.support.v4.content;

import java.io.*;
import android.content.*;

class ContextCompatHoneycomb
{
    public static File getObbDir(final Context context) {
        return context.getObbDir();
    }
    
    static void startActivities(final Context context, final Intent[] array) {
        context.startActivities(array);
    }
}
