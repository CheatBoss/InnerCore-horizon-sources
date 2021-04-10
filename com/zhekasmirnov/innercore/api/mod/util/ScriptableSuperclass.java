package com.zhekasmirnov.innercore.api.mod.util;

import org.mozilla.javascript.*;

public class ScriptableSuperclass extends ScriptableObject
{
    public String getClassName() {
        return this.getClass().getSimpleName();
    }
}
