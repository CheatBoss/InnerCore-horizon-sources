package bo.app;

public enum u
{
    a("yyyy-MM-dd"), 
    b("yyyy-MM-dd kk:mm:ss"), 
    c("MM-dd kk:mm:ss.SSS");
    
    private final String d;
    
    private u(final String d) {
        this.d = d;
    }
    
    public String a() {
        return this.d;
    }
}
