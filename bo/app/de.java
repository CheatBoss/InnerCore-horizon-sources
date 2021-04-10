package bo.app;

import com.appboy.support.*;

public abstract class de<T>
{
    private static final String a;
    private final Object b;
    private boolean c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(de.class);
    }
    
    public de() {
        this.b = new Object();
        this.c = false;
    }
    
    abstract T a();
    
    abstract void a(final T p0, final boolean p1);
    
    public T b() {
        synchronized (this.b) {
            if (this.c) {
                AppboyLogger.d(de.a, "Received call to export dirty object, but the cache was already locked.", false);
                return null;
            }
            this.c = true;
            return this.a();
        }
    }
    
    public boolean b(final T t, final boolean b) {
        synchronized (this.b) {
            if (!this.c) {
                final String a = de.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Tried to confirm outboundObject [");
                sb.append(t);
                sb.append("] with success [");
                sb.append(b);
                sb.append("], but the cache wasn't locked, so not doing anything.");
                AppboyLogger.w(a, sb.toString());
                return false;
            }
            this.a(t, b);
            this.c = false;
            synchronized (this) {
                AppboyLogger.i(de.a, "Notifying confirmAndUnlock listeners", false);
                this.notifyAll();
                return true;
            }
        }
    }
    
    public boolean c() {
        synchronized (this.b) {
            return this.c;
        }
    }
}
