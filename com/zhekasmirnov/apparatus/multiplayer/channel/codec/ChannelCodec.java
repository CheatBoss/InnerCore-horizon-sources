package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import java.util.*;

public abstract class ChannelCodec<T> implements IPacketListener
{
    private final DataChannel channel;
    private final List<ICommonListener<T>> commonListeners;
    
    public ChannelCodec(final DataChannel channel) {
        this.commonListeners = new ArrayList<ICommonListener<T>>();
        (this.channel = channel).addListener((DataChannel.IPacketListener)this);
    }
    
    public void addListener(final ICommonListener<T> commonListener) {
        synchronized (this.commonListeners) {
            this.commonListeners.add(commonListener);
        }
    }
    
    protected abstract T decode(final byte[] p0) throws ChannelCodecException;
    
    protected abstract byte[] encode(final T p0);
    
    public DataChannel getChannel() {
        return this.channel;
    }
    
    public abstract int getFormatId();
    
    @Override
    public void receive(final DataPacket dataPacket) {
        if (this.getFormatId() != dataPacket.formatId) {
            return;
        }
        final String name = dataPacket.name;
        try {
            final T decode = this.decode(dataPacket.data);
            synchronized (this.commonListeners) {
                final Iterator<ICommonListener<T>> iterator = this.commonListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().receive(name, decode);
                }
            }
        }
        catch (ChannelCodecException ex) {
            ex.printStackTrace();
        }
    }
    
    public void send(final String s, final T t) {
        this.channel.send(new DataPacket(s, this.getFormatId(), this.encode(t)));
    }
    
    public void sendUntyped(final String s, final Object o) {
        this.channel.send(new DataPacket(s, this.getFormatId(), this.encode(o)));
    }
    
    public interface ICommonListener<T>
    {
        void receive(final String p0, final T p1);
    }
}
