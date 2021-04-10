package android.support.v4.text;

import android.os.*;
import java.util.*;

public final class ICUCompat
{
    private static final ICUCompatImpl IMPL;
    
    static {
        final int sdk_INT = Build$VERSION.SDK_INT;
        ICUCompatImpl impl;
        if (sdk_INT >= 21) {
            impl = new ICUCompatImplLollipop();
        }
        else if (sdk_INT >= 14) {
            impl = new ICUCompatImplIcs();
        }
        else {
            impl = new ICUCompatImplBase();
        }
        IMPL = impl;
    }
    
    private ICUCompat() {
    }
    
    public static String maximizeAndGetScript(final Locale locale) {
        return ICUCompat.IMPL.maximizeAndGetScript(locale);
    }
    
    interface ICUCompatImpl
    {
        String maximizeAndGetScript(final Locale p0);
    }
    
    static class ICUCompatImplBase implements ICUCompatImpl
    {
        @Override
        public String maximizeAndGetScript(final Locale locale) {
            return null;
        }
    }
    
    static class ICUCompatImplIcs implements ICUCompatImpl
    {
        @Override
        public String maximizeAndGetScript(final Locale locale) {
            return ICUCompatIcs.maximizeAndGetScript(locale);
        }
    }
    
    static class ICUCompatImplLollipop implements ICUCompatImpl
    {
        @Override
        public String maximizeAndGetScript(final Locale locale) {
            return ICUCompatApi23.maximizeAndGetScript(locale);
        }
    }
}
