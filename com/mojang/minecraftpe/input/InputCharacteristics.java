package com.mojang.minecraftpe.input;

import android.os.*;
import android.view.*;
import java.io.*;

public class InputCharacteristics
{
    public static boolean allControllersHaveDoubleTriggers() {
        boolean b2;
        if (Build$VERSION.SDK_INT >= 19) {
            final int[] deviceIds = InputDevice.getDeviceIds();
            boolean b = false;
            int n = 0;
            while (true) {
                b2 = b;
                if (n >= deviceIds.length) {
                    break;
                }
                final InputDevice device = InputDevice.getDevice(deviceIds[n]);
                boolean b3 = b;
                if (!device.isVirtual()) {
                    b3 = b;
                    if (device.getControllerNumber() > 0) {
                        b3 = b;
                        if ((device.getSources() & 0x401) != 0x0) {
                            final boolean[] hasKeys = device.hasKeys(new int[] { 102, 103, 104, 105 });
                            final boolean b4 = hasKeys.length == 4;
                            int n2 = 0;
                            boolean b5;
                            while (true) {
                                b5 = b4;
                                if (n2 >= hasKeys.length) {
                                    break;
                                }
                                if (!hasKeys[n2]) {
                                    b5 = false;
                                    break;
                                }
                                ++n2;
                            }
                            boolean b6 = b5;
                            if (!b5) {
                                b6 = b5;
                                if (hasKeys[0]) {
                                    b6 = b5;
                                    if (hasKeys[1]) {
                                        final boolean b7 = device.getMotionRange(17) != null || device.getMotionRange(23) != null;
                                        final boolean b8 = device.getMotionRange(18) != null || device.getMotionRange(22) != null;
                                        b6 = (b7 && b8);
                                    }
                                }
                            }
                            boolean b9;
                            if (b9 = b6) {
                                b9 = b6;
                                if (device.getName().contains("EI-GP20")) {
                                    b9 = false;
                                }
                            }
                            if (!(b3 = b9)) {
                                return b9;
                            }
                        }
                    }
                }
                ++n;
                b = b3;
            }
        }
        else {
            b2 = true;
        }
        return b2;
    }
    
    public static boolean isCreteController(int i) {
        final InputDevice device = InputDevice.getDevice(i);
        if (Build$VERSION.SDK_INT >= 19 && device != null && !device.isVirtual() && device.getControllerNumber() > 0 && (device.getSources() & 0x401) != 0x0) {
            if (device.getVendorId() == 1118) {
                i = 1;
            }
            else {
                i = 0;
            }
            if ((((device.getProductId() == 736) ? 1 : 0) & i) != 0x0) {
                for (i = 0; i < 2; ++i) {
                    if (new File((new String[] { "/system/usr/keylayout/Vendor_045e_Product_02e0.kl", "/data/system/devices/keylayout/Vendor_045e_Product_02e0.kl" })[i]).exists()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
