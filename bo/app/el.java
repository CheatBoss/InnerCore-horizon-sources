package bo.app;

import android.content.*;
import com.appboy.support.*;
import org.json.*;
import com.appboy.models.*;

public class el extends en implements ek
{
    private static final String a;
    private IInAppMessage b;
    private br c;
    private String d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(el.class);
    }
    
    public el(JSONObject jsonObject, final br c) {
        super(jsonObject);
        final String a = el.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Parsing in-app message triggered action with JSON: ");
        sb.append(ec.a(jsonObject));
        AppboyLogger.d(a, sb.toString());
        jsonObject = jsonObject.getJSONObject("data");
        if (jsonObject != null) {
            this.c = c;
            this.b = eb.a(jsonObject, c);
            return;
        }
        AppboyLogger.w(el.a, "InAppMessageTriggeredAction Json did not contain in-app message.");
    }
    
    @Override
    public void a(final Context context, final ad ad, final fk fk, final long expirationTimestamp) {
        try {
            final String a = el.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Attempting to publish in-app message after delay of ");
            sb.append(this.c().d());
            sb.append(" seconds.");
            AppboyLogger.d(a, sb.toString());
            if (!StringUtils.isNullOrBlank(this.d)) {
                this.b.setLocalAssetPathForPrefetch(this.d);
            }
            this.b.setExpirationTimestamp(expirationTimestamp);
            ad.a(new ak(this, this.b, this.c.e()), ak.class);
        }
        catch (Exception ex) {
            AppboyLogger.w(el.a, "Caught exception while performing triggered action.", ex);
        }
    }
    
    @Override
    public void a(final String d) {
        this.d = d;
    }
    
    @Override
    public ga d() {
        if (StringUtils.isNullOrBlank(this.b.getRemoteAssetPathForPrefetch())) {
            return null;
        }
        if (this.b instanceof IInAppMessageHtml) {
            return new ga(fi.a, this.b.getRemoteAssetPathForPrefetch());
        }
        return new ga(fi.b, this.b.getRemoteAssetPathForPrefetch());
    }
    
    @Override
    public JSONObject e() {
        try {
            final JSONObject e = super.e();
            e.put("data", ((IPutIntoJson<Object>)this.b).forJsonPut());
            e.put("type", (Object)"inapp");
            return e;
        }
        catch (JSONException ex) {
            return null;
        }
    }
}
