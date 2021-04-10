package com.google.firebase.analytics.connector.internal;

import com.google.firebase.analytics.connector.*;
import com.google.firebase.*;
import android.content.*;
import com.google.firebase.events.*;
import com.google.firebase.components.*;
import java.util.*;

public class AnalyticsConnectorRegistrar implements ComponentRegistrar
{
    @Override
    public List<Component<?>> getComponents() {
        return (List<Component<?>>)Collections.singletonList(Component.builder(AnalyticsConnector.class).add(Dependency.required(FirebaseApp.class)).add(Dependency.required(Context.class)).add(Dependency.required(Subscriber.class)).factory(zzb.zzbsl).eagerInDefaultApp().build());
    }
}
