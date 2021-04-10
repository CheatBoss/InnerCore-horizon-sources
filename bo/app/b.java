package bo.app;

import com.appboy.support.*;
import org.json.*;
import android.util.*;

public class b implements c
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(b.class);
    }
    
    @Override
    public ca a(final String s) {
        try {
            return cg.c(s);
        }
        catch (JSONException ex) {
            final String a = b.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create feed card impression event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public ca c(final String s) {
        final String a = b.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot create card dismissed event for Feed card. Returning null. Card id: ");
        sb.append(s);
        Log.w(a, sb.toString());
        return null;
    }
    
    @Override
    public ca d(final String s) {
        final String a = b.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot create card control event for Feed card. Returning null. Card id: ");
        sb.append(s);
        Log.w(a, sb.toString());
        return null;
    }
    
    @Override
    public ca e(final String s) {
        try {
            return cg.d(s);
        }
        catch (JSONException ex) {
            final String a = b.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create feed card click event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
}
