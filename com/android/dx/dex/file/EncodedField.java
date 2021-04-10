package com.android.dx.dex.file;

import java.io.*;
import com.android.dex.*;
import com.android.dx.util.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.cst.*;

public final class EncodedField extends EncodedMember implements Comparable<EncodedField>
{
    private final CstFieldRef field;
    
    public EncodedField(final CstFieldRef field, final int n) {
        super(n);
        if (field == null) {
            throw new NullPointerException("field == null");
        }
        this.field = field;
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        dexFile.getFieldIds().intern(this.field);
    }
    
    @Override
    public int compareTo(final EncodedField encodedField) {
        return this.field.compareTo((Constant)encodedField.field);
    }
    
    @Override
    public void debugPrint(final PrintWriter printWriter, final boolean b) {
        printWriter.println(this.toString());
    }
    
    @Override
    public int encode(final DexFile dexFile, final AnnotatedOutput annotatedOutput, int n, int n2) {
        final int index = dexFile.getFieldIds().indexOf(this.field);
        n = index - n;
        final int accessFlags = this.getAccessFlags();
        if (annotatedOutput.annotates()) {
            annotatedOutput.annotate(0, String.format("  [%x] %s", n2, this.field.toHuman()));
            n2 = Leb128.unsignedLeb128Size(n);
            final StringBuilder sb = new StringBuilder();
            sb.append("    field_idx:    ");
            sb.append(Hex.u4(index));
            annotatedOutput.annotate(n2, sb.toString());
            n2 = Leb128.unsignedLeb128Size(accessFlags);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("    access_flags: ");
            sb2.append(AccessFlags.fieldString(accessFlags));
            annotatedOutput.annotate(n2, sb2.toString());
        }
        annotatedOutput.writeUleb128(n);
        annotatedOutput.writeUleb128(accessFlags);
        return index;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof EncodedField;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (this.compareTo((EncodedField)o) == 0) {
            b2 = true;
        }
        return b2;
    }
    
    @Override
    public CstString getName() {
        return this.field.getNat().getName();
    }
    
    public CstFieldRef getRef() {
        return this.field;
    }
    
    @Override
    public int hashCode() {
        return this.field.hashCode();
    }
    
    @Override
    public String toHuman() {
        return this.field.toHuman();
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer(100);
        sb.append(this.getClass().getName());
        sb.append('{');
        sb.append(Hex.u2(this.getAccessFlags()));
        sb.append(' ');
        sb.append(this.field);
        sb.append('}');
        return sb.toString();
    }
}
