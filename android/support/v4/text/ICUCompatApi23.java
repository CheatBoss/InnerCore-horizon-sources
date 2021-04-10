package android.support.v4.text;

import java.util.*;
import android.util.*;
import java.lang.reflect.*;

class ICUCompatApi23
{
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    
    static {
        try {
            ICUCompatApi23.sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    public static String maximizeAndGetScript(final Locale locale) {
        try {
            return ((Locale)ICUCompatApi23.sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
        }
        catch (InvocationTargetException | IllegalAccessException ex) {
            final Throwable t;
            Log.w("ICUCompatIcs", t);
            return locale.getScript();
        }
    }
}
