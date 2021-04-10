package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.*;
import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import com.google.android.gms.common.internal.*;
import android.text.*;
import android.content.*;
import android.support.v4.util.*;
import com.google.android.gms.common.api.internal.*;
import java.util.*;
import com.google.android.gms.common.util.*;
import android.os.*;

public final class zzcs extends zzf
{
    protected zzdm zzaqv;
    private AppMeasurement.EventInterceptor zzaqw;
    private final Set<AppMeasurement.OnEventListener> zzaqx;
    private boolean zzaqy;
    private final AtomicReference<String> zzaqz;
    protected boolean zzara;
    
    protected zzcs(final zzbt zzbt) {
        super(zzbt);
        this.zzaqx = new CopyOnWriteArraySet<AppMeasurement.OnEventListener>();
        this.zzara = true;
        this.zzaqz = new AtomicReference<String>();
    }
    
    private final void zza(final AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        final long currentTimeMillis = this.zzbx().currentTimeMillis();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        final String mName = conditionalUserProperty.mName;
        final Object mValue = conditionalUserProperty.mValue;
        if (this.zzgm().zzcs(mName) != 0) {
            this.zzgo().zzjd().zzg("Invalid conditional user property name", this.zzgl().zzbu(mName));
            return;
        }
        if (this.zzgm().zzi(mName, mValue) != 0) {
            this.zzgo().zzjd().zze("Invalid conditional user property value", this.zzgl().zzbu(mName), mValue);
            return;
        }
        final Object zzj = this.zzgm().zzj(mName, mValue);
        if (zzj == null) {
            this.zzgo().zzjd().zze("Unable to normalize conditional user property value", this.zzgl().zzbu(mName), mValue);
            return;
        }
        conditionalUserProperty.mValue = zzj;
        final long mTriggerTimeout = conditionalUserProperty.mTriggerTimeout;
        if (!TextUtils.isEmpty((CharSequence)conditionalUserProperty.mTriggerEventName) && (mTriggerTimeout > 15552000000L || mTriggerTimeout < 1L)) {
            this.zzgo().zzjd().zze("Invalid conditional user property timeout", this.zzgl().zzbu(mName), mTriggerTimeout);
            return;
        }
        final long mTimeToLive = conditionalUserProperty.mTimeToLive;
        if (mTimeToLive <= 15552000000L && mTimeToLive >= 1L) {
            this.zzgn().zzc(new zzda(this, conditionalUserProperty));
            return;
        }
        this.zzgo().zzjd().zze("Invalid conditional user property time to live", this.zzgl().zzbu(mName), mTimeToLive);
    }
    
    private final void zza(String s, String s2, final long n, final Bundle bundle, final boolean b, final boolean b2, final boolean b3, final String s3) {
        final String s4 = s2;
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotEmpty(s2);
        Preconditions.checkNotNull(bundle);
        this.zzaf();
        this.zzcl();
        if (!this.zzadj.isEnabled()) {
            this.zzgo().zzjk().zzbx("Event not sent since app measurement is disabled");
            return;
        }
        if (!this.zzaqy) {
            this.zzaqy = true;
            try {
                final Class<?> forName = Class.forName("com.google.android.gms.tagmanager.TagManagerService");
                try {
                    forName.getDeclaredMethod("initialize", Context.class).invoke(null, this.getContext());
                }
                catch (Exception ex) {
                    this.zzgo().zzjg().zzg("Failed to invoke Tag Manager's initialize() method", ex);
                }
            }
            catch (ClassNotFoundException ex2) {
                this.zzgo().zzjj().zzbx("Tag Manager is not found and thus will not be used");
            }
        }
        if (b3) {
            this.zzgr();
            if (!"_iap".equals(s4)) {
                final zzfk zzgm = this.zzadj.zzgm();
                int n2 = 0;
                Label_0230: {
                    if (zzgm.zzr("event", s4)) {
                        if (!zzgm.zza("event", AppMeasurement.Event.zzadk, s4)) {
                            n2 = 13;
                            break Label_0230;
                        }
                        if (zzgm.zza("event", 40, s4)) {
                            n2 = 0;
                            break Label_0230;
                        }
                    }
                    n2 = 2;
                }
                if (n2 != 0) {
                    this.zzgo().zzjf().zzg("Invalid public event name. Event will not be logged (FE)", this.zzgl().zzbs(s4));
                    this.zzadj.zzgm();
                    s = zzfk.zza(s4, 40, true);
                    int length;
                    if (s4 != null) {
                        length = s2.length();
                    }
                    else {
                        length = 0;
                    }
                    this.zzadj.zzgm().zza(n2, "_ev", s, length);
                    return;
                }
            }
        }
        this.zzgr();
        final zzdn zzla = this.zzgh().zzla();
        if (zzla != null && !bundle.containsKey("_sc")) {
            zzla.zzarn = true;
        }
        zzdo.zza(zzla, bundle, b && b3);
        final boolean equals = "am".equals(s);
        final boolean zzcv = zzfk.zzcv(s2);
        if (b && this.zzaqw != null && !zzcv && !equals) {
            this.zzgo().zzjk().zze("Passing event to registered event handler (FE)", this.zzgl().zzbs(s4), this.zzgl().zzd(bundle));
            this.zzaqw.interceptEvent(s, s4, bundle, n);
            return;
        }
        if (!this.zzadj.zzkr()) {
            return;
        }
        final int zzcr = this.zzgm().zzcr(s4);
        if (zzcr != 0) {
            this.zzgo().zzjf().zzg("Invalid event name. Event will not be logged (FE)", this.zzgl().zzbs(s4));
            this.zzgm();
            s = zzfk.zza(s4, 40, true);
            int length2;
            if (s4 != null) {
                length2 = s2.length();
            }
            else {
                length2 = 0;
            }
            this.zzadj.zzgm().zza(s3, zzcr, "_ev", s, length2);
            return;
        }
        final List<String> list = CollectionUtils.listOf(new String[] { "_o", "_sn", "_sc", "_si" });
        final zzfk zzgm2 = this.zzgm();
        final String s5 = "_o";
        Bundle zza = zzgm2.zza(s3, s4, bundle, list, b3, true);
        zzdn zzdn;
        if (zza != null && zza.containsKey("_sc") && zza.containsKey("_si")) {
            zzdn = new zzdn(zza.getString("_sn"), zza.getString("_sc"), zza.getLong("_si"));
        }
        else {
            zzdn = null;
        }
        zzdn zzdn2 = zzdn;
        if (zzdn == null) {
            zzdn2 = zzla;
        }
        List<Bundle> list2 = new ArrayList<Bundle>();
        list2.add(zza);
        final long nextLong = this.zzgm().zzmd().nextLong();
        final String[] array = zza.keySet().toArray(new String[bundle.size()]);
        Arrays.sort(array);
        final int length3 = array.length;
        int i = 0;
        int n3 = 0;
        List<String> list3 = list;
        while (i < length3) {
            final String s6 = array[i];
            final Object value = zza.get(s6);
            this.zzgm();
            final Bundle[] zze = zzfk.zze(value);
            zzdn zzdn3;
            Bundle bundle3;
            List<Bundle> list5;
            List<String> list6;
            if (zze != null) {
                zza.putInt(s6, zze.length);
                for (int j = 0; j < zze.length; ++j) {
                    final Bundle bundle2 = zze[j];
                    zzdo.zza(zzdn2, bundle2, true);
                    final Bundle zza2 = this.zzgm().zza(s3, "_ep", bundle2, list3, b3, false);
                    zza2.putString("_en", s4);
                    zza2.putLong("_eid", nextLong);
                    zza2.putString("_gn", s6);
                    zza2.putInt("_ll", zze.length);
                    zza2.putInt("_i", j);
                    list2.add(zza2);
                }
                final List<String> list4 = list3;
                zzdn3 = zzdn2;
                n3 += zze.length;
                bundle3 = zza;
                list5 = list2;
                list6 = list4;
            }
            else {
                final List<Bundle> list7 = list2;
                final zzdn zzdn4 = zzdn2;
                bundle3 = zza;
                list6 = list3;
                zzdn3 = zzdn4;
                list5 = list7;
            }
            ++i;
            final Bundle bundle4 = bundle3;
            zzdn2 = zzdn3;
            list3 = list6;
            list2 = list5;
            zza = bundle4;
        }
        if (n3 != 0) {
            zza.putLong("_eid", nextLong);
            zza.putInt("_epc", n3);
        }
        int k = 0;
        final String s7 = s5;
        final List<Bundle> list8 = list2;
        s2 = s4;
        while (k < list8.size()) {
            final Bundle bundle5 = list8.get(k);
            String s8;
            if (k != 0) {
                s8 = "_ep";
            }
            else {
                s8 = s2;
            }
            bundle5.putString(s7, s);
            Bundle zze2 = bundle5;
            if (b2) {
                zze2 = this.zzgm().zze(bundle5);
            }
            this.zzgo().zzjk().zze("Logging event (FE)", this.zzgl().zzbs(s2), this.zzgl().zzd(zze2));
            this.zzgg().zzb(new zzad(s8, new zzaa(zze2), s, n), s3);
            if (!equals) {
                final Iterator<AppMeasurement.OnEventListener> iterator = this.zzaqx.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onEvent(s, s2, new Bundle(zze2), n);
                }
            }
            ++k;
        }
        this.zzgr();
        if (this.zzgh().zzla() != null && "_ae".equals(s2)) {
            this.zzgj().zzn(true);
        }
    }
    
    private final void zza(final String s, final String s2, final long n, final Object o) {
        this.zzgn().zzc(new zzcv(this, s, s2, o, n));
    }
    
    private final void zza(final String mAppId, final String mName, final String mExpiredEventName, final Bundle mExpiredEventParams) {
        final long currentTimeMillis = this.zzbx().currentTimeMillis();
        Preconditions.checkNotEmpty(mName);
        final AppMeasurement.ConditionalUserProperty conditionalUserProperty = new AppMeasurement.ConditionalUserProperty();
        conditionalUserProperty.mAppId = mAppId;
        conditionalUserProperty.mName = mName;
        conditionalUserProperty.mCreationTimestamp = currentTimeMillis;
        if (mExpiredEventName != null) {
            conditionalUserProperty.mExpiredEventName = mExpiredEventName;
            conditionalUserProperty.mExpiredEventParams = mExpiredEventParams;
        }
        this.zzgn().zzc(new zzdb(this, conditionalUserProperty));
    }
    
    private final Map<String, Object> zzb(final String s, String iterator, final String s2, final boolean b) {
        zzar zzar;
        if (this.zzgn().zzkb()) {
            zzar = this.zzgo().zzjd();
            iterator = "Cannot get user properties from analytics worker thread";
        }
        else if (zzk.isMainThread()) {
            zzar = this.zzgo().zzjd();
            iterator = "Cannot get user properties from main thread";
        }
        else {
            final AtomicReference<List<?>> atomicReference = new AtomicReference<List<?>>();
            synchronized (atomicReference) {
                this.zzadj.zzgn().zzc(new zzde(this, atomicReference, s, (String)iterator, s2, b));
                try {
                    atomicReference.wait(5000L);
                }
                catch (InterruptedException ex) {
                    this.zzgo().zzjg().zzg("Interrupted waiting for get user properties", ex);
                }
                // monitorexit(atomicReference)
                iterator = atomicReference.get();
                if (iterator != null) {
                    final ArrayMap<String, Object> arrayMap = new ArrayMap<String, Object>(((List)iterator).size());
                    iterator = ((List<Object>)iterator).iterator();
                    while (((Iterator)iterator).hasNext()) {
                        final zzfh zzfh = ((Iterator<zzfh>)iterator).next();
                        arrayMap.put(zzfh.name, zzfh.getValue());
                    }
                    return arrayMap;
                }
                this.zzgo().zzjg();
                iterator = "Timed out waiting for get user properties";
            }
        }
        zzar.zzbx((String)iterator);
        return Collections.emptyMap();
    }
    
    private final void zzb(final AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        this.zzaf();
        this.zzcl();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        Preconditions.checkNotEmpty(conditionalUserProperty.mOrigin);
        Preconditions.checkNotNull(conditionalUserProperty.mValue);
        if (!this.zzadj.isEnabled()) {
            this.zzgo().zzjk().zzbx("Conditional property not sent since collection is disabled");
            return;
        }
        final zzfh zzfh = new zzfh(conditionalUserProperty.mName, conditionalUserProperty.mTriggeredTimestamp, conditionalUserProperty.mValue, conditionalUserProperty.mOrigin);
        try {
            this.zzgg().zzd(new zzl(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, zzfh, conditionalUserProperty.mCreationTimestamp, false, conditionalUserProperty.mTriggerEventName, this.zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mTimedOutEventName, conditionalUserProperty.mTimedOutEventParams, conditionalUserProperty.mOrigin, 0L, true, false), conditionalUserProperty.mTriggerTimeout, this.zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mTriggeredEventName, conditionalUserProperty.mTriggeredEventParams, conditionalUserProperty.mOrigin, 0L, true, false), conditionalUserProperty.mTimeToLive, this.zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, 0L, true, false)));
        }
        catch (IllegalArgumentException ex) {}
    }
    
    private final void zzb(final String s, final String s2, final long n, Bundle zzf, final boolean b, final boolean b2, final boolean b3, final String s3) {
        zzf = zzfk.zzf(zzf);
        this.zzgn().zzc(new zzcu(this, s, s2, n, zzf, b, b2, b3, s3));
    }
    
    private final void zzc(final AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        this.zzaf();
        this.zzcl();
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mName);
        if (!this.zzadj.isEnabled()) {
            this.zzgo().zzjk().zzbx("Conditional property not cleared since collection is disabled");
            return;
        }
        final zzfh zzfh = new zzfh(conditionalUserProperty.mName, 0L, null, null);
        try {
            this.zzgg().zzd(new zzl(conditionalUserProperty.mAppId, conditionalUserProperty.mOrigin, zzfh, conditionalUserProperty.mCreationTimestamp, conditionalUserProperty.mActive, conditionalUserProperty.mTriggerEventName, null, conditionalUserProperty.mTriggerTimeout, null, conditionalUserProperty.mTimeToLive, this.zzgm().zza(conditionalUserProperty.mAppId, conditionalUserProperty.mExpiredEventName, conditionalUserProperty.mExpiredEventParams, conditionalUserProperty.mOrigin, conditionalUserProperty.mCreationTimestamp, true, false)));
        }
        catch (IllegalArgumentException ex) {}
    }
    
    private final List<AppMeasurement.ConditionalUserProperty> zzf(final String s, String s2, final String s3) {
        zzar zzar;
        if (this.zzgn().zzkb()) {
            zzar = this.zzgo().zzjd();
            s2 = "Cannot get conditional user properties from analytics worker thread";
        }
        else if (zzk.isMainThread()) {
            zzar = this.zzgo().zzjd();
            s2 = "Cannot get conditional user properties from main thread";
        }
        else {
            Object o = new AtomicReference<List<zzl>>();
            synchronized (o) {
                this.zzadj.zzgn().zzc(new zzdc(this, (AtomicReference)o, s, s2, s3));
                try {
                    o.wait(5000L);
                }
                catch (InterruptedException ex) {
                    this.zzgo().zzjg().zze("Interrupted waiting for get conditional user properties", s, ex);
                }
                // monitorexit(o)
                final List<zzl> list = ((AtomicReference<List<zzl>>)o).get();
                if (list == null) {
                    this.zzgo().zzjg().zzg("Timed out waiting for get conditional user properties", s);
                    return Collections.emptyList();
                }
                final ArrayList list2 = new ArrayList<AppMeasurement.ConditionalUserProperty>(list.size());
                for (final zzl zzl : list) {
                    o = new AppMeasurement.ConditionalUserProperty();
                    ((AppMeasurement.ConditionalUserProperty)o).mAppId = zzl.packageName;
                    ((AppMeasurement.ConditionalUserProperty)o).mOrigin = zzl.origin;
                    ((AppMeasurement.ConditionalUserProperty)o).mCreationTimestamp = zzl.creationTimestamp;
                    ((AppMeasurement.ConditionalUserProperty)o).mName = zzl.zzahb.name;
                    ((AppMeasurement.ConditionalUserProperty)o).mValue = zzl.zzahb.getValue();
                    ((AppMeasurement.ConditionalUserProperty)o).mActive = zzl.active;
                    ((AppMeasurement.ConditionalUserProperty)o).mTriggerEventName = zzl.triggerEventName;
                    if (zzl.zzahc != null) {
                        ((AppMeasurement.ConditionalUserProperty)o).mTimedOutEventName = zzl.zzahc.name;
                        if (zzl.zzahc.zzaid != null) {
                            ((AppMeasurement.ConditionalUserProperty)o).mTimedOutEventParams = zzl.zzahc.zzaid.zziv();
                        }
                    }
                    ((AppMeasurement.ConditionalUserProperty)o).mTriggerTimeout = zzl.triggerTimeout;
                    if (zzl.zzahd != null) {
                        ((AppMeasurement.ConditionalUserProperty)o).mTriggeredEventName = zzl.zzahd.name;
                        if (zzl.zzahd.zzaid != null) {
                            ((AppMeasurement.ConditionalUserProperty)o).mTriggeredEventParams = zzl.zzahd.zzaid.zziv();
                        }
                    }
                    ((AppMeasurement.ConditionalUserProperty)o).mTriggeredTimestamp = zzl.zzahb.zzaue;
                    ((AppMeasurement.ConditionalUserProperty)o).mTimeToLive = zzl.timeToLive;
                    if (zzl.zzahe != null) {
                        ((AppMeasurement.ConditionalUserProperty)o).mExpiredEventName = zzl.zzahe.name;
                        if (zzl.zzahe.zzaid != null) {
                            ((AppMeasurement.ConditionalUserProperty)o).mExpiredEventParams = zzl.zzahe.zzaid.zziv();
                        }
                    }
                    list2.add((AppMeasurement.ConditionalUserProperty)o);
                }
                return (List<AppMeasurement.ConditionalUserProperty>)list2;
            }
        }
        zzar.zzbx(s2);
        return Collections.emptyList();
    }
    
    private final void zzky() {
        if (this.zzgq().zze(this.zzgf().zzal(), zzaf.zzalj)) {
            this.zzadj.zzj(false);
        }
        if (this.zzgq().zzbd(this.zzgf().zzal()) && this.zzadj.isEnabled() && this.zzara) {
            this.zzgo().zzjk().zzbx("Recording app launch after enabling measurement for the first time (FE)");
            this.zzkz();
            return;
        }
        this.zzgo().zzjk().zzbx("Updating Scion state (FE)");
        this.zzgg().zzlc();
    }
    
    public final void clearConditionalUserProperty(final String s, final String s2, final Bundle bundle) {
        this.zzgb();
        this.zza(null, s, s2, bundle);
    }
    
    public final void clearConditionalUserPropertyAs(final String s, final String s2, final String s3, final Bundle bundle) {
        Preconditions.checkNotEmpty(s);
        this.zzga();
        this.zza(s, s2, s3, bundle);
    }
    
    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserProperties(final String s, final String s2) {
        this.zzgb();
        return this.zzf(null, s, s2);
    }
    
    public final List<AppMeasurement.ConditionalUserProperty> getConditionalUserPropertiesAs(final String s, final String s2, final String s3) {
        Preconditions.checkNotEmpty(s);
        this.zzga();
        return this.zzf(s, s2, s3);
    }
    
    public final String getCurrentScreenClass() {
        final zzdn zzlb = this.zzadj.zzgh().zzlb();
        if (zzlb != null) {
            return zzlb.zzarl;
        }
        return null;
    }
    
    public final String getCurrentScreenName() {
        final zzdn zzlb = this.zzadj.zzgh().zzlb();
        if (zzlb != null) {
            return zzlb.zzuw;
        }
        return null;
    }
    
    public final String getGmpAppId() {
        if (this.zzadj.zzkk() != null) {
            return this.zzadj.zzkk();
        }
        try {
            return GoogleServices.getGoogleAppId();
        }
        catch (IllegalStateException ex) {
            this.zzadj.zzgo().zzjd().zzg("getGoogleAppId failed with exception", ex);
            return null;
        }
    }
    
    public final Map<String, Object> getUserProperties(final String s, final String s2, final boolean b) {
        this.zzgb();
        return this.zzb(null, s, s2, b);
    }
    
    public final Map<String, Object> getUserPropertiesAs(final String s, final String s2, final String s3, final boolean b) {
        Preconditions.checkNotEmpty(s);
        this.zzga();
        return this.zzb(s, s2, s3, b);
    }
    
    public final void logEvent(final String s, final String s2, final Bundle bundle) {
        this.logEvent(s, s2, bundle, true, true, this.zzbx().currentTimeMillis());
    }
    
    public final void logEvent(String s, final String s2, Bundle bundle, final boolean b, final boolean b2, final long n) {
        this.zzgb();
        if (s == null) {
            s = "app";
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.zzb(s, s2, n, bundle, b2, !b2 || this.zzaqw == null || zzfk.zzcv(s2), b ^ true, null);
    }
    
    public final void setConditionalUserProperty(AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        this.zzgb();
        conditionalUserProperty = new AppMeasurement.ConditionalUserProperty(conditionalUserProperty);
        if (!TextUtils.isEmpty((CharSequence)conditionalUserProperty.mAppId)) {
            this.zzgo().zzjg().zzbx("Package name should be null when calling setConditionalUserProperty");
        }
        conditionalUserProperty.mAppId = null;
        this.zza(conditionalUserProperty);
    }
    
    public final void setConditionalUserPropertyAs(final AppMeasurement.ConditionalUserProperty conditionalUserProperty) {
        Preconditions.checkNotNull(conditionalUserProperty);
        Preconditions.checkNotEmpty(conditionalUserProperty.mAppId);
        this.zzga();
        this.zza(new AppMeasurement.ConditionalUserProperty(conditionalUserProperty));
    }
    
    final void zza(final String s, final String s2, final long n, final Bundle bundle) {
        this.zzgb();
        this.zzaf();
        this.zza(s, s2, n, bundle, true, this.zzaqw == null || zzfk.zzcv(s2), false, null);
    }
    
    final void zza(final String s, final String s2, final Bundle bundle) {
        this.zzgb();
        this.zzaf();
        this.zza(s, s2, this.zzbx().currentTimeMillis(), bundle);
    }
    
    final void zza(final String s, final String s2, final Object o, final long n) {
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotEmpty(s2);
        this.zzaf();
        this.zzgb();
        this.zzcl();
        Object o2 = null;
        Label_0244: {
            if (this.zzgq().zze(this.zzgf().zzal(), zzaf.zzalj)) {
                o2 = o;
                if ("_ap".equals(s2)) {
                    o2 = o;
                    if (!"auto".equals(s)) {
                        if (o instanceof String) {
                            final String s3 = (String)o;
                            if (!TextUtils.isEmpty((CharSequence)s3)) {
                                final String lowerCase = s3.toLowerCase(Locale.ENGLISH);
                                final String s4 = "true";
                                long n2;
                                if (!"true".equals(lowerCase) && !"1".equals(o)) {
                                    n2 = 0L;
                                }
                                else {
                                    n2 = 1L;
                                }
                                final Long value = n2;
                                final zzbf zzans = this.zzgp().zzans;
                                String s5;
                                if (value == 1L) {
                                    s5 = s4;
                                }
                                else {
                                    s5 = "false";
                                }
                                zzans.zzcc(s5);
                                o2 = value;
                                break Label_0244;
                            }
                        }
                        if ((o2 = o) == null) {
                            this.zzgp().zzans.zzcc("unset");
                            this.zzgn().zzc(new zzcw(this));
                            o2 = o;
                        }
                    }
                }
            }
            else {
                o2 = o;
                if ("_ap".equals(s2)) {
                    return;
                }
            }
        }
        if (!this.zzadj.isEnabled()) {
            this.zzgo().zzjk().zzbx("User property not set since app measurement is disabled");
            return;
        }
        if (!this.zzadj.zzkr()) {
            return;
        }
        this.zzgo().zzjk().zze("Setting user property (FE)", this.zzgl().zzbs(s2), o2);
        this.zzgg().zzb(new zzfh(s2, n, o2, s));
    }
    
    public final void zza(String s, final String s2, final Object o, final boolean b, final long n) {
        String s3 = s;
        if (s == null) {
            s3 = "app";
        }
        int zzcs = 6;
        final boolean b2 = false;
        final boolean b3 = false;
        if (!b && !"_ap".equals(s2)) {
            final zzfk zzgm = this.zzgm();
            if (zzgm.zzr("user property", s2)) {
                if (!zzgm.zza("user property", AppMeasurement.UserProperty.zzado, s2)) {
                    zzcs = 15;
                }
                else if (zzgm.zza("user property", 24, s2)) {
                    zzcs = 0;
                }
            }
        }
        else {
            zzcs = this.zzgm().zzcs(s2);
        }
        if (zzcs != 0) {
            this.zzgm();
            s = zzfk.zza(s2, 24, true);
            int length = b3 ? 1 : 0;
            if (s2 != null) {
                length = s2.length();
            }
            this.zzadj.zzgm().zza(zzcs, "_ev", s, length);
            return;
        }
        if (o == null) {
            this.zza(s3, s2, n, (Object)null);
            return;
        }
        final int zzi = this.zzgm().zzi(s2, o);
        if (zzi != 0) {
            this.zzgm();
            s = zzfk.zza(s2, 24, true);
            int length2 = 0;
            Label_0223: {
                if (!(o instanceof String)) {
                    length2 = (b2 ? 1 : 0);
                    if (!(o instanceof CharSequence)) {
                        break Label_0223;
                    }
                }
                length2 = String.valueOf(o).length();
            }
            this.zzadj.zzgm().zza(zzi, "_ev", s, length2);
            return;
        }
        final Object zzj = this.zzgm().zzj(s2, o);
        if (zzj != null) {
            this.zza(s3, s2, n, zzj);
        }
    }
    
    public final void zzb(final String s, final String s2, final Object o, final boolean b) {
        this.zza(s, s2, o, b, this.zzbx().currentTimeMillis());
    }
    
    final void zzcm(final String s) {
        this.zzaqz.set(s);
    }
    
    public final void zzd(final boolean b) {
        this.zzcl();
        this.zzgb();
        this.zzgn().zzc(new zzdj(this, b));
    }
    
    public final String zzfx() {
        this.zzgb();
        return this.zzaqz.get();
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    public final void zzkz() {
        this.zzaf();
        this.zzgb();
        this.zzcl();
        if (!this.zzadj.zzkr()) {
            return;
        }
        this.zzgg().zzkz();
        this.zzara = false;
        final String zzjw = this.zzgp().zzjw();
        if (!TextUtils.isEmpty((CharSequence)zzjw)) {
            this.zzgk().zzcl();
            if (!zzjw.equals(Build$VERSION.RELEASE)) {
                final Bundle bundle = new Bundle();
                bundle.putString("_po", zzjw);
                this.logEvent("auto", "_ou", bundle);
            }
        }
    }
}
