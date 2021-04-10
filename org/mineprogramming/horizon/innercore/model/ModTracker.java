package org.mineprogramming.horizon.innercore.model;

import com.zhekasmirnov.innercore.modpack.*;
import java.util.*;
import java.io.*;

public class ModTracker
{
    private static ModTracker currentTracker;
    private boolean locationsDirty;
    private List<String> modLocations;
    private Map<Integer, String> modLocationsById;
    private final ModPack modPack;
    private Map<Integer, Integer> modVersions;
    private DirectorySetRequestHandler requestHandler;
    private boolean versionsDirty;
    
    private ModTracker(final ModPack modPack) {
        this.locationsDirty = true;
        this.versionsDirty = true;
        this.modPack = modPack;
    }
    
    public static ModTracker forPack(final ModPack modPack) {
        if (ModTracker.currentTracker != null && ModTracker.currentTracker.modPack == modPack) {
            return ModTracker.currentTracker;
        }
        return new ModTracker(modPack);
    }
    
    public static ModTracker getCurrent() {
        final ModPack currentModPack = ModPackContext.getInstance().getCurrentModPack();
        if (ModTracker.currentTracker == null || ModTracker.currentTracker.modPack != currentModPack) {
            ModTracker.currentTracker = new ModTracker(currentModPack);
        }
        return ModTracker.currentTracker;
    }
    
    private void rebuildLocationsList() {
        this.requestHandler = this.modPack.getRequestHandler(ModPackDirectory$DirectoryType.MODS);
        this.modLocations = (List<String>)this.requestHandler.getAllLocations();
        this.locationsDirty = false;
    }
    
    private void rebuildVersionsList() {
        this.rebuildLocationsListIfRequired();
        this.modVersions = new HashMap<Integer, Integer>();
        this.modLocationsById = new HashMap<Integer, String>();
        for (final String s : this.modLocations) {
            final ModPreferences modPreferences = new ModPreferences(this.requestHandler.get(s));
            final int icmodsId = modPreferences.getIcmodsId();
            if (icmodsId != 0) {
                this.modVersions.put(icmodsId, modPreferences.getIcmodsVersion());
                this.modLocationsById.put(icmodsId, s);
            }
        }
        this.versionsDirty = false;
    }
    
    public String getLocation(final int n) {
        this.rebuildVersionsListIfRequired();
        return this.modLocationsById.get(n);
    }
    
    public List<String> getModLocations() {
        this.rebuildLocationsListIfRequired();
        return this.modLocations;
    }
    
    public int getModsCount() {
        this.rebuildLocationsListIfRequired();
        return this.modLocations.size();
    }
    
    public Map<Integer, Integer> getVersions() {
        this.rebuildVersionsListIfRequired();
        return this.modVersions;
    }
    
    public void invalidate() {
        this.locationsDirty = true;
        this.versionsDirty = true;
    }
    
    public boolean isInstalled(final int n) {
        this.rebuildVersionsListIfRequired();
        return this.modVersions.containsKey(n);
    }
    
    public void onDeleted(final String s, final int n) {
        this.rebuildVersionsListIfRequired();
        this.modLocations.remove(s);
        this.modVersions.remove(n);
        this.modLocationsById.remove(n);
    }
    
    public void onInstalled(final File file, final String s, final int n, final int n2) {
        this.rebuildVersionsListIfRequired();
        this.modLocations.add(s);
        this.modVersions.put(n, n2);
        this.modLocationsById.put(n, s);
        new ModPreferences(file).setIcmodsData(n, n2);
    }
    
    public void rebuildLocationsListIfRequired() {
        if (this.modLocations == null || this.locationsDirty) {
            this.rebuildLocationsList();
            this.versionsDirty = true;
        }
    }
    
    public void rebuildVersionsListIfRequired() {
        if (this.modVersions == null || this.versionsDirty) {
            this.rebuildVersionsList();
        }
    }
}
