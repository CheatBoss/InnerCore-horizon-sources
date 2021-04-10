package com.google.android.gms.common.api.internal;

final class zzbh implements BackgroundStateChangeListener
{
    private final /* synthetic */ GoogleApiManager zzjy;
    
    zzbh(final GoogleApiManager zzjy) {
        this.zzjy = zzjy;
    }
    
    @Override
    public final void onBackgroundStateChanged(final boolean b) {
        this.zzjy.handler.sendMessage(this.zzjy.handler.obtainMessage(1, (Object)b));
    }
}
