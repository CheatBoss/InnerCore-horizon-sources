package com.google.android.gms.common.api.internal;

import android.app.*;
import android.content.*;

public interface LifecycleFragment
{
    Activity getLifecycleActivity();
    
    void startActivityForResult(final Intent p0, final int p1);
}
