package org.apache.commons.logging;

import java.lang.reflect.*;
import java.util.*;

@Deprecated
public class LogSource
{
    protected static boolean jdk14IsAvailable;
    protected static boolean log4jIsAvailable;
    protected static Constructor logImplctor;
    protected static Hashtable logs;
    
    LogSource() {
        throw new RuntimeException("Stub!");
    }
    
    public static Log getInstance(final Class clazz) {
        throw new RuntimeException("Stub!");
    }
    
    public static Log getInstance(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static String[] getLogNames() {
        throw new RuntimeException("Stub!");
    }
    
    public static Log makeNewLogInstance(final String s) {
        throw new RuntimeException("Stub!");
    }
    
    public static void setLogImplementation(final Class clazz) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
        throw new RuntimeException("Stub!");
    }
    
    public static void setLogImplementation(final String s) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException, ClassNotFoundException {
        throw new RuntimeException("Stub!");
    }
}
