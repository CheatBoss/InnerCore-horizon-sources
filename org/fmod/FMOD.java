package org.fmod;

import android.content.*;
import android.content.res.*;
import android.os.*;
import android.media.*;
import android.util.*;

public class FMOD
{
    private static Context gContext;
    
    public static boolean checkInit() {
        return FMOD.gContext != null;
    }
    
    public static void close() {
        FMOD.gContext = null;
    }
    
    public static AssetManager getAssetManager() {
        final Context gContext = FMOD.gContext;
        if (gContext != null) {
            return gContext.getAssets();
        }
        return null;
    }
    
    public static int getOutputBlockSize() {
        if (FMOD.gContext != null && Build$VERSION.SDK_INT >= 17) {
            final String property = ((AudioManager)FMOD.gContext.getSystemService("audio")).getProperty("android.media.property.OUTPUT_FRAMES_PER_BUFFER");
            if (property != null) {
                return Integer.parseInt(property);
            }
        }
        return 0;
    }
    
    public static int getOutputSampleRate() {
        if (FMOD.gContext != null && Build$VERSION.SDK_INT >= 17) {
            final String property = ((AudioManager)FMOD.gContext.getSystemService("audio")).getProperty("android.media.property.OUTPUT_SAMPLE_RATE");
            if (property != null) {
                return Integer.parseInt(property);
            }
        }
        return 0;
    }
    
    public static void init(final Context gContext) {
        FMOD.gContext = gContext;
    }
    
    public static boolean isBluetoothOn() {
        final Context gContext = FMOD.gContext;
        if (gContext != null) {
            final AudioManager audioManager = (AudioManager)gContext.getSystemService("audio");
            if (audioManager.isBluetoothA2dpOn() || audioManager.isBluetoothScoOn()) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean supportsLowLatency() {
        if (FMOD.gContext != null && Build$VERSION.SDK_INT >= 5) {
            final int outputBlockSize = getOutputBlockSize();
            final boolean hasSystemFeature = FMOD.gContext.getPackageManager().hasSystemFeature("android.hardware.audio.low_latency");
            final boolean hasSystemFeature2 = FMOD.gContext.getPackageManager().hasSystemFeature("android.hardware.audio.pro");
            final boolean b = outputBlockSize > 0 && outputBlockSize <= 1024;
            final boolean bluetoothOn = isBluetoothOn();
            final StringBuilder sb = new StringBuilder();
            sb.append("FMOD::supportsLowLatency                 : Low latency = ");
            sb.append(hasSystemFeature);
            sb.append(", Pro Audio = ");
            sb.append(hasSystemFeature2);
            sb.append(", Bluetooth On = ");
            sb.append(bluetoothOn);
            sb.append(", Acceptable Block Size = ");
            sb.append(b);
            sb.append(" (");
            sb.append(outputBlockSize);
            sb.append(")");
            Log.i("fmod", sb.toString());
            if (b && hasSystemFeature && !bluetoothOn) {
                return true;
            }
        }
        return false;
    }
}
