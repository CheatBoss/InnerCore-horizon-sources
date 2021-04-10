package com.mojang.minecraftpe.input;

import android.content.*;
import android.os.*;
import android.util.*;

public abstract class InputDeviceManager
{
    public static InputDeviceManager create(final Context context) {
        if (Build$VERSION.SDK_INT >= 16) {
            return new JellyBeanDeviceManager(context);
        }
        return new DefaultDeviceManager();
    }
    
    public abstract void register();
    
    public abstract void unregister();
    
    public static class DefaultDeviceManager extends InputDeviceManager
    {
        private DefaultDeviceManager() {
        }
        
        @Override
        public void register() {
            Log.w("MCPE", "INPUT Noop register device manager");
        }
        
        @Override
        public void unregister() {
            Log.w("MCPE", "INPUT Noop unregister device manager");
        }
    }
}
