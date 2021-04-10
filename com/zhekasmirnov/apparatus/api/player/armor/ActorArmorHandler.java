package com.zhekasmirnov.apparatus.api.player.armor;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.job.*;

@SynthesizedClassMap({ -$$Lambda$ActorArmorHandler$JC4PCxIQb_a-jvZK0W4uQzMl2bo.class, -$$Lambda$ActorArmorHandler$dCAAWUHc4K8NJA3pz4m2jmRtJR8.class, -$$Lambda$ActorArmorHandler$x1kg4z_NrC4p0ufDckWq9Lp96sg.class, -$$Lambda$ActorArmorHandler$uYUMsDVSiwoNFT-h5EJszzw0Hk0.class })
public class ActorArmorHandler
{
    private static final Map<Integer, OnHurtListener> onHurtListenerMap;
    private static final Map<Integer, OnTakeOffListener> onTakeOffListenerMap;
    private static final Map<Integer, OnTakeOnListener> onTakeOnListenerMap;
    private static final Map<Integer, OnTickListener> onTickListenerMap;
    private final EntityActor actor;
    private final ItemStack[] armorItems;
    private final JobExecutor delayedExecutor;
    private final JobExecutor instantExecutor;
    
    static {
        onTickListenerMap = new HashMap<Integer, OnTickListener>();
        onHurtListenerMap = new HashMap<Integer, OnHurtListener>();
        onTakeOnListenerMap = new HashMap<Integer, OnTakeOnListener>();
        onTakeOffListenerMap = new HashMap<Integer, OnTakeOffListener>();
    }
    
    public ActorArmorHandler(final EntityActor actor) {
        this.armorItems = new ItemStack[4];
        this.instantExecutor = Network.getSingleton().getInstantJobExecutor();
        this.delayedExecutor = Network.getSingleton().getServerThreadJobExecutor();
        this.actor = actor;
        for (int i = 0; i < 4; ++i) {
            this.armorItems[i] = new ItemStack();
        }
    }
    
    public static void registerOnHurtListener(final int n, final OnHurtListener onHurtListener) {
        ActorArmorHandler.onHurtListenerMap.put(n, onHurtListener);
    }
    
    public static void registerOnTakeOffListener(final int n, final OnTakeOffListener onTakeOffListener) {
        ActorArmorHandler.onTakeOffListenerMap.put(n, onTakeOffListener);
    }
    
    public static void registerOnTakeOnListener(final int n, final OnTakeOnListener onTakeOnListener) {
        ActorArmorHandler.onTakeOnListenerMap.put(n, onTakeOnListener);
    }
    
    public static void registerOnTickListener(final int n, final OnTickListener onTickListener) {
        ActorArmorHandler.onTickListenerMap.put(n, onTickListener);
    }
    
    public void onHurt(final long n, final int n2, final int n3, final boolean b, final boolean b2) {
        final long uid = this.actor.getUid();
        for (int i = 0; i < 4; ++i) {
            final ItemStack armorSlot = this.actor.getArmorSlot(i);
            final OnHurtListener onHurtListener = ActorArmorHandler.onHurtListenerMap.get(armorSlot.id);
            if (onHurtListener != null) {
                this.instantExecutor.add(new -$$Lambda$ActorArmorHandler$JC4PCxIQb_a-jvZK0W4uQzMl2bo(this, onHurtListener, armorSlot, i, uid, n2, n3, n, b, b2));
            }
        }
    }
    
    public void onTick() {
        final long uid = this.actor.getUid();
        for (int i = 0; i < 4; ++i) {
            final ItemStack armorSlot = this.actor.getArmorSlot(i);
            final ItemStack itemStack = this.armorItems[i];
            if (armorSlot.id != itemStack.id) {
                this.instantExecutor.add(new -$$Lambda$ActorArmorHandler$x1kg4z_NrC4p0ufDckWq9Lp96sg(itemStack, i, uid, armorSlot));
            }
            final OnTickListener onTickListener = ActorArmorHandler.onTickListenerMap.get(armorSlot.id);
            if (onTickListener != null) {
                this.instantExecutor.add(new -$$Lambda$ActorArmorHandler$dCAAWUHc4K8NJA3pz4m2jmRtJR8(this, onTickListener, armorSlot, i, uid));
            }
            this.armorItems[i] = armorSlot;
        }
    }
    
    public interface OnHurtListener
    {
        Object onHurt(final ItemStack p0, final int p1, final long p2, final int p3, final int p4, final long p5, final boolean p6, final boolean p7);
    }
    
    public interface OnTakeOffListener
    {
        void onTakeOff(final ItemStack p0, final int p1, final long p2);
    }
    
    public interface OnTakeOnListener
    {
        void onTakeOn(final ItemStack p0, final int p1, final long p2);
    }
    
    public interface OnTickListener
    {
        Object onTick(final ItemStack p0, final int p1, final long p2);
    }
}
