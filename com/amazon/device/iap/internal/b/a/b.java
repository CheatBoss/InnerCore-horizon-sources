package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.*;
import org.json.*;
import com.amazon.device.iap.internal.c.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;
import com.amazon.device.iap.model.*;

public final class b extends c
{
    private static final String a;
    
    static {
        a = b.class.getSimpleName();
    }
    
    public b(final e e) {
        super(e, "1.0");
    }
    
    private void a(final String s, String a, final String s2) {
        if (s != null && a != null) {
            if (s2 == null) {
                return;
            }
            try {
                final JSONObject jsonObject = new JSONObject(s2);
                if (PurchaseResponse.RequestStatus.safeValueOf(jsonObject.getString("orderStatus")) == PurchaseResponse.RequestStatus.SUCCESSFUL) {
                    a.a().a(s, a, com.amazon.device.iap.internal.util.a.a(jsonObject, a, s).getReceiptId(), s2);
                }
            }
            finally {
                a = b.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Error in savePendingReceipt: ");
                final Throwable t;
                sb.append(t);
                com.amazon.device.iap.internal.util.e.b(a, sb.toString());
            }
        }
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws Exception {
        final Map data = successResult.getData();
        final String a = b.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        final String s = this.getCommandData().get("requestId");
        final String userId = data.get("userId");
        final String marketplace = data.get("marketplace");
        final String s2 = data.get("receipt");
        if (s != null && com.amazon.device.iap.internal.c.b.a().a(s)) {
            Label_0121: {
                if (!com.amazon.device.iap.internal.util.d.a(s2)) {
                    final Receipt receipt = null;
                    final JSONObject jsonObject = new JSONObject(s2);
                    final PurchaseResponse.RequestStatus safeValue = PurchaseResponse.RequestStatus.safeValueOf(jsonObject.getString("orderStatus"));
                    if (safeValue == PurchaseResponse.RequestStatus.SUCCESSFUL) {
                        try {
                            if (ProductType.CONSUMABLE == com.amazon.device.iap.internal.util.a.a(jsonObject, userId, s).getProductType()) {
                                this.a(s, userId, s2);
                            }
                        }
                        finally {
                            break Label_0121;
                        }
                    }
                    final e b = this.b();
                    b.d().a(new PurchaseResponseBuilder().setRequestId(b.c()).setRequestStatus(safeValue).setUserData(new UserDataBuilder().setUserId(userId).setMarketplace(marketplace).build()).setReceipt(receipt).build());
                    return true;
                }
            }
            this.a(userId, marketplace, s, PurchaseResponse.RequestStatus.FAILED);
            return false;
        }
        this.b().d().b();
        return true;
    }
}
