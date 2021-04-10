package com.zhekasmirnov.innercore.api.runtime.saver.world;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.*;
import org.mozilla.javascript.*;
import java.io.*;

@SynthesizedClassMap({ -$$Lambda$ScriptableSaverScope$SKsTsK8MFXdC42bmQt3ZeMrD8mA.class })
public abstract class ScriptableSaverScope implements SaverScope
{
    public abstract void read(final Object p0);
    
    @Override
    public void readJson(Object scriptableFromJson) throws Exception {
        scriptableFromJson = ScriptableSerializer.scriptableFromJson(scriptableFromJson);
        if (scriptableFromJson instanceof Scriptable) {
            this.read(scriptableFromJson);
            return;
        }
        throw new IOException("scriptable saver scope readJson() de-serialized into non scriptable");
    }
    
    public abstract Object save();
    
    @Override
    public Object saveAsJson() throws Exception {
        return ScriptableSerializer.scriptableToJson(this.save(), (ScriptableSerializer.SerializationErrorHandler)-$$Lambda$ScriptableSaverScope$SKsTsK8MFXdC42bmQt3ZeMrD8mA.INSTANCE);
    }
}
