package com.zhekasmirnov.innercore.api.mod.ui.window;

import com.zhekasmirnov.innercore.api.mod.util.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.utils.*;

public class WindowProvider
{
    private static final int backPressEventTimeout = 250;
    private static long frame;
    public static final WindowProvider instance;
    private Thread currentThread;
    private int currentThreadId;
    private boolean isThreadRunning;
    private long lastBackPressEvent;
    private final ArrayList<IWindow> openedWindows;
    
    static {
        instance = new WindowProvider();
        WindowProvider.frame = 0L;
    }
    
    public WindowProvider() {
        this.currentThread = null;
        this.currentThreadId = 0;
        this.isThreadRunning = false;
        this.openedWindows = new ArrayList<IWindow>();
        this.lastBackPressEvent = 0L;
    }
    
    public static long getFrame() {
        return WindowProvider.frame;
    }
    
    private void refreshCurrentState() {
        if (this.openedWindows.size() == 0) {
            if (this.isThreadRunning) {
                this.stopThread();
            }
        }
        else if (!this.isThreadRunning) {
            this.startThread();
        }
        InventorySource.isUpdating = false;
        final Iterator<IWindow> iterator = this.openedWindows.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isInventoryNeeded()) {
                InventorySource.isUpdating = true;
            }
        }
    }
    
    private void startThread() {
        this.isThreadRunning = true;
        ++this.currentThreadId;
        (this.currentThread = new Thread(new ThreadRunnable(this))).start();
    }
    
    private void stopThread() {
        this.isThreadRunning = false;
        ++this.currentThreadId;
    }
    
    public void onActivityStopped() {
        final Iterator<IWindow> iterator = new ArrayList<IWindow>(this.openedWindows).iterator();
        while (iterator.hasNext()) {
            iterator.next().onBackPressed();
        }
    }
    
    public boolean onBackPressed() {
        if (System.currentTimeMillis() < this.lastBackPressEvent + 250L) {
            return false;
        }
        this.lastBackPressEvent = System.currentTimeMillis();
        final Iterator<IWindow> iterator = new ArrayList<IWindow>(this.openedWindows).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().onBackPressed()) {
                NativeAPI.preventPendingKeyEvent(0, 250);
                return true;
            }
        }
        return false;
    }
    
    public void onWindowClosed(final IWindow window) {
        if (this.openedWindows.contains(window)) {
            synchronized (this.openedWindows) {
                this.openedWindows.remove(window);
                this.refreshCurrentState();
            }
        }
    }
    
    public void onWindowOpened(final IWindow window) {
        if (!this.openedWindows.contains(window)) {
            synchronized (this.openedWindows) {
                this.openedWindows.add(0, window);
                this.refreshCurrentState();
            }
        }
    }
    
    private class ThreadRunnable implements Runnable
    {
        private int runId;
        private WindowProvider windowProvider;
        
        private ThreadRunnable(final WindowProvider windowProvider) {
            this.windowProvider = windowProvider;
            this.runId = windowProvider.currentThreadId;
        }
        
        private void setupThreadAsUI() {
            Compiler.assureContextForCurrentThread();
            ICLog.setupEventHandlerForCurrentThread(new UIEventHandler());
        }
        
        @Override
        public void run() {
            this.setupThreadAsUI();
            UIUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ThreadRunnable.this.setupThreadAsUI();
                }
            });
        Label_0046_Outer:
            while (this.runId == this.windowProvider.currentThreadId) {
                UIUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final long currentTimeMillis = System.currentTimeMillis();
                        synchronized (WindowProvider.this.openedWindows) {
                            final Iterator<IWindow> iterator = (Iterator<IWindow>)WindowProvider.this.openedWindows.iterator();
                            while (iterator.hasNext()) {
                                iterator.next().frame(currentTimeMillis);
                            }
                            // monitorexit(WindowProvider.access$300(this.this$1.this$0))
                            WindowProvider.frame = WindowProvider.frame;
                        }
                    }
                });
                while (true) {
                    try {
                        Thread.sleep(15L);
                        continue Label_0046_Outer;
                    }
                    catch (InterruptedException ex) {
                        continue;
                    }
                    break;
                }
                break;
            }
        }
    }
}
