package bo.app;

import com.appboy.models.*;
import com.appboy.support.*;
import java.io.*;
import org.json.*;

public final class ci implements cc, IPutIntoJson<JSONObject>
{
    private static final String a;
    private final String b;
    private final String c;
    private final String d;
    private final String e;
    private final String f;
    private String g;
    private final Boolean h;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ci.class);
    }
    
    public ci(final String b, final String c, final String d, final String e, final String g, final String f, final Boolean h) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.g = g;
        this.f = f;
        this.h = h;
    }
    
    public static ci a(final JSONObject jsonObject) {
        final v[] values = v.values();
        final int length = values.length;
        int i = 0;
        Boolean b = null;
        Boolean b2;
        Serializable s = b2 = b;
        Boolean b3;
        Serializable s2 = b3 = b2;
        Boolean value;
        Serializable s3 = value = b3;
        while (i < length) {
            final v v = values[i];
            Serializable emptyToNull = null;
            String emptyToNull2 = null;
            Serializable emptyToNull3 = null;
            String emptyToNull4 = null;
            Serializable emptyToNull5 = null;
            String emptyToNull6 = null;
            switch (ci$1.a[v.ordinal()]) {
                default: {
                    final String a = ci.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Unknown key encountered in Device createFromJson ");
                    sb.append(v);
                    AppboyLogger.e(a, sb.toString());
                    emptyToNull = b;
                    emptyToNull2 = (String)s;
                    emptyToNull3 = b2;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    break;
                }
                case 7: {
                    emptyToNull = b;
                    emptyToNull2 = (String)s;
                    emptyToNull3 = b2;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    if (jsonObject.has(v.a())) {
                        value = jsonObject.optBoolean(v.a(), true);
                        emptyToNull = b;
                        emptyToNull2 = (String)s;
                        emptyToNull3 = b2;
                        emptyToNull4 = (String)s2;
                        emptyToNull5 = b3;
                        emptyToNull6 = (String)s3;
                        break;
                    }
                    break;
                }
                case 6: {
                    emptyToNull3 = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull = b;
                    emptyToNull2 = (String)s;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    break;
                }
                case 5: {
                    emptyToNull4 = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull = b;
                    emptyToNull2 = (String)s;
                    emptyToNull3 = b2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    break;
                }
                case 4: {
                    emptyToNull6 = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull = b;
                    emptyToNull2 = (String)s;
                    emptyToNull3 = b2;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    break;
                }
                case 3: {
                    emptyToNull = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull2 = (String)s;
                    emptyToNull3 = b2;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    break;
                }
                case 2: {
                    emptyToNull2 = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull = b;
                    emptyToNull3 = b2;
                    emptyToNull4 = (String)s2;
                    emptyToNull5 = b3;
                    emptyToNull6 = (String)s3;
                    break;
                }
                case 1: {
                    emptyToNull5 = StringUtils.emptyToNull(jsonObject.optString(v.a()));
                    emptyToNull6 = (String)s3;
                    emptyToNull4 = (String)s2;
                    emptyToNull3 = b2;
                    emptyToNull2 = (String)s;
                    emptyToNull = b;
                    break;
                }
            }
            ++i;
            b = (Boolean)emptyToNull;
            s = emptyToNull2;
            b2 = (Boolean)emptyToNull3;
            s2 = emptyToNull4;
            b3 = (Boolean)emptyToNull5;
            s3 = emptyToNull6;
        }
        return new ci((String)b, (String)s, (String)b2, (String)s2, (String)b3, (String)s3, value);
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt(v.a.a(), (Object)this.b);
            jsonObject.putOpt(v.b.a(), (Object)this.c);
            jsonObject.putOpt(v.c.a(), (Object)this.d);
            jsonObject.putOpt(v.d.a(), (Object)this.f);
            jsonObject.putOpt(v.e.a(), (Object)this.e);
            jsonObject.putOpt(v.g.a(), (Object)this.h);
            if (!StringUtils.isNullOrBlank(this.g)) {
                jsonObject.put(v.f.a(), (Object)this.g);
                return jsonObject;
            }
        }
        catch (JSONException ex) {
            AppboyLogger.e(ci.a, "Caught exception creating device Json.", (Throwable)ex);
        }
        return jsonObject;
    }
    
    @Override
    public boolean b() {
        return this.a().length() == 0;
    }
}
