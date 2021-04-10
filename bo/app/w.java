package bo.app;

import com.appboy.models.*;
import java.util.*;

public enum w implements IPutIntoJson<String>
{
    A("se"), 
    B("tt"), 
    C("pd"), 
    D("lcaa"), 
    E("lcar");
    
    private static final Map<String, w> G;
    
    a("lr"), 
    b("ce"), 
    c("p"), 
    d("cic"), 
    e("pc"), 
    f("ca"), 
    g("i"), 
    h("ie"), 
    i("ci"), 
    j("cc"), 
    k("g"), 
    l("ccc"), 
    m("cci"), 
    n("ccic"), 
    o("ccd"), 
    p("inc"), 
    q("add"), 
    r("rem"), 
    s("set"), 
    t("si"), 
    u("iec"), 
    v("sc"), 
    w("sbc"), 
    x("sfe"), 
    y("uae"), 
    z("ss");
    
    private final String F;
    
    static {
        int n2 = 0;
        final HashMap<String, w> hashMap = new HashMap<String, w>();
        for (w[] values = values(); n2 < values.length; ++n2) {
            final w w2 = values[n2];
            hashMap.put(w2.F, w2);
        }
        G = new HashMap<String, w>(hashMap);
    }
    
    private w(final String f) {
        this.F = f;
    }
    
    public static w a(final String s) {
        if (w.G.containsKey(s)) {
            return w.G.get(s);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown String Value: ");
        sb.append(s);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static boolean a(final w w) {
        return b(w) || w.equals(w.f) || w.equals(w.d);
    }
    
    public static boolean b(final w w) {
        return w.equals(w.e);
    }
    
    public String a() {
        return this.F;
    }
}
