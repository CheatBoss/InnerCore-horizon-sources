package com.microsoft.xbox.telemetry.helpers;

public class UTCEventTracker
{
    public static String callStringTrackWrapper(final UTCStringEventDelegate utcStringEventDelegate) {
        try {
            return utcStringEventDelegate.call();
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
            return null;
        }
    }
    
    public static void callTrackWrapper(final UTCEventDelegate utcEventDelegate) {
        try {
            utcEventDelegate.call();
        }
        catch (Exception ex) {
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public interface UTCEventDelegate
    {
        void call();
    }
    
    public interface UTCStringEventDelegate
    {
        String call();
    }
}
