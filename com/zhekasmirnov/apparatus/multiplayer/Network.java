package com.zhekasmirnov.apparatus.multiplayer;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.client.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.job.*;
import com.zhekasmirnov.apparatus.multiplayer.server.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.*;
import com.zhekasmirnov.apparatus.mcpe.*;
import java.io.*;
import java.net.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;

@SynthesizedClassMap({ -$$Lambda$Network$RzNqTDgdS2FR-ZWgzgwsvPijj9g.class, -$$Lambda$Network$KGVl37o3ymKn0CV7b8ysFgbxG54.class, -$$Lambda$Network$shAlZ_CFBV9TuCfzwz85rhOI-4M.class, -$$Lambda$Network$q_rSX6S5GW2QQ9_OzL_NoM7srkI.class, -$$Lambda$Network$cahfn9tRJFTNLuEVhclt86qcSfA.class, -$$Lambda$Network$UCdPpLis7fs5YNA1wZvrdKkwaBU.class, -$$Lambda$Network$11Yyx_niqKsifdz2YQTY3JA3EhQ.class, -$$Lambda$Network$ZLH2VoXez7UIcwK5idpINdqq2GQ.class, -$$Lambda$Network$6ts9oGjnTes7YmYPcLI5_b_tZTE.class, -$$Lambda$Network$WkZUb2LsUxHql83Gh5eTy_vmVa8.class, -$$Lambda$Network$wP1ADAhmfhFfZHTW7YcuRmbFqcU.class, -$$Lambda$Network$N71W0HU_AyXs155o_OCSZP9X0zA.class, -$$Lambda$Network$T1a0VbqF80HrQdXvbsdPbE7XZFI.class, -$$Lambda$Network$_os911ntZ8Thajykk2NvbP5gjic.class, -$$Lambda$Network$7-T-qnCeQXGWbYyTVZzP3wwAxuA.class, -$$Lambda$Network$QLnkHa6RvGZGI5JYOKnnbzbcCQQ.class, -$$Lambda$Network$x4iNsO3FMLUtJapl4giJ6V4NV_U.class, -$$Lambda$Network$5BQD8b8jv6-ZFHEBtcZIGHMIJ08.class })
public class Network
{
    private static final Network singleton;
    private final ModdedClient client;
    private final JobExecutor clientThreadJobExecutor;
    private final NetworkConfig config;
    private final JobExecutor instantJobExecutor;
    private boolean isRunningLanServer;
    private final ModdedServer server;
    private final JobExecutor serverThreadJobExecutor;
    
    static {
        singleton = new Network();
    }
    
    private Network() {
        this.config = new NetworkConfig();
        this.server = new ModdedServer(this.config);
        this.client = new ModdedClient(this.config);
        this.isRunningLanServer = false;
        this.instantJobExecutor = new InstantJobExecutor("Default Instant Network Executor");
        this.serverThreadJobExecutor = new MainThreadJobExecutor(MainThreadQueue.serverThread, "Default Server Network Executor");
        this.clientThreadJobExecutor = new MainThreadJobExecutor(MainThreadQueue.localThread, "Default Client Network Executor");
        this.client.addOnRequestingConnectionListener((ModdedClient.OnRequestingConnectionListener)-$$Lambda$Network$7-T-qnCeQXGWbYyTVZzP3wwAxuA.INSTANCE);
        this.client.addOnConnectedListener((ModdedClient.OnConnectedListener)-$$Lambda$Network$UCdPpLis7fs5YNA1wZvrdKkwaBU.INSTANCE);
        this.client.addOnDisconnectedListener((ModdedClient.OnDisconnectedListener)new -$$Lambda$Network$KGVl37o3ymKn0CV7b8ysFgbxG54(this));
        this.server.addOnClientConnectionRequestedListener((ModdedServer.OnClientConnectionRequestedListener)-$$Lambda$Network$q_rSX6S5GW2QQ9_OzL_NoM7srkI.INSTANCE);
        this.server.addOnClientConnectedListener((ModdedServer.OnClientConnectedListener)-$$Lambda$Network$6ts9oGjnTes7YmYPcLI5_b_tZTE.INSTANCE);
        this.server.addOnClientDisconnectedListener((ModdedServer.OnClientDisconnectedListener)-$$Lambda$Network$_os911ntZ8Thajykk2NvbP5gjic.INSTANCE);
    }
    
    public static Network getSingleton() {
        return Network.singleton;
    }
    
    private DataChannel tryOpenClientToServerNativeDataChannel(final int n) throws IOException {
        final NativeDataChannel nativeDataChannel = new NativeDataChannel(NativeNetworking.getOrCreateClientToServerChannel());
        if (!nativeDataChannel.pingPong(n)) {
            throw new IOException("failed to open modded native channel");
        }
        return nativeDataChannel;
    }
    
    private DataChannel tryOpenClientToServerSocketDataChannel(final String s, int clientProtocolId) throws IOException {
        final Socket socket = new Socket(s, clientProtocolId);
        clientProtocolId = this.config.getClientProtocolId();
        socket.getOutputStream().write(clientProtocolId);
        return DataChannelFactory.newDataChannel(socket, clientProtocolId);
    }
    
    public void addClientInitializationPacket(final String s, final ClientInitializationPacketSender clientInitializationPacketSender, final ConnectedClient.InitializationPacketListener initializationPacketListener) {
        final ModdedClient client = this.getClient();
        client.addOnRequestingConnectionListener((ModdedClient.OnRequestingConnectionListener)new -$$Lambda$Network$ZLH2VoXez7UIcwK5idpINdqq2GQ(client, s, clientInitializationPacketSender));
        this.getServer().addUntypedInitializationPacketListener(s, initializationPacketListener);
    }
    
    public <T> void addClientInitializationPacket(final String s, final ClientInitializationPacketSender clientInitializationPacketSender, final ModdedServer.TypedInitializationPacketListener<T> typedInitializationPacketListener) {
        final ModdedClient client = this.getClient();
        client.addOnRequestingConnectionListener((ModdedClient.OnRequestingConnectionListener)new -$$Lambda$Network$wP1ADAhmfhFfZHTW7YcuRmbFqcU(client, s, clientInitializationPacketSender));
        this.getServer().addInitializationPacketListener(s, (ModdedServer.TypedInitializationPacketListener<Object>)typedInitializationPacketListener);
    }
    
    public void addClientPacket(final String s, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener) {
        this.addClientPacket(s, onPacketReceivedListener, this.getClientThreadJobExecutor());
    }
    
    public void addClientPacket(final String s, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener, final JobExecutor jobExecutor) {
        if (jobExecutor != null) {
            this.getClient().addUntypedPacketReceivedListener(s, (ModdedClient.OnPacketReceivedListener)new -$$Lambda$Network$T1a0VbqF80HrQdXvbsdPbE7XZFI(jobExecutor, onPacketReceivedListener));
            return;
        }
        this.getClient().addUntypedPacketReceivedListener(s, onPacketReceivedListener);
    }
    
    public <T> void addClientPacket(final String s, final ModdedClient.TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.addClientPacket(s, typedOnPacketReceivedListener, this.getClientThreadJobExecutor());
    }
    
    public <T> void addClientPacket(final String s, final ModdedClient.TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener, final JobExecutor jobExecutor) {
        if (jobExecutor != null) {
            this.getClient().addPacketReceivedListener(s, (ModdedClient.TypedOnPacketReceivedListener<Object>)new -$$Lambda$Network$5BQD8b8jv6-ZFHEBtcZIGHMIJ08(jobExecutor, typedOnPacketReceivedListener));
            return;
        }
        this.getClient().addPacketReceivedListener(s, (ModdedClient.TypedOnPacketReceivedListener<Object>)typedOnPacketReceivedListener);
    }
    
    public void addClientShutdownListener(final ModdedClient.OnDisconnectedListener onDisconnectedListener) {
        this.getClient().addOnDisconnectedListener(onDisconnectedListener);
    }
    
    public void addServerInitializationPacket(final String s, final ServerInitializationPacketSender serverInitializationPacketSender, final ModdedClient.OnPacketReceivedListener onPacketReceivedListener) {
        this.getServer().addOnClientConnectionRequestedListener((ModdedServer.OnClientConnectionRequestedListener)new -$$Lambda$Network$11Yyx_niqKsifdz2YQTY3JA3EhQ(s, serverInitializationPacketSender));
        this.getClient().addUntypedInitializationPacketListener(s, onPacketReceivedListener);
    }
    
    public <T> void addServerInitializationPacket(final String s, final ServerInitializationPacketSender serverInitializationPacketSender, final ModdedClient.TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.getServer().addOnClientConnectionRequestedListener((ModdedServer.OnClientConnectionRequestedListener)new -$$Lambda$Network$N71W0HU_AyXs155o_OCSZP9X0zA(s, serverInitializationPacketSender));
        this.getClient().addInitializationPacketListener(s, (ModdedClient.TypedOnPacketReceivedListener<Object>)typedOnPacketReceivedListener);
    }
    
    public void addServerPacket(final String s, final ModdedServer.OnPacketReceivedListener onPacketReceivedListener) {
        this.addServerPacket(s, onPacketReceivedListener, this.getServerThreadJobExecutor());
    }
    
    public void addServerPacket(final String s, final ModdedServer.OnPacketReceivedListener onPacketReceivedListener, final JobExecutor jobExecutor) {
        if (jobExecutor != null) {
            this.getServer().addUntypedPacketReceivedListener(s, (ModdedServer.OnPacketReceivedListener)new -$$Lambda$Network$cahfn9tRJFTNLuEVhclt86qcSfA(jobExecutor, onPacketReceivedListener));
            return;
        }
        this.getServer().addUntypedPacketReceivedListener(s, onPacketReceivedListener);
    }
    
    public <T> void addServerPacket(final String s, final ModdedServer.TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener) {
        this.addServerPacket(s, typedOnPacketReceivedListener, this.getServerThreadJobExecutor());
    }
    
    public <T> void addServerPacket(final String s, final ModdedServer.TypedOnPacketReceivedListener<T> typedOnPacketReceivedListener, final JobExecutor jobExecutor) {
        if (jobExecutor != null) {
            this.getServer().addPacketReceivedListener(s, (ModdedServer.TypedOnPacketReceivedListener<Object>)new -$$Lambda$Network$x4iNsO3FMLUtJapl4giJ6V4NV_U(jobExecutor, typedOnPacketReceivedListener));
            return;
        }
        this.getServer().addPacketReceivedListener(s, (ModdedServer.TypedOnPacketReceivedListener<Object>)typedOnPacketReceivedListener);
    }
    
    public void addServerShutdownListener(final ModdedServer.OnShutdownListener onShutdownListener) {
        this.getServer().addShutdownListener(onShutdownListener);
    }
    
    public ModdedClient getClient() {
        return this.client;
    }
    
    public JobExecutor getClientThreadJobExecutor() {
        return this.clientThreadJobExecutor;
    }
    
    public NetworkConfig getConfig() {
        return this.config;
    }
    
    public JobExecutor getInstantJobExecutor() {
        return this.instantJobExecutor;
    }
    
    public ModdedServer getServer() {
        return this.server;
    }
    
    public JobExecutor getServerThreadJobExecutor() {
        return this.serverThreadJobExecutor;
    }
    
    public void shutdown() {
        this.server.shutdown();
        this.client.shutdown();
    }
    
    public void startClient(final String s) throws IOException {
        this.startClient(s, this.config.getDefaultPort());
    }
    
    public void startClient(final String s, final int n) throws IOException {
        this.shutdown();
        this.isRunningLanServer = false;
        final NativeNetworking.NetworkLoopHandler start = new NativeNetworking.NetworkLoopHandler(5).start();
        this.getConfig().updateFromEngineConfig();
        DataChannel dataChannel;
        if (this.config.isNativeProtocolPrioritizedForRemoteConnection()) {
            try {
                dataChannel = this.tryOpenClientToServerNativeDataChannel(this.getConfig().getNativeChannelPingPongTimeout());
            }
            catch (IOException ex) {
                if (EngineConfig.isDeveloperMode()) {
                    UserDialog.toast("failed to connect via native protocol, trying socket connection");
                }
                dataChannel = this.tryOpenClientToServerSocketDataChannel(s, n);
            }
        }
        else {
            try {
                dataChannel = this.tryOpenClientToServerSocketDataChannel(s, n);
            }
            catch (IOException ex2) {
                if (EngineConfig.isDeveloperMode()) {
                    UserDialog.toast("failed to connect via socket, trying native protocol");
                }
                dataChannel = this.tryOpenClientToServerNativeDataChannel(this.getConfig().getNativeChannelPingPongTimeout());
            }
        }
        if (dataChannel instanceof NativeDataChannel) {
            final StringBuilder sb = new StringBuilder();
            sb.append("connected to ");
            sb.append(s);
            sb.append(" via native protocol");
            UserDialog.toast(sb.toString());
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("connected to ");
            sb2.append(s);
            sb2.append(" via socket at port ");
            sb2.append(n);
            UserDialog.toast(sb2.toString());
        }
        this.client.start(dataChannel);
        if (!this.client.awaitAllInitializationPackets(this.config.getInitializationTimeout())) {
            UserDialog.dialog("Connection Error", "Failed to connect to server - not all initialization packets were sent to client");
            start.stop();
            this.shutdown();
        }
        else {
            UserDialog.toast("successfully started client");
        }
        start.stop();
    }
    
    public void startLanServer() {
        this.startLanServer(this.config.getDefaultPort());
    }
    
    public void startLanServer(final int n) {
        this.shutdown();
        final NativeNetworking.NetworkLoopHandler start = new NativeNetworking.NetworkLoopHandler(7).start();
        this.startServer(n);
        this.client.start(this.server.openLocalClientChannel());
        this.isRunningLanServer = true;
        if (!this.client.awaitAllInitializationPackets(this.config.getInitializationTimeout())) {
            if (!this.client.isRunning()) {
                UserDialog.dialog("LAN Server Error", "Failed to startup LAN server - not all initialization packets were sent to client");
            }
            start.stop();
            this.shutdown();
        }
        start.stop();
    }
    
    public void startServer() {
        this.startServer(this.config.getDefaultPort());
    }
    
    public void startServer(final int n) {
        this.shutdown();
        this.getConfig().updateFromEngineConfig();
        this.server.start(n);
    }
    
    public interface ClientInitializationPacketSender
    {
        Object onSendingInitPacket();
    }
    
    public interface ServerInitializationPacketSender
    {
        Object onSendingInitPacket(final ConnectedClient p0);
    }
}
