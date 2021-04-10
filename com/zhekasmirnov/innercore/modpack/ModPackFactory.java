package com.zhekasmirnov.innercore.modpack;

import java.io.*;
import com.zhekasmirnov.innercore.modpack.strategy.extract.*;
import com.zhekasmirnov.innercore.modpack.strategy.request.*;
import com.zhekasmirnov.innercore.modpack.strategy.update.*;

public class ModPackFactory
{
    private static final ModPackFactory instance;
    
    static {
        instance = new ModPackFactory();
    }
    
    private ModPackFactory() {
    }
    
    public static ModPackFactory getInstance() {
        return ModPackFactory.instance;
    }
    
    private ModPackDirectory newDefaultPackDirectory(final ModPackDirectory.DirectoryType directoryType, final File file, final String s, final DirectoryRequestStrategy directoryRequestStrategy) {
        return this.newDefaultPackDirectory(directoryType, file, s, directoryRequestStrategy, new AllFilesDirectoryExtractStrategy());
    }
    
    private ModPackDirectory newDefaultPackDirectory(final ModPackDirectory.DirectoryType directoryType, final File file, final String s, final DirectoryRequestStrategy directoryRequestStrategy, final DirectoryExtractStrategy directoryExtractStrategy) {
        return new ModPackDirectory(directoryType, new File(file, s), s, directoryRequestStrategy, new DirectoryDeniedUpdateStrategy(), directoryExtractStrategy);
    }
    
    public ModPack createDefault(final File file) {
        return new ModPack(file).addDirectory(this.newDefaultPackDirectory(ModPackDirectory.DirectoryType.MODS, file, "mods", new DefaultDirectoryRequestStrategy(), new ModsWithoutConfigExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.CONFIG, new File(file, "mods"), "config", new ConfigInModDirectoryRequestStrategy(), new DirectoryDeniedUpdateStrategy(), new ConfigFromModsExtractStrategy())).addDirectory(this.newDefaultPackDirectory(ModPackDirectory.DirectoryType.MOD_ASSETS, file, "mod_assets", new DefaultDirectoryRequestStrategy())).addDirectory(this.newDefaultPackDirectory(ModPackDirectory.DirectoryType.CACHE, file, "cache", new DefaultDirectoryRequestStrategy(), new AllIgnoredDirectoryExtractStrategy())).addDirectory(this.newDefaultPackDirectory(ModPackDirectory.DirectoryType.RESOURCE_PACKS, file, "resource_packs", new DefaultDirectoryRequestStrategy())).addDirectory(this.newDefaultPackDirectory(ModPackDirectory.DirectoryType.BEHAVIOR_PACKS, file, "behavior_packs", new DefaultDirectoryRequestStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.TEXTURE_PACKS, new File(file.getParentFile(), "resourcepacks"), "texture_packs", new DefaultDirectoryRequestStrategy(), new DirectoryDeniedUpdateStrategy(), new AllFilesDirectoryExtractStrategy()));
    }
    
    public ModPack createFromDirectory(final File file) {
        return new ModPack(file).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.MODS, new File(file, "mods"), "mods", new DefaultDirectoryRequestStrategy(), new ResourceDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.MOD_ASSETS, new File(file, "mod_assets"), "mod_assets", new DefaultDirectoryRequestStrategy(), new ResourceDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.CONFIG, new File(file, "config"), "config", new ConfigDirectoryRequestStrategy(), new JsonMergeDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.CACHE, new File(file, "cache"), "cache", new DefaultDirectoryRequestStrategy(), new CacheDirectoryUpdateStrategy(), new AllIgnoredDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.RESOURCE_PACKS, new File(file, "resource_packs"), "resource_packs", new DefaultDirectoryRequestStrategy(), new ResourceDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.BEHAVIOR_PACKS, new File(file, "behavior_packs"), "behavior_packs", new DefaultDirectoryRequestStrategy(), new ResourceDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy())).addDirectory(new ModPackDirectory(ModPackDirectory.DirectoryType.TEXTURE_PACKS, new File(file, "texture_packs"), "texture_packs", new DefaultDirectoryRequestStrategy(), new ResourceDirectoryUpdateStrategy(), new AllFilesDirectoryExtractStrategy()));
    }
}
