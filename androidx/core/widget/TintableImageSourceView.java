package androidx.core.widget;

import android.content.res.*;
import androidx.annotation.*;
import android.graphics.*;

@RestrictTo({ RestrictTo$Scope.LIBRARY_GROUP_PREFIX })
public interface TintableImageSourceView
{
    @Nullable
    ColorStateList getSupportImageTintList();
    
    @Nullable
    PorterDuff$Mode getSupportImageTintMode();
    
    void setSupportImageTintList(@Nullable final ColorStateList p0);
    
    void setSupportImageTintMode(@Nullable final PorterDuff$Mode p0);
}
