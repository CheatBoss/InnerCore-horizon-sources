package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;

public class StringChannelCodec extends ChannelCodec<String>
{
    public static final int FORMAT_ID = 2;
    
    public StringChannelCodec(final DataChannel dataChannel) {
        super(dataChannel);
    }
    
    @Override
    protected String decode(final byte[] array) throws ChannelCodecException {
        return new String(array);
    }
    
    @Override
    protected byte[] encode(final String s) {
        return s.getBytes();
    }
    
    @Override
    public int getFormatId() {
        return 2;
    }
}
