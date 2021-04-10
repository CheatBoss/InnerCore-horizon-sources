package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.*;
import android.content.*;
import com.google.android.gms.common.*;

final class zzm implements Runnable
{
    private final zzl zzev;
    final /* synthetic */ zzk zzew;
    
    zzm(final zzk zzew, final zzl zzev) {
        this.zzew = zzew;
        this.zzev = zzev;
    }
    
    @Override
    public final void run() {
        if (!this.zzew.mStarted) {
            return;
        }
        final ConnectionResult connectionResult = this.zzev.getConnectionResult();
        if (connectionResult.hasResolution()) {
            this.zzew.mLifecycleFragment.startActivityForResult(GoogleApiActivity.zza((Context)this.zzew.getActivity(), connectionResult.getResolution(), this.zzev.zzu(), false), 1);
            return;
        }
        if (this.zzew.zzdg.isUserResolvableError(connectionResult.getErrorCode())) {
            this.zzew.zzdg.showErrorDialogFragment(this.zzew.getActivity(), this.zzew.mLifecycleFragment, connectionResult.getErrorCode(), 2, (DialogInterface$OnCancelListener)this.zzew);
            return;
        }
        if (connectionResult.getErrorCode() == 18) {
            this.zzew.zzdg.registerCallbackOnUpdate(this.zzew.getActivity().getApplicationContext(), new zzn(this, this.zzew.zzdg.showUpdatingDialog(this.zzew.getActivity(), (DialogInterface$OnCancelListener)this.zzew)));
            return;
        }
        this.zzew.zza(connectionResult, this.zzev.zzu());
    }
}
