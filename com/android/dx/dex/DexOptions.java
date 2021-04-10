package com.android.dx.dex;

import com.android.dex.*;

public class DexOptions
{
    public static final boolean ALIGN_64BIT_REGS_SUPPORT = true;
    public boolean ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER;
    public boolean forceJumbo;
    public int targetApiLevel;
    
    public DexOptions() {
        this.ALIGN_64BIT_REGS_IN_OUTPUT_FINISHER = true;
        this.targetApiLevel = 13;
        this.forceJumbo = false;
    }
    
    public String getMagic() {
        return DexFormat.apiToMagic(this.targetApiLevel);
    }
}
