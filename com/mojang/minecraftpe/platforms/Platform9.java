package com.mojang.minecraftpe.platforms;

import android.os.*;
import android.view.*;

public class Platform9 extends Platform
{
    @Override
    public String getABIS() {
        return Build.CPU_ABI;
    }
    
    @Override
    public void onAppStart(final View view) {
    }
    
    @Override
    public void onViewFocusChanged(final boolean b) {
    }
    
    @Override
    public void onVolumePressed() {
    }
}
