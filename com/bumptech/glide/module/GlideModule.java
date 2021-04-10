package com.bumptech.glide.module;

import android.content.*;
import com.bumptech.glide.*;

public interface GlideModule
{
    void applyOptions(final Context p0, final GlideBuilder p1);
    
    void registerComponents(final Context p0, final Glide p1);
}
