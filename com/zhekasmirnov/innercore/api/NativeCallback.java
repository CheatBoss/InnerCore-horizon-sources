package com.zhekasmirnov.innercore.api;

import java.util.*;
import com.zhekasmirnov.apparatus.ecs.ticking.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.block.*;
import com.zhekasmirnov.apparatus.ecs.core.*;
import com.zhekasmirnov.apparatus.minecraft.enums.*;
import android.content.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.apparatus.cpp.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import com.zhekasmirnov.innercore.api.runtime.saver.world.*;
import com.zhekasmirnov.innercore.api.mod.adaptedscript.*;
import com.zhekasmirnov.mcpe161.*;
import android.app.*;
import android.widget.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.apparatus.api.player.*;
import android.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.innercore.api.entities.*;
import com.zhekasmirnov.innercore.core.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import com.zhekasmirnov.innercore.mod.build.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.api.runtime.*;

public class NativeCallback
{
    public static final String LOGGER_TAG = "INNERCORE-CALLBACK";
    private static ArrayList<Long> allEntities;
    private static final TickingEntityService clientEcsTickingService;
    private static BlockBreakResult currentBlockBreakResultOverride;
    private static int currentPlayerDimension;
    private static boolean firstLevelTick;
    private static final Object generationLock;
    private static boolean isDestroyBlockCallbackInProgress;
    private static boolean isFirstLocalTick;
    private static boolean isLevelDisplayed;
    private static boolean isLocalTickDisabledDueToError;
    private static String lastNativeScreenName;
    private static final int legacyBreakResultOverrideDroppedItemEntityId;
    private static final int legacyBreakResultOverrideExpOrbEntityId;
    private static final TickingEntityService serverEcsTickingService;
    
    static {
        EntityEngineSetupHelper.loadClass();
        NativeCallback.allEntities = new ArrayList<Long>();
        NativeCallback.firstLevelTick = false;
        NativeCallback.currentPlayerDimension = 0;
        NativeCallback.isLevelDisplayed = false;
        new TPSMeter("main-thread", 20, 2000);
        new TPSMeter("mod-thread", 20, 2000);
        NativeCallback.isLocalTickDisabledDueToError = false;
        NativeCallback.isFirstLocalTick = true;
        clientEcsTickingService = EntityEngine.getSingleton(EntityEngine.EngineSingleton.CLIENT).getService(TickingEntityService.class);
        serverEcsTickingService = EntityEngine.getSingleton(EntityEngine.EngineSingleton.SERVER).getService(TickingEntityService.class);
        NativeCallback.isDestroyBlockCallbackInProgress = false;
        NativeCallback.currentBlockBreakResultOverride = null;
        legacyBreakResultOverrideDroppedItemEntityId = GameEnums.getInt(GameEnums.getSingleton().getEnum("entity_type", "item"));
        legacyBreakResultOverrideExpOrbEntityId = GameEnums.getInt(GameEnums.getSingleton().getEnum("entity_type", "experience_orb"));
        NativeCallback.lastNativeScreenName = "none";
        generationLock = new Object();
    }
    
    public static void _onBlockDestroyStarted(final int n, final int n2, final int n3, final int n4) {
        NativeBlock.onBlockDestroyStarted(n, n2, n3, n4);
        Callback.invokeAPICallback("DestroyBlockStart", new Coords(n, n2, n3, n4), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), NativeAPI.getPlayer());
    }
    
    private static native void assureCopyright(final Context p0);
    
    public static BlockBreakResult endOverrideBlockBreakResult() {
        final BlockBreakResult currentBlockBreakResultOverride = NativeCallback.currentBlockBreakResultOverride;
        NativeCallback.currentBlockBreakResultOverride = null;
        return currentBlockBreakResultOverride;
    }
    
    public static ArrayList<Long> getAllEntities() {
        return NativeCallback.allEntities;
    }
    
    public static native String getStringParam(final String p0);
    
    public static boolean isLevelDisplayed() {
        return NativeCallback.isLevelDisplayed;
    }
    
    public static void onAnimateBlockTick(final int n, final int n2, final int n3, final int n4, final int n5) {
        NativeBlock.onAnimateTickCallback(n, n2, n3, n4, n5);
    }
    
    public static void onBiomeMapGenerated(final int n, final int n2, final int n3) {
        WorldGen.onBiomeMapGenerated(n, n2 >> 4, n3 >> 4);
    }
    
    public static void onBlockBuild(final int n, final int n2, final int n3, final int n4, final long n5) {
        Callback.invokeAPICallback("BuildBlock", new Coords(n, n2, n3, n4), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), n5);
    }
    
    public static void onBlockChanged(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final int n7, final int n8, final int n9, final long n10) {
        Callback.invokeAPICallback("BlockChanged", new Coords(n, n2, n3), new FullBlock(n4, n5), new FullBlock(n6, n7), n8, n9, NativeBlockSource.getFromCallbackPointer(n10));
    }
    
    public static void onBlockDestroyContinued(final int n, final int n2, final int n3, final int n4, final float n5) {
        if (n5 < 1.0E-6) {
            _onBlockDestroyStarted(n, n2, n3, n4);
        }
        Callback.invokeAPICallback("DestroyBlockContinue", new Coords(n, n2, n3, n4), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), n5);
    }
    
    public static void onBlockDestroyStarted(final int n, final int n2, final int n3, final int n4) {
    }
    
    public static void onBlockDestroyed(final int n, final int n2, final int n3, final int n4, final long n5) {
        if (NativeCallback.isDestroyBlockCallbackInProgress) {
            return;
        }
        NativeCallback.isDestroyBlockCallbackInProgress = true;
        final FullBlock fullBlock = new FullBlock(n5, n, n2, n3);
        final NativeBlockSource defaultForActor = NativeBlockSource.getDefaultForActor(n5);
        if (defaultForActor != null) {
            Callback.invokeAPICallback("BreakBlock", defaultForActor, new Coords(n, n2, n3, n4), fullBlock, EngineConfig.isDeveloperMode() || new NativePlayer(n5).getGameMode() != 1, n5, new ItemInstance(NativeAPI.getEntityCarriedItem(n5)));
        }
        if (!NativeAPI.isDefaultPrevented()) {
            Callback.invokeAPICallback("DestroyBlock", new Coords(n, n2, n3, n4), fullBlock, n5);
        }
        if (defaultForActor != null) {
            if (!NativeAPI.isDefaultPrevented() && !IDRegistry.isVanilla(defaultForActor.getBlockId(n, n2, n3))) {
                defaultForActor.destroyBlock(n, n2, n3, false);
            }
            if (defaultForActor.getBlockId(n, n2, n3) == 0) {
                NativeAPI.preventDefault();
            }
        }
        NativeCallback.isDestroyBlockCallbackInProgress = false;
        if (!NativeAPI.isTileUpdateAllowed()) {
            NativeAPI.forceRenderRefresh(n, n2, n3, 0);
        }
    }
    
    public static void onBlockEventEntityInside(final int n, final int n2, final int n3, final long n4) {
        Callback.invokeAPICallback("BlockEventEntityInside", new Coords(n, n2, n3), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), n4);
    }
    
    public static void onBlockEventEntityStepOn(final int n, final int n2, final int n3, final long n4) {
        Callback.invokeAPICallback("BlockEventEntityStepOn", new Coords(n, n2, n3), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), n4);
    }
    
    public static void onBlockEventNeighbourChange(final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final long n7) {
        Callback.invokeAPICallback("BlockEventNeighbourChange", new Coords(n, n2, n3), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), new Coords(n4, n5, n6), NativeBlockSource.getFromCallbackPointer(n7));
    }
    
    public static void onBlockSpawnResources(final int n, final int n2, final int n3, final int n4, final int n5, final float n6, final int n7, final long n8) {
        Callback.invokeAPICallback("PopBlockResources", new Coords(n, n2, n3), new FullBlock(n4, n5), n6, n7, NativeBlockSource.getFromCallbackPointer(n8));
    }
    
    public static void onCallbackExceptionOccurred(final String s, final Throwable t) {
        t.printStackTrace();
        final StringBuilder sb = new StringBuilder();
        sb.append("uncaught error occurred in callback ");
        sb.append(s);
        ICLog.e("INNERCORE-CALLBACK", sb.toString(), t);
        if (InnerCoreConfig.getBool("developer_mode")) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("error in ");
            sb2.append(s);
            UserDialog.insignificantError(sb2.toString(), t);
        }
    }
    
    public static void onChunkPostProcessed(final int n, final int n2) {
        WorldGen.generateChunk(n, n2, 1);
    }
    
    public static void onCommandExec() {
        final String stringParam = getStringParam("command");
        Object trim;
        if (stringParam == null) {
            trim = null;
        }
        else {
            trim = stringParam.trim();
        }
        Callback.invokeAPICallback("NativeCommand", trim);
    }
    
    public static void onConnectToHost(final int n) {
        final String stringParam = getStringParam("host");
        NativeIdPlaceholderGenerator.clearAll();
        NativeIdConversionMap.clearAll();
        IdConversionMap.getSingleton().clearLocalIdMap();
        LevelInfo.levelName = null;
        LevelInfo.levelDir = null;
        Minecraft.onConnectToHost(stringParam, n);
        if (!MultiplayerModList.getSingleton().checkMultiplayerAllowed()) {
            Minecraft.leaveGame();
            return;
        }
        Callback.invokeAPICallback("ConnectingToHost", stringParam, n, Network.getSingleton().getConfig().getDefaultPort());
        WorldDataSaverHandler.getInstance().onConnectedToRemoteWorld();
        try {
            AdaptedScriptAPI.IDRegistry.rebuildNetworkIdMap();
            UserDialog.toast("starting modded client");
            Network.getSingleton().startClient(stringParam);
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot connect to modded server at ");
            sb.append(stringParam);
            UserDialog.dialog("CONNECTION ERROR", sb.toString(), ex, false);
        }
        onLevelPostLoaded(false);
    }
    
    public static void onCopyrightCheck() {
        final Activity currentActivity = EnvironmentSetup.getCurrentActivity();
        currentActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                Toast.makeText((Context)currentActivity, (CharSequence)"Inner Core is developed fully and only by zheka_smirnov (zheka2304), all rights are reserved.", 0).show();
                assureCopyright((Context)currentActivity);
            }
        });
    }
    
    public static void onCustomDimensionTransfer(final long n, final int n2, final int n3) {
        Callback.invokeAPICallback("CustomDimensionTransfer", n, n2, n3);
    }
    
    public static void onCustomTessellation(final long n, final int n2, final int n3, final int n4, final int n5, final int n6, final boolean b) {
        NativeBlockRenderer.onRenderCall(new NativeBlockRenderer.RenderAPI(n), new Coords(n2, n3, n4), new FullBlock(n5, n6), b);
    }
    
    public static void onDebugLog() {
        final StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(getStringParam("_log"));
        ICLog.d("NATIVE-DEBUG", sb.toString());
        ICLog.flush();
    }
    
    public static void onDialogRequested() {
        UserDialog.dialog(getStringParam("dialog_title"), getStringParam("dialog_text"));
    }
    
    public static void onDimensionChanged(final int currentPlayerDimension, final int n) {
        if (currentPlayerDimension != n) {
            Callback.invokeAPICallback("DimensionUnloaded", n);
            NativeAPI.clearAllStaticRenders();
            NativeAPI.clearAllRenderMappings();
        }
        NativeCallback.currentPlayerDimension = currentPlayerDimension;
        Callback.invokeAPICallback("DimensionLoaded", currentPlayerDimension, n);
        final StringBuilder sb = new StringBuilder();
        sb.append("player entered dimension ");
        sb.append(currentPlayerDimension);
        sb.append(" from ");
        sb.append(n);
        ICLog.d("INNERCORE-CALLBACK", sb.toString());
    }
    
    public static void onEntityAdded(final long n) {
        if (NativeCallback.currentBlockBreakResultOverride != null) {
            final int type = StaticEntity.getType(n);
            if (type == NativeCallback.legacyBreakResultOverrideDroppedItemEntityId) {
                NativeCallback.currentBlockBreakResultOverride.getItems().add(StaticEntity.getDroppedItem(n));
                NativeAPI.removeEntity(n);
                return;
            }
            if (type == NativeCallback.legacyBreakResultOverrideExpOrbEntityId) {
                NativeCallback.currentBlockBreakResultOverride.addExperience(StaticEntity.getExperienceOrbValue(n));
                NativeAPI.removeEntity(n);
                return;
            }
        }
        NativeCallback.allEntities.add(n);
        Callback.invokeAPICallback("EntityAdded", n);
    }
    
    public static void onEntityAttacked(final long n, final long n2) {
        Callback.invokeAPICallback("PlayerAttack", n2, n);
    }
    
    public static void onEntityDied(final long n, final long n2, final int n3) {
        Callback.invokeAPICallback("EntityDeath", n, n2, n3);
    }
    
    public static void onEntityHurt(final long n, final long n2, final int n3, final int n4, final boolean b, final boolean b2) {
        Callback.invokeAPICallback("EntityHurt", n2, n, n4, n3, b, b2);
        NetworkPlayerRegistry.getSingleton().onEntityHurt(n, n2, n3, n4, b, b2);
        if (n == NativeAPI.getPlayer()) {
            ArmorRegistry.onHurt(n2, n4, n3, b, b2);
        }
    }
    
    public static void onEntityPickUpDrop(final long n, final long n2, final int n3) {
        Callback.invokeAPICallback("EntityPickUpDrop", n, n2, new ItemInstance(new NativeItemInstance(NativeAPI.getItemFromDrop(n2))), n3);
    }
    
    public static void onEntityRemoved(final long n) {
        if (NativeCallback.currentBlockBreakResultOverride != null) {
            final int type = StaticEntity.getType(n);
            if (type == NativeCallback.legacyBreakResultOverrideDroppedItemEntityId || type == NativeCallback.legacyBreakResultOverrideExpOrbEntityId) {
                return;
            }
        }
        Callback.invokeAPICallback("EntityRemoved", n);
        final int index = NativeCallback.allEntities.indexOf(n);
        if (index >= 0) {
            NativeCallback.allEntities.remove(index);
        }
    }
    
    public static void onExpOrbsSpawned(final long n, final int n2, final float n3, final float n4, final float n5, final long n6) {
        Callback.invokeAPICallback("ExpOrbsSpawned", NativeBlockSource.getFromCallbackPointer(n), n2, new Coords(n3, n4, n5), n6);
    }
    
    public static void onExplode(final float n, final float n2, final float n3, final float n4, final long n5, final boolean b, final boolean b2, final float n6) {
        Callback.invokeAPICallback("Explosion", new Coords(n, n2, n3), new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"power", (Object)n4), new Pair((Object)"entity", (Object)n5), new Pair((Object)"onFire", (Object)b), new Pair((Object)"someBool", (Object)b2), new Pair((Object)"someFloat", (Object)n6) }));
    }
    
    public static void onFinalInitComplete() {
        UIUtils.initialize(UIUtils.getContext());
        AdsManager.getInstance().closeAllRequests();
        AdsManager.getInstance().closeInterstitialAds();
        final AsyncModLauncher asyncModLauncher = new AsyncModLauncher();
        if (InnerCoreConfig.getBool("disable_loading_screen")) {
            asyncModLauncher.launchModsInCurrentThread();
            return;
        }
        asyncModLauncher.launchModsInThread();
    }
    
    public static void onFinalInitStarted() {
        if (EngineConfig.isDeveloperMode()) {
            ICLog.i("NativeProfiling", "developer mode is enabled - turning on native callback profiling and signal handling");
            Profiler.setCallbackProfilingEnabled(true);
            Profiler.setExtremeSignalHandlingEnabled(true);
            return;
        }
        ICLog.i("NativeProfiling", "developer mode is disabled - turning off native callback profiling and signal handling");
        Profiler.setCallbackProfilingEnabled(false);
        Profiler.setExtremeSignalHandlingEnabled(false);
    }
    
    public static void onGameSetPaused(final boolean b) {
        if (b) {
            ICLog.d("INNERCORE-CALLBACK", "pausing ticking thread...");
            TickManager.pause();
            return;
        }
        ICLog.d("INNERCORE-CALLBACK", "resuming ticking thread...");
        TickManager.resume();
    }
    
    public static void onGameStopped() {
        NativeCallback.firstLevelTick = false;
        NativeCallback.isFirstLocalTick = true;
        NativeCallback.isLevelDisplayed = false;
        NativeCallback.isLocalTickDisabledDueToError = false;
        TickManager.clearLastFatalError();
        Callback.invokeAPICallback("LevelPreLeft", new Object[0]);
        Network.getSingleton().shutdown();
        NativeNetworking.onLevelLeft();
        NativeIdPlaceholderGenerator.clearAll();
        WorldDataSaverHandler.getInstance().onLevelLeft();
        Callback.invokeAPICallback("DimensionUnloaded", NativeCallback.currentPlayerDimension);
        Callback.invokeAPICallback("LevelLeft", new Object[0]);
        LevelInfo.onLeft();
        Updatable.cleanUpAll();
        TickManager.stop();
        NativeCallback.allEntities.clear();
        NativePathNavigation.cleanup();
        NativeBlockSource.resetDefaultBlockSources();
        MinecraftActivity.onLevelLeft();
        NativeIdConversionMap.clearAll();
        IdConversionMap.getSingleton().clearLocalIdMap();
    }
    
    public static void onInteractWithEntity(final long n, final long n2, final float n3, final float n4, final float n5) {
        Callback.invokeAPICallback("EntityInteract", n, n2, new Coords(n3, n4, n5));
    }
    
    public static void onItemDispensed(final float n, final float n2, final float n3, final int side, final int n4, final int n5, final int n6, final long n7, final long n8) {
        Callback.invokeAPICallback("ItemDispensed", new Coords(n, n2, n3).setSide(side), new ItemInstance(n4, n5, n6, NativeItemInstanceExtra.getExtraOrNull(n7)), NativeBlockSource.getFromCallbackPointer(n8));
    }
    
    public static void onItemIconOverride(final int n, final int n2, final int n3, final int n4) {
        synchronized (NativeItem.DYNAMIC_ICON_LOCK) {
            Callback.invokeAPICallback("ItemIconOverride", new ItemInstance(n, n2, n3, NativeItemInstanceExtra.getExtraOrNull(n4)), false);
        }
    }
    
    public static void onItemModelOverride(long pointer, final int n, final int n2, final int n3, final long n4) {
        final NativeItemModel byPointer = NativeItemModel.getByPointer(pointer);
        if (byPointer != null) {
            while (true) {
                while (true) {
                    Label_0066: {
                        try {
                            final NativeItemModel modelForItemInstance = byPointer.getModelForItemInstance(n, n2, n3, NativeItemInstanceExtra.getExtraOrNull(n4));
                            if (modelForItemInstance != byPointer) {
                                if (modelForItemInstance == null) {
                                    break Label_0066;
                                }
                                pointer = modelForItemInstance.pointer;
                                NativeAPI.overrideItemModel(pointer);
                            }
                            return;
                        }
                        catch (Throwable t) {
                            ICLog.e("INNERCORE-CALLBACK", "error occurred in model override callback", t);
                        }
                        break;
                    }
                    pointer = 0L;
                    continue;
                }
            }
        }
    }
    
    public static void onItemNameOverride(final int n, final int n2, final int n3, final int n4) {
        final String stringParam = getStringParam("name");
        final String translate = NameTranslation.translate(stringParam);
        synchronized (NativeItem.DYNAMIC_NAME_LOCK) {
            NativeAPI.overrideItemName(translate);
            Callback.invokeAPICallback("ItemNameOverride", new ItemInstance(n, n2, n3, NativeItemInstanceExtra.getExtraOrNull(n4)), translate, stringParam);
        }
    }
    
    public static void onItemUseComplete(final long n) {
        Callback.invokeAPICallback("ItemUsingComplete", new ItemInstance(NativeAPI.getEntityCarriedItem(n)), n);
    }
    
    public static void onItemUseReleased(final int n, final long n2) {
        Callback.invokeAPICallback("ItemUsingReleased", new ItemInstance(NativeAPI.getEntityCarriedItem(n2)), n, n2);
    }
    
    public static void onItemUsed(final int n, final int n2, final int n3, final int n4, final float n5, final float n6, final float n7, final boolean b, final boolean b2, final long n8) {
        final Coords coords = new Coords(n, n2, n3, n4);
        coords.put("vec", (Scriptable)coords, (Object)new Coords(n5, n6, n7));
        if (b) {
            Callback.invokeAPICallback("ItemUse", coords, new ItemInstance(NativeAPI.getEntityCarriedItem(n8)), new FullBlock(n8, n, n2, n3), b2, n8);
            return;
        }
        Callback.invokeAPICallback("ItemUseLocal", coords, new ItemInstance(NativeAPI.getEntityCarriedItem(n8)), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), n8);
        Callback.invokeAPICallback("ItemUseLocalServer", coords, new ItemInstance(NativeAPI.getEntityCarriedItem(n8)), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), false, n8);
    }
    
    public static void onItemUsedNoTarget(final long n) {
        Callback.invokeAPICallback("ItemUseNoTarget", new ItemInstance(NativeAPI.getEntityCarriedItem(n)), n);
    }
    
    public static void onKeyEventDispatched(final int n, final int n2) {
        Callback.invokeAPICallback("SystemKeyEventDispatched", n, n2);
        if (n == 0 && n2 == 1) {
            Callback.invokeAPICallback("NavigationBackPressed", new Object[0]);
            if (!NativeAPI.isDefaultPrevented()) {
                WindowProvider.instance.onBackPressed();
            }
        }
    }
    
    public static void onLevelCreated() {
        NativeCallback.firstLevelTick = true;
        Callback.invokeAPICallback("LevelCreated", new Object[0]);
    }
    
    public static void onLevelLoaded() {
        NativeCallback.isLevelDisplayed = true;
        Callback.invokeAPICallback("LevelDisplayed", new Object[0]);
        Minecraft.onLevelDisplayed();
    }
    
    private static void onLevelPostLoaded(final boolean b) {
        MainThreadQueue.localThread.clearQueue();
        MainThreadQueue.serverThread.clearQueue();
        Callback.invokeAPICallback("LevelPreLoaded", b);
        WorldDataSaverHandler.getInstance().onLevelLoading();
        if (b) {
            Network.getSingleton().startLanServer();
        }
        NameTranslation.refresh();
        LevelInfo.onLoaded();
        Callback.invokeAPICallback("LevelLoaded", b);
        setupThreadPriorityFromConfig();
        TickManager.setupAndStart();
        MainThreadQueue.localThread.clearQueue();
        MainThreadQueue.serverThread.clearQueue();
    }
    
    public static void onLocalServerStarted() {
        NativeCallback.firstLevelTick = true;
        NativeIdPlaceholderGenerator.clearAll();
        NativeIdConversionMap.clearAll();
        IdConversionMap.getSingleton().clearLocalIdMap();
        VanillaIdConversionMap.getSingleton().reloadFromAssets();
        AdaptedScriptAPI.IDRegistry.rebuildNetworkIdMap();
        final String stringParam = getStringParam("world_name");
        final String stringParam2 = getStringParam("world_dir");
        final String dir_PACK = FileTools.DIR_PACK;
        final StringBuilder sb = new StringBuilder();
        sb.append("worlds/");
        sb.append(stringParam2);
        final File file = new File(dir_PACK, sb.toString());
        ModLoader.instance.addResourceAndBehaviorPacksInWorld(file);
        WorldDataSaverHandler.getInstance().onLevelSelected(file);
        MainThreadQueue.localThread.clearQueue();
        MainThreadQueue.serverThread.clearQueue();
        Minecraft.onLevelSelected();
        Updatable.cleanUpAll();
        LevelInfo.onEnter(stringParam, stringParam2);
        Callback.invokeAPICallback("LevelSelected", stringParam, stringParam2);
    }
    
    public static void onLocalTick() {
        if (NativeCallback.isFirstLocalTick) {
            NativeCallback.isFirstLocalTick = false;
            NetworkThreadMarker.markThreadAs(NetworkThreadMarker.Mark.CLIENT);
            Network.getSingleton().getClient().setPlayerUid(NativeAPI.getLocalPlayer());
            Callback.invokeAPICallback("LocalLevelLoaded", new Object[0]);
            final int entityDimension = NativeAPI.getEntityDimension(NativeAPI.getLocalPlayer());
            onDimensionChanged(entityDimension, entityDimension);
        }
        else {
            final int entityDimension2 = NativeAPI.getEntityDimension(NativeAPI.getLocalPlayer());
            if (entityDimension2 != NativeCallback.currentPlayerDimension) {
                onDimensionChanged(entityDimension2, NativeCallback.currentPlayerDimension);
            }
        }
        if (!NativeAPI.isLocalServerRunning()) {
            InventorySource.tick();
        }
        if (!NativeCallback.isLocalTickDisabledDueToError) {
            try {
                Callback.invokeAPICallbackUnsafe("LocalTick", new Object[0]);
                Updatable.getForClient().onTickSingleThreaded();
            }
            catch (Throwable t) {
                ICLog.e("INNERCORE-CALLBACK", "error occurred in local tick callback", t);
                DialogHelper.reportFatalError("Fatal error occurred in local tick callback, local tick callback will be turned off until you re-enter the world.", t);
                NativeCallback.isLocalTickDisabledDueToError = true;
            }
        }
        if (TickManager.getLastFatalError() != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("§c");
            sb.append(AdaptedScriptAPI.Translation.translate("system.thread_stopped"));
            NativeAPI.tipMessage(sb.toString());
        }
        MainThreadQueue.localThread.executeQueue();
    }
    
    public static void onMinecraftAppSuspended() {
        Callback.invokeAPICallback("AppSuspended", new Object[0]);
    }
    
    public static void onModdedClientPacketReceived(final int n) {
        NativeNetworking.onClientPacketReceived(getStringParam("name"), n);
    }
    
    public static void onModdedServerPacketReceived(final int n) {
        NativeNetworking.onServerPacketReceived(getStringParam("client"), getStringParam("name"), n);
    }
    
    public static void onNativeGuiLoaded() {
        MinecraftActivity.onNativeGuiLoaded();
    }
    
    public static void onPathNavigationDone(final long n, final int n2) {
        NativePathNavigation.onNavigationResult(n, n2);
    }
    
    public static void onPlayerEat(final int n, final float n2, final long n3) {
        Callback.invokeAPICallback("FoodEaten", n, n2, n3);
        NetworkPlayerRegistry.getSingleton().onPlayerEat(n3, n, n2);
    }
    
    public static void onPlayerExpAdded(final int n, final long n2) {
        Callback.invokeAPICallback("ExpAdd", n, n2);
    }
    
    public static void onPlayerLevelAdded(final int n, final long n2) {
        Callback.invokeAPICallback("ExpLevelAdd", n, n2);
    }
    
    public static void onPreChunkPostProcessed(final int n, final int n2) {
        WorldGen.generateChunk(n, n2, 0);
    }
    
    public static void onRandomBlockTick(final int n, final int n2, final int n3, final int n4, final int n5, final long n6) {
        NativeBlock.onRandomTickCallback(n, n2, n3, n4, n5, NativeBlockSource.getFromCallbackPointer(n6));
    }
    
    public static void onRedstoneSignalChange(final int n, final int n2, final int n3, final int n4, final boolean b, final long n5) {
        Callback.invokeAPICallback("RedstoneSignal", new Coords(n, n2, n3), new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"power", (Object)n4), new Pair((Object)"signal", (Object)n4), new Pair((Object)"onLoad", (Object)b) }), new FullBlock(NativeAPI.getTileAndData(n, n2, n3)), NativeBlockSource.getFromCallbackPointer(n5));
    }
    
    public static void onScreenChanged(final boolean b) {
        AdsManager.getInstance().closeAllRequests();
        final String stringParam = getStringParam("screen_name");
        final String stringParam2 = getStringParam("last_screen_name");
        final StringBuilder sb = new StringBuilder();
        sb.append("screen changed: ");
        sb.append(stringParam2);
        sb.append(" -> ");
        sb.append(stringParam);
        String s;
        if (b) {
            s = " (pushed)";
        }
        else {
            s = " (popped)";
        }
        sb.append(s);
        Logger.debug(sb.toString());
        if (stringParam.equals("leave_level_screen") || stringParam.equals("pause_screen") || stringParam.startsWith("world_loading_progress_screen")) {
            WorldDataSaverHandler.getInstance().onPauseScreenOpened();
        }
        Callback.invokeAPICallback("NativeGuiChanged", stringParam, stringParam2, b);
        if (NativeCallback.lastNativeScreenName.startsWith("world_loading_progress_screen")) {
            stringParam.equals("in_game_play_screen");
        }
        if (!stringParam.equals("hud_screen")) {
            NativeCallback.lastNativeScreenName = stringParam;
        }
        if (!stringParam.equals("start_screen") && !stringParam.startsWith("play_screen - ") && !stringParam.startsWith("world_loading_progress_screen - ") && !stringParam.startsWith("world_saving_progress_screen")) {
            MainMenuBanner.getInstance().close("main");
            System.out.println("hiding main");
            return;
        }
        MainMenuBanner.getInstance().show("main", MainMenuBanner.Location.BOTTOM);
        System.out.println("showing main");
    }
    
    public static void onThrowableHit(final long n, final float n2, final float n3, final float n4, final long n5, final int n6, final int n7, final int n8, final int n9, final int n10, final int n11, final int n12, final long n13) {
        final ItemInstance itemInstance = new ItemInstance(n10, n11, n12, NativeItemInstanceExtra.getExtraOrNull(n13));
        final Pair pair = new Pair((Object)"x", (Object)n2);
        final Pair pair2 = new Pair((Object)"y", (Object)n3);
        final Pair pair3 = new Pair((Object)"z", (Object)n4);
        final Pair pair4 = new Pair((Object)"entity", (Object)n5);
        Object o;
        if (n6 == 0 && n7 == 0 && n8 == 0 && n9 == 0) {
            o = null;
        }
        else {
            o = new Coords(n6, n7, n8, n9);
        }
        Callback.invokeAPICallback("ProjectileHit", n, itemInstance, new ScriptableParams((Pair<String, Object>[])new Pair[] { pair, pair2, pair3, pair4, new Pair((Object)"coords", o) }));
    }
    
    public static void onTick() {
        if (NativeCallback.firstLevelTick) {
            NetworkThreadMarker.markThreadAs(NetworkThreadMarker.Mark.SERVER);
            onLevelPostLoaded(true);
            NativeCallback.firstLevelTick = false;
            setupWorld();
        }
        TickManager.nativeTick();
        NetworkPlayerRegistry.getSingleton().onTick();
        MainThreadQueue.serverThread.executeQueue();
    }
    
    public static void onToastRequested() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Native Toast: ");
        sb.append(getStringParam("toast"));
        UserDialog.toast(sb.toString());
    }
    
    private static void setupThreadPriorityFromConfig() {
        TickExecutor.getInstance().setAdditionalThreadCount(InnerCoreConfig.getInt("threading.additional_thread_count", 0));
        TickExecutor.getInstance().setAdditionalThreadPriority(InnerCoreConfig.getInt("threading.additional_thread_priority", 12) / 4);
        if (InnerCoreConfig.getBool("threading.advanced")) {
            NativeAPI.setNativeThreadPriorityParams(InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.priority_low", 1)), InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.priority_high", 40)), InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.threshold_fps", 45)));
            return;
        }
        NativeAPI.setNativeThreadPriorityParams(InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.priority_simple", 1)), InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.priority_simple", 1) + 10), InnerCoreConfig.convertThreadPriority(InnerCoreConfig.getInt("threading.threshold_fps", 45)));
    }
    
    private static void setupWorld() {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static void startOverrideBlockBreakResult() {
        NativeCallback.currentBlockBreakResultOverride = new BlockBreakResult();
    }
}
