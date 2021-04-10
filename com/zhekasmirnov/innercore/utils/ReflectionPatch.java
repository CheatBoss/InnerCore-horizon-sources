package com.zhekasmirnov.innercore.utils;

import com.zhekasmirnov.horizon.runtime.logger.*;
import org.mozilla.javascript.*;

public class ReflectionPatch
{
    public static Class<?> forName(final String s, final boolean b, final ClassLoader classLoader) throws ClassNotFoundException {
        if (s.startsWith("com.zhekasmirnov.horizon.launcher.ads")) {
            throw new ClassNotFoundException("Unauthorized");
        }
        String string = s;
        if (s.startsWith("zhekasmirnov.launcher.")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("com.zhekasmirnov.innercore.");
            sb.append(s.substring(22));
            string = sb.toString();
            Logger.debug("VERY_IMPORTANT", string);
        }
        return Class.forName(string, b, classLoader);
    }
    
    public static void init() {
        MembersPatch.addOverride("java.lang.Class.forName", "com.zhekasmirnov.innercore.utils.ReflectionPatch.forName");
    }
}
