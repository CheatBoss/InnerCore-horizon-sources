package com.zhekasmirnov.apparatus.adapter.innercore.game;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.runtime.*;

@SynthesizedClassMap({ -$$Lambda$cr2aX42L8zZb3KkLnIRmO2KFjXE.class })
public class Minecraft
{
    private static boolean isLeaveGamePosted;
    private static GameState state;
    
    static {
        Minecraft.state = GameState.NON_WORLD;
        Minecraft.isLeaveGamePosted = false;
    }
    
    public static GameState getGameState() {
        return Minecraft.state;
    }
    
    public static void leaveGame() {
        Minecraft.state = GameState.NON_WORLD;
        if (NativeCallback.isLevelDisplayed()) {
            MainThreadQueue.localThread.enqueue(-$$Lambda$cr2aX42L8zZb3KkLnIRmO2KFjXE.INSTANCE);
            return;
        }
        Minecraft.isLeaveGamePosted = true;
    }
    
    public static void onConnectToHost(final String s, final int n) {
        Minecraft.isLeaveGamePosted = false;
        Minecraft.state = GameState.REMOTE_WORLD;
    }
    
    public static void onLevelDisplayed() {
        if (Minecraft.isLeaveGamePosted) {
            Minecraft.isLeaveGamePosted = false;
            MainThreadQueue.localThread.enqueue(-$$Lambda$cr2aX42L8zZb3KkLnIRmO2KFjXE.INSTANCE);
        }
    }
    
    public static void onLevelSelected() {
        Minecraft.isLeaveGamePosted = false;
        Minecraft.state = GameState.HOST_WORLD;
    }
    
    public enum GameState
    {
        HOST_WORLD, 
        NON_WORLD, 
        REMOTE_WORLD;
    }
}
