package com.zhekasmirnov.horizon.modloader.java;

import java.io.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import java.util.*;
import java.lang.reflect.*;

public class JavaLibrary
{
    private final JavaDirectory directory;
    private final List<File> dexFiles;
    private boolean initialized;
    
    public JavaLibrary(final JavaDirectory directory, final File dexFile) {
        this.initialized = false;
        this.directory = directory;
        (this.dexFiles = new ArrayList<File>(1)).add(dexFile);
    }
    
    public JavaLibrary(final JavaDirectory directory, final List<File> dexFiles) {
        this.initialized = false;
        this.directory = directory;
        this.dexFiles = dexFiles;
    }
    
    public JavaDirectory getDirectory() {
        return this.directory;
    }
    
    public List<File> getDexFiles() {
        return this.dexFiles;
    }
    
    public boolean isInitialized() {
        return this.initialized;
    }
    
    public void initialize() {
        for (final File dexFile : this.dexFiles) {
            ClassLoaderPatch.addDexPath(JavaLibrary.class.getClassLoader(), dexFile);
        }
        final HashMap<String, Object> data = new HashMap<String, Object>();
        for (final String name : this.directory.getBootClassNames()) {
            try {
                final Class clazz = Class.forName(name);
                final Method method = clazz.getMethod("boot", HashMap.class);
                data.put("class_name", name);
                method.invoke(null, new HashMap(data));
            }
            catch (NoSuchMethodException e) {
                throw new RuntimeException("failed to find boot(HashMap) method in boot class " + name + " of " + this.directory, e);
            }
            catch (IllegalAccessException e2) {
                throw new RuntimeException("failed to access boot method class " + name + " of " + this.directory, e2);
            }
            catch (InvocationTargetException e3) {
                throw new RuntimeException("failed to call boot method in class " + name + " of " + this.directory, e3);
            }
            catch (ClassNotFoundException e4) {
                throw new RuntimeException("failed to find boot class " + name + " in " + this.directory, e4);
            }
        }
        this.initialized = true;
    }
}
