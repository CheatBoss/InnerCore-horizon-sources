package com.zhekasmirnov.horizon.modloader.resource;

import com.zhekasmirnov.horizon.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import java.util.*;
import com.zhekasmirnov.horizon.*;

public class ResourceOverride
{
    public final String path;
    public final String override;
    private final List<String> injectedOverrides;
    
    public ResourceOverride(final String path, final String override) {
        this.injectedOverrides = new ArrayList<String>();
        this.path = FileUtils.cleanupPath(path);
        this.override = FileUtils.cleanupPath(override);
    }
    
    public ResourceOverride(final String path, final File override) {
        this(path, override.getAbsolutePath());
    }
    
    public ResourceOverride(final Resource resource, final String override) {
        this(resource.getPath(), override);
    }
    
    public ResourceOverride(final Resource resource, final File override) {
        this(resource.getPath(), override.getAbsolutePath());
    }
    
    public ResourceOverride(final Resource resource) {
        this(resource, resource.file);
    }
    
    public boolean isActive() {
        return this.injectedOverrides.size() > 0 && AssetPatch.getSingleOverride(this.injectedOverrides.get(0)) != null;
    }
    
    public boolean deploy() {
        return this.deploy(new String[] { "" });
    }
    
    public boolean deploy(final List<String> prefixes) {
        return this.deploy(prefixes.toArray(new String[0]));
    }
    
    public boolean deploy(final String[] prefixes) {
        this.remove();
        for (final String prefix : prefixes) {
            final String asset = prefix + this.path;
            this.injectedOverrides.add(asset);
            AssetPatch.addSingleOverride(asset, this.override);
        }
        return true;
    }
    
    public boolean remove() {
        for (final String asset : this.injectedOverrides) {
            AssetPatch.removeSingleOverride(asset);
        }
        this.injectedOverrides.clear();
        return true;
    }
    
    static {
        HorizonLibrary.include();
    }
}
