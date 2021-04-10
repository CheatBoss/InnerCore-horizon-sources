package com.microsoft.aad.adal;

import java.text.*;
import android.util.*;
import java.util.*;

public class Logger
{
    private static final String CUSTOM_LOG_ERROR = "Custom log failed to log message:%s";
    static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Logger sINSTANCE;
    private boolean mAndroidLogEnabled;
    private String mCorrelationId;
    private boolean mEnablePII;
    private ILogger mExternalLogger;
    private LogLevel mLogLevel;
    
    static {
        Logger.sINSTANCE = new Logger();
    }
    
    public Logger() {
        this.mLogLevel = LogLevel.Verbose;
        this.mExternalLogger = null;
        this.mAndroidLogEnabled = false;
        this.mCorrelationId = null;
        this.mEnablePII = false;
    }
    
    private static String addMoreInfo(final String s) {
        if (!StringExtensions.isNullOrBlank(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(getUTCDateTimeAsString());
            sb.append("-");
            sb.append(getInstance().mCorrelationId);
            sb.append("-");
            sb.append(s);
            sb.append(" ver:");
            sb.append(AuthenticationContext.getVersionName());
            return sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(getUTCDateTimeAsString());
        sb2.append("-");
        sb2.append(getInstance().mCorrelationId);
        sb2.append("- ver:");
        sb2.append(AuthenticationContext.getVersionName());
        return sb2.toString();
    }
    
    public static void d(final String s, final String s2) {
        if (StringExtensions.isNullOrBlank(s2)) {
            return;
        }
        getInstance().log(s, s2, null, LogLevel.Debug, null, null);
    }
    
    public static void e(final String s, final String s2, final String s3, final ADALError adalError) {
        getInstance().log(s, s2, s3, LogLevel.Error, adalError, null);
    }
    
    public static void e(final String s, final String s2, final String s3, final ADALError adalError, final Throwable t) {
        getInstance().log(s, s2, s3, LogLevel.Error, adalError, t);
    }
    
    public static void e(final String s, final String s2, final Throwable t) {
        getInstance().log(s, s2, null, LogLevel.Error, null, t);
    }
    
    private static String getCodeName(final ADALError adalError) {
        if (adalError != null) {
            return adalError.name();
        }
        return "";
    }
    
    public static Logger getInstance() {
        return Logger.sINSTANCE;
    }
    
    private static String getUTCDateTimeAsString() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date());
    }
    
    public static void i(final String s, final String s2, final String s3) {
        getInstance().log(s, s2, s3, LogLevel.Info, null, null);
    }
    
    public static void i(final String s, final String s2, final String s3, final ADALError adalError) {
        getInstance().log(s, s2, s3, LogLevel.Info, adalError, null);
    }
    
    private void log(final String s, final String s2, String s3, final LogLevel logLevel, final ADALError adalError, final Throwable t) {
        if (logLevel.compareTo(this.mLogLevel) > 0) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        if (adalError != null) {
            sb.append(getCodeName(adalError));
            sb.append(':');
        }
        sb.append(addMoreInfo(s2));
        if (!StringExtensions.isNullOrBlank(s3) && this.mEnablePII) {
            sb.append(' ');
            sb.append(s3);
        }
        if (t != null) {
            sb.append('\n');
            sb.append(Log.getStackTraceString(t));
        }
        if (this.mAndroidLogEnabled) {
            this.sendLogcatLogs(s, logLevel, sb.toString());
        }
        if (this.mExternalLogger != null) {
            while (true) {
                while (true) {
                    Label_0288: {
                        try {
                            if (!StringExtensions.isNullOrBlank(s3) && this.mEnablePII) {
                                final ILogger mExternalLogger = this.mExternalLogger;
                                final String addMoreInfo = addMoreInfo(s2);
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append(s3);
                                if (t == null) {
                                    s3 = "";
                                }
                                else {
                                    s3 = Log.getStackTraceString(t);
                                }
                                sb2.append(s3);
                                mExternalLogger.Log(s, addMoreInfo, sb2.toString(), logLevel, adalError);
                                return;
                            }
                            final ILogger mExternalLogger2 = this.mExternalLogger;
                            final String addMoreInfo2 = addMoreInfo(s2);
                            if (t == null) {
                                s3 = null;
                                break Label_0288;
                            }
                            s3 = Log.getStackTraceString(t);
                            break Label_0288;
                            mExternalLogger2.Log(s, addMoreInfo2, s3, logLevel, adalError);
                            return;
                        }
                        catch (Exception ex) {
                            Log.w(s, String.format("Custom log failed to log message:%s", s2));
                        }
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    private void sendLogcatLogs(final String s, final LogLevel logLevel, final String s2) {
        final int n = Logger$1.$SwitchMap$com$microsoft$aad$adal$Logger$LogLevel[logLevel.ordinal()];
        if (n == 1) {
            Log.e(s, s2);
            return;
        }
        if (n == 2) {
            Log.w(s, s2);
            return;
        }
        if (n == 3) {
            Log.i(s, s2);
            return;
        }
        if (n == 4) {
            Log.v(s, s2);
            return;
        }
        if (n == 5) {
            Log.d(s, s2);
            return;
        }
        throw new IllegalArgumentException("Unknown loglevel");
    }
    
    public static void setCorrelationId(final UUID uuid) {
        getInstance().mCorrelationId = "";
        if (uuid != null) {
            getInstance().mCorrelationId = uuid.toString();
        }
    }
    
    public static void v(final String s, final String s2) {
        getInstance().log(s, s2, null, LogLevel.Verbose, null, null);
    }
    
    public static void v(final String s, final String s2, final String s3, final ADALError adalError) {
        getInstance().log(s, s2, s3, LogLevel.Verbose, adalError, null);
    }
    
    public static void w(final String s, final String s2) {
        getInstance().log(s, s2, null, LogLevel.Warn, null, null);
    }
    
    public static void w(final String s, final String s2, final String s3, final ADALError adalError) {
        getInstance().log(s, s2, s3, LogLevel.Warn, adalError, null);
    }
    
    public String getCorrelationId() {
        return this.mCorrelationId;
    }
    
    public void setAndroidLogEnabled(final boolean mAndroidLogEnabled) {
        this.mAndroidLogEnabled = mAndroidLogEnabled;
    }
    
    public void setEnablePII(final boolean mEnablePII) {
        this.mEnablePII = mEnablePII;
    }
    
    public void setExternalLogger(final ILogger mExternalLogger) {
        synchronized (this) {
            this.mExternalLogger = mExternalLogger;
        }
    }
    
    public void setLogLevel(final LogLevel mLogLevel) {
        this.mLogLevel = mLogLevel;
    }
    
    public interface ILogger
    {
        void Log(final String p0, final String p1, final String p2, final LogLevel p3, final ADALError p4);
    }
    
    public enum LogLevel
    {
        Debug(4), 
        Error(0), 
        Info(2), 
        Verbose(3), 
        Warn(1);
        
        private int mValue;
        
        private LogLevel(final int mValue) {
            this.mValue = mValue;
        }
    }
}
