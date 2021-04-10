package bo.app;

import java.util.concurrent.atomic.*;
import com.appboy.configuration.*;
import android.app.*;
import com.appboy.support.*;
import com.appboy.models.outgoing.*;
import java.util.*;
import org.json.*;

public class bl implements br
{
    private static final String a;
    private AtomicInteger b;
    private AtomicInteger c;
    private volatile String d;
    private final Object e;
    private final Object f;
    private final bn g;
    private final bm h;
    private final t i;
    private final ad j;
    private final bs k;
    private final AppboyConfigurationProvider l;
    private final dr m;
    private final bh n;
    private final String o;
    private final dq p;
    private boolean q;
    private Class<? extends Activity> r;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bl.class);
    }
    
    public bl(final bn g, final t i, final ad j, final bs k, final AppboyConfigurationProvider l, final dr m, final bh n, final String o, final boolean q, final bm h, final dq p11) {
        this.b = new AtomicInteger(0);
        this.c = new AtomicInteger(0);
        this.d = "";
        this.e = new Object();
        this.f = new Object();
        this.q = false;
        this.r = null;
        this.g = g;
        this.i = i;
        this.j = j;
        this.k = k;
        this.l = l;
        this.q = q;
        this.o = o;
        this.m = m;
        this.n = n;
        this.h = h;
        this.p = p11;
    }
    
    private boolean b(final Throwable t) {
        synchronized (this.f) {
            this.b.getAndIncrement();
            if (this.d.equals(t.getMessage()) && this.c.get() > 3 && this.b.get() < 100) {
                return true;
            }
            if (this.d.equals(t.getMessage())) {
                this.c.getAndIncrement();
            }
            else {
                this.c.set(0);
            }
            if (this.b.get() >= 100) {
                this.b.set(0);
            }
            this.d = t.getMessage();
            return false;
        }
    }
    
    public cd a() {
        if (this.p.a()) {
            AppboyLogger.w(bl.a, "SDK is disabled. Returning null session.");
            return null;
        }
        final cd a = this.g.a();
        final String a2 = bl.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Completed the openSession call. Starting or continuing session ");
        sb.append(a.a());
        AppboyLogger.i(a2, sb.toString());
        return a;
    }
    
    public cd a(final Activity activity) {
        if (this.p.a()) {
            AppboyLogger.w(bl.a, "SDK is disabled. Returning null session.");
            return null;
        }
        final cd a = this.a();
        this.r = activity.getClass();
        this.h.a();
        final String a2 = bl.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Opened session with activity: ");
        sb.append(activity.getLocalClassName());
        AppboyLogger.v(a2, sb.toString());
        return a;
    }
    
    public void a(final long n, final long n2) {
        this.i.a(new cr(this.l.getBaseUrlForRequests(), n, n2, this.o));
    }
    
    @Override
    public void a(final av av) {
        try {
            if (this.b(av)) {
                AppboyLogger.w(bl.a, "Not logging duplicate database exception.");
                return;
            }
            this.a(cg.a(av, this.b()));
        }
        catch (Exception ex) {
            AppboyLogger.e(bl.a, "Failed to log error.", ex);
        }
        catch (JSONException ex2) {
            final String a = bl.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create database exception event from ");
            sb.append(av);
            sb.append(".");
            AppboyLogger.e(a, sb.toString(), (Throwable)ex2);
        }
    }
    
    @Override
    public void a(final cb cb) {
        AppboyLogger.d(bl.a, "Posting geofence request for location.");
        this.a(new cu(this.l.getBaseUrlForRequests(), cb));
    }
    
    @Override
    public void a(final ck.a a) {
        if (a == null) {
            AppboyLogger.d(bl.a, "Cannot request data sync with null respond with object");
            return;
        }
        final dr m = this.m;
        if (m != null && m.p()) {
            a.a(new cj(this.m.k()));
        }
        a.a(this.e());
        final ck c = a.c();
        if (c.c() && (c.d() || c.e())) {
            this.m.a(false);
        }
        this.a(new cs(this.l.getBaseUrlForRequests(), c));
    }
    
    void a(final cw cw) {
        if (this.p.a()) {
            AppboyLogger.w(bl.a, "SDK is disabled. Not adding request to dispatch.");
            return;
        }
        this.i.a(cw);
    }
    
    @Override
    public void a(final em em, final fk fk) {
        this.a(new dc(this.l.getBaseUrlForRequests(), em, fk, this, this.e()));
    }
    
    @Override
    public void a(final fk fk) {
        this.j.a(new at(fk), at.class);
    }
    
    public void a(final String s, final String s2, final boolean b) {
        if (s == null || !ValidationUtils.isValidEmailAddress(s)) {
            throw new IllegalArgumentException("Reply to email address is invalid");
        }
        if (!StringUtils.isNullOrBlank(s2)) {
            this.a(new ct(this.l.getBaseUrlForRequests(), new Feedback(s2, s, b, this.k.a(), this.e())));
            return;
        }
        throw new IllegalArgumentException("Feedback message cannot be null or blank");
    }
    
    @Override
    public void a(final Throwable t) {
        try {
            if (this.b(t)) {
                AppboyLogger.w(bl.a, "Not logging duplicate error.");
                return;
            }
            this.a(cg.a(t, this.b()));
        }
        catch (Exception ex) {
            AppboyLogger.e(bl.a, "Failed to log error.", ex);
        }
        catch (JSONException ex2) {
            final String a = bl.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create error event from ");
            sb.append(t);
            sb.append(".");
            AppboyLogger.e(a, sb.toString(), (Throwable)ex2);
        }
    }
    
    @Override
    public void a(final List<String> list, final long n) {
        this.a(new dd(this.l.getBaseUrlForRequests(), list, n, this.o));
    }
    
    public void a(final boolean q) {
        this.q = q;
    }
    
    @Override
    public boolean a(final ca ca) {
        final boolean a = this.p.a();
        boolean b = false;
        if (a) {
            final String a2 = bl.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("SDK is disabled. Not logging event: ");
            sb.append(ca);
            AppboyLogger.w(a2, sb.toString());
            return false;
        }
        final Object e = this.e;
        // monitorenter(e)
        Label_0362: {
            if (ca == null) {
                break Label_0362;
            }
            while (true) {
                try {
                    if (!this.g.d() && this.g.c() != null) {
                        ca.a(this.g.c());
                        b = true;
                    }
                    else {
                        final String a3 = bl.a;
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Not adding session id to event: ");
                        sb2.append(ec.a(ca.forJsonPut()));
                        AppboyLogger.d(a3, sb2.toString());
                    }
                    if (!StringUtils.isNullOrEmpty(this.e())) {
                        ca.a(this.e());
                    }
                    else {
                        final String a4 = bl.a;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Not adding user id to event: ");
                        sb3.append(ec.a(ca.forJsonPut()));
                        AppboyLogger.d(a4, sb3.toString());
                    }
                    if (w.b(ca.b())) {
                        AppboyLogger.d(bl.a, "Publishing an internal push body clicked event for any awaiting triggers.");
                        this.c(ca);
                    }
                    this.n.a(ca);
                    if (w.a(ca.b()) && !b) {
                        AppboyLogger.d(bl.a, "Adding push click to dispatcher pending list");
                        this.i.b(ca);
                    }
                    else {
                        this.i.a(ca);
                    }
                    if (ca.b().equals(w.z)) {
                        this.i.a(ca.f());
                    }
                    if (!b) {
                        this.d();
                    }
                    // monitorexit(e)
                    return true;
                    AppboyLogger.e(bl.a, "Appboy manager received null event.");
                    throw new NullPointerException();
                    // monitorexit(e)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    public cd b(final Activity activity) {
        if (this.p.a()) {
            AppboyLogger.w(bl.a, "SDK is disabled. Returning null session.");
            return null;
        }
        if (this.r != null && !activity.getClass().equals(this.r)) {
            return null;
        }
        this.h.b();
        final String a = bl.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Closed session with activity: ");
        sb.append(activity.getLocalClassName());
        AppboyLogger.v(a, sb.toString());
        return this.g.b();
    }
    
    public ce b() {
        return this.g.c();
    }
    
    @Override
    public void b(final ca ca) {
        AppboyLogger.d(bl.a, "Posting geofence report for geofence event.");
        this.a(new cv(this.l.getBaseUrlForRequests(), ca));
    }
    
    public void c() {
        if (this.p.a()) {
            AppboyLogger.w(bl.a, "SDK is disabled. Not force closing session.");
            return;
        }
        this.r = null;
        this.g.e();
    }
    
    void c(final ca ca) {
        final JSONObject c = ca.c();
        if (c != null) {
            final String optString = c.optString("cid", (String)null);
            if (ca.b().equals(w.e)) {
                this.j.a(new as(optString, ca), as.class);
            }
        }
        else {
            AppboyLogger.w(bl.a, "Event json was null. Not publishing push clicked trigger event.");
        }
    }
    
    public void d() {
        this.a(new ck.a());
    }
    
    @Override
    public String e() {
        return this.o;
    }
}
