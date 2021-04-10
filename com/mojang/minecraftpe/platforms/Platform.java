package com.mojang.minecraftpe.platforms;

import android.os.*;
import android.view.*;

public abstract class Platform
{
    public static Platform createPlatform(final boolean b) {
        if (Build$VERSION.SDK_INT >= 19) {
            return new Platform19(b);
        }
        if (Build$VERSION.SDK_INT >= 21) {
            return new Platform21(b);
        }
        return new Platform9();
    }
    
    public abstract String getABIS();
    
    public abstract void onAppStart(final View p0);
    
    public abstract void onViewFocusChanged(final boolean p0);
    
    public abstract void onVolumePressed();
}
