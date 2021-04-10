package com.zhekasmirnov.innercore.mod.executable;

import com.zhekasmirnov.innercore.api.mod.*;

public class CompilerConfig
{
    private API apiInstance;
    public boolean isLibrary;
    private String modName;
    private String name;
    private int optimizationLevel;
    
    public CompilerConfig(final API apiInstance) {
        this.optimizationLevel = -1;
        this.name = "Unknown Executable";
        this.modName = null;
        this.isLibrary = false;
        this.apiInstance = apiInstance;
    }
    
    public API getApiInstance() {
        return this.apiInstance;
    }
    
    public String getFullName() {
        if (this.modName != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.modName);
            sb.append("$");
            sb.append(this.name);
            return sb.toString();
        }
        return this.name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getOptimizationLevel() {
        return this.optimizationLevel;
    }
    
    public void setModName(final String modName) {
        this.modName = modName;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setOptimizationLevel(final int optimizationLevel) {
        this.optimizationLevel = optimizationLevel;
    }
}
