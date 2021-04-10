package com.microsoft.xbox.telemetry.helpers;

import com.microsoft.xbox.idp.ui.*;
import com.microsoft.xbox.telemetry.utc.*;

public class UTCError
{
    private static final String UINEEDEDERROR = "Client Error Type - UI Needed";
    
    public static void trackClose(final ErrorActivity.ErrorScreen errorScreen, final CharSequence charSequence) {
        try {
            UTCPageAction.track("Errors - Close error screen", charSequence);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackClose");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public static void trackException(final Exception ex, String s) {
        final ClientError clientError = new ClientError();
        if (ex != null && s != null) {
            UTCLog.log(String.format("%s:%s", s, ex.getMessage()), new Object[0]);
            clientError.errorName = ex.getClass().getSimpleName();
            clientError.errorText = ex.getMessage();
            final StackTraceElement[] stackTrace = ex.getStackTrace();
            String format = s;
            if (stackTrace != null) {
                format = s;
                if (stackTrace.length > 0) {
                    int n = 0;
                    while (true) {
                        format = s;
                        if (n >= stackTrace.length) {
                            break;
                        }
                        format = s;
                        if (n >= 10) {
                            break;
                        }
                        final StackTraceElement stackTraceElement = stackTrace[n];
                        format = s;
                        if (stackTraceElement != null) {
                            format = String.format("%s;%s", s, stackTraceElement.toString());
                        }
                        if (format.length() > 200) {
                            break;
                        }
                        ++n;
                        s = format;
                    }
                }
            }
            clientError.callStack = format;
            clientError.pageName = UTCPageView.getCurrentPage();
            UTCTelemetry.LogEvent(clientError);
        }
    }
    
    public static void trackGoToEnforcement(final ErrorActivity.ErrorScreen errorScreen, final CharSequence charSequence) {
        try {
            UTCPageAction.track("Errors - View enforcement site", charSequence);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackGoToEnforcement");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public static void trackPageView(final ErrorActivity.ErrorScreen errorScreen, final CharSequence charSequence) {
        try {
            UTCPageView.track(UTCTelemetry.getErrorScreen(errorScreen), charSequence);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackPageView");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public static void trackRightButton(final ErrorActivity.ErrorScreen errorScreen, final CharSequence charSequence) {
        try {
            UTCPageAction.track("Errors - Ok", charSequence);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackRightButton");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public static void trackTryAgain(final ErrorActivity.ErrorScreen errorScreen, final CharSequence charSequence) {
        try {
            UTCPageAction.track("Errors - Try again", charSequence);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackTryAgain");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
    
    public static void trackUINeeded(final String s, final boolean b, final UTCTelemetry.CallBackSources callBackSources) {
        try {
            final ClientError clientError = new ClientError();
            clientError.pageName = UTCPageView.getCurrentPage();
            clientError.errorName = "Client Error Type - UI Needed";
            clientError.additionalInfo.put("isSilent", b);
            clientError.additionalInfo.put("job", s);
            clientError.additionalInfo.put("source", callBackSources);
            UTCLog.log("Error:%s, additionalInfo:%s", "Client Error Type - UI Needed", clientError.GetAdditionalInfoString());
            UTCTelemetry.LogEvent(clientError);
        }
        catch (Exception ex) {
            trackException(ex, "UTCError.trackUINeeded");
            UTCLog.log(ex.getMessage(), new Object[0]);
        }
    }
}
