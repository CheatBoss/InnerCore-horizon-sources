package androidx.core.content.res;

import android.content.res.*;
import androidx.annotation.*;
import android.os.*;

public final class ConfigurationHelper
{
    private ConfigurationHelper() {
    }
    
    public static int getDensityDpi(@NonNull final Resources resources) {
        if (Build$VERSION.SDK_INT >= 17) {
            return resources.getConfiguration().densityDpi;
        }
        return resources.getDisplayMetrics().densityDpi;
    }
}
