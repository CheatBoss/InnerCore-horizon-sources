package com.zhekasmirnov.innercore.api.commontypes;

import android.util.*;
import org.mozilla.javascript.*;

public class ScriptableParams extends ScriptableObject
{
    public ScriptableParams(final Pair<String, Object>... array) {
        for (int length = array.length, i = 0; i < length; ++i) {
            final Pair<String, Object> pair = array[i];
            this.put((String)pair.first, (Scriptable)this, pair.second);
        }
    }
    
    public String getClassName() {
        return "Parameters";
    }
}
