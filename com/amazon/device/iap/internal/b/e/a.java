package com.amazon.device.iap.internal.b.e;

import com.amazon.device.iap.internal.b.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;

public final class a extends e
{
    public a(final RequestId requestId) {
        super(requestId);
        final c c = new c(this);
        c.b(new d(this));
        this.a(c);
    }
    
    @Override
    public void a() {
        this.a(this.d().a());
    }
    
    @Override
    public void b() {
        UserDataResponse build;
        if ((build = (UserDataResponse)this.d().a()) == null) {
            build = new UserDataResponseBuilder().setRequestId(this.c()).setRequestStatus(UserDataResponse.RequestStatus.FAILED).build();
        }
        this.a(build);
    }
}
