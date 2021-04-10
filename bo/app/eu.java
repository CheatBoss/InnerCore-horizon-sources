package bo.app;

import com.appboy.support.*;
import java.io.*;
import java.util.*;
import org.json.*;
import com.appboy.models.outgoing.*;

public class eu implements eq
{
    private static final String a;
    private fh b;
    private String c;
    private int d;
    private Object e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(eu.class);
    }
    
    eu(final fh b, final String c, final int d) {
        this.b = b;
        this.c = c;
        this.d = d;
    }
    
    protected eu(final JSONObject jsonObject) {
        this(ec.a(jsonObject, "property_type", fh.class, fh.e), jsonObject.getString("property_key"), jsonObject.getInt("comparator"));
        if (jsonObject.has("property_value")) {
            Serializable e;
            if (this.b.equals(fh.a)) {
                e = jsonObject.getString("property_value");
            }
            else if (this.b.equals(fh.d)) {
                e = jsonObject.getBoolean("property_value");
            }
            else if (this.b.equals(fh.c)) {
                e = jsonObject.getDouble("property_value");
            }
            else {
                if (this.b.equals(fh.b)) {
                    this.e = jsonObject.getLong("property_value");
                }
                return;
            }
            this.e = e;
        }
    }
    
    private boolean a(final Object o) {
        if (!(o instanceof Integer) && !(o instanceof Double)) {
            return this.d == 2;
        }
        final double doubleValue = ((Number)o).doubleValue();
        final double doubleValue2 = ((Number)this.e).doubleValue();
        final int d = this.d;
        if (d == 1) {
            return doubleValue == doubleValue2;
        }
        if (d == 2) {
            return doubleValue != doubleValue2;
        }
        if (d != 3) {
            return d == 5 && doubleValue < doubleValue2;
        }
        return doubleValue > doubleValue2;
    }
    
    private boolean a(final Object o, final long n) {
        Date a;
        if (o instanceof String) {
            a = du.a((String)o, u.b);
        }
        else {
            a = null;
        }
        if (a == null) {
            return this.d == 2;
        }
        final long a2 = du.a(a);
        final long longValue = ((Number)this.e).longValue();
        final int d = this.d;
        if (d == 15) {
            return a2 < n + longValue;
        }
        if (d == 16) {
            return a2 > n + longValue;
        }
        switch (d) {
            default: {
                return false;
            }
            case 6: {
                return a2 <= n - longValue;
            }
            case 5: {
                return a2 < longValue;
            }
            case 4: {
                return a2 >= n - longValue;
            }
            case 3: {
                return a2 > longValue;
            }
            case 2: {
                return a2 != longValue;
            }
            case 1: {
                return a2 == longValue;
            }
        }
    }
    
    private boolean b(final Object o) {
        final boolean b = o instanceof Boolean;
        boolean b2 = false;
        if (!b) {
            if (this.d == 2) {
                b2 = true;
            }
            return b2;
        }
        final int d = this.d;
        if (d != 1) {
            return d == 2 && (o.equals(this.e) ^ true);
        }
        return o.equals(this.e);
    }
    
    private boolean c(final Object o) {
        final boolean b = o instanceof String;
        boolean b2 = false;
        if (!b) {
            final int d = this.d;
            if (d == 2 || d == 17) {
                b2 = true;
            }
            return b2;
        }
        final int d2 = this.d;
        if (d2 == 1) {
            return o.equals(this.e);
        }
        if (d2 == 2) {
            return o.equals(this.e) ^ true;
        }
        if (d2 != 10) {
            return d2 == 17 && (((String)o).matches((String)this.e) ^ true);
        }
        return ((String)o).matches((String)this.e);
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            if (!this.b.equals(fh.e)) {
                jsonObject.put("property_type", (Object)this.b.toString());
            }
            jsonObject.put("property_key", (Object)this.c);
            jsonObject.put("comparator", this.d);
            jsonObject.put("property_value", this.e);
            return jsonObject;
        }
        catch (JSONException ex) {
            AppboyLogger.e(eu.a, "Caught exception creating property filter Json.", (Throwable)ex);
            return jsonObject;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        final boolean b = fk instanceof fl;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        final AppboyProperties f = ((fl)fk).f();
        Object opt = null;
        while (true) {
            if (f != null) {
                while (true) {
                    try {
                        opt = f.forJsonPut().opt(this.c);
                        if (opt == null) {
                            if (this.d == 12 || this.d == 17) {
                                return true;
                            }
                            if (this.d == 2) {
                                return true;
                            }
                            return b2;
                        }
                        else {
                            if (this.d == 11) {
                                return true;
                            }
                            if (this.d == 12) {
                                return false;
                            }
                            final int n = eu$1.a[this.b.ordinal()];
                            if (n == 1) {
                                return this.c(opt);
                            }
                            if (n == 2) {
                                return this.b(opt);
                            }
                            if (n != 3) {
                                return n == 4 && this.a(opt);
                            }
                            return this.a(opt, fk.c());
                        }
                        final Exception ex;
                        AppboyLogger.e(eu.a, "Caught exception checking property filter condition.", ex);
                        return false;
                    }
                    catch (Exception ex) {
                        continue;
                    }
                    break;
                }
                b2 = true;
                return b2;
            }
            continue;
        }
    }
}
