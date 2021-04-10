package com.zhekasmirnov.innercore.api.mod.ui.types;

import java.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class FrameTextureSource
{
    private static HashMap<String, FrameTexture> loadedTextures;
    
    static {
        FrameTextureSource.loadedTextures = new HashMap<String, FrameTexture>();
    }
    
    public static FrameTexture getFrameTexture(final String s) {
        return getFrameTexture(s, null);
    }
    
    public static FrameTexture getFrameTexture(String bitmapName, UIStyle default1) {
        if (default1 == null) {
            default1 = UIStyle.DEFAULT;
        }
        bitmapName = default1.getBitmapName(bitmapName);
        if (FrameTextureSource.loadedTextures.containsKey(bitmapName)) {
            return FrameTextureSource.loadedTextures.get(bitmapName);
        }
        final FrameTexture frameTexture = new FrameTexture(TextureSource.instance.getSafe(bitmapName));
        FrameTextureSource.loadedTextures.put(bitmapName, frameTexture);
        return frameTexture;
    }
    
    public static boolean[] scriptableAsSides(final ScriptableObject scriptableObject) {
        if (scriptableObject == null) {
            return new boolean[] { true, true, true, true };
        }
        return new boolean[] { ScriptableObjectHelper.getBooleanProperty(scriptableObject, "left", true), ScriptableObjectHelper.getBooleanProperty(scriptableObject, "right", true), ScriptableObjectHelper.getBooleanProperty(scriptableObject, "up", true), ScriptableObjectHelper.getBooleanProperty(scriptableObject, "down", true) };
    }
}
