package com.amazon.device.iap.internal.b.a;

import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.internal.b.f.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;

public final class d extends e
{
    public d(final RequestId requestId) {
        super(requestId);
        final a a = new a(this);
        a.b(new b(this));
        this.a(a);
    }
    
    @Override
    public void a() {
        final PurchaseResponse purchaseResponse = (PurchaseResponse)this.d().a();
        if (purchaseResponse == null) {
            return;
        }
        final Receipt receipt = purchaseResponse.getReceipt();
        final boolean b = receipt != null;
        final c c = new c(this, b);
        if (b && (ProductType.ENTITLED == receipt.getProductType() || ProductType.SUBSCRIPTION == receipt.getProductType())) {
            c.b(new com.amazon.device.iap.internal.b.f.b(this, this.c().toString()));
        }
        this.a(purchaseResponse, c);
    }
    
    @Override
    public void b() {
        PurchaseResponse build;
        if ((build = (PurchaseResponse)this.d().a()) == null) {
            build = new PurchaseResponseBuilder().setRequestId(this.c()).setRequestStatus(PurchaseResponse.RequestStatus.FAILED).build();
        }
        this.a(build, new c(this, false));
    }
}
