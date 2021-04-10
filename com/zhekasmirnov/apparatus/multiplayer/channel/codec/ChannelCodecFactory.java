package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import org.json.*;
import org.mozilla.javascript.*;

public class ChannelCodecFactory
{
    public static ChannelCodec<?> create(final int n, final DataChannel dataChannel) {
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("cannot create channel codec for format id: ");
                sb.append(n);
                throw new IllegalArgumentException(sb.toString());
            }
            case 3: {
                return new ScriptableChannelCodec(dataChannel);
            }
            case 2: {
                return new StringChannelCodec(dataChannel);
            }
            case 1: {
                return new JsonChannelCodec(dataChannel);
            }
        }
    }
    
    public static ChannelCodec<?> create(final Class<?> clazz, final DataChannel dataChannel) {
        if (String.class.isAssignableFrom(clazz)) {
            return new StringChannelCodec(dataChannel);
        }
        if (JSONObject.class.isAssignableFrom(clazz)) {
            return new JsonChannelCodec(dataChannel);
        }
        if (Scriptable.class.isAssignableFrom(clazz)) {
            return new ScriptableChannelCodec(dataChannel);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("cannot create channel codec for data class: ");
        sb.append(clazz.getSimpleName());
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static Class<?> toCodecClass(final Class<?> clazz) {
        if (String.class.isAssignableFrom(clazz)) {
            return String.class;
        }
        if (JSONObject.class.isAssignableFrom(clazz)) {
            return JSONObject.class;
        }
        if (Scriptable.class.isAssignableFrom(clazz)) {
            return Scriptable.class;
        }
        return clazz;
    }
}
