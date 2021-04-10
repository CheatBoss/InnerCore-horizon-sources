package android.support.v4.content;

import android.content.*;
import android.net.*;
import android.database.*;
import android.os.*;

class ContentResolverCompatJellybean
{
    static boolean isFrameworkOperationCanceledException(final Exception ex) {
        return ex instanceof OperationCanceledException;
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final Object o) {
        return contentResolver.query(uri, array, s, array2, s2, (CancellationSignal)o);
    }
}
