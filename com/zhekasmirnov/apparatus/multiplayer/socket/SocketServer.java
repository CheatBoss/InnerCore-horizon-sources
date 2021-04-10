package com.zhekasmirnov.apparatus.multiplayer.socket;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;
import java.io.*;
import java.net.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$SocketServer$QfXaQGKH46lwukF6ulYU7PQL4Og.class })
public class SocketServer
{
    private final List<IClientConnectListener> clientConnectListeners;
    private boolean isRunning;
    private final Object lock;
    private ServerSocket serverSocket;
    
    public SocketServer() {
        this.lock = new Object();
        this.serverSocket = null;
        this.isRunning = false;
        this.clientConnectListeners = new ArrayList<IClientConnectListener>();
    }
    
    public void addClientConnectListener(final IClientConnectListener clientConnectListener) {
        this.clientConnectListeners.add(clientConnectListener);
    }
    
    public void close() {
        synchronized (this.lock) {
            this.isRunning = false;
            if (this.serverSocket != null) {
                try {
                    this.serverSocket.close();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
                this.serverSocket = null;
            }
        }
    }
    
    public void start(final int n) throws IOException {
        synchronized (this.lock) {
            if (this.isRunning) {
                throw new IllegalStateException("SocketServer is already running");
            }
            this.isRunning = true;
            this.serverSocket = new ServerSocket(n);
            new Thread(new -$$Lambda$SocketServer$QfXaQGKH46lwukF6ulYU7PQL4Og(this)).start();
        }
    }
    
    public interface IClientConnectListener
    {
        boolean onClientConnected(final DataChannel p0, final Socket p1, final boolean p2);
    }
}
