package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import java.util.concurrent.*;
import android.util.*;
import java.io.*;

public class DirectDataChannel extends DataChannel
{
    public static final int PROTOCOL_ID = 0;
    private static final DataPacket channelClosedPacket;
    private DirectDataChannel linkedChannel;
    private final BlockingQueue<DataPacket> packets;
    
    static {
        channelClosedPacket = new DataPacket("", 0, null);
    }
    
    private DirectDataChannel() {
        this.packets = new LinkedBlockingQueue<DataPacket>();
    }
    
    public static Pair<DirectDataChannel, DirectDataChannel> createDirectChannelPair() {
        final DirectDataChannel linkedChannel = new DirectDataChannel();
        final DirectDataChannel linkedChannel2 = new DirectDataChannel();
        linkedChannel.setLinkedChannel(linkedChannel2);
        linkedChannel2.setLinkedChannel(linkedChannel);
        return (Pair<DirectDataChannel, DirectDataChannel>)new Pair((Object)linkedChannel, (Object)linkedChannel2);
    }
    
    private void setLinkedChannel(final DirectDataChannel linkedChannel) {
        this.linkedChannel = linkedChannel;
    }
    
    @Override
    protected void closeImpl() throws IOException {
        try {
            this.linkedChannel.packets.put(DirectDataChannel.channelClosedPacket);
        }
        catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }
    
    @Override
    public int getProtocolId() {
        return 0;
    }
    
    @Override
    protected DataPacket receiveImpl() throws IOException {
        try {
            final DataPacket dataPacket = this.packets.take();
            if (dataPacket.equals(DirectDataChannel.channelClosedPacket)) {
                throw new IOException("direct channel closed");
            }
            return dataPacket;
        }
        catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }
    
    @Override
    protected void sendImpl(final DataPacket dataPacket) throws IOException {
        try {
            this.linkedChannel.packets.put(dataPacket);
        }
        catch (InterruptedException ex) {
            throw new IOException(ex);
        }
    }
}
