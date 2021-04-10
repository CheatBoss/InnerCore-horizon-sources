package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import java.net.*;
import java.io.*;

public class SocketDataChannel extends DataChannel
{
    public static final int PROTOCOL_ID = 1;
    public final DataInputStream inputStream;
    public final DataOutputStream outputStream;
    public final Socket socket;
    
    public SocketDataChannel(final Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.outputStream = new DataOutputStream(socket.getOutputStream());
        socket.setKeepAlive(true);
    }
    
    public void closeImpl() throws IOException {
        try {
            this.inputStream.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            this.outputStream.close();
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
        try {
            this.socket.shutdownOutput();
            this.socket.shutdownInput();
        }
        finally {
            this.socket.close();
        }
    }
    
    @Override
    public int getProtocolId() {
        return 1;
    }
    
    public DataPacket receiveImpl() throws IOException {
        final String utf = this.inputStream.readUTF();
        final byte byte1 = this.inputStream.readByte();
        final int int1 = this.inputStream.readInt();
        if (int1 >= 0 && int1 <= 67108864) {
            final byte[] array = new byte[int1];
            this.inputStream.readFully(array);
            return new DataPacket(utf, byte1, array);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("packet has invalid size ");
        sb.append(int1);
        throw new IOException(sb.toString());
    }
    
    public void sendImpl(final DataPacket dataPacket) throws IOException {
        this.outputStream.writeUTF(dataPacket.name);
        this.outputStream.writeByte(dataPacket.formatId);
        this.outputStream.writeInt(dataPacket.data.length);
        this.outputStream.write(dataPacket.data);
    }
}
