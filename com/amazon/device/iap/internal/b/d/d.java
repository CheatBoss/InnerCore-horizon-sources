package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.c.*;
import org.json.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;

public final class d extends b
{
    private static final String b;
    private static final Date c;
    
    static {
        b = d.class.getSimpleName();
        c = new Date(0L);
    }
    
    public d(final e e) {
        super(e, "1.0", true);
    }
    
    @Override
    protected boolean a(SuccessResult successResult) throws Exception {
        final Map data = successResult.getData();
        final String b = d.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(b, sb.toString());
        final String userId = data.get("userId");
        final String s = data.get("requestId");
        final String marketplace = data.get("marketplace");
        final ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        final JSONArray jsonArray = new JSONArray((String)data.get("receipts"));
        final int n = 0;
        for (int i = 0; i < jsonArray.length(); ++i) {
            String s2 = null;
            StringBuilder sb2 = null;
            String s3 = null;
            try {
                final Receipt a = com.amazon.device.iap.internal.util.a.a(jsonArray.getJSONObject(i), userId, null);
                receipts.add(a);
                if (ProductType.ENTITLED == a.getProductType()) {
                    com.amazon.device.iap.internal.c.c.a().a(userId, a.getReceiptId(), a.getSku());
                }
                continue;
            }
            catch (com.amazon.device.iap.internal.b.d d) {
                final String b2 = d.b;
                new StringBuilder().append("fail to verify receipt, requestId:");
                d.a();
            }
            catch (com.amazon.device.iap.internal.b.a a2) {
                s2 = d.b;
                sb2 = new StringBuilder();
                sb2.append("fail to parse receipt, requestId:");
                s3 = a2.a();
            }
            finally {
                s2 = d.b;
                sb2 = new StringBuilder();
                sb2.append("fail to verify receipt, requestId:");
                final Throwable t;
                s3 = t.getMessage();
            }
            sb2.append(s3);
            com.amazon.device.iap.internal.util.e.b(s2, sb2.toString());
        }
        successResult = (SuccessResult)new JSONArray((String)data.get("revocations"));
        for (int j = n; j < ((JSONArray)successResult).length(); ++j) {
            try {
                final String string = ((JSONArray)successResult).getString(j);
                receipts.add(new ReceiptBuilder().setSku(string).setProductType(ProductType.ENTITLED).setPurchaseDate(null).setCancelDate(d.c).setReceiptId(com.amazon.device.iap.internal.c.c.a().a(userId, string)).build());
            }
            catch (JSONException ex) {
                final String b3 = d.b;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("fail to parse JSON[");
                sb3.append(j);
                sb3.append("] in \"");
                sb3.append(successResult);
                sb3.append("\"");
                com.amazon.device.iap.internal.util.e.b(b3, sb3.toString());
            }
        }
        final String s4 = data.get("cursor");
        final boolean equalsIgnoreCase = "true".equalsIgnoreCase(data.get("hasMore"));
        final e b4 = this.b();
        final PurchaseUpdatesResponse build = new PurchaseUpdatesResponseBuilder().setRequestId(b4.c()).setRequestStatus(PurchaseUpdatesResponse.RequestStatus.SUCCESSFUL).setUserData(new UserDataBuilder().setUserId(userId).setMarketplace(marketplace).build()).setReceipts(receipts).setHasMore(equalsIgnoreCase).build();
        build.getReceipts().addAll(com.amazon.device.iap.internal.c.a.a().b(build.getUserData().getUserId()));
        b4.d().a(build);
        b4.d().a("newCursor", s4);
        return true;
    }
}
