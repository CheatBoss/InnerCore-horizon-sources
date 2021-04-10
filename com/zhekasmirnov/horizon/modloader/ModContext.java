package com.zhekasmirnov.horizon.modloader;

import android.content.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.zhekasmirnov.horizon.modloader.mod.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.util.*;

public class ModContext
{
    public final Context context;
    public final ResourceManager resourceManager;
    public final ExecutionDirectory executionDirectory;
    private final List<Mod> disabledMods;
    private final List<Mod> activeMods;
    private final EventLogger logger;
    private LaunchSequence sequence;
    private final HashMap<String, List<EventReceiver>> eventReceivers;
    
    public ModContext(final Context context, final ResourceManager resourceManager, final ExecutionDirectory executionDirectory) {
        this.disabledMods = new ArrayList<Mod>();
        this.activeMods = new ArrayList<Mod>();
        this.logger = new EventLogger();
        this.eventReceivers = new HashMap<String, List<EventReceiver>>();
        this.context = context;
        this.resourceManager = resourceManager;
        this.executionDirectory = executionDirectory;
    }
    
    public Context getActivityContext() {
        return this.context;
    }
    
    public ResourceManager getResourceManager() {
        return this.resourceManager;
    }
    
    public ExecutionDirectory getExecutionDirectory() {
        return this.executionDirectory;
    }
    
    public List<Mod> getActiveMods() {
        return this.activeMods;
    }
    
    public List<Mod> getDisabledMods() {
        return this.disabledMods;
    }
    
    public EventLogger getEventLogger() {
        return this.logger;
    }
    
    private static native void nativeClearModuleRegistry();
    
    private static native void nativeInitializeAllModules();
    
    public void clearContext() {
        this.resourceManager.clear();
        this.executionDirectory.clear();
        nativeClearModuleRegistry();
        this.sendEvent("clearContext", new Mod[0]);
    }
    
    public void clearModsAndContext() {
        this.clearContext();
        this.activeMods.clear();
        this.disabledMods.clear();
        this.sendEvent("clearMods", new Mod[0]);
    }
    
    private void handleModSafety(final Mod mod) {
        final Mod.SafetyInterface safety = mod.getSafetyInterface();
        final Mod.ConfigurationInterface config = mod.getConfigurationInterface();
        if (safety.isCrashRegistered()) {
            safety.setDisabledDueToCrash(true);
            safety.removeCrashLock();
            config.setActive(false);
        }
        if (safety.isDisabledDueToCrash()) {
            if (config.isActive()) {
                safety.setDisabledDueToCrash(false);
            }
            else {
                this.sendEvent("disabledDueToCrash", mod);
            }
        }
    }
    
    public void addMod(final Mod mod) {
        final Mod.ConfigurationInterface config = mod.getConfigurationInterface();
        this.handleModSafety(mod);
        if (config.isActive()) {
            this.activeMods.add(mod);
        }
        else {
            this.disabledMods.add(mod);
        }
    }
    
    public void addMods(final Collection<Mod> mods) {
        for (final Mod mod : mods) {
            this.addMod(mod);
        }
    }
    
    public void injectAll() {
        this.sendEvent("injectAll", new Mod[0]);
        for (final Mod mod : this.activeMods) {
            mod.inject();
        }
    }
    
    public void buildAll() {
        this.sendEvent("buildAll", new Mod[0]);
        this.getEventLogger().section("build");
        (this.sequence = this.executionDirectory.build(this.context, this.getEventLogger())).buildSequence(this.getEventLogger());
    }
    
    public void initializeAll() {
        this.getEventLogger().section("initialize");
        this.sendEvent("initializeAll", new Mod[0]);
        this.sequence.loadAll(this.getEventLogger());
        for (final Mod mod : this.activeMods) {
            mod.initialize();
        }
    }
    
    public void launchAll() {
        this.sendEvent("launchAll", new Mod[0]);
        this.resourceManager.deployAllOverrides();
        nativeInitializeAllModules();
    }
    
    public void addEventReceiver(final String event, final EventReceiver receiver) {
        List<EventReceiver> events = this.eventReceivers.get(event);
        if (events == null) {
            events = new ArrayList<EventReceiver>();
            this.eventReceivers.put(event, events);
        }
        events.add(receiver);
    }
    
    private void sendEvent(final String event, final Mod... mods) {
        final List<EventReceiver> events = this.eventReceivers.get(event);
        if (events != null) {
            for (final EventReceiver receiver : events) {
                receiver.onEvent(mods);
            }
        }
    }
    
    public interface EventReceiver
    {
        void onEvent(final Mod... p0);
    }
}
