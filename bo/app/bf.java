package bo.app;

import android.content.*;
import com.appboy.support.*;
import com.amazon.device.messaging.development.*;
import com.amazon.device.messaging.*;

public class bf
{
    private static final String c;
    private final Context a;
    private final bv b;
    
    static {
        c = AppboyLogger.getAppboyLogTag(bf.class);
    }
    
    public bf(final Context a, final bv b) {
        this.a = a;
        this.b = b;
    }
    
    public static boolean a(final Context context) {
        return b() && b(context);
    }
    
    private static boolean b() {
        try {
            Class.forName("com.amazon.device.messaging.ADM");
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.i(bf.c, "com.amazon.device.messaging.ADM not found");
            return false;
        }
    }
    
    private static boolean b(final Context context) {
        try {
            ADMManifest.checkManifestAuthoredProperly(context);
            return true;
        }
        catch (Exception ex) {
            AppboyLogger.i(bf.c, "Manifest not authored properly to support ADM.");
            AppboyLogger.i(bf.c, "ADM manifest exception: ", ex);
            return false;
        }
    }
    
    public void a() {
        if (this.b.a() != null) {
            AppboyLogger.i(bf.c, "The device is already registered with the ADM server and is eligible to receive ADM messages.");
            final String c = bf.c;
            final StringBuilder sb = new StringBuilder();
            sb.append("ADM registration id: ");
            sb.append(this.b.a());
            AppboyLogger.i(c, sb.toString());
            final bv b = this.b;
            b.a(b.a());
            return;
        }
        final ADM adm = new ADM(this.a);
        if (adm.isSupported()) {
            AppboyLogger.i(bf.c, "Registering with ADM server...");
            adm.startRegister();
        }
    }
}
