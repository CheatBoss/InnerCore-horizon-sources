package com.zhekasmirnov.horizon.modloader.resource.runtime;

import java.io.*;
import android.util.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import java.util.*;

public class RuntimeResourceDirectory
{
    public final ResourceManager resourceManager;
    public final File directory;
    private HashMap<String, Pair<RuntimeResource, RuntimeResourceHandler>> resources;
    
    public RuntimeResourceDirectory(final ResourceManager resourceManager, final File directory) {
        this.resources = new HashMap<String, Pair<RuntimeResource, RuntimeResourceHandler>>();
        this.resourceManager = resourceManager;
        this.directory = directory;
        if (!this.directory.exists()) {
            this.directory.mkdirs();
        }
        if (!this.directory.isDirectory()) {
            throw new IllegalArgumentException("Non-directory file passed to RuntimeResourceDirectory constructor");
        }
    }
    
    public void clear() {
        FileUtils.clearFileTree(this.directory, false);
    }
    
    public void addHandler(final RuntimeResourceHandler handler) {
        final String name = handler.getResourceName();
        final ResourceOverride override = new ResourceOverride(handler.getResourcePath(), new File(this.directory, name));
        final RuntimeResource resource = new RuntimeResource(this, override, name);
        this.resources.put(name, (Pair<RuntimeResource, RuntimeResourceHandler>)new Pair((Object)resource, (Object)handler));
    }
    
    private void handleResource(final RuntimeResource resource, final RuntimeResourceHandler handler) {
        resource.getOverride().deploy(this.resourceManager.getResourceOverridePrefixes());
        handler.handle(resource);
    }
    
    public void handleAll() {
        for (final Pair<RuntimeResource, RuntimeResourceHandler> resource : this.resources.values()) {
            this.handleResource((RuntimeResource)resource.first, (RuntimeResourceHandler)resource.second);
        }
    }
}
