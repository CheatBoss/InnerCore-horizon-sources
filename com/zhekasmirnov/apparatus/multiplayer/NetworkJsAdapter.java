package com.zhekasmirnov.apparatus.multiplayer;

import com.zhekasmirnov.apparatus.multiplayer.client.*;
import com.zhekasmirnov.apparatus.job.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;
import com.zhekasmirnov.apparatus.api.player.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;

public class NetworkJsAdapter
{
    private final Network network;
    
    public NetworkJsAdapter(final Network network) {
        this.network = network;
    }
    
    public void addClientInitializationPacket(final String s, final Network.ClientInitializationPacketSender clientInitializationPacketSender, final ConnectedClient.InitializationPacketListener initializationPacketListener) {
        this.network.addClientInitializationPacket(s, clientInitializationPacketSender, initializationPacketListener);
    }
    
    public void addClientPacket(final String s, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener) {
        this.network.addClientPacket(s, onPacketReceivedListener);
    }
    
    public void addClientPacket(final String s, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener, final JobExecutor jobExecutor) {
        this.network.addClientPacket(s, onPacketReceivedListener, jobExecutor);
    }
    
    public void addServerInitializationPacket(final String s, final Network.ServerInitializationPacketSender serverInitializationPacketSender, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener) {
        this.network.addServerInitializationPacket(s, serverInitializationPacketSender, onPacketReceivedListener);
    }
    
    public void addServerPacket(final String s, final ModdedServer.OnPacketReceivedListener onPacketReceivedListener) {
        this.network.addServerPacket(s, onPacketReceivedListener);
    }
    
    public void addServerPacket(final String s, final ModdedServer.OnPacketReceivedListener onPacketReceivedListener, final JobExecutor jobExecutor) {
        this.network.addServerPacket(s, onPacketReceivedListener, jobExecutor);
    }
    
    public ModdedClient getClient() {
        return this.network.getClient();
    }
    
    public ConnectedClient getClientForPlayer(final long n) {
        return this.network.getServer().getConnectedClientForPlayer(n);
    }
    
    public NativeArray getConnectedClients() {
        return ScriptableObjectHelper.createArray(this.network.getServer().getConnectedClients());
    }
    
    public NativeArray getConnectedPlayers() {
        return ScriptableObjectHelper.createArray(this.network.getServer().getConnectedPlayers());
    }
    
    public NetworkPlayerHandler getHandlerForPlayer(final long n) {
        return NetworkPlayerRegistry.getSingleton().getHandlerFor(n);
    }
    
    public Network getNetworkInstance() {
        return this.network;
    }
    
    public ModdedServer getServer() {
        return this.network.getServer();
    }
    
    public boolean inRemoteWorld() {
        return Minecraft.getGameState() == Minecraft.GameState.REMOTE_WORLD;
    }
    
    public int localToServerId(final int n) {
        return IdConversionMap.localToServer(n);
    }
    
    public void sendServerMessage(final String s) {
        NetworkThreadMarker.assertServerThread();
        this.network.getServer().sendMessageToAll(s);
    }
    
    public void sendToAllClients(final String s, final Object o) {
        NetworkThreadMarker.assertServerThread();
        this.network.getServer().sendToAll(s, o);
    }
    
    public void sendToServer(final String s, final Object o) {
        NetworkThreadMarker.assertClientThread();
        this.network.getClient().send(s, o);
    }
    
    public int serverToLocalId(final int n) {
        return IdConversionMap.serverToLocal(n);
    }
    
    public void throwInitializationPacketError(final String s) throws InitializationPacketException {
        throw new InitializationPacketException(s);
    }
}
