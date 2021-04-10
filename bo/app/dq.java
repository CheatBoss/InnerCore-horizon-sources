package bo.app;

import com.appboy.support.*;
import android.content.*;

public class dq
{
    private static final String a;
    private final SharedPreferences b;
    
    static {
        a = AppboyLogger.getAppboyLogTag(dq.class);
    }
    
    public dq(final Context context) {
        this.b = context.getSharedPreferences("persistent.com.appboy.storage.sdk_enabled_cache", 0);
    }
    
    public void a(final boolean b) {
        final String a = dq.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Setting Appboy SDK disabled to: ");
        sb.append(b);
        AppboyLogger.i(a, sb.toString());
        this.b.edit().putBoolean("appboy_sdk_disabled", b).apply();
    }
    
    public boolean a() {
        return this.b.getBoolean("appboy_sdk_disabled", false);
    }
}
