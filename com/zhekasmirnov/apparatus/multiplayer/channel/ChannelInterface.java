package com.zhekasmirnov.apparatus.multiplayer.channel;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import org.json.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.codec.*;
import java.util.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;

@SynthesizedClassMap({ -$$Lambda$ChannelInterface$Wt2VPAsx5cLUAuNOiO9vZ0jLZP4.class, -$$Lambda$ChannelInterface$jJEufG0Dxzj8RtDcsJUgu2csnW4.class, -$$Lambda$ChannelInterface$eChXQEP38OoB72Gr9RP11Y3qKdQ.class })
public class ChannelInterface
{
    private final DataChannel channel;
    private final Map<Class<?>, ChannelCodec<?>> codecMap;
    private final List<OnPacketReceivedListener> listeners;
    
    public ChannelInterface(final DataChannel channel) {
        this.listeners = new ArrayList<OnPacketReceivedListener>();
        this.codecMap = new HashMap<Class<?>, ChannelCodec<?>>();
        this.channel = channel;
        this.getCodec(String.class);
        this.getCodec(JSONObject.class);
        this.getCodec(Scriptable.class);
    }
    
    public void addListener(final OnPacketReceivedListener onPacketReceivedListener) {
        this.listeners.add(onPacketReceivedListener);
    }
    
    public void close() {
        this.channel.close();
    }
    
    public DataChannel getChannel() {
        return this.channel;
    }
    
    public <T> ChannelCodec<T> getCodec(final Class<T> clazz) {
        final Class<?> codecClass = ChannelCodecFactory.toCodecClass(clazz);
        return Java8BackComp.computeIfAbsent((Map<Class<?>, ChannelCodec<T>>)this.codecMap, codecClass, new -$$Lambda$ChannelInterface$jJEufG0Dxzj8RtDcsJUgu2csnW4(this, codecClass));
    }
    
    public boolean isClosed() {
        return this.channel.isClosed();
    }
    
    public void listenerLoop() {
        this.channel.listenerLoop();
    }
    
    public void send(final String s, final Object o) {
        this.getCodec(o.getClass()).sendUntyped(s, o);
    }
    
    public <T> void send(final String s, final T t, final Class<T> clazz) {
        this.getCodec(clazz).send(s, t);
    }
    
    public void shutdownAndAwaitDisconnect() {
        this.channel.shutdownAndAwaitDisconnect();
    }
    
    public void shutdownAndAwaitDisconnect(final int n) {
        this.shutdownAndAwaitDisconnect();
        new Thread(new -$$Lambda$ChannelInterface$eChXQEP38OoB72Gr9RP11Y3qKdQ(this, n)).start();
    }
    
    public interface OnPacketReceivedListener
    {
        void onPacketReceived(final String p0, final Object p1, final Class<?> p2);
    }
}
