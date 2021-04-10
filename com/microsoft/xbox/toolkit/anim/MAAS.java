package com.microsoft.xbox.toolkit.anim;

import java.util.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import java.io.*;

public class MAAS
{
    private static MAAS instance;
    private final String ASSET_FILENAME;
    private final String SDCARD_FILENAME;
    private Hashtable<String, MAASAnimation> maasFileCache;
    private boolean usingSdcard;
    
    static {
        MAAS.instance = new MAAS();
    }
    
    public MAAS() {
        this.SDCARD_FILENAME = "/sdcard/bishop/maas/%sAnimation.xml";
        this.ASSET_FILENAME = "animation/%sAnimation.xml";
        this.usingSdcard = false;
        this.maasFileCache = new Hashtable<String, MAASAnimation>();
    }
    
    public static MAAS getInstance() {
        return MAAS.instance;
    }
    
    private MAASAnimation getMAASFile(final String s) {
        if (!this.maasFileCache.containsKey(s)) {
            final MAASAnimation loadMAASFile = this.loadMAASFile(s);
            if (loadMAASFile != null) {
                this.maasFileCache.put(s, loadMAASFile);
            }
        }
        return this.maasFileCache.get(s);
    }
    
    private MAASAnimation loadMAASFile(String format) {
        try {
            InputStream open;
            if (this.usingSdcard) {
                open = new FileInputStream(new File(String.format("/sdcard/bishop/maas/%sAnimation.xml", format)));
            }
            else {
                format = String.format("animation/%sAnimation.xml", format);
                open = XboxTcuiSdk.getAssetManager().open(format);
            }
            return XMLHelper.instance().load(open, MAASAnimation.class);
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    public MAASAnimation getAnimation(final String s) {
        if (s != null) {
            return this.getMAASFile(s);
        }
        throw new IllegalArgumentException();
    }
    
    public enum MAASAnimationType
    {
        ANIMATE_IN, 
        ANIMATE_OUT;
    }
}
