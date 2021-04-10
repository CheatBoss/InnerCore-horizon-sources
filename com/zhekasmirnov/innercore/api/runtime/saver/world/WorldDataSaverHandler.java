package com.zhekasmirnov.innercore.api.runtime.saver.world;

import java.io.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;

public class WorldDataSaverHandler
{
    private static final WorldDataSaverHandler instance;
    private boolean autoSaveEnabled;
    private int autoSaveInterval;
    private boolean autoSaveMinecraftWorld;
    private long previousAutoSave;
    private boolean saveWasQueued;
    private WorldDataSaver worldDataSaver;
    
    static {
        instance = new WorldDataSaverHandler();
    }
    
    public WorldDataSaverHandler() {
        this.worldDataSaver = null;
        this.autoSaveEnabled = true;
        this.autoSaveMinecraftWorld = true;
        this.autoSaveInterval = 30000;
        this.saveWasQueued = false;
        this.previousAutoSave = 0L;
    }
    
    public static WorldDataSaverHandler getInstance() {
        return WorldDataSaverHandler.instance;
    }
    
    private WorldDataSaver initSaverOnNewWorldLoad(final File file) {
        synchronized (this) {
            if (this.worldDataSaver != null && (file == null || !file.equals(this.worldDataSaver.getWorldDirectory()))) {
                this.saveAndReleaseSaver();
            }
            return this.worldDataSaver = new WorldDataSaver(file);
        }
    }
    
    private void readDataOnLoad() {
        synchronized (this) {
            if (this.worldDataSaver != null) {
                final OperationTimeLogger start = new OperationTimeLogger(EngineConfig.isDeveloperMode()).start();
                this.worldDataSaver.readAllData(true);
                start.finish("reading all mod data done in %f seconds");
            }
            else {
                this.reportUnexpectedStateError("World data saver was not initialized during readDataOnLoad() call");
            }
        }
    }
    
    private void reportUnexpectedStateError(final String s) {
        UserDialog.dialog("UNEXPECTED WORLD SAVER STATE", s);
    }
    
    private void runWorldAndDataSave() {
        while (true) {
            synchronized (this) {
                if (this.worldDataSaver == null) {
                    this.reportUnexpectedStateError("World data saver was not initialized during runWorldAndDataSave() call");
                    return;
                }
                final OperationTimeLogger start = new OperationTimeLogger(false).start();
                this.worldDataSaver.saveAllData(true);
                start.finish("saving all mod data done in %f seconds");
                if (this.autoSaveMinecraftWorld) {
                    NativeAPI.forceLevelSave();
                    start.finish("minecraft world done in %f seconds");
                }
            }
        }
    }
    
    private void saveAndReleaseSaver() {
        synchronized (this) {
            if (this.worldDataSaver != null) {
                final OperationTimeLogger start = new OperationTimeLogger(EngineConfig.isDeveloperMode()).start();
                this.worldDataSaver.saveAllData(true);
                start.finish("saving all mod data done in %f seconds").start();
                this.worldDataSaver = null;
            }
            else {
                this.reportUnexpectedStateError("World data saver was not initialized during saveAndReleaseSaver() call");
            }
        }
    }
    
    public void fetchParamsFromConfig() {
        this.setParams(EngineConfig.getBoolean("background.auto_save", true), EngineConfig.getBoolean("background.auto_save_world", true), EngineConfig.getInt("background.auto_save_period", 30) * 1000);
    }
    
    public WorldDataSaver getWorldDataSaver() {
        synchronized (this) {
            return this.worldDataSaver;
        }
    }
    
    public void onConnectedToRemoteWorld() {
        this.initSaverOnNewWorldLoad(null);
    }
    
    public void onLevelLeft() {
        final StringBuilder sb = new StringBuilder();
        sb.append("level left: ");
        String string;
        if (this.worldDataSaver != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("dir=");
            sb2.append(this.worldDataSaver.getWorldDirectory());
            string = sb2.toString();
        }
        else {
            string = "save is null";
        }
        sb.append(string);
        ICLog.d("WorldDataSaverHandler", sb.toString());
        this.saveAndReleaseSaver();
    }
    
    public void onLevelLoading() {
        final StringBuilder sb = new StringBuilder();
        sb.append("reading data: ");
        String string;
        if (this.worldDataSaver != null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("dir=");
            sb2.append(this.worldDataSaver.getWorldDirectory());
            string = sb2.toString();
        }
        else {
            string = "save is null";
        }
        sb.append(string);
        ICLog.d("WorldDataSaverHandler", sb.toString());
        this.readDataOnLoad();
        this.previousAutoSave = System.currentTimeMillis();
    }
    
    public void onLevelSelected(final File file) {
        final StringBuilder sb = new StringBuilder();
        sb.append("level selected: ");
        sb.append(file.getAbsolutePath());
        ICLog.d("WorldDataSaverHandler", sb.toString());
        this.initSaverOnNewWorldLoad(file);
    }
    
    public void onPauseScreenOpened() {
        this.queueSave();
    }
    
    public void onTick() {
        if (this.saveWasQueued) {
            this.saveWasQueued = false;
            this.runWorldAndDataSave();
        }
        if (this.autoSaveEnabled && this.autoSaveInterval + this.previousAutoSave < System.currentTimeMillis()) {
            this.previousAutoSave = System.currentTimeMillis();
            this.runWorldAndDataSave();
        }
    }
    
    public void queueSave() {
        this.saveWasQueued = true;
    }
    
    public void setParams(final boolean autoSaveEnabled, final boolean autoSaveMinecraftWorld, final int autoSaveInterval) {
        this.autoSaveEnabled = autoSaveEnabled;
        this.autoSaveMinecraftWorld = autoSaveMinecraftWorld;
        this.autoSaveInterval = autoSaveInterval;
    }
}
