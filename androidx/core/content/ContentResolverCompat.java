package androidx.core.content;

import android.content.*;
import android.net.*;
import androidx.core.os.*;
import android.database.*;
import android.os.*;

public final class ContentResolverCompat
{
    private ContentResolverCompat() {
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
        while (true) {
            Label_0059: {
                if (Build$VERSION.SDK_INT < 16) {
                    break Label_0059;
                }
                Label_0023: {
                    if (cancellationSignal == null) {
                        final Object cancellationSignalObject = null;
                        break Label_0023;
                    }
                    try {
                        final Object cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
                        return contentResolver.query(uri, array, s, array2, s2, (android.os.CancellationSignal)cancellationSignalObject);
                        // iftrue(Label_0057:, !ex instanceof OperationCanceledException)
                        throw new androidx.core.os.OperationCanceledException();
                        // iftrue(Label_0069:, cancellationSignal == null)
                        cancellationSignal.throwIfCanceled();
                        return contentResolver.query(uri, array, s, array2, s2);
                        Label_0057: {
                            throw;
                        }
                        Label_0069:
                        return contentResolver.query(uri, array, s, array2, s2);
                    }
                    catch (Exception ex2) {}
                }
            }
            final Exception ex2;
            final Exception ex = ex2;
            continue;
        }
    }
}
