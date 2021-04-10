package com.microsoft.xal.logging;

import java.text.*;
import java.util.*;
import android.util.*;

public class XalLogger implements AutoCloseable
{
    private static final SimpleDateFormat LogDateFormat;
    private static final String TAG = "XALJAVA";
    private LogLevel m_leastVerboseLevel;
    private final ArrayList<LogEntry> m_logs;
    private final String m_subArea;
    
    static {
        LogDateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    }
    
    public XalLogger(final String subArea) {
        this.m_subArea = subArea;
        this.m_logs = new ArrayList<LogEntry>();
        this.m_leastVerboseLevel = LogLevel.Verbose;
        this.Verbose("XalLogger created.");
    }
    
    private String Timestamp() {
        return XalLogger.LogDateFormat.format(Calendar.getInstance().getTime());
    }
    
    private static native void nativeLogBatch(final int p0, final LogEntry[] p1);
    
    public void Error(final String s) {
        Log.e("XALJAVA", String.format("[%s] %s", this.m_subArea, s));
        this.Log(LogLevel.Error, s);
    }
    
    public void Flush() {
        synchronized (this) {
            if (this.m_logs.isEmpty()) {
                return;
            }
            try {
                nativeLogBatch(this.m_leastVerboseLevel.ToInt(), this.m_logs.toArray(new LogEntry[this.m_logs.size()]));
                this.m_logs.clear();
                this.m_leastVerboseLevel = LogLevel.Verbose;
            }
            catch (UnsatisfiedLinkError unsatisfiedLinkError) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to flush logs: ");
                sb.append(unsatisfiedLinkError.toString());
                sb.toString();
            }
            catch (Exception ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to flush logs: ");
                sb2.append(ex.toString());
                sb2.toString();
                goto Label_0094;
            }
        }
    }
    
    public void Important(final String s) {
        Log.w("XALJAVA", String.format("[%c][%s] %s", LogLevel.Important.ToChar(), this.m_subArea, s));
        this.Log(LogLevel.Important, s);
    }
    
    public void Information(final String s) {
        Log.i("XALJAVA", String.format("[%s] %s", this.m_subArea, s));
        this.Log(LogLevel.Information, s);
    }
    
    public void Log(final LogLevel leastVerboseLevel, final String s) {
        synchronized (this) {
            this.m_logs.add(new LogEntry(leastVerboseLevel, String.format("[%c][%s][%s] %s", leastVerboseLevel.ToChar(), this.Timestamp(), this.m_subArea, s)));
            if (this.m_leastVerboseLevel.ToInt() > leastVerboseLevel.ToInt()) {
                this.m_leastVerboseLevel = leastVerboseLevel;
            }
        }
    }
    
    public void Verbose(final String s) {
        Log.v("XALJAVA", String.format("[%s] %s", this.m_subArea, s));
        this.Log(LogLevel.Verbose, s);
    }
    
    public void Warning(final String s) {
        Log.w("XALJAVA", String.format("[%s] %s", this.m_subArea, s));
        this.Log(LogLevel.Warning, s);
    }
    
    @Override
    public void close() {
        this.Flush();
    }
    
    public enum LogLevel
    {
        Error(1, 'E'), 
        Important(3, 'P'), 
        Information(4, 'I'), 
        Verbose(5, 'V'), 
        Warning(2, 'W');
        
        private final char m_levelChar;
        private final int m_val;
        
        private LogLevel(final int val, final char levelChar) {
            this.m_val = val;
            this.m_levelChar = levelChar;
        }
        
        public char ToChar() {
            return this.m_levelChar;
        }
        
        public int ToInt() {
            return this.m_val;
        }
    }
}
