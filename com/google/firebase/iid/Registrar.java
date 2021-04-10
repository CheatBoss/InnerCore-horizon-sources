package com.google.firebase.iid;

import com.google.firebase.*;
import com.google.firebase.events.*;
import com.google.firebase.components.*;
import com.google.firebase.iid.internal.*;
import java.util.*;

public final class Registrar implements ComponentRegistrar
{
    @Override
    public final List<Component<?>> getComponents() {
        return Arrays.asList(Component.builder(FirebaseInstanceId.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.required(Subscriber.class)).factory(zzan.zzcj).alwaysEager().build(), Component.builder(FirebaseInstanceIdInternal.class).add(Dependency.required(FirebaseInstanceId.class)).factory(zzao.zzcj).build());
    }
    
    private static final class zza implements FirebaseInstanceIdInternal
    {
        private final FirebaseInstanceId zzck;
        
        public zza(final FirebaseInstanceId zzck) {
            this.zzck = zzck;
        }
    }
}
