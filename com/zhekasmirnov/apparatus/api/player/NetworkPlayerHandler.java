package com.zhekasmirnov.apparatus.api.player;

import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.apparatus.api.player.armor.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

public class NetworkPlayerHandler
{
    private final EntityActor actor;
    private final ActorArmorHandler armorHandler;
    private int dimensionId;
    private final JobExecutor instantExecutor;
    private boolean isInitialized;
    private boolean isTickCallbackDisabled;
    private final long playerUid;
    
    public NetworkPlayerHandler(final long playerUid) {
        this.instantExecutor = Network.getSingleton().getInstantJobExecutor();
        this.isInitialized = false;
        this.isTickCallbackDisabled = false;
        this.dimensionId = 0;
        this.playerUid = playerUid;
        this.actor = new EntityActor(playerUid);
        this.armorHandler = new ActorArmorHandler(this.actor);
    }
    
    private void initialize() {
        Callback.invokeAPICallback("ServerPlayerLoaded", this.actor.getUid());
        this.onChangeDimension(this.dimensionId = this.actor.getDimension(), this.dimensionId);
    }
    
    public EntityActor getActor() {
        return this.actor;
    }
    
    public ConnectedClient getAssociatedClient() {
        return Network.getSingleton().getServer().getConnectedClientForPlayer(this.playerUid);
    }
    
    public boolean isPlayerDead() {
        return this.actor.getHealth() <= 0;
    }
    
    public void onChangeDimension(final int dimensionId, final int n) {
        this.dimensionId = dimensionId;
        Callback.invokeAPICallback("PlayerChangedDimension", this.actor.getUid(), dimensionId, n);
    }
    
    public void onEat(final int n, final float n2) {
    }
    
    public void onHurt(final long n, final int n2, final int n3, final boolean b, final boolean b2) {
        this.armorHandler.onHurt(n, n2, n3, b, b2);
    }
    
    public void onTick() {
        final boolean playerDead = this.isPlayerDead();
        if (!playerDead) {
            if (!this.isInitialized) {
                this.initialize();
                this.isInitialized = true;
            }
            final int dimension = this.actor.getDimension();
            if (dimension != this.dimensionId) {
                this.onChangeDimension(dimension, this.dimensionId);
            }
        }
        if (!this.isTickCallbackDisabled) {
            try {
                Callback.invokeCallback("ServerPlayerTick", this.actor.getUid(), playerDead);
            }
            catch (Throwable t) {
                this.isTickCallbackDisabled = true;
                final StringBuilder sb = new StringBuilder();
                sb.append("Fatal error occurred in ticking callback for player entity ");
                sb.append(this.actor.getUid());
                sb.append(", ticking callback will be disabled for this player, until re-entering the world.");
                UserDialog.dialog("FATAL ERROR", sb.toString(), t, false);
            }
        }
        this.armorHandler.onTick();
    }
}
