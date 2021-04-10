package com.zhekasmirnov.innercore.mod.build;

import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.modpack.*;
import java.util.*;
import com.zhekasmirnov.innercore.mod.build.enums.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import java.io.*;
import org.json.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.apparatus.minecraft.enums.*;
import com.zhekasmirnov.innercore.mod.executable.library.*;

public class Mod
{
    private static int guiIconCounter;
    public BuildConfig buildConfig;
    public HashMap<String, Executable> compiledCustomSources;
    public ArrayList<Executable> compiledLauncherScripts;
    public ArrayList<Executable> compiledLibs;
    public ArrayList<Executable> compiledModSources;
    public ArrayList<Executable> compiledPreloaderScripts;
    public Config config;
    private ModDebugInfo debugInfo;
    public String dir;
    private String guiIconName;
    private boolean isClientOnly;
    private boolean isConfiguredForMultiplayer;
    public boolean isEnabled;
    private boolean isLauncherRunning;
    public boolean isModRunning;
    private boolean isPreloaderRunning;
    private JSONObject modInfoJson;
    private ModPack modPack;
    private String modPackLocationName;
    private String multiplayerName;
    private String multiplayerVersion;
    
    static {
        Mod.guiIconCounter = 0;
    }
    
    public Mod(final String dir) {
        this.debugInfo = new ModDebugInfo();
        this.compiledLibs = new ArrayList<Executable>();
        this.compiledModSources = new ArrayList<Executable>();
        this.compiledLauncherScripts = new ArrayList<Executable>();
        this.compiledPreloaderScripts = new ArrayList<Executable>();
        this.compiledCustomSources = new HashMap<String, Executable>();
        this.isConfiguredForMultiplayer = false;
        this.isClientOnly = false;
        this.multiplayerName = null;
        this.multiplayerVersion = null;
        this.isEnabled = false;
        this.guiIconName = "missing_mod_icon";
        this.modInfoJson = new JSONObject();
        this.isPreloaderRunning = false;
        this.isLauncherRunning = false;
        this.isModRunning = false;
        this.dir = dir;
    }
    
    private void importConfigIfNeeded() {
        if (this.config == null) {
            this.config = new Config(this.modPack.getRequestHandler(ModPackDirectory.DirectoryType.CONFIG).get(this.modPackLocationName, "config.json"));
            try {
                this.config.checkAndRestore("{\"enabled\":true}");
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.isEnabled = (boolean)this.config.get("enabled");
    }
    
    public void RunLauncherScripts() {
        if (!this.isEnabled) {
            return;
        }
        if (this.isLauncherRunning) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mod ");
            sb.append(this);
            sb.append(" is already running launcher scripts.");
            throw new RuntimeException(sb.toString());
        }
        this.isLauncherRunning = true;
        for (int i = 0; i < this.compiledLauncherScripts.size(); ++i) {
            this.compiledLauncherScripts.get(i).run();
        }
    }
    
    public void RunMod(final ScriptableObject scriptableObject) {
        if (!this.isEnabled) {
            return;
        }
        if (this.isModRunning) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mod ");
            sb.append(this);
            sb.append(" is already running.");
            throw new RuntimeException(sb.toString());
        }
        ItemModelCacheManager.getSingleton().setCurrentCacheGroup(null, null);
        this.isModRunning = true;
        for (int i = 0; i < this.compiledModSources.size(); ++i) {
            final Executable executable = this.compiledModSources.get(i);
            executable.addToScope(scriptableObject);
            executable.run();
        }
        ItemModelCacheManager.getSingleton().setCurrentCacheGroup(null, null);
    }
    
    public void RunPreloaderScripts() {
        if (!this.isEnabled) {
            return;
        }
        if (this.isPreloaderRunning) {
            final StringBuilder sb = new StringBuilder();
            sb.append("mod ");
            sb.append(this);
            sb.append(" is already running preloader scripts.");
            throw new RuntimeException(sb.toString());
        }
        this.isPreloaderRunning = true;
        for (int i = 0; i < this.compiledPreloaderScripts.size(); ++i) {
            this.compiledPreloaderScripts.get(i).run();
        }
    }
    
    public void configureMultiplayer(final String multiplayerName, final String multiplayerVersion, final boolean isClientOnly) {
        this.multiplayerName = multiplayerName;
        if (this.multiplayerName == null || this.multiplayerName.equals("auto")) {
            this.multiplayerName = this.getName();
        }
        this.multiplayerVersion = multiplayerVersion;
        if (this.multiplayerVersion == null || this.multiplayerVersion.equals("auto")) {
            this.multiplayerVersion = this.getVersion();
        }
        this.isConfiguredForMultiplayer = true;
        this.isClientOnly = isClientOnly;
    }
    
    public CompiledSources createCompiledSources() {
        return new CompiledSources(new File(this.dir, ".dex"));
    }
    
    public ArrayList<Executable> getAllExecutables() {
        final ArrayList<Executable> list = new ArrayList<Executable>();
        list.addAll(this.compiledModSources);
        list.addAll(this.compiledLibs);
        list.addAll(this.compiledLauncherScripts);
        list.addAll(this.compiledPreloaderScripts);
        return list;
    }
    
    public BuildType getBuildType() {
        return this.buildConfig.getBuildType();
    }
    
    public Config getConfig() {
        this.importConfigIfNeeded();
        return this.config;
    }
    
    public ModDebugInfo getDebugInfo() {
        return this.debugInfo;
    }
    
    public String getFormattedAPIName() {
        final API apiInstance = this.buildConfig.defaultConfig.apiInstance;
        if (apiInstance != null) {
            return apiInstance.getCurrentAPIName();
        }
        return "???";
    }
    
    public String getGuiIcon() {
        return this.guiIconName;
    }
    
    public Object getInfoProperty(final String s) {
        if (this.modInfoJson != null) {
            return this.modInfoJson.opt(s);
        }
        return null;
    }
    
    public ModPack getModPack() {
        return this.modPack;
    }
    
    public String getModPackLocationName() {
        return this.modPackLocationName;
    }
    
    public String getMultiplayerName() {
        if (this.multiplayerName != null) {
            return this.multiplayerName;
        }
        return this.getName();
    }
    
    public String getMultiplayerVersion() {
        if (this.multiplayerVersion != null) {
            return this.multiplayerVersion;
        }
        return this.getVersion();
    }
    
    public String getName() {
        final Object infoProperty = this.getInfoProperty("name");
        if (infoProperty != null) {
            return infoProperty.toString();
        }
        return this.buildConfig.getName();
    }
    
    public String getVersion() {
        final Object infoProperty = this.getInfoProperty("version");
        if (infoProperty != null) {
            return infoProperty.toString();
        }
        return this.buildConfig.getName();
    }
    
    public boolean isClientOnly() {
        return this.isClientOnly;
    }
    
    public boolean isConfiguredForMultiplayer() {
        return this.isConfiguredForMultiplayer;
    }
    
    public void onImport() {
        this.isEnabled = true;
        this.importConfigIfNeeded();
        if (this.isEnabled) {
            final API defaultAPI = this.buildConfig.getDefaultAPI();
            if (defaultAPI != null) {
                defaultAPI.onModLoaded(this);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(this.dir);
        sb.append("mod_icon.png");
        if (FileTools.exists(sb.toString())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(this.dir);
            sb2.append("mod_icon.png");
            final Bitmap fileAsBitmap = FileTools.readFileAsBitmap(sb2.toString());
            if (fileAsBitmap != null) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("_modIcon");
                final int guiIconCounter = Mod.guiIconCounter;
                Mod.guiIconCounter = guiIconCounter + 1;
                sb3.append(guiIconCounter);
                this.guiIconName = sb3.toString();
                TextureSource.instance.put(this.guiIconName, fileAsBitmap);
            }
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.dir);
        sb4.append("mod.info");
        if (FileTools.exists(sb4.toString())) {
            try {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(this.dir);
                sb5.append("mod.info");
                this.modInfoJson = FileTools.readJSON(sb5.toString());
            }
            catch (IOException ex) {}
            catch (JSONException ex2) {}
        }
    }
    
    public void onImportExecutable(final Executable executable) {
        this.importConfigIfNeeded();
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        empty.put("__version__", (Scriptable)empty, (Object)MinecraftVersions.getCurrent().getCode());
        empty.put("__mod__", (Scriptable)empty, (Object)this);
        empty.put("__name__", (Scriptable)empty, (Object)this.getName());
        empty.put("__dir__", (Scriptable)empty, (Object)this.dir);
        empty.put("__config__", (Scriptable)empty, Context.javaToJS((Object)this.config, (Scriptable)executable.getScope()));
        empty.put("__debug_typecheck__", (Scriptable)empty, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                final StringBuilder sb = new StringBuilder();
                sb.append("checked object type: obj=");
                sb.append(array[0]);
                sb.append(" class=");
                sb.append(array[0].getClass());
                ICLog.d("DEBUG", sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(array[0].getClass());
                return sb2.toString();
            }
        });
        new EnumsJsInjector((Scriptable)empty, true).injectAllEnumScopes("E");
        empty.put("runCustomSource", (Scriptable)empty, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                final String s = (String)array[0];
                ScriptableObject scriptableObject = null;
                if (array.length > 1) {
                    scriptableObject = scriptableObject;
                    if (array[1] instanceof ScriptableObject) {
                        scriptableObject = (ScriptableObject)array[1];
                    }
                }
                Mod.this.runCustomSource(s, scriptableObject);
                return null;
            }
        });
        executable.addToScope(empty);
        executable.setParentMod(this);
        if (executable instanceof Library) {
            LibraryRegistry.addLibrary((Library)executable);
        }
    }
    
    public void runCustomSource(final String s, final ScriptableObject scriptableObject) {
        if (this.compiledCustomSources.containsKey(s)) {
            final Executable executable = this.compiledCustomSources.get(s);
            if (scriptableObject != null) {
                executable.addToScope(scriptableObject);
            }
            executable.reset();
            executable.run();
        }
    }
    
    public void setBuildType(final BuildType buildType) {
        this.buildConfig.defaultConfig.setBuildType(buildType);
        this.buildConfig.save();
    }
    
    public void setBuildType(final String s) {
        this.setBuildType(BuildType.fromString(s));
    }
    
    public void setModPackAndLocation(final ModPack modPack, final String modPackLocationName) {
        this.modPack = modPack;
        this.modPackLocationName = modPackLocationName;
    }
}
