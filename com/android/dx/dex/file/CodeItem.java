package com.android.dx.dex.file;

import com.android.dex.util.*;
import com.android.dx.dex.code.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.util.*;
import java.io.*;
import com.android.dx.rop.type.*;

public final class CodeItem extends OffsettedItem
{
    private static final int ALIGNMENT = 4;
    private static final int HEADER_SIZE = 16;
    private CatchStructs catches;
    private final DalvCode code;
    private DebugInfoItem debugInfo;
    private final boolean isStatic;
    private final CstMethodRef ref;
    private final TypeList throwsList;
    
    public CodeItem(final CstMethodRef ref, final DalvCode code, final boolean isStatic, final TypeList throwsList) {
        super(4, -1);
        if (ref == null) {
            throw new NullPointerException("ref == null");
        }
        if (code == null) {
            throw new NullPointerException("code == null");
        }
        if (throwsList == null) {
            throw new NullPointerException("throwsList == null");
        }
        this.ref = ref;
        this.code = code;
        this.isStatic = isStatic;
        this.throwsList = throwsList;
        this.catches = null;
        this.debugInfo = null;
    }
    
    private int getInsSize() {
        return this.ref.getParameterWordCount(this.isStatic);
    }
    
    private int getOutsSize() {
        return this.code.getInsns().getOutsSize();
    }
    
    private int getRegistersSize() {
        return this.code.getInsns().getRegistersSize();
    }
    
    private void writeCodes(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final DalvInsnList insns = this.code.getInsns();
        try {
            insns.writeTo(annotatedOutput);
        }
        catch (RuntimeException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("...while writing instructions for ");
            sb.append(this.ref.toHuman());
            throw ExceptionWithContext.withContext(ex, sb.toString());
        }
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final MixedItemSection byteData = dexFile.getByteData();
        final TypeIdsSection typeIds = dexFile.getTypeIds();
        if (this.code.hasPositions() || this.code.hasLocals()) {
            byteData.add(this.debugInfo = new DebugInfoItem(this.code, this.isStatic, this.ref));
        }
        if (this.code.hasAnyCatches()) {
            final Iterator<Type> iterator = this.code.getCatchTypes().iterator();
            while (iterator.hasNext()) {
                typeIds.intern(iterator.next());
            }
            this.catches = new CatchStructs(this.code);
        }
        final Iterator<Constant> iterator2 = this.code.getInsnConstants().iterator();
        while (iterator2.hasNext()) {
            dexFile.internIfAppropriate(iterator2.next());
        }
    }
    
    public void debugPrint(final PrintWriter printWriter, final String s, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.ref.toHuman());
        sb.append(":");
        printWriter.println(sb.toString());
        final DalvInsnList insns = this.code.getInsns();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("regs: ");
        sb2.append(Hex.u2(this.getRegistersSize()));
        sb2.append("; ins: ");
        sb2.append(Hex.u2(this.getInsSize()));
        sb2.append("; outs: ");
        sb2.append(Hex.u2(this.getOutsSize()));
        printWriter.println(sb2.toString());
        insns.debugPrint(printWriter, s, b);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("  ");
        final String string = sb3.toString();
        if (this.catches != null) {
            printWriter.print(s);
            printWriter.println("catches");
            this.catches.debugPrint(printWriter, string);
        }
        if (this.debugInfo != null) {
            printWriter.print(s);
            printWriter.println("debug info");
            this.debugInfo.debugPrint(printWriter, string);
        }
    }
    
    public CstMethodRef getRef() {
        return this.ref;
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_CODE_ITEM;
    }
    
    @Override
    protected void place0(final Section section, int writeSize) {
        final DexFile file = section.getFile();
        this.code.assignIndices((DalvCode.AssignIndicesCallback)new DalvCode.AssignIndicesCallback() {
            @Override
            public int getIndex(final Constant constant) {
                final IndexedItem itemOrNull = file.findItemOrNull(constant);
                if (itemOrNull == null) {
                    return -1;
                }
                return itemOrNull.getIndex();
            }
        });
        if (this.catches != null) {
            this.catches.encode(file);
            writeSize = this.catches.writeSize();
        }
        else {
            writeSize = 0;
        }
        int codeSize;
        final int n = codeSize = this.code.getInsns().codeSize();
        if ((n & 0x1) != 0x0) {
            codeSize = n + 1;
        }
        this.setWriteSize(codeSize * 2 + 16 + writeSize);
    }
    
    @Override
    public String toHuman() {
        return this.ref.toHuman();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("CodeItem{");
        sb.append(this.toHuman());
        sb.append("}");
        return sb.toString();
    }
    
    @Override
    protected void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final boolean annotates = annotatedOutput.annotates();
        final int registersSize = this.getRegistersSize();
        final int outsSize = this.getOutsSize();
        final int insSize = this.getInsSize();
        final int codeSize = this.code.getInsns().codeSize();
        final boolean b = (codeSize & 0x1) != 0x0;
        int triesSize;
        if (this.catches == null) {
            triesSize = 0;
        }
        else {
            triesSize = this.catches.triesSize();
        }
        int absoluteOffset;
        if (this.debugInfo == null) {
            absoluteOffset = 0;
        }
        else {
            absoluteOffset = this.debugInfo.getAbsoluteOffset();
        }
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(' ');
            sb.append(this.ref.toHuman());
            annotatedOutput.annotate(0, sb.toString());
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  registers_size: ");
            sb2.append(Hex.u2(registersSize));
            annotatedOutput.annotate(2, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  ins_size:       ");
            sb3.append(Hex.u2(insSize));
            annotatedOutput.annotate(2, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  outs_size:      ");
            sb4.append(Hex.u2(outsSize));
            annotatedOutput.annotate(2, sb4.toString());
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("  tries_size:     ");
            sb5.append(Hex.u2(triesSize));
            annotatedOutput.annotate(2, sb5.toString());
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("  debug_off:      ");
            sb6.append(Hex.u4(absoluteOffset));
            annotatedOutput.annotate(4, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("  insns_size:     ");
            sb7.append(Hex.u4(codeSize));
            annotatedOutput.annotate(4, sb7.toString());
            if (this.throwsList.size() != 0) {
                final StringBuilder sb8 = new StringBuilder();
                sb8.append("  throws ");
                sb8.append(StdTypeList.toHuman(this.throwsList));
                annotatedOutput.annotate(0, sb8.toString());
            }
        }
        annotatedOutput.writeShort(registersSize);
        annotatedOutput.writeShort(insSize);
        annotatedOutput.writeShort(outsSize);
        annotatedOutput.writeShort(triesSize);
        annotatedOutput.writeInt(absoluteOffset);
        annotatedOutput.writeInt(codeSize);
        this.writeCodes(dexFile, annotatedOutput);
        if (this.catches != null) {
            if (b) {
                if (annotates) {
                    annotatedOutput.annotate(2, "  padding: 0");
                }
                annotatedOutput.writeShort(0);
            }
            this.catches.writeTo(dexFile, annotatedOutput);
        }
        if (annotates && this.debugInfo != null) {
            annotatedOutput.annotate(0, "  debug info");
            this.debugInfo.annotateTo(dexFile, annotatedOutput, "    ");
        }
    }
}
