package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.b.*;
import org.json.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;

public final class c extends b
{
    private static final String b;
    
    static {
        b = c.class.getSimpleName();
    }
    
    public c(final e e, final boolean b) {
        super(e, "2.0", b);
    }
    
    private List<Receipt> a(final String s, String s2, final String s3) throws JSONException {
        final ArrayList<Receipt> list = new ArrayList<Receipt>();
        final JSONArray jsonArray = new JSONArray(s2);
        for (int i = 0; i < jsonArray.length(); ++i) {
            String s4 = null;
            StringBuilder sb = null;
            try {
                list.add(com.amazon.device.iap.internal.util.a.a(jsonArray.getJSONObject(i), s, s3));
                continue;
            }
            catch (d d) {
                final String b = c.b;
                new StringBuilder().append("fail to verify receipt, requestId:");
                s2 = d.a();
            }
            catch (com.amazon.device.iap.internal.b.a a) {
                s4 = c.b;
                sb = new StringBuilder();
                sb.append("fail to parse receipt, requestId:");
                s2 = a.a();
            }
            finally {
                s4 = c.b;
                sb = new StringBuilder();
                sb.append("fail to verify receipt, requestId:");
                final Throwable t;
                s2 = t.getMessage();
            }
            sb.append(s2);
            com.amazon.device.iap.internal.util.e.b(s4, sb.toString());
        }
        return list;
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws Exception {
        final Map data = successResult.getData();
        final String b = c.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(b, sb.toString());
        final String userId = data.get("userId");
        final String marketplace = data.get("marketplace");
        final List<Receipt> a = this.a(userId, data.get("receipts"), data.get("requestId"));
        final String s = data.get("cursor");
        final boolean booleanValue = Boolean.valueOf(data.get("hasMore"));
        final e b2 = this.b();
        final PurchaseUpdatesResponse build = new PurchaseUpdatesResponseBuilder().setRequestId(b2.c()).setRequestStatus(PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL).setUserData(new UserDataBuilder().setUserId(userId).setMarketplace(marketplace).build()).setReceipts(a).setHasMore(booleanValue).build();
        b2.d().a("newCursor", s);
        b2.d().a(build);
        return true;
    }
}
