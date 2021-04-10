package com.android.dx.dex.file;

import com.android.dx.dex.code.*;
import com.android.dx.rop.type.*;
import java.io.*;
import com.android.dex.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;

public final class EncodedMethod extends EncodedMember implements Comparable<EncodedMethod>
{
    private final CodeItem code;
    private final CstMethodRef method;
    
    public EncodedMethod(final CstMethodRef method, final int n, final DalvCode dalvCode, final TypeList list) {
        super(n);
        if (method == null) {
            throw new NullPointerException("method == null");
        }
        this.method = method;
        if (dalvCode == null) {
            this.code = null;
            return;
        }
        this.code = new CodeItem(method, dalvCode, (n & 0x8) != 0x0, list);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        final MethodIdsSection methodIds = dexFile.getMethodIds();
        final MixedItemSection wordData = dexFile.getWordData();
        methodIds.intern(this.method);
        if (this.code != null) {
            wordData.add(this.code);
        }
    }
    
    @Override
    public int compareTo(final EncodedMethod encodedMethod) {
        return this.method.compareTo((Constant)encodedMethod.method);
    }
    
    @Override
    public void debugPrint(final PrintWriter printWriter, final boolean b) {
        if (this.code == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getRef().toHuman());
            sb.append(": abstract or native");
            printWriter.println(sb.toString());
            return;
        }
        this.code.debugPrint(printWriter, "  ", b);
    }
    
    @Override
    public int encode(final DexFile dexFile, final AnnotatedOutput annotatedOutput, int n, final int n2) {
        final int index = dexFile.getMethodIds().indexOf(this.method);
        final int n3 = index - n;
        final int accessFlags = this.getAccessFlags();
        final int absoluteOffsetOr0 = OffsettedItem.getAbsoluteOffsetOr0(this.code);
        if (absoluteOffsetOr0 != 0) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != (((accessFlags & 0x500) == 0x0) ? 1 : 0)) {
            throw new UnsupportedOperationException("code vs. access_flags mismatch");
        }
        if (annotatedOutput.annotates()) {
            annotatedOutput.annotate(0, String.format("  [%x] %s", n2, this.method.toHuman()));
            n = Leb128.unsignedLeb128Size(n3);
            final StringBuilder sb = new StringBuilder();
            sb.append("    method_idx:   ");
            sb.append(Hex.u4(index));
            annotatedOutput.annotate(n, sb.toString());
            n = Leb128.unsignedLeb128Size(accessFlags);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("    access_flags: ");
            sb2.append(AccessFlags.methodString(accessFlags));
            annotatedOutput.annotate(n, sb2.toString());
            n = Leb128.unsignedLeb128Size(absoluteOffsetOr0);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("    code_off:     ");
            sb3.append(Hex.u4(absoluteOffsetOr0));
            annotatedOutput.annotate(n, sb3.toString());
        }
        annotatedOutput.writeUleb128(n3);
        annotatedOutput.writeUleb128(accessFlags);
        annotatedOutput.writeUleb128(absoluteOffsetOr0);
        return index;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof EncodedMethod;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (this.compareTo((EncodedMethod)o) == 0) {
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public final CstString getName() {
        return this.method.getNat().getName();
    }
    
    public final CstMethodRef getRef() {
        return this.method;
    }
    
    @Override
    public final String toHuman() {
        return this.method.toHuman();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Hex.u2(this.getAccessFlags()));
        sb.append(' ');
        sb.append(this.method);
        if (this.code != null) {
            sb.append(' ');
            sb.append(this.code);
        }
        sb.append('}');
        return sb.toString();
    }
}
