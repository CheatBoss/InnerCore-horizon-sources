package bo.app;

import com.appboy.support.*;
import org.json.*;
import java.util.*;

public abstract class fb implements eq
{
    private static final String b;
    protected List<eq> a;
    
    static {
        b = AppboyLogger.getAppboyLogTag(fb.class);
    }
    
    protected fb(final List<eq> a) {
        this.a = a;
    }
    
    public JSONArray a() {
        final JSONArray jsonArray = new JSONArray();
        try {
            final Iterator<eq> iterator = this.a.iterator();
            while (iterator.hasNext()) {
                jsonArray.put(iterator.next().forJsonPut());
            }
        }
        catch (Exception ex) {
            AppboyLogger.e(fb.b, "Caught exception creating Json.", ex);
        }
        return jsonArray;
    }
}
