package com.google.firebase.iid;

import android.util.*;
import com.google.android.gms.tasks.*;

final class zzaq implements Continuation
{
    private final zzap zzcm;
    private final Pair zzcn;
    
    zzaq(final zzap zzcm, final Pair zzcn) {
        this.zzcm = zzcm;
        this.zzcn = zzcn;
    }
    
    @Override
    public final Object then(final Task task) {
        return this.zzcm.zza(this.zzcn, task);
    }
}
