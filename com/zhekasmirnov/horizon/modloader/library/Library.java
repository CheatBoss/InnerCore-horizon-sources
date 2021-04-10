package com.zhekasmirnov.horizon.modloader.library;

import com.zhekasmirnov.horizon.modloader.mod.*;
import android.annotation.*;
import java.util.*;

public class Library
{
    private static HashMap<String, Library> loadedLibraries;
    private final long handle;
    private final int result;
    private List<Module> modules;
    
    private static native long nativeLoad(final String p0);
    
    @SuppressLint({ "UnsafeDynamicallyLoadedCode" })
    public static Library load(final String path) {
        if (Library.loadedLibraries.containsKey(path)) {
            return Library.loadedLibraries.get(path);
        }
        Library library = null;
        System.load(path);
        final long handle = nativeLoad(path);
        if (handle != 0L) {
            library = new Library(handle);
        }
        Library.loadedLibraries.put(path, library);
        return library;
    }
    
    private Library(final long handle) {
        this.modules = new ArrayList<Module>();
        this.handle = handle;
        this.result = this.nativeGetResult(handle);
        this.refreshModuleList();
    }
    
    private native long[] nativeGetModules(final long p0);
    
    public int getResult() {
        return this.result;
    }
    
    private native int nativeGetResult(final long p0);
    
    public void refreshModuleList() {
        this.modules.clear();
        for (final long module : this.nativeGetModules(this.handle)) {
            this.modules.add(Module.getInstance(module));
        }
    }
    
    public List<Module> getModules() {
        return this.modules;
    }
    
    static {
        Library.loadedLibraries = new HashMap<String, Library>();
    }
}
