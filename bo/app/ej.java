package bo.app;

import com.appboy.support.*;
import android.content.*;
import com.appboy.configuration.*;
import java.util.concurrent.*;
import android.app.*;

public final class ej
{
    private static final String a;
    private final dt b;
    private final di c;
    private final ac d;
    private final n e;
    private final bl f;
    private final dl g;
    private final ab h;
    private final ThreadPoolExecutor i;
    private final d j;
    private final q k;
    private final bn l;
    private final bu m;
    private final fy n;
    private final dr o;
    private final bi p;
    private final bh q;
    private final dh r;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ej.class);
    }
    
    public ej(final Context context, final l l, final AppboyConfigurationProvider appboyConfigurationProvider, final ad ad, final bg bg, final bv bv, final boolean b, final boolean b2, final bx bx) {
        final String a = l.a();
        final String string = appboyConfigurationProvider.getAppboyApiKey().toString();
        final dq dq = new dq(context);
        final ax ax = new ax();
        this.i = new ThreadPoolExecutor(ei.a(), ei.b(), ei.c(), TimeUnit.SECONDS, ei.d(), ax);
        this.d = new ac(this.i, dq);
        this.o = new dr(context, string);
        df df;
        if (a.equals("")) {
            this.b = new dt(context, bv, this.o, dq);
            this.c = new di(context);
            df = bo.app.df.a(context, null, string);
        }
        else {
            this.b = new dt(context, a, string, bv, this.o, dq);
            this.c = new di(context, a, string);
            df = bo.app.df.a(context, a, string);
        }
        final bo bo = new bo(context, appboyConfigurationProvider, a, bg, this.c);
        this.j = new d();
        final r r = new r(this.b, bo, appboyConfigurationProvider);
        final dk dk = new dk(new ds(context, a, string), this.d);
        final az az = new az(ax);
        ax.a(new ay(this.d));
        final dj dj = new dj(new dg(new dp(df), az), this.d);
        final AlarmManager alarmManager = (AlarmManager)context.getSystemService("alarm");
        this.l = new bn(context, dk, this.d, alarmManager, this.o, appboyConfigurationProvider.getSessionTimeoutSeconds(), appboyConfigurationProvider.getIsSessionStartBasedTimeoutEnabled());
        this.q = new bh(dj);
        this.g = new dl(context, a);
        this.r = new dh(context, a, string);
        final db db = new db(this.j, bo.app.e.a(), this.d, ad, this.i, this.g, this.o, this.r);
        (this.k = new q(context, this.d, new o(), alarmManager, new p(context), a)).a(this.d);
        this.k.a(b2);
        this.e = new n(appboyConfigurationProvider, this.d, db, r, ax, b);
        this.f = new bl(this.l, this.e, this.d, bo, appboyConfigurationProvider, this.o, this.q, a, b2, new bm(context, this.d, this.o), dq);
        this.n = new fy(context, this.f, this.i, this.d, appboyConfigurationProvider, a, string);
        this.p = new bi(context, string, this.f, appboyConfigurationProvider, this.o);
        if (!b && db instanceof db) {
            db.a(this.f);
        }
        this.g.a(this.f);
        this.r.a(this.f);
        this.m = new bk(context, this.f, appboyConfigurationProvider, this.o);
        final bu m = this.m;
        final n e = this.e;
        final bl f = this.f;
        final dt b3 = this.b;
        final di c = this.c;
        final dr o = this.o;
        final fy n = this.n;
        this.h = new ab(context, m, e, f, b3, c, o, n, n.a(), this.q, this.p, bx, ad);
    }
    
    public dr a() {
        return this.o;
    }
    
    public q b() {
        return this.k;
    }
    
    public ab c() {
        return this.h;
    }
    
    public bl d() {
        return this.f;
    }
    
    public n e() {
        return this.e;
    }
    
    public ac f() {
        return this.d;
    }
    
    public dt g() {
        return this.b;
    }
    
    public ThreadPoolExecutor h() {
        return this.i;
    }
    
    public dl i() {
        return this.g;
    }
    
    public bu j() {
        return this.m;
    }
    
    public bh k() {
        return this.q;
    }
    
    public fy l() {
        return this.n;
    }
    
    public bi m() {
        return this.p;
    }
    
    public dh n() {
        return this.r;
    }
    
    public void o() {
        this.i.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (ej.this.b) {
                        if (ej.this.b.c()) {
                            AppboyLogger.i(ej.a, "User cache was locked, waiting.");
                            try {
                                ej.this.b.wait();
                                AppboyLogger.d(ej.a, "User cache notified.");
                            }
                            catch (InterruptedException ex3) {}
                        }
                        // monitorexit(ej.a(this.a))
                        ej.this.e.a(ej.this.d);
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.w(ej.a, "Exception while shutting down dispatch manager. Continuing.", ex);
                }
                try {
                    ej.this.k.b();
                }
                catch (Exception ex2) {
                    AppboyLogger.w(ej.a, "Exception while un-registering data refresh broadcast receivers. Continuing.", ex2);
                }
            }
        });
    }
}
