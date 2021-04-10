package com.zhekasmirnov.apparatus.multiplayer.channel.data;

import java.util.*;

public class DataPacket
{
    public final byte[] data;
    public final int formatId;
    public final String name;
    
    public DataPacket(final String name, final int formatId, final byte[] data) {
        this.name = name;
        this.formatId = formatId;
        this.data = data;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DataPacket{name='");
        sb.append(this.name);
        sb.append('\'');
        sb.append(", formatId=");
        sb.append(this.formatId);
        sb.append(", data=");
        sb.append(Arrays.toString(this.data));
        sb.append('}');
        return sb.toString();
    }
}
