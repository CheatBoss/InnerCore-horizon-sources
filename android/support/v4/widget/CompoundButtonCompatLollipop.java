package android.support.v4.widget;

import android.widget.*;
import android.content.res.*;
import android.graphics.*;

class CompoundButtonCompatLollipop
{
    static ColorStateList getButtonTintList(final CompoundButton compoundButton) {
        return compoundButton.getButtonTintList();
    }
    
    static PorterDuff$Mode getButtonTintMode(final CompoundButton compoundButton) {
        return compoundButton.getButtonTintMode();
    }
    
    static void setButtonTintList(final CompoundButton compoundButton, final ColorStateList buttonTintList) {
        compoundButton.setButtonTintList(buttonTintList);
    }
    
    static void setButtonTintMode(final CompoundButton compoundButton, final PorterDuff$Mode buttonTintMode) {
        compoundButton.setButtonTintMode(buttonTintMode);
    }
}
