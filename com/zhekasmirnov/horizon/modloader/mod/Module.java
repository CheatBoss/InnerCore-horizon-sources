package com.zhekasmirnov.horizon.modloader.mod;

import java.util.*;
import com.zhekasmirnov.horizon.*;

public class Module
{
    private final long handle;
    private static HashMap<Long, Module> moduleByHandle;
    
    private Module(final long handle) {
        Module.moduleByHandle.put(handle, this);
        this.handle = handle;
    }
    
    public static Module getInstance(final long handle) {
        final Module module = Module.moduleByHandle.get(handle);
        return (module != null) ? module : new Module(handle);
    }
    
    private static native long nativeGetParent(final long p0);
    
    public Module getParent() {
        return getInstance(nativeGetParent(this.handle));
    }
    
    private static native String nativeGetNameID(final long p0);
    
    public String getNameID() {
        return nativeGetNameID(this.handle);
    }
    
    private static native String nativeGetType(final long p0);
    
    public String getType() {
        return nativeGetType(this.handle);
    }
    
    private static native boolean nativeIsInitialized(final long p0);
    
    public boolean isInitialized() {
        return nativeIsInitialized(this.handle);
    }
    
    private static native void nativeOnEvent(final long p0, final String p1);
    
    public void onEvent(final String event) {
        nativeOnEvent(this.handle, event);
    }
    
    public boolean isMod() {
        return "MOD".equals(this.getType());
    }
    
    static {
        HorizonLibrary.include();
        Module.moduleByHandle = new HashMap<Long, Module>();
    }
}
