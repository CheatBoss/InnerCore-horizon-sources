package bo.app;

import com.appboy.support.*;

public class cz implements cy
{
    private static final String a;
    private final ad b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cz.class);
    }
    
    public cz(final ad b) {
        this.b = b;
    }
    
    private void c(final cx cx) {
        AppboyLogger.i(cz.a, "Short circuiting execution of network request and immediately marking it as succeeded.", false);
        cx.a(this.b, (cm)null);
        cx.a(this.b);
        this.b.a(new af((cw)cx), af.class);
    }
    
    @Override
    public void a(final cx cx) {
        this.c(cx);
    }
    
    @Override
    public void b(final cx cx) {
        this.c(cx);
    }
}
