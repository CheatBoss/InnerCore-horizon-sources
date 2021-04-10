package com.android.dx.dex.file;

import java.io.*;
import com.android.dx.util.*;
import com.android.dx.rop.cst.*;

public abstract class EncodedMember implements ToHuman
{
    private final int accessFlags;
    
    public EncodedMember(final int accessFlags) {
        this.accessFlags = accessFlags;
    }
    
    public abstract void addContents(final DexFile p0);
    
    public abstract void debugPrint(final PrintWriter p0, final boolean p1);
    
    public abstract int encode(final DexFile p0, final AnnotatedOutput p1, final int p2, final int p3);
    
    public final int getAccessFlags() {
        return this.accessFlags;
    }
    
    public abstract CstString getName();
}
