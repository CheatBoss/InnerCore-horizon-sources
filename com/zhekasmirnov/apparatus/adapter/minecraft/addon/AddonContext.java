package com.zhekasmirnov.apparatus.adapter.minecraft.addon;

import com.zhekasmirnov.apparatus.adapter.minecraft.addon.recipe.*;
import com.zhekasmirnov.apparatus.adapter.minecraft.version.*;

public class AddonContext
{
    private static final AddonContext instance;
    private final AddonRecipeParser recipeParser;
    private final MinecraftVersion version;
    
    static {
        instance = MinecraftVersions.getCurrent().createAddonContext();
    }
    
    public AddonContext(final MinecraftVersion version, final AddonRecipeParser recipeParser) {
        this.version = version;
        this.recipeParser = recipeParser;
    }
    
    public static AddonContext getInstance() {
        return AddonContext.instance;
    }
    
    public AddonRecipeParser getRecipeParser() {
        return this.recipeParser;
    }
    
    public MinecraftVersion getVersion() {
        return this.version;
    }
}
