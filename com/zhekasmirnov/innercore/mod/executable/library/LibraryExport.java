package com.zhekasmirnov.innercore.mod.executable.library;

public class LibraryExport
{
    public final String name;
    private int targetVersion;
    public final Object value;
    
    public LibraryExport(final String name, final Object value) {
        this.targetVersion = -1;
        this.name = name;
        this.value = value;
    }
    
    public int getTargetVersion() {
        return this.targetVersion;
    }
    
    public boolean hasTargetVersion() {
        return this.targetVersion != -1;
    }
    
    public void setTargetVersion(final int targetVersion) {
        this.targetVersion = targetVersion;
    }
}
