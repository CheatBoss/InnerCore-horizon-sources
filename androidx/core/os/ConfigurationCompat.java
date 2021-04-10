package androidx.core.os;

import android.content.res.*;
import androidx.annotation.*;
import android.os.*;
import java.util.*;

public final class ConfigurationCompat
{
    private ConfigurationCompat() {
    }
    
    @NonNull
    public static LocaleListCompat getLocales(@NonNull final Configuration configuration) {
        if (Build$VERSION.SDK_INT >= 24) {
            return LocaleListCompat.wrap(configuration.getLocales());
        }
        return LocaleListCompat.create(configuration.locale);
    }
}
