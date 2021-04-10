package com.google.android.gms.common;

import com.google.android.gms.common.internal.*;
import android.content.*;
import android.os.*;
import android.app.*;

public class ErrorDialogFragment extends DialogFragment
{
    private Dialog mDialog;
    private DialogInterface$OnCancelListener zzap;
    
    public ErrorDialogFragment() {
        this.mDialog = null;
        this.zzap = null;
    }
    
    public static ErrorDialogFragment newInstance(Dialog mDialog, final DialogInterface$OnCancelListener zzap) {
        final ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
        mDialog = Preconditions.checkNotNull(mDialog, "Cannot display null dialog");
        mDialog.setOnCancelListener((DialogInterface$OnCancelListener)null);
        mDialog.setOnDismissListener((DialogInterface$OnDismissListener)null);
        errorDialogFragment.mDialog = mDialog;
        if (zzap != null) {
            errorDialogFragment.zzap = zzap;
        }
        return errorDialogFragment;
    }
    
    public void onCancel(final DialogInterface dialogInterface) {
        final DialogInterface$OnCancelListener zzap = this.zzap;
        if (zzap != null) {
            zzap.onCancel(dialogInterface);
        }
    }
    
    public Dialog onCreateDialog(final Bundle bundle) {
        if (this.mDialog == null) {
            this.setShowsDialog(false);
        }
        return this.mDialog;
    }
    
    public void show(final FragmentManager fragmentManager, final String s) {
        super.show(fragmentManager, s);
    }
}
