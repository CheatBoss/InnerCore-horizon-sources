package com.google.android.gms.common.api.internal;

import android.app.*;

public class LifecycleCallback
{
    protected final LifecycleFragment mLifecycleFragment;
    
    private static LifecycleFragment getChimeraLifecycleFragmentImpl(final LifecycleActivity lifecycleActivity) {
        throw new IllegalStateException("Method not available in SDK.");
    }
    
    public final Activity getActivity() {
        return this.mLifecycleFragment.getLifecycleActivity();
    }
}
