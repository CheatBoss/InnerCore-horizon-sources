package com.google.android.gms.common.internal;

import android.content.*;
import android.accounts.*;
import android.util.*;
import android.os.*;
import com.google.android.gms.common.*;

public class AccountAccessor extends Stub
{
    private Context mContext;
    private int zzqu;
    private Account zzs;
    
    public static Account getAccountBinderSafe(final IAccountAccessor accountAccessor) {
        if (accountAccessor != null) {
            final long clearCallingIdentity = Binder.clearCallingIdentity();
            try {
                try {
                    final Account account = accountAccessor.getAccount();
                    Binder.restoreCallingIdentity(clearCallingIdentity);
                    return account;
                }
                finally {}
            }
            catch (RemoteException ex) {
                Log.w("AccountAccessor", "Remote account accessor probably died");
                Binder.restoreCallingIdentity(clearCallingIdentity);
                return null;
            }
            Binder.restoreCallingIdentity(clearCallingIdentity);
        }
        return null;
    }
    
    public boolean equals(final Object o) {
        return this == o || (o instanceof AccountAccessor && this.zzs.equals((Object)((AccountAccessor)o).zzs));
    }
    
    @Override
    public Account getAccount() {
        final int callingUid = Binder.getCallingUid();
        if (callingUid == this.zzqu) {
            return this.zzs;
        }
        if (GooglePlayServicesUtilLight.isGooglePlayServicesUid(this.mContext, callingUid)) {
            this.zzqu = callingUid;
            return this.zzs;
        }
        throw new SecurityException("Caller is not GooglePlayServices");
    }
}
