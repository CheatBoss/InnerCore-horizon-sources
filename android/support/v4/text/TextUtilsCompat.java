package android.support.v4.text;

import java.util.*;
import android.os.*;
import android.support.annotation.*;

public final class TextUtilsCompat
{
    private static String ARAB_SCRIPT_SUBTAG;
    private static String HEBR_SCRIPT_SUBTAG;
    private static final TextUtilsCompatImpl IMPL;
    public static final Locale ROOT;
    
    static {
        Object impl;
        if (Build$VERSION.SDK_INT >= 17) {
            impl = new TextUtilsCompatJellybeanMr1Impl();
        }
        else {
            impl = new TextUtilsCompatImpl();
        }
        IMPL = (TextUtilsCompatImpl)impl;
        ROOT = new Locale("", "");
        TextUtilsCompat.ARAB_SCRIPT_SUBTAG = "Arab";
        TextUtilsCompat.HEBR_SCRIPT_SUBTAG = "Hebr";
    }
    
    private TextUtilsCompat() {
    }
    
    public static int getLayoutDirectionFromLocale(@Nullable final Locale locale) {
        return TextUtilsCompat.IMPL.getLayoutDirectionFromLocale(locale);
    }
    
    @NonNull
    public static String htmlEncode(@NonNull final String s) {
        return TextUtilsCompat.IMPL.htmlEncode(s);
    }
    
    private static class TextUtilsCompatImpl
    {
        private static int getLayoutDirectionFromFirstChar(@NonNull final Locale locale) {
            switch (Character.getDirectionality(locale.getDisplayName(locale).charAt(0))) {
                default: {
                    return 0;
                }
                case 1:
                case 2: {
                    return 1;
                }
            }
        }
        
        public int getLayoutDirectionFromLocale(@Nullable final Locale locale) {
            if (locale != null && !locale.equals(TextUtilsCompat.ROOT)) {
                final String maximizeAndGetScript = ICUCompat.maximizeAndGetScript(locale);
                if (maximizeAndGetScript == null) {
                    return getLayoutDirectionFromFirstChar(locale);
                }
                if (maximizeAndGetScript.equalsIgnoreCase(TextUtilsCompat.ARAB_SCRIPT_SUBTAG) || maximizeAndGetScript.equalsIgnoreCase(TextUtilsCompat.HEBR_SCRIPT_SUBTAG)) {
                    return 1;
                }
            }
            return 0;
        }
        
        @NonNull
        public String htmlEncode(@NonNull final String s) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); ++i) {
                final char char1 = s.charAt(i);
                String s2;
                if (char1 != '\"') {
                    if (char1 != '<') {
                        if (char1 != '>') {
                            switch (char1) {
                                default: {
                                    sb.append(char1);
                                    continue;
                                }
                                case 39: {
                                    s2 = "&#39;";
                                    break;
                                }
                                case 38: {
                                    s2 = "&amp;";
                                    break;
                                }
                            }
                        }
                        else {
                            s2 = "&gt;";
                        }
                    }
                    else {
                        s2 = "&lt;";
                    }
                }
                else {
                    s2 = "&quot;";
                }
                sb.append(s2);
            }
            return sb.toString();
        }
    }
    
    private static class TextUtilsCompatJellybeanMr1Impl extends TextUtilsCompatImpl
    {
        @Override
        public int getLayoutDirectionFromLocale(@Nullable final Locale locale) {
            return TextUtilsCompatJellybeanMr1.getLayoutDirectionFromLocale(locale);
        }
        
        @NonNull
        @Override
        public String htmlEncode(@NonNull final String s) {
            return TextUtilsCompatJellybeanMr1.htmlEncode(s);
        }
    }
}
