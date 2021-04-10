package bo.app;

import com.appboy.support.*;
import android.util.*;

public abstract class fr implements fk
{
    private static final String a;
    private long b;
    private long c;
    private ca d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(fr.class);
    }
    
    protected fr() {
        final long c = du.c();
        this.c = c;
        this.b = c / 1000L;
    }
    
    protected fr(final ca d) {
        this();
        this.d = d;
    }
    
    protected String a(final String s) {
        if (StringUtils.isNullOrBlank(s)) {
            return null;
        }
        try {
            return new String(Base64.decode(s, 0)).split("_")[0];
        }
        catch (Exception ex) {
            final String a = fr.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error decoding Base64 encoded campaign Id ");
            sb.append(s);
            AppboyLogger.e(a, sb.toString(), ex);
            return null;
        }
    }
    
    @Override
    public long c() {
        return this.b;
    }
    
    @Override
    public long d() {
        return this.c;
    }
    
    @Override
    public ca e() {
        return this.d;
    }
}
