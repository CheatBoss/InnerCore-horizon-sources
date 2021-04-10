package com.zhekasmirnov.innercore.modpack;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import java.util.concurrent.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$ModPackContext$t3c2vG6R7kQT6HyPXIINf91gF4w.class })
public class ModPackContext
{
    private static final ModPackContext instance;
    private ModPack currentModPack;
    private final List<ModPackDeselectedListener> deselectedListenerList;
    private final List<ModPackSelectedListener> selectedListenerList;
    private final ModPackStorage storage;
    private final LinkedBlockingQueue<Runnable> taskQueue;
    private Thread taskThread;
    
    static {
        instance = new ModPackContext();
    }
    
    private ModPackContext() {
        this.storage = new ModPackStorage(new File(FileTools.DIR_PACK, "modpacks"), new File(FileTools.DIR_PACK, "modpacks-archive"), new File(FileTools.DIR_WORK));
        this.currentModPack = null;
        this.selectedListenerList = new ArrayList<ModPackSelectedListener>();
        this.deselectedListenerList = new ArrayList<ModPackDeselectedListener>();
        this.taskQueue = new LinkedBlockingQueue<Runnable>();
        this.taskThread = null;
    }
    
    public static ModPackContext getInstance() {
        return ModPackContext.instance;
    }
    
    public void addDeselectedListener(final ModPackDeselectedListener modPackDeselectedListener) {
        this.deselectedListenerList.add(modPackDeselectedListener);
    }
    
    public void addSelectedListener(final ModPackSelectedListener modPackSelectedListener) {
        this.selectedListenerList.add(modPackSelectedListener);
    }
    
    public ModPackJsAdapter assureJsAdapter() {
        this.assurePackSelected();
        return this.getCurrentModPack().getJsAdapter();
    }
    
    public void assurePackSelected() {
        if (this.getCurrentModPack() == null) {
            ModPackSelector.restoreSelected();
        }
    }
    
    public ModPack getCurrentModPack() {
        return this.currentModPack;
    }
    
    public ModPackStorage getStorage() {
        return this.storage;
    }
    
    public void queueTask(final Runnable runnable) {
        synchronized (this.taskQueue) {
            if (this.taskThread == null) {
                (this.taskThread = new Thread(new -$$Lambda$ModPackContext$t3c2vG6R7kQT6HyPXIINf91gF4w(this))).start();
            }
            try {
                this.taskQueue.put(runnable);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void setCurrentModPack(final ModPack currentModPack) {
        synchronized (this) {
            if (this.currentModPack == currentModPack) {
                return;
            }
            if (this.currentModPack != null) {
                final Iterator<ModPackDeselectedListener> iterator = this.deselectedListenerList.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onDeselected(this.currentModPack);
                }
            }
            (this.currentModPack = currentModPack).reloadAndValidateManifest();
            if (this.currentModPack != null) {
                final Iterator<ModPackSelectedListener> iterator2 = this.selectedListenerList.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().onSelected(this.currentModPack);
                }
            }
        }
    }
    
    public interface ModPackDeselectedListener
    {
        void onDeselected(final ModPack p0);
    }
    
    public interface ModPackSelectedListener
    {
        void onSelected(final ModPack p0);
    }
}
