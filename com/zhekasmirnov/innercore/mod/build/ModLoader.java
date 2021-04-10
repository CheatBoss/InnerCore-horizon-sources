package com.zhekasmirnov.innercore.mod.build;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.horizon.activity.util.*;
import java.io.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.mcpe161.*;
import java.util.*;
import org.json.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import com.zhekasmirnov.apparatus.modloader.*;

@SynthesizedClassMap({ -$$Lambda$ModLoader$lplr3acSrIo8RFRCRoW0bW4p9Lg.class, -$$Lambda$ModLoader$ubVxldxDVN9CfvLesKW_4qOJ--U.class })
public class ModLoader
{
    public static final String LOGGER_TAG = "INNERCORE-MOD";
    private static final ModLoaderReporter defaultLogReporter;
    public static ModLoader instance;
    private List<File> behaviorPackDirs;
    private List<MinecraftPack> minecraftPacks;
    public ArrayList<Mod> modsList;
    private List<File> resourcePackDirs;
    private final HashMap<String, File> runtimePackDirs;
    
    static {
        defaultLogReporter = -$$Lambda$ModLoader$lplr3acSrIo8RFRCRoW0bW4p9Lg.INSTANCE;
    }
    
    public ModLoader() {
        this.modsList = new ArrayList<Mod>();
        this.resourcePackDirs = new ArrayList<File>();
        this.behaviorPackDirs = new ArrayList<File>();
        this.minecraftPacks = new ArrayList<MinecraftPack>();
        this.runtimePackDirs = new HashMap<String, File>();
    }
    
    public static void addGlobalMinecraftPacks() {
        new File(FileTools.DIR_WORK, "resource_packs").mkdirs();
        new File(FileTools.DIR_WORK, "behavior_packs").mkdirs();
        final DirectorySetRequestHandler requestHandler = ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.RESOURCE_PACKS);
        final Iterator<String> iterator = requestHandler.getAllLocations().iterator();
        while (iterator.hasNext()) {
            final File value = requestHandler.get(iterator.next());
            if (value.isDirectory()) {
                addMinecraftResourcePack(value);
            }
        }
        final DirectorySetRequestHandler requestHandler2 = ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.BEHAVIOR_PACKS);
        final Iterator<String> iterator2 = requestHandler2.getAllLocations().iterator();
        while (iterator2.hasNext()) {
            final File value2 = requestHandler2.get(iterator2.next());
            if (value2.isDirectory()) {
                addMinecraftBehaviorPack(value2);
            }
        }
    }
    
    public static void addMinecraftBehaviorPack(final File file) {
        if (!ModLoader.instance.behaviorPackDirs.contains(file)) {
            ModLoader.instance.behaviorPackDirs.add(file);
            final StringBuilder sb = new StringBuilder();
            sb.append("added minecraft pack: ");
            sb.append(file);
            ICLog.d("ModLoader", sb.toString());
        }
    }
    
    public static void addMinecraftResourcePack(final File file) {
        if (!ModLoader.instance.resourcePackDirs.contains(file)) {
            ModLoader.instance.resourcePackDirs.add(file);
            final StringBuilder sb = new StringBuilder();
            sb.append("added minecraft pack: ");
            sb.append(file);
            ICLog.d("ModLoader", sb.toString());
        }
    }
    
    private void deleteDuplicatePacksInDir(final File file, final MinecraftPackType minecraftPackType) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                final MinecraftPack fromDirectory = MinecraftPack.fromDirectory(file2, minecraftPackType);
                if (fromDirectory != null) {
                    for (final MinecraftPack minecraftPack : this.minecraftPacks) {
                        if (minecraftPack.type == minecraftPackType && minecraftPack.uuid.equals(fromDirectory.uuid)) {
                            FileUtils.clearFileTree(file2, true);
                        }
                    }
                }
            }
        }
    }
    
    private void deleteTempPacksInDirectory(final File file) {
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                if (new File(file2, ".tmp_resources").exists()) {
                    FileUtils.clearFileTree(file2, true);
                }
            }
        }
    }
    
    private List<File> findPackInDirectory(final File file, final MinecraftPack minecraftPack) {
        final ArrayList<File> list = new ArrayList<File>();
        if (file.isDirectory()) {
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                final MinecraftPack fromDirectory = MinecraftPack.fromDirectory(file2, minecraftPack.type);
                if (fromDirectory != null && fromDirectory.uuid != null && fromDirectory.uuid.equals(minecraftPack.uuid)) {
                    list.add(file2);
                }
            }
        }
        return list;
    }
    
    public static void initialize() {
        ModLoader.instance = new ModLoader();
    }
    
    private void injectPacksInDirectory(final File file, final MinecraftPackType minecraftPackType) {
        this.deleteTempPacksInDirectory(file);
        try {
            for (final MinecraftPack minecraftPack : this.minecraftPacks) {
                if (minecraftPack.type == minecraftPackType) {
                    final Iterator<File> iterator2 = this.findPackInDirectory(file, minecraftPack).iterator();
                    while (iterator2.hasNext()) {
                        FileUtils.clearFileTree((File)iterator2.next(), true);
                    }
                    File file2;
                    StringBuilder sb;
                    for (file2 = new File(file, minecraftPack.directory.getName()); file2.exists(); file2 = new File(file, sb.toString())) {
                        sb = new StringBuilder();
                        sb.append(file2.getName());
                        sb.append("-");
                    }
                    file2.mkdirs();
                    try {
                        FileUtils.copyFileTree(minecraftPack.directory, file2, (DialogHelper$ProgressDialogHolder)null, (String)null);
                        new File(file2, ".tmp_resources").createNewFile();
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    private void loadMinecraftPacksIntoDirectory(final MinecraftPackType minecraftPackType, final File file, final List<File> list) {
        if (file.isDirectory()) {
            FileUtils.clearFileTree(file, false);
        }
        else {
            file.delete();
            file.mkdirs();
        }
        for (final File file2 : list) {
            File file3;
            StringBuilder sb;
            for (file3 = new File(file, file2.getName()); file3.exists(); file3 = new File(file, sb.toString())) {
                sb = new StringBuilder();
                sb.append(file3.getName());
                sb.append("-");
            }
            file3.mkdirs();
            try {
                FileUtils.copyFileTree(file2, file3, (DialogHelper$ProgressDialogHolder)null, (String)null);
                final MinecraftPack fromDirectory = MinecraftPack.fromDirectory(file3, minecraftPackType);
                if (fromDirectory == null) {
                    continue;
                }
                this.minecraftPacks.add(fromDirectory);
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public static void loadModsAndSetupEnvViaNewModLoader() {
        final ArrayList<-$$Lambda$ModLoader$ubVxldxDVN9CfvLesKW_4qOJ--U> list = new ArrayList<-$$Lambda$ModLoader$ubVxldxDVN9CfvLesKW_4qOJ--U>();
        list.add(-$$Lambda$ModLoader$ubVxldxDVN9CfvLesKW_4qOJ--U.INSTANCE);
        ApparatusModLoader.getSingleton().reloadModsAndSetupEnvironment((List<ApparatusModLoader.AbstractModSource>)list, InnerCore.getEnvironmentSetupProxy(), ModLoader.defaultLogReporter);
    }
    
    public static void prepareResourcesViaNewModLoader() {
        ApparatusModLoader.getSingleton().prepareModResources(ModLoader.defaultLogReporter);
    }
    
    public static void runModsViaNewModLoader() {
        ApparatusModLoader.getSingleton().runMods(ModLoader.defaultLogReporter);
    }
    
    public void addResourceAndBehaviorPacksInWorld(final File file) {
        final File file2 = new File(file, "world_resource_packs.json");
        final File file3 = new File(file, "resource_packs");
        final File file4 = new File(file, "world_behavior_packs.json");
        final File file5 = new File(file, "behavior_packs");
        final JSONArray jsonArray = new JSONArray();
        final JSONArray jsonArray2 = new JSONArray();
        this.injectPacksInDirectory(file3, MinecraftPackType.RESOURCE);
        this.injectPacksInDirectory(file5, MinecraftPackType.BEHAVIOR);
        final ArrayList<MinecraftPack> list = new ArrayList<MinecraftPack>(this.minecraftPacks);
        final File[] listFiles = file3.listFiles();
        final int n = 0;
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final MinecraftPack fromDirectory = MinecraftPack.fromDirectory(listFiles[i], MinecraftPackType.RESOURCE);
                if (fromDirectory != null) {
                    list.add(fromDirectory);
                }
            }
        }
        final File[] listFiles2 = file5.listFiles();
        if (listFiles2 != null) {
            for (int length2 = listFiles2.length, j = n; j < length2; ++j) {
                final MinecraftPack fromDirectory2 = MinecraftPack.fromDirectory(listFiles2[j], MinecraftPackType.BEHAVIOR);
                if (fromDirectory2 != null) {
                    list.add(fromDirectory2);
                }
            }
        }
        for (final MinecraftPack minecraftPack : list) {
            switch (minecraftPack.type) {
                default: {
                    continue;
                }
                case BEHAVIOR: {
                    jsonArray2.put((Object)minecraftPack.getJsonForWorldPacks());
                    continue;
                }
                case RESOURCE: {
                    jsonArray.put((Object)minecraftPack.getJsonForWorldPacks());
                    continue;
                }
            }
        }
        try {
            FileUtils.writeJSON(file2, jsonArray);
            FileUtils.writeJSON(file4, jsonArray2);
        }
        catch (IOException ex) {
            ex.printStackTrace();
            ICLog.e("ERROR", "failed to write world packs json", ex);
        }
    }
    
    public File addRuntimePack(final MinecraftPackType minecraftPackType, final String ex) {
        if (this.runtimePackDirs.containsKey(ex)) {
            return this.runtimePackDirs.get(ex);
        }
        File file = null;
        Object o = null;
        final String string = UUID.randomUUID().toString();
        final String string2 = UUID.randomUUID().toString();
        final StringBuilder sb = new StringBuilder();
        sb.append("This pack is generated by Inner Core mod, pack name ID is ");
        sb.append((String)ex);
        final String string3 = sb.toString();
        switch (minecraftPackType) {
            case BEHAVIOR: {
                file = new File(FileTools.DIR_ROOT, "games/horizon/behavior_packs");
                o = "data";
                break;
            }
            case RESOURCE: {
                file = new File(FileTools.DIR_ROOT, "games/horizon/resource_packs");
                o = "resources";
                break;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("runtime_");
        sb2.append((String)ex);
        final File file2 = new File(file, sb2.toString());
        file2.mkdirs();
        final JSONArray put = new JSONArray().put(0).put(0).put(1);
        try {
            final JSONObject jsonObject = new JSONObject();
            final JSONObject put2 = jsonObject.put("format_version", 1);
            final JSONObject put3 = new JSONObject().put("uuid", (Object)string);
            final StringBuilder sb3 = new StringBuilder();
            try {
                sb3.append("runtime pack: ");
                sb3.append((String)ex);
                final JSONObject put4 = put2.put("header", (Object)put3.put("name", (Object)sb3.toString()).put("version", (Object)put).put("description", (Object)string3));
                final JSONArray jsonArray = new JSONArray();
                final JSONObject put5 = new JSONObject().put("uuid", (Object)string2).put("type", o).put("version", (Object)put);
                final StringBuilder sb4 = new StringBuilder();
                try {
                    sb4.append("module ");
                    sb4.append((String)ex);
                    put4.put("modules", (Object)jsonArray.put((Object)put5.put("description", (Object)sb4.toString())));
                    FileTools.writeJSON(new File(file2, "manifest.json").getAbsolutePath(), jsonObject);
                }
                catch (IOException ex) {}
                catch (JSONException ex2) {}
            }
            catch (IOException ex) {}
            catch (JSONException ex) {}
        }
        catch (IOException ex3) {}
        catch (JSONException ex4) {}
        this.minecraftPacks.add(new MinecraftPack(file2, minecraftPackType, string, put));
        return file2;
    }
    
    public void loadMods() {
        this.modsList.clear();
        final ModPack currentModPack = ModPackContext.getInstance().getCurrentModPack();
        final DirectorySetRequestHandler requestHandler = currentModPack.getRequestHandler(ModPackDirectory.DirectoryType.MODS);
        final List<String> allLocations = requestHandler.getAllLocations();
        final StringBuilder sb = new StringBuilder();
        sb.append("found ");
        sb.append(allLocations.size());
        sb.append(" potential mod dirs.");
        Logger.debug("INNERCORE-MOD", sb.toString());
        int n = 1;
        for (final String s : allLocations) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("investigating mod location: ");
            sb2.append(s);
            Logger.debug("INNERCORE-MOD", sb2.toString());
            final File value = requestHandler.get(s);
            int n2 = n;
            if (value.isDirectory()) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Loading Mods: ");
                sb3.append(n);
                sb3.append("/");
                sb3.append(allLocations.size());
                LoadingUI.setTextAndProgressBar(sb3.toString(), n * 0.25f / allLocations.size() + 0.15f);
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(value.getAbsolutePath());
                sb4.append("/");
                final String string = sb4.toString();
                if (ModBuilder.analyzeAndSetupModDir(string)) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("building and importing mod: ");
                    sb5.append(value.getName());
                    Logger.debug("INNERCORE-MOD", sb5.toString());
                    final Mod buildModForDir = ModBuilder.buildModForDir(string, currentModPack, s);
                    if (buildModForDir != null) {
                        this.modsList.add(buildModForDir);
                        buildModForDir.onImport();
                    }
                    else {
                        Logger.debug("INNERCORE-MOD", "failed to build mod: build.config file could not be parsed.");
                    }
                }
                n2 = n + 1;
            }
            n = n2;
        }
    }
    
    public void loadResourceAndBehaviorPacks() {
        this.minecraftPacks.clear();
        this.loadMinecraftPacksIntoDirectory(MinecraftPackType.RESOURCE, new File(FileTools.DIR_ROOT, "games/horizon/resource_packs"), this.resourcePackDirs);
        this.loadMinecraftPacksIntoDirectory(MinecraftPackType.BEHAVIOR, new File(FileTools.DIR_ROOT, "games/horizon/behavior_packs"), this.behaviorPackDirs);
        Callback.invokeAPICallback("AddRuntimePacks", new Object[0]);
    }
    
    public void runPreloaderScripts() {
        LoadingStage.setStage(3);
        final StringBuilder sb = new StringBuilder();
        sb.append("imported ");
        sb.append(this.modsList.size());
        sb.append(" mods.");
        Logger.debug("INNERCORE-MOD", sb.toString());
        for (int i = 0; i < this.modsList.size(); ++i) {
            final Mod mod = this.modsList.get(i);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Initializing Mods: ");
            sb2.append(i + 1);
            sb2.append("/");
            sb2.append(this.modsList.size());
            sb2.append(": ");
            sb2.append(mod.getName());
            LoadingUI.setText(sb2.toString());
            mod.RunPreloaderScripts();
        }
    }
    
    public void startMods() {
        for (int i = 0; i < this.modsList.size(); ++i) {
            final Mod mod = this.modsList.get(i);
            final StringBuilder sb = new StringBuilder();
            sb.append("Running Mods: ");
            sb.append(i + 1);
            sb.append("/");
            sb.append(this.modsList.size());
            sb.append(" ");
            LoadingUI.setTextAndProgressBar(sb.toString(), i * 0.3f / this.modsList.size() + 0.7f);
            mod.RunLauncherScripts();
        }
        for (final Mod mod2 : this.modsList) {
            if (mod2.isEnabled) {
                MultiplayerModList.getSingleton().add(new LegacyInnerCoreMod(mod2));
            }
        }
    }
    
    private static class MinecraftPack
    {
        private final File directory;
        private final MinecraftPackType type;
        private final String uuid;
        private final JSONArray version;
        
        public MinecraftPack(final File directory, final MinecraftPackType type, final String uuid, final JSONArray version) {
            this.directory = directory;
            this.type = type;
            this.uuid = uuid;
            this.version = version;
        }
        
        public static MinecraftPack fromDirectory(final File file, final MinecraftPackType minecraftPackType) {
            final File file2 = new File(file, "manifest.json");
            if (file2.isFile()) {
                try {
                    final JSONObject optJSONObject = FileUtils.readJSON(file2).optJSONObject("header");
                    if (optJSONObject != null) {
                        final String optString = optJSONObject.optString("uuid", (String)null);
                        final JSONArray optJSONArray = optJSONObject.optJSONArray("version");
                        if (optString != null && optJSONArray != null) {
                            return new MinecraftPack(file, minecraftPackType, optString, optJSONArray);
                        }
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("failed to read minecraft pack uuid or version: ");
            sb.append(file);
            ICLog.i("ERROR", sb.toString());
            return null;
        }
        
        public JSONObject getJsonForWorldPacks() {
            final JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("version", (Object)this.version);
                jsonObject.put("pack_id", (Object)this.uuid);
                return jsonObject;
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                return jsonObject;
            }
        }
    }
    
    public enum MinecraftPackType
    {
        BEHAVIOR, 
        RESOURCE;
        
        public static MinecraftPackType fromString(final String s) {
            final int hashCode = s.hashCode();
            int n = 0;
            Label_0070: {
                if (hashCode != -406349635) {
                    if (hashCode != -341064690) {
                        if (hashCode == 1510912594) {
                            if (s.equals("behavior")) {
                                n = 1;
                                break Label_0070;
                            }
                        }
                    }
                    else if (s.equals("resource")) {
                        n = 0;
                        break Label_0070;
                    }
                }
                else if (s.equals("behaviour")) {
                    n = 2;
                    break Label_0070;
                }
                n = -1;
            }
            switch (n) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invalid minecraft pack type: ");
                    sb.append(s);
                    throw new IllegalArgumentException(sb.toString());
                }
                case 1:
                case 2: {
                    return MinecraftPackType.BEHAVIOR;
                }
                case 0: {
                    return MinecraftPackType.RESOURCE;
                }
            }
        }
    }
}
