package androidx.core.telephony.mbms;

import android.content.*;
import android.telephony.mbms.*;
import android.os.*;
import java.util.*;
import android.annotation.*;
import androidx.annotation.*;

public final class MbmsHelper
{
    private MbmsHelper() {
    }
    
    @SuppressLint({ "BanTargetApiAnnotation" })
    @TargetApi(28)
    @Nullable
    public static CharSequence getBestNameForService(@NonNull final Context context, @NonNull final ServiceInfo serviceInfo) {
        if (Build$VERSION.SDK_INT < 28) {
            return null;
        }
        final LocaleList locales = context.getResources().getConfiguration().getLocales();
        final int size = serviceInfo.getNamedContentLocales().size();
        if (size == 0) {
            return null;
        }
        final String[] array = new String[size];
        int n = 0;
        final Iterator<Locale> iterator = serviceInfo.getNamedContentLocales().iterator();
        while (iterator.hasNext()) {
            array[n] = iterator.next().toLanguageTag();
            ++n;
        }
        final Locale firstMatch = locales.getFirstMatch(array);
        if (firstMatch == null) {
            return null;
        }
        return serviceInfo.getNameForLocale(firstMatch);
    }
}
