package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import java.io.*;

public class ChannelCodecException extends IOException
{
    public ChannelCodecException() {
    }
    
    public ChannelCodecException(final String s) {
        super(s);
    }
    
    public ChannelCodecException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public ChannelCodecException(final Throwable t) {
        super(t);
    }
}
