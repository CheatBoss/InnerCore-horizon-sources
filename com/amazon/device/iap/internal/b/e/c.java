package com.amazon.device.iap.internal.b.e;

import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import com.amazon.device.iap.internal.util.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;
import com.amazon.device.iap.model.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

public final class c extends b
{
    private static final String b;
    
    static {
        b = c.class.getSimpleName();
    }
    
    public c(final e e) {
        super(e, "2.0");
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws RemoteException, KiwiException {
        final String b = c.b;
        final StringBuilder sb = new StringBuilder();
        sb.append("onResult: result = ");
        sb.append(successResult);
        com.amazon.device.iap.internal.util.e.a(b, sb.toString());
        final Map data = successResult.getData();
        final String b2 = c.b;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("data: ");
        sb2.append(data);
        com.amazon.device.iap.internal.util.e.a(b2, sb2.toString());
        final String userId = data.get("userId");
        final String marketplace = data.get("marketplace");
        final e b3 = this.b();
        if (!com.amazon.device.iap.internal.util.d.a(userId) && !com.amazon.device.iap.internal.util.d.a(marketplace)) {
            final UserData build = new UserDataBuilder().setUserId(userId).setMarketplace(marketplace).build();
            final UserDataResponse build2 = new UserDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(UserDataResponse.RequestStatus.SUCCESSFUL).setUserData(build).build();
            b3.d().a("userId", build.getUserId());
            b3.d().a(build2);
            return true;
        }
        b3.d().a(new UserDataResponseBuilder().setRequestId(b3.c()).setRequestStatus(UserDataResponse.RequestStatus.FAILED).build());
        return false;
    }
}
