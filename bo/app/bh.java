package bo.app;

import com.appboy.support.*;
import java.util.concurrent.*;
import java.util.*;

public class bh
{
    private static final String a;
    private final dm b;
    private boolean c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(bn.class);
    }
    
    public bh(final dm b) {
        this.c = false;
        this.b = b;
    }
    
    public void a() {
        this.c = true;
        this.b.b();
    }
    
    public void a(final ca ca) {
        if (this.c) {
            final String a = bh.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage manager is closed. Not adding event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.b.a(ca);
    }
    
    public void a(final Executor executor, final t t) {
        if (this.c) {
            AppboyLogger.w(bh.a, "Storage manager is closed. Not starting offline recovery.");
            return;
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                AppboyLogger.d(bh.a, "Started offline AppboyEvent recovery task.");
                final Iterator<ca> iterator = bh.this.b.a().iterator();
                while (iterator.hasNext()) {
                    t.a(iterator.next());
                }
            }
        });
    }
    
    public void b(final ca ca) {
        if (this.c) {
            final String a = bh.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage manager is closed. Not deleting event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.b.b(ca);
    }
}
