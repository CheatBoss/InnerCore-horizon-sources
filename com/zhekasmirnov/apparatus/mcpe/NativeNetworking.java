package com.zhekasmirnov.apparatus.mcpe;

import com.android.tools.r8.annotations.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import com.zhekasmirnov.apparatus.util.*;
import java.util.function.*;
import java.util.concurrent.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;

@SynthesizedClassMap({ -$$Lambda$NativeNetworking$LgFbKBSqjqZWO5NfNLEQNpDjRRA.class })
public class NativeNetworking
{
    private static NativeChannelImpl clientToServerChannel;
    private static final List<ConnectionListener> connectionListeners;
    private static final Map<String, NativeChannelImpl> serverToClientChannelMap;
    
    static {
        connectionListeners = new ArrayList<ConnectionListener>();
        NativeNetworking.clientToServerChannel = null;
        serverToClientChannelMap = new HashMap<String, NativeChannelImpl>();
    }
    
    public static void addConnectionListener(final ConnectionListener connectionListener) {
        NativeNetworking.connectionListeners.add(connectionListener);
    }
    
    public static NativeChannelImpl getClientToServerChannel() {
        return NativeNetworking.clientToServerChannel;
    }
    
    private static native byte[] getCurrentNativePacketBytes();
    
    private static byte[] getCurrentNativePacketBytesNonNull() {
        final byte[] currentNativePacketBytes = getCurrentNativePacketBytes();
        if (currentNativePacketBytes != null) {
            return currentNativePacketBytes;
        }
        return new byte[0];
    }
    
    public static NativeChannelImpl getOrCreateClientToServerChannel() {
        if (NativeNetworking.clientToServerChannel == null) {
            NativeNetworking.clientToServerChannel = new NativeChannelImpl((String)null);
        }
        return NativeNetworking.clientToServerChannel;
    }
    
    public static NativeChannelImpl getServerToClientChannel(final String s) {
        return NativeNetworking.serverToClientChannelMap.get(s);
    }
    
    public static void onClientPacketReceived(final String s, final int n) {
        if (NativeNetworking.clientToServerChannel == null) {
            NativeNetworking.clientToServerChannel = new NativeChannelImpl((String)null);
        }
        NativeNetworking.clientToServerChannel.onReceived(new DataPacket(s, n, getCurrentNativePacketBytesNonNull()));
    }
    
    public static void onLevelLeft() {
        synchronized (NativeNetworking.serverToClientChannelMap) {
            final Iterator<NativeChannelImpl> iterator = NativeNetworking.serverToClientChannelMap.values().iterator();
            while (iterator.hasNext()) {
                iterator.next().close();
            }
            NativeNetworking.serverToClientChannelMap.clear();
            if (NativeNetworking.clientToServerChannel != null) {
                NativeNetworking.clientToServerChannel.close();
                NativeNetworking.clientToServerChannel = null;
            }
        }
    }
    
    public static void onServerPacketReceived(final String s, final String s2, final int n) {
        synchronized (NativeNetworking.serverToClientChannelMap) {
            Java8BackComp.computeIfAbsent(NativeNetworking.serverToClientChannelMap, s, new -$$Lambda$NativeNetworking$LgFbKBSqjqZWO5NfNLEQNpDjRRA(s)).onReceived(new DataPacket(s2, n, getCurrentNativePacketBytesNonNull()));
        }
    }
    
    public static void removeConnectionListener(final ConnectionListener connectionListener) {
        NativeNetworking.connectionListeners.remove(connectionListener);
    }
    
    private static native void runClientNetworkEventLoop(final boolean p0);
    
    private static native void runMinecraftNetworkEventLoop(final boolean p0);
    
    private static native void runServerNetworkEventLoop(final boolean p0);
    
    private static native void sendPacketToClient(final String p0, final String p1, final int p2, final byte[] p3);
    
    private static native void sendPacketToServer(final String p0, final int p1, final byte[] p2);
    
    public interface ConnectionListener
    {
        void onNativeChannelConnected(final NativeDataChannel p0);
    }
    
    public static class NativeChannelImpl
    {
        private static final DataPacket channelClosedPacket;
        private static final int initialPacketPollTimeout = 75000;
        private static final int packetPollTimeout = 20000;
        private long channelOpenedTime;
        private final String client;
        private boolean isClosed;
        private final BlockingQueue<DataPacket> packets;
        private final BlockingQueue<DataPacket> pongs;
        
        static {
            channelClosedPacket = new DataPacket("", 0, null);
        }
        
        private NativeChannelImpl(final String client) {
            this.isClosed = false;
            this.channelOpenedTime = 0L;
            this.packets = new LinkedBlockingQueue<DataPacket>();
            this.pongs = new LinkedBlockingQueue<DataPacket>();
            this.client = client;
        }
        
        private void close() {
            try {
                this.isClosed = true;
                this.packets.put(NativeChannelImpl.channelClosedPacket);
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        private void onReceived(final DataPacket dataPacket) {
            try {
                if ("system.native_ping".equals(dataPacket.name)) {
                    this.send(new DataPacket("system.native_pong", 2, new byte[0]));
                }
                else if ("system.native_pong".equals(dataPacket.name)) {
                    this.pongs.put(dataPacket);
                }
                else {
                    this.packets.put(dataPacket);
                }
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        private void unlink() {
            if (this.client != null) {
                synchronized (NativeNetworking.serverToClientChannelMap) {
                    NativeNetworking.serverToClientChannelMap.remove(this.client);
                    return;
                }
            }
            NativeNetworking.clientToServerChannel = null;
        }
        
        public void closeAndUnlink() {
            this.close();
            this.unlink();
        }
        
        public String getClient() {
            return this.client;
        }
        
        public String getClientRepresentation() {
            if (this.client == null) {
                return "server";
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("client:");
            sb.append(this.client);
            return sb.toString();
        }
        
        public boolean isClosed() {
            return this.isClosed;
        }
        
        public boolean isServerSide() {
            return this.client != null;
        }
        
        public boolean pingPong(final int n) throws InterruptedException {
            this.pongs.clear();
            this.sendPing();
            return this.pongs.poll(n, TimeUnit.MILLISECONDS) != null;
        }
        
        public DataPacket receive() throws IOException {
            if (this.channelOpenedTime == 0L) {
                this.channelOpenedTime = System.currentTimeMillis();
            }
            while (true) {
                while (true) {
                    Label_0334: {
                        try {
                            DataPacket dataPacket = this.packets.poll(20000L, TimeUnit.MILLISECONDS);
                            if (dataPacket == null) {
                                this.pongs.clear();
                                this.sendPing();
                                dataPacket = this.packets.poll(20000L, TimeUnit.MILLISECONDS);
                                final DataPacket dataPacket2 = this.pongs.poll(0L, TimeUnit.MILLISECONDS);
                                if (dataPacket == null && dataPacket2 == null && System.currentTimeMillis() > this.channelOpenedTime + 75000L) {
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("native channel \"");
                                    sb.append(this.getClientRepresentation());
                                    sb.append("\" has not responded for a ping in ");
                                    sb.append(20000);
                                    sb.append(" milliseconds, it will be closed");
                                    Logger.debug(sb.toString());
                                    this.isClosed = true;
                                    this.unlink();
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append("native channel \"");
                                    sb2.append(this.getClientRepresentation());
                                    sb2.append("\" closed (no response for a ping)");
                                    throw new IOException(sb2.toString());
                                }
                                break Label_0334;
                            }
                            else {
                                if (dataPacket.equals(NativeChannelImpl.channelClosedPacket)) {
                                    this.isClosed = true;
                                    this.unlink();
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append("native channel \"");
                                    sb3.append(this.getClientRepresentation());
                                    sb3.append("\" closed (manually)");
                                    throw new IOException(sb3.toString());
                                }
                                return dataPacket;
                            }
                        }
                        catch (InterruptedException ex) {
                            this.isClosed = true;
                            this.unlink();
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("native channel \"");
                            sb4.append(this.getClientRepresentation());
                            sb4.append("\" closed (thread interrupted)");
                            throw new IOException(sb4.toString(), ex);
                        }
                    }
                    continue;
                }
            }
        }
        
        public void send(final DataPacket dataPacket) {
            if (this.client != null) {
                sendPacketToClient(this.client, dataPacket.name, dataPacket.formatId, dataPacket.data);
                return;
            }
            sendPacketToServer(dataPacket.name, dataPacket.formatId, dataPacket.data);
        }
        
        public void sendPing() {
            this.send(new DataPacket("system.native_ping", 2, new byte[0]));
        }
    }
    
    @SynthesizedClassMap({ -$$Lambda$NativeNetworking$NetworkLoopHandler$uXM-aiHwxxiyI3YLrtWkpqjkO8Q.class })
    public static class NetworkLoopHandler
    {
        public static final int CLIENT_HANDLER = 4;
        public static final int GLOBAL_HANDLER = 1;
        public static final int SERVER_HANDLER = 2;
        private final boolean boolPar;
        private final int domain;
        private int sessionId;
        private Thread thread;
        private final int updateDelay;
        
        public NetworkLoopHandler(final int n) {
            this(n, false);
        }
        
        public NetworkLoopHandler(final int domain, final int updateDelay, final boolean boolPar) {
            this.sessionId = 0;
            this.thread = null;
            this.domain = domain;
            this.updateDelay = updateDelay;
            this.boolPar = boolPar;
        }
        
        public NetworkLoopHandler(final int n, final boolean b) {
            this(n, 100, b);
        }
        
        public NetworkLoopHandler start() {
            this.stop();
            (this.thread = new Thread(new -$$Lambda$NativeNetworking$NetworkLoopHandler$uXM-aiHwxxiyI3YLrtWkpqjkO8Q(this, this.sessionId))).start();
            return this;
        }
        
        public NetworkLoopHandler stop() {
            ++this.sessionId;
            if (this.thread != null) {
                this.thread.interrupt();
                this.thread = null;
            }
            return this;
        }
    }
}
