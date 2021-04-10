package bo.app;

import com.appboy.support.*;
import android.graphics.*;
import java.io.*;

public class ba
{
    private static final String a;
    private final bb b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ba.class);
    }
    
    public ba(final File file, final int n, final int n2, final long n3) {
        this.b = bb.a(file, n, n2, n3);
    }
    
    private String c(final String s) {
        return Integer.toString(s.hashCode());
    }
    
    public Bitmap a(String s) {
        final String c = this.c(s);
        s = null;
        Label_0152: {
            bb.b b2;
            try {
                final bb.b b = (bb.b)(s = (String)this.b.a(c));
                try {
                    try {
                        final Bitmap decodeStream = BitmapFactory.decodeStream(b.a(0));
                        if (b != null) {
                            b.close();
                        }
                        return decodeStream;
                    }
                    finally {}
                }
                catch (IOException ex) {}
            }
            catch (IOException ex) {
                b2 = null;
            }
            finally {
                break Label_0152;
            }
            final String a = ba.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to get bitmap from disk cache for key ");
            sb.append(c);
            final IOException ex;
            AppboyLogger.e(a, sb.toString(), ex);
            if (b2 != null) {
                b2.close();
            }
            s = ba.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to load image from disk cache: ");
            sb2.append(c);
            AppboyLogger.d(s, sb2.toString());
            return null;
        }
        if (s != null) {
            ((bb.b)s).close();
        }
    }
    
    public void a(String ex, final Bitmap bitmap) {
        final String c = this.c((String)ex);
        final StringBuilder sb = null;
        final IOException ex2 = ex = null;
        Serializable s;
        final IOException ex3;
        try {
            try {
                final bb.a b = this.b.b(c);
                ex = ex2;
                final OutputStream a = b.a(0);
                try {
                    bitmap.compress(Bitmap$CompressFormat.PNG, 100, a);
                    a.flush();
                    a.close();
                    b.a();
                    if (a == null) {
                        return;
                    }
                    try {
                        a.close();
                        return;
                    }
                    catch (IOException a) {
                        ex = (IOException)ba.a;
                        final StringBuilder sb2 = new StringBuilder();
                    }
                }
                catch (IOException ex) {}
            }
            finally {
                s = ex;
                ex = ex3;
            }
        }
        catch (IOException ex3) {
            s = sb;
        }
        final String a2 = ba.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Exception while producing output stream or compressing bitmap for key ");
        sb3.append(c);
        AppboyLogger.e(a2, sb3.toString(), ex3);
        if (s == null) {
            return;
        }
        try {
            ((OutputStream)s).close();
            return;
        }
        catch (IOException ex3) {
            ex = (IOException)ba.a;
            s = new StringBuilder();
        }
        ((StringBuilder)s).append("Exception while closing disk cache output stream for key");
        ((StringBuilder)s).append(c);
        AppboyLogger.e((String)ex, ((StringBuilder)s).toString(), ex3);
        return;
        if (s != null) {
            try {
                ((OutputStream)s).close();
            }
            catch (IOException ex4) {
                final String a3 = ba.a;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Exception while closing disk cache output stream for key");
                sb4.append(c);
                AppboyLogger.e(a3, sb4.toString(), ex4);
            }
        }
        throw ex;
    }
    
    public boolean b(String c) {
        c = this.c((String)c);
        boolean b = false;
        try {
            try {
                final bb.b a = this.b.a((String)c);
                if (a != null) {
                    b = true;
                }
                if (a != null) {
                    a.close();
                }
                return b;
            }
            finally {}
        }
        catch (IOException ex) {
            final String a2 = ba.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Error while retrieving disk for key ");
            sb.append((String)c);
            AppboyLogger.e(a2, sb.toString(), ex);
            return false;
        }
    }
}
