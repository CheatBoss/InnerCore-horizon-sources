package com.microsoft.xbox.telemetry.helpers;

import com.microsoft.xbox.telemetry.utc.*;
import com.microsoft.xbox.idp.ui.*;

public class UTCTelemetry
{
    public static final String UNKNOWNPAGE = "Unknown";
    
    public static void LogEvent(final CommonData commonData) {
        WriteEvent(commonData.ToJson());
    }
    
    private static native void WriteEvent(final String p0);
    
    public static String getErrorScreen(final ErrorActivity.ErrorScreen errorScreen) {
        final int n = UTCTelemetry$1.$SwitchMap$com$microsoft$xbox$idp$ui$ErrorActivity$ErrorScreen[errorScreen.ordinal()];
        if (n == 1) {
            return "Banned error view";
        }
        if (n == 2) {
            return "Generic error view";
        }
        if (n == 3) {
            return "Create error view";
        }
        if (n != 4) {
            return String.format("%sErrorScreen", "Unknown");
        }
        return "Offline error view";
    }
    
    public enum CallBackSources
    {
        Account, 
        Ticket;
    }
}
