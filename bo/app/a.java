package bo.app;

import com.appboy.support.*;
import org.json.*;

public class a implements c
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(a.class);
    }
    
    @Override
    public ca a(final String s) {
        try {
            return cg.e(s);
        }
        catch (JSONException ex) {
            final String a = bo.app.a.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create Content Cards impression event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    public cg b(final String s) {
        try {
            return cg.g(s);
        }
        catch (JSONException ex) {
            final String a = bo.app.a.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create Content Cards click event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public ca c(final String s) {
        try {
            return cg.h(s);
        }
        catch (JSONException ex) {
            final String a = bo.app.a.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create Content Cards dismissed event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
    
    @Override
    public ca d(final String s) {
        try {
            return cg.f(s);
        }
        catch (JSONException ex) {
            final String a = bo.app.a.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to create Content Cards control impression event for card: ");
            sb.append(s);
            AppboyLogger.w(a, sb.toString(), (Throwable)ex);
            return null;
        }
    }
}
