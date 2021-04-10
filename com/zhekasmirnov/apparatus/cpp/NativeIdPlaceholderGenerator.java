package com.zhekasmirnov.apparatus.cpp;

import com.zhekasmirnov.innercore.api.*;
import org.json.*;
import java.util.*;

public class NativeIdPlaceholderGenerator
{
    public static void addBlock(final int n, final String s) {
        addBlockPlaceholder(n, NativeAPI.convertNameId(s));
    }
    
    private static native void addBlockPlaceholder(final int p0, final String p1);
    
    public static void addItem(final int n, final String s) {
        addItemPlaceholder(n, NativeAPI.convertNameId(s));
    }
    
    private static native void addItemPlaceholder(final int p0, final String p1);
    
    public static native void clearAll();
    
    public static void rebuildFromServerPacket(final JSONObject jsonObject) {
        clearAll();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final int optInt = jsonObject.optInt(s);
            if (optInt != 0) {
                if (s.startsWith("block:")) {
                    addBlock(optInt, s.substring(6));
                }
                else {
                    if (!s.startsWith("item:")) {
                        continue;
                    }
                    addItem(optInt, s.substring(5));
                }
            }
        }
    }
    
    private static native void setPlaceholderBlockTexture(final String p0, final int p1);
    
    private static native void setPlaceholderItemTexture(final String p0, final int p1);
}
