package bo.app;

import android.content.*;
import java.util.*;

public class bg implements bt
{
    final SharedPreferences a;
    
    public bg(final Context context) {
        this.a = context.getSharedPreferences("com.appboy.device", 0);
    }
    
    private void a(final String s) {
        final SharedPreferences$Editor edit = this.a.edit();
        edit.putString("device_id", s);
        edit.putString("persistent_device_id", b());
        edit.apply();
    }
    
    static String b() {
        return String.valueOf(722989291);
    }
    
    private boolean c() {
        return this.a.contains("persistent_device_id") && (b().equals(this.a.getString("persistent_device_id", "")) ^ true);
    }
    
    @Override
    public String a() {
        String string = this.a.getString("device_id", (String)null);
        if (this.c()) {
            string = null;
        }
        if (string == null) {
            final String string2 = UUID.randomUUID().toString();
            this.a(string2);
            return string2;
        }
        if (!this.a.contains("persistent_device_id")) {
            this.a(string);
        }
        return string;
    }
}
