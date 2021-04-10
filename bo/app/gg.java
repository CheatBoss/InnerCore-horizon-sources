package bo.app;

public enum gg
{
    a(false, true), 
    b(true, false), 
    c(false, false), 
    d(true, true);
    
    private final boolean e;
    private final boolean f;
    
    private gg(final boolean e, final boolean f) {
        this.e = e;
        this.f = f;
    }
    
    public boolean a() {
        return this.e;
    }
    
    public boolean b() {
        return this.f;
    }
}
