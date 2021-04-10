package com.google.android.gms.measurement.internal;

import java.math.*;
import android.text.*;
import android.support.v4.util.*;
import java.util.regex.*;
import com.google.android.gms.common.internal.*;
import java.util.*;
import com.google.android.gms.internal.measurement.*;
import android.content.*;
import java.io.*;
import android.util.*;
import android.database.sqlite.*;

final class zzj extends zzez
{
    zzj(final zzfa zzfa) {
        super(zzfa);
    }
    
    private final Boolean zza(final double n, final zzfx zzfx) {
        try {
            return zza(new BigDecimal(n), zzfx, Math.ulp(n));
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    private final Boolean zza(final long n, final zzfx zzfx) {
        try {
            return zza(new BigDecimal(n), zzfx, 0.0);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    private final Boolean zza(final zzfv zzfv, final String s, final zzgg[] array, final long n) {
        final zzfx zzavi = zzfv.zzavi;
        final int n2 = 0;
        final Boolean value = false;
        if (zzavi != null) {
            final Boolean zza = this.zza(n, zzfv.zzavi);
            if (zza == null) {
                return null;
            }
            if (!zza) {
                return value;
            }
        }
        final HashSet<String> set = new HashSet<String>();
        final zzfw[] zzavg = zzfv.zzavg;
        for (int length = zzavg.length, i = 0; i < length; ++i) {
            final zzfw zzfw = zzavg[i];
            if (TextUtils.isEmpty((CharSequence)zzfw.zzavn)) {
                this.zzgo().zzjg().zzg("null or empty param name in filter. event", this.zzgl().zzbs(s));
                return null;
            }
            set.add(zzfw.zzavn);
        }
        final ArrayMap<Object, String> arrayMap = new ArrayMap<Object, String>();
        for (int length2 = array.length, j = 0; j < length2; ++j) {
            final zzgg zzgg = array[j];
            if (set.contains(zzgg.name)) {
                String s2;
                Serializable s3;
                if (zzgg.zzawx != null) {
                    s2 = zzgg.name;
                    s3 = zzgg.zzawx;
                }
                else if (zzgg.zzauh != null) {
                    s2 = zzgg.name;
                    s3 = zzgg.zzauh;
                }
                else {
                    if (zzgg.zzamp == null) {
                        this.zzgo().zzjg().zze("Unknown value for param. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzgg.name));
                        return null;
                    }
                    s2 = zzgg.name;
                    s3 = zzgg.zzamp;
                }
                arrayMap.put(s2, (String)s3);
            }
        }
        final zzfw[] zzavg2 = zzfv.zzavg;
        for (int length3 = zzavg2.length, k = n2; k < length3; ++k) {
            final zzfw zzfw2 = zzavg2[k];
            final boolean equals = Boolean.TRUE.equals(zzfw2.zzavm);
            final String zzavn = zzfw2.zzavn;
            if (TextUtils.isEmpty((CharSequence)zzavn)) {
                this.zzgo().zzjg().zzg("Event has empty param name. event", this.zzgl().zzbs(s));
                return null;
            }
            final Object value2 = arrayMap.get(zzavn);
            if (value2 instanceof Long) {
                if (zzfw2.zzavl == null) {
                    this.zzgo().zzjg().zze("No number filter for long param. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                    return null;
                }
                final Boolean zza2 = this.zza((long)value2, zzfw2.zzavl);
                if (zza2 == null) {
                    return null;
                }
                if (true ^ zza2 ^ equals) {
                    return value;
                }
            }
            else if (value2 instanceof Double) {
                if (zzfw2.zzavl == null) {
                    this.zzgo().zzjg().zze("No number filter for double param. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                    return null;
                }
                final Boolean zza3 = this.zza((double)value2, zzfw2.zzavl);
                if (zza3 == null) {
                    return null;
                }
                if (true ^ zza3 ^ equals) {
                    return value;
                }
            }
            else if (value2 instanceof String) {
                Boolean b;
                if (zzfw2.zzavk != null) {
                    b = this.zza((String)value2, zzfw2.zzavk);
                }
                else {
                    if (zzfw2.zzavl == null) {
                        this.zzgo().zzjg().zze("No filter for String param. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                        return null;
                    }
                    final String s4 = (String)value2;
                    if (!zzfg.zzcp(s4)) {
                        this.zzgo().zzjg().zze("Invalid param value for number filter. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                        return null;
                    }
                    b = this.zza(s4, zzfw2.zzavl);
                }
                if (b == null) {
                    return null;
                }
                if (true ^ b ^ equals) {
                    return value;
                }
            }
            else {
                if (value2 == null) {
                    this.zzgo().zzjl().zze("Missing param for filter. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                    return value;
                }
                this.zzgo().zzjg().zze("Unknown param type. event, param", this.zzgl().zzbs(s), this.zzgl().zzbt(zzavn));
                return null;
            }
        }
        return true;
    }
    
    private final Boolean zza(final zzfy zzfy, final zzgl zzgl) {
        final zzfw zzavv = zzfy.zzavv;
        if (zzavv == null) {
            this.zzgo().zzjg().zzg("Missing property filter. property", this.zzgl().zzbu(zzgl.name));
            return null;
        }
        final boolean equals = Boolean.TRUE.equals(zzavv.zzavm);
        Boolean b;
        if (zzgl.zzawx != null) {
            if (zzavv.zzavl == null) {
                this.zzgo().zzjg().zzg("No number filter for long property. property", this.zzgl().zzbu(zzgl.name));
                return null;
            }
            b = this.zza(zzgl.zzawx, zzavv.zzavl);
        }
        else if (zzgl.zzauh != null) {
            if (zzavv.zzavl == null) {
                this.zzgo().zzjg().zzg("No number filter for double property. property", this.zzgl().zzbu(zzgl.name));
                return null;
            }
            b = this.zza(zzgl.zzauh, zzavv.zzavl);
        }
        else {
            if (zzgl.zzamp == null) {
                this.zzgo().zzjg().zzg("User property has no value, property", this.zzgl().zzbu(zzgl.name));
                return null;
            }
            if (zzavv.zzavk == null) {
                if (zzavv.zzavl == null) {
                    this.zzgo().zzjg().zzg("No string or number filter defined. property", this.zzgl().zzbu(zzgl.name));
                    return null;
                }
                if (!zzfg.zzcp(zzgl.zzamp)) {
                    this.zzgo().zzjg().zze("Invalid user property value for Numeric number filter. property, value", this.zzgl().zzbu(zzgl.name), zzgl.zzamp);
                    return null;
                }
                b = this.zza(zzgl.zzamp, zzavv.zzavl);
            }
            else {
                b = this.zza(zzgl.zzamp, zzavv.zzavk);
            }
        }
        return zza(b, equals);
    }
    
    private static Boolean zza(final Boolean b, final boolean b2) {
        if (b == null) {
            return null;
        }
        return b ^ b2;
    }
    
    private final Boolean zza(final String s, int n, final boolean b, final String s2, final List<String> list, final String s3) {
        if (s == null) {
            return null;
        }
        if (n == 6) {
            if (list == null || list.size() == 0) {
                return null;
            }
        }
        else if (s2 == null) {
            return null;
        }
        String upperCase = s;
        if (!b) {
            if (n == 1) {
                upperCase = s;
            }
            else {
                upperCase = s.toUpperCase(Locale.ENGLISH);
            }
        }
        boolean b2 = false;
        switch (n) {
            default: {
                return null;
            }
            case 6: {
                b2 = list.contains(upperCase);
                break;
            }
            case 5: {
                b2 = upperCase.equals(s2);
                break;
            }
            case 4: {
                b2 = upperCase.contains(s2);
                break;
            }
            case 3: {
                b2 = upperCase.endsWith(s2);
                break;
            }
            case 2: {
                b2 = upperCase.startsWith(s2);
                break;
            }
            case 1: {
                if (b) {
                    n = 0;
                }
                else {
                    n = 66;
                }
                try {
                    return Pattern.compile(s3, n).matcher(upperCase).matches();
                }
                catch (PatternSyntaxException ex) {
                    this.zzgo().zzjg().zzg("Invalid regular expression in REGEXP audience filter. expression", s3);
                    return null;
                }
                break;
            }
        }
        return b2;
    }
    
    private final Boolean zza(final String s, final zzfx zzfx) {
        if (!zzfg.zzcp(s)) {
            return null;
        }
        try {
            return zza(new BigDecimal(s), zzfx, 0.0);
        }
        catch (NumberFormatException ex) {
            return null;
        }
    }
    
    private final Boolean zza(final String s, final zzfz zzfz) {
        Preconditions.checkNotNull(zzfz);
        if (s == null) {
            return null;
        }
        if (zzfz.zzavw == null) {
            return null;
        }
        if (zzfz.zzavw == 0) {
            return null;
        }
        if (zzfz.zzavw == 6) {
            if (zzfz.zzavz == null || zzfz.zzavz.length == 0) {
                return null;
            }
        }
        else if (zzfz.zzavx == null) {
            return null;
        }
        final int intValue = zzfz.zzavw;
        final Boolean zzavy = zzfz.zzavy;
        int i = 0;
        final boolean b = zzavy != null && zzfz.zzavy;
        String s2;
        if (!b && intValue != 1 && intValue != 6) {
            s2 = zzfz.zzavx.toUpperCase(Locale.ENGLISH);
        }
        else {
            s2 = zzfz.zzavx;
        }
        List<String> list;
        if (zzfz.zzavz == null) {
            list = null;
        }
        else {
            final String[] zzavz = zzfz.zzavz;
            if (b) {
                list = Arrays.asList(zzavz);
            }
            else {
                list = new ArrayList<String>();
                while (i < zzavz.length) {
                    list.add(zzavz[i].toUpperCase(Locale.ENGLISH));
                    ++i;
                }
            }
        }
        String s3;
        if (intValue == 1) {
            s3 = s2;
        }
        else {
            s3 = null;
        }
        return this.zza(s, intValue, b, s2, list, s3);
    }
    
    private static Boolean zza(final BigDecimal bigDecimal, final zzfx zzfx, final double n) {
        Preconditions.checkNotNull(zzfx);
        if (zzfx.zzavo != null) {
            if (zzfx.zzavo == 0) {
                return null;
            }
            if (zzfx.zzavo == 4) {
                if (zzfx.zzavr == null || zzfx.zzavs == null) {
                    return null;
                }
            }
            else if (zzfx.zzavq == null) {
                return null;
            }
            final int intValue = zzfx.zzavo;
            Label_0166: {
                if (zzfx.zzavo == 4) {
                    if (zzfg.zzcp(zzfx.zzavr)) {
                        if (!zzfg.zzcp(zzfx.zzavs)) {
                            return null;
                        }
                        try {
                            final BigDecimal bigDecimal2 = new BigDecimal(zzfx.zzavr);
                            final BigDecimal bigDecimal3 = new BigDecimal(zzfx.zzavs);
                            final BigDecimal bigDecimal4 = null;
                            break Label_0166;
                        }
                        catch (NumberFormatException ex) {}
                    }
                    return null;
                }
                if (!zzfg.zzcp(zzfx.zzavq)) {
                    return null;
                }
                try {
                    final BigDecimal bigDecimal4 = new BigDecimal(zzfx.zzavq);
                    final BigDecimal bigDecimal2;
                    final BigDecimal bigDecimal3 = bigDecimal2 = null;
                    if (intValue == 4) {
                        if (bigDecimal2 == null) {
                            return null;
                        }
                    }
                    else if (bigDecimal4 == null) {
                        return null;
                    }
                    final boolean b = false;
                    boolean b2 = false;
                    final boolean b3 = false;
                    final boolean b4 = false;
                    final boolean b5 = false;
                    if (intValue == 1) {
                        boolean b6 = b4;
                        if (bigDecimal.compareTo(bigDecimal4) == -1) {
                            b6 = true;
                        }
                        return b6;
                    }
                    if (intValue == 2) {
                        boolean b7 = b3;
                        if (bigDecimal.compareTo(bigDecimal4) == 1) {
                            b7 = true;
                        }
                        return b7;
                    }
                    if (intValue != 3) {
                        if (intValue != 4) {
                            return null;
                        }
                        boolean b8 = b5;
                        if (bigDecimal.compareTo(bigDecimal2) != -1) {
                            b8 = b5;
                            if (bigDecimal.compareTo(bigDecimal3) != 1) {
                                b8 = true;
                            }
                        }
                        return b8;
                    }
                    else {
                        if (n != 0.0) {
                            boolean b9 = b;
                            if (bigDecimal.compareTo(bigDecimal4.subtract(new BigDecimal(n).multiply(new BigDecimal(2)))) == 1) {
                                b9 = b;
                                if (bigDecimal.compareTo(bigDecimal4.add(new BigDecimal(n).multiply(new BigDecimal(2)))) == -1) {
                                    b9 = true;
                                }
                            }
                            return b9;
                        }
                        if (bigDecimal.compareTo(bigDecimal4) == 0) {
                            b2 = true;
                        }
                        return b2;
                    }
                }
                catch (NumberFormatException ex2) {}
            }
        }
        return null;
    }
    
    private static void zza(final Map<Integer, Long> map, final int n, long n2) {
        final Long n3 = map.get(n);
        n2 /= 1000L;
        if (n3 == null || n2 > n3) {
            map.put(n, n2);
        }
    }
    
    private static void zzb(final Map<Integer, List<Long>> map, final int n, final long n2) {
        List<Long> list;
        if ((list = map.get(n)) == null) {
            list = new ArrayList<Long>();
            map.put(n, list);
        }
        list.add(n2 / 1000L);
    }
    
    private static zzge[] zzd(final Map<Integer, Long> map) {
        if (map == null) {
            return null;
        }
        int n = 0;
        final zzge[] array = new zzge[map.size()];
        for (final Integer zzawq : map.keySet()) {
            final zzge zzge = new zzge();
            zzge.zzawq = zzawq;
            zzge.zzawr = map.get(zzawq);
            array[n] = zzge;
            ++n;
        }
        return array;
    }
    
    final zzgd[] zza(final String s, zzgf[] array, zzgl[] writableDatabase) {
        String s2 = s;
        final zzgf[] array2 = array;
        Preconditions.checkNotEmpty(s);
        final HashSet<Integer> set = new HashSet<Integer>();
        final zzgf[] array3 = (Object)new ArrayMap<Object, zzgd>();
        final ArrayMap<Integer, Object> arrayMap = new ArrayMap<Integer, Object>();
        final ArrayMap<Object, Object> arrayMap2 = new ArrayMap<Object, Object>();
        final ArrayMap<Object, Object> arrayMap3 = new ArrayMap<Object, Object>();
        final ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap4 = new ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>>();
        final boolean zzd = this.zzgq().zzd(s2, zzaf.zzakw);
        final Map<Integer, zzgj> zzbo = this.zzjq().zzbo(s2);
        Boolean value;
        final Boolean b = value = (boolean)(1 != 0);
        ArrayMap<Object, Object> arrayMap5 = arrayMap3;
        Map<K, V> map = (Map<K, V>)arrayMap2;
        ArrayMap<Integer, Object> arrayMap6 = arrayMap;
        HashSet<Integer> set2 = set;
        if (zzbo != null) {
            final Iterator<Integer> iterator = zzbo.keySet().iterator();
            while (true) {
                value = b;
                arrayMap5 = arrayMap3;
                map = (Map<K, V>)arrayMap2;
                arrayMap6 = arrayMap;
                set2 = set;
                if (!iterator.hasNext()) {
                    break;
                }
                final int intValue = iterator.next();
                final zzgj zzawn = zzbo.get(intValue);
                BitSet set3 = (BitSet)arrayMap.get(intValue);
                BitSet set4 = (BitSet)arrayMap2.get(intValue);
                Map<Integer, Long> map2;
                if (zzd) {
                    final ArrayMap<Integer, Long> arrayMap7 = new ArrayMap<Integer, Long>();
                    if (zzawn != null) {
                        if (zzawn.zzayg != null) {
                            final zzge[] zzayg = zzawn.zzayg;
                            for (int length = zzayg.length, i = 0; i < length; ++i) {
                                final zzge zzge = zzayg[i];
                                if (zzge.zzawq != null) {
                                    arrayMap7.put(zzge.zzawq, zzge.zzawr);
                                }
                            }
                        }
                    }
                    arrayMap3.put(intValue, arrayMap7);
                    map2 = arrayMap7;
                }
                else {
                    map2 = null;
                }
                if (set3 == null) {
                    set3 = new BitSet();
                    arrayMap.put((K)intValue, (V)set3);
                    set4 = new BitSet();
                    arrayMap2.put(intValue, set4);
                }
                for (int j = 0; j < zzawn.zzaye.length << 6; ++j) {
                    boolean b2 = false;
                    Label_0495: {
                        if (zzfg.zza(zzawn.zzaye, j)) {
                            this.zzgo().zzjl().zze("Filter already evaluated. audience ID, filter ID", intValue, j);
                            set4.set(j);
                            if (zzfg.zza(zzawn.zzayf, j)) {
                                set3.set(j);
                                b2 = true;
                                break Label_0495;
                            }
                        }
                        b2 = false;
                    }
                    if (map2 != null && !b2) {
                        map2.remove(j);
                    }
                }
                final zzgd zzgd = new zzgd();
                ((Map<K, V>)(Object)array3).put((K)intValue, (V)zzgd);
                zzgd.zzawo = false;
                zzgd.zzawn = zzawn;
                zzgd.zzawm = new zzgj();
                zzgd.zzawm.zzayf = zzfg.zza(set3);
                zzgd.zzawm.zzaye = zzfg.zza(set4);
                if (!zzd) {
                    continue;
                }
                zzgd.zzawm.zzayg = zzd(map2);
                arrayMap4.put(intValue, new ArrayMap<Integer, Map<Integer, Map<Integer, Map>>>());
            }
        }
        Object o = arrayMap5;
        Object o2 = map;
        Map<K, V> map3 = (Map<K, V>)arrayMap6;
        HashSet<Integer> set5 = set2;
        String s3 = "Filter definition";
        String s4 = "Skipping failed audience ID";
        ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap8 = arrayMap4;
        String s5 = s4;
        zzgf[] array4 = array3;
        String s6 = s3;
        String s7 = s2;
        Boolean b3 = value;
        HashSet<Integer> set6 = set5;
        Object o3 = o;
        Object o4 = o2;
        Object o5 = map3;
        if (array2 != null) {
            Map<String, Map<?, List>> map4 = new ArrayMap<String, Map<?, List>>();
            int length2 = array2.length;
            final zzgf zzgf = null;
            int n = 0;
            final Long n2 = null;
            long n3 = 0L;
            zzgf[] array5 = array3;
            Long n4 = n2;
            ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap9 = arrayMap4;
            zzgf zzgf2 = zzgf;
            while (true) {
                arrayMap8 = arrayMap9;
                s5 = s4;
                array4 = array5;
                s6 = s3;
                s7 = s2;
                b3 = value;
                set6 = set5;
                o3 = o;
                o4 = o2;
                o5 = map3;
                if (n >= length2) {
                    break;
                }
                final zzgf zzgf3 = array[n];
                final String name = zzgf3.name;
                final zzgg[] zzawt = zzgf3.zzawt;
                Object o6 = null;
                HashSet<Integer> set7 = null;
                Map<String, Map<?, List>> map6 = null;
                ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap11 = null;
                Object o7 = null;
                Object o8 = null;
                Boolean b6 = null;
                zzgf[] array10 = null;
                String s11 = null;
                String s12 = null;
                Label_3038: {
                    Long n6 = null;
                    zzgg[] array8 = null;
                    zzgf zzgf5 = null;
                    String s9 = null;
                    Label_1600: {
                        Label_1588: {
                            if (this.zzgq().zzd(s2, zzaf.zzakq)) {
                                this.zzjo();
                                final Long n5 = (Long)zzfg.zzb(zzgf3, "_eid");
                                final boolean b4 = n5 != null;
                                if (b4 && name.equals("_ep")) {
                                    this.zzjo();
                                    final String s8 = (String)zzfg.zzb(zzgf3, "_en");
                                    Label_1410: {
                                        if (!TextUtils.isEmpty((CharSequence)s8)) {
                                            zzgf zzgf4;
                                            if (zzgf2 != null && n4 != null && n5 == (long)n4) {
                                                zzgf4 = zzgf2;
                                                n6 = n4;
                                            }
                                            else {
                                                final Pair<zzgf, Long> zza = this.zzjq().zza(s2, n5);
                                                if (zza == null || zza.first == null) {
                                                    this.zzgo().zzjd().zze("Extra parameter without existing main event. eventName, eventId", s8, n5);
                                                    break Label_1410;
                                                }
                                                zzgf4 = (zzgf)zza.first;
                                                n3 = (long)zza.second;
                                                this.zzjo();
                                                n6 = (Long)zzfg.zzb(zzgf4, "_eid");
                                            }
                                            --n3;
                                            if (n3 <= 0L) {
                                                final zzq zzjq = this.zzjq();
                                                zzjq.zzaf();
                                                zzjq.zzgo().zzjl().zzg("Clearing complex main event info. appId", s2);
                                                Label_1155: {
                                                    try {
                                                        final SQLiteDatabase writableDatabase2 = zzjq.getWritableDatabase();
                                                        try {
                                                            writableDatabase2.execSQL("delete from main_event_params where app_id=?", (Object[])new String[] { s2 });
                                                            break Label_1155;
                                                        }
                                                        catch (SQLiteException ex) {}
                                                    }
                                                    catch (SQLiteException ex) {}
                                                    final SQLiteException ex;
                                                    zzjq.zzgo().zzjd().zzg("Error clearing complex main event", ex);
                                                }
                                            }
                                            else {
                                                this.zzjq().zza(s2, n5, n3, zzgf4);
                                            }
                                            final int n7 = length2;
                                            final int n8 = zzgf4.zzawt.length + zzawt.length;
                                            final zzgg[] array6 = new zzgg[n8];
                                            final zzgg[] zzawt2 = zzgf4.zzawt;
                                            final int length3 = zzawt2.length;
                                            int k = 0;
                                            int n9 = 0;
                                            while (k < length3) {
                                                final zzgg zzgg = zzawt2[k];
                                                this.zzjo();
                                                int n10 = n9;
                                                if (zzfg.zza(zzgf3, zzgg.name) == null) {
                                                    array6[n9] = zzgg;
                                                    n10 = n9 + 1;
                                                }
                                                ++k;
                                                n9 = n10;
                                            }
                                            zzgg[] array7;
                                            if (n9 > 0) {
                                                for (int length4 = zzawt.length, l = 0; l < length4; ++l, ++n9) {
                                                    array6[n9] = zzawt[l];
                                                }
                                                if (n9 == n8) {
                                                    array7 = array6;
                                                }
                                                else {
                                                    array7 = Arrays.copyOf(array6, n9);
                                                }
                                            }
                                            else {
                                                this.zzgo().zzjg().zzg("No unique parameters in main event. eventName", s8);
                                                array7 = zzawt;
                                            }
                                            array8 = array7;
                                            zzgf5 = zzgf4;
                                            s9 = s8;
                                            length2 = n7;
                                            break Label_1600;
                                        }
                                        this.zzgo().zzjd().zzg("Extra parameter without an event name. eventId", n5);
                                    }
                                    final Map<String, Map<?, List>> map5 = map4;
                                    final ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap10 = arrayMap9;
                                    final zzgf[] array9 = array5;
                                    final String s10 = s4;
                                    final Boolean b5 = value;
                                    o6 = o;
                                    set7 = set5;
                                    map6 = map5;
                                    arrayMap11 = arrayMap10;
                                    o7 = o2;
                                    o8 = map3;
                                    b6 = b5;
                                    array10 = array9;
                                    s11 = s3;
                                    s12 = s10;
                                    break Label_3038;
                                }
                                if (b4) {
                                    this.zzjo();
                                    Object value2 = 0L;
                                    final Object zzb = zzfg.zzb(zzgf3, "_epc");
                                    if (zzb != null) {
                                        value2 = zzb;
                                    }
                                    n3 = (long)value2;
                                    if (n3 <= 0L) {
                                        this.zzgo().zzjg().zzg("Complex event with zero extra param count. eventName", name);
                                    }
                                    else {
                                        this.zzjq().zza(s2, n5, n3, zzgf3);
                                    }
                                    n6 = n5;
                                    zzgf5 = zzgf3;
                                    break Label_1588;
                                }
                            }
                            final Long n11 = n4;
                            zzgf5 = zzgf2;
                            n6 = n11;
                        }
                        final String s13 = name;
                        array8 = zzawt;
                        s9 = s13;
                    }
                    final Boolean b7 = value;
                    final zzz zzg = this.zzjq().zzg(s2, zzgf3.name);
                    zzz zziu;
                    if (zzg == null) {
                        this.zzgo().zzjg().zze("Event aggregate wasn't created during raw event logging. appId, event", zzap.zzbv(s), this.zzgl().zzbs(s9));
                        zziu = new zzz(s2, zzgf3.name, 1L, 1L, zzgf3.zzawu, 0L, null, null, null, null);
                    }
                    else {
                        zziu = zzg.zziu();
                    }
                    zzgf zzgf6 = zzgf3;
                    final ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap12 = arrayMap9;
                    final Object o9 = o;
                    zzgf[] array11 = array5;
                    final HashSet<Integer> set8 = set5;
                    final Object o10 = o2;
                    String s14 = s3;
                    this.zzjq().zza(zziu);
                    final long zzaie = zziu.zzaie;
                    Object zzl = map4.get(s9);
                    if (zzl == null) {
                        if ((zzl = this.zzjq().zzl(s, s9)) == null) {
                            zzl = new ArrayMap<Object, List<zzfv>>();
                        }
                        map4.put(s9, (Map<?, List>)zzl);
                    }
                    final Iterator<?> iterator2 = ((Map<?, List<zzfv>>)zzl).keySet().iterator();
                    ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap13 = arrayMap12;
                    Object o11 = o9;
                    HashSet<Integer> set9 = set8;
                    Object o12 = map3;
                    Boolean zzawo = b7;
                    final Iterator<?> iterator3 = iterator2;
                    Map<?, List<zzfv>> map7 = (Map<?, List<zzfv>>)zzl;
                    final String s15 = s9;
                    while (iterator3.hasNext()) {
                        final int intValue2 = (int)iterator3.next();
                        if (set9.contains(intValue2)) {
                            this.zzgo().zzjl().zzg(s4, intValue2);
                        }
                        else {
                            final String s16 = s4;
                            final Map<K, V> map8 = (Object)array11;
                            final zzgd zzgd2 = (zzgd)map8.get(intValue2);
                            final Object o13 = o12;
                            BitSet set10 = (BitSet)((Map<K, V>)o13).get(intValue2);
                            final BitSet set11 = (BitSet)((Map<K, V>)o10).get(intValue2);
                            Map<Integer, Long> map9;
                            Map<Integer, Map<Integer, Map<Integer, Map>>> map10;
                            if (zzd) {
                                map9 = (Map<Integer, Long>)((Map<K, V>)o11).get(intValue2);
                                map10 = arrayMap13.get(intValue2);
                            }
                            else {
                                map10 = null;
                                map9 = null;
                            }
                            final HashSet<Integer> set12 = set9;
                            final Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map>>>> map11 = (Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map>>>>)arrayMap13;
                            final Map<K, V> map12 = (Map<K, V>)o11;
                            Map<Integer, Map<Integer, Map<Integer, Map>>> map13 = null;
                            BitSet set14 = null;
                            Label_2188: {
                                BitSet set13;
                                if (zzgd2 == null) {
                                    final zzgd zzgd3 = new zzgd();
                                    map8.put((K)intValue2, (V)zzgd3);
                                    zzgd3.zzawo = zzawo;
                                    set10 = new BitSet();
                                    ((Map<K, V>)o13).put((K)intValue2, (V)set10);
                                    set13 = new BitSet();
                                    ((Map<K, V>)o10).put((K)intValue2, (V)set13);
                                    if (zzd) {
                                        map9 = new ArrayMap<Integer, Long>();
                                        map12.put((K)intValue2, (V)map9);
                                        map13 = new ArrayMap<Integer, Map<Integer, Map<Integer, Map>>>();
                                        map11.put(intValue2, map13);
                                        set14 = set13;
                                        break Label_2188;
                                    }
                                }
                                else {
                                    set13 = set11;
                                }
                                map13 = map10;
                                set14 = set13;
                            }
                            final Iterator<zzfv> iterator4 = map7.get(intValue2).iterator();
                            final BitSet set15 = set14;
                            Object o14 = map12;
                            final Boolean b8 = zzawo;
                            set9 = set12;
                            final zzgf zzgf7 = zzgf6;
                            final Map<?, List<zzfv>> map14 = map7;
                            final Map<Integer, Map<Integer, Map<Integer, Map>>> map15 = map13;
                            final Map<K, V> map16 = (Map<K, V>)o13;
                            final String s17 = s16;
                            Object o15 = map8;
                            final Iterator<zzfv> iterator5 = iterator4;
                            final String s18 = s14;
                            final BitSet set16 = set10;
                            while (iterator5.hasNext()) {
                                final zzfv zzfv = iterator5.next();
                                if (this.zzgo().isLoggable(2)) {
                                    this.zzgo().zzjl().zzd("Evaluating filter. audience, filter, event", intValue2, zzfv.zzave, this.zzgl().zzbs(zzfv.zzavf));
                                    this.zzgo().zzjl().zzg(s18, this.zzjo().zza(zzfv));
                                }
                                final Object o16 = o15;
                                HashSet<Integer> set18 = null;
                                Map<Integer, Long> map17 = null;
                                Label_2893: {
                                    if (zzfv.zzave != null && zzfv.zzave <= 256) {
                                        if (zzd) {
                                            final boolean b9 = zzfv != null && zzfv.zzavb != null && zzfv.zzavb;
                                            final boolean b10 = zzfv != null && zzfv.zzavc != null && zzfv.zzavc;
                                            if (set16.get(zzfv.zzave) && !b9 && !b10) {
                                                this.zzgo().zzjl().zze("Event filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID", intValue2, zzfv.zzave);
                                                o15 = o16;
                                                continue;
                                            }
                                            final Boolean zza2 = this.zza(zzfv, s15, array8, zzaie);
                                            final zzar zzjl = this.zzgo().zzjl();
                                            String s19;
                                            if (zza2 == null) {
                                                s19 = "null";
                                            }
                                            else {
                                                s19 = (String)zza2;
                                            }
                                            zzjl.zzg("Event filter result", s19);
                                            if (zza2 == null) {
                                                set9.add(intValue2);
                                            }
                                            else {
                                                set15.set(zzfv.zzave);
                                                if (zza2) {
                                                    set16.set(zzfv.zzave);
                                                    if (b9 || b10) {
                                                        final zzgf zzgf8 = zzgf7;
                                                        if (zzgf8.zzawu != null) {
                                                            if (b10) {
                                                                zzb((Map<Integer, List<Long>>)map15, zzfv.zzave, zzgf8.zzawu);
                                                            }
                                                            else {
                                                                zza(map9, zzfv.zzave, zzgf8.zzawu);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        else {
                                            final BitSet set17 = set16;
                                            if (!set17.get(zzfv.zzave)) {
                                                final Boolean zza3 = this.zza(zzfv, s15, array8, zzaie);
                                                final zzar zzjl2 = this.zzgo().zzjl();
                                                String s20;
                                                if (zza3 == null) {
                                                    s20 = "null";
                                                }
                                                else {
                                                    s20 = (String)zza3;
                                                }
                                                zzjl2.zzg("Event filter result", s20);
                                                if (zza3 == null) {
                                                    set9.add(intValue2);
                                                }
                                                else {
                                                    set15.set(zzfv.zzave);
                                                    if (zza3) {
                                                        set17.set(zzfv.zzave);
                                                    }
                                                }
                                                set18 = set9;
                                                map17 = map9;
                                                break Label_2893;
                                            }
                                            this.zzgo().zzjl().zze("Event filter already evaluated true. audience ID, filter ID", intValue2, zzfv.zzave);
                                        }
                                        set18 = set9;
                                        map17 = map9;
                                    }
                                    else {
                                        set18 = set9;
                                        map17 = map9;
                                        this.zzgo().zzjg().zze("Invalid event filter ID. appId, id", zzap.zzbv(s), String.valueOf(zzfv.zzave));
                                    }
                                }
                                final Object o17 = o14;
                                final HashSet<Integer> set19 = set18;
                                o15 = o16;
                                map9 = map17;
                                set9 = set19;
                                o14 = o17;
                            }
                            s4 = s17;
                            o11 = o14;
                            final Object o18 = o15;
                            s14 = s18;
                            map7 = map14;
                            zzawo = b8;
                            o12 = map16;
                            array11 = (zzgf[])o18;
                            arrayMap13 = (ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>>)map11;
                            zzgf6 = zzgf7;
                        }
                    }
                    final String s21 = s14;
                    s2 = s;
                    final Boolean b11 = zzawo;
                    final HashSet<Integer> set20 = set9;
                    array10 = array11;
                    final Map<K, V> map18 = (Map<K, V>)o11;
                    final ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap14 = arrayMap13;
                    s12 = s4;
                    final Long n12 = n6;
                    zzgf2 = zzgf5;
                    s11 = s21;
                    b6 = b11;
                    o8 = o12;
                    o7 = o10;
                    arrayMap11 = arrayMap14;
                    map6 = map4;
                    set7 = set20;
                    n4 = n12;
                    o6 = map18;
                }
                ++n;
                final Object o19 = o6;
                final HashSet<Integer> set21 = set7;
                final Object o20 = o7;
                final Object o21 = o8;
                arrayMap9 = arrayMap11;
                map4 = map6;
                s4 = s12;
                array5 = array10;
                s3 = s11;
                value = b6;
                set5 = set21;
                o = o19;
                o2 = o20;
                map3 = (Map<K, V>)o21;
            }
        }
        ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap15 = arrayMap8;
        zzgf[] array12 = array4;
        String s22 = s7;
        final Object o22 = o3;
        Object o23 = o4;
        Object o24 = o5;
        final SQLiteException ex2 = writableDatabase;
        String s23 = s22;
        Object o25 = o22;
        ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap16 = arrayMap15;
        Object o26 = o23;
        Object o27 = o24;
        zzgf[] array13 = array12;
        if (ex2 != null) {
            Map<String, Map<Integer, Map<Integer, Map<Integer, Map>>>> map19 = new ArrayMap<String, Map<Integer, Map<Integer, Map<Integer, Map>>>>();
            final int length5 = ex2.length;
            int n13 = 0;
            String s24 = s5;
            String s25 = s6;
            Boolean b12 = b3;
            while (true) {
                s23 = s22;
                o25 = o22;
                arrayMap16 = arrayMap15;
                o26 = o23;
                o27 = o24;
                array13 = array12;
                if (n13 >= length5) {
                    break;
                }
                final zzgl zzgl = writableDatabase[n13];
                Object zzm;
                if ((zzm = map19.get(zzgl.name)) == null) {
                    if ((zzm = this.zzjq().zzm(s22, zzgl.name)) == null) {
                        zzm = new ArrayMap<Integer, List<zzfy>>();
                    }
                    map19.put(zzgl.name, (Map<Integer, Map<Integer, Map<Integer, Map>>>)zzm);
                }
                Iterator<Integer> iterator6 = ((Map<Integer, List<zzfy>>)zzm).keySet().iterator();
                String s26 = s24;
                String s27 = s25;
                zzgf[] array14 = array12;
                Boolean zzawo2 = b12;
                Object o28 = o24;
                Map<Integer, List<zzfy>> map20 = (Map<Integer, List<zzfy>>)zzm;
                final Map<String, Map<Integer, Map<Integer, Map<Integer, Map>>>> map21 = map19;
            Label_3339:
                while (iterator6.hasNext()) {
                    final int intValue3 = iterator6.next();
                    if (!set6.contains(intValue3)) {
                        final Map<Integer, zzgd> map22 = (Object)array14;
                        final zzgd zzgd4 = map22.get(intValue3);
                        final Object o29 = o28;
                        BitSet set22 = ((Map<Object, BitSet>)o29).get(intValue3);
                        final Object o30 = o23;
                        final BitSet set23 = ((Map<Object, BitSet>)o30).get(intValue3);
                        Map<Integer, Long> map23;
                        Object o31;
                        if (zzd) {
                            map23 = ((Map<Object, Map<Integer, Long>>)o22).get(intValue3);
                            o31 = arrayMap15.get(intValue3);
                        }
                        else {
                            o31 = null;
                            map23 = null;
                        }
                        final Iterator<Integer> iterator7 = iterator6;
                        final Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map>>>> map24 = (Map<Integer, Map<Integer, Map<Integer, Map<Integer, Map>>>>)arrayMap15;
                        BitSet set24;
                        Map<Integer, Long> map25;
                        Object o32;
                        if (zzgd4 == null) {
                            final zzgd zzgd5 = new zzgd();
                            map22.put(intValue3, zzgd5);
                            zzgd5.zzawo = zzawo2;
                            set22 = new BitSet();
                            ((Map<Integer, BitSet>)o29).put(Integer.valueOf(intValue3), set22);
                            set24 = new BitSet();
                            ((Map<Integer, BitSet>)o30).put(Integer.valueOf(intValue3), set24);
                            if (zzd) {
                                map25 = new ArrayMap<Integer, Long>();
                                ((Map<Integer, Map<Integer, Long>>)o22).put(Integer.valueOf(intValue3), map25);
                                o32 = new ArrayMap<Integer, List<Long>>();
                                map24.put(intValue3, (Map<Integer, Map<Integer, Map<Integer, Map>>>)o32);
                            }
                            else {
                                map25 = map23;
                                o32 = o31;
                            }
                        }
                        else {
                            set24 = set23;
                            o32 = o31;
                            map25 = map23;
                        }
                        final String s28 = s26;
                        final Boolean b13 = zzawo2;
                        final Iterator<zzfy> iterator8 = map20.get(intValue3).iterator();
                        final String s29 = s27;
                        final Map<Integer, zzgd> map26 = map22;
                        final Map<Integer, List<zzfy>> map27 = map20;
                        final Map<Integer, BitSet> map28 = (Map<Integer, BitSet>)o30;
                        String s30 = s22;
                        final Map<Integer, BitSet> map29 = (Map<Integer, BitSet>)o29;
                        while (true) {
                            while (iterator8.hasNext()) {
                                final zzfy zzfy = iterator8.next();
                                if (this.zzgo().isLoggable(2)) {
                                    this.zzgo().zzjl().zzd("Evaluating filter. audience, filter, property", intValue3, zzfy.zzave, this.zzgl().zzbu(zzfy.zzavu));
                                    this.zzgo().zzjl().zzg(s29, this.zzjo().zza(zzfy));
                                }
                                if (zzfy.zzave == null || zzfy.zzave > 256) {
                                    this.zzgo().zzjg().zze("Invalid property filter ID. appId, id", zzap.zzbv(s), String.valueOf(zzfy.zzave));
                                    set6.add(intValue3);
                                    final Map<Integer, BitSet> map30 = map29;
                                    s22 = s;
                                    zzawo2 = b13;
                                    final Map<Integer, BitSet> map31 = map28;
                                    arrayMap15 = (ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>>)map24;
                                    iterator6 = iterator7;
                                    map20 = map27;
                                    o23 = map31;
                                    o28 = map30;
                                    array14 = (zzgf[])(Object)map26;
                                    s27 = s29;
                                    s26 = s28;
                                    continue Label_3339;
                                }
                                Label_4276: {
                                    if (zzd) {
                                        final boolean b14 = zzfy != null && zzfy.zzavb != null && zzfy.zzavb;
                                        final boolean b15 = zzfy != null && zzfy.zzavc != null && zzfy.zzavc;
                                        if (set22.get(zzfy.zzave) && !b14 && !b15) {
                                            this.zzgo().zzjl().zze("Property filter already evaluated true and it is not associated with a dynamic audience. audience ID, filter ID", intValue3, zzfy.zzave);
                                            break Label_4276;
                                        }
                                        final Boolean zza4 = this.zza(zzfy, zzgl);
                                        final zzar zzjl3 = this.zzgo().zzjl();
                                        Serializable s31;
                                        if (zza4 == null) {
                                            s31 = "null";
                                        }
                                        else {
                                            s31 = zza4;
                                        }
                                        zzjl3.zzg("Property filter result", s31);
                                        if (zza4 != null) {
                                            set24.set(zzfy.zzave);
                                            set22.set(zzfy.zzave, zza4);
                                            if (!zza4 || (!b14 && !b15) || zzgl.zzayl == null) {
                                                break Label_4276;
                                            }
                                            if (b15) {
                                                zzb((Map<Integer, List<Long>>)o32, zzfy.zzave, zzgl.zzayl);
                                                break Label_4276;
                                            }
                                            zza(map25, zzfy.zzave, zzgl.zzayl);
                                            break Label_4276;
                                        }
                                    }
                                    else {
                                        if (set22.get(zzfy.zzave)) {
                                            this.zzgo().zzjl().zze("Property filter already evaluated true. audience ID, filter ID", intValue3, zzfy.zzave);
                                            break Label_4276;
                                        }
                                        final Boolean zza5 = this.zza(zzfy, zzgl);
                                        final zzar zzjl4 = this.zzgo().zzjl();
                                        String s32;
                                        if (zza5 == null) {
                                            s32 = "null";
                                        }
                                        else {
                                            s32 = (String)zza5;
                                        }
                                        zzjl4.zzg("Property filter result", s32);
                                        if (zza5 != null) {
                                            set24.set(zzfy.zzave);
                                            if (zza5) {
                                                set22.set(zzfy.zzave);
                                            }
                                            break Label_4276;
                                        }
                                    }
                                    set6.add(intValue3);
                                }
                                s30 = s;
                            }
                            final Map<Integer, BitSet> map30 = map29;
                            s22 = s30;
                            continue;
                        }
                    }
                    this.zzgo().zzjl().zzg(s26, intValue3);
                }
                ++n13;
                map19 = map21;
                o24 = o28;
                b12 = zzawo2;
                array12 = array14;
                s25 = s27;
                s24 = s26;
            }
        }
        final Object o33 = o25;
        final ArrayMap<Object, Map<Integer, Map<Integer, Map<Integer, Map>>>> arrayMap17 = arrayMap16;
        array = array13;
        final zzgd[] array15 = new zzgd[((Map)o27).size()];
        final Iterator<Integer> iterator9 = ((Map<Integer, Object>)o27).keySet().iterator();
        int n14 = 0;
        final Object o34 = o26;
        while (iterator9.hasNext()) {
            final int intValue4 = iterator9.next();
            if (!set6.contains(intValue4)) {
                zzgd zzgd6;
                if ((zzgd6 = ((Map<K, zzgd>)(Object)array).get(intValue4)) == null) {
                    zzgd6 = new zzgd();
                }
                array15[n14] = zzgd6;
                zzgd6.zzauy = intValue4;
                zzgd6.zzawm = new zzgj();
                zzgd6.zzawm.zzayf = zzfg.zza(((Map<Integer, BitSet>)o27).get((Object)intValue4));
                zzgd6.zzawm.zzaye = zzfg.zza(((Map<Object, BitSet>)o34).get((Object)intValue4));
                if (zzd) {
                    zzgd6.zzawm.zzayg = zzd(((Map<Object, Map<Integer, Long>>)o33).get((Object)intValue4));
                    final zzgj zzawm = zzgd6.zzawm;
                    final Map<Integer, Map<Integer, Map<Integer, Map>>> map32 = arrayMap17.get(intValue4);
                    zzgk[] zzayh;
                    if (map32 == null) {
                        zzayh = new zzgk[0];
                    }
                    else {
                        final zzgk[] array16 = new zzgk[map32.size()];
                        final Iterator<Integer> iterator10 = map32.keySet().iterator();
                        int n15 = 0;
                        while (iterator10.hasNext()) {
                            final Integer zzawq = iterator10.next();
                            final zzgk zzgk = new zzgk();
                            zzgk.zzawq = zzawq;
                            final List list = (List)map32.get(zzawq);
                            if (list != null) {
                                Collections.sort((List<Comparable>)list);
                                final long[] zzayj = new long[list.size()];
                                final Iterator<Long> iterator11 = list.iterator();
                                int n16 = 0;
                                while (iterator11.hasNext()) {
                                    zzayj[n16] = iterator11.next();
                                    ++n16;
                                }
                                zzgk.zzayj = zzayj;
                            }
                            array16[n15] = zzgk;
                            ++n15;
                        }
                        zzayh = array16;
                    }
                    zzawm.zzayh = zzayh;
                }
                Object o35 = this.zzjq();
                final zzgj zzawm2 = zzgd6.zzawm;
                ((zzez)o35).zzcl();
                ((zzco)o35).zzaf();
                Preconditions.checkNotEmpty(s);
                Preconditions.checkNotNull(zzawm2);
                Label_5080: {
                    String s33 = null;
                    Label_5068: {
                        try {
                            final int zzvu = zzawm2.zzvu();
                            writableDatabase = (SQLiteException)(Object)new byte[zzvu];
                            try {
                                final zzyy zzk = zzyy.zzk((byte[])(Object)writableDatabase, 0, zzvu);
                                zzawm2.zza(zzk);
                                zzk.zzyt();
                                final ContentValues contentValues = new ContentValues();
                                contentValues.put("app_id", s23);
                                contentValues.put("audience_id", Integer.valueOf(intValue4));
                                contentValues.put("current_results", (byte[])(Object)writableDatabase);
                                try {
                                    writableDatabase = (SQLiteException)((zzq)o35).getWritableDatabase();
                                    try {
                                        if (((SQLiteDatabase)writableDatabase).insertWithOnConflict("audience_filter_values", (String)null, contentValues, 5) == -1L) {
                                            ((zzco)o35).zzgo().zzjd().zzg("Failed to insert filter results (got -1). appId", zzap.zzbv(s));
                                        }
                                        break Label_5080;
                                    }
                                    catch (SQLiteException writableDatabase) {}
                                }
                                catch (SQLiteException ex3) {}
                                o35 = ((zzco)o35).zzgo().zzjd();
                                s33 = "Error storing filter results. appId";
                                break Label_5068;
                            }
                            catch (IOException ex4) {}
                        }
                        catch (IOException writableDatabase) {}
                        o35 = ((zzco)o35).zzgo().zzjd();
                        s33 = "Configuration loss. Failed to serialize filter results. appId";
                    }
                    ((zzar)o35).zze(s33, zzap.zzbv(s), writableDatabase);
                }
                ++n14;
            }
        }
        return Arrays.copyOf(array15, n14);
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
}
