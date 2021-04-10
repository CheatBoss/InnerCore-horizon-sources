package androidx.core.text;

import android.os.*;
import android.util.*;
import java.util.*;
import java.lang.reflect.*;
import android.icu.util.*;
import androidx.annotation.*;

public final class ICUCompat
{
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;
    
    static {
        if (Build$VERSION.SDK_INT < 21) {
            try {
                final Class<?> forName = Class.forName("libcore.icu.ICU");
                if (forName != null) {
                    ICUCompat.sGetScriptMethod = forName.getMethod("getScript", String.class);
                    ICUCompat.sAddLikelySubtagsMethod = forName.getMethod("addLikelySubtags", String.class);
                }
            }
            catch (Exception ex) {
                ICUCompat.sGetScriptMethod = null;
                ICUCompat.sAddLikelySubtagsMethod = null;
                Log.w("ICUCompat", (Throwable)ex);
            }
            return;
        }
        if (Build$VERSION.SDK_INT < 24) {
            try {
                ICUCompat.sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
            }
            catch (Exception ex2) {
                throw new IllegalStateException(ex2);
            }
        }
    }
    
    private ICUCompat() {
    }
    
    private static String addLikelySubtags(Locale string) {
        string = (Locale)string.toString();
        try {
            if (ICUCompat.sAddLikelySubtagsMethod != null) {
                return (String)ICUCompat.sAddLikelySubtagsMethod.invoke(null, string);
            }
        }
        catch (InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
            return (String)string;
        }
        catch (IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return (String)string;
    }
    
    private static String getScript(String s) {
        try {
            if (ICUCompat.sGetScriptMethod != null) {
                s = (String)ICUCompat.sGetScriptMethod.invoke(null, s);
                return s;
            }
        }
        catch (InvocationTargetException ex) {
            Log.w("ICUCompat", (Throwable)ex);
            return null;
        }
        catch (IllegalAccessException ex2) {
            Log.w("ICUCompat", (Throwable)ex2);
        }
        return null;
    }
    
    @Nullable
    public static String maximizeAndGetScript(final Locale locale) {
        if (Build$VERSION.SDK_INT >= 24) {
            return ULocale.addLikelySubtags(ULocale.forLocale(locale)).getScript();
        }
        if (Build$VERSION.SDK_INT >= 21) {
            try {
                return ((Locale)ICUCompat.sAddLikelySubtagsMethod.invoke(null, locale)).getScript();
            }
            catch (IllegalAccessException ex) {
                Log.w("ICUCompat", (Throwable)ex);
            }
            catch (InvocationTargetException ex2) {
                Log.w("ICUCompat", (Throwable)ex2);
            }
            return locale.getScript();
        }
        final String addLikelySubtags = addLikelySubtags(locale);
        if (addLikelySubtags != null) {
            return getScript(addLikelySubtags);
        }
        return null;
    }
}
