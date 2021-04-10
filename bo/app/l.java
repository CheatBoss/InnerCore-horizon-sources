package bo.app;

import com.appboy.support.*;
import android.content.*;

public class l
{
    private final SharedPreferences a;
    
    public l(final Context context) {
        this.a = context.getSharedPreferences("com.appboy.offline.storagemap", 0);
    }
    
    public String a() {
        return this.a.getString("last_user", "");
    }
    
    public void a(final String s) {
        StringUtils.checkNotNullOrEmpty(s);
        final SharedPreferences$Editor edit = this.a.edit();
        edit.putString("last_user", s);
        edit.apply();
    }
    
    public void b(final String s) {
        StringUtils.checkNotNullOrEmpty(s);
        final SharedPreferences$Editor edit = this.a.edit();
        edit.putString("default_user", s);
        edit.putString("last_user", s);
        edit.apply();
    }
}
