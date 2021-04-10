package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.io.*;
import com.android.dx.util.*;
import com.android.dx.dex.code.*;
import com.android.dex.util.*;

public class DebugInfoItem extends OffsettedItem
{
    private static final int ALIGNMENT = 1;
    private static final boolean ENABLE_ENCODER_SELF_CHECK = false;
    private final DalvCode code;
    private byte[] encoded;
    private final boolean isStatic;
    private final CstMethodRef ref;
    
    public DebugInfoItem(final DalvCode code, final boolean isStatic, final CstMethodRef ref) {
        super(1, -1);
        if (code == null) {
            throw new NullPointerException("code == null");
        }
        this.code = code;
        this.isStatic = isStatic;
        this.ref = ref;
    }
    
    private byte[] encode(final DexFile dexFile, final String s, final PrintWriter printWriter, final AnnotatedOutput annotatedOutput, final boolean b) {
        return this.encode0(dexFile, s, printWriter, annotatedOutput, b);
    }
    
    private byte[] encode0(final DexFile dexFile, final String s, final PrintWriter printWriter, final AnnotatedOutput annotatedOutput, final boolean b) {
        final PositionList positions = this.code.getPositions();
        final LocalList locals = this.code.getLocals();
        final DalvInsnList insns = this.code.getInsns();
        final DebugInfoEncoder debugInfoEncoder = new DebugInfoEncoder(positions, locals, dexFile, insns.codeSize(), insns.getRegistersSize(), this.isStatic, this.ref);
        if (printWriter == null && annotatedOutput == null) {
            return debugInfoEncoder.convert();
        }
        return debugInfoEncoder.convertAndAnnotate(s, printWriter, annotatedOutput, b);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
    }
    
    public void annotateTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput, final String s) {
        this.encode(dexFile, s, null, annotatedOutput, false);
    }
    
    public void debugPrint(final PrintWriter printWriter, final String s) {
        this.encode(null, s, printWriter, null, false);
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_DEBUG_INFO_ITEM;
    }
    
    @Override
    protected void place0(final Section section, final int n) {
        try {
            this.encoded = this.encode(section.getFile(), null, null, null, false);
            this.setWriteSize(this.encoded.length);
        }
        catch (RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("...while placing debug info for ");
            sb.append(this.ref.toHuman());
            throw ExceptionWithContext.withContext(ex, sb.toString());
        }
    }
    
    @Override
    public String toHuman() {
        throw new RuntimeException("unsupported");
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" debug info");
            annotatedOutput.annotate(sb.toString());
            this.encode(dexFile, null, null, annotatedOutput, true);
        }
        annotatedOutput.write(this.encoded);
    }
}
