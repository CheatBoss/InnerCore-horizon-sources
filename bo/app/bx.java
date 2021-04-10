package bo.app;

import com.appboy.support.*;
import java.util.*;

public class bx
{
    private static final String a;
    private final List<String> b;
    private long c;
    private boolean d;
    private final Object e;
    private br f;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bx.class);
    }
    
    public bx() {
        this.d = false;
        this.e = new Object();
        this.b = new ArrayList<String>(32);
    }
    
    static String b(String s, String string, final Throwable t) {
        if (StringUtils.isNullOrBlank(s)) {
            return null;
        }
        if (StringUtils.isNullOrBlank(string) && (t == null || StringUtils.isNullOrBlank(t.getMessage()))) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(c());
        sb.append(" ");
        sb.append(s);
        final String s2 = s = sb.toString();
        if (string != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s2);
            sb2.append(": ");
            sb2.append(string);
            s = sb2.toString();
        }
        string = s;
        if (t != null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(": ");
            sb3.append(t.getMessage());
            string = sb3.toString();
        }
        return string.substring(0, Math.min(string.length(), 1000));
    }
    
    private static String c() {
        return du.a(new Date(), u.c);
    }
    
    private boolean d() {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        boolean b = false;
        if (stackTrace != null) {
            if (stackTrace.length == 0) {
                return true;
            }
            final StackTraceElement stackTraceElement = stackTrace[1];
            final String methodName = stackTraceElement.getMethodName();
            final String className = stackTraceElement.getClassName();
            final int length = stackTrace.length;
            int i = 0;
            int n = 0;
            while (i < length) {
                final StackTraceElement stackTraceElement2 = stackTrace[i];
                int n2 = n;
                if (stackTraceElement2.getClassName().equals(className)) {
                    n2 = n;
                    if (stackTraceElement2.getMethodName().equals(methodName)) {
                        n2 = n + 1;
                    }
                }
                ++i;
                n = n2;
            }
            if (n != 1) {
                return true;
            }
        }
        else {
            b = true;
        }
        return b;
    }
    
    public void a(final br f) {
        this.f = f;
    }
    
    public void a(final co co) {
        this.a(co.o());
    }
    
    public void a(String b, final String s, final Throwable t) {
        if (!this.d) {
            return;
        }
        if (s != null && (s.contains("device_logs") || s.contains("test_user_data"))) {
            return;
        }
        if (this.d()) {
            return;
        }
        synchronized (this.e) {
            if (this.b.size() >= 32) {
                this.b();
            }
            if (this.b.isEmpty() || this.c == 0L) {
                this.c = du.a();
            }
            b = b(b, s, t);
            if (b != null) {
                this.b.add(b);
            }
        }
    }
    
    public void a(final boolean d) {
        final Object e = this.e;
        // monitorenter(e)
        Label_0023: {
            if (d) {
                break Label_0023;
            }
            while (true) {
                try {
                    this.b.clear();
                    while (true) {
                        this.d = d;
                        return;
                        throw;
                        AppboyLogger.i(bx.a, "Test user device logging is enabled.", false);
                        continue;
                    }
                }
                // monitorexit(e)
                // monitorexit(e)
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public boolean a() {
        return this.d;
    }
    
    void b() {
        synchronized (this.e) {
            if (this.f != null) {
                final ArrayList<String> list = new ArrayList<String>();
                final Iterator<String> iterator = this.b.iterator();
                while (iterator.hasNext()) {
                    list.add(iterator.next());
                }
                this.f.a(list, this.c);
            }
            this.b.clear();
            this.c = 0L;
        }
    }
}
