package android.support.v4.content;

import android.os.*;
import android.content.*;
import android.net.*;
import android.database.*;
import android.support.v4.os.*;

public final class ContentResolverCompat
{
    private static final ContentResolverCompatImpl IMPL;
    
    static {
        ContentResolverCompatImpl impl;
        if (Build$VERSION.SDK_INT >= 16) {
            impl = new ContentResolverCompatImplJB();
        }
        else {
            impl = new ContentResolverCompatImplBase();
        }
        IMPL = impl;
    }
    
    private ContentResolverCompat() {
    }
    
    public static Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
        return ContentResolverCompat.IMPL.query(contentResolver, uri, array, s, array2, s2, cancellationSignal);
    }
    
    interface ContentResolverCompatImpl
    {
        Cursor query(final ContentResolver p0, final Uri p1, final String[] p2, final String p3, final String[] p4, final String p5, final CancellationSignal p6);
    }
    
    static class ContentResolverCompatImplBase implements ContentResolverCompatImpl
    {
        @Override
        public Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
            if (cancellationSignal != null) {
                cancellationSignal.throwIfCanceled();
            }
            return contentResolver.query(uri, array, s, array2, s2);
        }
    }
    
    static class ContentResolverCompatImplJB extends ContentResolverCompatImplBase
    {
        @Override
        public Cursor query(final ContentResolver contentResolver, final Uri uri, final String[] array, final String s, final String[] array2, final String s2, final CancellationSignal cancellationSignal) {
            while (true) {
                Label_0053: {
                    if (cancellationSignal == null) {
                        break Label_0053;
                    }
                    while (true) {
                        try {
                            final Object cancellationSignalObject = cancellationSignal.getCancellationSignalObject();
                            return ContentResolverCompatJellybean.query(contentResolver, uri, array, s, array2, s2, cancellationSignalObject);
                            Label_0047: {
                                throw;
                            }
                            // iftrue(Label_0047:, !ContentResolverCompatJellybean.isFrameworkOperationCanceledException(ex))
                            throw new OperationCanceledException();
                        }
                        catch (Exception ex) {
                            continue;
                        }
                        break;
                    }
                }
                final Object cancellationSignalObject = null;
                continue;
            }
        }
    }
}
