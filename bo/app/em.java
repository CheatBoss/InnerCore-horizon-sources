package bo.app;

import android.content.*;
import com.appboy.support.*;
import org.json.*;

public class em extends en implements ek
{
    private static final String a;
    private br b;
    private String c;
    private String d;
    private String e;
    private String f;
    private long g;
    
    static {
        a = AppboyLogger.getAppboyLogTag(em.class);
    }
    
    public em(JSONObject jsonObject, final br b) {
        super(jsonObject);
        this.g = -1L;
        final String a = em.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Parsing templated triggered action with JSON: ");
        sb.append(ec.a(jsonObject));
        AppboyLogger.d(a, sb.toString());
        jsonObject = jsonObject.getJSONObject("data");
        this.c = jsonObject.getString("trigger_id");
        final JSONArray optJSONArray = jsonObject.optJSONArray("prefetch_image_urls");
        if (optJSONArray != null) {
            this.d = optJSONArray.getString(0);
        }
        final JSONArray optJSONArray2 = jsonObject.optJSONArray("prefetch_zip_urls");
        if (optJSONArray2 != null) {
            this.e = optJSONArray2.getString(0);
        }
        this.b = b;
    }
    
    @Override
    public void a(final Context context, final ad ad, final fk fk, final long g) {
        if (this.b != null) {
            this.g = g;
            final String a = em.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Posting templating request after delay of ");
            sb.append(this.c().d());
            sb.append(" seconds.");
            AppboyLogger.d(a, sb.toString());
            this.b.a(this, fk);
        }
    }
    
    @Override
    public void a(final String f) {
        this.f = f;
    }
    
    @Override
    public ga d() {
        if (!StringUtils.isNullOrBlank(this.d)) {
            return new ga(fi.b, this.d);
        }
        if (!StringUtils.isNullOrBlank(this.e)) {
            return new ga(fi.a, this.e);
        }
        return null;
    }
    
    @Override
    public JSONObject e() {
        try {
            final JSONObject e = super.e();
            e.put("type", (Object)"templated_iam");
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("trigger_id", (Object)this.c);
            final JSONArray jsonArray = new JSONArray();
            if (!StringUtils.isNullOrBlank(this.d)) {
                jsonArray.put((Object)this.d);
                jsonObject.put("prefetch_image_urls", (Object)jsonArray);
            }
            final JSONArray jsonArray2 = new JSONArray();
            if (!StringUtils.isNullOrBlank(this.e)) {
                jsonArray2.put((Object)this.e);
                jsonObject.put("prefetch_zip_urls", (Object)jsonArray2);
            }
            e.put("data", (Object)jsonObject);
            return e;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    public long f() {
        return this.g;
    }
    
    public String g() {
        return this.c;
    }
    
    public String h() {
        return this.f;
    }
}
