package bo.app;

import com.appboy.configuration.*;
import com.appboy.support.*;
import android.content.*;

public class bw implements bv
{
    private static final String b;
    final SharedPreferences a;
    private final AppboyConfigurationProvider c;
    
    static {
        b = AppboyLogger.getAppboyLogTag(bw.class);
    }
    
    public bw(final Context context, final AppboyConfigurationProvider c) {
        this.c = c;
        this.a = context.getSharedPreferences("com.appboy.push_registration", 0);
    }
    
    private boolean b() {
        return this.c.isGcmMessagingRegistrationEnabled() || this.c.isAdmMessagingRegistrationEnabled() || this.c.isFirebaseCloudMessagingRegistrationEnabled();
    }
    
    @Override
    public String a() {
        synchronized (this) {
            if (this.b() && this.a.contains("version_code") && this.c.getVersionCode() != this.a.getInt("version_code", Integer.MIN_VALUE)) {
                return null;
            }
            if (this.a.contains("device_identifier") && !bg.b().equals(this.a.getString("device_identifier", ""))) {
                AppboyLogger.i(bw.b, "Device identifier differs from saved device identifier. Returning null token.");
                return null;
            }
            return this.a.getString("registration_id", (String)null);
        }
    }
    
    @Override
    public void a(final String s) {
        // monitorenter(this)
        if (s != null) {
            Label_0075: {
                try {
                    final SharedPreferences$Editor edit = this.a.edit();
                    edit.putString("registration_id", s);
                    edit.putInt("version_code", this.c.getVersionCode());
                    edit.putString("device_identifier", bg.b());
                    edit.apply();
                    // monitorexit(this)
                    return;
                }
                finally {
                    break Label_0075;
                }
                throw new NullPointerException();
            }
        }
        // monitorexit(this)
        throw new NullPointerException();
    }
}
