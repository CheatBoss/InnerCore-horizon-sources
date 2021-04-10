package com.amazon.device.iap.internal.b.g;

import com.amazon.device.iap.model.*;
import java.util.*;
import com.amazon.device.iap.internal.b.*;

public final class b extends e
{
    private final String a;
    private final FulfillmentResult b;
    
    public b(final RequestId requestId, final String a, final FulfillmentResult b) {
        super(requestId);
        final HashSet<String> set = new HashSet<String>();
        set.add(a);
        this.a = a;
        this.b = b;
        this.a(new a(this, set, b.toString()));
    }
    
    @Override
    public void a() {
    }
    
    @Override
    public void b() {
        if (FulfillmentResult.FULFILLED == this.b || FulfillmentResult.UNAVAILABLE == this.b) {
            final String c = com.amazon.device.iap.internal.c.a.a().c(this.a);
            if (c != null) {
                new com.amazon.device.iap.internal.b.f.b(this, c).a_();
                com.amazon.device.iap.internal.c.a.a().a(this.a);
            }
        }
    }
}
