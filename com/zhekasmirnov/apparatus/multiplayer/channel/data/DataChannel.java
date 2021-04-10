package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import java.io.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.util.*;

public abstract class DataChannel
{
    private IBrokenChannelListener brokenChannelListener;
    private ICloseListener closeListener;
    private boolean isClosed;
    private boolean isPanic;
    private boolean isShutdown;
    private final List<IPacketListener> packetListeners;
    private final Object receiveLock;
    private final Object sendLock;
    
    public DataChannel() {
        this.sendLock = new Object();
        this.receiveLock = new Object();
        this.isClosed = false;
        this.isShutdown = false;
        this.packetListeners = new ArrayList<IPacketListener>();
        this.closeListener = null;
        this.brokenChannelListener = null;
        this.isPanic = false;
    }
    
    private void channelBroke(final IOException ex) {
        synchronized (this) {
            if (this.isShutdown) {
                return;
            }
            if (this.isPanic) {
                return;
            }
            this.isPanic = true;
            if (ex != null) {
                ex.printStackTrace();
            }
            if (!this.isClosed && EngineConfig.isDeveloperMode()) {
                UserDialog.dialog("channel broke, panic", "", ex, false);
            }
            if (this.brokenChannelListener != null) {
                this.brokenChannelListener.onBroke(ex);
            }
            this.isPanic = false;
            this.close();
        }
    }
    
    public void addListener(final IPacketListener packetListener) {
        synchronized (this.packetListeners) {
            this.packetListeners.add(packetListener);
        }
    }
    
    public void close() {
        synchronized (this) {
            if (!this.isClosed) {
                this.isClosed = true;
                try {
                    if (!this.isShutdown && this.closeListener != null) {
                        this.closeListener.onClose();
                    }
                    this.closeImpl();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    
    protected abstract void closeImpl() throws IOException;
    
    public abstract int getProtocolId();
    
    public boolean isClosed() {
        return this.isClosed || this.isShutdown;
    }
    
    public void listenerLoop() {
        while (!this.isClosed) {
            final DataPacket receive = this.receive();
            if (receive != null) {
                synchronized (this.packetListeners) {
                    final Iterator<IPacketListener> iterator = this.packetListeners.iterator();
                    while (iterator.hasNext()) {
                        iterator.next().receive(receive);
                    }
                }
            }
        }
    }
    
    public DataPacket receive() {
        final Object receiveLock = this.receiveLock;
        // monitorenter(receiveLock)
        while (true) {
            try {
                try {
                    // monitorexit(receiveLock)
                    return this.receiveImpl();
                }
                catch (IOException ex) {
                    if (!"socket closed".equalsIgnoreCase(ex.getMessage())) {
                        this.channelBroke(ex);
                    }
                    // monitorexit(receiveLock)
                    return null;
                }
                // monitorexit(receiveLock)
                throw;
            }
            finally {
                continue;
            }
            break;
        }
    }
    
    protected abstract DataPacket receiveImpl() throws IOException;
    
    public void removeListener(final IPacketListener packetListener) {
        synchronized (this.packetListeners) {
            this.packetListeners.remove(packetListener);
        }
    }
    
    public void send(final DataPacket dataPacket) {
        synchronized (this.sendLock) {
            if (!this.isClosed && !this.isShutdown) {
                try {
                    this.sendImpl(dataPacket);
                }
                catch (IOException ex) {
                    this.channelBroke(ex);
                }
            }
        }
    }
    
    protected abstract void sendImpl(final DataPacket p0) throws IOException;
    
    public void setBrokenChannelListener(final IBrokenChannelListener brokenChannelListener) {
        this.brokenChannelListener = brokenChannelListener;
    }
    
    public void setCloseListener(final ICloseListener closeListener) {
        this.closeListener = closeListener;
    }
    
    public void shutdownAndAwaitDisconnect() {
        synchronized (this) {
            if (!this.isShutdown && !this.isClosed) {
                this.isShutdown = true;
                if (this.closeListener != null) {
                    this.closeListener.onClose();
                }
            }
        }
    }
    
    public interface IBrokenChannelListener
    {
        void onBroke(final IOException p0);
    }
    
    public interface ICloseListener
    {
        void onClose();
    }
    
    public interface IPacketListener
    {
        void receive(final DataPacket p0);
    }
}
