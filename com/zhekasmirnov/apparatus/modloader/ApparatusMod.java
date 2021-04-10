package com.zhekasmirnov.apparatus.modloader;

import com.zhekasmirnov.apparatus.adapter.env.*;

public abstract class ApparatusMod
{
    private final ApparatusModInfo info;
    private ModState modState;
    
    public ApparatusMod() {
        this.modState = ModState.INITIALIZED;
        this.info = new ApparatusModInfo();
    }
    
    public ApparatusModInfo getInfo() {
        return this.info;
    }
    
    public ModState getModState() {
        return this.modState;
    }
    
    public abstract boolean isEnabledAndAbleToRun();
    
    public abstract void onPrepareResources(final ModLoaderReporter p0);
    
    public abstract void onRunningMod(final ModLoaderReporter p0);
    
    public abstract void onSettingUpEnvironment(final EnvironmentSetupProxy p0, final ModLoaderReporter p1);
    
    public abstract void onShuttingDown(final ModLoaderReporter p0);
    
    void setModState(final ModState modState) {
        this.modState = modState;
    }
    
    public enum ModState
    {
        ENVIRONMENT_SETUP, 
        INITIALIZED, 
        PREPARED, 
        RUNNING, 
        SHUTDOWN;
    }
}
