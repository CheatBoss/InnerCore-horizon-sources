package bo.app;

import com.appboy.support.*;
import java.util.*;
import org.json.*;

public abstract class en implements ek
{
    private static final String a;
    private final String b;
    private final fe c;
    private final List<er> d;
    private boolean e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(en.class);
    }
    
    protected en(final JSONObject jsonObject) {
        this.d = new ArrayList<er>();
        this.b = jsonObject.getString("id");
        this.c = new fg(jsonObject);
        final JSONArray jsonArray = jsonObject.getJSONArray("trigger_condition");
        if (jsonArray != null && jsonArray.length() > 0) {
            this.d.addAll(gb.a(jsonArray));
        }
        this.e = jsonObject.optBoolean("prefetch", true);
    }
    
    @Override
    public boolean a() {
        return this.e;
    }
    
    @Override
    public boolean a(final fk fk) {
        if (!this.i()) {
            final String a = en.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Triggered action ");
            sb.append(this.b);
            sb.append("not eligible to be triggered by ");
            sb.append(fk.b());
            sb.append(" event. Current device time outside triggered action time window.");
            AppboyLogger.d(a, sb.toString());
            return false;
        }
        final Iterator<er> iterator = this.d.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().a(fk)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String b() {
        return this.b;
    }
    
    @Override
    public fe c() {
        return this.c;
    }
    
    public JSONObject e() {
        try {
            final JSONObject jsonObject = this.c.forJsonPut();
            jsonObject.put("id", (Object)this.b);
            if (this.d != null) {
                final JSONArray jsonArray = new JSONArray();
                final Iterator<er> iterator = this.d.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put(iterator.next().forJsonPut());
                }
                jsonObject.put("trigger_condition", (Object)jsonArray);
                jsonObject.put("prefetch", this.e);
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    boolean i() {
        return this.j() && this.k();
    }
    
    boolean j() {
        return this.c.a() == -1L || du.a() > this.c.a();
    }
    
    boolean k() {
        return this.c.b() == -1L || du.a() < this.c.b();
    }
}
