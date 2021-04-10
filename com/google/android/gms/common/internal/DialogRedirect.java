package com.google.android.gms.common.internal;

import android.app.*;
import com.google.android.gms.common.api.internal.*;
import android.util.*;
import android.content.*;

public abstract class DialogRedirect implements DialogInterface$OnClickListener
{
    public static DialogRedirect getInstance(final Activity activity, final Intent intent, final int n) {
        return new zzb(intent, activity, n);
    }
    
    public static DialogRedirect getInstance(final LifecycleFragment lifecycleFragment, final Intent intent, final int n) {
        return new zzd(intent, lifecycleFragment, n);
    }
    
    public void onClick(final DialogInterface dialogInterface, final int n) {
        try {
            try {
                this.redirect();
                dialogInterface.dismiss();
                return;
            }
            finally {}
        }
        catch (ActivityNotFoundException ex) {
            Log.e("DialogRedirect", "Failed to start resolution intent", (Throwable)ex);
            dialogInterface.dismiss();
            return;
        }
        dialogInterface.dismiss();
    }
    
    protected abstract void redirect();
}
