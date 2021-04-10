package org.mineprogramming.horizon.innercore.model;

import android.net.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import java.util.*;

public abstract class ModSource
{
    protected static Uri missingIcon;
    private boolean loadedLastNotified;
    private ModSourceListener modSourceListener;
    private List<Mod> mods;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_PACK);
        sb.append("assets/innercore/ui/missing_mod_icon.png");
        ModSource.missingIcon = Uri.fromFile(new File(sb.toString()));
    }
    
    public ModSource() {
        this.loadedLastNotified = true;
        this.mods = new ArrayList<Mod>();
    }
    
    private void notifyChange() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onChange();
        }
    }
    
    protected void addMod(final Mod mod) {
        this.mods.add(mod);
        this.notifyChange();
    }
    
    protected void clearMods() {
        this.mods.clear();
        this.notifyChange();
    }
    
    public Mod get(final int n) {
        return this.mods.get(n);
    }
    
    public int getItemCount() {
        return this.mods.size();
    }
    
    protected void notifyLoadFailed() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadFailed();
        }
    }
    
    protected void notifyLoadInProgress() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadInProgress();
        }
    }
    
    protected void notifyLoadLast() {
        if (this.modSourceListener != null) {
            this.modSourceListener.onLoadLast();
            return;
        }
        this.loadedLastNotified = false;
    }
    
    public void requestMore() {
    }
    
    public void retryLoad() {
    }
    
    public void setOnChangeListener(final ModSourceListener modSourceListener) {
        this.modSourceListener = modSourceListener;
        if (!this.loadedLastNotified) {
            this.loadedLastNotified = true;
            modSourceListener.onLoadLast();
        }
    }
    
    public interface ModSourceListener
    {
        void onChange();
        
        void onLoadFailed();
        
        void onLoadInProgress();
        
        void onLoadLast();
    }
}
