package bo.app;

public class fm extends fr implements fk
{
    private String a;
    private String b;
    
    public fm(final String s) {
        this.a = this.a(s);
    }
    
    public fm(final String s, final String b) {
        this(s);
        this.b = b;
    }
    
    public String a() {
        return this.a;
    }
    
    @Override
    public String b() {
        return "iam_click";
    }
    
    public String f() {
        return this.b;
    }
}
