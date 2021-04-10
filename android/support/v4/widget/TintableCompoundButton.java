package android.support.v4.widget;

import android.content.res.*;
import android.support.annotation.*;
import android.graphics.*;

public interface TintableCompoundButton
{
    @Nullable
    ColorStateList getSupportButtonTintList();
    
    @Nullable
    PorterDuff$Mode getSupportButtonTintMode();
    
    void setSupportButtonTintList(@Nullable final ColorStateList p0);
    
    void setSupportButtonTintMode(@Nullable final PorterDuff$Mode p0);
}
