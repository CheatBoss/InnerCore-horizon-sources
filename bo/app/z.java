package bo.app;

import java.util.*;

public enum z
{
    a("unknown"), 
    b("none"), 
    c("2g"), 
    d("3g"), 
    e("4g"), 
    f("wifi");
    
    private static final Map<String, z> g;
    private final String h;
    
    static {
        g = new HashMap<String, z>();
        for (final z z : EnumSet.allOf(z.class)) {
            z.g.put(z.a(), z);
        }
    }
    
    private z(final String h) {
        this.h = h;
    }
    
    public String a() {
        return this.h;
    }
}
