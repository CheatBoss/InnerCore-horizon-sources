package bo.app;

import com.appboy.support.*;
import android.net.*;
import java.net.*;

public final class ea
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(ea.class);
    }
    
    public static URI a(final Uri uri) {
        try {
            return new URI(uri.toString());
        }
        catch (URISyntaxException ex) {
            final String a = ea.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Could not create URI from uri [");
            sb.append(uri.toString());
            sb.append("]");
            AppboyLogger.e(a, sb.toString());
            return null;
        }
    }
    
    public static URL a(final URI uri) {
        try {
            return new URL(uri.toString());
        }
        catch (MalformedURLException ex) {
            final String a = ea.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Unable to parse URI [");
            sb.append(ex.getMessage());
            sb.append("]");
            AppboyLogger.e(a, sb.toString(), ex);
            return null;
        }
    }
}
