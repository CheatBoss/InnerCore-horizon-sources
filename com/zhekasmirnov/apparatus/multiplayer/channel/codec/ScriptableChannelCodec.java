package com.zhekasmirnov.apparatus.multiplayer.channel.codec;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.channel.data.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;

@SynthesizedClassMap({ -$$Lambda$ScriptableChannelCodec$ovFmcVVSXht3mrC-jiUswABQ4Mg.class })
public class ScriptableChannelCodec extends ChannelCodec<Scriptable>
{
    public static final int FORMAT_ID = 3;
    
    public ScriptableChannelCodec(final DataChannel dataChannel) {
        super(dataChannel);
    }
    
    @Override
    protected Scriptable decode(final byte[] array) throws ChannelCodecException {
        try {
            return (Scriptable)NativeJSON.parse(Context.enter(), (Scriptable)ScriptableObjectHelper.getDefaultScope(), new String(array), (Callable)-$$Lambda$ScriptableChannelCodec$ovFmcVVSXht3mrC-jiUswABQ4Mg.INSTANCE);
        }
        catch (ClassCastException ex) {
            throw new ChannelCodecException(ex.getMessage(), ex);
        }
    }
    
    @Override
    protected byte[] encode(final Scriptable scriptable) {
        return NativeJSON.stringify(Context.enter(), (Scriptable)ScriptableObjectHelper.getDefaultScope(), (Object)scriptable, (Object)null, (Object)null).toString().getBytes();
    }
    
    @Override
    public int getFormatId() {
        return 0;
    }
}
