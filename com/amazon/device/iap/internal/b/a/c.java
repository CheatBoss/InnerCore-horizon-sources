package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.internal.model.*;
import com.amazon.device.iap.model.*;

abstract class c extends i
{
    c(final e e, final String s) {
        super(e, "purchase_response", s);
    }
    
    protected void a(final String userId, final String marketplace, final String s, final PurchaseResponse.RequestStatus requestStatus) {
        final e b = this.b();
        b.d().a(new PurchaseResponseBuilder().setRequestId(b.c()).setRequestStatus(requestStatus).setUserData(new UserDataBuilder().setUserId(userId).setMarketplace(marketplace).build()).setReceipt(null).build());
    }
}
