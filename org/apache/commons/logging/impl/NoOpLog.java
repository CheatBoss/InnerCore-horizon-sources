package org.apache.commons.logging.impl;

import org.apache.commons.logging.*;
import java.io.*;

@Deprecated
public class NoOpLog implements Log, Serializable
{
    public NoOpLog() {
        throw new RuntimeException("Stub!");
    }
    
    public NoOpLog(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void debug(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void debug(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public void error(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void error(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public void fatal(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void fatal(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public void info(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void info(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isDebugEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isErrorEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isFatalEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isInfoEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isTraceEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public final boolean isWarnEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public void trace(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void trace(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
    
    public void warn(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public void warn(final Object o, final Throwable t) {
        throw new RuntimeException("Stub!");
    }
}
