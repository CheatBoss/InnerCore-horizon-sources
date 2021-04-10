package bo.app;

import com.appboy.support.*;
import java.util.*;
import org.json.*;

public abstract class ez implements er
{
    private static final String b;
    fa a;
    
    static {
        b = AppboyLogger.getAppboyLogTag(ez.class);
    }
    
    public ez(final JSONObject jsonObject) {
        final JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("property_filters");
        final ArrayList<eq> list = new ArrayList<eq>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            final JSONArray jsonArray2 = jsonArray.getJSONArray(i);
            final ArrayList<eq> list2 = new ArrayList<eq>();
            for (int j = 0; j < jsonArray2.length(); ++j) {
                list2.add(new eu(jsonArray2.getJSONObject(j)));
            }
            list.add(new fc(list2));
        }
        this.a = new fa(list);
    }
    
    public JSONObject a() {
        final JSONObject jsonObject = new JSONObject();
        try {
            final JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("property_filters", (Object)this.a.a());
            jsonObject.put("data", (Object)jsonObject2);
            return jsonObject;
        }
        catch (JSONException ex) {
            AppboyLogger.e(ez.b, "Caught exception creating Json.", (Throwable)ex);
            return jsonObject;
        }
    }
    
    @Override
    public boolean a(final fk fk) {
        return this.a.a(fk);
    }
}
