package bo.app;

import java.util.*;
import org.json.*;

public class gh
{
    private boolean a;
    private StringBuilder b;
    private String c;
    private Object d;
    private Object e;
    private final List<gd> f;
    private final List<gd> g;
    private final List<gd> h;
    
    public gh() {
        this(true, null);
    }
    
    private gh(final boolean a, final String s) {
        this.f = new ArrayList<gd>();
        this.g = new ArrayList<gd>();
        this.h = new ArrayList<gd>();
        this.a = a;
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        this.b = new StringBuilder(s2);
    }
    
    private static String a(final Object o) {
        if (o instanceof JSONArray) {
            return "a JSON array";
        }
        if (o instanceof JSONObject) {
            return "a JSON object";
        }
        return o.toString();
    }
    
    private String b(final String s, final Object o, final Object o2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("\nExpected: ");
        sb.append(a(o));
        sb.append("\n     got: ");
        sb.append(a(o2));
        sb.append("\n");
        return sb.toString();
    }
    
    private String c(final String s, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("\nExpected: ");
        sb.append(a(o));
        sb.append("\n     but none found\n");
        return sb.toString();
    }
    
    private String d(final String s, final Object o) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("\nUnexpected: ");
        sb.append(a(o));
        sb.append("\n");
        return sb.toString();
    }
    
    public gh a(final String s, final Object o) {
        this.g.add(new gd(s, o, null));
        this.a(this.c(s, o));
        return this;
    }
    
    public gh a(final String c, final Object d, final Object e) {
        this.f.add(new gd(c, d, e));
        this.c = c;
        this.d = d;
        this.e = e;
        this.a(this.b(c, d, e));
        return this;
    }
    
    public void a(final String s) {
        this.a = false;
        StringBuilder sb;
        if (this.b.length() == 0) {
            sb = this.b;
        }
        else {
            sb = this.b;
            sb.append(" ; ");
        }
        sb.append(s);
    }
    
    public boolean a() {
        return this.a;
    }
    
    public gh b(final String s, final Object o) {
        this.h.add(new gd(s, null, o));
        this.a(this.d(s, o));
        return this;
    }
    
    public boolean b() {
        return this.a ^ true;
    }
    
    public String c() {
        return this.b.toString();
    }
    
    @Override
    public String toString() {
        return this.b.toString();
    }
}
