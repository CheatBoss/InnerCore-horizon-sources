package com.zhekasmirnov.apparatus.minecraft.version;

import java.util.*;
import com.zhekasmirnov.apparatus.minecraft.addon.*;
import com.zhekasmirnov.apparatus.util.*;

public abstract class MinecraftVersion
{
    public static final String FEATURE_ACTOR_RENDER_OVERRIDE = "actor_render_override";
    public static final String FEATURE_ATTACHABLE_RENDER = "attachable_render";
    public static final String FEATURE_GLOBAL_SHADER_UNIFORM_SET = "global_shader_uniform_set";
    public static final String FEATURE_VANILLA_ID_MAPPING = "vanilla_id_mapping";
    public static final String FEATURE_VANILLA_WORLD_GENERATION_LEVELS = "vanilla_world_generation_levels";
    private final int code;
    private final boolean isBeta;
    private final String name;
    private final Set<String> supportedFeatures;
    
    protected MinecraftVersion(final String name, final int code, final boolean isBeta) {
        this.supportedFeatures = new HashSet<String>();
        this.name = name;
        this.code = code;
        this.isBeta = isBeta;
        this.addSupportedFeatures(this.supportedFeatures);
    }
    
    public abstract void addSupportedFeatures(final Set<String> p0);
    
    public abstract AddonContext createAddonContext();
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final MinecraftVersion minecraftVersion = (MinecraftVersion)o;
        return this.code == minecraftVersion.code && Java8BackComp.equals(this.name, minecraftVersion.name);
    }
    
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
    
    public Set<String> getSupportedFeatures() {
        return this.supportedFeatures;
    }
    
    public abstract String[] getVanillaBehaviorPacksDirs();
    
    public abstract String[] getVanillaResourcePacksDirs();
    
    @Override
    public int hashCode() {
        return Java8BackComp.hash(this.code, this.name);
    }
    
    public boolean isBeta() {
        return this.isBeta;
    }
    
    public boolean isFeatureSupported(final String s) {
        return this.supportedFeatures.contains(s);
    }
}
