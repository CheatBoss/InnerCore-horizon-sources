package org.apache.http.util;

import java.util.*;

@Deprecated
public class VersionInfo
{
    public static final String PROPERTY_MODULE = "info.module";
    public static final String PROPERTY_RELEASE = "info.release";
    public static final String PROPERTY_TIMESTAMP = "info.timestamp";
    public static final String UNAVAILABLE = "UNAVAILABLE";
    public static final String VERSION_PROPERTY_FILE = "version.properties";
    
    protected VersionInfo(final String s, final String s2, final String s3, final String s4, final String s5) {
        throw new RuntimeException("Stub!");
    }
    
    protected static final VersionInfo fromMap(final String s, final Map map, final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    public static final VersionInfo loadVersionInfo(final String s, final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    public static final VersionInfo[] loadVersionInfo(final String[] array, final ClassLoader classLoader) {
        throw new RuntimeException("Stub!");
    }
    
    public final String getClassloader() {
        throw new RuntimeException("Stub!");
    }
    
    public final String getModule() {
        throw new RuntimeException("Stub!");
    }
    
    public final String getPackage() {
        throw new RuntimeException("Stub!");
    }
    
    public final String getRelease() {
        throw new RuntimeException("Stub!");
    }
    
    public final String getTimestamp() {
        throw new RuntimeException("Stub!");
    }
    
    @Override
    public String toString() {
        throw new RuntimeException("Stub!");
    }
}
