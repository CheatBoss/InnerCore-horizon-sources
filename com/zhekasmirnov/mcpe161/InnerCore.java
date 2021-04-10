package com.zhekasmirnov.mcpe161;

import com.zhekasmirnov.apparatus.adapter.env.*;
import java.lang.ref.*;
import android.app.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import com.zhekasmirnov.apparatus.modloader.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.innercore.modpack.*;
import org.json.*;
import java.util.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import android.content.pm.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class InnerCore
{
    public static final String LOGGER_TAG = "INNERCORE-LAUNHER";
    private static final EnvironmentSetupProxy environmentSetupProxy;
    private static InnerCore instance;
    public static final boolean isLicenceVersion = true;
    private static final List<File> javaDirectoriesFromProxy;
    private static final List<File> nativeDirectoriesFromProxy;
    private static final List<File> resourceDirectoriesFromProxy;
    public List<File> allResourceDirectories;
    private WeakReference<Activity> currentActivity;
    private final Pack pack;
    
    static {
        javaDirectoriesFromProxy = new ArrayList<File>();
        nativeDirectoriesFromProxy = new ArrayList<File>();
        resourceDirectoriesFromProxy = new ArrayList<File>();
        environmentSetupProxy = new EnvironmentSetupProxy() {
            @Override
            public void addBehaviorPackDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    ModLoader.addMinecraftBehaviorPack(file);
                }
            }
            
            @Override
            public void addGuiAssetsDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    TextureSource.instance.loadDirectory(file);
                }
            }
            
            @Override
            public void addJavaDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    InnerCore.javaDirectoriesFromProxy.add(file);
                }
            }
            
            @Override
            public void addNativeDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    InnerCore.nativeDirectoriesFromProxy.add(file);
                }
            }
            
            @Override
            public void addResourceDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    InnerCore.resourceDirectoriesFromProxy.add(file);
                }
            }
            
            @Override
            public void addResourcePackDirectory(final ApparatusMod apparatusMod, final File file) {
                if (file.isDirectory()) {
                    ModLoader.addMinecraftResourcePack(file);
                }
            }
        };
    }
    
    public InnerCore(final Activity activity, final Pack pack) {
        this.allResourceDirectories = new ArrayList<File>();
        FileTools.initializeDirectories(pack.directory);
        InnerCore.instance = this;
        this.pack = pack;
        this.currentActivity = new WeakReference<Activity>(activity);
        Logger.info("initializing innercore");
    }
    
    private void addAllModResources() {
        this.allResourceDirectories.add(new File(this.pack.getWorkingDirectory(), "assets/textures/"));
        this.allResourceDirectories.addAll(InnerCore.resourceDirectoriesFromProxy);
    }
    
    private void addAllResourcePacks() {
        final File file = new File(this.pack.getWorkingDirectory(), "resourcepacks");
        if (!file.isDirectory()) {
            file.delete();
        }
        if (!file.isDirectory()) {
            file.mkdirs();
        }
        final DirectorySetRequestHandler requestHandler = ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.TEXTURE_PACKS);
        final ArrayList<String> list = new ArrayList<String>();
        final ArrayList<String> list2 = new ArrayList<String>();
        JSONObject json = new JSONObject();
        try {
            json = FileUtils.readJSON(requestHandler.get("", "resourcepacks.json"));
        }
        catch (JSONException ex) {}
        catch (IOException ex2) {}
        final JSONArray optJSONArray = json.optJSONArray("packs");
        if (optJSONArray != null) {
            for (final JSONObject jsonObject : new JsonIterator(optJSONArray)) {
                if (jsonObject != null) {
                    final String optString = jsonObject.optString("name", (String)null);
                    if (optString == null || optString.length() <= 0 || list.contains(optString)) {
                        continue;
                    }
                    if (jsonObject.optBoolean("enabled", true)) {
                        list2.add(optString);
                    }
                    list.add(optString);
                }
            }
        }
        for (final String s : requestHandler.getAllLocations()) {
            if (!list.contains(s)) {
                list2.add(s);
                list.add(s);
            }
        }
        final Iterator<Object> iterator3 = list2.iterator();
        while (iterator3.hasNext()) {
            final File value = requestHandler.get(iterator3.next());
            if (value.isDirectory()) {
                this.allResourceDirectories.add(value);
            }
        }
    }
    
    public static boolean checkLicence(final Activity activity) {
        return isMCPEInstalled(activity);
    }
    
    public static EnvironmentSetupProxy getEnvironmentSetupProxy() {
        return InnerCore.environmentSetupProxy;
    }
    
    public static InnerCore getInstance() {
        return InnerCore.instance;
    }
    
    public static List<File> getJavaDirectoriesFromProxy() {
        return InnerCore.javaDirectoriesFromProxy;
    }
    
    private void initiateLoading() {
        Logger.debug("INNERCORE", String.format("Inner Core %s Started", PackInfo.getPackVersionName()));
        LoadingStage.setStage(1);
        this.preloadInnerCore();
    }
    
    private static boolean isMCPEInstalled(final Activity activity) {
        try {
            activity.getPackageManager().getPackageInfo("com.mojang.minecraftpe", 0);
            return true;
        }
        catch (NullPointerException ex) {
            return true;
        }
        catch (PackageManager$NameNotFoundException ex2) {
            return false;
        }
    }
    
    private void preloadInnerCore() {
        ModPackContext.getInstance().assurePackSelected();
        ICLog.setupEventHandlerForCurrentThread(new ModLoaderEventHandler());
        LoadingUI.setTextAndProgressBar("Initializing Resources...", 0.0f);
        try {
            Thread.sleep(500L);
        }
        catch (InterruptedException ex) {}
        LoadingStage.setStage(2);
        UIUtils.initialize(this.currentActivity.get());
        ResourcePackManager.instance.initializeResources();
        LoadingUI.setTextAndProgressBar("Loading Mods...", 0.15f);
        ModLoader.initialize();
        ModLoader.loadModsAndSetupEnvViaNewModLoader();
        this.addAllResourcePacks();
        this.addAllModResources();
        ModLoader.prepareResourcesViaNewModLoader();
        ModLoader.addGlobalMinecraftPacks();
        ModLoader.instance.loadResourceAndBehaviorPacks();
        LoadingUI.setTextAndProgressBar("Generating Cache...", 0.4f);
        LoadingUI.setTextAndProgressBar("Starting Minecraft...", 0.5f);
    }
    
    public void addJavaDirectories(final ArrayList<JavaDirectory> list) {
        final Iterator<File> iterator = InnerCore.javaDirectoriesFromProxy.iterator();
        while (iterator.hasNext()) {
            list.add(new JavaDirectory((Mod)null, (File)iterator.next()));
        }
    }
    
    public void addNativeDirectories(final ArrayList<LibraryDirectory> list) {
        final Iterator<File> iterator = InnerCore.nativeDirectoriesFromProxy.iterator();
        while (iterator.hasNext()) {
            list.add(new LibraryDirectory((File)iterator.next()));
        }
    }
    
    public void addResourceDirectories(final ArrayList<ResourceDirectory> list) {
        final ResourceManager resourceManager = this.getResourceManager();
        final StringBuilder sb = new StringBuilder();
        sb.append(list.size());
        sb.append(" ");
        sb.append(list.toString());
        Logger.debug("addResourceDirectories", sb.toString());
        final Iterator<File> iterator = this.allResourceDirectories.iterator();
        while (iterator.hasNext()) {
            list.add(new ResourceDirectory(resourceManager, (File)iterator.next()));
        }
    }
    
    public TextureAtlas getBlockTextureAtlas() {
        return EnvironmentSetup.getBlockTextureAtlas();
    }
    
    public Activity getCurrentActivity() {
        return this.currentActivity.get();
    }
    
    public TextureAtlas getItemTextureAtlas() {
        return EnvironmentSetup.getItemTextureAtlas();
    }
    
    public Pack getPack() {
        return this.pack;
    }
    
    public ResourceManager getResourceManager() {
        return this.pack.getModContext().getResourceManager();
    }
    
    public String getWorkingDirectory() {
        return this.pack.getWorkingDirectory().getAbsolutePath();
    }
    
    public void load() {
        ReflectionPatch.init();
        ColorsPatch.init();
        API.loadAllAPIs();
        this.initiateLoading();
    }
    
    public void onFinalLoadComplete() {
        LoadingStage.setStage(7);
        LoadingUI.close();
        ICLog.showIfErrorsAreFound();
        LoadingStage.outputTimeMap();
    }
    
    public void setMinecraftActivity(final Activity activity) {
        this.currentActivity = new WeakReference<Activity>(activity);
    }
}
