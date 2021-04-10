package com.google.android.gms.measurement.internal;

import android.database.sqlite.*;
import android.database.*;
import android.content.*;
import com.google.android.gms.common.internal.*;
import android.text.*;
import java.security.*;
import android.util.*;
import android.os.*;
import java.io.*;
import java.util.*;
import com.google.android.gms.internal.measurement.*;

final class zzq extends zzez
{
    private static final String[] zzahi;
    private static final String[] zzahj;
    private static final String[] zzahk;
    private static final String[] zzahl;
    private static final String[] zzahm;
    private static final String[] zzahn;
    private final zzt zzaho;
    private final zzev zzahp;
    
    static {
        zzahi = new String[] { "last_bundled_timestamp", "ALTER TABLE events ADD COLUMN last_bundled_timestamp INTEGER;", "last_bundled_day", "ALTER TABLE events ADD COLUMN last_bundled_day INTEGER;", "last_sampled_complex_event_id", "ALTER TABLE events ADD COLUMN last_sampled_complex_event_id INTEGER;", "last_sampling_rate", "ALTER TABLE events ADD COLUMN last_sampling_rate INTEGER;", "last_exempt_from_sampling", "ALTER TABLE events ADD COLUMN last_exempt_from_sampling INTEGER;" };
        zzahj = new String[] { "origin", "ALTER TABLE user_attributes ADD COLUMN origin TEXT;" };
        zzahk = new String[] { "app_version", "ALTER TABLE apps ADD COLUMN app_version TEXT;", "app_store", "ALTER TABLE apps ADD COLUMN app_store TEXT;", "gmp_version", "ALTER TABLE apps ADD COLUMN gmp_version INTEGER;", "dev_cert_hash", "ALTER TABLE apps ADD COLUMN dev_cert_hash INTEGER;", "measurement_enabled", "ALTER TABLE apps ADD COLUMN measurement_enabled INTEGER;", "last_bundle_start_timestamp", "ALTER TABLE apps ADD COLUMN last_bundle_start_timestamp INTEGER;", "day", "ALTER TABLE apps ADD COLUMN day INTEGER;", "daily_public_events_count", "ALTER TABLE apps ADD COLUMN daily_public_events_count INTEGER;", "daily_events_count", "ALTER TABLE apps ADD COLUMN daily_events_count INTEGER;", "daily_conversions_count", "ALTER TABLE apps ADD COLUMN daily_conversions_count INTEGER;", "remote_config", "ALTER TABLE apps ADD COLUMN remote_config BLOB;", "config_fetched_time", "ALTER TABLE apps ADD COLUMN config_fetched_time INTEGER;", "failed_config_fetch_time", "ALTER TABLE apps ADD COLUMN failed_config_fetch_time INTEGER;", "app_version_int", "ALTER TABLE apps ADD COLUMN app_version_int INTEGER;", "firebase_instance_id", "ALTER TABLE apps ADD COLUMN firebase_instance_id TEXT;", "daily_error_events_count", "ALTER TABLE apps ADD COLUMN daily_error_events_count INTEGER;", "daily_realtime_events_count", "ALTER TABLE apps ADD COLUMN daily_realtime_events_count INTEGER;", "health_monitor_sample", "ALTER TABLE apps ADD COLUMN health_monitor_sample TEXT;", "android_id", "ALTER TABLE apps ADD COLUMN android_id INTEGER;", "adid_reporting_enabled", "ALTER TABLE apps ADD COLUMN adid_reporting_enabled INTEGER;", "ssaid_reporting_enabled", "ALTER TABLE apps ADD COLUMN ssaid_reporting_enabled INTEGER;", "admob_app_id", "ALTER TABLE apps ADD COLUMN admob_app_id TEXT;", "linked_admob_app_id", "ALTER TABLE apps ADD COLUMN linked_admob_app_id TEXT;" };
        zzahl = new String[] { "realtime", "ALTER TABLE raw_events ADD COLUMN realtime INTEGER;" };
        zzahm = new String[] { "has_realtime", "ALTER TABLE queue ADD COLUMN has_realtime INTEGER;", "retry_count", "ALTER TABLE queue ADD COLUMN retry_count INTEGER;" };
        zzahn = new String[] { "previous_install_count", "ALTER TABLE app2 ADD COLUMN previous_install_count INTEGER;" };
    }
    
    zzq(final zzfa zzfa) {
        super(zzfa);
        this.zzahp = new zzev(this.zzbx());
        this.zzaho = new zzt(this, this.getContext(), "google_app_measurement.db");
    }
    
    private final long zza(final String s, final String[] array) {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        Cursor cursor = null;
        final Cursor cursor2 = null;
        try {
            try {
                final Cursor rawQuery = writableDatabase.rawQuery(s, array);
                try {
                    if (rawQuery.moveToFirst()) {
                        final long long1 = rawQuery.getLong(0);
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return long1;
                    }
                    throw new SQLiteException("Database returned empty set");
                }
                catch (SQLiteException cursor) {}
            }
            finally {
                final Cursor cursor3 = cursor2;
            }
        }
        catch (SQLiteException ex) {
            final Cursor cursor3 = cursor;
            cursor = (Cursor)ex;
        }
        this.zzgo().zzjd().zze("Database error", s, cursor);
        throw cursor;
        Cursor cursor3 = null;
        if (cursor3 != null) {
            cursor3.close();
        }
    }
    
    private final long zza(final String s, String[] rawQuery, long long1) {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        final String[] array = null;
        Object o = null;
        try {
            try {
                rawQuery = (String[])(Object)writableDatabase.rawQuery(s, rawQuery);
                try {
                    if (((Cursor)(Object)rawQuery).moveToFirst()) {
                        long1 = ((Cursor)(Object)rawQuery).getLong(0);
                        if (rawQuery != null) {
                            ((Cursor)(Object)rawQuery).close();
                        }
                        return long1;
                    }
                    if (rawQuery != null) {
                        ((Cursor)(Object)rawQuery).close();
                    }
                    return long1;
                }
                catch (SQLiteException writableDatabase) {}
                finally {
                    o = rawQuery;
                }
            }
            finally {}
        }
        catch (SQLiteException writableDatabase) {
            rawQuery = array;
        }
        this.zzgo().zzjd().zze("Database error", s, writableDatabase);
        throw writableDatabase;
        if (o != null) {
            ((Cursor)o).close();
        }
    }
    
    private final Object zza(final Cursor cursor, final int n) {
        final int type = cursor.getType(n);
        if (type == 0) {
            this.zzgo().zzjd().zzbx("Loaded invalid null value from database");
            return null;
        }
        if (type == 1) {
            return cursor.getLong(n);
        }
        if (type == 2) {
            return cursor.getDouble(n);
        }
        if (type == 3) {
            return cursor.getString(n);
        }
        if (type != 4) {
            this.zzgo().zzjd().zzg("Loaded invalid unknown value type, ignoring it", type);
            return null;
        }
        this.zzgo().zzjd().zzbx("Loaded invalid blob type value, ignoring it");
        return null;
    }
    
    private static void zza(final ContentValues contentValues, final String s, final Object o) {
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotNull(o);
        if (o instanceof String) {
            contentValues.put(s, (String)o);
            return;
        }
        if (o instanceof Long) {
            contentValues.put(s, (Long)o);
            return;
        }
        if (o instanceof Double) {
            contentValues.put(s, (Double)o);
            return;
        }
        throw new IllegalArgumentException("Invalid value type");
    }
    
    private final boolean zza(final String s, final int n, final zzfv zzfv) {
        this.zzcl();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotNull(zzfv);
        if (TextUtils.isEmpty((CharSequence)zzfv.zzavf)) {
            this.zzgo().zzjg().zzd("Event filter had no event name. Audience definition ignored. appId, audienceId, filterId", zzap.zzbv(s), n, String.valueOf(zzfv.zzave));
            return false;
        }
        try {
            final int zzvu = zzfv.zzvu();
            final byte[] array = new byte[zzvu];
            final zzyy zzk = zzyy.zzk(array, 0, zzvu);
            zzfv.zza(zzk);
            zzk.zzyt();
            final ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", s);
            contentValues.put("audience_id", Integer.valueOf(n));
            contentValues.put("filter_id", zzfv.zzave);
            contentValues.put("event_name", zzfv.zzavf);
            contentValues.put("data", array);
            try {
                if (this.getWritableDatabase().insertWithOnConflict("event_filters", (String)null, contentValues, 5) == -1L) {
                    this.zzgo().zzjd().zzg("Failed to insert event filter (got -1). appId", zzap.zzbv(s));
                }
                return true;
            }
            catch (SQLiteException ex) {
                this.zzgo().zzjd().zze("Error storing event filter. appId", zzap.zzbv(s), ex);
                return false;
            }
        }
        catch (IOException ex2) {
            this.zzgo().zzjd().zze("Configuration loss. Failed to serialize event filter. appId", zzap.zzbv(s), ex2);
            return false;
        }
    }
    
    private final boolean zza(final String s, final int n, final zzfy zzfy) {
        this.zzcl();
        this.zzaf();
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotNull(zzfy);
        if (TextUtils.isEmpty((CharSequence)zzfy.zzavu)) {
            this.zzgo().zzjg().zzd("Property filter had no property name. Audience definition ignored. appId, audienceId, filterId", zzap.zzbv(s), n, String.valueOf(zzfy.zzave));
            return false;
        }
        try {
            final int zzvu = zzfy.zzvu();
            final byte[] array = new byte[zzvu];
            final zzyy zzk = zzyy.zzk(array, 0, zzvu);
            zzfy.zza(zzk);
            zzk.zzyt();
            final ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", s);
            contentValues.put("audience_id", Integer.valueOf(n));
            contentValues.put("filter_id", zzfy.zzave);
            contentValues.put("property_name", zzfy.zzavu);
            contentValues.put("data", array);
            try {
                if (this.getWritableDatabase().insertWithOnConflict("property_filters", (String)null, contentValues, 5) == -1L) {
                    this.zzgo().zzjd().zzg("Failed to insert property filter (got -1). appId", zzap.zzbv(s));
                    return false;
                }
                return true;
            }
            catch (SQLiteException ex) {
                this.zzgo().zzjd().zze("Error storing property filter. appId", zzap.zzbv(s), ex);
                return false;
            }
        }
        catch (IOException ex2) {
            this.zzgo().zzjd().zze("Configuration loss. Failed to serialize property filter. appId", zzap.zzbv(s), ex2);
            return false;
        }
    }
    
    private final boolean zza(final String s, final List<Integer> list) {
        Preconditions.checkNotEmpty(s);
        this.zzcl();
        this.zzaf();
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        try {
            final long zza = this.zza("select count(1) from audience_filter_values where app_id=?", new String[] { s });
            final int max = Math.max(0, Math.min(2000, this.zzgq().zzb(s, zzaf.zzaki)));
            if (zza <= max) {
                return false;
            }
            final ArrayList<String> list2 = new ArrayList<String>();
            for (int i = 0; i < list.size(); ++i) {
                final Integer n = list.get(i);
                if (n == null) {
                    return false;
                }
                if (!(n instanceof Integer)) {
                    return false;
                }
                list2.add(Integer.toString(n));
            }
            final String join = TextUtils.join((CharSequence)",", (Iterable)list2);
            final StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
            sb.append("(");
            sb.append(join);
            sb.append(")");
            final String string = sb.toString();
            final StringBuilder sb2 = new StringBuilder(String.valueOf(string).length() + 140);
            sb2.append("audience_id in (select audience_id from audience_filter_values where app_id=? and audience_id not in ");
            sb2.append(string);
            sb2.append(" order by rowid desc limit -1 offset ?)");
            return writableDatabase.delete("audience_filter_values", sb2.toString(), new String[] { s, Integer.toString(max) }) > 0;
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Database error querying filters. appId", zzap.zzbv(s), ex);
            return false;
        }
    }
    
    private final boolean zzil() {
        return this.getContext().getDatabasePath("google_app_measurement.db").exists();
    }
    
    public final void beginTransaction() {
        this.zzcl();
        this.getWritableDatabase().beginTransaction();
    }
    
    public final void endTransaction() {
        this.zzcl();
        this.getWritableDatabase().endTransaction();
    }
    
    final SQLiteDatabase getWritableDatabase() {
        this.zzaf();
        try {
            return this.zzaho.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjg().zzg("Error opening database", ex);
            throw ex;
        }
    }
    
    public final void setTransactionSuccessful() {
        this.zzcl();
        this.getWritableDatabase().setTransactionSuccessful();
    }
    
    public final long zza(final zzgi zzgi) throws IOException {
        this.zzaf();
        this.zzcl();
        Preconditions.checkNotNull(zzgi);
        Preconditions.checkNotEmpty(zzgi.zztt);
        try {
            final int zzvu = zzgi.zzvu();
            final byte[] array = new byte[zzvu];
            final zzyy zzk = zzyy.zzk(array, 0, zzvu);
            zzgi.zza(zzk);
            zzk.zzyt();
            final zzfg zzjo = this.zzjo();
            Preconditions.checkNotNull(array);
            zzjo.zzgm().zzaf();
            final MessageDigest messageDigest = zzfk.getMessageDigest();
            long zzc;
            if (messageDigest == null) {
                zzjo.zzgo().zzjd().zzbx("Failed to get MD5");
                zzc = 0L;
            }
            else {
                zzc = zzfk.zzc(messageDigest.digest(array));
            }
            final ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", zzgi.zztt);
            contentValues.put("metadata_fingerprint", Long.valueOf(zzc));
            contentValues.put("metadata", array);
            try {
                this.getWritableDatabase().insertWithOnConflict("raw_events_metadata", (String)null, contentValues, 4);
                return zzc;
            }
            catch (SQLiteException ex) {
                this.zzgo().zzjd().zze("Error storing raw event metadata. appId", zzap.zzbv(zzgi.zztt), ex);
                throw ex;
            }
        }
        catch (IOException ex2) {
            this.zzgo().zzjd().zze("Data loss. Failed to serialize event metadata. appId", zzap.zzbv(zzgi.zztt), ex2);
            throw ex2;
        }
    }
    
    public final Pair<zzgf, Long> zza(final String p0, final Long p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     8: aconst_null    
        //     9: astore          5
        //    11: aload_0        
        //    12: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    15: ldc_w           "select main_event, children_to_process from main_event_params where app_id=? and event_id=?"
        //    18: iconst_2       
        //    19: anewarray       Ljava/lang/String;
        //    22: dup            
        //    23: iconst_0       
        //    24: aload_1        
        //    25: aastore        
        //    26: dup            
        //    27: iconst_1       
        //    28: aload_2        
        //    29: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //    32: aastore        
        //    33: invokevirtual   android/database/sqlite/SQLiteDatabase.rawQuery:(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        //    36: astore          6
        //    38: aload           6
        //    40: astore          5
        //    42: aload           6
        //    44: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    49: ifne            83
        //    52: aload           6
        //    54: astore          5
        //    56: aload_0        
        //    57: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //    60: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjl:()Lcom/google/android/gms/measurement/internal/zzar;
        //    63: ldc_w           "Main event not found"
        //    66: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzbx:(Ljava/lang/String;)V
        //    69: aload           6
        //    71: ifnull          81
        //    74: aload           6
        //    76: invokeinterface android/database/Cursor.close:()V
        //    81: aconst_null    
        //    82: areturn        
        //    83: aload           6
        //    85: astore          5
        //    87: aload           6
        //    89: iconst_0       
        //    90: invokeinterface android/database/Cursor.getBlob:(I)[B
        //    95: astore          7
        //    97: aload           6
        //    99: astore          5
        //   101: aload           6
        //   103: iconst_1       
        //   104: invokeinterface android/database/Cursor.getLong:(I)J
        //   109: lstore_3       
        //   110: aload           6
        //   112: astore          5
        //   114: aload           7
        //   116: iconst_0       
        //   117: aload           7
        //   119: arraylength    
        //   120: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   123: astore          7
        //   125: aload           6
        //   127: astore          5
        //   129: new             Lcom/google/android/gms/internal/measurement/zzgf;
        //   132: dup            
        //   133: invokespecial   com/google/android/gms/internal/measurement/zzgf.<init>:()V
        //   136: astore          8
        //   138: aload           6
        //   140: astore          5
        //   142: aload           8
        //   144: aload           7
        //   146: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   149: pop            
        //   150: aload           6
        //   152: astore          5
        //   154: aload           8
        //   156: lload_3        
        //   157: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   160: invokestatic    android/util/Pair.create:(Ljava/lang/Object;Ljava/lang/Object;)Landroid/util/Pair;
        //   163: astore_1       
        //   164: aload           6
        //   166: ifnull          176
        //   169: aload           6
        //   171: invokeinterface android/database/Cursor.close:()V
        //   176: aload_1        
        //   177: areturn        
        //   178: astore          7
        //   180: aload           6
        //   182: astore          5
        //   184: aload_0        
        //   185: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   188: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   191: ldc_w           "Failed to merge main event. appId, eventId"
        //   194: aload_1        
        //   195: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   198: aload_2        
        //   199: aload           7
        //   201: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   204: aload           6
        //   206: ifnull          216
        //   209: aload           6
        //   211: invokeinterface android/database/Cursor.close:()V
        //   216: aconst_null    
        //   217: areturn        
        //   218: astore_2       
        //   219: aload           6
        //   221: astore_1       
        //   222: goto            232
        //   225: astore_1       
        //   226: goto            262
        //   229: astore_2       
        //   230: aconst_null    
        //   231: astore_1       
        //   232: aload_1        
        //   233: astore          5
        //   235: aload_0        
        //   236: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   239: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   242: ldc_w           "Error selecting main event"
        //   245: aload_2        
        //   246: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   249: aload_1        
        //   250: ifnull          259
        //   253: aload_1        
        //   254: invokeinterface android/database/Cursor.close:()V
        //   259: aconst_null    
        //   260: areturn        
        //   261: astore_1       
        //   262: aload           5
        //   264: ifnull          274
        //   267: aload           5
        //   269: invokeinterface android/database/Cursor.close:()V
        //   274: aload_1        
        //   275: athrow         
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/Long;)Landroid/util/Pair<Lcom/google/android/gms/internal/measurement/zzgf;Ljava/lang/Long;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  11     38     229    232    Landroid/database/sqlite/SQLiteException;
        //  11     38     225    229    Any
        //  42     52     218    225    Landroid/database/sqlite/SQLiteException;
        //  42     52     261    262    Any
        //  56     69     218    225    Landroid/database/sqlite/SQLiteException;
        //  56     69     261    262    Any
        //  87     97     218    225    Landroid/database/sqlite/SQLiteException;
        //  87     97     261    262    Any
        //  101    110    218    225    Landroid/database/sqlite/SQLiteException;
        //  101    110    261    262    Any
        //  114    125    218    225    Landroid/database/sqlite/SQLiteException;
        //  114    125    261    262    Any
        //  129    138    218    225    Landroid/database/sqlite/SQLiteException;
        //  129    138    261    262    Any
        //  142    150    178    218    Ljava/io/IOException;
        //  142    150    218    225    Landroid/database/sqlite/SQLiteException;
        //  142    150    261    262    Any
        //  154    164    218    225    Landroid/database/sqlite/SQLiteException;
        //  154    164    261    262    Any
        //  184    204    218    225    Landroid/database/sqlite/SQLiteException;
        //  184    204    261    262    Any
        //  235    249    261    262    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0081:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final zzr zza(final long p0, final String p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5, final boolean p6) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    13: new             Lcom/google/android/gms/measurement/internal/zzr;
        //    16: dup            
        //    17: invokespecial   com/google/android/gms/measurement/internal/zzr.<init>:()V
        //    20: astore          12
        //    22: aload_0        
        //    23: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    26: astore          11
        //    28: aload           11
        //    30: ldc_w           "apps"
        //    33: bipush          6
        //    35: anewarray       Ljava/lang/String;
        //    38: dup            
        //    39: iconst_0       
        //    40: ldc             "day"
        //    42: aastore        
        //    43: dup            
        //    44: iconst_1       
        //    45: ldc             "daily_events_count"
        //    47: aastore        
        //    48: dup            
        //    49: iconst_2       
        //    50: ldc             "daily_public_events_count"
        //    52: aastore        
        //    53: dup            
        //    54: iconst_3       
        //    55: ldc             "daily_conversions_count"
        //    57: aastore        
        //    58: dup            
        //    59: iconst_4       
        //    60: ldc             "daily_error_events_count"
        //    62: aastore        
        //    63: dup            
        //    64: iconst_5       
        //    65: ldc             "daily_realtime_events_count"
        //    67: aastore        
        //    68: ldc_w           "app_id=?"
        //    71: iconst_1       
        //    72: anewarray       Ljava/lang/String;
        //    75: dup            
        //    76: iconst_0       
        //    77: aload_3        
        //    78: aastore        
        //    79: aconst_null    
        //    80: aconst_null    
        //    81: aconst_null    
        //    82: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    85: astore          10
        //    87: aload           10
        //    89: astore          9
        //    91: aload           10
        //    93: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    98: ifne            137
        //   101: aload           10
        //   103: astore          9
        //   105: aload_0        
        //   106: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   109: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   112: ldc_w           "Not updating daily counts, app is not known. appId"
        //   115: aload_3        
        //   116: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   119: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   122: aload           10
        //   124: ifnull          134
        //   127: aload           10
        //   129: invokeinterface android/database/Cursor.close:()V
        //   134: aload           12
        //   136: areturn        
        //   137: aload           10
        //   139: astore          9
        //   141: aload           10
        //   143: iconst_0       
        //   144: invokeinterface android/database/Cursor.getLong:(I)J
        //   149: lload_1        
        //   150: lcmp           
        //   151: ifne            239
        //   154: aload           10
        //   156: astore          9
        //   158: aload           12
        //   160: aload           10
        //   162: iconst_1       
        //   163: invokeinterface android/database/Cursor.getLong:(I)J
        //   168: putfield        com/google/android/gms/measurement/internal/zzr.zzahr:J
        //   171: aload           10
        //   173: astore          9
        //   175: aload           12
        //   177: aload           10
        //   179: iconst_2       
        //   180: invokeinterface android/database/Cursor.getLong:(I)J
        //   185: putfield        com/google/android/gms/measurement/internal/zzr.zzahq:J
        //   188: aload           10
        //   190: astore          9
        //   192: aload           12
        //   194: aload           10
        //   196: iconst_3       
        //   197: invokeinterface android/database/Cursor.getLong:(I)J
        //   202: putfield        com/google/android/gms/measurement/internal/zzr.zzahs:J
        //   205: aload           10
        //   207: astore          9
        //   209: aload           12
        //   211: aload           10
        //   213: iconst_4       
        //   214: invokeinterface android/database/Cursor.getLong:(I)J
        //   219: putfield        com/google/android/gms/measurement/internal/zzr.zzaht:J
        //   222: aload           10
        //   224: astore          9
        //   226: aload           12
        //   228: aload           10
        //   230: iconst_5       
        //   231: invokeinterface android/database/Cursor.getLong:(I)J
        //   236: putfield        com/google/android/gms/measurement/internal/zzr.zzahu:J
        //   239: iload           4
        //   241: ifeq            260
        //   244: aload           10
        //   246: astore          9
        //   248: aload           12
        //   250: aload           12
        //   252: getfield        com/google/android/gms/measurement/internal/zzr.zzahr:J
        //   255: lconst_1       
        //   256: ladd           
        //   257: putfield        com/google/android/gms/measurement/internal/zzr.zzahr:J
        //   260: iload           5
        //   262: ifeq            281
        //   265: aload           10
        //   267: astore          9
        //   269: aload           12
        //   271: aload           12
        //   273: getfield        com/google/android/gms/measurement/internal/zzr.zzahq:J
        //   276: lconst_1       
        //   277: ladd           
        //   278: putfield        com/google/android/gms/measurement/internal/zzr.zzahq:J
        //   281: iload           6
        //   283: ifeq            302
        //   286: aload           10
        //   288: astore          9
        //   290: aload           12
        //   292: aload           12
        //   294: getfield        com/google/android/gms/measurement/internal/zzr.zzahs:J
        //   297: lconst_1       
        //   298: ladd           
        //   299: putfield        com/google/android/gms/measurement/internal/zzr.zzahs:J
        //   302: iload           7
        //   304: ifeq            323
        //   307: aload           10
        //   309: astore          9
        //   311: aload           12
        //   313: aload           12
        //   315: getfield        com/google/android/gms/measurement/internal/zzr.zzaht:J
        //   318: lconst_1       
        //   319: ladd           
        //   320: putfield        com/google/android/gms/measurement/internal/zzr.zzaht:J
        //   323: iload           8
        //   325: ifeq            344
        //   328: aload           10
        //   330: astore          9
        //   332: aload           12
        //   334: aload           12
        //   336: getfield        com/google/android/gms/measurement/internal/zzr.zzahu:J
        //   339: lconst_1       
        //   340: ladd           
        //   341: putfield        com/google/android/gms/measurement/internal/zzr.zzahu:J
        //   344: aload           10
        //   346: astore          9
        //   348: new             Landroid/content/ContentValues;
        //   351: dup            
        //   352: invokespecial   android/content/ContentValues.<init>:()V
        //   355: astore          13
        //   357: aload           10
        //   359: astore          9
        //   361: aload           13
        //   363: ldc             "day"
        //   365: lload_1        
        //   366: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   369: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   372: aload           10
        //   374: astore          9
        //   376: aload           13
        //   378: ldc             "daily_public_events_count"
        //   380: aload           12
        //   382: getfield        com/google/android/gms/measurement/internal/zzr.zzahq:J
        //   385: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   388: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   391: aload           10
        //   393: astore          9
        //   395: aload           13
        //   397: ldc             "daily_events_count"
        //   399: aload           12
        //   401: getfield        com/google/android/gms/measurement/internal/zzr.zzahr:J
        //   404: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   407: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   410: aload           10
        //   412: astore          9
        //   414: aload           13
        //   416: ldc             "daily_conversions_count"
        //   418: aload           12
        //   420: getfield        com/google/android/gms/measurement/internal/zzr.zzahs:J
        //   423: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   426: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   429: aload           10
        //   431: astore          9
        //   433: aload           13
        //   435: ldc             "daily_error_events_count"
        //   437: aload           12
        //   439: getfield        com/google/android/gms/measurement/internal/zzr.zzaht:J
        //   442: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   445: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   448: aload           10
        //   450: astore          9
        //   452: aload           13
        //   454: ldc             "daily_realtime_events_count"
        //   456: aload           12
        //   458: getfield        com/google/android/gms/measurement/internal/zzr.zzahu:J
        //   461: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   464: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   467: aload           10
        //   469: astore          9
        //   471: aload           11
        //   473: ldc_w           "apps"
        //   476: aload           13
        //   478: ldc_w           "app_id=?"
        //   481: iconst_1       
        //   482: anewarray       Ljava/lang/String;
        //   485: dup            
        //   486: iconst_0       
        //   487: aload_3        
        //   488: aastore        
        //   489: invokevirtual   android/database/sqlite/SQLiteDatabase.update:(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
        //   492: pop            
        //   493: aload           10
        //   495: ifnull          505
        //   498: aload           10
        //   500: invokeinterface android/database/Cursor.close:()V
        //   505: aload           12
        //   507: areturn        
        //   508: astore          11
        //   510: goto            525
        //   513: astore_3       
        //   514: aconst_null    
        //   515: astore          9
        //   517: goto            564
        //   520: astore          11
        //   522: aconst_null    
        //   523: astore          10
        //   525: aload           10
        //   527: astore          9
        //   529: aload_0        
        //   530: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   533: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   536: ldc_w           "Error updating daily counts. appId"
        //   539: aload_3        
        //   540: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   543: aload           11
        //   545: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   548: aload           10
        //   550: ifnull          560
        //   553: aload           10
        //   555: invokeinterface android/database/Cursor.close:()V
        //   560: aload           12
        //   562: areturn        
        //   563: astore_3       
        //   564: aload           9
        //   566: ifnull          576
        //   569: aload           9
        //   571: invokeinterface android/database/Cursor.close:()V
        //   576: aload_3        
        //   577: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  22     87     520    525    Landroid/database/sqlite/SQLiteException;
        //  22     87     513    520    Any
        //  91     101    508    513    Landroid/database/sqlite/SQLiteException;
        //  91     101    563    564    Any
        //  105    122    508    513    Landroid/database/sqlite/SQLiteException;
        //  105    122    563    564    Any
        //  141    154    508    513    Landroid/database/sqlite/SQLiteException;
        //  141    154    563    564    Any
        //  158    171    508    513    Landroid/database/sqlite/SQLiteException;
        //  158    171    563    564    Any
        //  175    188    508    513    Landroid/database/sqlite/SQLiteException;
        //  175    188    563    564    Any
        //  192    205    508    513    Landroid/database/sqlite/SQLiteException;
        //  192    205    563    564    Any
        //  209    222    508    513    Landroid/database/sqlite/SQLiteException;
        //  209    222    563    564    Any
        //  226    239    508    513    Landroid/database/sqlite/SQLiteException;
        //  226    239    563    564    Any
        //  248    260    508    513    Landroid/database/sqlite/SQLiteException;
        //  248    260    563    564    Any
        //  269    281    508    513    Landroid/database/sqlite/SQLiteException;
        //  269    281    563    564    Any
        //  290    302    508    513    Landroid/database/sqlite/SQLiteException;
        //  290    302    563    564    Any
        //  311    323    508    513    Landroid/database/sqlite/SQLiteException;
        //  311    323    563    564    Any
        //  332    344    508    513    Landroid/database/sqlite/SQLiteException;
        //  332    344    563    564    Any
        //  348    357    508    513    Landroid/database/sqlite/SQLiteException;
        //  348    357    563    564    Any
        //  361    372    508    513    Landroid/database/sqlite/SQLiteException;
        //  361    372    563    564    Any
        //  376    391    508    513    Landroid/database/sqlite/SQLiteException;
        //  376    391    563    564    Any
        //  395    410    508    513    Landroid/database/sqlite/SQLiteException;
        //  395    410    563    564    Any
        //  414    429    508    513    Landroid/database/sqlite/SQLiteException;
        //  414    429    563    564    Any
        //  433    448    508    513    Landroid/database/sqlite/SQLiteException;
        //  433    448    563    564    Any
        //  452    467    508    513    Landroid/database/sqlite/SQLiteException;
        //  452    467    563    564    Any
        //  471    493    508    513    Landroid/database/sqlite/SQLiteException;
        //  471    493    563    564    Any
        //  529    548    563    564    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0134:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void zza(final zzg zzg) {
        Preconditions.checkNotNull(zzg);
        this.zzaf();
        this.zzcl();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzg.zzal());
        contentValues.put("app_instance_id", zzg.getAppInstanceId());
        contentValues.put("gmp_app_id", zzg.getGmpAppId());
        contentValues.put("resettable_device_id_hash", zzg.zzgx());
        contentValues.put("last_bundle_index", Long.valueOf(zzg.zzhe()));
        contentValues.put("last_bundle_start_timestamp", Long.valueOf(zzg.zzgy()));
        contentValues.put("last_bundle_end_timestamp", Long.valueOf(zzg.zzgz()));
        contentValues.put("app_version", zzg.zzak());
        contentValues.put("app_store", zzg.zzhb());
        contentValues.put("gmp_version", Long.valueOf(zzg.zzhc()));
        contentValues.put("dev_cert_hash", Long.valueOf(zzg.zzhd()));
        contentValues.put("measurement_enabled", Boolean.valueOf(zzg.isMeasurementEnabled()));
        contentValues.put("day", Long.valueOf(zzg.zzhi()));
        contentValues.put("daily_public_events_count", Long.valueOf(zzg.zzhj()));
        contentValues.put("daily_events_count", Long.valueOf(zzg.zzhk()));
        contentValues.put("daily_conversions_count", Long.valueOf(zzg.zzhl()));
        contentValues.put("config_fetched_time", Long.valueOf(zzg.zzhf()));
        contentValues.put("failed_config_fetch_time", Long.valueOf(zzg.zzhg()));
        contentValues.put("app_version_int", Long.valueOf(zzg.zzha()));
        contentValues.put("firebase_instance_id", zzg.getFirebaseInstanceId());
        contentValues.put("daily_error_events_count", Long.valueOf(zzg.zzhn()));
        contentValues.put("daily_realtime_events_count", Long.valueOf(zzg.zzhm()));
        contentValues.put("health_monitor_sample", zzg.zzho());
        contentValues.put("android_id", Long.valueOf(zzg.zzhq()));
        contentValues.put("adid_reporting_enabled", Boolean.valueOf(zzg.zzhr()));
        contentValues.put("ssaid_reporting_enabled", Boolean.valueOf(zzg.zzhs()));
        contentValues.put("admob_app_id", zzg.zzgw());
        try {
            final SQLiteDatabase writableDatabase = this.getWritableDatabase();
            if (writableDatabase.update("apps", contentValues, "app_id = ?", new String[] { zzg.zzal() }) == 0L && writableDatabase.insertWithOnConflict("apps", (String)null, contentValues, 5) == -1L) {
                this.zzgo().zzjd().zzg("Failed to insert/update app (got -1). appId", zzap.zzbv(zzg.zzal()));
            }
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Error storing app. appId", zzap.zzbv(zzg.zzal()), ex);
        }
    }
    
    public final void zza(final zzz zzz) {
        Preconditions.checkNotNull(zzz);
        this.zzaf();
        this.zzcl();
        final ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzz.zztt);
        contentValues.put("name", zzz.name);
        contentValues.put("lifetime_count", Long.valueOf(zzz.zzaie));
        contentValues.put("current_bundle_count", Long.valueOf(zzz.zzaif));
        contentValues.put("last_fire_timestamp", Long.valueOf(zzz.zzaig));
        contentValues.put("last_bundled_timestamp", Long.valueOf(zzz.zzaih));
        contentValues.put("last_bundled_day", zzz.zzaii);
        contentValues.put("last_sampled_complex_event_id", zzz.zzaij);
        contentValues.put("last_sampling_rate", zzz.zzaik);
        Long value;
        if (zzz.zzail != null && zzz.zzail) {
            value = 1L;
        }
        else {
            value = null;
        }
        contentValues.put("last_exempt_from_sampling", value);
        try {
            if (this.getWritableDatabase().insertWithOnConflict("events", (String)null, contentValues, 5) == -1L) {
                this.zzgo().zzjd().zzg("Failed to insert/update event aggregates (got -1). appId", zzap.zzbv(zzz.zztt));
            }
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Error storing event aggregates. appId", zzap.zzbv(zzz.zztt), ex);
        }
    }
    
    final void zza(final String p0, final zzfu[] p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     8: aload_1        
        //     9: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    12: pop            
        //    13: aload_2        
        //    14: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //    17: pop            
        //    18: aload_0        
        //    19: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    22: astore          15
        //    24: aload           15
        //    26: invokevirtual   android/database/sqlite/SQLiteDatabase.beginTransaction:()V
        //    29: aload_0        
        //    30: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    33: aload_0        
        //    34: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //    37: aload_1        
        //    38: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    41: pop            
        //    42: aload_0        
        //    43: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    46: astore          11
        //    48: iconst_0       
        //    49: istore          7
        //    51: aload           11
        //    53: ldc_w           "property_filters"
        //    56: ldc_w           "app_id=?"
        //    59: iconst_1       
        //    60: anewarray       Ljava/lang/String;
        //    63: dup            
        //    64: iconst_0       
        //    65: aload_1        
        //    66: aastore        
        //    67: invokevirtual   android/database/sqlite/SQLiteDatabase.delete:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
        //    70: pop            
        //    71: aload           11
        //    73: ldc_w           "event_filters"
        //    76: ldc_w           "app_id=?"
        //    79: iconst_1       
        //    80: anewarray       Ljava/lang/String;
        //    83: dup            
        //    84: iconst_0       
        //    85: aload_1        
        //    86: aastore        
        //    87: invokevirtual   android/database/sqlite/SQLiteDatabase.delete:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
        //    90: pop            
        //    91: aload_2        
        //    92: arraylength    
        //    93: istore          8
        //    95: iconst_0       
        //    96: istore          4
        //    98: iload           4
        //   100: iload           8
        //   102: if_icmpge       492
        //   105: aload_2        
        //   106: iload           4
        //   108: aaload         
        //   109: astore          11
        //   111: aload_0        
        //   112: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //   115: aload_0        
        //   116: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //   119: aload_1        
        //   120: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //   123: pop            
        //   124: aload           11
        //   126: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //   129: pop            
        //   130: aload           11
        //   132: getfield        com/google/android/gms/internal/measurement/zzfu.zzava:[Lcom/google/android/gms/internal/measurement/zzfv;
        //   135: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //   138: pop            
        //   139: aload           11
        //   141: getfield        com/google/android/gms/internal/measurement/zzfu.zzauz:[Lcom/google/android/gms/internal/measurement/zzfy;
        //   144: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotNull:(Ljava/lang/Object;)Ljava/lang/Object;
        //   147: pop            
        //   148: aload           11
        //   150: getfield        com/google/android/gms/internal/measurement/zzfu.zzauy:Ljava/lang/Integer;
        //   153: ifnonnull       176
        //   156: aload_0        
        //   157: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   160: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   163: ldc_w           "Audience with no ID. appId"
        //   166: aload_1        
        //   167: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   170: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   173: goto            597
        //   176: aload           11
        //   178: getfield        com/google/android/gms/internal/measurement/zzfu.zzauy:Ljava/lang/Integer;
        //   181: invokevirtual   java/lang/Integer.intValue:()I
        //   184: istore          9
        //   186: aload           11
        //   188: getfield        com/google/android/gms/internal/measurement/zzfu.zzava:[Lcom/google/android/gms/internal/measurement/zzfv;
        //   191: astore          12
        //   193: aload           12
        //   195: arraylength    
        //   196: istore          5
        //   198: iconst_0       
        //   199: istore_3       
        //   200: iload_3        
        //   201: iload           5
        //   203: if_icmpge       257
        //   206: aload           12
        //   208: iload_3        
        //   209: aaload         
        //   210: getfield        com/google/android/gms/internal/measurement/zzfv.zzave:Ljava/lang/Integer;
        //   213: ifnonnull       562
        //   216: aload_0        
        //   217: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   220: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   223: astore          12
        //   225: ldc_w           "Event filter with no ID. Audience definition ignored. appId, audienceId"
        //   228: astore          13
        //   230: aload_1        
        //   231: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   234: astore          14
        //   236: aload           11
        //   238: getfield        com/google/android/gms/internal/measurement/zzfu.zzauy:Ljava/lang/Integer;
        //   241: astore          11
        //   243: aload           12
        //   245: aload           13
        //   247: aload           14
        //   249: aload           11
        //   251: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   254: goto            597
        //   257: aload           11
        //   259: getfield        com/google/android/gms/internal/measurement/zzfu.zzauz:[Lcom/google/android/gms/internal/measurement/zzfy;
        //   262: astore          12
        //   264: aload           12
        //   266: arraylength    
        //   267: istore          5
        //   269: iconst_0       
        //   270: istore_3       
        //   271: iload_3        
        //   272: iload           5
        //   274: if_icmpge       317
        //   277: aload           12
        //   279: iload_3        
        //   280: aaload         
        //   281: getfield        com/google/android/gms/internal/measurement/zzfy.zzave:Ljava/lang/Integer;
        //   284: ifnonnull       569
        //   287: aload_0        
        //   288: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   291: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjg:()Lcom/google/android/gms/measurement/internal/zzar;
        //   294: astore          12
        //   296: ldc_w           "Property filter with no ID. Audience definition ignored. appId, audienceId"
        //   299: astore          13
        //   301: aload_1        
        //   302: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   305: astore          14
        //   307: aload           11
        //   309: getfield        com/google/android/gms/internal/measurement/zzfu.zzauy:Ljava/lang/Integer;
        //   312: astore          11
        //   314: goto            243
        //   317: aload           11
        //   319: getfield        com/google/android/gms/internal/measurement/zzfu.zzava:[Lcom/google/android/gms/internal/measurement/zzfv;
        //   322: astore          12
        //   324: aload           12
        //   326: arraylength    
        //   327: istore          5
        //   329: iconst_0       
        //   330: istore_3       
        //   331: iload_3        
        //   332: iload           5
        //   334: if_icmpge       583
        //   337: aload_0        
        //   338: aload_1        
        //   339: iload           9
        //   341: aload           12
        //   343: iload_3        
        //   344: aaload         
        //   345: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Ljava/lang/String;ILcom/google/android/gms/internal/measurement/zzfv;)Z
        //   348: ifne            576
        //   351: iconst_0       
        //   352: istore_3       
        //   353: goto            356
        //   356: iload_3        
        //   357: istore          6
        //   359: iload_3        
        //   360: ifeq            409
        //   363: aload           11
        //   365: getfield        com/google/android/gms/internal/measurement/zzfu.zzauz:[Lcom/google/android/gms/internal/measurement/zzfy;
        //   368: astore          11
        //   370: aload           11
        //   372: arraylength    
        //   373: istore          10
        //   375: iconst_0       
        //   376: istore          5
        //   378: iload_3        
        //   379: istore          6
        //   381: iload           5
        //   383: iload           10
        //   385: if_icmpge       409
        //   388: aload_0        
        //   389: aload_1        
        //   390: iload           9
        //   392: aload           11
        //   394: iload           5
        //   396: aaload         
        //   397: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Ljava/lang/String;ILcom/google/android/gms/internal/measurement/zzfy;)Z
        //   400: ifne            588
        //   403: iconst_0       
        //   404: istore          6
        //   406: goto            409
        //   409: iload           6
        //   411: ifne            597
        //   414: aload_0        
        //   415: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //   418: aload_0        
        //   419: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //   422: aload_1        
        //   423: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //   426: pop            
        //   427: aload_0        
        //   428: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //   431: astore          11
        //   433: aload           11
        //   435: ldc_w           "property_filters"
        //   438: ldc_w           "app_id=? and audience_id=?"
        //   441: iconst_2       
        //   442: anewarray       Ljava/lang/String;
        //   445: dup            
        //   446: iconst_0       
        //   447: aload_1        
        //   448: aastore        
        //   449: dup            
        //   450: iconst_1       
        //   451: iload           9
        //   453: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //   456: aastore        
        //   457: invokevirtual   android/database/sqlite/SQLiteDatabase.delete:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
        //   460: pop            
        //   461: aload           11
        //   463: ldc_w           "event_filters"
        //   466: ldc_w           "app_id=? and audience_id=?"
        //   469: iconst_2       
        //   470: anewarray       Ljava/lang/String;
        //   473: dup            
        //   474: iconst_0       
        //   475: aload_1        
        //   476: aastore        
        //   477: dup            
        //   478: iconst_1       
        //   479: iload           9
        //   481: invokestatic    java/lang/String.valueOf:(I)Ljava/lang/String;
        //   484: aastore        
        //   485: invokevirtual   android/database/sqlite/SQLiteDatabase.delete:(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)I
        //   488: pop            
        //   489: goto            597
        //   492: new             Ljava/util/ArrayList;
        //   495: dup            
        //   496: invokespecial   java/util/ArrayList.<init>:()V
        //   499: astore          11
        //   501: aload_2        
        //   502: arraylength    
        //   503: istore          4
        //   505: iload           7
        //   507: istore_3       
        //   508: iload_3        
        //   509: iload           4
        //   511: if_icmpge       535
        //   514: aload           11
        //   516: aload_2        
        //   517: iload_3        
        //   518: aaload         
        //   519: getfield        com/google/android/gms/internal/measurement/zzfu.zzauy:Ljava/lang/Integer;
        //   522: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   527: pop            
        //   528: iload_3        
        //   529: iconst_1       
        //   530: iadd           
        //   531: istore_3       
        //   532: goto            508
        //   535: aload_0        
        //   536: aload_1        
        //   537: aload           11
        //   539: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Ljava/lang/String;Ljava/util/List;)Z
        //   542: pop            
        //   543: aload           15
        //   545: invokevirtual   android/database/sqlite/SQLiteDatabase.setTransactionSuccessful:()V
        //   548: aload           15
        //   550: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   553: return         
        //   554: astore_1       
        //   555: aload           15
        //   557: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   560: aload_1        
        //   561: athrow         
        //   562: iload_3        
        //   563: iconst_1       
        //   564: iadd           
        //   565: istore_3       
        //   566: goto            200
        //   569: iload_3        
        //   570: iconst_1       
        //   571: iadd           
        //   572: istore_3       
        //   573: goto            271
        //   576: iload_3        
        //   577: iconst_1       
        //   578: iadd           
        //   579: istore_3       
        //   580: goto            331
        //   583: iconst_1       
        //   584: istore_3       
        //   585: goto            356
        //   588: iload           5
        //   590: iconst_1       
        //   591: iadd           
        //   592: istore          5
        //   594: goto            378
        //   597: iload           4
        //   599: iconst_1       
        //   600: iadd           
        //   601: istore          4
        //   603: goto            98
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  29     48     554    562    Any
        //  51     95     554    562    Any
        //  111    173    554    562    Any
        //  176    198    554    562    Any
        //  206    225    554    562    Any
        //  230    243    554    562    Any
        //  243    254    554    562    Any
        //  257    269    554    562    Any
        //  277    296    554    562    Any
        //  301    314    554    562    Any
        //  317    329    554    562    Any
        //  337    351    554    562    Any
        //  363    375    554    562    Any
        //  388    403    554    562    Any
        //  414    489    554    562    Any
        //  492    505    554    562    Any
        //  514    528    554    562    Any
        //  535    548    554    562    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final boolean zza(final zzgi zzgi, final boolean b) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public final boolean zza(final zzfj zzfj) {
        Preconditions.checkNotNull(zzfj);
        this.zzaf();
        this.zzcl();
        if (this.zzi(zzfj.zztt, zzfj.name) == null) {
            if (zzfk.zzcq(zzfj.name)) {
                if (this.zza("select count(1) from user_attributes where app_id=? and name not like '!_%' escape '!'", new String[] { zzfj.zztt }) >= 25L) {
                    return false;
                }
            }
            else {
                final long zza = this.zza("select count(1) from user_attributes where app_id=? and origin=? AND name like '!_%' escape '!'", new String[] { zzfj.zztt, zzfj.origin });
                if (this.zzgq().zze(zzfj.zztt, zzaf.zzalj)) {
                    if (!"_ap".equals(zzfj.name) && zza >= 25L) {
                        return false;
                    }
                }
                else if (zza >= 25L) {
                    return false;
                }
            }
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzfj.zztt);
        contentValues.put("origin", zzfj.origin);
        contentValues.put("name", zzfj.name);
        contentValues.put("set_timestamp", Long.valueOf(zzfj.zzaue));
        zza(contentValues, "value", zzfj.value);
        try {
            if (this.getWritableDatabase().insertWithOnConflict("user_attributes", (String)null, contentValues, 5) == -1L) {
                this.zzgo().zzjd().zzg("Failed to insert/update user property (got -1). appId", zzap.zzbv(zzfj.zztt));
                return true;
            }
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Error storing user property. appId", zzap.zzbv(zzfj.zztt), ex);
        }
        return true;
    }
    
    public final boolean zza(final zzl zzl) {
        Preconditions.checkNotNull(zzl);
        this.zzaf();
        this.zzcl();
        if (this.zzi(zzl.packageName, zzl.zzahb.name) == null && this.zza("SELECT COUNT(1) FROM conditional_properties WHERE app_id=?", new String[] { zzl.packageName }) >= 1000L) {
            return false;
        }
        final ContentValues contentValues = new ContentValues();
        contentValues.put("app_id", zzl.packageName);
        contentValues.put("origin", zzl.origin);
        contentValues.put("name", zzl.zzahb.name);
        zza(contentValues, "value", zzl.zzahb.getValue());
        contentValues.put("active", Boolean.valueOf(zzl.active));
        contentValues.put("trigger_event_name", zzl.triggerEventName);
        contentValues.put("trigger_timeout", Long.valueOf(zzl.triggerTimeout));
        this.zzgm();
        contentValues.put("timed_out_event", zzfk.zza((Parcelable)zzl.zzahc));
        contentValues.put("creation_timestamp", Long.valueOf(zzl.creationTimestamp));
        this.zzgm();
        contentValues.put("triggered_event", zzfk.zza((Parcelable)zzl.zzahd));
        contentValues.put("triggered_timestamp", Long.valueOf(zzl.zzahb.zzaue));
        contentValues.put("time_to_live", Long.valueOf(zzl.timeToLive));
        this.zzgm();
        contentValues.put("expired_event", zzfk.zza((Parcelable)zzl.zzahe));
        try {
            if (this.getWritableDatabase().insertWithOnConflict("conditional_properties", (String)null, contentValues, 5) == -1L) {
                this.zzgo().zzjd().zzg("Failed to insert/update conditional user property (got -1)", zzap.zzbv(zzl.packageName));
                return true;
            }
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Error storing conditional user property", zzap.zzbv(zzl.packageName), ex);
        }
        return true;
    }
    
    public final boolean zza(final zzy zzy, final long n, final boolean b) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public final boolean zza(final String s, final Long ex, final long n, final zzgf zzgf) {
        this.zzaf();
        this.zzcl();
        Preconditions.checkNotNull(zzgf);
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotNull(ex);
        try {
            final int zzvu = zzgf.zzvu();
            final byte[] array = new byte[zzvu];
            final zzyy zzk = zzyy.zzk(array, 0, zzvu);
            zzgf.zza(zzk);
            zzk.zzyt();
            this.zzgo().zzjl().zze("Saving complex main event, appId, data size", this.zzgl().zzbs(s), zzvu);
            final ContentValues contentValues = new ContentValues();
            contentValues.put("app_id", s);
            contentValues.put("event_id", (Long)ex);
            contentValues.put("children_to_process", Long.valueOf(n));
            contentValues.put("main_event", array);
            try {
                if (this.getWritableDatabase().insertWithOnConflict("main_event_params", (String)null, contentValues, 5) == -1L) {
                    this.zzgo().zzjd().zzg("Failed to insert complex main event (got -1). appId", zzap.zzbv(s));
                    return false;
                }
                return true;
            }
            catch (SQLiteException ex) {
                this.zzgo().zzjd().zze("Error storing complex main event. appId", zzap.zzbv(s), ex);
                return false;
            }
        }
        catch (IOException ex2) {
            this.zzgo().zzjd().zzd("Data loss. Failed to serialize event params/data. appId, eventId", zzap.zzbv(s), ex, ex2);
            return false;
        }
    }
    
    public final String zzah(final long p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     8: aconst_null    
        //     9: astore_3       
        //    10: aload_0        
        //    11: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    14: ldc_w           "select app_id from apps where app_id in (select distinct app_id from raw_events) and config_fetched_time < ? order by failed_config_fetch_time limit 1;"
        //    17: iconst_1       
        //    18: anewarray       Ljava/lang/String;
        //    21: dup            
        //    22: iconst_0       
        //    23: lload_1        
        //    24: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //    27: aastore        
        //    28: invokevirtual   android/database/sqlite/SQLiteDatabase.rawQuery:(Ljava/lang/String;[Ljava/lang/String;)Landroid/database/Cursor;
        //    31: astore          4
        //    33: aload           4
        //    35: astore_3       
        //    36: aload           4
        //    38: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    43: ifne            76
        //    46: aload           4
        //    48: astore_3       
        //    49: aload_0        
        //    50: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //    53: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjl:()Lcom/google/android/gms/measurement/internal/zzar;
        //    56: ldc_w           "No expired configs for apps with pending events"
        //    59: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzbx:(Ljava/lang/String;)V
        //    62: aload           4
        //    64: ifnull          74
        //    67: aload           4
        //    69: invokeinterface android/database/Cursor.close:()V
        //    74: aconst_null    
        //    75: areturn        
        //    76: aload           4
        //    78: astore_3       
        //    79: aload           4
        //    81: iconst_0       
        //    82: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //    87: astore          5
        //    89: aload           4
        //    91: ifnull          101
        //    94: aload           4
        //    96: invokeinterface android/database/Cursor.close:()V
        //   101: aload           5
        //   103: areturn        
        //   104: astore          5
        //   106: goto            119
        //   109: astore          4
        //   111: goto            153
        //   114: astore          5
        //   116: aconst_null    
        //   117: astore          4
        //   119: aload           4
        //   121: astore_3       
        //   122: aload_0        
        //   123: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   126: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   129: ldc_w           "Error selecting expired configs"
        //   132: aload           5
        //   134: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   137: aload           4
        //   139: ifnull          149
        //   142: aload           4
        //   144: invokeinterface android/database/Cursor.close:()V
        //   149: aconst_null    
        //   150: areturn        
        //   151: astore          4
        //   153: aload_3        
        //   154: ifnull          163
        //   157: aload_3        
        //   158: invokeinterface android/database/Cursor.close:()V
        //   163: aload           4
        //   165: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  10     33     114    119    Landroid/database/sqlite/SQLiteException;
        //  10     33     109    114    Any
        //  36     46     104    109    Landroid/database/sqlite/SQLiteException;
        //  36     46     151    153    Any
        //  49     62     104    109    Landroid/database/sqlite/SQLiteException;
        //  49     62     151    153    Any
        //  79     89     104    109    Landroid/database/sqlite/SQLiteException;
        //  79     89     151    153    Any
        //  122    137    151    153    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0074:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final List<Pair<zzgi, Long>> zzb(final String s, int n, final int n2) {
        this.zzaf();
        this.zzcl();
        Preconditions.checkArgument(n > 0);
        Preconditions.checkArgument(n2 > 0);
        Preconditions.checkNotEmpty(s);
        Object zzj = null;
        Object emptyList = null;
        Object query;
        byte[] zza = null;
        try {
            try {
                query = this.getWritableDatabase().query("queue", new String[] { "rowid", "data", "retry_count" }, "app_id=?", new String[] { s }, (String)null, (String)null, "rowid", String.valueOf(n));
                try {
                    if (!((Cursor)query).moveToFirst()) {
                        emptyList = Collections.emptyList();
                        if (query != null) {
                            ((Cursor)query).close();
                        }
                        return (List<Pair<zzgi, Long>>)emptyList;
                    }
                    emptyList = new ArrayList<Pair<zzgi, Long>>();
                    n = 0;
                    int n3;
                    do {
                        final long long1 = ((Cursor)query).getLong(0);
                        try {
                            zza = this.zzjo().zza(((Cursor)query).getBlob(1));
                            if (!((List)emptyList).isEmpty() && zza.length + n > n2) {
                                break;
                            }
                            zzj = zzyx.zzj(zza, 0, zza.length);
                            final zzgi zzgi = new zzgi();
                            try {
                                zzgi.zza((zzyx)zzj);
                                if (!((Cursor)query).isNull(2)) {
                                    zzgi.zzayc = ((Cursor)query).getInt(2);
                                }
                                n3 = n + zza.length;
                                ((List<Pair<zzgi, Long>>)emptyList).add((Pair<zzgi, Long>)Pair.create((Object)zzgi, (Object)long1));
                            }
                            catch (IOException zza) {
                                this.zzgo().zzjd().zze("Failed to merge queued bundle. appId", zzap.zzbv(s), zza);
                                n3 = n;
                            }
                        }
                        catch (IOException zza) {
                            this.zzgo().zzjd().zze("Failed to unzip queued bundle. appId", zzap.zzbv(s), zza);
                            n3 = n;
                        }
                    } while (((Cursor)query).moveToNext() && (n = n3) <= n2);
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return (List<Pair<zzgi, Long>>)emptyList;
                }
                catch (SQLiteException zza) {}
                finally {
                    emptyList = query;
                }
            }
            finally {}
        }
        catch (SQLiteException zza) {
            query = zzj;
        }
        this.zzgo().zzjd().zze("Error querying bundles. appId", zzap.zzbv(s), zza);
        final List<Pair<zzgi, Long>> emptyList2 = Collections.emptyList();
        if (query != null) {
            ((Cursor)query).close();
        }
        return emptyList2;
        if (emptyList != null) {
            ((Cursor)emptyList).close();
        }
    }
    
    public final List<zzfj> zzb(final String p0, final String p1, final String p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    13: new             Ljava/util/ArrayList;
        //    16: dup            
        //    17: invokespecial   java/util/ArrayList.<init>:()V
        //    20: astore          10
        //    22: aconst_null    
        //    23: astore          9
        //    25: new             Ljava/util/ArrayList;
        //    28: dup            
        //    29: iconst_3       
        //    30: invokespecial   java/util/ArrayList.<init>:(I)V
        //    33: astore          11
        //    35: aload           11
        //    37: aload_1        
        //    38: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    43: pop            
        //    44: new             Ljava/lang/StringBuilder;
        //    47: dup            
        //    48: ldc_w           "app_id=?"
        //    51: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    54: astore          7
        //    56: aload_2        
        //    57: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    60: istore          4
        //    62: iload           4
        //    64: ifne            568
        //    67: aload           11
        //    69: aload_2        
        //    70: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //    75: pop            
        //    76: aload           7
        //    78: ldc_w           " and origin=?"
        //    81: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    84: pop            
        //    85: goto            88
        //    88: aload_2        
        //    89: astore          8
        //    91: aload_3        
        //    92: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //    95: ifne            125
        //    98: aload           11
        //   100: aload_3        
        //   101: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //   104: ldc_w           "*"
        //   107: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //   110: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   115: pop            
        //   116: aload           7
        //   118: ldc_w           " and name glob ?"
        //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   124: pop            
        //   125: aload           11
        //   127: aload           11
        //   129: invokeinterface java/util/List.size:()I
        //   134: anewarray       Ljava/lang/String;
        //   137: invokeinterface java/util/List.toArray:([Ljava/lang/Object;)[Ljava/lang/Object;
        //   142: checkcast       [Ljava/lang/String;
        //   145: astore          11
        //   147: aload_0        
        //   148: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //   151: astore          12
        //   153: aload           7
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: astore          7
        //   160: aload           12
        //   162: ldc_w           "user_attributes"
        //   165: iconst_4       
        //   166: anewarray       Ljava/lang/String;
        //   169: dup            
        //   170: iconst_0       
        //   171: ldc_w           "name"
        //   174: aastore        
        //   175: dup            
        //   176: iconst_1       
        //   177: ldc_w           "set_timestamp"
        //   180: aastore        
        //   181: dup            
        //   182: iconst_2       
        //   183: ldc_w           "value"
        //   186: aastore        
        //   187: dup            
        //   188: iconst_3       
        //   189: ldc             "origin"
        //   191: aastore        
        //   192: aload           7
        //   194: aload           11
        //   196: aconst_null    
        //   197: aconst_null    
        //   198: ldc_w           "rowid"
        //   201: ldc_w           "1001"
        //   204: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   207: astore          7
        //   209: aload           8
        //   211: astore          9
        //   213: aload           7
        //   215: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   220: istore          4
        //   222: aload           8
        //   224: astore_2       
        //   225: iload           4
        //   227: ifne            245
        //   230: aload           7
        //   232: ifnull          242
        //   235: aload           7
        //   237: invokeinterface android/database/Cursor.close:()V
        //   242: aload           10
        //   244: areturn        
        //   245: aload_2        
        //   246: astore          9
        //   248: aload           10
        //   250: invokeinterface java/util/List.size:()I
        //   255: sipush          1000
        //   258: if_icmplt       286
        //   261: aload_2        
        //   262: astore          9
        //   264: aload_0        
        //   265: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   268: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   271: ldc_w           "Read more than the max allowed user properties, ignoring excess"
        //   274: sipush          1000
        //   277: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   280: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   283: goto            424
        //   286: aload_2        
        //   287: astore          9
        //   289: aload           7
        //   291: iconst_0       
        //   292: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   297: astore          11
        //   299: aload_2        
        //   300: astore          9
        //   302: aload           7
        //   304: iconst_1       
        //   305: invokeinterface android/database/Cursor.getLong:(I)J
        //   310: lstore          5
        //   312: aload           7
        //   314: astore          9
        //   316: aload_0        
        //   317: aload           7
        //   319: iconst_2       
        //   320: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Landroid/database/Cursor;I)Ljava/lang/Object;
        //   323: astore          12
        //   325: aload           7
        //   327: astore          9
        //   329: aload           7
        //   331: iconst_3       
        //   332: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   337: astore          8
        //   339: aload           12
        //   341: ifnonnull       378
        //   344: aload           7
        //   346: astore          9
        //   348: aload_0        
        //   349: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   352: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   355: ldc_w           "(2)Read invalid user property value, ignoring it"
        //   358: aload_1        
        //   359: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   362: aload           8
        //   364: aload_3        
        //   365: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   368: goto            406
        //   371: astore_2       
        //   372: aload           8
        //   374: astore_3       
        //   375: goto            502
        //   378: aload           7
        //   380: astore          9
        //   382: aload           10
        //   384: new             Lcom/google/android/gms/measurement/internal/zzfj;
        //   387: dup            
        //   388: aload_1        
        //   389: aload           8
        //   391: aload           11
        //   393: lload           5
        //   395: aload           12
        //   397: invokespecial   com/google/android/gms/measurement/internal/zzfj.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V
        //   400: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   405: pop            
        //   406: aload           7
        //   408: astore          9
        //   410: aload           7
        //   412: invokeinterface android/database/Cursor.moveToNext:()Z
        //   417: istore          4
        //   419: iload           4
        //   421: ifne            439
        //   424: aload           7
        //   426: ifnull          436
        //   429: aload           7
        //   431: invokeinterface android/database/Cursor.close:()V
        //   436: aload           10
        //   438: areturn        
        //   439: aload           8
        //   441: astore_2       
        //   442: goto            245
        //   445: astore_2       
        //   446: goto            450
        //   449: astore_3       
        //   450: aload           8
        //   452: astore_3       
        //   453: goto            502
        //   456: astore          8
        //   458: aload_2        
        //   459: astore_3       
        //   460: aload           8
        //   462: astore_2       
        //   463: goto            502
        //   466: astore_1       
        //   467: goto            544
        //   470: astore_2       
        //   471: aload           9
        //   473: astore_3       
        //   474: goto            463
        //   477: astore_3       
        //   478: goto            560
        //   481: astore_1       
        //   482: aload           9
        //   484: astore_2       
        //   485: goto            547
        //   488: astore_3       
        //   489: goto            478
        //   492: aconst_null    
        //   493: astore          8
        //   495: aload           7
        //   497: astore_3       
        //   498: aload           8
        //   500: astore          7
        //   502: aload           7
        //   504: astore          9
        //   506: aload_0        
        //   507: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   510: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   513: ldc_w           "(2)Error querying user properties"
        //   516: aload_1        
        //   517: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   520: aload_3        
        //   521: aload_2        
        //   522: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   525: aload           7
        //   527: ifnull          537
        //   530: aload           7
        //   532: invokeinterface android/database/Cursor.close:()V
        //   537: aconst_null    
        //   538: areturn        
        //   539: astore_1       
        //   540: aload           9
        //   542: astore          7
        //   544: aload           7
        //   546: astore_2       
        //   547: aload_2        
        //   548: ifnull          557
        //   551: aload_2        
        //   552: invokeinterface android/database/Cursor.close:()V
        //   557: aload_1        
        //   558: athrow         
        //   559: astore_3       
        //   560: aload_2        
        //   561: astore          7
        //   563: aload_3        
        //   564: astore_2       
        //   565: goto            492
        //   568: goto            88
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/util/List<Lcom/google/android/gms/measurement/internal/zzfj;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  25     35     488    492    Landroid/database/sqlite/SQLiteException;
        //  25     35     481    488    Any
        //  35     62     477    478    Landroid/database/sqlite/SQLiteException;
        //  35     62     481    488    Any
        //  67     85     559    560    Landroid/database/sqlite/SQLiteException;
        //  67     85     481    488    Any
        //  91     125    559    560    Landroid/database/sqlite/SQLiteException;
        //  91     125    481    488    Any
        //  125    209    559    560    Landroid/database/sqlite/SQLiteException;
        //  125    209    481    488    Any
        //  213    222    470    477    Landroid/database/sqlite/SQLiteException;
        //  213    222    466    470    Any
        //  248    261    470    477    Landroid/database/sqlite/SQLiteException;
        //  248    261    466    470    Any
        //  264    283    470    477    Landroid/database/sqlite/SQLiteException;
        //  264    283    466    470    Any
        //  289    299    470    477    Landroid/database/sqlite/SQLiteException;
        //  289    299    466    470    Any
        //  302    312    470    477    Landroid/database/sqlite/SQLiteException;
        //  302    312    466    470    Any
        //  316    325    456    463    Landroid/database/sqlite/SQLiteException;
        //  316    325    539    544    Any
        //  329    339    456    463    Landroid/database/sqlite/SQLiteException;
        //  329    339    539    544    Any
        //  348    368    371    378    Landroid/database/sqlite/SQLiteException;
        //  348    368    539    544    Any
        //  382    406    445    449    Landroid/database/sqlite/SQLiteException;
        //  382    406    539    544    Any
        //  410    419    445    449    Landroid/database/sqlite/SQLiteException;
        //  410    419    539    544    Any
        //  506    525    539    544    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0088:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final List<zzl> zzb(String query, final String[] array) {
        String string = null;
        while (true) {
            this.zzaf();
            this.zzcl();
            Serializable s = new ArrayList<Object>();
        Block_2:
            while (true) {
                Label_0502: {
                    try {
                        query = (String)this.getWritableDatabase().query("conditional_properties", new String[] { "app_id", "origin", "name", "value", "active", "trigger_event_name", "trigger_timeout", "timed_out_event", "creation_timestamp", "triggered_event", "triggered_timestamp", "time_to_live", "expired_event" }, query, array, (String)null, (String)null, "rowid", "1001");
                        try {
                            if (!((Cursor)query).moveToFirst()) {
                                if (query != null) {
                                    ((Cursor)query).close();
                                }
                                return (List<zzl>)s;
                            }
                            while (true) {
                                while (((List)s).size() < 1000) {
                                    string = ((Cursor)query).getString(0);
                                    final String string2 = ((Cursor)query).getString(1);
                                    final String string3 = ((Cursor)query).getString(2);
                                    final Object zza = this.zza((Cursor)query, 3);
                                    if (((Cursor)query).getInt(4) == 0) {
                                        break Label_0502;
                                    }
                                    final boolean b = true;
                                    ((List<zzl>)s).add(new zzl(string, string2, new zzfh(string3, ((Cursor)query).getLong(10), zza, string2), ((Cursor)query).getLong(8), b, ((Cursor)query).getString(5), this.zzjo().zza(((Cursor)query).getBlob(7), zzad.CREATOR), ((Cursor)query).getLong(6), this.zzjo().zza(((Cursor)query).getBlob(9), zzad.CREATOR), ((Cursor)query).getLong(11), this.zzjo().zza(((Cursor)query).getBlob(12), zzad.CREATOR)));
                                    if (!((Cursor)query).moveToNext()) {
                                        if (query != null) {
                                            ((Cursor)query).close();
                                        }
                                        return (List<zzl>)s;
                                    }
                                }
                                this.zzgo().zzjd().zzg("Read more than the max allowed conditional properties, ignoring extra", 1000);
                                continue;
                            }
                        }
                        catch (SQLiteException ex) {}
                        finally {
                            s = query;
                        }
                    }
                    catch (SQLiteException string) {
                        query = null;
                    }
                    finally {
                        string = null;
                        break;
                    }
                    break Block_2;
                }
                final boolean b = false;
                continue;
            }
            try {
                this.zzgo().zzjd().zzg("Error querying conditional user property value", string);
                final List<zzl> emptyList = Collections.emptyList();
                if (query != null) {
                    ((Cursor)query).close();
                }
                return emptyList;
            }
            finally {
                string = query;
                final String s2;
                query = s2;
            }
            break;
        }
        if (string != null) {
            ((Cursor)string).close();
        }
        throw query;
    }
    
    public final List<zzfj> zzbk(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    13: new             Ljava/util/ArrayList;
        //    16: dup            
        //    17: invokespecial   java/util/ArrayList.<init>:()V
        //    20: astore          8
        //    22: aconst_null    
        //    23: astore          6
        //    25: aload_0        
        //    26: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    29: ldc_w           "user_attributes"
        //    32: iconst_4       
        //    33: anewarray       Ljava/lang/String;
        //    36: dup            
        //    37: iconst_0       
        //    38: ldc_w           "name"
        //    41: aastore        
        //    42: dup            
        //    43: iconst_1       
        //    44: ldc             "origin"
        //    46: aastore        
        //    47: dup            
        //    48: iconst_2       
        //    49: ldc_w           "set_timestamp"
        //    52: aastore        
        //    53: dup            
        //    54: iconst_3       
        //    55: ldc_w           "value"
        //    58: aastore        
        //    59: ldc_w           "app_id=?"
        //    62: iconst_1       
        //    63: anewarray       Ljava/lang/String;
        //    66: dup            
        //    67: iconst_0       
        //    68: aload_1        
        //    69: aastore        
        //    70: aconst_null    
        //    71: aconst_null    
        //    72: ldc_w           "rowid"
        //    75: ldc_w           "1000"
        //    78: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    81: astore          5
        //    83: aload           5
        //    85: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    90: istore_2       
        //    91: iload_2        
        //    92: ifne            110
        //    95: aload           5
        //    97: ifnull          107
        //   100: aload           5
        //   102: invokeinterface android/database/Cursor.close:()V
        //   107: aload           8
        //   109: areturn        
        //   110: aload           5
        //   112: iconst_0       
        //   113: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   118: astore          9
        //   120: aload           5
        //   122: iconst_1       
        //   123: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   128: astore          6
        //   130: aload           6
        //   132: astore          7
        //   134: aload           6
        //   136: ifnonnull       144
        //   139: ldc_w           ""
        //   142: astore          7
        //   144: aload           5
        //   146: iconst_2       
        //   147: invokeinterface android/database/Cursor.getLong:(I)J
        //   152: lstore_3       
        //   153: aload           5
        //   155: astore          6
        //   157: aload_0        
        //   158: aload           5
        //   160: iconst_3       
        //   161: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Landroid/database/Cursor;I)Ljava/lang/Object;
        //   164: astore          10
        //   166: aload           10
        //   168: ifnonnull       195
        //   171: aload           5
        //   173: astore          6
        //   175: aload_0        
        //   176: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   179: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   182: ldc_w           "Read invalid user property value, ignoring it. appId"
        //   185: aload_1        
        //   186: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   189: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   192: goto            222
        //   195: aload           5
        //   197: astore          6
        //   199: aload           8
        //   201: new             Lcom/google/android/gms/measurement/internal/zzfj;
        //   204: dup            
        //   205: aload_1        
        //   206: aload           7
        //   208: aload           9
        //   210: lload_3        
        //   211: aload           10
        //   213: invokespecial   com/google/android/gms/measurement/internal/zzfj.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V
        //   216: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   221: pop            
        //   222: aload           5
        //   224: astore          6
        //   226: aload           5
        //   228: invokeinterface android/database/Cursor.moveToNext:()Z
        //   233: istore_2       
        //   234: iload_2        
        //   235: ifne            110
        //   238: aload           5
        //   240: ifnull          250
        //   243: aload           5
        //   245: invokeinterface android/database/Cursor.close:()V
        //   250: aload           8
        //   252: areturn        
        //   253: astore          6
        //   255: aload           6
        //   257: astore          7
        //   259: goto            287
        //   262: astore_1       
        //   263: goto            329
        //   266: goto            332
        //   269: astore          6
        //   271: goto            255
        //   274: astore_1       
        //   275: aload           6
        //   277: astore          5
        //   279: goto            332
        //   282: astore          7
        //   284: aconst_null    
        //   285: astore          5
        //   287: aload           5
        //   289: astore          6
        //   291: aload_0        
        //   292: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   295: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   298: ldc_w           "Error querying user properties. appId"
        //   301: aload_1        
        //   302: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   305: aload           7
        //   307: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   310: aload           5
        //   312: ifnull          322
        //   315: aload           5
        //   317: invokeinterface android/database/Cursor.close:()V
        //   322: aconst_null    
        //   323: areturn        
        //   324: astore_1       
        //   325: aload           6
        //   327: astore          5
        //   329: goto            266
        //   332: aload           5
        //   334: ifnull          344
        //   337: aload           5
        //   339: invokeinterface android/database/Cursor.close:()V
        //   344: aload_1        
        //   345: athrow         
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/List<Lcom/google/android/gms/measurement/internal/zzfj;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  25     83     282    287    Landroid/database/sqlite/SQLiteException;
        //  25     83     274    282    Any
        //  83     91     269    274    Landroid/database/sqlite/SQLiteException;
        //  83     91     262    266    Any
        //  110    130    269    274    Landroid/database/sqlite/SQLiteException;
        //  110    130    262    266    Any
        //  144    153    269    274    Landroid/database/sqlite/SQLiteException;
        //  144    153    262    266    Any
        //  157    166    253    255    Landroid/database/sqlite/SQLiteException;
        //  157    166    324    329    Any
        //  175    192    253    255    Landroid/database/sqlite/SQLiteException;
        //  175    192    324    329    Any
        //  199    222    253    255    Landroid/database/sqlite/SQLiteException;
        //  199    222    324    329    Any
        //  226    234    253    255    Landroid/database/sqlite/SQLiteException;
        //  226    234    324    329    Any
        //  291    310    324    329    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0195:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final zzg zzbl(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    13: aconst_null    
        //    14: astore          7
        //    16: aload_0        
        //    17: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    20: astore          6
        //    22: iconst_1       
        //    23: istore_3       
        //    24: aload           6
        //    26: ldc_w           "apps"
        //    29: bipush          26
        //    31: anewarray       Ljava/lang/String;
        //    34: dup            
        //    35: iconst_0       
        //    36: ldc_w           "app_instance_id"
        //    39: aastore        
        //    40: dup            
        //    41: iconst_1       
        //    42: ldc_w           "gmp_app_id"
        //    45: aastore        
        //    46: dup            
        //    47: iconst_2       
        //    48: ldc_w           "resettable_device_id_hash"
        //    51: aastore        
        //    52: dup            
        //    53: iconst_3       
        //    54: ldc_w           "last_bundle_index"
        //    57: aastore        
        //    58: dup            
        //    59: iconst_4       
        //    60: ldc             "last_bundle_start_timestamp"
        //    62: aastore        
        //    63: dup            
        //    64: iconst_5       
        //    65: ldc_w           "last_bundle_end_timestamp"
        //    68: aastore        
        //    69: dup            
        //    70: bipush          6
        //    72: ldc             "app_version"
        //    74: aastore        
        //    75: dup            
        //    76: bipush          7
        //    78: ldc             "app_store"
        //    80: aastore        
        //    81: dup            
        //    82: bipush          8
        //    84: ldc             "gmp_version"
        //    86: aastore        
        //    87: dup            
        //    88: bipush          9
        //    90: ldc             "dev_cert_hash"
        //    92: aastore        
        //    93: dup            
        //    94: bipush          10
        //    96: ldc             "measurement_enabled"
        //    98: aastore        
        //    99: dup            
        //   100: bipush          11
        //   102: ldc             "day"
        //   104: aastore        
        //   105: dup            
        //   106: bipush          12
        //   108: ldc             "daily_public_events_count"
        //   110: aastore        
        //   111: dup            
        //   112: bipush          13
        //   114: ldc             "daily_events_count"
        //   116: aastore        
        //   117: dup            
        //   118: bipush          14
        //   120: ldc             "daily_conversions_count"
        //   122: aastore        
        //   123: dup            
        //   124: bipush          15
        //   126: ldc             "config_fetched_time"
        //   128: aastore        
        //   129: dup            
        //   130: bipush          16
        //   132: ldc             "failed_config_fetch_time"
        //   134: aastore        
        //   135: dup            
        //   136: bipush          17
        //   138: ldc             "app_version_int"
        //   140: aastore        
        //   141: dup            
        //   142: bipush          18
        //   144: ldc             "firebase_instance_id"
        //   146: aastore        
        //   147: dup            
        //   148: bipush          19
        //   150: ldc             "daily_error_events_count"
        //   152: aastore        
        //   153: dup            
        //   154: bipush          20
        //   156: ldc             "daily_realtime_events_count"
        //   158: aastore        
        //   159: dup            
        //   160: bipush          21
        //   162: ldc             "health_monitor_sample"
        //   164: aastore        
        //   165: dup            
        //   166: bipush          22
        //   168: ldc             "android_id"
        //   170: aastore        
        //   171: dup            
        //   172: bipush          23
        //   174: ldc             "adid_reporting_enabled"
        //   176: aastore        
        //   177: dup            
        //   178: bipush          24
        //   180: ldc             "ssaid_reporting_enabled"
        //   182: aastore        
        //   183: dup            
        //   184: bipush          25
        //   186: ldc             "admob_app_id"
        //   188: aastore        
        //   189: ldc_w           "app_id=?"
        //   192: iconst_1       
        //   193: anewarray       Ljava/lang/String;
        //   196: dup            
        //   197: iconst_0       
        //   198: aload_1        
        //   199: aastore        
        //   200: aconst_null    
        //   201: aconst_null    
        //   202: aconst_null    
        //   203: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   206: astore          6
        //   208: aload           6
        //   210: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   215: istore_2       
        //   216: iload_2        
        //   217: ifne            234
        //   220: aload           6
        //   222: ifnull          232
        //   225: aload           6
        //   227: invokeinterface android/database/Cursor.close:()V
        //   232: aconst_null    
        //   233: areturn        
        //   234: aload           6
        //   236: astore          7
        //   238: new             Lcom/google/android/gms/measurement/internal/zzg;
        //   241: dup            
        //   242: aload_0        
        //   243: getfield        com/google/android/gms/measurement/internal/zzq.zzamz:Lcom/google/android/gms/measurement/internal/zzfa;
        //   246: invokevirtual   com/google/android/gms/measurement/internal/zzfa.zzmb:()Lcom/google/android/gms/measurement/internal/zzbt;
        //   249: aload_1        
        //   250: invokespecial   com/google/android/gms/measurement/internal/zzg.<init>:(Lcom/google/android/gms/measurement/internal/zzbt;Ljava/lang/String;)V
        //   253: astore          8
        //   255: aload           6
        //   257: astore          7
        //   259: aload           8
        //   261: aload           6
        //   263: iconst_0       
        //   264: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   269: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzam:(Ljava/lang/String;)V
        //   272: aload           6
        //   274: astore          7
        //   276: aload           8
        //   278: aload           6
        //   280: iconst_1       
        //   281: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   286: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzan:(Ljava/lang/String;)V
        //   289: aload           6
        //   291: astore          7
        //   293: aload           8
        //   295: aload           6
        //   297: iconst_2       
        //   298: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   303: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzap:(Ljava/lang/String;)V
        //   306: aload           6
        //   308: astore          7
        //   310: aload           8
        //   312: aload           6
        //   314: iconst_3       
        //   315: invokeinterface android/database/Cursor.getLong:(I)J
        //   320: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzx:(J)V
        //   323: aload           6
        //   325: astore          7
        //   327: aload           8
        //   329: aload           6
        //   331: iconst_4       
        //   332: invokeinterface android/database/Cursor.getLong:(I)J
        //   337: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzs:(J)V
        //   340: aload           6
        //   342: astore          7
        //   344: aload           8
        //   346: aload           6
        //   348: iconst_5       
        //   349: invokeinterface android/database/Cursor.getLong:(I)J
        //   354: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzt:(J)V
        //   357: aload           6
        //   359: astore          7
        //   361: aload           8
        //   363: aload           6
        //   365: bipush          6
        //   367: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   372: invokevirtual   com/google/android/gms/measurement/internal/zzg.setAppVersion:(Ljava/lang/String;)V
        //   375: aload           6
        //   377: astore          7
        //   379: aload           8
        //   381: aload           6
        //   383: bipush          7
        //   385: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   390: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzar:(Ljava/lang/String;)V
        //   393: aload           6
        //   395: astore          7
        //   397: aload           8
        //   399: aload           6
        //   401: bipush          8
        //   403: invokeinterface android/database/Cursor.getLong:(I)J
        //   408: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzv:(J)V
        //   411: aload           6
        //   413: astore          7
        //   415: aload           8
        //   417: aload           6
        //   419: bipush          9
        //   421: invokeinterface android/database/Cursor.getLong:(I)J
        //   426: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzw:(J)V
        //   429: aload           6
        //   431: astore          7
        //   433: aload           6
        //   435: bipush          10
        //   437: invokeinterface android/database/Cursor.isNull:(I)Z
        //   442: ifne            1022
        //   445: aload           6
        //   447: astore          7
        //   449: aload           6
        //   451: bipush          10
        //   453: invokeinterface android/database/Cursor.getInt:(I)I
        //   458: ifeq            1017
        //   461: goto            1022
        //   464: aload           6
        //   466: astore          7
        //   468: aload           8
        //   470: iload_2        
        //   471: invokevirtual   com/google/android/gms/measurement/internal/zzg.setMeasurementEnabled:(Z)V
        //   474: aload           6
        //   476: astore          7
        //   478: aload           8
        //   480: aload           6
        //   482: bipush          11
        //   484: invokeinterface android/database/Cursor.getLong:(I)J
        //   489: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzaa:(J)V
        //   492: aload           6
        //   494: astore          7
        //   496: aload           8
        //   498: aload           6
        //   500: bipush          12
        //   502: invokeinterface android/database/Cursor.getLong:(I)J
        //   507: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzab:(J)V
        //   510: aload           6
        //   512: astore          7
        //   514: aload           8
        //   516: aload           6
        //   518: bipush          13
        //   520: invokeinterface android/database/Cursor.getLong:(I)J
        //   525: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzac:(J)V
        //   528: aload           6
        //   530: astore          7
        //   532: aload           8
        //   534: aload           6
        //   536: bipush          14
        //   538: invokeinterface android/database/Cursor.getLong:(I)J
        //   543: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzad:(J)V
        //   546: aload           6
        //   548: astore          7
        //   550: aload           8
        //   552: aload           6
        //   554: bipush          15
        //   556: invokeinterface android/database/Cursor.getLong:(I)J
        //   561: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzy:(J)V
        //   564: aload           6
        //   566: astore          7
        //   568: aload           8
        //   570: aload           6
        //   572: bipush          16
        //   574: invokeinterface android/database/Cursor.getLong:(I)J
        //   579: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzz:(J)V
        //   582: aload           6
        //   584: astore          7
        //   586: aload           6
        //   588: bipush          17
        //   590: invokeinterface android/database/Cursor.isNull:(I)Z
        //   595: ifeq            606
        //   598: ldc2_w          -2147483648
        //   601: lstore          4
        //   603: goto            622
        //   606: aload           6
        //   608: astore          7
        //   610: aload           6
        //   612: bipush          17
        //   614: invokeinterface android/database/Cursor.getInt:(I)I
        //   619: i2l            
        //   620: lstore          4
        //   622: aload           6
        //   624: astore          7
        //   626: aload           8
        //   628: lload           4
        //   630: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzu:(J)V
        //   633: aload           6
        //   635: astore          7
        //   637: aload           8
        //   639: aload           6
        //   641: bipush          18
        //   643: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   648: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzaq:(Ljava/lang/String;)V
        //   651: aload           6
        //   653: astore          7
        //   655: aload           8
        //   657: aload           6
        //   659: bipush          19
        //   661: invokeinterface android/database/Cursor.getLong:(I)J
        //   666: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzaf:(J)V
        //   669: aload           6
        //   671: astore          7
        //   673: aload           8
        //   675: aload           6
        //   677: bipush          20
        //   679: invokeinterface android/database/Cursor.getLong:(I)J
        //   684: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzae:(J)V
        //   687: aload           6
        //   689: astore          7
        //   691: aload           8
        //   693: aload           6
        //   695: bipush          21
        //   697: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   702: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzas:(Ljava/lang/String;)V
        //   705: aload           6
        //   707: astore          7
        //   709: aload           6
        //   711: bipush          22
        //   713: invokeinterface android/database/Cursor.isNull:(I)Z
        //   718: ifeq            727
        //   721: lconst_0       
        //   722: lstore          4
        //   724: goto            742
        //   727: aload           6
        //   729: astore          7
        //   731: aload           6
        //   733: bipush          22
        //   735: invokeinterface android/database/Cursor.getLong:(I)J
        //   740: lstore          4
        //   742: aload           6
        //   744: astore          7
        //   746: aload           8
        //   748: lload           4
        //   750: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzag:(J)V
        //   753: aload           6
        //   755: astore          7
        //   757: aload           6
        //   759: bipush          23
        //   761: invokeinterface android/database/Cursor.isNull:(I)Z
        //   766: ifne            1032
        //   769: aload           6
        //   771: astore          7
        //   773: aload           6
        //   775: bipush          23
        //   777: invokeinterface android/database/Cursor.getInt:(I)I
        //   782: ifeq            1027
        //   785: goto            1032
        //   788: aload           6
        //   790: astore          7
        //   792: aload           8
        //   794: iload_2        
        //   795: invokevirtual   com/google/android/gms/measurement/internal/zzg.zze:(Z)V
        //   798: iload_3        
        //   799: istore_2       
        //   800: aload           6
        //   802: astore          7
        //   804: aload           6
        //   806: bipush          24
        //   808: invokeinterface android/database/Cursor.isNull:(I)Z
        //   813: ifne            837
        //   816: aload           6
        //   818: astore          7
        //   820: aload           6
        //   822: bipush          24
        //   824: invokeinterface android/database/Cursor.getInt:(I)I
        //   829: ifeq            1037
        //   832: iload_3        
        //   833: istore_2       
        //   834: goto            837
        //   837: aload           6
        //   839: astore          7
        //   841: aload           8
        //   843: iload_2        
        //   844: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzf:(Z)V
        //   847: aload           6
        //   849: astore          7
        //   851: aload           8
        //   853: aload           6
        //   855: bipush          25
        //   857: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   862: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzao:(Ljava/lang/String;)V
        //   865: aload           6
        //   867: astore          7
        //   869: aload           8
        //   871: invokevirtual   com/google/android/gms/measurement/internal/zzg.zzgv:()V
        //   874: aload           6
        //   876: astore          7
        //   878: aload           6
        //   880: invokeinterface android/database/Cursor.moveToNext:()Z
        //   885: ifeq            909
        //   888: aload           6
        //   890: astore          7
        //   892: aload_0        
        //   893: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   896: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   899: ldc_w           "Got multiple records for app, expected one. appId"
        //   902: aload_1        
        //   903: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   906: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   909: aload           6
        //   911: ifnull          921
        //   914: aload           6
        //   916: invokeinterface android/database/Cursor.close:()V
        //   921: aload           8
        //   923: areturn        
        //   924: astore          7
        //   926: goto            935
        //   929: astore_1       
        //   930: goto            942
        //   933: astore          7
        //   935: aload           7
        //   937: astore          8
        //   939: goto            958
        //   942: goto            1003
        //   945: astore_1       
        //   946: aload           7
        //   948: astore          6
        //   950: goto            1003
        //   953: astore          8
        //   955: aconst_null    
        //   956: astore          6
        //   958: aload           6
        //   960: astore          7
        //   962: aload_0        
        //   963: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   966: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   969: ldc_w           "Error querying app. appId"
        //   972: aload_1        
        //   973: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   976: aload           8
        //   978: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   981: aload           6
        //   983: ifnull          993
        //   986: aload           6
        //   988: invokeinterface android/database/Cursor.close:()V
        //   993: aconst_null    
        //   994: areturn        
        //   995: astore_1       
        //   996: aload           7
        //   998: astore          6
        //  1000: goto            942
        //  1003: aload           6
        //  1005: ifnull          1015
        //  1008: aload           6
        //  1010: invokeinterface android/database/Cursor.close:()V
        //  1015: aload_1        
        //  1016: athrow         
        //  1017: iconst_0       
        //  1018: istore_2       
        //  1019: goto            464
        //  1022: iconst_1       
        //  1023: istore_2       
        //  1024: goto            464
        //  1027: iconst_0       
        //  1028: istore_2       
        //  1029: goto            788
        //  1032: iconst_1       
        //  1033: istore_2       
        //  1034: goto            788
        //  1037: iconst_0       
        //  1038: istore_2       
        //  1039: goto            837
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  16     22     953    958    Landroid/database/sqlite/SQLiteException;
        //  16     22     945    953    Any
        //  24     208    953    958    Landroid/database/sqlite/SQLiteException;
        //  24     208    945    953    Any
        //  208    216    933    935    Landroid/database/sqlite/SQLiteException;
        //  208    216    929    933    Any
        //  238    255    924    929    Landroid/database/sqlite/SQLiteException;
        //  238    255    995    1003   Any
        //  259    272    924    929    Landroid/database/sqlite/SQLiteException;
        //  259    272    995    1003   Any
        //  276    289    924    929    Landroid/database/sqlite/SQLiteException;
        //  276    289    995    1003   Any
        //  293    306    924    929    Landroid/database/sqlite/SQLiteException;
        //  293    306    995    1003   Any
        //  310    323    924    929    Landroid/database/sqlite/SQLiteException;
        //  310    323    995    1003   Any
        //  327    340    924    929    Landroid/database/sqlite/SQLiteException;
        //  327    340    995    1003   Any
        //  344    357    924    929    Landroid/database/sqlite/SQLiteException;
        //  344    357    995    1003   Any
        //  361    375    924    929    Landroid/database/sqlite/SQLiteException;
        //  361    375    995    1003   Any
        //  379    393    924    929    Landroid/database/sqlite/SQLiteException;
        //  379    393    995    1003   Any
        //  397    411    924    929    Landroid/database/sqlite/SQLiteException;
        //  397    411    995    1003   Any
        //  415    429    924    929    Landroid/database/sqlite/SQLiteException;
        //  415    429    995    1003   Any
        //  433    445    924    929    Landroid/database/sqlite/SQLiteException;
        //  433    445    995    1003   Any
        //  449    461    924    929    Landroid/database/sqlite/SQLiteException;
        //  449    461    995    1003   Any
        //  468    474    924    929    Landroid/database/sqlite/SQLiteException;
        //  468    474    995    1003   Any
        //  478    492    924    929    Landroid/database/sqlite/SQLiteException;
        //  478    492    995    1003   Any
        //  496    510    924    929    Landroid/database/sqlite/SQLiteException;
        //  496    510    995    1003   Any
        //  514    528    924    929    Landroid/database/sqlite/SQLiteException;
        //  514    528    995    1003   Any
        //  532    546    924    929    Landroid/database/sqlite/SQLiteException;
        //  532    546    995    1003   Any
        //  550    564    924    929    Landroid/database/sqlite/SQLiteException;
        //  550    564    995    1003   Any
        //  568    582    924    929    Landroid/database/sqlite/SQLiteException;
        //  568    582    995    1003   Any
        //  586    598    924    929    Landroid/database/sqlite/SQLiteException;
        //  586    598    995    1003   Any
        //  610    622    924    929    Landroid/database/sqlite/SQLiteException;
        //  610    622    995    1003   Any
        //  626    633    924    929    Landroid/database/sqlite/SQLiteException;
        //  626    633    995    1003   Any
        //  637    651    924    929    Landroid/database/sqlite/SQLiteException;
        //  637    651    995    1003   Any
        //  655    669    924    929    Landroid/database/sqlite/SQLiteException;
        //  655    669    995    1003   Any
        //  673    687    924    929    Landroid/database/sqlite/SQLiteException;
        //  673    687    995    1003   Any
        //  691    705    924    929    Landroid/database/sqlite/SQLiteException;
        //  691    705    995    1003   Any
        //  709    721    924    929    Landroid/database/sqlite/SQLiteException;
        //  709    721    995    1003   Any
        //  731    742    924    929    Landroid/database/sqlite/SQLiteException;
        //  731    742    995    1003   Any
        //  746    753    924    929    Landroid/database/sqlite/SQLiteException;
        //  746    753    995    1003   Any
        //  757    769    924    929    Landroid/database/sqlite/SQLiteException;
        //  757    769    995    1003   Any
        //  773    785    924    929    Landroid/database/sqlite/SQLiteException;
        //  773    785    995    1003   Any
        //  792    798    924    929    Landroid/database/sqlite/SQLiteException;
        //  792    798    995    1003   Any
        //  804    816    924    929    Landroid/database/sqlite/SQLiteException;
        //  804    816    995    1003   Any
        //  820    832    924    929    Landroid/database/sqlite/SQLiteException;
        //  820    832    995    1003   Any
        //  841    847    924    929    Landroid/database/sqlite/SQLiteException;
        //  841    847    995    1003   Any
        //  851    865    924    929    Landroid/database/sqlite/SQLiteException;
        //  851    865    995    1003   Any
        //  869    874    924    929    Landroid/database/sqlite/SQLiteException;
        //  869    874    995    1003   Any
        //  878    888    924    929    Landroid/database/sqlite/SQLiteException;
        //  878    888    995    1003   Any
        //  892    909    924    929    Landroid/database/sqlite/SQLiteException;
        //  892    909    995    1003   Any
        //  962    981    995    1003   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0464:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final long zzbm(final String s) {
        Preconditions.checkNotEmpty(s);
        this.zzaf();
        this.zzcl();
        try {
            return this.getWritableDatabase().delete("raw_events", "rowid in (select rowid from raw_events where app_id=? order by rowid desc limit -1 offset ?)", new String[] { s, String.valueOf(Math.max(0, Math.min(1000000, this.zzgq().zzb(s, zzaf.zzajs)))) });
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zze("Error deleting over the limit events. appId", zzap.zzbv(s), ex);
            return 0L;
        }
    }
    
    public final byte[] zzbn(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_0        
        //     6: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     9: aload_0        
        //    10: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    13: aconst_null    
        //    14: astore_3       
        //    15: aload_0        
        //    16: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    19: ldc_w           "apps"
        //    22: iconst_1       
        //    23: anewarray       Ljava/lang/String;
        //    26: dup            
        //    27: iconst_0       
        //    28: ldc             "remote_config"
        //    30: aastore        
        //    31: ldc_w           "app_id=?"
        //    34: iconst_1       
        //    35: anewarray       Ljava/lang/String;
        //    38: dup            
        //    39: iconst_0       
        //    40: aload_1        
        //    41: aastore        
        //    42: aconst_null    
        //    43: aconst_null    
        //    44: aconst_null    
        //    45: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    48: astore          4
        //    50: aload           4
        //    52: astore_3       
        //    53: aload           4
        //    55: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    60: istore_2       
        //    61: iload_2        
        //    62: ifne            79
        //    65: aload           4
        //    67: ifnull          77
        //    70: aload           4
        //    72: invokeinterface android/database/Cursor.close:()V
        //    77: aconst_null    
        //    78: areturn        
        //    79: aload           4
        //    81: astore_3       
        //    82: aload           4
        //    84: iconst_0       
        //    85: invokeinterface android/database/Cursor.getBlob:(I)[B
        //    90: astore          5
        //    92: aload           4
        //    94: astore_3       
        //    95: aload           4
        //    97: invokeinterface android/database/Cursor.moveToNext:()Z
        //   102: ifeq            125
        //   105: aload           4
        //   107: astore_3       
        //   108: aload_0        
        //   109: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   112: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   115: ldc_w           "Got multiple records for app config, expected one. appId"
        //   118: aload_1        
        //   119: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   122: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   125: aload           4
        //   127: ifnull          137
        //   130: aload           4
        //   132: invokeinterface android/database/Cursor.close:()V
        //   137: aload           5
        //   139: areturn        
        //   140: astore          5
        //   142: goto            154
        //   145: astore_1       
        //   146: goto            191
        //   149: astore          5
        //   151: aconst_null    
        //   152: astore          4
        //   154: aload           4
        //   156: astore_3       
        //   157: aload_0        
        //   158: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   161: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   164: ldc_w           "Error querying remote config. appId"
        //   167: aload_1        
        //   168: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   171: aload           5
        //   173: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   176: aload           4
        //   178: ifnull          188
        //   181: aload           4
        //   183: invokeinterface android/database/Cursor.close:()V
        //   188: aconst_null    
        //   189: areturn        
        //   190: astore_1       
        //   191: aload_3        
        //   192: ifnull          201
        //   195: aload_3        
        //   196: invokeinterface android/database/Cursor.close:()V
        //   201: aload_1        
        //   202: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  15     50     149    154    Landroid/database/sqlite/SQLiteException;
        //  15     50     145    149    Any
        //  53     61     140    145    Landroid/database/sqlite/SQLiteException;
        //  53     61     190    191    Any
        //  82     92     140    145    Landroid/database/sqlite/SQLiteException;
        //  82     92     190    191    Any
        //  95     105    140    145    Landroid/database/sqlite/SQLiteException;
        //  95     105    190    191    Any
        //  108    125    140    145    Landroid/database/sqlite/SQLiteException;
        //  108    125    190    191    Any
        //  157    176    190    191    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0077:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    final Map<Integer, zzgj> zzbo(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     8: aload_1        
        //     9: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    12: pop            
        //    13: aload_0        
        //    14: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    17: astore          5
        //    19: aconst_null    
        //    20: astore          4
        //    22: aload           5
        //    24: ldc_w           "audience_filter_values"
        //    27: iconst_2       
        //    28: anewarray       Ljava/lang/String;
        //    31: dup            
        //    32: iconst_0       
        //    33: ldc_w           "audience_id"
        //    36: aastore        
        //    37: dup            
        //    38: iconst_1       
        //    39: ldc_w           "current_results"
        //    42: aastore        
        //    43: ldc_w           "app_id=?"
        //    46: iconst_1       
        //    47: anewarray       Ljava/lang/String;
        //    50: dup            
        //    51: iconst_0       
        //    52: aload_1        
        //    53: aastore        
        //    54: aconst_null    
        //    55: aconst_null    
        //    56: aconst_null    
        //    57: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    60: astore          5
        //    62: aload           5
        //    64: astore          4
        //    66: aload           5
        //    68: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    73: istore_3       
        //    74: iload_3        
        //    75: ifne            92
        //    78: aload           5
        //    80: ifnull          90
        //    83: aload           5
        //    85: invokeinterface android/database/Cursor.close:()V
        //    90: aconst_null    
        //    91: areturn        
        //    92: aload           5
        //    94: astore          4
        //    96: new             Landroid/support/v4/util/ArrayMap;
        //    99: dup            
        //   100: invokespecial   android/support/v4/util/ArrayMap.<init>:()V
        //   103: astore          6
        //   105: aload           5
        //   107: astore          4
        //   109: aload           5
        //   111: iconst_0       
        //   112: invokeinterface android/database/Cursor.getInt:(I)I
        //   117: istore_2       
        //   118: aload           5
        //   120: astore          4
        //   122: aload           5
        //   124: iconst_1       
        //   125: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   130: astore          7
        //   132: aload           5
        //   134: astore          4
        //   136: aload           7
        //   138: iconst_0       
        //   139: aload           7
        //   141: arraylength    
        //   142: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   145: astore          7
        //   147: aload           5
        //   149: astore          4
        //   151: new             Lcom/google/android/gms/internal/measurement/zzgj;
        //   154: dup            
        //   155: invokespecial   com/google/android/gms/internal/measurement/zzgj.<init>:()V
        //   158: astore          8
        //   160: aload           5
        //   162: astore          4
        //   164: aload           8
        //   166: aload           7
        //   168: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   171: pop            
        //   172: aload           5
        //   174: astore          4
        //   176: aload           6
        //   178: iload_2        
        //   179: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   182: aload           8
        //   184: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   189: pop            
        //   190: goto            222
        //   193: astore          7
        //   195: aload           5
        //   197: astore          4
        //   199: aload_0        
        //   200: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   203: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   206: ldc_w           "Failed to merge filter results. appId, audienceId, error"
        //   209: aload_1        
        //   210: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   213: iload_2        
        //   214: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   217: aload           7
        //   219: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   222: aload           5
        //   224: astore          4
        //   226: aload           5
        //   228: invokeinterface android/database/Cursor.moveToNext:()Z
        //   233: istore_3       
        //   234: iload_3        
        //   235: ifne            105
        //   238: aload           5
        //   240: ifnull          250
        //   243: aload           5
        //   245: invokeinterface android/database/Cursor.close:()V
        //   250: aload           6
        //   252: areturn        
        //   253: astore          6
        //   255: goto            267
        //   258: astore_1       
        //   259: goto            305
        //   262: astore          6
        //   264: aconst_null    
        //   265: astore          5
        //   267: aload           5
        //   269: astore          4
        //   271: aload_0        
        //   272: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   275: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   278: ldc_w           "Database error querying filter results. appId"
        //   281: aload_1        
        //   282: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   285: aload           6
        //   287: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   290: aload           5
        //   292: ifnull          302
        //   295: aload           5
        //   297: invokeinterface android/database/Cursor.close:()V
        //   302: aconst_null    
        //   303: areturn        
        //   304: astore_1       
        //   305: aload           4
        //   307: ifnull          317
        //   310: aload           4
        //   312: invokeinterface android/database/Cursor.close:()V
        //   317: aload_1        
        //   318: athrow         
        //    Signature:
        //  (Ljava/lang/String;)Ljava/util/Map<Ljava/lang/Integer;Lcom/google/android/gms/internal/measurement/zzgj;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  22     62     262    267    Landroid/database/sqlite/SQLiteException;
        //  22     62     258    262    Any
        //  66     74     253    258    Landroid/database/sqlite/SQLiteException;
        //  66     74     304    305    Any
        //  96     105    253    258    Landroid/database/sqlite/SQLiteException;
        //  96     105    304    305    Any
        //  109    118    253    258    Landroid/database/sqlite/SQLiteException;
        //  109    118    304    305    Any
        //  122    132    253    258    Landroid/database/sqlite/SQLiteException;
        //  122    132    304    305    Any
        //  136    147    253    258    Landroid/database/sqlite/SQLiteException;
        //  136    147    304    305    Any
        //  151    160    253    258    Landroid/database/sqlite/SQLiteException;
        //  151    160    304    305    Any
        //  164    172    193    222    Ljava/io/IOException;
        //  164    172    253    258    Landroid/database/sqlite/SQLiteException;
        //  164    172    304    305    Any
        //  176    190    253    258    Landroid/database/sqlite/SQLiteException;
        //  176    190    304    305    Any
        //  199    222    253    258    Landroid/database/sqlite/SQLiteException;
        //  199    222    304    305    Any
        //  226    234    253    258    Landroid/database/sqlite/SQLiteException;
        //  226    234    304    305    Any
        //  271    290    304    305    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0090:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final long zzbp(final String s) {
        Preconditions.checkNotEmpty(s);
        return this.zza("select count(1) from events where app_id=? and name not like '!_%' escape '!'", new String[] { s }, 0L);
    }
    
    public final List<zzl> zzc(final String s, final String s2, final String s3) {
        Preconditions.checkNotEmpty(s);
        this.zzaf();
        this.zzcl();
        final ArrayList<String> list = new ArrayList<String>(3);
        list.add(s);
        final StringBuilder sb = new StringBuilder("app_id=?");
        if (!TextUtils.isEmpty((CharSequence)s2)) {
            list.add(s2);
            sb.append(" and origin=?");
        }
        if (!TextUtils.isEmpty((CharSequence)s3)) {
            list.add(String.valueOf(s3).concat("*"));
            sb.append(" and name glob ?");
        }
        return this.zzb(sb.toString(), list.toArray(new String[list.size()]));
    }
    
    final void zzc(final List<Long> list) {
        this.zzaf();
        this.zzcl();
        Preconditions.checkNotNull(list);
        Preconditions.checkNotZero(list.size());
        if (!this.zzil()) {
            return;
        }
        final String join = TextUtils.join((CharSequence)",", (Iterable)list);
        final StringBuilder sb = new StringBuilder(String.valueOf(join).length() + 2);
        sb.append("(");
        sb.append(join);
        sb.append(")");
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder(String.valueOf(string).length() + 80);
        sb2.append("SELECT COUNT(1) FROM queue WHERE rowid IN ");
        sb2.append(string);
        sb2.append(" AND retry_count =  2147483647 LIMIT 1");
        if (this.zza(sb2.toString(), (String[])null) > 0L) {
            this.zzgo().zzjg().zzbx("The number of upload retries exceeds the limit. Will remain unchanged.");
        }
        try {
            final SQLiteDatabase writableDatabase = this.getWritableDatabase();
            final StringBuilder sb3 = new StringBuilder(String.valueOf(string).length() + 127);
            sb3.append("UPDATE queue SET retry_count = IFNULL(retry_count, 0) + 1 WHERE rowid IN ");
            sb3.append(string);
            sb3.append(" AND (retry_count IS NULL OR retry_count < 2147483647)");
            writableDatabase.execSQL(sb3.toString());
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zzg("Error incrementing retry count. error", ex);
        }
    }
    
    public final zzz zzg(final String s, String s2) {
        Long value = null;
        Object o = null;
        boolean b;
        long long1;
        long long2;
        long long3;
        long long4;
        Boolean value2;
        Object value3;
        Long value4;
        Label_0290_Outer:Label_0195_Outer:Label_0258_Outer:
        while (true) {
            Preconditions.checkNotEmpty(s);
            Preconditions.checkNotEmpty(s2);
            this.zzaf();
            this.zzcl();
            value = null;
            while (true) {
                Label_0528: {
                    while (true) {
                        Label_0525: {
                            try {
                                o = this.getWritableDatabase();
                                b = false;
                                o = ((SQLiteDatabase)o).query("events", new String[] { "lifetime_count", "current_bundle_count", "last_fire_timestamp", "last_bundled_timestamp", "last_bundled_day", "last_sampled_complex_event_id", "last_sampling_rate", "last_exempt_from_sampling" }, "app_id=? and name=?", new String[] { s, s2 }, (String)null, (String)null, (String)null);
                                try {
                                    if (!((Cursor)o).moveToFirst()) {
                                        if (o != null) {
                                            ((Cursor)o).close();
                                        }
                                        return null;
                                    }
                                    long1 = ((Cursor)o).getLong(0);
                                    long2 = ((Cursor)o).getLong(1);
                                    long3 = ((Cursor)o).getLong(2);
                                    if (((Cursor)o).isNull(3)) {
                                        long4 = 0L;
                                        break Label_0525;
                                    }
                                    long4 = ((Cursor)o).getLong(3);
                                    break Label_0525;
                                    // iftrue(Label_0347:, o.isNull(7))
                                    // iftrue(Label_0242:, !o.isNull(5))
                                    // iftrue(Label_0212:, !o.isNull(4))
                                    while (true) {
                                    Label_0347_Outer:
                                        while (true) {
                                            value = null;
                                        Block_11:
                                            while (true) {
                                                while (true) {
                                                    Block_13: {
                                                        Label_0225: {
                                                            break Label_0225;
                                                            Label_0212: {
                                                                value = ((Cursor)o).getLong(4);
                                                            }
                                                            break Label_0225;
                                                            value2 = null;
                                                            Block_14: {
                                                                break Block_14;
                                                                break Block_13;
                                                            }
                                                            try {
                                                                value3 = new zzz(s, s2, long1, long2, long3, long4, value, (Long)value3, value4, value2);
                                                                value = (Long)o;
                                                                try {
                                                                    if (((Cursor)value).moveToNext()) {
                                                                        this.zzgo().zzjd().zzg("Got multiple records for event aggregates, expected one. appId", zzap.zzbv(s));
                                                                    }
                                                                    if (value != null) {
                                                                        ((Cursor)value).close();
                                                                    }
                                                                    return (zzz)value3;
                                                                }
                                                                catch (SQLiteException value) {}
                                                            }
                                                            catch (SQLiteException value) {}
                                                        }
                                                        break Block_11;
                                                    }
                                                    try {
                                                        if (((Cursor)o).getLong(7) == 1L) {
                                                            b = true;
                                                        }
                                                        value2 = b;
                                                        continue Label_0195_Outer;
                                                    }
                                                    catch (SQLiteException value) {
                                                        break;
                                                    }
                                                    finally {
                                                        s2 = (String)o;
                                                        goto Label_0457;
                                                    }
                                                    break;
                                                }
                                                continue Label_0290_Outer;
                                            }
                                            value3 = null;
                                            break Label_0528;
                                            continue Label_0347_Outer;
                                        }
                                        Block_12: {
                                            break Block_12;
                                            Label_0242: {
                                                value3 = ((Cursor)o).getLong(5);
                                            }
                                            break Label_0528;
                                            Label_0276:
                                            value4 = ((Cursor)o).getLong(6);
                                            continue Label_0195_Outer;
                                        }
                                        value4 = null;
                                        continue Label_0195_Outer;
                                    }
                                }
                                // iftrue(Label_0276:, !o.isNull(6))
                                catch (SQLiteException ex) {}
                            }
                            catch (SQLiteException value) {
                                o = null;
                            }
                            finally {
                                s2 = (String)value;
                            }
                            break;
                        }
                        continue Label_0258_Outer;
                    }
                }
                continue;
            }
        }
        try {
            this.zzgo().zzjd().zzd("Error querying events. appId", zzap.zzbv(s), this.zzgl().zzbs(s2), value);
            if (o != null) {
                ((Cursor)o).close();
            }
            return null;
        }
        finally {
            s2 = (String)o;
            goto Label_0457;
        }
        if (s2 != null) {
            ((Cursor)s2).close();
        }
        throw s;
    }
    
    @Override
    protected final boolean zzgt() {
        return false;
    }
    
    public final void zzh(final String s, final String s2) {
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotEmpty(s2);
        this.zzaf();
        this.zzcl();
        try {
            this.zzgo().zzjl().zzg("Deleted user attribute rows", this.getWritableDatabase().delete("user_attributes", "app_id=? and name=?", new String[] { s, s2 }));
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zzd("Error deleting user attribute. appId", zzap.zzbv(s), this.zzgl().zzbu(s2), ex);
        }
    }
    
    public final zzfj zzi(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_2        
        //     6: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     9: pop            
        //    10: aload_0        
        //    11: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //    14: aload_0        
        //    15: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    18: aconst_null    
        //    19: astore          7
        //    21: aload_0        
        //    22: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    25: ldc_w           "user_attributes"
        //    28: iconst_3       
        //    29: anewarray       Ljava/lang/String;
        //    32: dup            
        //    33: iconst_0       
        //    34: ldc_w           "set_timestamp"
        //    37: aastore        
        //    38: dup            
        //    39: iconst_1       
        //    40: ldc_w           "value"
        //    43: aastore        
        //    44: dup            
        //    45: iconst_2       
        //    46: ldc             "origin"
        //    48: aastore        
        //    49: ldc_w           "app_id=? and name=?"
        //    52: iconst_2       
        //    53: anewarray       Ljava/lang/String;
        //    56: dup            
        //    57: iconst_0       
        //    58: aload_1        
        //    59: aastore        
        //    60: dup            
        //    61: iconst_1       
        //    62: aload_2        
        //    63: aastore        
        //    64: aconst_null    
        //    65: aconst_null    
        //    66: aconst_null    
        //    67: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    70: astore          6
        //    72: aload           6
        //    74: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    79: istore_3       
        //    80: iload_3        
        //    81: ifne            98
        //    84: aload           6
        //    86: ifnull          96
        //    89: aload           6
        //    91: invokeinterface android/database/Cursor.close:()V
        //    96: aconst_null    
        //    97: areturn        
        //    98: aload           6
        //   100: iconst_0       
        //   101: invokeinterface android/database/Cursor.getLong:(I)J
        //   106: lstore          4
        //   108: aload           6
        //   110: astore          7
        //   112: aload_0        
        //   113: aload           6
        //   115: iconst_1       
        //   116: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Landroid/database/Cursor;I)Ljava/lang/Object;
        //   119: astore          8
        //   121: aload           6
        //   123: astore          7
        //   125: new             Lcom/google/android/gms/measurement/internal/zzfj;
        //   128: dup            
        //   129: aload_1        
        //   130: aload           6
        //   132: iconst_2       
        //   133: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   138: aload_2        
        //   139: lload           4
        //   141: aload           8
        //   143: invokespecial   com/google/android/gms/measurement/internal/zzfj.<init>:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JLjava/lang/Object;)V
        //   146: astore          8
        //   148: aload           6
        //   150: astore          7
        //   152: aload           6
        //   154: invokeinterface android/database/Cursor.moveToNext:()Z
        //   159: ifeq            183
        //   162: aload           6
        //   164: astore          7
        //   166: aload_0        
        //   167: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   170: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   173: ldc_w           "Got multiple records for user property, expected one. appId"
        //   176: aload_1        
        //   177: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   180: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzg:(Ljava/lang/String;Ljava/lang/Object;)V
        //   183: aload           6
        //   185: ifnull          195
        //   188: aload           6
        //   190: invokeinterface android/database/Cursor.close:()V
        //   195: aload           8
        //   197: areturn        
        //   198: astore          7
        //   200: aload           7
        //   202: astore          8
        //   204: goto            228
        //   207: astore_1       
        //   208: goto            278
        //   211: astore          7
        //   213: goto            200
        //   216: astore_1       
        //   217: aload           7
        //   219: astore_2       
        //   220: goto            281
        //   223: astore          8
        //   225: aconst_null    
        //   226: astore          6
        //   228: aload           6
        //   230: astore          7
        //   232: aload_0        
        //   233: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   236: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   239: ldc_w           "Error querying user property. appId"
        //   242: aload_1        
        //   243: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   246: aload_0        
        //   247: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //   250: aload_2        
        //   251: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbu:(Ljava/lang/String;)Ljava/lang/String;
        //   254: aload           8
        //   256: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   259: aload           6
        //   261: ifnull          271
        //   264: aload           6
        //   266: invokeinterface android/database/Cursor.close:()V
        //   271: aconst_null    
        //   272: areturn        
        //   273: astore_1       
        //   274: aload           7
        //   276: astore          6
        //   278: aload           6
        //   280: astore_2       
        //   281: aload_2        
        //   282: ifnull          291
        //   285: aload_2        
        //   286: invokeinterface android/database/Cursor.close:()V
        //   291: aload_1        
        //   292: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  21     72     223    228    Landroid/database/sqlite/SQLiteException;
        //  21     72     216    223    Any
        //  72     80     211    216    Landroid/database/sqlite/SQLiteException;
        //  72     80     207    211    Any
        //  98     108    211    216    Landroid/database/sqlite/SQLiteException;
        //  98     108    207    211    Any
        //  112    121    198    200    Landroid/database/sqlite/SQLiteException;
        //  112    121    273    278    Any
        //  125    148    198    200    Landroid/database/sqlite/SQLiteException;
        //  125    148    273    278    Any
        //  152    162    198    200    Landroid/database/sqlite/SQLiteException;
        //  152    162    273    278    Any
        //  166    183    198    200    Landroid/database/sqlite/SQLiteException;
        //  166    183    273    278    Any
        //  232    259    273    278    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0183:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final String zzid() {
        final SQLiteDatabase writableDatabase = this.getWritableDatabase();
        Cursor rawQuery = null;
        Label_0104: {
            Cursor cursor2;
            try {
                final Cursor cursor = rawQuery = writableDatabase.rawQuery("select app_id from queue order by has_realtime desc, rowid asc limit 1;", (String[])null);
                try {
                    try {
                        if (cursor.moveToFirst()) {
                            rawQuery = cursor;
                            final String string = cursor.getString(0);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return string;
                        }
                        if (cursor != null) {
                            cursor.close();
                        }
                        return null;
                    }
                    finally {}
                }
                catch (SQLiteException ex) {}
            }
            catch (SQLiteException ex) {
                cursor2 = null;
            }
            finally {
                break Label_0104;
            }
            final SQLiteException ex;
            this.zzgo().zzjd().zzg("Database error getting next bundle app id", ex);
            if (cursor2 != null) {
                cursor2.close();
            }
            return null;
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
    }
    
    public final boolean zzie() {
        return this.zza("select count(1) > 0 from queue where has_realtime = 1", (String[])null) != 0L;
    }
    
    final void zzif() {
        this.zzaf();
        this.zzcl();
        if (!this.zzil()) {
            return;
        }
        final long value = this.zzgp().zzanh.get();
        final long elapsedRealtime = this.zzbx().elapsedRealtime();
        if (Math.abs(elapsedRealtime - value) > zzaf.zzakb.get()) {
            this.zzgp().zzanh.set(elapsedRealtime);
            this.zzaf();
            this.zzcl();
            if (this.zzil()) {
                final int delete = this.getWritableDatabase().delete("queue", "abs(bundle_end_timestamp - ?) > cast(? as integer)", new String[] { String.valueOf(this.zzbx().currentTimeMillis()), String.valueOf(zzn.zzhw()) });
                if (delete > 0) {
                    this.zzgo().zzjl().zzg("Deleted stale rows. rowsDeleted", delete);
                }
            }
        }
    }
    
    public final long zzig() {
        return this.zza("select max(bundle_end_timestamp) from queue", null, 0L);
    }
    
    public final long zzih() {
        return this.zza("select max(timestamp) from raw_events", null, 0L);
    }
    
    public final boolean zzii() {
        return this.zza("select count(1) > 0 from raw_events", (String[])null) != 0L;
    }
    
    public final boolean zzij() {
        return this.zza("select count(1) > 0 from raw_events where realtime = 1", (String[])null) != 0L;
    }
    
    public final long zzik() {
        final Cursor cursor = null;
        Object o = null;
        Object o2 = null;
        Cursor cursor2;
        try {
            try {
                final Cursor rawQuery = this.getWritableDatabase().rawQuery("select rowid from raw_events order by rowid desc limit 1;", (String[])null);
                try {
                    if (!rawQuery.moveToFirst()) {
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        return -1L;
                    }
                    final long long1 = rawQuery.getLong(0);
                    if (rawQuery != null) {
                        rawQuery.close();
                    }
                    return long1;
                }
                catch (SQLiteException o) {
                    o2 = o;
                }
                finally {
                    o = rawQuery;
                }
            }
            finally {}
        }
        catch (SQLiteException o2) {
            cursor2 = cursor;
        }
        this.zzgo().zzjd().zzg("Error querying raw events", o2);
        if (cursor2 != null) {
            cursor2.close();
        }
        return -1L;
        if (o != null) {
            ((Cursor)o).close();
        }
    }
    
    public final zzl zzj(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_2        
        //     6: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     9: pop            
        //    10: aload_0        
        //    11: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //    14: aload_0        
        //    15: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    18: aconst_null    
        //    19: astore          13
        //    21: aload_0        
        //    22: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    25: ldc_w           "conditional_properties"
        //    28: bipush          11
        //    30: anewarray       Ljava/lang/String;
        //    33: dup            
        //    34: iconst_0       
        //    35: ldc             "origin"
        //    37: aastore        
        //    38: dup            
        //    39: iconst_1       
        //    40: ldc_w           "value"
        //    43: aastore        
        //    44: dup            
        //    45: iconst_2       
        //    46: ldc_w           "active"
        //    49: aastore        
        //    50: dup            
        //    51: iconst_3       
        //    52: ldc_w           "trigger_event_name"
        //    55: aastore        
        //    56: dup            
        //    57: iconst_4       
        //    58: ldc_w           "trigger_timeout"
        //    61: aastore        
        //    62: dup            
        //    63: iconst_5       
        //    64: ldc_w           "timed_out_event"
        //    67: aastore        
        //    68: dup            
        //    69: bipush          6
        //    71: ldc_w           "creation_timestamp"
        //    74: aastore        
        //    75: dup            
        //    76: bipush          7
        //    78: ldc_w           "triggered_event"
        //    81: aastore        
        //    82: dup            
        //    83: bipush          8
        //    85: ldc_w           "triggered_timestamp"
        //    88: aastore        
        //    89: dup            
        //    90: bipush          9
        //    92: ldc_w           "time_to_live"
        //    95: aastore        
        //    96: dup            
        //    97: bipush          10
        //    99: ldc_w           "expired_event"
        //   102: aastore        
        //   103: ldc_w           "app_id=? and name=?"
        //   106: iconst_2       
        //   107: anewarray       Ljava/lang/String;
        //   110: dup            
        //   111: iconst_0       
        //   112: aload_1        
        //   113: aastore        
        //   114: dup            
        //   115: iconst_1       
        //   116: aload_2        
        //   117: aastore        
        //   118: aconst_null    
        //   119: aconst_null    
        //   120: aconst_null    
        //   121: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   124: astore          12
        //   126: aload           12
        //   128: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   133: istore_3       
        //   134: iload_3        
        //   135: ifne            152
        //   138: aload           12
        //   140: ifnull          150
        //   143: aload           12
        //   145: invokeinterface android/database/Cursor.close:()V
        //   150: aconst_null    
        //   151: areturn        
        //   152: aload           12
        //   154: iconst_0       
        //   155: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   160: astore          14
        //   162: aload           12
        //   164: astore          13
        //   166: aload_0        
        //   167: aload           12
        //   169: iconst_1       
        //   170: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Landroid/database/Cursor;I)Ljava/lang/Object;
        //   173: astore          15
        //   175: aload           12
        //   177: astore          13
        //   179: aload           12
        //   181: iconst_2       
        //   182: invokeinterface android/database/Cursor.getInt:(I)I
        //   187: ifeq            549
        //   190: iconst_1       
        //   191: istore_3       
        //   192: goto            195
        //   195: aload           12
        //   197: astore          13
        //   199: aload           12
        //   201: iconst_3       
        //   202: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   207: astore          16
        //   209: aload           12
        //   211: astore          13
        //   213: aload           12
        //   215: iconst_4       
        //   216: invokeinterface android/database/Cursor.getLong:(I)J
        //   221: lstore          4
        //   223: aload           12
        //   225: astore          13
        //   227: aload_0        
        //   228: invokevirtual   com/google/android/gms/measurement/internal/zzey.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //   231: aload           12
        //   233: iconst_5       
        //   234: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   239: getstatic       com/google/android/gms/measurement/internal/zzad.CREATOR:Landroid/os/Parcelable$Creator;
        //   242: invokevirtual   com/google/android/gms/measurement/internal/zzfg.zza:([BLandroid/os/Parcelable$Creator;)Landroid/os/Parcelable;
        //   245: checkcast       Lcom/google/android/gms/measurement/internal/zzad;
        //   248: astore          17
        //   250: aload           12
        //   252: astore          13
        //   254: aload           12
        //   256: bipush          6
        //   258: invokeinterface android/database/Cursor.getLong:(I)J
        //   263: lstore          6
        //   265: aload           12
        //   267: astore          13
        //   269: aload_0        
        //   270: invokevirtual   com/google/android/gms/measurement/internal/zzey.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //   273: aload           12
        //   275: bipush          7
        //   277: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   282: getstatic       com/google/android/gms/measurement/internal/zzad.CREATOR:Landroid/os/Parcelable$Creator;
        //   285: invokevirtual   com/google/android/gms/measurement/internal/zzfg.zza:([BLandroid/os/Parcelable$Creator;)Landroid/os/Parcelable;
        //   288: checkcast       Lcom/google/android/gms/measurement/internal/zzad;
        //   291: astore          18
        //   293: aload           12
        //   295: astore          13
        //   297: aload           12
        //   299: bipush          8
        //   301: invokeinterface android/database/Cursor.getLong:(I)J
        //   306: lstore          8
        //   308: aload           12
        //   310: astore          13
        //   312: aload           12
        //   314: bipush          9
        //   316: invokeinterface android/database/Cursor.getLong:(I)J
        //   321: lstore          10
        //   323: aload           12
        //   325: astore          13
        //   327: aload_0        
        //   328: invokevirtual   com/google/android/gms/measurement/internal/zzey.zzjo:()Lcom/google/android/gms/measurement/internal/zzfg;
        //   331: aload           12
        //   333: bipush          10
        //   335: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   340: getstatic       com/google/android/gms/measurement/internal/zzad.CREATOR:Landroid/os/Parcelable$Creator;
        //   343: invokevirtual   com/google/android/gms/measurement/internal/zzfg.zza:([BLandroid/os/Parcelable$Creator;)Landroid/os/Parcelable;
        //   346: checkcast       Lcom/google/android/gms/measurement/internal/zzad;
        //   349: astore          19
        //   351: aload           12
        //   353: astore          13
        //   355: new             Lcom/google/android/gms/measurement/internal/zzl;
        //   358: dup            
        //   359: aload_1        
        //   360: aload           14
        //   362: new             Lcom/google/android/gms/measurement/internal/zzfh;
        //   365: dup            
        //   366: aload_2        
        //   367: lload           8
        //   369: aload           15
        //   371: aload           14
        //   373: invokespecial   com/google/android/gms/measurement/internal/zzfh.<init>:(Ljava/lang/String;JLjava/lang/Object;Ljava/lang/String;)V
        //   376: lload           6
        //   378: iload_3        
        //   379: aload           16
        //   381: aload           17
        //   383: lload           4
        //   385: aload           18
        //   387: lload           10
        //   389: aload           19
        //   391: invokespecial   com/google/android/gms/measurement/internal/zzl.<init>:(Ljava/lang/String;Ljava/lang/String;Lcom/google/android/gms/measurement/internal/zzfh;JZLjava/lang/String;Lcom/google/android/gms/measurement/internal/zzad;JLcom/google/android/gms/measurement/internal/zzad;JLcom/google/android/gms/measurement/internal/zzad;)V
        //   394: astore          14
        //   396: aload           12
        //   398: astore          13
        //   400: aload           12
        //   402: invokeinterface android/database/Cursor.moveToNext:()Z
        //   407: ifeq            439
        //   410: aload           12
        //   412: astore          13
        //   414: aload_0        
        //   415: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   418: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   421: ldc_w           "Got multiple records for conditional property, expected one"
        //   424: aload_1        
        //   425: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   428: aload_0        
        //   429: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //   432: aload_2        
        //   433: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbu:(Ljava/lang/String;)Ljava/lang/String;
        //   436: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   439: aload           12
        //   441: ifnull          451
        //   444: aload           12
        //   446: invokeinterface android/database/Cursor.close:()V
        //   451: aload           14
        //   453: areturn        
        //   454: astore          13
        //   456: aload           13
        //   458: astore          14
        //   460: goto            484
        //   463: astore_1       
        //   464: goto            534
        //   467: astore          13
        //   469: goto            456
        //   472: astore_1       
        //   473: aload           13
        //   475: astore_2       
        //   476: goto            537
        //   479: astore          14
        //   481: aconst_null    
        //   482: astore          12
        //   484: aload           12
        //   486: astore          13
        //   488: aload_0        
        //   489: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   492: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   495: ldc_w           "Error querying conditional property"
        //   498: aload_1        
        //   499: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   502: aload_0        
        //   503: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgl:()Lcom/google/android/gms/measurement/internal/zzan;
        //   506: aload_2        
        //   507: invokevirtual   com/google/android/gms/measurement/internal/zzan.zzbu:(Ljava/lang/String;)Ljava/lang/String;
        //   510: aload           14
        //   512: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   515: aload           12
        //   517: ifnull          527
        //   520: aload           12
        //   522: invokeinterface android/database/Cursor.close:()V
        //   527: aconst_null    
        //   528: areturn        
        //   529: astore_1       
        //   530: aload           13
        //   532: astore          12
        //   534: aload           12
        //   536: astore_2       
        //   537: aload_2        
        //   538: ifnull          547
        //   541: aload_2        
        //   542: invokeinterface android/database/Cursor.close:()V
        //   547: aload_1        
        //   548: athrow         
        //   549: iconst_0       
        //   550: istore_3       
        //   551: goto            195
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  21     126    479    484    Landroid/database/sqlite/SQLiteException;
        //  21     126    472    479    Any
        //  126    134    467    472    Landroid/database/sqlite/SQLiteException;
        //  126    134    463    467    Any
        //  152    162    467    472    Landroid/database/sqlite/SQLiteException;
        //  152    162    463    467    Any
        //  166    175    454    456    Landroid/database/sqlite/SQLiteException;
        //  166    175    529    534    Any
        //  179    190    454    456    Landroid/database/sqlite/SQLiteException;
        //  179    190    529    534    Any
        //  199    209    454    456    Landroid/database/sqlite/SQLiteException;
        //  199    209    529    534    Any
        //  213    223    454    456    Landroid/database/sqlite/SQLiteException;
        //  213    223    529    534    Any
        //  227    250    454    456    Landroid/database/sqlite/SQLiteException;
        //  227    250    529    534    Any
        //  254    265    454    456    Landroid/database/sqlite/SQLiteException;
        //  254    265    529    534    Any
        //  269    293    454    456    Landroid/database/sqlite/SQLiteException;
        //  269    293    529    534    Any
        //  297    308    454    456    Landroid/database/sqlite/SQLiteException;
        //  297    308    529    534    Any
        //  312    323    454    456    Landroid/database/sqlite/SQLiteException;
        //  312    323    529    534    Any
        //  327    351    454    456    Landroid/database/sqlite/SQLiteException;
        //  327    351    529    534    Any
        //  355    396    454    456    Landroid/database/sqlite/SQLiteException;
        //  355    396    529    534    Any
        //  400    410    454    456    Landroid/database/sqlite/SQLiteException;
        //  400    410    529    534    Any
        //  414    439    454    456    Landroid/database/sqlite/SQLiteException;
        //  414    439    529    534    Any
        //  488    515    529    534    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0195:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final int zzk(final String s, final String s2) {
        Preconditions.checkNotEmpty(s);
        Preconditions.checkNotEmpty(s2);
        this.zzaf();
        this.zzcl();
        try {
            return this.getWritableDatabase().delete("conditional_properties", "app_id=? and name=?", new String[] { s, s2 });
        }
        catch (SQLiteException ex) {
            this.zzgo().zzjd().zzd("Error deleting conditional property", zzap.zzbv(s), this.zzgl().zzbu(s2), ex);
            return 0;
        }
    }
    
    final Map<Integer, List<zzfv>> zzl(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     8: aload_1        
        //     9: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    12: pop            
        //    13: aload_2        
        //    14: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    17: pop            
        //    18: new             Landroid/support/v4/util/ArrayMap;
        //    21: dup            
        //    22: invokespecial   android/support/v4/util/ArrayMap.<init>:()V
        //    25: astore          8
        //    27: aload_0        
        //    28: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    31: astore          5
        //    33: aconst_null    
        //    34: astore          6
        //    36: aload           5
        //    38: ldc_w           "event_filters"
        //    41: iconst_2       
        //    42: anewarray       Ljava/lang/String;
        //    45: dup            
        //    46: iconst_0       
        //    47: ldc_w           "audience_id"
        //    50: aastore        
        //    51: dup            
        //    52: iconst_1       
        //    53: ldc_w           "data"
        //    56: aastore        
        //    57: ldc_w           "app_id=? AND event_name=?"
        //    60: iconst_2       
        //    61: anewarray       Ljava/lang/String;
        //    64: dup            
        //    65: iconst_0       
        //    66: aload_1        
        //    67: aastore        
        //    68: dup            
        //    69: iconst_1       
        //    70: aload_2        
        //    71: aastore        
        //    72: aconst_null    
        //    73: aconst_null    
        //    74: aconst_null    
        //    75: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    78: astore          5
        //    80: aload           5
        //    82: astore_2       
        //    83: aload           5
        //    85: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    90: ifne            116
        //    93: aload           5
        //    95: astore_2       
        //    96: invokestatic    java/util/Collections.emptyMap:()Ljava/util/Map;
        //    99: astore          6
        //   101: aload           5
        //   103: ifnull          113
        //   106: aload           5
        //   108: invokeinterface android/database/Cursor.close:()V
        //   113: aload           6
        //   115: areturn        
        //   116: aload           5
        //   118: astore_2       
        //   119: aload           5
        //   121: iconst_1       
        //   122: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   127: astore          6
        //   129: aload           5
        //   131: astore_2       
        //   132: aload           6
        //   134: iconst_0       
        //   135: aload           6
        //   137: arraylength    
        //   138: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   141: astore          6
        //   143: aload           5
        //   145: astore_2       
        //   146: new             Lcom/google/android/gms/internal/measurement/zzfv;
        //   149: dup            
        //   150: invokespecial   com/google/android/gms/internal/measurement/zzfv.<init>:()V
        //   153: astore          9
        //   155: aload           5
        //   157: astore_2       
        //   158: aload           9
        //   160: aload           6
        //   162: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   165: pop            
        //   166: aload           5
        //   168: astore_2       
        //   169: aload           5
        //   171: iconst_0       
        //   172: invokeinterface android/database/Cursor.getInt:(I)I
        //   177: istore_3       
        //   178: aload           5
        //   180: astore_2       
        //   181: aload           8
        //   183: iload_3        
        //   184: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   187: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   192: checkcast       Ljava/util/List;
        //   195: astore          7
        //   197: aload           7
        //   199: astore          6
        //   201: aload           7
        //   203: ifnonnull       235
        //   206: aload           5
        //   208: astore_2       
        //   209: new             Ljava/util/ArrayList;
        //   212: dup            
        //   213: invokespecial   java/util/ArrayList.<init>:()V
        //   216: astore          6
        //   218: aload           5
        //   220: astore_2       
        //   221: aload           8
        //   223: iload_3        
        //   224: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   227: aload           6
        //   229: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   234: pop            
        //   235: aload           5
        //   237: astore_2       
        //   238: aload           6
        //   240: aload           9
        //   242: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   247: pop            
        //   248: goto            275
        //   251: astore          6
        //   253: aload           5
        //   255: astore_2       
        //   256: aload_0        
        //   257: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   260: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   263: ldc_w           "Failed to merge filter. appId"
        //   266: aload_1        
        //   267: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   270: aload           6
        //   272: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   275: aload           5
        //   277: astore_2       
        //   278: aload           5
        //   280: invokeinterface android/database/Cursor.moveToNext:()Z
        //   285: istore          4
        //   287: iload           4
        //   289: ifne            116
        //   292: aload           5
        //   294: ifnull          304
        //   297: aload           5
        //   299: invokeinterface android/database/Cursor.close:()V
        //   304: aload           8
        //   306: areturn        
        //   307: astore          6
        //   309: goto            324
        //   312: astore_1       
        //   313: aload           6
        //   315: astore_2       
        //   316: goto            361
        //   319: astore          6
        //   321: aconst_null    
        //   322: astore          5
        //   324: aload           5
        //   326: astore_2       
        //   327: aload_0        
        //   328: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   331: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   334: ldc_w           "Database error querying filters. appId"
        //   337: aload_1        
        //   338: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   341: aload           6
        //   343: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   346: aload           5
        //   348: ifnull          358
        //   351: aload           5
        //   353: invokeinterface android/database/Cursor.close:()V
        //   358: aconst_null    
        //   359: areturn        
        //   360: astore_1       
        //   361: aload_2        
        //   362: ifnull          371
        //   365: aload_2        
        //   366: invokeinterface android/database/Cursor.close:()V
        //   371: aload_1        
        //   372: athrow         
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/String;)Ljava/util/Map<Ljava/lang/Integer;Ljava/util/List<Lcom/google/android/gms/internal/measurement/zzfv;>;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  36     80     319    324    Landroid/database/sqlite/SQLiteException;
        //  36     80     312    319    Any
        //  83     93     307    312    Landroid/database/sqlite/SQLiteException;
        //  83     93     360    361    Any
        //  96     101    307    312    Landroid/database/sqlite/SQLiteException;
        //  96     101    360    361    Any
        //  119    129    307    312    Landroid/database/sqlite/SQLiteException;
        //  119    129    360    361    Any
        //  132    143    307    312    Landroid/database/sqlite/SQLiteException;
        //  132    143    360    361    Any
        //  146    155    307    312    Landroid/database/sqlite/SQLiteException;
        //  146    155    360    361    Any
        //  158    166    251    275    Ljava/io/IOException;
        //  158    166    307    312    Landroid/database/sqlite/SQLiteException;
        //  158    166    360    361    Any
        //  169    178    307    312    Landroid/database/sqlite/SQLiteException;
        //  169    178    360    361    Any
        //  181    197    307    312    Landroid/database/sqlite/SQLiteException;
        //  181    197    360    361    Any
        //  209    218    307    312    Landroid/database/sqlite/SQLiteException;
        //  209    218    360    361    Any
        //  221    235    307    312    Landroid/database/sqlite/SQLiteException;
        //  221    235    360    361    Any
        //  238    248    307    312    Landroid/database/sqlite/SQLiteException;
        //  238    248    360    361    Any
        //  256    275    307    312    Landroid/database/sqlite/SQLiteException;
        //  256    275    360    361    Any
        //  278    287    307    312    Landroid/database/sqlite/SQLiteException;
        //  278    287    360    361    Any
        //  327    346    360    361    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0113:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    final Map<Integer, List<zzfy>> zzm(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //     4: aload_0        
        //     5: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //     8: aload_1        
        //     9: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    12: pop            
        //    13: aload_2        
        //    14: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //    17: pop            
        //    18: new             Landroid/support/v4/util/ArrayMap;
        //    21: dup            
        //    22: invokespecial   android/support/v4/util/ArrayMap.<init>:()V
        //    25: astore          8
        //    27: aload_0        
        //    28: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    31: astore          5
        //    33: aconst_null    
        //    34: astore          6
        //    36: aload           5
        //    38: ldc_w           "property_filters"
        //    41: iconst_2       
        //    42: anewarray       Ljava/lang/String;
        //    45: dup            
        //    46: iconst_0       
        //    47: ldc_w           "audience_id"
        //    50: aastore        
        //    51: dup            
        //    52: iconst_1       
        //    53: ldc_w           "data"
        //    56: aastore        
        //    57: ldc_w           "app_id=? AND property_name=?"
        //    60: iconst_2       
        //    61: anewarray       Ljava/lang/String;
        //    64: dup            
        //    65: iconst_0       
        //    66: aload_1        
        //    67: aastore        
        //    68: dup            
        //    69: iconst_1       
        //    70: aload_2        
        //    71: aastore        
        //    72: aconst_null    
        //    73: aconst_null    
        //    74: aconst_null    
        //    75: invokevirtual   android/database/sqlite/SQLiteDatabase.query:(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //    78: astore          5
        //    80: aload           5
        //    82: astore_2       
        //    83: aload           5
        //    85: invokeinterface android/database/Cursor.moveToFirst:()Z
        //    90: ifne            116
        //    93: aload           5
        //    95: astore_2       
        //    96: invokestatic    java/util/Collections.emptyMap:()Ljava/util/Map;
        //    99: astore          6
        //   101: aload           5
        //   103: ifnull          113
        //   106: aload           5
        //   108: invokeinterface android/database/Cursor.close:()V
        //   113: aload           6
        //   115: areturn        
        //   116: aload           5
        //   118: astore_2       
        //   119: aload           5
        //   121: iconst_1       
        //   122: invokeinterface android/database/Cursor.getBlob:(I)[B
        //   127: astore          6
        //   129: aload           5
        //   131: astore_2       
        //   132: aload           6
        //   134: iconst_0       
        //   135: aload           6
        //   137: arraylength    
        //   138: invokestatic    com/google/android/gms/internal/measurement/zzyx.zzj:([BII)Lcom/google/android/gms/internal/measurement/zzyx;
        //   141: astore          6
        //   143: aload           5
        //   145: astore_2       
        //   146: new             Lcom/google/android/gms/internal/measurement/zzfy;
        //   149: dup            
        //   150: invokespecial   com/google/android/gms/internal/measurement/zzfy.<init>:()V
        //   153: astore          9
        //   155: aload           5
        //   157: astore_2       
        //   158: aload           9
        //   160: aload           6
        //   162: invokevirtual   com/google/android/gms/internal/measurement/zzzg.zza:(Lcom/google/android/gms/internal/measurement/zzyx;)Lcom/google/android/gms/internal/measurement/zzzg;
        //   165: pop            
        //   166: aload           5
        //   168: astore_2       
        //   169: aload           5
        //   171: iconst_0       
        //   172: invokeinterface android/database/Cursor.getInt:(I)I
        //   177: istore_3       
        //   178: aload           5
        //   180: astore_2       
        //   181: aload           8
        //   183: iload_3        
        //   184: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   187: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   192: checkcast       Ljava/util/List;
        //   195: astore          7
        //   197: aload           7
        //   199: astore          6
        //   201: aload           7
        //   203: ifnonnull       235
        //   206: aload           5
        //   208: astore_2       
        //   209: new             Ljava/util/ArrayList;
        //   212: dup            
        //   213: invokespecial   java/util/ArrayList.<init>:()V
        //   216: astore          6
        //   218: aload           5
        //   220: astore_2       
        //   221: aload           8
        //   223: iload_3        
        //   224: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   227: aload           6
        //   229: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   234: pop            
        //   235: aload           5
        //   237: astore_2       
        //   238: aload           6
        //   240: aload           9
        //   242: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   247: pop            
        //   248: goto            275
        //   251: astore          6
        //   253: aload           5
        //   255: astore_2       
        //   256: aload_0        
        //   257: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   260: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   263: ldc_w           "Failed to merge filter"
        //   266: aload_1        
        //   267: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   270: aload           6
        //   272: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   275: aload           5
        //   277: astore_2       
        //   278: aload           5
        //   280: invokeinterface android/database/Cursor.moveToNext:()Z
        //   285: istore          4
        //   287: iload           4
        //   289: ifne            116
        //   292: aload           5
        //   294: ifnull          304
        //   297: aload           5
        //   299: invokeinterface android/database/Cursor.close:()V
        //   304: aload           8
        //   306: areturn        
        //   307: astore          6
        //   309: goto            324
        //   312: astore_1       
        //   313: aload           6
        //   315: astore_2       
        //   316: goto            361
        //   319: astore          6
        //   321: aconst_null    
        //   322: astore          5
        //   324: aload           5
        //   326: astore_2       
        //   327: aload_0        
        //   328: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   331: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   334: ldc_w           "Database error querying filters. appId"
        //   337: aload_1        
        //   338: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   341: aload           6
        //   343: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   346: aload           5
        //   348: ifnull          358
        //   351: aload           5
        //   353: invokeinterface android/database/Cursor.close:()V
        //   358: aconst_null    
        //   359: areturn        
        //   360: astore_1       
        //   361: aload_2        
        //   362: ifnull          371
        //   365: aload_2        
        //   366: invokeinterface android/database/Cursor.close:()V
        //   371: aload_1        
        //   372: athrow         
        //    Signature:
        //  (Ljava/lang/String;Ljava/lang/String;)Ljava/util/Map<Ljava/lang/Integer;Ljava/util/List<Lcom/google/android/gms/internal/measurement/zzfy;>;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  36     80     319    324    Landroid/database/sqlite/SQLiteException;
        //  36     80     312    319    Any
        //  83     93     307    312    Landroid/database/sqlite/SQLiteException;
        //  83     93     360    361    Any
        //  96     101    307    312    Landroid/database/sqlite/SQLiteException;
        //  96     101    360    361    Any
        //  119    129    307    312    Landroid/database/sqlite/SQLiteException;
        //  119    129    360    361    Any
        //  132    143    307    312    Landroid/database/sqlite/SQLiteException;
        //  132    143    360    361    Any
        //  146    155    307    312    Landroid/database/sqlite/SQLiteException;
        //  146    155    360    361    Any
        //  158    166    251    275    Ljava/io/IOException;
        //  158    166    307    312    Landroid/database/sqlite/SQLiteException;
        //  158    166    360    361    Any
        //  169    178    307    312    Landroid/database/sqlite/SQLiteException;
        //  169    178    360    361    Any
        //  181    197    307    312    Landroid/database/sqlite/SQLiteException;
        //  181    197    360    361    Any
        //  209    218    307    312    Landroid/database/sqlite/SQLiteException;
        //  209    218    360    361    Any
        //  221    235    307    312    Landroid/database/sqlite/SQLiteException;
        //  221    235    360    361    Any
        //  238    248    307    312    Landroid/database/sqlite/SQLiteException;
        //  238    248    360    361    Any
        //  256    275    307    312    Landroid/database/sqlite/SQLiteException;
        //  256    275    360    361    Any
        //  278    287    307    312    Landroid/database/sqlite/SQLiteException;
        //  278    287    360    361    Any
        //  327    346    360    361    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0113:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected final long zzn(final String p0, final String p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     4: pop            
        //     5: aload_2        
        //     6: invokestatic    com/google/android/gms/common/internal/Preconditions.checkNotEmpty:(Ljava/lang/String;)Ljava/lang/String;
        //     9: pop            
        //    10: aload_0        
        //    11: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzaf:()V
        //    14: aload_0        
        //    15: invokevirtual   com/google/android/gms/measurement/internal/zzez.zzcl:()V
        //    18: aload_0        
        //    19: invokevirtual   com/google/android/gms/measurement/internal/zzq.getWritableDatabase:()Landroid/database/sqlite/SQLiteDatabase;
        //    22: astore          10
        //    24: aload           10
        //    26: invokevirtual   android/database/sqlite/SQLiteDatabase.beginTransaction:()V
        //    29: lconst_0       
        //    30: lstore          5
        //    32: new             Ljava/lang/StringBuilder;
        //    35: dup            
        //    36: aload_2        
        //    37: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //    40: invokevirtual   java/lang/String.length:()I
        //    43: bipush          32
        //    45: iadd           
        //    46: invokespecial   java/lang/StringBuilder.<init>:(I)V
        //    49: astore          9
        //    51: aload           9
        //    53: ldc_w           "select "
        //    56: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    59: pop            
        //    60: aload           9
        //    62: aload_2        
        //    63: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    66: pop            
        //    67: aload           9
        //    69: ldc_w           " from app2 where app_id=?"
        //    72: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    75: pop            
        //    76: aload           9
        //    78: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    81: astore          9
        //    83: aload_0        
        //    84: aload           9
        //    86: iconst_1       
        //    87: anewarray       Ljava/lang/String;
        //    90: dup            
        //    91: iconst_0       
        //    92: aload_1        
        //    93: aastore        
        //    94: ldc2_w          -1
        //    97: invokespecial   com/google/android/gms/measurement/internal/zzq.zza:(Ljava/lang/String;[Ljava/lang/String;J)J
        //   100: lstore          7
        //   102: lload           7
        //   104: lstore_3       
        //   105: lload           7
        //   107: ldc2_w          -1
        //   110: lcmp           
        //   111: ifne            203
        //   114: new             Landroid/content/ContentValues;
        //   117: dup            
        //   118: invokespecial   android/content/ContentValues.<init>:()V
        //   121: astore          9
        //   123: aload           9
        //   125: ldc_w           "app_id"
        //   128: aload_1        
        //   129: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/String;)V
        //   132: aload           9
        //   134: ldc_w           "first_open_count"
        //   137: iconst_0       
        //   138: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   141: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Integer;)V
        //   144: aload           9
        //   146: ldc             "previous_install_count"
        //   148: iconst_0       
        //   149: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //   152: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Integer;)V
        //   155: aload           10
        //   157: ldc_w           "app2"
        //   160: aconst_null    
        //   161: aload           9
        //   163: iconst_5       
        //   164: invokevirtual   android/database/sqlite/SQLiteDatabase.insertWithOnConflict:(Ljava/lang/String;Ljava/lang/String;Landroid/content/ContentValues;I)J
        //   167: ldc2_w          -1
        //   170: lcmp           
        //   171: ifne            201
        //   174: aload_0        
        //   175: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   178: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   181: ldc_w           "Failed to insert column (got -1). appId"
        //   184: aload_1        
        //   185: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   188: aload_2        
        //   189: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   192: aload           10
        //   194: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   197: ldc2_w          -1
        //   200: lreturn        
        //   201: lconst_0       
        //   202: lstore_3       
        //   203: new             Landroid/content/ContentValues;
        //   206: dup            
        //   207: invokespecial   android/content/ContentValues.<init>:()V
        //   210: astore          9
        //   212: aload           9
        //   214: ldc_w           "app_id"
        //   217: aload_1        
        //   218: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/String;)V
        //   221: aload           9
        //   223: aload_2        
        //   224: lload_3        
        //   225: lconst_1       
        //   226: ladd           
        //   227: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //   230: invokevirtual   android/content/ContentValues.put:(Ljava/lang/String;Ljava/lang/Long;)V
        //   233: aload           10
        //   235: ldc_w           "app2"
        //   238: aload           9
        //   240: ldc_w           "app_id = ?"
        //   243: iconst_1       
        //   244: anewarray       Ljava/lang/String;
        //   247: dup            
        //   248: iconst_0       
        //   249: aload_1        
        //   250: aastore        
        //   251: invokevirtual   android/database/sqlite/SQLiteDatabase.update:(Ljava/lang/String;Landroid/content/ContentValues;Ljava/lang/String;[Ljava/lang/String;)I
        //   254: i2l            
        //   255: lconst_0       
        //   256: lcmp           
        //   257: ifne            287
        //   260: aload_0        
        //   261: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   264: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   267: ldc_w           "Failed to update column (got 0). appId"
        //   270: aload_1        
        //   271: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   274: aload_2        
        //   275: invokevirtual   com/google/android/gms/measurement/internal/zzar.zze:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V
        //   278: aload           10
        //   280: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   283: ldc2_w          -1
        //   286: lreturn        
        //   287: aload           10
        //   289: invokevirtual   android/database/sqlite/SQLiteDatabase.setTransactionSuccessful:()V
        //   292: aload           10
        //   294: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   297: lload_3        
        //   298: lreturn        
        //   299: astore          9
        //   301: goto            321
        //   304: astore          9
        //   306: goto            311
        //   309: astore          9
        //   311: lload           5
        //   313: lstore_3       
        //   314: goto            321
        //   317: astore_1       
        //   318: goto            352
        //   321: aload_0        
        //   322: invokevirtual   com/google/android/gms/measurement/internal/zzco.zzgo:()Lcom/google/android/gms/measurement/internal/zzap;
        //   325: invokevirtual   com/google/android/gms/measurement/internal/zzap.zzjd:()Lcom/google/android/gms/measurement/internal/zzar;
        //   328: ldc_w           "Error inserting column. appId"
        //   331: aload_1        
        //   332: invokestatic    com/google/android/gms/measurement/internal/zzap.zzbv:(Ljava/lang/String;)Ljava/lang/Object;
        //   335: aload_2        
        //   336: aload           9
        //   338: invokevirtual   com/google/android/gms/measurement/internal/zzar.zzd:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V
        //   341: aload           10
        //   343: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   346: lload_3        
        //   347: lreturn        
        //   348: astore_1       
        //   349: goto            318
        //   352: aload           10
        //   354: invokevirtual   android/database/sqlite/SQLiteDatabase.endTransaction:()V
        //   357: aload_1        
        //   358: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  32     83     309    311    Landroid/database/sqlite/SQLiteException;
        //  32     83     317    318    Any
        //  83     102    304    309    Landroid/database/sqlite/SQLiteException;
        //  83     102    348    352    Any
        //  114    192    304    309    Landroid/database/sqlite/SQLiteException;
        //  114    192    348    352    Any
        //  203    278    299    304    Landroid/database/sqlite/SQLiteException;
        //  203    278    348    352    Any
        //  287    292    299    304    Landroid/database/sqlite/SQLiteException;
        //  287    292    348    352    Any
        //  321    341    348    352    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0201:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
}
