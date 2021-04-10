package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.util.*;
import org.json.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;
import com.amazon.device.iap.model.*;

public final class a extends c
{
    private static final String a;
    
    static {
        a = a.class.getSimpleName();
    }
    
    public a(final e e) {
        super(e, "2.0");
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws Exception {
        final Map data = successResult.getData();
        final String a = com.amazon.device.iap.internal.b.a.a.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        final String s = this.getCommandData().get("requestId");
        final String userId = data.get("userId");
        final String marketplace = data.get("marketplace");
        final String s2 = data.get("receipt");
        Label_0101: {
            if (!com.amazon.device.iap.internal.util.d.a(s2)) {
                final Receipt receipt = null;
                final JSONObject jsonObject = new JSONObject(s2);
                final PurchaseResponse.RequestStatus safeValue = PurchaseResponse.RequestStatus.safeValueOf(jsonObject.getString("orderStatus"));
                if (safeValue == PurchaseResponse.RequestStatus.SUCCESSFUL) {
                    try {
                        com.amazon.device.iap.internal.util.a.a(jsonObject, userId, s);
                    }
                    finally {
                        break Label_0101;
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
}
