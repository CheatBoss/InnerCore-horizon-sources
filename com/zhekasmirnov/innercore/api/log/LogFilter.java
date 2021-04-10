package com.zhekasmirnov.innercore.api.log;

import java.util.*;

public class LogFilter
{
    private static boolean isContainingErrorTags;
    ArrayList<LogMessage> logMessages;
    
    static {
        LogFilter.isContainingErrorTags = false;
    }
    
    public LogFilter() {
        this.logMessages = new ArrayList<LogMessage>();
    }
    
    private LogMessage getMessageByIndex(final int n) {
        if (n >= 0 && n < this.logMessages.size()) {
            return this.logMessages.get(n);
        }
        return null;
    }
    
    public static boolean isContainingErrorTags() {
        return LogFilter.isContainingErrorTags;
    }
    
    public String buildFilteredLog(final boolean b) {
        String string = "";
        for (int i = 0; i < this.logMessages.size(); ++i) {
            final LogMessage messageByIndex = this.getMessageByIndex(i);
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(messageByIndex.format(b));
            String s;
            if (b) {
                s = "<br>";
            }
            else {
                s = "\n";
            }
            sb.append(s);
            string = sb.toString();
        }
        return string;
    }
    
    public void log(final LogType logType, final String s, final String s2) {
        if (logType == LogType.ERROR || s.equals("ERROR")) {
            LogFilter.isContainingErrorTags = true;
        }
        this.logMessages.add(new LogMessage(logType, s, s2));
    }
    
    public static class LogMessage
    {
        public final String message;
        public final LogPrefix prefix;
        public final String strPrefix;
        public final LogType type;
        
        public LogMessage(final LogType type, final String strPrefix, final String message) {
            this.type = type;
            this.strPrefix = strPrefix;
            this.prefix = LogPrefix.fromString(strPrefix);
            this.message = message;
        }
        
        public String format(final boolean b) {
            if (b) {
                return this.toHtml();
            }
            return this.toString();
        }
        
        public String toHtml() {
            final StringBuilder sb = new StringBuilder();
            sb.append("<font color='");
            sb.append(this.prefix.toFontColor());
            sb.append("'><b>[");
            sb.append(this.strPrefix);
            sb.append("]</b> ");
            sb.append(this.message);
            sb.append("</font>");
            return sb.toString();
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");
            sb.append(this.strPrefix);
            sb.append("] ");
            sb.append(this.message);
            return sb.toString();
        }
    }
}
