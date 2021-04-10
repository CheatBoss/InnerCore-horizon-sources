package com.amazon.device.iap.internal.b.d;

import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.internal.b.e.*;
import com.amazon.device.iap.model.*;
import java.util.*;
import com.amazon.device.iap.internal.model.*;

public final class a extends e
{
    public a(final RequestId requestId, final boolean b) {
        super(requestId);
        final c c = new c(this);
        c.a(new com.amazon.device.iap.internal.b.d.c(this, b));
        final d d = new d(this);
        d.a(new com.amazon.device.iap.internal.b.d.d(this));
        c.b(d);
        this.a(c);
    }
    
    @Override
    public void a() {
        final PurchaseUpdatesResponse purchaseUpdatesResponse = (PurchaseUpdatesResponse)this.d().a();
        i i;
        if (purchaseUpdatesResponse.getReceipts() != null && purchaseUpdatesResponse.getReceipts().size() > 0) {
            final HashSet<String> set = new HashSet<String>();
            for (final Receipt receipt : purchaseUpdatesResponse.getReceipts()) {
                if (!com.amazon.device.iap.internal.util.d.a(receipt.getReceiptId())) {
                    set.add(receipt.getReceiptId());
                }
            }
            i = new com.amazon.device.iap.internal.b.g.a(this, set, com.amazon.device.iap.internal.model.a.a.toString());
        }
        else {
            i = null;
        }
        this.a(purchaseUpdatesResponse, i);
    }
    
    @Override
    public void b() {
        final Object a = this.d().a();
        PurchaseUpdatesResponse build;
        if (a != null && a instanceof PurchaseUpdatesResponse) {
            build = (PurchaseUpdatesResponse)a;
        }
        else {
            build = new PurchaseUpdatesResponseBuilder().setRequestId(this.c()).setRequestStatus(PurchaseUpdatesResponse.RequestStatus.FAILED).build();
        }
        this.a(build);
    }
}
