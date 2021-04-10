package com.zhekasmirnov.innercore.utils;

import com.zhekasmirnov.horizon.runtime.logger.*;
import android.graphics.*;
import org.mozilla.javascript.*;

public class ColorsPatch
{
    public static int argb(final float n, final float n2, final float n3, final float n4) {
        Logger.debug("MEMBERS_PATCH", "Called patched Color.argb() method");
        if (n2 <= 1.0f && n3 <= 1.0f && n4 <= 1.0f) {
            return Color.argb(n, n2, n3, n4);
        }
        return Color.argb((int)n, (int)n2, (int)n3, (int)n4);
    }
    
    public static void init() {
        MembersPatch.addOverride("android.graphics.Color.rgb", "com.zhekasmirnov.innercore.utils.ColorsPatch.rgb");
        MembersPatch.addOverride("android.graphics.Color.argb", "com.zhekasmirnov.innercore.utils.ColorsPatch.argb");
    }
    
    public static int rgb(final float n, final float n2, final float n3) {
        Logger.debug("MEMBERS_PATCH", "Called patched Color.rgb() method");
        if (n <= 1.0f && n2 <= 1.0f && n3 <= 1.0f) {
            return Color.rgb(n, n2, n3);
        }
        return Color.rgb((int)n, (int)n2, (int)n3);
    }
}
