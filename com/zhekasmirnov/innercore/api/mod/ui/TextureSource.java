package com.zhekasmirnov.innercore.api.mod.ui;

import java.util.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.api.mod.ui.memory.*;
import android.graphics.*;
import java.io.*;
import com.zhekasmirnov.innercore.utils.*;

public class TextureSource
{
    public static final TextureSource instance;
    private HashMap<String, Bitmap> loadedTextures;
    
    static {
        LoadingUI.setTip("Loading UI Resources");
        instance = new TextureSource();
        BitmapCache.init();
        TextureSource.instance.loadAllStandartAssets();
        LoadingUI.setTip("");
    }
    
    public TextureSource() {
        this.loadedTextures = new HashMap<String, Bitmap>();
    }
    
    public Bitmap get(final String s) {
        return this.loadedTextures.get(s);
    }
    
    public Bitmap getSafe(final String s) {
        final Bitmap value = this.get(s);
        if (value == null) {
            return this.get("missing_texture");
        }
        return value;
    }
    
    public void loadAllStandartAssets() {
        this.loadAsset("innercore/ui/missing_texture.png");
        this.loadAsset("innercore/ui/missing_mod_icon.png");
        final String[] listAssets = FileTools.listAssets("innercore/ui");
        for (int length = listAssets.length, i = 0; i < length; ++i) {
            final String s = listAssets[i];
            final StringBuilder sb = new StringBuilder();
            sb.append("innercore/ui/");
            sb.append(s);
            this.loadAsset(sb.toString());
        }
        if (this.get("missing_texture") == null) {
            this.put("missing_texture", Bitmap.createBitmap(16, 16, Bitmap$Config.ARGB_8888));
        }
    }
    
    public void loadAsset(String s) {
        final Bitmap assetAsBitmap = FileTools.getAssetAsBitmap(s);
        if (assetAsBitmap != null) {
            s = s.substring(s.lastIndexOf("/") + 1);
            s = s.substring(0, s.lastIndexOf("."));
            this.loadedTextures.put(s, assetAsBitmap);
        }
    }
    
    public void loadDirectory(final File file) {
        this.loadDirectory(file, "");
    }
    
    public void loadDirectory(final File file, final String s) {
        if (file.exists() && file.isDirectory()) {
            String string = s;
            if (s.length() > 0) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(".");
                string = sb.toString();
            }
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                if (file2.isDirectory()) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(string);
                    sb2.append(file2.getName());
                    this.loadDirectory(file2, sb2.toString());
                }
                else if (file2.getName().endsWith(".png")) {
                    this.loadFile(file2, string);
                }
            }
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("failed to load directory of ui files: ");
        sb3.append(file);
        sb3.append(" does not exist or not a directory.");
        UIUtils.log(sb3.toString());
    }
    
    public void loadFile(final File file, String s) {
        String s2 = s;
        if (s == null) {
            s2 = "";
        }
        if (file.exists()) {
            s = file.getName();
            final StringBuilder sb = new StringBuilder();
            sb.append(s2);
            sb.append(s.substring(0, s.lastIndexOf(".")));
            s = sb.toString();
            this.loadedTextures.put(s, FileTools.readFileAsBitmap(file.getAbsolutePath()));
        }
    }
    
    public void put(final String s, final Bitmap bitmap) {
        this.loadedTextures.put(s, bitmap);
    }
}
