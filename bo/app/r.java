package bo.app;

import java.util.concurrent.*;
import com.appboy.configuration.*;
import com.appboy.support.*;
import java.util.*;
import com.appboy.*;
import com.appboy.models.*;

public final class r implements t
{
    private static final String c;
    final ConcurrentHashMap<String, ca> a;
    final ConcurrentHashMap<String, ca> b;
    private final bs d;
    private final dt e;
    private final LinkedBlockingQueue<cw> f;
    private final AppboyConfigurationProvider g;
    
    static {
        c = AppboyLogger.getAppboyLogTag(r.class);
    }
    
    public r(final dt e, final bs d, final AppboyConfigurationProvider g) {
        this.e = e;
        this.d = d;
        this.f = new LinkedBlockingQueue<cw>(1000);
        this.g = g;
        this.a = new ConcurrentHashMap<String, ca>();
        this.b = new ConcurrentHashMap<String, ca>();
    }
    
    private void c(final cw cw) {
        if (this.d.c() != null) {
            cw.a(this.d.c());
        }
        if (this.g.getAppboyApiKey() != null) {
            cw.b(this.g.getAppboyApiKey().toString());
        }
        cw.c("2.6.0");
        cw.a(du.a());
    }
    
    private void d(final cw cw) {
        cw.d(this.d.e());
        cw.a(this.g.getSdkFlavor());
        cw.a(this.d.b());
        cw.a(this.e.b());
        cw.a(this.e());
    }
    
    private by e() {
        synchronized (this) {
            final Collection<ca> values = this.a.values();
            final ArrayList<ca> list = new ArrayList<ca>();
            for (final ca ca : values) {
                list.add(ca);
                values.remove(ca);
                final String c = r.c;
                final StringBuilder sb = new StringBuilder();
                sb.append("Event dispatched: ");
                sb.append(((IPutIntoJson<Object>)ca).forJsonPut());
                sb.append(" with uid: ");
                sb.append(ca.d());
                AppboyLogger.d(c, sb.toString());
            }
            return new by(new HashSet<ca>(list));
        }
    }
    
    @Override
    public void a(final ca ca) {
        if (ca == null) {
            AppboyLogger.w(r.c, "Tried to add null AppboyEvent to dispatch.");
            return;
        }
        this.a.putIfAbsent(ca.d(), ca);
    }
    
    @Override
    public void a(final ce ce) {
        synchronized (this) {
            if (this.b.isEmpty()) {
                return;
            }
            AppboyLogger.d(r.c, "Flushing pending events to dispatcher map");
            final Iterator<ca> iterator = this.b.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().a(ce);
            }
            this.a.putAll(this.b);
            this.b.clear();
        }
    }
    
    @Override
    public void a(final cw cw) {
        if (cw == null) {
            throw null;
        }
        if (this.d()) {
            AppboyLogger.i(r.c, "Network requests are offline, not adding request to queue.");
            return;
        }
        final String c = r.c;
        final StringBuilder sb = new StringBuilder();
        sb.append("Adding request to dispatcher with parameters: ");
        sb.append(ec.a(cw.g()));
        AppboyLogger.i(c, sb.toString(), false);
        this.f.add(cw);
    }
    
    public boolean a() {
        return this.f.isEmpty() ^ true;
    }
    
    public cw b() {
        return this.b(this.f.take());
    }
    
    cw b(final cw cw) {
        // monitorenter(this)
        if (cw == null) {
            // monitorexit(this)
            return null;
        }
        try {
            this.c(cw);
            if (cw instanceof dc) {
                return cw;
            }
            if (cw instanceof cu || cw instanceof cv) {
                return cw;
            }
            if (cw instanceof cr) {
                return cw;
            }
            this.d(cw);
            return cw;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    @Override
    public void b(final ca ca) {
        // monitorenter(this)
        Label_0023: {
            if (ca == null) {
                Label_0041: {
                    try {
                        AppboyLogger.w(r.c, "Tried to add null AppboyEvent to pending dispatch.");
                        // monitorexit(this)
                        return;
                    }
                    finally {
                        break Label_0041;
                    }
                    break Label_0023;
                }
            }
            // monitorexit(this)
        }
        this.b.putIfAbsent(ca.d(), ca);
    }
    // monitorexit(this)
    
    public cw c() {
        final cw cw = this.f.poll();
        if (cw != null) {
            this.b(cw);
        }
        return cw;
    }
    
    boolean d() {
        return Appboy.getOutboundNetworkRequestsOffline();
    }
}
