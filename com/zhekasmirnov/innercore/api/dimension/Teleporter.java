package com.zhekasmirnov.innercore.api.dimension;

import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;

public class Teleporter
{
    public static final Teleporter OVERWORLD;
    public static final int STATE_COMPLETE = 3;
    public static final int STATE_IDLE = 0;
    public static final int STATE_QUEUED = 1;
    public static final int STATE_RUNNING = 2;
    private ITeleporterCallbacks callbacks;
    public CustomDimension dimension;
    private float loadingProgress;
    private int loadingRadius;
    public Region region;
    private int state;
    private float targetX;
    private float targetY;
    private float targetZ;
    private Container uiContainer;
    private IWindow uiScreen;
    
    static {
        OVERWORLD = new Teleporter(null);
    }
    
    public Teleporter(final CustomDimension dimension) {
        this.state = 0;
        this.targetX = 0.0f;
        this.targetY = 0.0f;
        this.targetZ = 0.0f;
        this.loadingRadius = 3;
        this.loadingProgress = 0.0f;
        this.uiContainer = new Container();
        this.dimension = dimension;
        if (dimension != null) {
            this.region = dimension.getRegion();
            return;
        }
        this.region = Region.OVERWORLD;
    }
    
    private int findSurfaceCustom(int n, int n2, final int n3) {
        final boolean b = NativeAPI.getTile(n, n2, n3) > 0;
        int n4;
        if (b) {
            n4 = 1;
        }
        else {
            n4 = -1;
        }
        while (n2 >= 0 && NativeAPI.getTile(n, n2, n3) > 0 == b) {
            n2 += n4;
        }
        n = n2;
        if (b) {
            n = n2 - 1;
        }
        return n;
    }
    
    private void lockTargetPosition(final boolean b) {
        NativeAPI.teleportTo(NativeAPI.getPlayer(), this.targetX, this.targetY, this.targetZ);
        NativeAPI.setImmobile(NativeAPI.getPlayer(), b);
    }
    
    public boolean enter() {
        if (NativeAPI.getDimension() != 0) {
            ICLog.i("ERROR", "Teleporter cannot call enter() in Nether or End dimensions");
            return false;
        }
        TeleportationHandler.enqueueTeleportation(this);
        return true;
    }
    
    void finish() {
        this.targetX = (float)Math.floor(this.targetX);
        this.targetZ = (float)Math.floor(this.targetZ);
        this.targetY = (float)this.findSurfaceCustom((int)this.targetX, 128, (int)this.targetZ);
        if (this.targetY <= 4.0f) {
            this.targetY = 256.0f;
        }
        this.targetX += 0.5f;
        this.targetY += 1.65f;
        this.targetZ += 0.5f;
        if (this.callbacks != null) {
            this.callbacks.onComplete(this, this.targetX, this.targetY, this.targetZ);
        }
        this.lockTargetPosition(false);
        if (this.uiScreen != null) {
            this.uiContainer.close();
        }
    }
    
    public int getState() {
        return this.state;
    }
    
    public Container getUiContainer() {
        return this.uiContainer;
    }
    
    boolean handle() {
        final int n = (int)Math.pow(this.loadingRadius * 2 + 1, 2.0);
        int n2 = 0;
        final int n3 = (int)Math.floor(this.targetX / 16.0);
        final int n4 = (int)Math.floor(this.targetZ / 16.0);
        for (int i = -this.loadingRadius; i <= this.loadingRadius; ++i) {
            int n5;
            for (int j = -this.loadingRadius; j <= this.loadingRadius; ++j, n2 = n5) {
                n5 = n2;
                if (NativeAPI.isChunkLoaded(n3 + i, n4 + j)) {
                    n5 = n2 + 1;
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("dimension loading progress: ");
        sb.append(n2);
        sb.append("/");
        sb.append(n);
        ICLog.d("DEBUG", sb.toString());
        this.loadingProgress = n2 / (float)n;
        if (this.callbacks != null) {
            this.callbacks.onHandle(this, this.loadingProgress);
        }
        if (this.uiScreen != null) {
            this.uiContainer.setScale("loadingProgress", this.loadingProgress);
        }
        return n2 == n;
    }
    
    public boolean isIdle() {
        return this.state == 0 || this.state == 3;
    }
    
    public boolean isRunning() {
        return this.state == 2;
    }
    
    public boolean isWaiting() {
        return this.state == 1;
    }
    
    public void setCallbacks(final ITeleporterCallbacks callbacks) {
        this.callbacks = callbacks;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public void setTargetPosition(final float targetX, final float targetY, final float targetZ) {
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }
    
    public void setUiScreen(final IWindow uiScreen) {
        this.uiScreen = uiScreen;
    }
    
    boolean start() {
        if (NativeAPI.getDimension() != 0) {
            ICLog.i("ERROR", "Teleporter cannot call start() in Nether or End dimensions");
            return false;
        }
        if (this.uiScreen != null) {
            this.uiContainer.openAs(this.uiScreen);
        }
        final float[] array = new float[3];
        NativeAPI.getPosition(NativeAPI.getPlayer(), array);
        final int n = (int)array[0];
        final int n2 = (int)array[2];
        final Region region = Region.getRegionAt(n, n2);
        this.targetX = (float)((this.region.regionX - region.regionX) * 200000 + n);
        this.targetZ = (float)((this.region.regionZ - region.regionZ) * 200000 + n2);
        this.targetY = 257.63f;
        DimensionRegistry.setCurrentCustomDimension(this.dimension);
        this.lockTargetPosition(true);
        this.loadingProgress = 0.0f;
        if (this.callbacks != null) {
            this.callbacks.onStart(this);
        }
        return true;
    }
    
    public interface ITeleporterCallbacks
    {
        void onComplete(final Teleporter p0, final float p1, final float p2, final float p3);
        
        void onHandle(final Teleporter p0, final float p1);
        
        void onStart(final Teleporter p0);
    }
}
