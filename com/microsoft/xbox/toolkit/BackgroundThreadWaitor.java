package com.microsoft.xbox.toolkit;

import java.util.*;
import android.os.*;

public class BackgroundThreadWaitor
{
    private static BackgroundThreadWaitor instance;
    private BackgroundThreadWaitorChangedCallback blockingChangedCallback;
    private Hashtable<WaitType, WaitObject> blockingTable;
    private Ready waitReady;
    private ArrayList<Runnable> waitingRunnables;
    
    static {
        BackgroundThreadWaitor.instance = new BackgroundThreadWaitor();
    }
    
    public BackgroundThreadWaitor() {
        this.waitReady = new Ready();
        this.blockingTable = new Hashtable<WaitType, WaitObject>();
        this.blockingChangedCallback = null;
        this.waitingRunnables = new ArrayList<Runnable>();
    }
    
    private void drainWaitingRunnables() {
        XLEAssert.assertTrue(ThreadManager.UIThread == Thread.currentThread());
        final Iterator<Runnable> iterator = this.waitingRunnables.iterator();
        while (iterator.hasNext()) {
            iterator.next().run();
        }
        this.waitingRunnables.clear();
    }
    
    public static BackgroundThreadWaitor getInstance() {
        if (BackgroundThreadWaitor.instance == null) {
            BackgroundThreadWaitor.instance = new BackgroundThreadWaitor();
        }
        return BackgroundThreadWaitor.instance;
    }
    
    private void updateWaitReady() {
        final Thread uiThread = ThreadManager.UIThread;
        final Thread currentThread = Thread.currentThread();
        final boolean b = false;
        XLEAssert.assertTrue(uiThread == currentThread);
        final HashSet<WaitType> set = new HashSet<WaitType>();
        final EnumSet<WaitType> none = EnumSet.noneOf(WaitType.class);
        final Enumeration<WaitObject> elements = this.blockingTable.elements();
        while (elements.hasMoreElements()) {
            final WaitObject waitObject = elements.nextElement();
            if (waitObject.isExpired()) {
                set.add(waitObject.type);
            }
            else {
                none.add(waitObject.type);
            }
        }
        final Iterator<WaitType> iterator = set.iterator();
        while (iterator.hasNext()) {
            this.blockingTable.remove(iterator.next());
        }
        boolean b2;
        if (this.blockingTable.size() == 0) {
            this.waitReady.setReady();
            this.drainWaitingRunnables();
            b2 = b;
        }
        else {
            this.waitReady.reset();
            b2 = true;
        }
        final BackgroundThreadWaitorChangedCallback blockingChangedCallback = this.blockingChangedCallback;
        if (blockingChangedCallback != null) {
            blockingChangedCallback.run(none, b2);
        }
    }
    
    public void clearBlocking(final WaitType waitType) {
        XLEAssert.assertTrue(ThreadManager.UIThread == Thread.currentThread());
        this.blockingTable.remove(waitType);
        this.updateWaitReady();
    }
    
    public boolean isBlocking() {
        return this.waitReady.getIsReady() ^ true;
    }
    
    public void postRunnableAfterReady(final Runnable runnable) {
        XLEAssert.assertTrue(ThreadManager.UIThread == Thread.currentThread());
        if (runnable == null) {
            return;
        }
        if (!this.isBlocking()) {
            runnable.run();
            return;
        }
        this.waitingRunnables.add(runnable);
    }
    
    public void setBlocking(final WaitType waitType, final int n) {
        XLEAssert.assertTrue(ThreadManager.UIThread == Thread.currentThread());
        this.blockingTable.put(waitType, new WaitObject(waitType, n));
        this.updateWaitReady();
    }
    
    public void setChangedCallback(final BackgroundThreadWaitorChangedCallback blockingChangedCallback) {
        this.blockingChangedCallback = blockingChangedCallback;
    }
    
    public void waitForReady(final int n) {
        XLEAssert.assertTrue(ThreadManager.UIThread != Thread.currentThread());
        ThreadManager.UIThreadPost(new Runnable() {
            @Override
            public void run() {
                BackgroundThreadWaitor.this.updateWaitReady();
            }
        });
        this.waitReady.waitForReady(n);
    }
    
    public interface BackgroundThreadWaitorChangedCallback
    {
        void run(final EnumSet<WaitType> p0, final boolean p1);
    }
    
    private class WaitObject
    {
        private long expires;
        private WaitType type;
        
        public WaitObject(final WaitType type, final long n) {
            this.type = type;
            this.expires = SystemClock.uptimeMillis() + n;
        }
        
        public boolean isExpired() {
            return this.expires < SystemClock.uptimeMillis();
        }
    }
    
    public enum WaitType
    {
        ApplicationBar, 
        ListLayout, 
        ListScroll, 
        Navigation, 
        PivotScroll;
    }
}
