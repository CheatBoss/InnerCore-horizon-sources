package android.support.v4.widget;

import android.widget.*;
import android.support.annotation.*;

class TextViewCompatApi23
{
    public static void setTextAppearance(@NonNull final TextView textView, @StyleRes final int textAppearance) {
        textView.setTextAppearance(textAppearance);
    }
}
