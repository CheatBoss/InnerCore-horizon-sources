package bo.app;

import com.appboy.support.*;

public class dk implements do
{
    private static final String a;
    private final do b;
    private final ad c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dk.class);
    }
    
    public dk(final do b, final ad c) {
        this.b = b;
        this.c = c;
    }
    
    @Override
    public cd a() {
        try {
            return this.b.a();
        }
        catch (Exception ex) {
            AppboyLogger.e(dk.a, "Failed to get the active session from the storage.", ex);
            this.a(this.c, ex);
            return null;
        }
    }
    
    void a(final ad ad, final Throwable t) {
        try {
            ad.a(new av("A database exception has occurred. Please view the stack trace for more details.", t), av.class);
        }
        catch (Exception ex) {
            AppboyLogger.e(dk.a, "Failed to log throwable.", ex);
        }
    }
    
    @Override
    public void a(final cd cd) {
        try {
            this.b.a(cd);
        }
        catch (Exception ex) {
            AppboyLogger.e(dk.a, "Failed to upsert active session in the storage.", ex);
            this.a(this.c, ex);
        }
    }
    
    @Override
    public void b(final cd cd) {
        try {
            this.b.b(cd);
        }
        catch (Exception ex) {
            AppboyLogger.e(dk.a, "Failed to delete the sealed session from the storage.", ex);
            this.a(this.c, ex);
        }
    }
}
