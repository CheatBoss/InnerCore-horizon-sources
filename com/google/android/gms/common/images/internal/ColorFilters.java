package com.google.android.gms.common.images.internal;

import android.graphics.*;

public final class ColorFilters
{
    public static final ColorFilter COLOR_FILTER_BW;
    private static final ColorMatrix zzpv;
    
    static {
        (zzpv = new ColorMatrix()).setSaturation(0.0f);
        COLOR_FILTER_BW = (ColorFilter)new ColorMatrixColorFilter(ColorFilters.zzpv);
    }
}
