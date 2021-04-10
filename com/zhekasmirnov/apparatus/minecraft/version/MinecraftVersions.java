package com.zhekasmirnov.apparatus.minecraft.version;

import com.zhekasmirnov.apparatus.minecraft.addon.*;
import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import java.util.*;

public class MinecraftVersions
{
    public static final MinecraftVersion MINECRAFT_1_11_4;
    public static final MinecraftVersion MINECRAFT_1_16_201;
    private static final List<MinecraftVersion> allVersions;
    
    static {
        MINECRAFT_1_11_4 = new MinecraftVersion("1.11.4", 11, false) {
            @Override
            public void addSupportedFeatures(final Set<String> set) {
                set.add("actor_render_override");
            }
            
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
        };
        MINECRAFT_1_16_201 = new MinecraftVersion("1.16.201", 16, false) {
            @Override
            public void addSupportedFeatures(final Set<String> set) {
                set.add("vanilla_id_mapping");
                set.add("vanilla_world_generation_levels");
                set.add("attachable_render");
                set.add("global_shader_uniform_set");
            }
            
            @Override
            public AddonContext createAddonContext() {
                return new AddonContext(this, new AddonRecipeParser16());
            }
            
            @Override
            public String[] getVanillaBehaviorPacksDirs() {
                return new String[] { "behavior_packs/vanilla/", "behavior_packs/vanilla_1.14/", "behavior_packs/vanilla_1.15/", "behavior_packs/vanilla_1.16/", "behavior_packs/vanilla_1.16.100/", "behavior_packs/vanilla_1.16.200/" };
            }
            
            @Override
            public String[] getVanillaResourcePacksDirs() {
                return new String[] { "resource_packs/vanilla/", "resource_packs/vanilla_1.14/", "resource_packs/vanilla_1.15/", "resource_packs/vanilla_1.16/", "resource_packs/vanilla_1.16.100/", "resource_packs/vanilla_1.16.200/" };
            }
        };
        (allVersions = new ArrayList<MinecraftVersion>()).add(MinecraftVersions.MINECRAFT_1_11_4);
        MinecraftVersions.allVersions.add(MinecraftVersions.MINECRAFT_1_16_201);
    }
    
    public static List<MinecraftVersion> getAllVersions() {
        return MinecraftVersions.allVersions;
    }
    
    public static MinecraftVersion getCurrent() {
        return MinecraftVersions.MINECRAFT_1_16_201;
    }
    
    public static MinecraftVersion getVersionByCode(final int n) {
        for (final MinecraftVersion minecraftVersion : MinecraftVersions.allVersions) {
            if (minecraftVersion.getCode() == n) {
                return minecraftVersion;
            }
        }
        return null;
    }
}
