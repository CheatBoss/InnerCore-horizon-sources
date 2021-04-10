package bo.app;

import com.appboy.models.outgoing.*;
import com.appboy.support.*;
import android.net.*;
import java.util.*;
import com.appboy.models.response.*;
import com.appboy.events.*;
import org.json.*;

public final class ct extends cp
{
    private static final String b;
    private final Feedback c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(ct.class);
    }
    
    public ct(final String s, final Feedback c) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("data");
        super(Uri.parse(sb.toString()), null);
        this.c = c;
    }
    
    @Override
    public void a(final ad ad, final cm cm) {
        ad.a(new SubmitFeedbackSucceeded(this.c), SubmitFeedbackSucceeded.class);
    }
    
    @Override
    public void a(final ad ad, final ResponseError responseError) {
        super.a(ad, responseError);
        ad.a(new SubmitFeedbackFailed(this.c, responseError), SubmitFeedbackFailed.class);
    }
    
    @Override
    public JSONObject g() {
        final JSONObject g = super.g();
        if (g == null) {
            return null;
        }
        try {
            final JSONArray jsonArray = new JSONArray();
            jsonArray.put((Object)this.c.forJsonPut());
            g.put("feedback", (Object)jsonArray);
            return g;
        }
        catch (JSONException ex) {
            AppboyLogger.w(ct.b, "Experienced JSONException while retrieving parameters. Returning null.", (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public boolean h() {
        return false;
    }
    
    @Override
    public y i() {
        return y.b;
    }
}
