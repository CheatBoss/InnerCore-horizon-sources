package com.google.android.gms.common.images;

import java.util.*;
import android.net.*;

public final class ImageManager
{
    private static final Object zzov;
    private static HashSet<Uri> zzow;
    
    static {
        zzov = new Object();
        ImageManager.zzow = new HashSet<Uri>();
    }
    
    public interface OnImageLoadedListener
    {
    }
}
