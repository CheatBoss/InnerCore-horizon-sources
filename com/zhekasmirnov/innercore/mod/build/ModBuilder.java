package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.innercore.mod.build.enums.*;
import java.util.*;
import java.io.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import org.mozilla.javascript.annotations.*;

public class ModBuilder
{
    public static final String LOGGER_TAG = "INNERCORE-MOD-BUILD";
    
    public static void addGuiDir(String string, final BuildConfig.ResourceDir resourceDir) {
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(resourceDir.path);
        string = sb.toString();
        if (!FileTools.exists(string)) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to import resource or ui dir ");
            sb2.append(resourceDir.path);
            sb2.append(": it does not exist");
            ICLog.d("INNERCORE-MOD-BUILD", sb2.toString());
            return;
        }
        TextureSource.instance.loadDirectory(new File(string));
    }
    
    public static boolean analyzeAndSetupModDir(final String s) {
        switch (analyzeModDir(checkRedirect(s))) {
            case MODPE_MOD_ARRAY: {
                return false;
            }
            case UNKNOWN: {
                return false;
            }
        }
        return true;
    }
    
    public static AnalyzedModType analyzeModDir(String checkRedirect) {
        checkRedirect = checkRedirect(checkRedirect);
        final StringBuilder sb = new StringBuilder();
        sb.append(checkRedirect);
        sb.append("/build.config");
        if (FileTools.exists(sb.toString())) {
            return AnalyzedModType.INNER_CORE_MOD;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(checkRedirect);
        sb2.append("/main.js");
        if (FileTools.exists(sb2.toString())) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(checkRedirect);
            sb3.append("/launcher.js");
            if (FileTools.exists(sb3.toString())) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(checkRedirect);
                sb4.append("/config.json");
                if (FileTools.exists(sb4.toString())) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append(checkRedirect);
                    sb5.append("/resources.json");
                    FileTools.exists(sb5.toString());
                }
            }
        }
        return AnalyzedModType.UNKNOWN;
    }
    
    public static Mod buildModForDir(String o, ModPack allSourcesToCompile, String debugInfo) {
        o = checkRedirect((String)o);
        if (!FileTools.exists((String)o)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to load mod, dir does not exist, maybe redirect file is pointing to the missing dir ");
            sb.append((String)o);
            ICLog.d("INNERCORE-MOD-BUILD", sb.toString());
            return null;
        }
        final Mod mod = new Mod((String)o);
        mod.setModPackAndLocation(allSourcesToCompile, debugInfo);
        mod.buildConfig = loadBuildConfigForDir((String)o);
        if (!mod.buildConfig.isValid()) {
            return null;
        }
        mod.buildConfig.save();
        debugInfo = (String)mod.getDebugInfo();
        if (mod.buildConfig.getBuildType() == BuildType.DEVELOP) {
            final ArrayList<BuildConfig.BuildableDir> buildableDirs = mod.buildConfig.buildableDirs;
            for (int i = 0; i < buildableDirs.size(); ++i) {
                BuildHelper.buildDir((String)o, buildableDirs.get(i));
            }
        }
        final ArrayList<BuildConfig.ResourceDir> resourceDirs = mod.buildConfig.resourceDirs;
        for (int j = 0; j < resourceDirs.size(); ++j) {
            final BuildConfig.ResourceDir resourceDir = resourceDirs.get(j);
            if (resourceDir.resourceType == ResourceDirType.GUI) {
                addGuiDir((String)o, resourceDir);
            }
        }
        if (mod.getConfig().getBool("enabled")) {
            final String resourcePacksDir = mod.buildConfig.defaultConfig.resourcePacksDir;
            if (resourcePacksDir != null) {
                final File file = new File((String)o, resourcePacksDir);
                if (file.isDirectory()) {
                    final File[] listFiles = file.listFiles();
                    for (int length = listFiles.length, k = 0; k < length; ++k) {
                        final File file2 = listFiles[k];
                        if (file2.isDirectory() && new File(file2, "manifest.json").isFile()) {
                            ModLoader.addMinecraftResourcePack(file2);
                        }
                    }
                }
            }
            final String behaviorPacksDir = mod.buildConfig.defaultConfig.behaviorPacksDir;
            if (behaviorPacksDir != null) {
                final File file3 = new File((String)o, behaviorPacksDir);
                if (file3.isDirectory()) {
                    final File[] listFiles2 = file3.listFiles();
                    for (int length2 = listFiles2.length, l = 0; l < length2; ++l) {
                        final File file4 = listFiles2[l];
                        if (file4.isDirectory() && new File(file4, "manifest.json").isFile()) {
                            ModLoader.addMinecraftBehaviorPack(file4);
                        }
                    }
                }
            }
        }
        o = mod.createCompiledSources();
        allSourcesToCompile = (ModPack)mod.buildConfig.getAllSourcesToCompile(true);
        for (int n = 0; n < ((ArrayList)allSourcesToCompile).size(); ++n) {
            final BuildConfig.Source source = ((ArrayList<BuildConfig.Source>)allSourcesToCompile).get(n);
            if (source.gameVersion.isCompatible()) {
                if (source.apiInstance == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("could not find api for ");
                    sb2.append(source.path);
                    sb2.append(", maybe it is missing in build.config or name is incorrect, compilation failed.");
                    final String string = sb2.toString();
                    ICLog.d("INNERCORE-MOD-BUILD", string);
                    ((ModDebugInfo)debugInfo).putStatus(source.path, new IllegalArgumentException(string));
                }
                else {
                    while (true) {
                        while (true) {
                            Label_0773: {
                                try {
                                    final Executable compileOrLoadExecutable = compileOrLoadExecutable(mod, (CompiledSources)o, source);
                                    switch (source.sourceType) {
                                        case CUSTOM: {
                                            mod.compiledCustomSources.put(source.path, compileOrLoadExecutable);
                                            break;
                                        }
                                        case MOD: {
                                            mod.compiledModSources.add(compileOrLoadExecutable);
                                            break;
                                        }
                                        case LIBRARY: {
                                            mod.compiledLibs.add(compileOrLoadExecutable);
                                            break;
                                        }
                                        case LAUNCHER: {
                                            mod.compiledLauncherScripts.add(compileOrLoadExecutable);
                                            setupLauncherScript(compileOrLoadExecutable, mod);
                                            break;
                                        }
                                        case PRELOADER: {
                                            mod.compiledPreloaderScripts.add(compileOrLoadExecutable);
                                            break;
                                        }
                                        default: {
                                            break Label_0773;
                                        }
                                    }
                                    mod.onImportExecutable(compileOrLoadExecutable);
                                    ((ModDebugInfo)debugInfo).putStatus(source.path, compileOrLoadExecutable);
                                }
                                catch (Exception ex) {
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("failed to compile source ");
                                    sb3.append(source.path);
                                    sb3.append(":");
                                    ICLog.e("INNERCORE-MOD-BUILD", sb3.toString(), ex);
                                    ((ModDebugInfo)debugInfo).putStatus(source.path, ex);
                                }
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
        }
        return mod;
    }
    
    public static String checkRedirect(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".redirect");
        final File file = new File(sb.toString());
        if (file.exists()) {
            try {
                return FileTools.readFileText(file.getAbsolutePath()).trim();
            }
            catch (IOException ex) {}
        }
        return s;
    }
    
    private static Executable compileOrLoadExecutable(final Mod mod, final CompiledSources compiledSources, final BuildConfig.Source source) throws IOException {
        final CompilerConfig compilerConfig = source.getCompilerConfig();
        compilerConfig.setModName(mod.getName());
        if (mod.getBuildType() == BuildType.RELEASE) {
            final Executable compiledExecutable = compiledSources.getCompiledExecutableFor(source.path, compilerConfig);
            if (compiledExecutable != null) {
                return compiledExecutable;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("no multidex executable created for ");
            sb.append(source.path);
            ICLog.d("INNERCORE-MOD-BUILD", sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(mod.dir);
        sb2.append(source.path);
        final FileReader fileReader = new FileReader(new File(sb2.toString()));
        compilerConfig.setOptimizationLevel(-1);
        return Compiler.compileReader(fileReader, compilerConfig);
    }
    
    public static BuildConfig loadBuildConfigForDir(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append("/build.config");
        final BuildConfig buildConfig = new BuildConfig(new File(sb.toString()));
        buildConfig.read();
        return buildConfig;
    }
    
    private static void setupLauncherScript(final Executable executable, final Mod mod) {
        final LauncherScope launcherScope = new LauncherScope(mod);
        launcherScope.defineFunctionProperties(new String[] { "Launch", "ConfigureMultiplayer" }, (Class)launcherScope.getClass(), 2);
        executable.addToScope(launcherScope);
    }
    
    private static class LauncherScope extends ScriptableObject
    {
        Mod mod;
        
        public LauncherScope(final Mod mod) {
            this.mod = mod;
        }
        
        @JSFunction
        public void ConfigureMultiplayer(final ScriptableObject scriptableObject) {
            final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper((Scriptable)scriptableObject);
            this.mod.configureMultiplayer(scriptableObjectWrapper.getString("name"), scriptableObjectWrapper.getString("version"), scriptableObjectWrapper.getBoolean("isClientOnly") || scriptableObjectWrapper.getBoolean("isClientSide"));
        }
        
        @JSFunction
        public void Launch(final ScriptableObject scriptableObject) {
            this.mod.RunMod(scriptableObject);
        }
        
        public String getClassName() {
            return "LauncherAPI";
        }
    }
}
