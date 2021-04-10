package com.zhekasmirnov.apparatus.adapter.innercore.mod;

import java.io.*;
import com.zhekasmirnov.innercore.mod.build.*;

public class ApparatusMod
{
    private final File directory;
    private final ApparatusModInfo info;
    
    public ApparatusMod(final Mod mod) {
        this(new File(mod.dir));
        this.info.pullLegacyModProperties(mod);
    }
    
    public ApparatusMod(final File directory) {
        this.info = new ApparatusModInfo();
        this.directory = directory;
    }
    
    public File getDirectory() {
        return this.directory;
    }
    
    public ApparatusModInfo getInfo() {
        return this.info;
    }
}
