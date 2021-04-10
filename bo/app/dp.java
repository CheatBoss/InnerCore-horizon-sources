package bo.app;

import android.database.sqlite.*;
import com.appboy.support.*;
import android.database.*;
import java.util.*;
import org.json.*;
import android.content.*;

public class dp implements dm
{
    private static final String a;
    private static final String[] b;
    private SQLiteDatabase c;
    private boolean d;
    private final df e;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dp.class);
        b = new String[] { "session_id", "user_id", "event_type", "event_data", "event_guid", "timestamp" };
    }
    
    public dp(final df e) {
        this.d = false;
        this.e = e;
    }
    
    private Collection<ca> a(final Cursor cursor) {
        final ArrayList<ca> list = new ArrayList<ca>();
        final int columnIndex = cursor.getColumnIndex("session_id");
        final int columnIndex2 = cursor.getColumnIndex("user_id");
        final int columnIndex3 = cursor.getColumnIndex("event_type");
        final int columnIndex4 = cursor.getColumnIndex("event_data");
        final int columnIndex5 = cursor.getColumnIndex("event_guid");
        final int columnIndex6 = cursor.getColumnIndex("timestamp");
        while (cursor.moveToNext()) {
            final String string = cursor.getString(columnIndex3);
            final String string2 = cursor.getString(columnIndex4);
            final double double1 = cursor.getDouble(columnIndex6);
            final String string3 = cursor.getString(columnIndex5);
            final String string4 = cursor.getString(columnIndex2);
            final String string5 = cursor.getString(columnIndex);
            try {
                list.add(cg.a(string, string2, double1, string3, string4, string5));
            }
            catch (JSONException ex) {
                final String a = dp.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Could not create AppboyEvent from [type=");
                sb.append(string);
                sb.append(", data=");
                sb.append(string2);
                sb.append(", timestamp=");
                sb.append(double1);
                sb.append(", uniqueId=");
                sb.append(string3);
                sb.append(", userId=");
                sb.append(string4);
                sb.append(", sessionId=");
                sb.append(string5);
                sb.append("] ... Skipping");
                AppboyLogger.e(a, sb.toString());
            }
        }
        return list;
    }
    
    private ContentValues c(final ca ca) {
        final ContentValues contentValues = new ContentValues();
        contentValues.put("event_type", ca.b().a());
        contentValues.put("event_data", ca.c().toString());
        contentValues.put("timestamp", Double.valueOf(ca.a()));
        if (ca.f() != null) {
            contentValues.put("session_id", ca.f().toString());
        }
        if (ca.e() != null) {
            contentValues.put("user_id", ca.e());
        }
        if (ca.d() != null) {
            contentValues.put("event_guid", ca.d());
        }
        return contentValues;
    }
    
    @Override
    public Collection<ca> a() {
        final boolean d = this.d;
        final Cursor cursor = null;
        if (d) {
            AppboyLogger.w(dp.a, "Storage provider is closed. Not getting all events.");
            return null;
        }
        Cursor cursor2;
        Throwable t;
        try {
            final Cursor query = this.c().query("ab_events", dp.b, (String)null, (String[])null, (String)null, (String)null, (String)null);
            try {
                final Collection<ca> a = this.a(query);
                if (query != null) {
                    query.close();
                }
                return a;
            }
            finally {}
        }
        finally {
            cursor2 = cursor;
            final Throwable t2;
            t = t2;
        }
        if (cursor2 != null) {
            cursor2.close();
        }
        throw t;
    }
    
    @Override
    public void a(final ca ca) {
        if (this.d) {
            final String a = dp.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not adding event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        if (this.c().insert("ab_events", (String)null, this.c(ca)) == -1L) {
            final String a2 = dp.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to add event [");
            sb2.append(ca.toString());
            sb2.append("] to storage");
            AppboyLogger.w(a2, sb2.toString());
        }
    }
    
    @Override
    public void b() {
        AppboyLogger.w(dp.a, "Closing SQL database and setting this provider to closed.");
        this.d = true;
        this.c().close();
    }
    
    @Override
    public void b(final ca ca) {
        if (this.d) {
            final String a = dp.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Storage provider is closed. Not deleting event: ");
            sb.append(ca);
            AppboyLogger.w(a, sb.toString());
            return;
        }
        this.c().beginTransaction();
        try {
            final int delete = this.c().delete("ab_events", "event_guid = ?", new String[] { ca.d() });
            final String a2 = dp.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Deleting event with uid ");
            sb2.append(ca.d());
            sb2.append(" removed ");
            sb2.append(delete);
            sb2.append(" row.");
            AppboyLogger.d(a2, sb2.toString(), false);
            this.c().setTransactionSuccessful();
        }
        finally {
            this.c().endTransaction();
        }
    }
    
    public SQLiteDatabase c() {
        synchronized (this) {
            if (this.c == null || !this.c.isOpen()) {
                this.c = this.e.getWritableDatabase();
            }
            return this.c;
        }
    }
}
