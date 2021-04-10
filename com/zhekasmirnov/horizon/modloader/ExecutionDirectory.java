package com.zhekasmirnov.horizon.modloader;

import java.io.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import android.content.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import java.util.*;

public class ExecutionDirectory
{
    public final File directory;
    public final boolean isPackDriven;
    private final HashMap<String, List<RuntimeLibrary>> libraries;
    private final List<JavaDirectory> javaDirectories;
    
    public ExecutionDirectory(final File directory, final boolean isPackDriven) {
        this.libraries = new HashMap<String, List<RuntimeLibrary>>();
        this.javaDirectories = new ArrayList<JavaDirectory>();
        this.directory = directory;
        this.isPackDriven = isPackDriven;
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
        if (!this.directory.isDirectory()) {
            throw new IllegalArgumentException("Non-directory file passed to ExecutionDirectory constructor");
        }
    }
    
    public synchronized void addLibraryDirectory(final LibraryDirectory lib) {
        final String name = lib.getSoFileName();
        List<RuntimeLibrary> libsByName = this.libraries.get(name);
        if (libsByName != null) {
            if (lib.isSharedLibrary()) {
                final RuntimeLibrary first = libsByName.get(0);
                final LibraryDirectory firstLib = first.library;
                if (firstLib.isSharedLibrary()) {
                    if (firstLib.getVersionCode() < lib.getVersionCode()) {
                        libsByName.set(0, new RuntimeLibrary(lib, name));
                    }
                }
                else {
                    libsByName.add(first.rename(name + libsByName.size()));
                    libsByName.set(0, new RuntimeLibrary(lib, name));
                }
            }
            else {
                libsByName.add(new RuntimeLibrary(lib, name + libsByName.size()));
            }
        }
        else {
            libsByName = new ArrayList<RuntimeLibrary>();
            libsByName.add(new RuntimeLibrary(lib, name));
            this.libraries.put(name, libsByName);
        }
    }
    
    public LibraryDirectory getLibByName(final String name) {
        final List<RuntimeLibrary> libsByName = this.libraries.get("lib" + name + ".so");
        if (libsByName != null) {
            return libsByName.get(0).library;
        }
        return null;
    }
    
    public void addJavaDirectory(final JavaDirectory directory) {
        this.javaDirectories.add(directory);
    }
    
    public LaunchSequence build(final Context context, final EventLogger logger) {
        if (!this.isPackDriven) {
            FileUtils.clearFileTree(this.directory, false);
        }
        final List<LibraryDirectory> libs = new ArrayList<LibraryDirectory>();
        for (final List<RuntimeLibrary> libByName : this.libraries.values()) {
            for (final RuntimeLibrary lib : libByName) {
                final LibraryDirectory library = lib.library;
                try {
                    library.addToExecutionDirectory(this, context, lib.soFile);
                    libs.add(library);
                }
                catch (Throwable err) {
                    logger.fault("BUILD", "details: lang=c++ dir=" + library.directory, err);
                }
            }
        }
        final List<JavaLibrary> javaLibraries = new ArrayList<JavaLibrary>();
        for (final JavaDirectory javaDir : this.javaDirectories) {
            try {
                javaLibraries.add(javaDir.addToExecutionDirectory(this, context));
            }
            catch (Throwable err2) {
                logger.fault("BUILD", "details: lang=java dir=" + javaDir.directory, err2);
            }
        }
        return new LaunchSequence(this, libs, javaLibraries);
    }
    
    public void clear() {
        this.libraries.clear();
        this.javaDirectories.clear();
        if (!this.isPackDriven) {
            FileUtils.clearFileTree(this.directory, false);
        }
    }
    
    private class RuntimeLibrary
    {
        final LibraryDirectory library;
        final File soFile;
        final String name;
        
        private RuntimeLibrary(final LibraryDirectory library, final String name) {
            this.library = library;
            this.name = name;
            this.soFile = new File(ExecutionDirectory.this.directory, this.name);
        }
        
        public RuntimeLibrary rename(final String name) {
            return new RuntimeLibrary(this.library, name);
        }
    }
}
