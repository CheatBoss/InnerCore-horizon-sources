package com.zhekasmirnov.horizon.modloader.resource.runtime;

import com.zhekasmirnov.horizon.modloader.resource.*;
import java.io.*;

public class RuntimeResource
{
    private final RuntimeResourceDirectory directory;
    private final ResourceOverride override;
    private final String name;
    
    public RuntimeResource(final RuntimeResourceDirectory directory, final ResourceOverride override, final String name) {
        this.directory = directory;
        this.override = override;
        this.name = name;
    }
    
    public ResourceOverride getOverride() {
        return this.override;
    }
    
    public RuntimeResourceDirectory getDirectory() {
        return this.directory;
    }
    
    public String getName() {
        return this.name;
    }
    
    public File getFile() {
        final File file = new File(this.override.override);
        final File directory = file.getParentFile();
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return file;
    }
}
