package bo.app;

import org.json.*;
import java.util.*;
import com.appboy.support.*;

public final class es implements er
{
    private String a;
    private Set<String> b;
    
    public es(JSONObject jsonObject) {
        this.b = new HashSet<String>();
        jsonObject = jsonObject.getJSONObject("data");
        this.a = jsonObject.getString("id");
        final JSONArray optJSONArray = jsonObject.optJSONArray("buttons");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); ++i) {
                this.b.add(optJSONArray.getString(i));
            }
        }
    }
    
    public JSONObject a() {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("type", (Object)"iam_click");
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("id", (Object)this.a);
            if (this.b.size() > 0) {
                final JSONArray jsonArray = new JSONArray();
                final Iterator<String> iterator = this.b.iterator();
                while (iterator.hasNext()) {
                    jsonArray.put((Object)iterator.next());
                }
                jsonObject2.put("buttons", (Object)jsonArray);
            }
            jsonObject.put("data", (Object)jsonObject2);
            return jsonObject;
        }
        catch (JSONException ex) {
            return null;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        final boolean b = fk instanceof fm;
        final boolean b2 = false;
        if (b) {
            final fm fm = (fm)fk;
            if (!StringUtils.isNullOrBlank(fm.a()) && fm.a().equals(this.a)) {
                if (this.b.size() > 0) {
                    boolean b3 = b2;
                    if (!StringUtils.isNullOrBlank(fm.f())) {
                        b3 = b2;
                        if (this.b.contains(fm.f())) {
                            b3 = true;
                        }
                    }
                    return b3;
                }
                return StringUtils.isNullOrBlank(fm.f());
            }
        }
        return false;
    }
}
