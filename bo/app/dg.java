package bo.app;

import com.appboy.support.*;
import java.util.*;
import java.util.concurrent.*;

public class dg implements dm
{
    private static final String a;
    private final dm b;
    private final az c;
    private boolean d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dg.class);
    }
    
    public dg(final dm b, final az c) {
        this.d = false;
        this.b = b;
        this.c = c;
    }
    
    @Override
    public Collection<ca> a() {
        synchronized (this) {
            if (this.d) {
                AppboyLogger.w(dg.a, "Storage provider is closed. Not getting all events.");
                return null;
            }
            try {
                return this.c.submit((Callable<Collection<ca>>)new Callable<Collection<ca>>() {
                    public Collection<ca> a() {
                        return dg.this.b.a();
                    }
                }).get();
            }
            catch (Exception ex) {
                throw new RuntimeException("Error while trying to asynchronously get all events.", ex);
            }
        }
    }
    
    @Override
    public void a(final ca ca) {
        if (this.d) {
            final String a = dg.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not adding event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.c.execute(new Runnable() {
            @Override
            public void run() {
                dg.this.b.a(ca);
            }
        });
    }
    
    @Override
    public void b() {
        synchronized (this) {
            AppboyLogger.w(dg.a, "Setting this provider and internal storage provider to closed. Cancelling all queued storage provider work.");
            this.d = true;
            this.b.b();
            this.c.shutdownNow();
        }
    }
    
    @Override
    public void b(final ca ca) {
        if (this.d) {
            final String a = dg.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not deleting event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.c.execute(new Runnable() {
            @Override
            public void run() {
                dg.this.b.b(ca);
            }
        });
    }
}
