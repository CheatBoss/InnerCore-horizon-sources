package com.google.firebase.iid;

import com.google.firebase.components.*;

final class zzao implements ComponentFactory
{
    static final ComponentFactory zzcj;
    
    static {
        zzcj = new zzao();
    }
    
    private zzao() {
    }
    
    @Override
    public final Object create(final ComponentContainer componentContainer) {
        return new Registrar.zza(componentContainer.get(FirebaseInstanceId.class));
    }
}
