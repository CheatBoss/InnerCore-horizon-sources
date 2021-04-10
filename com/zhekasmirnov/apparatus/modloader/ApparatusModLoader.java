package com.zhekasmirnov.apparatus.modloader;

import java.util.*;
import com.zhekasmirnov.apparatus.adapter.env.*;
import com.zhekasmirnov.innercore.ui.*;

public class ApparatusModLoader
{
    private static final ApparatusModLoader singleton;
    private final List<ApparatusMod> allMods;
    
    static {
        singleton = new ApparatusModLoader();
    }
    
    private ApparatusModLoader() {
        this.allMods = new ArrayList<ApparatusMod>();
    }
    
    public static ApparatusModLoader getSingleton() {
        return ApparatusModLoader.singleton;
    }
    
    public List<ApparatusMod> getAllMods() {
        return this.allMods;
    }
    
    public void prepareModResources(final ModLoaderReporter modLoaderReporter) {
        for (final ApparatusMod apparatusMod : this.allMods) {
            apparatusMod.onPrepareResources(modLoaderReporter);
            apparatusMod.setModState(ApparatusMod.ModState.PREPARED);
        }
    }
    
    public void reloadModsAndSetupEnvironment(final List<AbstractModSource> list, final EnvironmentSetupProxy environmentSetupProxy, final ModLoaderReporter modLoaderReporter) {
        this.shutdownAndClear(modLoaderReporter);
        final ArrayList<ApparatusMod> list2 = new ArrayList<ApparatusMod>();
        final Iterator<AbstractModSource> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().addMods(list2, modLoaderReporter);
        }
        for (final ApparatusMod apparatusMod : list2) {
            if (apparatusMod.isEnabledAndAbleToRun()) {
                this.allMods.add(apparatusMod);
            }
        }
        for (final ApparatusMod apparatusMod2 : this.allMods) {
            apparatusMod2.onSettingUpEnvironment(environmentSetupProxy, modLoaderReporter);
            apparatusMod2.setModState(ApparatusMod.ModState.ENVIRONMENT_SETUP);
        }
    }
    
    public void runMods(final ModLoaderReporter modLoaderReporter) {
        int n = 1;
        final int size = this.allMods.size();
        for (final ApparatusMod apparatusMod : this.allMods) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Running Mods  ");
            sb.append(n);
            sb.append("/");
            sb.append(size);
            sb.append("...");
            LoadingUI.setTextAndProgressBar(sb.toString(), n / (float)size * 0.3f + 0.5f);
            LoadingUI.setTip(apparatusMod.getInfo().getString("displayed_name", ""));
            ++n;
            apparatusMod.onRunningMod(modLoaderReporter);
            apparatusMod.setModState(ApparatusMod.ModState.RUNNING);
        }
    }
    
    public void shutdownAndClear(final ModLoaderReporter modLoaderReporter) {
        synchronized (this.allMods) {
            final Iterator<ApparatusMod> iterator = this.allMods.iterator();
            while (iterator.hasNext()) {
                iterator.next().onShuttingDown(modLoaderReporter);
            }
            this.allMods.clear();
        }
    }
    
    public interface AbstractModSource
    {
        void addMods(final List<ApparatusMod> p0, final ModLoaderReporter p1);
    }
}
