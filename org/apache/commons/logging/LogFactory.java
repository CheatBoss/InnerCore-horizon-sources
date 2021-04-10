package org.apache.commons.logging;

import java.util.*;

@Deprecated
public abstract class LogFactory
{
    public static final String DIAGNOSTICS_DEST_PROPERTY = "org.apache.commons.logging.diagnostics.dest";
    public static final String FACTORY_DEFAULT = "org.apache.commons.logging.impl.LogFactoryImpl";
    public static final String FACTORY_PROPERTIES = "commons-logging.properties";
    public static final String FACTORY_PROPERTY = "org.apache.commons.logging.LogFactory";
    public static final String HASHTABLE_IMPLEMENTATION_PROPERTY = "org.apache.commons.logging.LogFactory.HashtableImpl";
    public static final String PRIORITY_KEY = "priority";
    protected static final String SERVICE_ID = "META-INF/services/org.apache.commons.logging.LogFactory";
    public static final String TCCL_KEY = "use_tccl";
    protected static Hashtable factories;
    protected static LogFactory nullClassLoaderFactory;
    
    protected LogFactory() {
        throw new RuntimeException("Stub!");
    }
    
    protected static Object createFactory(final String s, final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    protected static ClassLoader directGetContextClassLoader() throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    protected static ClassLoader getClassLoader(final Class clazz) {
        throw new RuntimeException("Stub!");
    }
    
    protected static ClassLoader getContextClassLoader() throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public static LogFactory getFactory() throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public static Log getLog(final Class clazz) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public static Log getLog(final String s) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    protected static boolean isDiagnosticsEnabled() {
        throw new RuntimeException("Stub!");
    }
    
    protected static final void logRawDiagnostic(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    protected static LogFactory newFactory(final String s, final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    protected static LogFactory newFactory(final String s, final ClassLoader classLoader, final ClassLoader classLoader2) throws LogConfigurationException {
        throw new RuntimeException("Stub!");
    }
    
    public static String objectId(final Object o) {
        throw new RuntimeException("Stub!");
    }
    
    public static void release(final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    public static void releaseAll() {
        throw new RuntimeException("Stub!");
    }
    
    public abstract Object getAttribute(final String p0);
    
    public abstract String[] getAttributeNames();
    
    public abstract Log getInstance(final Class p0) throws LogConfigurationException;
    
    public abstract Log getInstance(final String p0) throws LogConfigurationException;
    
    public abstract void release();
    
    public abstract void removeAttribute(final String p0);
    
    public abstract void setAttribute(final String p0, final Object p1);
}
