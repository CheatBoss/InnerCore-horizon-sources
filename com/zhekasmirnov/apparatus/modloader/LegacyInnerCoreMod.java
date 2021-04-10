package com.zhekasmirnov.apparatus.modloader;

import java.io.*;
import com.zhekasmirnov.apparatus.adapter.env.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import java.util.*;

public class LegacyInnerCoreMod extends DirectoryBasedMod
{
    private final Mod legacyModInstance;
    
    public LegacyInnerCoreMod(final Mod legacyModInstance) {
        super(new File(legacyModInstance.dir));
        this.getInfo().pullLegacyModProperties(legacyModInstance);
        this.legacyModInstance = legacyModInstance;
    }
    
    public Mod getLegacyModInstance() {
        return this.legacyModInstance;
    }
    
    @Override
    public boolean isEnabledAndAbleToRun() {
        return this.legacyModInstance.getConfig().getBool("enabled") && this.legacyModInstance.buildConfig.defaultConfig.gameVersion.isCompatible();
    }
    
    @Override
    public void onPrepareResources(final ModLoaderReporter modLoaderReporter) {
        this.legacyModInstance.RunPreloaderScripts();
    }
    
    @Override
    public void onRunningMod(final ModLoaderReporter modLoaderReporter) {
        this.legacyModInstance.RunLauncherScripts();
    }
    
    @Override
    public void onSettingUpEnvironment(final EnvironmentSetupProxy environmentSetupProxy, final ModLoaderReporter modLoaderReporter) {
        for (final BuildConfig.ResourceDir resourceDir : this.legacyModInstance.buildConfig.resourceDirs) {
            if (resourceDir.gameVersion.isCompatible()) {
                switch (resourceDir.resourceType) {
                    default: {
                        continue;
                    }
                    case GUI: {
                        environmentSetupProxy.addGuiAssetsDirectory(this, new File(this.getDirectory(), resourceDir.path));
                        continue;
                    }
                    case RESOURCE: {
                        environmentSetupProxy.addResourceDirectory(this, new File(this.getDirectory(), resourceDir.path));
                        continue;
                    }
                }
            }
        }
        final String behaviorPacksDir = this.legacyModInstance.buildConfig.defaultConfig.behaviorPacksDir;
        final int n = 0;
        if (behaviorPacksDir != null) {
            final File[] listFiles = new File(this.getDirectory(), behaviorPacksDir).listFiles();
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    final File file = listFiles[i];
                    if (new ResourceGameVersion(new File(file, "game_version.json")).isCompatible()) {
                        environmentSetupProxy.addBehaviorPackDirectory(this, file);
                    }
                }
            }
        }
        final String resourcePacksDir = this.legacyModInstance.buildConfig.defaultConfig.resourcePacksDir;
        if (resourcePacksDir != null) {
            final File[] listFiles2 = new File(this.getDirectory(), resourcePacksDir).listFiles();
            if (listFiles2 != null) {
                for (int length2 = listFiles2.length, j = n; j < length2; ++j) {
                    final File file2 = listFiles2[j];
                    if (new ResourceGameVersion(new File(file2, "game_version.json")).isCompatible()) {
                        environmentSetupProxy.addResourcePackDirectory(this, file2);
                    }
                }
            }
        }
        for (final BuildConfig.DeclaredDirectory declaredDirectory : this.legacyModInstance.buildConfig.javaDirectories) {
            if (declaredDirectory.version.isCompatible()) {
                environmentSetupProxy.addJavaDirectory(this, declaredDirectory.getFile(this.getDirectory()));
            }
        }
        for (final BuildConfig.DeclaredDirectory declaredDirectory2 : this.legacyModInstance.buildConfig.nativeDirectories) {
            if (declaredDirectory2.version.isCompatible()) {
                environmentSetupProxy.addNativeDirectory(this, declaredDirectory2.getFile(this.getDirectory()));
            }
        }
    }
    
    @Override
    public void onShuttingDown(final ModLoaderReporter modLoaderReporter) {
    }
}
