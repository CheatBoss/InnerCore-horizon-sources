package bo.app;

public class fp extends fr implements fk
{
    private String a;
    
    public fp(final String s, final ca ca) {
        super(ca);
        this.a = this.a(s);
    }
    
    public String a() {
        return this.a;
    }
    
    @Override
    public String b() {
        return "push_click";
    }
}
