package com.zhekasmirnov.horizon.runtime.logger;

import java.util.*;
import java.io.*;

public class EventLogger
{
    private List<Message> messages;
    private String currentSection;
    
    public EventLogger() {
        this.messages = new ArrayList<Message>();
        this.currentSection = null;
    }
    
    public synchronized List<Message> getMessages(final Filter filter) {
        final List<Message> result = new ArrayList<Message>();
        for (final Message message : this.messages) {
            if (filter.filter(message)) {
                result.add(message);
            }
        }
        return result;
    }
    
    private synchronized void addMessage(final MessageType type, final String tag, final String message) {
        this.messages.add(new Message(type, tag, message));
    }
    
    public void section(final String section) {
        this.currentSection = section;
    }
    
    public void debug(final String tag, final String message) {
        this.addMessage(MessageType.DEBUG, tag, message);
        Logger.debug(tag, message.replaceAll("%", "%%"));
    }
    
    public void info(final String tag, final String message) {
        this.addMessage(MessageType.INFO, tag, message);
        Logger.info(tag, message.replaceAll("%", "%%"));
    }
    
    private static String getStackTrace(final Throwable err) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw);
        err.printStackTrace(pw);
        return sw.toString();
    }
    
    public void fault(final String tag, final String message, final Throwable error) {
        Logger.error(tag, message.replaceAll("%", "%%"));
        this.addMessage(MessageType.FAULT, tag, message);
        if (error != null) {
            this.addMessage(MessageType.EXCEPTION, tag, error.getMessage());
            final String trace = getStackTrace(error);
            Logger.error(tag, trace.replaceAll("%", "%%"));
            this.addMessage(MessageType.STACKTRACE, tag, trace);
        }
    }
    
    public void fault(final String tag, final String message) {
        this.fault(tag, message, null);
    }
    
    public OutputStream getStream(final MessageType type, final String tag) {
        return new OutputStream() {
            StringBuffer buffer = new StringBuffer();
            
            @Override
            public void write(final int b) throws IOException {
                this.buffer.append((char)b);
                if (b == 10) {
                    final String message = this.buffer.toString();
                    EventLogger.this.addMessage(type, tag, message);
                    Logger.info(type + "/BUILD", message);
                    this.buffer = new StringBuffer();
                }
            }
        };
    }
    
    public void clear() {
        this.messages.clear();
    }
    
    public enum MessageType
    {
        DEBUG, 
        INFO, 
        FAULT, 
        EXCEPTION, 
        STACKTRACE;
    }
    
    public class Message
    {
        public final MessageType type;
        public final String tag;
        public final String message;
        public final String section;
        
        private Message(final MessageType type, final String tag, final String message) {
            this.type = type;
            this.tag = tag;
            this.message = message;
            this.section = EventLogger.this.currentSection;
        }
    }
    
    public interface Filter
    {
        boolean filter(final Message p0);
    }
}
