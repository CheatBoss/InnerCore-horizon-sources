package com.zhekasmirnov.horizon.modloader.resource;

import android.content.*;
import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import com.zhekasmirnov.horizon.compiler.packages.*;
import java.io.*;
import android.content.res.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import java.util.*;

public class ResourceManager
{
    public final Context context;
    public final RuntimeResourceDirectory runtimeDir;
    private List<String> resourceOverridePrefixes;
    private List<ResourceProcessor> processors;
    private List<ResourceDirectory> directories;
    private HashSet<String> resourcePaths;
    
    public ResourceManager(final Context context) {
        this.resourceOverridePrefixes = new ArrayList<String>();
        this.processors = new ArrayList<ResourceProcessor>();
        this.directories = new ArrayList<ResourceDirectory>();
        this.resourcePaths = new HashSet<String>();
        this.context = context;
        this.runtimeDir = new RuntimeResourceDirectory(this, new File(Environment.getDataDirFile(context), "resovd"));
        this.addResourcePrefixes("");
    }
    
    public AssetManager getAssets() {
        return this.context.getAssets();
    }
    
    public void addResourcePrefixes(final String... prefixes) {
        for (final String prefix : prefixes) {
            if (!this.resourceOverridePrefixes.contains(prefix)) {
                this.resourceOverridePrefixes.add(prefix);
            }
        }
    }
    
    public List<String> getResourceOverridePrefixes() {
        return this.resourceOverridePrefixes;
    }
    
    public void addResourceProcessor(final ResourceProcessor processor) {
        this.processors.add(processor);
        processor.initialize(this);
    }
    
    public void addRuntimeResourceHandler(final RuntimeResourceHandler handler) {
        this.runtimeDir.addHandler(handler);
    }
    
    public void addResourceDirectory(final ResourceDirectory directory) {
        for (final ResourceDirectory dir : this.directories) {
            if (dir.equals(directory)) {
                return;
            }
        }
        this.directories.add(directory);
        directory.initialize();
    }
    
    public void clear() {
        this.directories.clear();
        this.runtimeDir.clear();
    }
    
    public List<Resource> getProcessedResources(List<Resource> resources) {
        List<Resource> processed = resources;
        for (final ResourceProcessor processor : this.processors) {
            processed = new ArrayList<Resource>();
            for (final Resource resource : resources) {
                processor.process(resource, processed);
            }
            resources = processed;
        }
        return processed;
    }
    
    private static void mergeResourceArrays(final List<Resource> source, final List<Resource> adding) {
        int srcIndex = 0;
        int addIndex = 0;
        while (addIndex < adding.size()) {
            final Resource srcResource = source.get(srcIndex);
            final Resource addResource = adding.get(addIndex);
            if (srcResource.getIndex() > addResource.getIndex()) {
                source.add(srcIndex, addResource);
                ++addIndex;
            }
            else if (srcResource.getIndex() < addResource.getIndex()) {
                if (addIndex >= source.size()) {
                    source.add(addResource);
                }
                ++srcIndex;
            }
            else {
                ++addIndex;
            }
        }
    }
    
    public List<Resource> getResource(final String path) {
        List<Resource> resources = null;
        for (final ResourceDirectory directory : this.directories) {
            final List<Resource> dirResources = directory.getResource(path);
            if (dirResources != null) {
                if (resources != null) {
                    mergeResourceArrays(resources, dirResources);
                }
                else {
                    resources = new ArrayList<Resource>(dirResources);
                }
            }
        }
        return resources;
    }
    
    public void addResourcePath(final String path) {
        this.resourcePaths.add(path);
    }
    
    public void deployAllOverrides() {
        final List<ResourceOverride> overrides = new ArrayList<ResourceOverride>();
        for (final String path : this.resourcePaths) {
            final List<Resource> resources = this.getResource(path);
            if (resources != null) {
                for (final Resource resource : this.getProcessedResources(resources)) {
                    resource.addOverrides(overrides);
                }
            }
        }
        for (final ResourceOverride override : overrides) {
            override.deploy(this.resourceOverridePrefixes);
        }
        this.runtimeDir.handleAll();
    }
}
