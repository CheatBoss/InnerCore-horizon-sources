package com.zhekasmirnov.apparatus.multiplayer;

import com.android.tools.r8.annotations.*;
import java.util.function.*;

@SynthesizedClassMap({ -$$Lambda$NetworkThreadMarker$RMArfuq8Pe_tL3zsBatDn2xWGqg.class })
public class NetworkThreadMarker
{
    private static final NetworkThreadMarker singleton;
    private final ThreadLocal<Mark> threadMark;
    
    static {
        singleton = new NetworkThreadMarker();
    }
    
    private NetworkThreadMarker() {
        this.threadMark = new SuppliedThreadLocal<Mark>(-$$Lambda$NetworkThreadMarker$RMArfuq8Pe_tL3zsBatDn2xWGqg.INSTANCE);
    }
    
    public static void assertClientThread() {
        final Mark mark = NetworkThreadMarker.singleton.threadMark.get();
        if (mark != Mark.CLIENT && mark != Mark.NON_NETWORK) {
            throw new IllegalStateException("working with client stuff on server thread!");
        }
    }
    
    public static void assertServerThread() {
        final Mark mark = NetworkThreadMarker.singleton.threadMark.get();
        if (mark != Mark.SERVER && mark != Mark.NON_NETWORK) {
            throw new IllegalStateException("working with server stuff on client thread!");
        }
    }
    
    public static NetworkThreadMarker getSingleton() {
        return NetworkThreadMarker.singleton;
    }
    
    public static boolean isClientThread() {
        return NetworkThreadMarker.singleton.threadMark.get().equals(Mark.CLIENT);
    }
    
    public static boolean isServerThread() {
        return NetworkThreadMarker.singleton.threadMark.get().equals(Mark.SERVER);
    }
    
    public static void markThreadAs(final Mark mark) {
        NetworkThreadMarker.singleton.threadMark.set(mark);
    }
    
    public Mark getCurrentThreadMark() {
        return this.threadMark.get();
    }
    
    public enum Mark
    {
        CLIENT, 
        NON_NETWORK, 
        SERVER;
    }
    
    static final class SuppliedThreadLocal<T> extends ThreadLocal<T>
    {
        private final Supplier<? extends T> supplier;
        
        SuppliedThreadLocal(final Supplier<? extends T> supplier) {
            supplier.getClass();
            this.supplier = supplier;
        }
        
        @Override
        protected T initialValue() {
            return (T)this.supplier.get();
        }
    }
}
