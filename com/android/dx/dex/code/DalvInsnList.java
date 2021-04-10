package com.android.dx.dex.code;

import java.util.*;
import java.io.*;
import com.android.dx.rop.cst.*;
import com.android.dx.util.*;
import com.android.dex.util.*;

public final class DalvInsnList extends FixedSizeList
{
    private final int regCount;
    
    public DalvInsnList(final int n, final int regCount) {
        super(n);
        this.regCount = regCount;
    }
    
    public static DalvInsnList makeImmutable(final ArrayList<DalvInsn> list, int i) {
        final int size = list.size();
        final DalvInsnList list2 = new DalvInsnList(size, i);
        for (i = 0; i < size; ++i) {
            list2.set(i, list.get(i));
        }
        list2.setImmutable();
        return list2;
    }
    
    public int codeSize() {
        final int size = this.size();
        if (size == 0) {
            return 0;
        }
        return this.get(size - 1).getNextAddress();
    }
    
    public void debugPrint(final OutputStream outputStream, final String s, final boolean b) {
        final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
        this.debugPrint(outputStreamWriter, s, b);
        try {
            outputStreamWriter.flush();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void debugPrint(final Writer writer, final String s, final boolean b) {
        final IndentingWriter indentingWriter = new IndentingWriter(writer, 0, s);
        final int size = this.size();
        int n = 0;
    Label_0072_Outer:
        while (true) {
            while (true) {
                if (n < size) {
                    while (true) {
                        try {
                            final DalvInsn dalvInsn = (DalvInsn)this.get0(n);
                            String listingString;
                            if (dalvInsn.codeSize() != 0 || b) {
                                listingString = dalvInsn.listingString("", 0, b);
                            }
                            else {
                                listingString = null;
                            }
                            if (listingString != null) {
                                indentingWriter.write(listingString);
                            }
                            ++n;
                            continue Label_0072_Outer;
                            indentingWriter.flush();
                            return;
                            final IOException ex;
                            throw new RuntimeException(ex);
                        }
                        catch (IOException ex2) {}
                        final IOException ex2;
                        final IOException ex = ex2;
                        continue;
                    }
                }
                continue;
            }
        }
    }
    
    public DalvInsn get(final int n) {
        return (DalvInsn)this.get0(n);
    }
    
    public int getOutsSize() {
        final int size = this.size();
        int n = 0;
        int n2;
        for (int i = 0; i < size; ++i, n = n2) {
            final DalvInsn dalvInsn = (DalvInsn)this.get0(i);
            if (!(dalvInsn instanceof CstInsn)) {
                n2 = n;
            }
            else {
                final Constant constant = ((CstInsn)dalvInsn).getConstant();
                if (!(constant instanceof CstBaseMethodRef)) {
                    n2 = n;
                }
                else {
                    final int parameterWordCount = ((CstBaseMethodRef)constant).getParameterWordCount(dalvInsn.getOpcode().getFamily() == 113);
                    if (parameterWordCount > (n2 = n)) {
                        n2 = parameterWordCount;
                    }
                }
            }
        }
        return n;
    }
    
    public int getRegistersSize() {
        return this.regCount;
    }
    
    public void set(final int n, final DalvInsn dalvInsn) {
        this.set0(n, dalvInsn);
    }
    
    public void writeTo(final AnnotatedOutput annotatedOutput) {
        final int cursor = annotatedOutput.getCursor();
        final int size = this.size();
        final boolean annotates = annotatedOutput.annotates();
        final int n = 0;
        if (annotates) {
            final boolean verbose = annotatedOutput.isVerbose();
            for (int i = 0; i < size; ++i) {
                final DalvInsn dalvInsn = (DalvInsn)this.get0(i);
                final int n2 = dalvInsn.codeSize() * 2;
                String listingString;
                if (n2 == 0 && !verbose) {
                    listingString = null;
                }
                else {
                    listingString = dalvInsn.listingString("  ", annotatedOutput.getAnnotationWidth(), true);
                }
                if (listingString != null) {
                    annotatedOutput.annotate(n2, listingString);
                }
                else if (n2 != 0) {
                    annotatedOutput.annotate(n2, "");
                }
            }
        }
        int j = n;
        while (j < size) {
            final DalvInsn dalvInsn2 = (DalvInsn)this.get0(j);
            try {
                dalvInsn2.writeTo(annotatedOutput);
                ++j;
                continue;
            }
            catch (RuntimeException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("...while writing ");
                sb.append(dalvInsn2);
                throw ExceptionWithContext.withContext(ex, sb.toString());
            }
            break;
        }
        final int n3 = (annotatedOutput.getCursor() - cursor) / 2;
        if (n3 != this.codeSize()) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("write length mismatch; expected ");
            sb2.append(this.codeSize());
            sb2.append(" but actually wrote ");
            sb2.append(n3);
            throw new RuntimeException(sb2.toString());
        }
    }
}
