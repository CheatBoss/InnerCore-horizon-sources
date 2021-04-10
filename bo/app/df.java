package bo.app;

import java.util.*;
import android.content.*;
import java.io.*;
import android.database.sqlite.*;
import com.appboy.support.*;

public final class df extends SQLiteOpenHelper
{
    private static final String a;
    private static volatile Map<String, df> b;
    private static final String[] c;
    
    static {
        a = AppboyLogger.getAppboyLogTag(df.class);
        df.b = new HashMap<String, df>();
        c = new String[] { "ab_events", "ab_sessions", "sessions", "appboy_events" };
    }
    
    private df(final Context context, final String s) {
        super(context, s, (SQLiteDatabase$CursorFactory)null, 3);
    }
    
    public static df a(final Context context, String b, final String s) {
        b = b(context, b, s);
        if (!df.b.containsKey(b)) {
            synchronized (df.class) {
                if (!df.b.containsKey(b)) {
                    final df df = new df(context, b);
                    bo.app.df.b.put(b, df);
                    return df;
                }
            }
        }
        return df.b.get(b);
    }
    
    public static void a(final Context context) {
        final File parentFile = context.getDatabasePath(" ").getParentFile();
        if (parentFile.exists() && parentFile.isDirectory()) {
            final File[] listFiles = parentFile.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(final File file, final String s) {
                    return s.startsWith("appboy.db");
                }
            });
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                final String a = df.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("Deleting database file at: ");
                sb.append(file.getAbsolutePath());
                AppboyLogger.v(a, sb.toString());
                context.deleteDatabase(file.getName());
            }
        }
    }
    
    private static void a(final SQLiteDatabase sqLiteDatabase) {
        final String[] c = df.c;
        for (int length = c.length, i = 0; i < length; ++i) {
            final String s = c[i];
            final StringBuilder sb = new StringBuilder();
            sb.append("DROP TABLE IF EXISTS ");
            sb.append(s);
            sqLiteDatabase.execSQL(sb.toString());
            final String a = df.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Dropped table with name: ");
            sb2.append(s);
            AppboyLogger.d(a, sb2.toString());
        }
    }
    
    static String b(final Context context, final String s, final String s2) {
        if (StringUtils.isNullOrBlank(s)) {
            return "appboy.db";
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("appboy.db");
        sb.append(StringUtils.getCacheFileSuffix(context, s, s2));
        return sb.toString();
    }
    
    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        AppboyLogger.i(df.a, "Creating ab_events table");
        sqLiteDatabase.execSQL("CREATE TABLE ab_events (_id INTEGER PRIMARY KEY AUTOINCREMENT, session_id TEXT, user_id TEXT, event_type TEXT NOT NULL, event_data TEXT NOT NULL, event_guid TEXT NOT NULL, timestamp INTEGER NOT NULL);");
    }
    
    public void onDowngrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        final String a = df.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Downgrading storage from version ");
        sb.append(n);
        sb.append(" to ");
        sb.append(n2);
        sb.append(". Dropping all current tables.");
        AppboyLogger.i(a, sb.toString());
        a(sqLiteDatabase);
        this.onCreate(sqLiteDatabase);
    }
    
    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        super.onOpen(sqLiteDatabase);
        sqLiteDatabase.setForeignKeyConstraintsEnabled(true);
    }
    
    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
        final String a = df.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Upgrading storage from version ");
        sb.append(n);
        sb.append(" to ");
        sb.append(n2);
        sb.append(". Dropping all current tables.");
        AppboyLogger.i(a, sb.toString());
        a(sqLiteDatabase);
        this.onCreate(sqLiteDatabase);
    }
}
