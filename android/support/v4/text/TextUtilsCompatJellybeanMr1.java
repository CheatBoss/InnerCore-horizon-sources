package android.support.v4.text;

import java.util.*;
import android.text.*;
import android.support.annotation.*;

class TextUtilsCompatJellybeanMr1
{
    public static int getLayoutDirectionFromLocale(@Nullable final Locale locale) {
        return TextUtils.getLayoutDirectionFromLocale(locale);
    }
    
    @NonNull
    public static String htmlEncode(@NonNull final String s) {
        return TextUtils.htmlEncode(s);
    }
}
