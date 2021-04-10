package com.zhekasmirnov.horizon.modloader.mod;

import com.zhekasmirnov.horizon.modloader.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.modloader.repo.location.*;
import java.io.*;
import org.json.*;
import java.util.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import com.zhekasmirnov.horizon.modloader.configuration.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.horizon.activity.util.*;

public class Mod
{
    public final File directory;
    public final ModManifest manifest;
    private final ModContext context;
    private final ExecutionDirectory executionDirectory;
    private final ResourceManager resourceManager;
    public final List<LibraryDirectory> libraries;
    public final List<ResourceDirectory> resources;
    public final List<JavaDirectory> java;
    public final List<Module> modules;
    public final List<ModInstance> modInstances;
    public final List<ModLocation> subModLocations;
    private ConfigurationInterface configInterface;
    private DeveloperInterface devInterface;
    private SafetyInterface safetyInterface;
    private ModGraphics graphics;
    
    public Mod(final ModContext ctx, final File directory) {
        this.libraries = new ArrayList<LibraryDirectory>();
        this.resources = new ArrayList<ResourceDirectory>();
        this.java = new ArrayList<JavaDirectory>();
        this.modules = new ArrayList<Module>();
        this.modInstances = new ArrayList<ModInstance>();
        this.subModLocations = new ArrayList<ModLocation>();
        try {
            this.manifest = new ModManifest(new File(directory, "manifest"));
        }
        catch (IOException err) {
            throw new RuntimeException("failed to read mod manifest for " + directory, err);
        }
        catch (JSONException err2) {
            throw new RuntimeException("failed to read mod manifest for " + directory, (Throwable)err2);
        }
        this.directory = directory;
        this.context = ctx;
        this.executionDirectory = ctx.executionDirectory;
        this.resourceManager = ctx.resourceManager;
        this.devInterface = new DeveloperInterface();
        this.configInterface = new ConfigurationInterface();
        this.safetyInterface = new SafetyInterface();
        for (final ModManifest.Directory subDirectory : this.manifest.getDirectories()) {
            switch (subDirectory.type) {
                case LIBRARY: {
                    this.libraries.add(new LibraryDirectory(this, subDirectory.file));
                    continue;
                }
                case JAVA: {
                    this.java.add(new JavaDirectory(this, subDirectory.file));
                    continue;
                }
                case RESOURCE: {
                    this.resources.add(new ResourceDirectory(this.resourceManager, this, subDirectory.file));
                    continue;
                }
                case SUBMOD: {
                    this.subModLocations.add(subDirectory.asModLocation());
                    continue;
                }
            }
        }
    }
    
    public void inject() {
        for (final LibraryDirectory library : this.libraries) {
            this.executionDirectory.addLibraryDirectory(library);
        }
        for (final JavaDirectory library2 : this.java) {
            this.executionDirectory.addJavaDirectory(library2);
        }
        for (final ResourceDirectory resource : this.resources) {
            this.resourceManager.addResourceDirectory(resource);
        }
    }
    
    public void initialize() {
        this.modules.clear();
        this.modInstances.clear();
        for (final LibraryDirectory libDirectory : this.libraries) {
            final Library library = libDirectory.getLibrary();
            if (library != null) {
                for (final Module module : library.getModules()) {
                    this.modules.add(module);
                    if (module.isMod()) {
                        this.modInstances.add(new ModInstance(module));
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "[Mod name=" + this.getDisplayedName() + " version=" + this.manifest.getMainModule().versionName + " dir=" + this.directory + "]";
    }
    
    public String getDisplayedName() {
        return this.manifest.getName();
    }
    
    public ConfigurationInterface getConfigurationInterface() {
        return this.configInterface;
    }
    
    public DeveloperInterface getDeveloperInterface() {
        return this.devInterface;
    }
    
    public SafetyInterface getSafetyInterface() {
        return this.safetyInterface;
    }
    
    public ModGraphics getGraphics() {
        if (this.graphics == null) {
            this.graphics = new ModGraphics(new File(this.directory, "visual"));
        }
        return this.graphics;
    }
    
    public class ConfigurationInterface
    {
        public Configuration configuration;
        
        public ConfigurationInterface() {
            (this.configuration = new ConfigurationFile(new File(Mod.this.directory, "config.json"), false)).refresh();
            this.configuration.checkAndRestore("\n            {\n                \"enabled\": true\n            }");
            this.configuration.save();
        }
        
        public boolean isActive() {
            return this.configuration.getBoolean("enabled");
        }
        
        public void setActive(final boolean active) {
            this.configuration.set("enabled", active);
            this.configuration.save();
        }
    }
    
    public class DeveloperInterface
    {
        public void toProductionMode(final EventLogger logger) {
            for (final LibraryDirectory library : Mod.this.libraries) {
                try {
                    library.compileToTargetFile(Mod.this.executionDirectory, Mod.this.context.getActivityContext(), library.soFile);
                    library.setPreCompiled(false);
                }
                catch (Throwable err) {
                    if (logger == null) {
                        throw err;
                    }
                    logger.fault("MOD", "failed to compile native library for production mode: " + library.getName(), err);
                }
            }
            for (final JavaDirectory javaDir : Mod.this.java) {
                try {
                    javaDir.compileToClassesFile(Mod.this.context.getActivityContext());
                    javaDir.setPreCompiled(false);
                }
                catch (Throwable err) {
                    if (logger == null) {
                        throw err;
                    }
                    logger.fault("MOD", "failed to compile java library for production mode: " + javaDir.getName(), err);
                }
            }
        }
        
        public void toDeveloperMode() {
            for (final LibraryDirectory library : Mod.this.libraries) {
                if (!library.isPreCompiled() && library.soFile.exists()) {
                    library.soFile.delete();
                }
            }
            for (final JavaDirectory javaDir : Mod.this.java) {
                if (!javaDir.isPreCompiled()) {
                    final List<File> files = javaDir.getCompiledClassesFiles();
                    for (final File classes : files) {
                        if (classes.exists()) {
                            classes.delete();
                        }
                    }
                }
            }
        }
        
        public boolean toProductModeUiProtocol() {
            this.toProductionMode(Mod.this.context.getEventLogger());
            return !CompilerErrorDialogHelper.showCompilationErrors(Mod.this.context);
        }
        
        public boolean anyForDeveloperModeTransfer() {
            for (final LibraryDirectory library : Mod.this.libraries) {
                if (!library.isPreCompiled() && library.soFile.exists()) {
                    return true;
                }
            }
            for (final JavaDirectory javaDir : Mod.this.java) {
                final File classes = javaDir.getCompiledClassesFile();
                if (!javaDir.isPreCompiled() && classes.exists()) {
                    return true;
                }
            }
            return false;
        }
        
        public boolean anyForProductionModeTransfer() {
            for (final LibraryDirectory library : Mod.this.libraries) {
                if (!library.soFile.exists()) {
                    return true;
                }
            }
            for (final JavaDirectory javaDir : Mod.this.java) {
                final File classes = javaDir.getCompiledClassesFile();
                if (!classes.exists()) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public class SafetyInterface
    {
        public static final String CRASH_LOCK = ".crash-lock";
        public static final String CRASH_DISABLED_LOCK = ".crash-disabled-lock";
        private boolean isInUnsafeSection;
        
        public SafetyInterface() {
            this.isInUnsafeSection = false;
        }
        
        public boolean getLock(final String name) {
            return new File(Mod.this.directory, name).exists();
        }
        
        public boolean setLock(final String name, final boolean exists) {
            final File lock = new File(Mod.this.directory, name);
            if (exists && !lock.exists()) {
                try {
                    return lock.createNewFile();
                }
                catch (IOException e) {
                    return false;
                }
            }
            return !lock.exists() || lock.delete();
        }
        
        public boolean beginUnsafeSection() {
            if (this.isInUnsafeSection) {
                return true;
            }
            if (this.getLock(".crash-lock")) {
                return false;
            }
            this.isInUnsafeSection = true;
            return this.setLock(".crash-lock", true);
        }
        
        public boolean endUnsafeSection() {
            if (!this.isInUnsafeSection) {
                return true;
            }
            if (!this.getLock(".crash-lock")) {
                return false;
            }
            this.isInUnsafeSection = false;
            return this.setLock(".crash-lock", false);
        }
        
        public boolean isInUnsafeSection() {
            return this.isInUnsafeSection;
        }
        
        public boolean isCrashRegistered() {
            return !this.isInUnsafeSection && this.getLock(".crash-lock");
        }
        
        public boolean removeCrashLock() {
            return this.isCrashRegistered() && this.setLock(".crash-lock", false);
        }
        
        public boolean isDisabledDueToCrash() {
            return this.getLock(".crash-disabled-lock");
        }
        
        public boolean setDisabledDueToCrash(final boolean disabled) {
            return this.setLock(".crash-disabled-lock", disabled);
        }
    }
}
