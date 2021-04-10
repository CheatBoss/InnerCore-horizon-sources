package android.support.v4.text;

import android.util.*;
import java.util.*;
import java.lang.reflect.*;

class ICUCompatIcs
{
    private static final String TAG = "ICUCompatIcs";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;
    
    static {
        try {
            final Class<?> forName = Class.forName("libcore.icu.ICU");
            if (forName != null) {
                ICUCompatIcs.sGetScriptMethod = forName.getMethod("getScript", String.class);
                ICUCompatIcs.sAddLikelySubtagsMethod = forName.getMethod("addLikelySubtags", String.class);
            }
        }
        catch (Exception ex) {
            ICUCompatIcs.sGetScriptMethod = null;
            ICUCompatIcs.sAddLikelySubtagsMethod = null;
            Log.w("ICUCompatIcs", (Throwable)ex);
        }
    }
    
    private static String addLikelySubtags(Locale string) {
        string = (Locale)string.toString();
        try {
            if (ICUCompatIcs.sAddLikelySubtagsMethod != null) {
                return (String)ICUCompatIcs.sAddLikelySubtagsMethod.invoke(null, string);
            }
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            Log.w("ICUCompatIcs", t);
        }
        return (String)string;
    }
    
    private static String getScript(String s) {
        try {
            if (ICUCompatIcs.sGetScriptMethod != null) {
                s = (String)ICUCompatIcs.sGetScriptMethod.invoke(null, s);
                return s;
            }
        }
        catch (IllegalAccessException | InvocationTargetException ex) {
            final Throwable t;
            Log.w("ICUCompatIcs", t);
        }
        return null;
    }
    
    public static String maximizeAndGetScript(final Locale locale) {
        final String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }
}
