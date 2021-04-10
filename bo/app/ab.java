package bo.app;

import java.util.concurrent.atomic.*;
import android.content.*;
import com.appboy.support.*;
import org.json.*;
import java.util.*;
import java.util.concurrent.*;
import com.appboy.*;
import com.appboy.events.*;
import android.util.*;

public class ab
{
    private static final String c;
    AtomicBoolean a;
    long b;
    private final bu d;
    private final t e;
    private final br f;
    private final Context g;
    private final dt h;
    private final di i;
    private final dr j;
    private final fu k;
    private final bh l;
    private final bi m;
    private final bx n;
    private final ad o;
    private final fw p;
    private AtomicBoolean q;
    private as r;
    
    static {
        c = AppboyLogger.getAppboyLogTag(ab.class);
    }
    
    public ab(final Context g, final bu d, final t e, final bl f, final dt h, final di i, final dr j, final fu k, final fw p13, final bh l, final bi m, final bx n, final ad o) {
        this.a = new AtomicBoolean(false);
        this.q = new AtomicBoolean(false);
        this.b = 0L;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
        this.k = k;
        this.p = p13;
        this.l = l;
        this.m = m;
        this.n = n;
        this.o = o;
    }
    
    private void a(final ap ap) {
        try {
            final cd a = ap.a();
            final cg a2 = cg.a(a.f());
            a2.a(a.a());
            this.f.a(a2);
        }
        catch (JSONException ex) {
            AppboyLogger.w(ab.c, "Could not create session end event.");
        }
    }
    
    protected IEventSubscriber<af> a() {
        return new IEventSubscriber<af>() {
            public void a(final af af) {
                final cw a = af.a();
                final ck e = a.e();
                if (e != null && e.c()) {
                    ab.this.j.a(false);
                }
                final ci c = a.c();
                if (c != null) {
                    ab.this.i.b(c, true);
                }
                final cl d = a.d();
                if (d != null) {
                    ab.this.h.b(d, true);
                }
                final by f = a.f();
                if (f != null) {
                    final Iterator<ca> iterator = f.a().iterator();
                    while (iterator.hasNext()) {
                        ab.this.l.b(iterator.next());
                    }
                }
            }
        };
    }
    
    protected IEventSubscriber<Throwable> a(final Semaphore semaphore) {
        return new IEventSubscriber<Throwable>() {
            public void a(final Throwable t) {
                try {
                    Label_0049: {
                        try {
                            ab.this.f.a(t);
                            final Semaphore a = semaphore;
                            if (a != null) {
                                break Label_0049;
                            }
                        }
                        finally {
                            final Semaphore a2 = semaphore;
                            if (a2 != null) {
                                a2.release();
                            }
                            final Semaphore a;
                            a.release();
                        }
                    }
                }
                catch (Exception ex) {}
            }
        };
    }
    
    public void a(final ac ac) {
        ac.a(this.b(), ae.class);
        ac.a(this.e(), ao.class);
        ac.a(this.g(), ap.class);
        ac.a(this.j(), as.class);
        ac.a(this.h(), an.class);
        ac.a(this.a((Semaphore)null), Throwable.class);
        ac.a(this.o(), av.class);
        ac.a(this.k(), au.class);
        ac.a(this.f(), al.class);
        ac.a(this.a(), af.class);
        ac.a(this.i(), aj.class);
        ac.a(this.l(), at.class);
        ac.a(this.m(), ak.class);
    }
    
    protected IEventSubscriber<ae> b() {
        return new IEventSubscriber<ae>() {
            public void a(final ae ae) {
                final cw a = ae.a();
                final ck e = a.e();
                if (e != null) {
                    if (e.d()) {
                        ab.this.c();
                        ab.this.d();
                    }
                    if (e.c()) {
                        ab.this.j.a(true);
                    }
                }
                final ci c = a.c();
                if (c != null) {
                    ab.this.i.b(c, false);
                }
                final cl d = a.d();
                if (d != null) {
                    ab.this.h.b(d, false);
                }
                final by f = a.f();
                if (f != null) {
                    final Iterator<ca> iterator = f.a().iterator();
                    while (iterator.hasNext()) {
                        ab.this.e.a(iterator.next());
                    }
                }
            }
        };
    }
    
    protected void c() {
        if (this.a.compareAndSet(true, false)) {
            this.k.a(new fn());
        }
    }
    
    protected void d() {
        if (this.q.compareAndSet(true, false) && this.r.a() != null) {
            this.k.a(new fp(this.r.a(), this.r.b()));
            this.r = null;
        }
    }
    
    protected IEventSubscriber<ao> e() {
        return new IEventSubscriber<ao>() {
            public void a(final ao ao) {
                AppboyLogger.d(ab.c, "Session start event for new session received.");
                ab.this.f.a(cg.j());
                ab.this.d.b();
                ab.this.d.c();
                ab.this.n();
                AppboyInternal.requestGeofenceRefresh(ab.this.g, false);
                ab.this.h.d();
            }
        };
    }
    
    protected IEventSubscriber<al> f() {
        return new IEventSubscriber<al>() {
            public void a(final al al) {
                ab.this.n();
            }
        };
    }
    
    protected IEventSubscriber<ap> g() {
        return new IEventSubscriber<ap>() {
            public void a(final ap ap) {
                ab.this.a(ap);
                Appboy.getInstance(ab.this.g).requestImmediateDataFlush();
            }
        };
    }
    
    protected IEventSubscriber<an> h() {
        return new IEventSubscriber<an>() {
            public void a(final an an) {
                ab.this.d.a(an.a());
                ab.this.m.a(an.a());
                ab.this.n.a(an.a());
            }
        };
    }
    
    protected IEventSubscriber<aj> i() {
        return new IEventSubscriber<aj>() {
            public void a(final aj aj) {
                ab.this.m.a(aj.a());
            }
        };
    }
    
    protected IEventSubscriber<as> j() {
        return new IEventSubscriber<as>() {
            public void a(final as as) {
                ab.this.q.set(true);
                ab.this.r = as;
                AppboyLogger.i(ab.c, "Requesting trigger update due to trigger-eligible push click event");
                ab.this.f.a(new ck.a().b());
            }
        };
    }
    
    protected IEventSubscriber<au> k() {
        return new IEventSubscriber<au>() {
            public void a(final au au) {
                ab.this.k.a(au.a());
                ab.this.c();
                ab.this.d();
            }
        };
    }
    
    protected IEventSubscriber<at> l() {
        return new IEventSubscriber<at>() {
            public void a(final at at) {
                ab.this.k.a(at.a());
            }
        };
    }
    
    protected IEventSubscriber<ak> m() {
        return new IEventSubscriber<ak>() {
            public void a(final ak ak) {
                final ek a = ak.a();
                synchronized (ab.this.p) {
                    if (ab.this.p.a(a)) {
                        ab.this.o.a(new InAppMessageEvent(ak.b(), ak.c()), InAppMessageEvent.class);
                        ab.this.p.a(a, du.a());
                        ab.this.k.a(du.a());
                    }
                    else {
                        final String p = ab.c;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Could not publish in-app message with trigger action id: ");
                        sb.append(a.b());
                        Log.d(p, sb.toString());
                    }
                }
            }
        };
    }
    
    void n() {
        if (this.b + 5L < du.a()) {
            this.a.set(true);
            AppboyLogger.d(ab.c, "Requesting trigger refresh.");
            this.f.a(new ck.a().b());
            this.b = du.a();
        }
    }
    
    protected IEventSubscriber<av> o() {
        return new IEventSubscriber<av>() {
            public void a(final av av) {
                try {
                    ab.this.f.a(av);
                }
                catch (Exception ex) {
                    AppboyLogger.e(ab.c, "Failed to log the database exception.", ex);
                }
            }
        };
    }
}
