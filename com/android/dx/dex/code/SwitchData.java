package com.android.dx.dex.code;

import com.android.dx.rop.code.*;
import com.android.dx.util.*;

public final class SwitchData extends VariableSizeInsn
{
    private final IntList cases;
    private final boolean packed;
    private final CodeAddress[] targets;
    private final CodeAddress user;
    
    public SwitchData(final SourcePosition sourcePosition, final CodeAddress user, final IntList cases, final CodeAddress[] targets) {
        super(sourcePosition, RegisterSpecList.EMPTY);
        if (user == null) {
            throw new NullPointerException("user == null");
        }
        if (cases == null) {
            throw new NullPointerException("cases == null");
        }
        if (targets == null) {
            throw new NullPointerException("targets == null");
        }
        final int size = cases.size();
        if (size != targets.length) {
            throw new IllegalArgumentException("cases / targets mismatch");
        }
        if (size > 65535) {
            throw new IllegalArgumentException("too many cases");
        }
        this.user = user;
        this.cases = cases;
        this.targets = targets;
        this.packed = shouldPack(cases);
    }
    
    private static long packedCodeSize(final IntList list) {
        final long n = (list.get(list.size() - 1) - (long)list.get(0) + 1L) * 2L + 4L;
        if (n <= 2147483647L) {
            return n;
        }
        return -1L;
    }
    
    private static boolean shouldPack(final IntList list) {
        if (list.size() < 2) {
            return true;
        }
        final long packedCodeSize = packedCodeSize(list);
        final long sparseCodeSize = sparseCodeSize(list);
        return packedCodeSize >= 0L && packedCodeSize <= 5L * sparseCodeSize / 4L;
    }
    
    private static long sparseCodeSize(final IntList list) {
        return list.size() * 4L + 2L;
    }
    
    @Override
    protected String argString() {
        final StringBuffer sb = new StringBuffer(100);
        for (int length = this.targets.length, i = 0; i < length; ++i) {
            sb.append("\n    ");
            sb.append(this.cases.get(i));
            sb.append(": ");
            sb.append(this.targets[i]);
        }
        return sb.toString();
    }
    
    @Override
    public int codeSize() {
        long n;
        if (this.packed) {
            n = packedCodeSize(this.cases);
        }
        else {
            n = sparseCodeSize(this.cases);
        }
        return (int)n;
    }
    
    public boolean isPacked() {
        return this.packed;
    }
    
    @Override
    protected String listingString0(final boolean b) {
        final int address = this.user.getAddress();
        final StringBuffer sb = new StringBuffer(100);
        final int length = this.targets.length;
        String s;
        if (this.packed) {
            s = "packed";
        }
        else {
            s = "sparse";
        }
        sb.append(s);
        sb.append("-switch-payload // for switch @ ");
        sb.append(Hex.u2(address));
        for (int i = 0; i < length; ++i) {
            final int address2 = this.targets[i].getAddress();
            sb.append("\n  ");
            sb.append(this.cases.get(i));
            sb.append(": ");
            sb.append(Hex.u4(address2));
            sb.append(" // ");
            sb.append(Hex.s4(address2 - address));
        }
        return sb.toString();
    }
    
    @Override
    public DalvInsn withRegisters(final RegisterSpecList list) {
        return new SwitchData(this.getPosition(), this.user, this.cases, this.targets);
    }
    
    @Override
    public void writeTo(final AnnotatedOutput annotatedOutput) {
        final int address = this.user.getAddress();
        final int codeSize = Dops.PACKED_SWITCH.getFormat().codeSize();
        final int length = this.targets.length;
        final boolean packed = this.packed;
        final int n = 0;
        final int n2 = 0;
        if (packed) {
            int value;
            if (length == 0) {
                value = 0;
            }
            else {
                value = this.cases.get(0);
            }
            int value2;
            if (length == 0) {
                value2 = 0;
            }
            else {
                value2 = this.cases.get(length - 1);
            }
            final int n3 = value2 - value + 1;
            annotatedOutput.writeShort(256);
            annotatedOutput.writeShort(n3);
            annotatedOutput.writeInt(value);
            int n4 = 0;
            for (int i = n2; i < n3; ++i) {
                int n5;
                if (this.cases.get(n4) > value + i) {
                    n5 = codeSize;
                }
                else {
                    n5 = this.targets[n4].getAddress() - address;
                    ++n4;
                }
                annotatedOutput.writeInt(n5);
            }
            return;
        }
        annotatedOutput.writeShort(512);
        annotatedOutput.writeShort(length);
        for (int j = 0; j < length; ++j) {
            annotatedOutput.writeInt(this.cases.get(j));
        }
        for (int k = n; k < length; ++k) {
            annotatedOutput.writeInt(this.targets[k].getAddress() - address);
        }
    }
}
