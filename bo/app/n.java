package bo.app;

import com.appboy.configuration.*;
import com.appboy.support.*;
import java.util.concurrent.*;

public class n implements t
{
    private static final String a;
    private final AppboyConfigurationProvider b;
    private final cy c;
    private final r d;
    private final Object e;
    private volatile boolean f;
    private volatile Thread g;
    private volatile boolean h;
    private cz i;
    private boolean j;
    
    static {
        a = AppboyLogger.getAppboyLogTag(n.class);
    }
    
    public n(final AppboyConfigurationProvider b, final ad ad, final cy c, final r d, final ThreadFactory threadFactory, final boolean j) {
        this.e = new Object();
        this.f = false;
        this.h = true;
        this.j = false;
        this.b = b;
        this.c = c;
        this.d = d;
        this.g = threadFactory.newThread(new a());
        this.i = new cz(ad);
        this.j = j;
    }
    
    private void b(final cw cw) {
        if (!cw.h() && !this.j) {
            this.c.a(cw);
            return;
        }
        this.i.a(cw);
    }
    
    private cs c() {
        return new cs(this.b.getBaseUrlForRequests());
    }
    
    private void c(final cw cw) {
        if (!cw.h() && !this.j) {
            this.c.b(cw);
            return;
        }
        this.i.b(cw);
    }
    
    public void a() {
        synchronized (this.e) {
            if (this.f) {
                AppboyLogger.d(n.a, "Automatic request execution start was previously requested, continuing without action.");
                return;
            }
            if (this.g != null) {
                this.g.start();
            }
            this.f = true;
        }
    }
    
    public void a(final ac ac) {
        Object o = this.e;
        synchronized (o) {
            this.h = false;
            this.g.interrupt();
            this.g = null;
            // monitorexit(o)
            if (!this.d.a()) {
                this.d.a(this.c());
            }
            o = this.d.c();
            if (o != null) {
                this.c((cw)o);
            }
            ac.a();
        }
    }
    
    @Override
    public void a(final ca ca) {
        this.d.a(ca);
    }
    
    @Override
    public void a(final ce ce) {
        this.d.a(ce);
    }
    
    @Override
    public void a(final cw cw) {
        this.d.a(cw);
    }
    
    @Override
    public void b(final ca ca) {
        this.d.b(ca);
    }
    
    class a implements Runnable
    {
        private a() {
        }
        
        @Override
        public void run() {
            while (n.this.h) {
                try {
                    n.this.b(n.this.d.b());
                }
                catch (InterruptedException ex) {
                    final String b = n.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Automatic thread interrupted! [");
                    sb.append(ex.getMessage());
                    sb.append("]");
                    AppboyLogger.e(b, sb.toString());
                }
            }
        }
    }
}
