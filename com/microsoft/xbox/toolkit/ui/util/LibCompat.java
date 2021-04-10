package com.microsoft.xbox.toolkit.ui.util;

import android.widget.*;

public final class LibCompat
{
    private LibCompat() {
    }
    
    public static void setTextAppearance(final TextView textView, final int n) {
        textView.setTextAppearance(textView.getContext(), n);
    }
}
