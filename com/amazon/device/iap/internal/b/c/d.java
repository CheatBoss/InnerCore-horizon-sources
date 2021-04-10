package com.amazon.device.iap.internal.b.c;

import java.util.*;
import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;

public final class d extends e
{
    public d(final RequestId requestId, final Set<String> set) {
        super(requestId);
        final a a = new a(this, set);
        a.b(new b(this, set));
        this.a(a);
    }
    
    @Override
    public void a() {
        this.a(this.d().a());
    }
    
    @Override
    public void b() {
        ProductDataResponse build;
        if ((build = (ProductDataResponse)this.d().a()) == null) {
            build = new ProductDataResponseBuilder().setRequestId(this.c()).setRequestStatus(ProductDataResponse.RequestStatus.FAILED).build();
        }
        this.a(build);
    }
}
