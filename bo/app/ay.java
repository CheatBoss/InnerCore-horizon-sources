package bo.app;

import com.appboy.support.*;

public class ay implements UncaughtExceptionHandler
{
    private static final String a;
    private ad b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ay.class);
    }
    
    public ay(final ad b) {
        this.b = b;
    }
    
    public void a(final ad b) {
        this.b = b;
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        try {
            if (this.b != null) {
                AppboyLogger.w(ay.a, "Uncaught exception from thread. Publishing as Throwable event.", t);
                this.b.a(t, Throwable.class);
            }
        }
        catch (Exception ex) {
            AppboyLogger.w(ay.a, "Failed to log throwable.", ex);
        }
    }
}
