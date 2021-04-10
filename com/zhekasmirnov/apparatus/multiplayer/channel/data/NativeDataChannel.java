package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import com.zhekasmirnov.apparatus.mcpe.*;
import java.io.*;

public class NativeDataChannel extends DataChannel
{
    public static final int PROTOCOL_ID = 3;
    private final NativeNetworking.NativeChannelImpl nativeImpl;
    
    public NativeDataChannel(final NativeNetworking.NativeChannelImpl nativeImpl) {
        this.nativeImpl = nativeImpl;
    }
    
    @Override
    protected void closeImpl() throws IOException {
        this.nativeImpl.closeAndUnlink();
    }
    
    @Override
    public int getProtocolId() {
        return 3;
    }
    
    public boolean pingPong(final int n) {
        try {
            return this.nativeImpl.pingPong(n);
        }
        catch (InterruptedException ex) {
            return false;
        }
    }
    
    @Override
    protected DataPacket receiveImpl() throws IOException {
        return this.nativeImpl.receive();
    }
    
    @Override
    protected void sendImpl(final DataPacket dataPacket) throws IOException {
        this.nativeImpl.send(dataPacket);
    }
}
