package com.microsoft.xbox.toolkit.ui;

import java.util.*;
import android.graphics.*;
import android.content.*;

public class FontManager
{
    private static FontManager instance;
    private HashMap<String, Typeface> fonts;
    
    static {
        FontManager.instance = new FontManager();
    }
    
    public static FontManager Instance() {
        return FontManager.instance;
    }
    
    public Typeface getTypeface(final Context context, final String s) {
        if (this.fonts == null) {
            this.fonts = new HashMap<String, Typeface>();
        }
        if (!this.fonts.containsKey(s)) {
            this.fonts.put(s, Typeface.createFromAsset(context.getAssets(), s));
        }
        return this.fonts.get(s);
    }
}
