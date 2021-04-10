package androidx.core.widget;

import android.content.res.*;
import androidx.annotation.*;
import android.graphics.*;

public interface TintableCompoundDrawablesView
{
    @Nullable
    ColorStateList getSupportCompoundDrawablesTintList();
    
    @Nullable
    PorterDuff$Mode getSupportCompoundDrawablesTintMode();
    
    void setSupportCompoundDrawablesTintList(@Nullable final ColorStateList p0);
    
    void setSupportCompoundDrawablesTintMode(@Nullable final PorterDuff$Mode p0);
}
