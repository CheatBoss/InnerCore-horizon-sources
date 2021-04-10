package bo.app;

import android.net.*;
import java.util.*;

public abstract class da implements cx
{
    public final Uri a;
    private Map<String, String> b;
    
    protected da(final Uri uri, final Map<String, String> b) {
        this.b = b;
        final StringBuilder sb = new StringBuilder();
        sb.append(uri);
        sb.append(this.j());
        this.a = Uri.parse(sb.toString());
    }
    
    @Override
    public Uri a() {
        return this.a;
    }
    
    public String j() {
        final Map<String, String> b = this.b;
        if (b != null && b.size()) {
            final Iterator<String> iterator = this.b.keySet().iterator();
            String string = "?";
            while (iterator.hasNext()) {
                final String s = iterator.next();
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(s);
                sb.append("=");
                sb.append(this.b.get(s));
                sb.append("&");
                string = sb.toString();
            }
            return string.substring(0, string.length() - 1);
        }
        return "";
    }
}
