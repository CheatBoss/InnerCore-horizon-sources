package com.amazon.device.iap.internal.b.e;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;
import com.amazon.device.iap.model.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public final class d extends b
{
    private static final String b;
    
    static {
        b = d.class.getSimpleName();
    }
    
    public d(final e e) {
        super(e, "1.0");
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws RemoteException, KiwiException {
        final String b = d.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("onSuccessInternal: result = ");
        sb.append(successResult);
        com.amazon.device.iap.internal.util.e.a(b, sb.toString());
        final Map data = successResult.getData();
        final String b2 = d.b;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("data: ");
        sb2.append(data);
        com.amazon.device.iap.internal.util.e.a(b2, sb2.toString());
        final String userId = data.get("userId");
        final e b3 = this.b();
        if (com.amazon.device.iap.internal.util.d.a(userId)) {
            b3.d().a(new UserDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(UserDataResponse.RequestStatus.FAILED).build());
            return false;
        }
        final UserData build = new UserDataBuilder().setUserId(userId).setMarketplace(d.a).build();
        final UserDataResponse build2 = new UserDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(UserDataResponse.RequestStatus.SUCCESSFUL).setUserData(build).build();
        b3.d().a("userId", build.getUserId());
        b3.d().a(build2);
        return true;
    }
}
