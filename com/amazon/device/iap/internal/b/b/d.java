package com.amazon.device.iap.internal.b.b;

import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;

public final class d extends e
{
    public d(final RequestId requestId, final String s) {
        super(requestId);
        final c c = new c(this, s);
        c.b(new b(this, s));
        this.a(c);
    }
    
    @Override
    public void a() {
    }
    
    @Override
    public void b() {
        PurchaseResponse build;
        if ((build = (PurchaseResponse)this.d().a()) == null) {
            build = new PurchaseResponseBuilder().setRequestId(this.c()).setRequestStatus(PurchaseResponse.RequestStatus.FAILED).build();
        }
        this.a(build);
    }
}
