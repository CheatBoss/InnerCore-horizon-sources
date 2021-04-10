package bo.app;

import com.appboy.support.*;
import android.content.*;
import android.net.*;

public final class o implements s
{
    private static final String a;
    private z b;
    private boolean c;
    private boolean d;
    
    static {
        a = AppboyLogger.getAppboyLogTag(o.class);
    }
    
    public o() {
        this.b = z.a;
        this.c = false;
        this.d = false;
    }
    
    @Override
    public z a() {
        return this.b;
    }
    
    @Override
    public void a(final Intent intent, final ConnectivityManager connectivityManager) {
        final String action = intent.getAction();
        if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
            try {
                final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                final boolean booleanExtra = intent.getBooleanExtra("noConnectivity", false);
                if (activeNetworkInfo == null || booleanExtra) {
                    this.b = z.b;
                    this.d = false;
                    this.c = false;
                    return;
                }
                this.d = activeNetworkInfo.isConnectedOrConnecting();
                this.c = activeNetworkInfo.isRoaming();
                switch (activeNetworkInfo.getType()) {
                    default: {
                        this.b = z.a;
                        return;
                    }
                    case 9: {
                        this.b = z.a;
                        return;
                    }
                    case 8: {
                        this.b = z.a;
                        return;
                    }
                    case 7: {
                        this.b = z.a;
                        return;
                    }
                    case 6: {
                        this.b = z.f;
                        return;
                    }
                    case 5: {
                        this.b = z.a;
                        return;
                    }
                    case 4: {
                        this.b = z.a;
                        return;
                    }
                    case 3: {
                        this.b = z.a;
                        return;
                    }
                    case 2: {
                        this.b = z.a;
                        return;
                    }
                    case 1: {
                        this.b = z.f;
                        return;
                    }
                    case 0: {
                        final int subtype = activeNetworkInfo.getSubtype();
                        if (subtype == 3) {
                            this.b = z.d;
                            return;
                        }
                        if (subtype != 13) {
                            this.b = z.c;
                            return;
                        }
                        this.b = z.e;
                        return;
                    }
                }
            }
            catch (SecurityException ex) {
                AppboyLogger.e(o.a, "Failed to get active network information. Ensure the permission android.permission.ACCESS_NETWORK_STATE is defined in your AndroidManifest.xml", ex);
                return;
            }
        }
        final String a = o.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Unexpected system broadcast received [");
        sb.append(action);
        sb.append("]");
        AppboyLogger.w(a, sb.toString());
    }
}
