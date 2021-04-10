package bo.app;

import com.appboy.support.*;
import org.json.*;
import android.content.*;

public class ds implements do
{
    private static final String a;
    private final SharedPreferences b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ds.class);
    }
    
    public ds(final Context context, final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.session_storage");
        sb.append(StringUtils.getCacheFileSuffix(context, s, s2));
        this.b = context.getSharedPreferences(sb.toString(), 0);
    }
    
    private void a(final JSONObject jsonObject) {
        if (!jsonObject.has("end_time")) {
            try {
                jsonObject.put("end_time", du.b());
            }
            catch (JSONException ex) {
                AppboyLogger.e(ds.a, "Failed to set end time to now for session json data");
            }
        }
    }
    
    @Override
    public cd a() {
        if (!this.b.contains("current_open_session")) {
            AppboyLogger.d(ds.a, "No stored open session in storage.");
            return null;
        }
        Object string;
        JSONObject jsonObject;
        try {
            string = this.b.getString("current_open_session", "");
            try {
                jsonObject = new JSONObject(this.b.getString((String)string, ""));
                try {
                    return new cd(jsonObject);
                }
                catch (JSONException ex) {}
            }
            catch (JSONException ex) {
                jsonObject = null;
            }
        }
        catch (JSONException ex) {
            string = (jsonObject = null);
        }
        final String a = ds.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Could not create new mutable session for open session with id: ");
        sb.append((String)string);
        sb.append(" and json data: ");
        sb.append(jsonObject);
        final JSONException ex;
        AppboyLogger.e(a, sb.toString(), (Throwable)ex);
        return null;
    }
    
    @Override
    public void a(final cd cd) {
        final String string = cd.a().toString();
        final JSONObject g = cd.g();
        final SharedPreferences$Editor edit = this.b.edit();
        this.a(g);
        edit.putString(string, g.toString());
        if (!cd.d()) {
            edit.putString("current_open_session", string);
        }
        else if (this.b.getString("current_open_session", "").equals(string)) {
            edit.remove("current_open_session");
        }
        edit.apply();
    }
    
    @Override
    public void b(final cd cd) {
        final String string = this.b.getString("current_open_session", (String)null);
        final String string2 = cd.a().toString();
        final SharedPreferences$Editor edit = this.b.edit();
        edit.remove(string2);
        if (string2.equals(string)) {
            edit.remove("current_open_session");
        }
        edit.apply();
    }
}
