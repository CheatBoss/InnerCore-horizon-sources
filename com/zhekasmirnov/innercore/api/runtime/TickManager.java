package com.zhekasmirnov.innercore.api.runtime;

import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.api.runtime.saver.world.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;

public class TickManager
{
    private static final Object[] EMPTY_ARGS;
    private static String FATAL_ERROR_MESSAGE;
    private static TickManager currentThread;
    private static long debugTickTime;
    public static int globalTickCounter;
    private static boolean isCurrentTreadStopped;
    private static Throwable lastFatalError;
    private boolean isRunningNow;
    private boolean pause;
    private boolean running;
    private ArrayList<Tick> tickQueue;
    public int time;
    
    static {
        TickManager.FATAL_ERROR_MESSAGE = "Fatal error occurred in mods thread, all mods are stopped. To resume you must re-enter the world.";
        TickManager.isCurrentTreadStopped = false;
        TickManager.lastFatalError = null;
        TickManager.globalTickCounter = 0;
        TickManager.debugTickTime = 0L;
        EMPTY_ARGS = new Object[0];
    }
    
    public TickManager() {
        this.running = false;
        this.pause = false;
        this.isRunningNow = false;
        this.time = 0;
        this.tickQueue = new ArrayList<Tick>();
    }
    
    private void callTick(final Tick tick) {
        if (tick == null) {
            ++this.time;
        }
        else {
            this.time = tick.time;
        }
        ++TickManager.globalTickCounter;
        InventorySource.tick();
        ArmorRegistry.onTick();
        WorldDataSaverHandler.getInstance().onTick();
        final TickExecutor instance = TickExecutor.getInstance();
        if (instance.isAvailable()) {
            instance.execute(Callback.getCallbackAsRunnableList("tick", TickManager.EMPTY_ARGS));
        }
        else {
            Callback.invokeAPICallbackUnsafe("tick", TickManager.EMPTY_ARGS);
        }
        Updatable.getForServer().onTick();
        instance.blockUntilExecuted();
        Updatable.getForServer().onPostTick();
    }
    
    public static void clearLastFatalError() {
        TickManager.lastFatalError = null;
    }
    
    public static Throwable getLastFatalError() {
        return TickManager.lastFatalError;
    }
    
    public static int getTime() {
        if (TickManager.currentThread != null) {
            return TickManager.currentThread.time;
        }
        return TickManager.globalTickCounter;
    }
    
    public static boolean isPaused() {
        return TickManager.currentThread != null && TickManager.currentThread.pause;
    }
    
    public static boolean isRunningNow() {
        return TickManager.currentThread != null && TickManager.currentThread.isRunningNow;
    }
    
    public static boolean isStopped() {
        return TickManager.isCurrentTreadStopped;
    }
    
    public static void nativeTick() {
        if (TickManager.currentThread == null) {
            return;
        }
        if (TickManager.currentThread.running && !TickManager.currentThread.pause) {
            try {
                final long currentTimeMillis = System.currentTimeMillis();
                TickManager.currentThread.callTick(next());
                TickManager.debugTickTime += System.currentTimeMillis() - currentTimeMillis;
            }
            catch (Throwable t) {
                reportFatalError(t);
                stop();
            }
        }
    }
    
    public static void pause() {
        setPaused(true);
    }
    
    private static void reportFatalError(final Throwable t) {
        ICLog.e("THREADING", "error occurred in ticking thread, it will be stopped for current session", t);
        DialogHelper.reportFatalError(TickManager.FATAL_ERROR_MESSAGE, t);
    }
    
    public static void resume() {
        setPaused(false);
    }
    
    public static void setPaused(final boolean pause) {
        if (TickManager.currentThread != null) {
            TickManager.currentThread.pause = pause;
        }
    }
    
    public static void setupAndStart() {
        stop();
        (TickManager.currentThread = new TickManager()).prepare();
        TickManager.globalTickCounter = 0;
        TickManager.currentThread.running = true;
        TickManager.currentThread.pause = false;
        TickManager.lastFatalError = null;
        TickManager.isCurrentTreadStopped = false;
        ICLog.d("THREADING", "ticking thread started");
    }
    
    public static void stop() {
        if (TickManager.currentThread != null) {
            TickManager.currentThread.running = false;
            TickManager.currentThread.tickQueue.clear();
            TickManager.currentThread = null;
            TickManager.isCurrentTreadStopped = true;
        }
    }
    
    public void prepare() {
        this.tickQueue.clear();
        resetTickCounter();
    }
    
    public static class Tick
    {
        private static int tickCounter;
        private final int time;
        
        static {
            Tick.tickCounter = 0;
        }
        
        private Tick(final int time) {
            this.time = time;
        }
        
        private static Tick next() {
            final int tickCounter = Tick.tickCounter;
            Tick.tickCounter = tickCounter + 1;
            return new Tick(tickCounter);
        }
        
        private static void resetTickCounter() {
            Tick.tickCounter = 0;
        }
    }
}
