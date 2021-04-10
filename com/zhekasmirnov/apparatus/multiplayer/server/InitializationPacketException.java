package com.zhekasmirnov.apparatus.multiplayer.server;

import java.io.*;

public class InitializationPacketException extends IOException
{
    public InitializationPacketException() {
    }
    
    public InitializationPacketException(final String s) {
        super(s);
    }
    
    public InitializationPacketException(final String s, final Throwable t) {
        super(s, t);
    }
    
    public InitializationPacketException(final Throwable t) {
        super(t);
    }
}
