package org.mozilla.javascript;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class ClassCache implements Serializable
{
    private static final Object AKEY;
    private static final long serialVersionUID = -8866246036237312215L;
    private Scriptable associatedScope;
    private volatile boolean cachingIsEnabled;
    private transient Map<JavaAdapter.JavaAdapterSignature, Class<?>> classAdapterCache;
    private transient Map<Class<?>, JavaMembers> classTable;
    private int generatedClassSerial;
    private transient Map<Class<?>, Object> interfaceAdapterCache;
    
    static {
        AKEY = "ClassCache";
    }
    
    public ClassCache() {
        this.cachingIsEnabled = true;
    }
    
    public static ClassCache get(final Scriptable scriptable) {
        final ClassCache classCache = (ClassCache)ScriptableObject.getTopScopeValue(scriptable, ClassCache.AKEY);
        if (classCache == null) {
            throw new RuntimeException("Can't find top level scope for ClassCache.get");
        }
        return classCache;
    }
    
    public boolean associate(final ScriptableObject associatedScope) {
        if (associatedScope.getParentScope() != null) {
            throw new IllegalArgumentException();
        }
        if (this == associatedScope.associateValue(ClassCache.AKEY, this)) {
            this.associatedScope = associatedScope;
            return true;
        }
        return false;
    }
    
    void cacheInterfaceAdapter(final Class<?> clazz, final Object o) {
        synchronized (this) {
            if (this.cachingIsEnabled) {
                if (this.interfaceAdapterCache == null) {
                    this.interfaceAdapterCache = new ConcurrentHashMap<Class<?>, Object>(16, 0.75f, 1);
                }
                this.interfaceAdapterCache.put(clazz, o);
            }
        }
    }
    
    public void clearCaches() {
        synchronized (this) {
            this.classTable = null;
            this.classAdapterCache = null;
            this.interfaceAdapterCache = null;
        }
    }
    
    Scriptable getAssociatedScope() {
        return this.associatedScope;
    }
    
    Map<Class<?>, JavaMembers> getClassCacheMap() {
        if (this.classTable == null) {
            this.classTable = new ConcurrentHashMap<Class<?>, JavaMembers>(16, 0.75f, 1);
        }
        return this.classTable;
    }
    
    Object getInterfaceAdapter(final Class<?> clazz) {
        if (this.interfaceAdapterCache == null) {
            return null;
        }
        return this.interfaceAdapterCache.get(clazz);
    }
    
    Map<JavaAdapter.JavaAdapterSignature, Class<?>> getInterfaceAdapterCacheMap() {
        if (this.classAdapterCache == null) {
            this.classAdapterCache = new ConcurrentHashMap<JavaAdapter.JavaAdapterSignature, Class<?>>(16, 0.75f, 1);
        }
        return this.classAdapterCache;
    }
    
    public final boolean isCachingEnabled() {
        return this.cachingIsEnabled;
    }
    
    @Deprecated
    public boolean isInvokerOptimizationEnabled() {
        return false;
    }
    
    public final int newClassSerialNumber() {
        synchronized (this) {
            return ++this.generatedClassSerial;
        }
    }
    
    public void setCachingEnabled(final boolean cachingIsEnabled) {
        synchronized (this) {
            if (cachingIsEnabled == this.cachingIsEnabled) {
                return;
            }
            if (!cachingIsEnabled) {
                this.clearCaches();
            }
            this.cachingIsEnabled = cachingIsEnabled;
        }
    }
    
    @Deprecated
    public void setInvokerOptimizationEnabled(final boolean b) {
    }
    // monitorenter(this)
    // monitorexit(this)
}
