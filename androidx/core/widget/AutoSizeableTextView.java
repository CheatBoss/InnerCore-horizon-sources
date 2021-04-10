package androidx.core.widget;

import android.os.*;
import androidx.annotation.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface AutoSizeableTextView
{
    @RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
    public static final boolean PLATFORM_SUPPORTS_AUTOSIZE = Build$VERSION.SDK_INT >= 27;
    
    int getAutoSizeMaxTextSize();
    
    int getAutoSizeMinTextSize();
    
    int getAutoSizeStepGranularity();
    
    int[] getAutoSizeTextAvailableSizes();
    
    int getAutoSizeTextType();
    
    void setAutoSizeTextTypeUniformWithConfiguration(final int p0, final int p1, final int p2, final int p3) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull final int[] p0, final int p1) throws IllegalArgumentException;
    
    void setAutoSizeTextTypeWithDefaults(final int p0);
}
