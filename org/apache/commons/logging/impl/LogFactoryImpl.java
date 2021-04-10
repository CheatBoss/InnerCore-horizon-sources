package org.apache.commons.logging.impl;

import java.util.*;
import java.lang.reflect.*;
import org.apache.commons.logging.*;

@Deprecated
public class LogFactoryImpl extends LogFactory
{
    public static final String ALLOW_FLAWED_CONTEXT_PROPERTY = "org.apache.commons.logging.Log.allowFlawedContext";
    public static final String ALLOW_FLAWED_DISCOVERY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedDiscovery";
    public static final String ALLOW_FLAWED_HIERARCHY_PROPERTY = "org.apache.commons.logging.Log.allowFlawedHierarchy";
    public static final String LOG_PROPERTY = "org.apache.commons.logging.Log";
    protected static final String LOG_PROPERTY_OLD = "org.apache.commons.logging.log";
    protected Hashtable attributes;
    protected Hashtable instances;
    protected Constructor logConstructor;
    protected Class[] logConstructorSignature;
    protected Method logMethod;
    protected Class[] logMethodSignature;
    
    public LogFactoryImpl() {
        this.logConstructorSignature = null;
        this.logMethodSignature = null;
        throw new RuntimeException("Stub!");
    }
    
    protected static ClassLoader getClassLoader(final Class clazz) {
        throw new RuntimeException("Stub!");
    }
    
    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    protected static boolean isDiagnosticsEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    public Object getAttribute(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public String[] getAttributeNames() {
        throw new RuntimeException("Stub!");
    }
    
    public Log getInstance(final Class clazz) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public Log getInstance(final String s) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected String getLogClassName() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected Constructor getLogConstructor() throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected boolean isJdk13LumberjackAvailable() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected boolean isJdk14Available() {
        throw new RuntimeException("Stub!");
    }
    
    @Deprecated
    protected boolean isLog4JAvailable() {
        throw new RuntimeException("Stub!");
    }
    
    protected void logDiagnostic(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected Log newInstance(final String s) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public void release() {
        throw new RuntimeException("Stub!");
    }
    
    public void removeAttribute(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public void setAttribute(final String s, final Object o) {
        throw new RuntimeException("Stub!");
    }
}
