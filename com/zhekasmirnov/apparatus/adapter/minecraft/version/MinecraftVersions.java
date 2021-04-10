package com.zhekasmirnov.apparatus.adapter.minecraft.version;

import com.zhekasmirnov.apparatus.adapter.minecraft.addon.*;
import com.zhekasmirnov.apparatus.adapter.minecraft.addon.recipe.*;

public class MinecraftVersions
{
    public static final MinecraftVersion MINECRAFT_1_11_4;
    public static final MinecraftVersion MINECRAFT_1_16_201;
    
    static {
        MINECRAFT_1_11_4 = new MinecraftVersion("1.11.4", 11, false) {
            @Override
            public AddonContext createAddonContext() {
                return new AddonContext(this, new AddonRecipeParser11());
            }
            
            @Override
            public String[] getVanillaBehaviorPacksDirs() {
                return new String[] { "behavior_packs/vanilla/" };
            }
            
            @Override
            public String[] getVanillaResourcePacksDirs() {
                return new String[] { "resource_packs/vanilla/" };
            }
            
            @Override
            public boolean isVanillaIdRemappingRequired() {
                return false;
            }
        };
        MINECRAFT_1_16_201 = new MinecraftVersion("1.16.201", 16, false) {
            @Override
            public AddonContext createAddonContext() {
                return new AddonContext(this, new AddonRecipeParser16());
            }
            
            @Override
            public String[] getVanillaBehaviorPacksDirs() {
                return new String[] { "behavior_packs/vanilla/", "behavior_packs/vanilla_1.16/", "behavior_packs/vanilla_1.16.100/", "behavior_packs/vanilla_1.16.200/" };
            }
            
            @Override
            public String[] getVanillaResourcePacksDirs() {
                return new String[] { "behavior_packs/vanilla/", "resource_packs/vanilla_1.16/", "resource_packs/vanilla_1.16.100/", "resource_packs/vanilla_1.16.200/" };
            }
            
            @Override
            public boolean isVanillaIdRemappingRequired() {
                return true;
            }
        };
    }
    
    public static MinecraftVersion getCurrent() {
        return MinecraftVersions.MINECRAFT_1_16_201;
    }
}
