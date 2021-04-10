package com.google.firebase.iid;

import com.google.firebase.components.*;
import com.google.firebase.*;
import com.google.firebase.events.*;

final class zzan implements ComponentFactory
{
    static final ComponentFactory zzcj;
    
    static {
        zzcj = new zzan();
    }
    
    private zzan() {
    }
    
    @Override
    public final Object create(final ComponentContainer componentContainer) {
        return new FirebaseInstanceId(componentContainer.get(FirebaseApp.class), componentContainer.get(Subscriber.class));
    }
}
