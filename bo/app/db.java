package bo.app;

import java.util.concurrent.*;

public class db implements cy
{
    private final d a;
    private final g b;
    private final ad c;
    private final ad d;
    private final Executor e;
    private final dl f;
    private final dr g;
    private final dh h;
    private br i;
    
    public db(final d a, final g b, final ad c, final ad d, final Executor e, final dl f, final dr g, final dh h) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
    }
    
    private cq a(final cw cw) {
        return new cq(cw, this.a, this.b, this.c, this.d, this.f, this.i, this.g, this.h);
    }
    
    public void a(final br i) {
        this.i = i;
    }
    
    @Override
    public void a(final cx cx) {
        this.e.execute(this.a((cw)cx));
    }
    
    @Override
    public void b(final cx cx) {
        this.a((cw)cx).run();
    }
}
