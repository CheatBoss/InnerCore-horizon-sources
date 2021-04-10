package com.zhekasmirnov.apparatus.adapter.minecraft.version;

import com.zhekasmirnov.apparatus.adapter.minecraft.addon.*;

public abstract class MinecraftVersion
{
    private final int code;
    private final boolean isBeta;
    private final String name;
    
    protected MinecraftVersion(final String name, final int code, final boolean isBeta) {
        this.name = name;
        this.code = code;
        this.isBeta = isBeta;
    }
    
    public abstract AddonContext createAddonContext();
    
    public int getCode() {
        return this.code;
    }
    
    public String getMainVanillaBehaviorPack() {
        return this.getVanillaBehaviorPacksDirs()[0];
    }
    
    public String getMainVanillaResourcePack() {
        return this.getVanillaResourcePacksDirs()[0];
    }
    
    public String getName() {
        return this.name;
    }
    
    public abstract String[] getVanillaBehaviorPacksDirs();
    
    public abstract String[] getVanillaResourcePacksDirs();
    
    public boolean isBeta() {
        return this.isBeta;
    }
    
    public abstract boolean isVanillaIdRemappingRequired();
}
