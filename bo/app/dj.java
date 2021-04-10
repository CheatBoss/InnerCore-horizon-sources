package bo.app;

import com.appboy.support.*;
import java.util.*;

public class dj implements dm
{
    private static final String a;
    private final dm b;
    private final ad c;
    private boolean d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dj.class);
    }
    
    public dj(final dm b, final ad c) {
        this.d = false;
        this.b = b;
        this.c = c;
    }
    
    private void a(final ad ad, final Throwable t) {
        try {
            ad.a(new av("A database exception has occurred. Please view the stack trace for more details.", t), av.class);
        }
        catch (Exception ex) {
            AppboyLogger.e(dj.a, "Failed to log throwable.", ex);
        }
    }
    
    @Override
    public Collection<ca> a() {
        if (this.d) {
            AppboyLogger.w(dj.a, "Storage provider is closed. Not getting all events.");
        }
        else {
            try {
                return this.b.a();
            }
            catch (Exception ex) {
                AppboyLogger.e(dj.a, "Failed to get all events from storage.", ex);
                this.a(this.c, ex);
            }
        }
        return (Collection<ca>)Collections.emptyList();
    }
    
    @Override
    public void a(final ca ca) {
        if (this.d) {
            final String a = dj.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not adding event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        try {
            this.b.a(ca);
        }
        catch (Exception ex) {
            AppboyLogger.e(dj.a, "Failed to insert event into storage.", ex);
            this.a(this.c, ex);
        }
    }
    
    @Override
    public void b() {
        AppboyLogger.w(dj.a, "Setting this provider and internal storage provider to closed.");
        this.d = true;
        this.b.b();
    }
    
    @Override
    public void b(final ca ca) {
        if (this.d) {
            final String a = dj.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not deleting event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        try {
            this.b.b(ca);
        }
        catch (Exception ex) {
            AppboyLogger.e(dj.a, "Failed to delete event from storage.", ex);
            this.a(this.c, ex);
        }
    }
}
