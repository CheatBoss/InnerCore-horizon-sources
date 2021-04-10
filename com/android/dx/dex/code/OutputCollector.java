package com.android.dx.dex.code;

import java.util.*;
import com.android.dx.dex.*;

public final class OutputCollector
{
    private final OutputFinisher finisher;
    private ArrayList<DalvInsn> suffix;
    
    public OutputCollector(final DexOptions dexOptions, final int n, final int n2, final int n3, final int n4) {
        this.finisher = new OutputFinisher(dexOptions, n, n3, n4);
        this.suffix = new ArrayList<DalvInsn>(n2);
    }
    
    private void appendSuffixToOutput() {
        for (int size = this.suffix.size(), i = 0; i < size; ++i) {
            this.finisher.add(this.suffix.get(i));
        }
        this.suffix = null;
    }
    
    public void add(final DalvInsn dalvInsn) {
        this.finisher.add(dalvInsn);
    }
    
    public void addSuffix(final DalvInsn dalvInsn) {
        this.suffix.add(dalvInsn);
    }
    
    public OutputFinisher getFinisher() {
        if (this.suffix == null) {
            throw new UnsupportedOperationException("already processed");
        }
        this.appendSuffixToOutput();
        return this.finisher;
    }
    
    public void reverseBranch(final int n, final CodeAddress codeAddress) {
        this.finisher.reverseBranch(n, codeAddress);
    }
}
