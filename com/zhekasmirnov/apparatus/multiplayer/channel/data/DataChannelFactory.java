package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import java.net.*;
import java.io.*;

public class DataChannelFactory
{
    public static DataChannel newDataChannel(final Socket socket, final int n) throws IOException {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to create DataChannel: invalid protocol id ");
                sb.append(n);
                throw new IOException(sb.toString());
            }
            case 1: {
                return new SocketDataChannel(socket);
            }
            case 0: {
                throw new IOException("failed to create DataChannel: DirectDataChannel is not a network channel protocol");
            }
        }
    }
}
