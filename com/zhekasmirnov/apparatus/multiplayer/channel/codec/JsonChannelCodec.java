package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import org.json.*;

public class JsonChannelCodec extends ChannelCodec<JSONObject>
{
    public static final int FORMAT_ID = 1;
    
    public JsonChannelCodec(final DataChannel dataChannel) {
        super(dataChannel);
    }
    
    @Override
    protected JSONObject decode(final byte[] array) throws ChannelCodecException {
        try {
            return new JSONObject(new String(array));
        }
        catch (JSONException ex) {
            throw new ChannelCodecException(ex.getMessage(), (Throwable)ex);
        }
    }
    
    @Override
    protected byte[] encode(final JSONObject jsonObject) {
        return jsonObject.toString().getBytes();
    }
    
    @Override
    public int getFormatId() {
        return 1;
    }
}
