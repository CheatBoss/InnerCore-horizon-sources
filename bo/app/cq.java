package bo.app;

import java.util.*;
import com.appboy.support.*;
import java.net.*;
import com.appboy.models.response.*;
import org.json.*;
import com.appboy.events.*;
import com.appboy.models.*;

public final class cq implements Runnable
{
    private static final String a;
    private final cw b;
    private final ad c;
    private final ad d;
    private final Map<String, String> e;
    private final g f;
    private final dl g;
    private final dr h;
    private final dh i;
    private final br j;
    
    static {
        a = AppboyLogger.getAppboyLogTag(cq.class);
    }
    
    public cq(final cw b, final d d, final g f, final ad c, final ad d2, final dl g, final br j, final dr h, final dh i) {
        this.b = b;
        this.c = c;
        this.d = d2;
        final Map<String, String> a = d.a();
        this.e = a;
        this.b.a(a);
        this.f = f;
        this.g = g;
        this.j = j;
        this.h = h;
        this.i = i;
    }
    
    private cm a() {
        final URI a = ea.a(this.b.a());
        final int n = cq$1.a[this.b.i().ordinal()];
        if (n == 1) {
            return new cm(this.f.a(a, this.e), this.b, this.j);
        }
        if (n != 2) {
            final String a2 = cq.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Received a request with an unknown Http verb: [");
            sb.append(this.b.i());
            sb.append("]");
            AppboyLogger.w(a2, sb.toString());
            return null;
        }
        final JSONObject g = this.b.g();
        if (g == null) {
            AppboyLogger.e(cq.a, "Could not parse request parameters for put request to [%s], canceling request.");
            return null;
        }
        return new cm(this.f.a(a, this.e, g), this.b, this.j);
    }
    
    private void a(final ResponseError responseError) {
        final String a = cq.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Received server error from request: ");
        sb.append(responseError.getMessage());
        AppboyLogger.e(a, sb.toString());
    }
    
    void a(final cm cm) {
        if (!cm.e()) {
            this.b.a(this.d, cm);
        }
        else {
            this.a(cm.n());
            this.b.a(this.d, cm.n());
        }
        this.b(cm);
        this.b.a(this.c);
    }
    
    void b(final cm cm) {
        final String e = this.j.e();
        if (cm.a()) {
            try {
                final FeedUpdatedEvent a = this.g.a(cm.h(), e);
                if (a != null) {
                    this.d.a(a, FeedUpdatedEvent.class);
                }
            }
            catch (JSONException ex2) {
                AppboyLogger.w(cq.a, "Unable to update/publish feed.");
            }
        }
        if (cm.g()) {
            try {
                final ContentCardsUpdatedEvent a2 = this.i.a(cm.m(), e);
                if (a2 != null) {
                    this.d.a(a2, ContentCardsUpdatedEvent.class);
                }
            }
            catch (JSONException ex) {
                AppboyLogger.e(cq.a, "Encountered JSON exception while parsing Content Cards update. Unable to publish Content Cards update event.", (Throwable)ex);
            }
        }
        if (cm.c()) {
            this.h.a(cm.j());
            this.c.a(new an(cm.j()), an.class);
        }
        if (cm.d()) {
            this.c.a(new au(cm.k()), au.class);
        }
        if (cm.b()) {
            final cw b = this.b;
            if (b instanceof dc) {
                final dc dc = (dc)b;
                final IInAppMessage i = cm.i();
                i.setExpirationTimestamp(dc.k());
                this.c.a(new ak(dc.l(), i, e), ak.class);
            }
        }
        if (cm.f()) {
            this.c.a(new aj(cm.l()), aj.class);
        }
    }
    
    @Override
    public void run() {
        try {
            final cm a = this.a();
            if (a != null) {
                this.a(a);
                this.c.a(new ah(this.b), ah.class);
                this.c.a(new af(this.b), af.class);
                return;
            }
            AppboyLogger.w(cq.a, "Api response was null, failing task.");
        }
        catch (Exception ex) {
            if (ex instanceof aw) {
                AppboyLogger.d(cq.a, "Experienced network communication exception processing API response. Sending network error event.");
                this.c.a(new ag(this.b), ag.class);
            }
            AppboyLogger.w(cq.a, "Experienced exception processing API response. Failing task.", ex);
        }
        this.b.a(this.d, new ResponseError("An error occurred during request processing, resulting in no valid response being received. Check the error log for more details."));
        this.c.a(new ae(this.b), ae.class);
    }
}
