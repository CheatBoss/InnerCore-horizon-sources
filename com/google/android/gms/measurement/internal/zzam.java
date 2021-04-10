package com.google.android.gms.measurement.internal;

import android.content.*;
import android.database.sqlite.*;
import android.os.*;
import android.database.*;

final class zzam extends SQLiteOpenHelper
{
    private final /* synthetic */ zzal zzals;
    
    zzam(final zzal zzals, final Context context, final String s) {
        this.zzals = zzals;
        super(context, s, (SQLiteDatabase$CursorFactory)null, 1);
    }
    
    public final SQLiteDatabase getWritableDatabase() throws SQLiteException {
        try {
            return super.getWritableDatabase();
        }
        catch (SQLiteException ex3) {
            this.zzals.zzgo().zzjd().zzbx("Opening the local database failed, dropping and recreating it");
            if (!this.zzals.getContext().getDatabasePath("google_app_measurement_local.db").delete()) {
                this.zzals.zzgo().zzjd().zzg("Failed to delete corrupted local db file", "google_app_measurement_local.db");
            }
            try {
                return super.getWritableDatabase();
            }
            catch (SQLiteException ex) {
                this.zzals.zzgo().zzjd().zzg("Failed to open local database. Events will bypass local storage", ex);
                return null;
            }
        }
        catch (SQLiteDatabaseLockedException ex2) {
            throw ex2;
        }
    }
    
    public final void onCreate(final SQLiteDatabase sqLiteDatabase) {
        zzu.zza(this.zzals.zzgo(), sqLiteDatabase);
    }
    
    public final void onDowngrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
    }
    
    public final void onOpen(SQLiteDatabase sqLiteDatabase) {
        Label_0061: {
            if (Build$VERSION.SDK_INT < 15) {
                final SQLiteDatabase sqLiteDatabase2 = null;
                try {
                    final Cursor rawQuery = sqLiteDatabase.rawQuery("PRAGMA journal_mode=memory", (String[])null);
                    try {
                        rawQuery.moveToFirst();
                        if (rawQuery != null) {
                            rawQuery.close();
                        }
                        break Label_0061;
                    }
                    finally {}
                }
                finally {
                    sqLiteDatabase = sqLiteDatabase2;
                }
                if (sqLiteDatabase != null) {
                    ((Cursor)sqLiteDatabase).close();
                }
            }
        }
        zzu.zza(this.zzals.zzgo(), sqLiteDatabase, "messages", "create table if not exists messages ( type INTEGER NOT NULL, entry BLOB NOT NULL)", "type,entry", null);
    }
    
    public final void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
    }
}
