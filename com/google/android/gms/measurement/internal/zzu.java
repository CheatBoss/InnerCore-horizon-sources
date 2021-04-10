package com.google.android.gms.measurement.internal;

import java.io.*;
import android.database.sqlite.*;
import android.text.*;
import java.util.*;
import android.database.*;

public final class zzu
{
    static void zza(final zzap zzap, final SQLiteDatabase sqLiteDatabase) {
        if (zzap != null) {
            final File file = new File(sqLiteDatabase.getPath());
            if (!file.setReadable(false, false)) {
                zzap.zzjg().zzbx("Failed to turn off database read permission");
            }
            if (!file.setWritable(false, false)) {
                zzap.zzjg().zzbx("Failed to turn off database write permission");
            }
            if (!file.setReadable(true, true)) {
                zzap.zzjg().zzbx("Failed to turn on database read permission for owner");
            }
            if (!file.setWritable(true, true)) {
                zzap.zzjg().zzbx("Failed to turn on database write permission for owner");
            }
            return;
        }
        throw new IllegalArgumentException("Monitor must not be null");
    }
    
    static void zza(final zzap zzap, final SQLiteDatabase sqLiteDatabase, final String s, final String s2, String s3, final String[] array) throws SQLiteException {
        while (true) {
            Label_0232: {
                if (zzap == null) {
                    break Label_0232;
                }
                if (!zza(zzap, sqLiteDatabase, s)) {
                    sqLiteDatabase.execSQL(s2);
                }
                Label_0210: {
                    if (zzap == null) {
                        break Label_0210;
                    }
                    try {
                        final Set<String> zzb = zzb(sqLiteDatabase, s);
                        final String[] split = s3.split(",");
                        final int length = split.length;
                        final int n = 0;
                        for (int i = 0; i < length; ++i) {
                            s3 = split[i];
                            if (!zzb.remove(s3)) {
                                final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 35 + String.valueOf(s3).length());
                                sb.append("Table ");
                                sb.append(s);
                                sb.append(" is missing required column: ");
                                sb.append(s3);
                                throw new SQLiteException(sb.toString());
                            }
                        }
                        if (array != null) {
                            for (int j = n; j < array.length; j += 2) {
                                if (!zzb.remove(array[j])) {
                                    sqLiteDatabase.execSQL(array[j + 1]);
                                }
                            }
                        }
                        if (!zzb.isEmpty()) {
                            zzap.zzjg().zze("Table has extra columns. table, columns", s, TextUtils.join((CharSequence)", ", (Iterable)zzb));
                            return;
                        }
                        return;
                        zzap.zzjd().zzg("Failed to verify columns on table that was just created", s);
                        throw;
                        throw new IllegalArgumentException("Monitor must not be null");
                        throw new IllegalArgumentException("Monitor must not be null");
                    }
                    catch (SQLiteException ex2) {}
                }
            }
            final SQLiteException ex2;
            final SQLiteException ex = ex2;
            continue;
        }
    }
    
    private static boolean zza(final zzap zzap, SQLiteDatabase query, final String s) {
        if (zzap == null) {
            throw new IllegalArgumentException("Monitor must not be null");
        }
        final SQLiteDatabase sqLiteDatabase = null;
        Object o = null;
        try {
            try {
                query = (SQLiteDatabase)query.query("SQLITE_MASTER", new String[] { "name" }, "name=?", new String[] { s }, (String)null, (String)null, (String)null);
                try {
                    final boolean moveToFirst = ((Cursor)query).moveToFirst();
                    if (query != null) {
                        ((Cursor)query).close();
                    }
                    return moveToFirst;
                }
                catch (SQLiteException ex) {}
                finally {
                    o = query;
                }
            }
            finally {}
        }
        catch (SQLiteException ex) {
            query = sqLiteDatabase;
        }
        final SQLiteException ex;
        zzap.zzjg().zze("Error querying for table", s, ex);
        if (query != null) {
            ((Cursor)query).close();
        }
        return false;
        if (o != null) {
            ((Cursor)o).close();
        }
    }
    
    private static Set<String> zzb(SQLiteDatabase rawQuery, final String s) {
        final HashSet<Object> set = new HashSet<Object>();
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 22);
        sb.append("SELECT * FROM ");
        sb.append(s);
        sb.append(" LIMIT 0");
        rawQuery = (SQLiteDatabase)rawQuery.rawQuery(sb.toString(), (String[])null);
        try {
            Collections.addAll(set, ((Cursor)rawQuery).getColumnNames());
            return (Set<String>)set;
        }
        finally {
            ((Cursor)rawQuery).close();
        }
    }
}
