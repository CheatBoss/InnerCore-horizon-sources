package com.google.android.gms.measurement.internal;

import com.google.android.gms.common.internal.*;
import android.content.*;
import android.os.*;

public final class zzao extends BaseGmsClient<zzag>
{
    public zzao(final Context context, final Looper looper, final BaseConnectionCallbacks baseConnectionCallbacks, final BaseOnConnectionFailedListener baseOnConnectionFailedListener) {
        super(context, looper, 93, baseConnectionCallbacks, baseOnConnectionFailedListener, null);
    }
    
    @Override
    public final int getMinApkVersion() {
        return 12451000;
    }
    
    @Override
    protected final String getServiceDescriptor() {
        return "com.google.android.gms.measurement.internal.IMeasurementService";
    }
    
    @Override
    protected final String getStartServiceAction() {
        return "com.google.android.gms.measurement.START";
    }
}
