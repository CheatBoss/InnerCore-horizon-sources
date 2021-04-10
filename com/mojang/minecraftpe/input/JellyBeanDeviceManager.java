package com.mojang.minecraftpe.input;

import android.hardware.input.*;
import android.content.*;
import android.os.*;

public class JellyBeanDeviceManager extends InputDeviceManager implements InputManager$InputDeviceListener
{
    private final InputManager inputManager;
    
    JellyBeanDeviceManager(final Context context) {
        this.inputManager = (InputManager)context.getSystemService("input");
    }
    
    public void onInputDeviceAdded(final int n) {
        this.onInputDeviceAddedNative(n);
        this.setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        this.setCreteControllerNative(n, InputCharacteristics.isCreteController(n));
    }
    
    native void onInputDeviceAddedNative(final int p0);
    
    public void onInputDeviceChanged(final int n) {
        this.onInputDeviceChangedNative(n);
        this.setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        this.setCreteControllerNative(n, InputCharacteristics.isCreteController(n));
    }
    
    native void onInputDeviceChangedNative(final int p0);
    
    public void onInputDeviceRemoved(final int n) {
        this.onInputDeviceRemovedNative(n);
        this.setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        this.setCreteControllerNative(n, InputCharacteristics.isCreteController(n));
    }
    
    native void onInputDeviceRemovedNative(final int p0);
    
    @Override
    public void register() {
        final int[] inputDeviceIds = this.inputManager.getInputDeviceIds();
        this.inputManager.registerInputDeviceListener((InputManager$InputDeviceListener)this, (Handler)null);
        this.setDoubleTriggersSupportedNative(InputCharacteristics.allControllersHaveDoubleTriggers());
        for (int i = 0; i < inputDeviceIds.length; ++i) {
            this.setCreteControllerNative(inputDeviceIds[i], InputCharacteristics.isCreteController(inputDeviceIds[i]));
        }
    }
    
    native void setCreteControllerNative(final int p0, final boolean p1);
    
    native void setDoubleTriggersSupportedNative(final boolean p0);
    
    @Override
    public void unregister() {
        this.inputManager.unregisterInputDeviceListener((InputManager$InputDeviceListener)this);
    }
}
