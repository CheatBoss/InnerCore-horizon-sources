package com.zhekasmirnov.horizon.modloader.resource.directory;

import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.horizon.util.*;

public class ResourceDirectory
{
    public final ResourceManager manager;
    public final RuntimeResourceDirectory runtimeDir;
    public final Mod mod;
    public final File directory;
    private HashMap<String, List<Resource>> resources;
    
    public ResourceDirectory(final ResourceManager manager, final Mod mod, final File directory) {
        this.resources = new HashMap<String, List<Resource>>();
        this.mod = mod;
        if (!directory.isDirectory()) {
            throw new IllegalStateException("non-directory file passed to ResourceDirectory constructor: " + directory);
        }
        this.manager = manager;
        this.runtimeDir = manager.runtimeDir;
        this.directory = directory;
    }
    
    public ResourceDirectory(final ResourceManager manager, final File directory) {
        this(manager, null, directory);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof ResourceDirectory) {
            return ((ResourceDirectory)obj).directory.getAbsolutePath().equals(this.directory.getAbsolutePath());
        }
        return super.equals(obj);
    }
    
    private void initializeDirectory(final List<Resource> resources, final File directory) {
        for (final File file : directory.listFiles()) {
            if (file.isDirectory()) {
                this.initializeDirectory(resources, file);
            }
            else {
                resources.add(new Resource(this, file));
            }
        }
    }
    
    private void addResourceByPath(final String path, final Resource resource) {
        List<Resource> resourcesByPath = this.resources.get(path);
        if (resourcesByPath == null) {
            resourcesByPath = new ArrayList<Resource>();
            this.resources.put(path, resourcesByPath);
        }
        int index = 0;
        for (final Resource res : resourcesByPath) {
            ++index;
            if (res.getIndex() < resource.getIndex()) {
                break;
            }
        }
        resourcesByPath.add(index, resource);
        this.manager.addResourcePath(path);
    }
    
    public void initialize() {
        final List<Resource> resourceList = new ArrayList<Resource>();
        this.initializeDirectory(resourceList, this.directory);
        this.resources.clear();
        for (final Resource resource : resourceList) {
            this.addResourceByPath(resource.getPath(), resource);
        }
    }
    
    public String getResourceName(final File file) {
        final String dir = this.directory.getAbsolutePath();
        if (file.getAbsolutePath().startsWith(dir)) {
            return FileUtils.cleanupPath(file.getAbsolutePath().substring(dir.length()));
        }
        return null;
    }
    
    public List<Resource> getResource(final String path) {
        return this.resources.get(path);
    }
}
