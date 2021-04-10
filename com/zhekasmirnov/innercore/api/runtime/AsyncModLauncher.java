package com.zhekasmirnov.innercore.api.runtime;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import java.io.*;
import com.zhekasmirnov.apparatus.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.mod.executable.library.*;
import com.zhekasmirnov.innercore.api.mod.recipes.furnace.*;
import com.zhekasmirnov.innercore.api.mod.recipes.*;
import com.zhekasmirnov.innercore.api.mod.coreengine.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.innercore.api.mod.ui.icon.*;
import com.zhekasmirnov.mcpe161.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.ui.*;

public class AsyncModLauncher
{
    private static void invokePostLoadedCallbacks() {
        Callback.invokeAPICallback("CoreConfigured", InnerCoreConfig.config);
        Callback.invokeAPICallback("PreLoaded", new Object[0]);
        Callback.invokeAPICallback("APILoaded", new Object[0]);
        Callback.invokeAPICallback("ModsLoaded", new Object[0]);
        Callback.invokeAPICallback("PostLoaded", new Object[0]);
    }
    
    private static void loadAllMenuScripts() {
        loadMenuScript("innercore/scripts/workbench", "screen_workbench");
    }
    
    private static void loadMenuScript(final String s, final String name) {
        final CompilerConfig compilerConfig = new CompilerConfig(API.getInstanceByName("PrefsWinAPI"));
        compilerConfig.setName(name);
        compilerConfig.setOptimizationLevel(-1);
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".js");
            Compiler.compileReader(new InputStreamReader(FileTools.getAssetInputStream(sb.toString())), compilerConfig).run();
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("failed to load script ");
            sb2.append(name);
            ICLog.e("ERROR", sb2.toString(), ex);
        }
    }
    
    public void launchModsInCurrentThread() {
        Apparatus.loadClasses();
        ModPackContext.getInstance().assurePackSelected();
        LoadingUI.setTextAndProgressBar("Preparing...", 0.65f);
        PrintStacking.prepare();
        NameTranslation.refresh();
        NativeAPI.setTileUpdateAllowed(true);
        loadAllMenuScripts();
        ICLog.setupEventHandlerForCurrentThread(new ModLoaderEventHandler());
        LoadingStage.setStage(6);
        NativeAPI.setInnerCoreVersion(Version.INNER_CORE_VERSION.toString());
        VanillaIdConversionMap.getSingleton().reloadFromAssets();
        BlockRegistry.onInit();
        LibraryRegistry.loadAllBuiltInLibraries();
        LibraryRegistry.prepareAllLibraries();
        FurnaceRecipeRegistry.loadNativeRecipesIfNeeded();
        new RecipeLoader().load();
        LoadingUI.setTextAndProgressBar("Running Core Engine...", 0.4f);
        CoreEngineAPI.getOrLoadCoreEngine();
        LoadingUI.setTextAndProgressBar("Running Mods...", 0.5f);
        ModLoader.runModsViaNewModLoader();
        LoadingUI.setTextAndProgressBar("Defining Blocks...", 1.0f);
        BlockRegistry.onModsLoaded();
        LoadingUI.setTextAndProgressBar("Generating Icons...", 1.0f);
        ItemIconSource.generateAllModItemModels();
        LoadingUI.setTextAndProgressBar("Post Initialization...", 1.0f);
        invokePostLoadedCallbacks();
        InnerCore.getInstance().onFinalLoadComplete();
        ICLog.flush();
    }
    
    public void launchModsInThread() {
        final ModLoadingOverlay modLoadingOverlay = new ModLoadingOverlay(UIUtils.getContext());
        modLoadingOverlay.await(500);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setPriority(10);
                final long currentTimeMillis = System.currentTimeMillis();
                AsyncModLauncher.this.launchModsInCurrentThread();
                modLoadingOverlay.close();
                final StringBuilder sb = new StringBuilder();
                sb.append("mods launched in ");
                sb.append(System.currentTimeMillis() - currentTimeMillis);
                sb.append("ms");
                ICLog.i("LOADING", sb.toString());
            }
        }).start();
    }
}
