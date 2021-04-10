package com.mojang.minecraftpe.platforms;

import android.os.*;

public class Platform21 extends Platform19
{
    public Platform21(final boolean b) {
        super(b);
    }
    
    @Override
    public String getABIS() {
        return Build.SUPPORTED_ABIS.toString();
    }
}
