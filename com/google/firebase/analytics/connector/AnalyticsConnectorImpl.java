package com.google.firebase.analytics.connector;

import com.google.android.gms.measurement.*;
import java.util.*;
import com.google.android.gms.common.internal.*;
import java.util.concurrent.*;
import android.content.*;
import android.os.*;
import com.google.firebase.*;
import com.google.android.gms.measurement.internal.*;
import com.google.firebase.events.*;
import com.google.firebase.analytics.connector.internal.*;

public class AnalyticsConnectorImpl implements AnalyticsConnector
{
    private static volatile AnalyticsConnector zzbsf;
    private final AppMeasurement zzbsg;
    final Map<String, Object> zzbsh;
    
    private AnalyticsConnectorImpl(final AppMeasurement zzbsg) {
        Preconditions.checkNotNull(zzbsg);
        this.zzbsg = zzbsg;
        this.zzbsh = new ConcurrentHashMap<String, Object>();
    }
    
    public static AnalyticsConnector getInstance(final FirebaseApp firebaseApp, final Context context, final Subscriber subscriber) {
        Preconditions.checkNotNull(firebaseApp);
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(subscriber);
        Preconditions.checkNotNull(context.getApplicationContext());
        if (AnalyticsConnectorImpl.zzbsf == null) {
            synchronized (AnalyticsConnectorImpl.class) {
                if (AnalyticsConnectorImpl.zzbsf == null) {
                    final Bundle bundle = new Bundle(1);
                    if (firebaseApp.isDefaultApp()) {
                        subscriber.subscribe(DataCollectionDefaultChange.class, zza.zzbsi, zzb.zzbsj);
                        bundle.putBoolean("dataCollectionDefaultEnabled", firebaseApp.isDataCollectionDefaultEnabled());
                    }
                    AnalyticsConnectorImpl.zzbsf = new AnalyticsConnectorImpl(zzbt.zza(context, zzak.zzc(bundle)).zzki());
                }
            }
        }
        return AnalyticsConnectorImpl.zzbsf;
    }
    
    @Override
    public void logEvent(final String s, final String s2, final Bundle bundle) {
        Bundle bundle2 = bundle;
        if (bundle == null) {
            bundle2 = new Bundle();
        }
        if (!zzc.zzfo(s)) {
            return;
        }
        if (!zzc.zza(s2, bundle2)) {
            return;
        }
        if (!zzc.zzb(s, s2, bundle2)) {
            return;
        }
        this.zzbsg.logEventInternal(s, s2, bundle2);
    }
    
    @Override
    public void setUserProperty(final String s, final String s2, final Object o) {
        if (!zzc.zzfo(s)) {
            return;
        }
        if (!zzc.zzy(s, s2)) {
            return;
        }
        this.zzbsg.setUserPropertyInternal(s, s2, o);
    }
}
