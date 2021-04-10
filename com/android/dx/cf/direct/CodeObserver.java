package com.android.dx.cf.direct;

import com.android.dx.cf.iface.*;
import com.android.dx.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.cst.*;
import java.util.*;
import com.android.dx.cf.code.*;

public class CodeObserver implements Visitor
{
    private final ByteArray bytes;
    private final ParseObserver observer;
    
    public CodeObserver(final ByteArray bytes, final ParseObserver observer) {
        if (bytes == null) {
            throw new NullPointerException("bytes == null");
        }
        if (observer == null) {
            throw new NullPointerException("observer == null");
        }
        this.bytes = bytes;
        this.observer = observer;
    }
    
    private String header(final int n) {
        final int unsignedByte = this.bytes.getUnsignedByte(n);
        String s = ByteOps.opName(unsignedByte);
        if (unsignedByte == 196) {
            final int unsignedByte2 = this.bytes.getUnsignedByte(n + 1);
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" ");
            sb.append(ByteOps.opName(unsignedByte2));
            s = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(Hex.u2(n));
        sb2.append(": ");
        sb2.append(s);
        return sb2.toString();
    }
    
    private void visitLiteralDouble(final int n, final int n2, final int n3, final long n4) {
        String string;
        if (n3 != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" #");
            sb.append(Hex.u8(n4));
            string = sb.toString();
        }
        else {
            string = "";
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.header(n2));
        sb2.append(string);
        sb2.append(" // ");
        sb2.append(Double.longBitsToDouble(n4));
        observer.parsed(bytes, n2, n3, sb2.toString());
    }
    
    private void visitLiteralFloat(final int n, final int n2, final int n3, final int n4) {
        String string;
        if (n3 != 1) {
            final StringBuilder sb = new StringBuilder();
            sb.append(" #");
            sb.append(Hex.u4(n4));
            string = sb.toString();
        }
        else {
            string = "";
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(this.header(n2));
        sb2.append(string);
        sb2.append(" // ");
        sb2.append(Float.intBitsToFloat(n4));
        observer.parsed(bytes, n2, n3, sb2.toString());
    }
    
    private void visitLiteralInt(int unsignedByte, final int n, final int n2, final int n3) {
        String s;
        if (n2 == 1) {
            s = " // ";
        }
        else {
            s = " ";
        }
        unsignedByte = this.bytes.getUnsignedByte(n);
        String s2;
        if (n2 != 1 && unsignedByte != 16) {
            if (unsignedByte == 17) {
                final StringBuilder sb = new StringBuilder();
                sb.append("#");
                sb.append(Hex.s2(n3));
                s2 = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("#");
                sb2.append(Hex.s4(n3));
                s2 = sb2.toString();
            }
        }
        else {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("#");
            sb3.append(Hex.s1(n3));
            s2 = sb3.toString();
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(this.header(n));
        sb4.append(s);
        sb4.append(s2);
        observer.parsed(bytes, n, n2, sb4.toString());
    }
    
    private void visitLiteralLong(final int n, final int n2, final int n3, final long n4) {
        String s;
        if (n3 == 1) {
            s = " // ";
        }
        else {
            s = " #";
        }
        String s2;
        if (n3 == 1) {
            s2 = Hex.s1((int)n4);
        }
        else {
            s2 = Hex.s8(n4);
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.header(n2));
        sb.append(s);
        sb.append(s2);
        observer.parsed(bytes, n2, n3, sb.toString());
    }
    
    @Override
    public int getPreviousOffset() {
        return -1;
    }
    
    @Override
    public void setPreviousOffset(final int n) {
    }
    
    @Override
    public void visitBranch(final int n, final int n2, final int n3, final int n4) {
        String s;
        if (n3 <= 3) {
            s = Hex.u2(n4);
        }
        else {
            s = Hex.u4(n4);
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.header(n2));
        sb.append(" ");
        sb.append(s);
        observer.parsed(bytes, n2, n3, sb.toString());
    }
    
    @Override
    public void visitConstant(final int n, final int n2, final int n3, final Constant constant, final int n4) {
        if (constant instanceof CstKnownNull) {
            this.visitNoArgs(n, n2, n3, null);
            return;
        }
        if (constant instanceof CstInteger) {
            this.visitLiteralInt(n, n2, n3, n4);
            return;
        }
        if (constant instanceof CstLong) {
            this.visitLiteralLong(n, n2, n3, ((CstLong)constant).getValue());
            return;
        }
        if (constant instanceof CstFloat) {
            this.visitLiteralFloat(n, n2, n3, ((CstFloat)constant).getIntBits());
            return;
        }
        if (constant instanceof CstDouble) {
            this.visitLiteralDouble(n, n2, n3, ((CstDouble)constant).getLongBits());
            return;
        }
        String s = "";
        if (n4 != 0) {
            if (n == 197) {
                final StringBuilder sb = new StringBuilder();
                sb.append(", ");
                sb.append(Hex.u1(n4));
                s = sb.toString();
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(", ");
                sb2.append(Hex.u2(n4));
                s = sb2.toString();
            }
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.header(n2));
        sb3.append(" ");
        sb3.append(constant);
        sb3.append(s);
        observer.parsed(bytes, n2, n3, sb3.toString());
    }
    
    @Override
    public void visitInvalid(final int n, final int n2, final int n3) {
        this.observer.parsed(this.bytes, n2, n3, this.header(n2));
    }
    
    @Override
    public void visitLocal(final int n, final int n2, final int n3, int n4, final Type type, final int n5) {
        String s;
        if (n3 <= 3) {
            s = Hex.u1(n4);
        }
        else {
            s = Hex.u2(n4);
        }
        n4 = 1;
        if (n3 != 1) {
            n4 = 0;
        }
        String string = "";
        if (n == 132) {
            final StringBuilder sb = new StringBuilder();
            sb.append(", #");
            String s2;
            if (n3 <= 3) {
                s2 = Hex.s1(n5);
            }
            else {
                s2 = Hex.s2(n5);
            }
            sb.append(s2);
            string = sb.toString();
        }
        String string2 = "";
        if (type.isCategory2()) {
            final StringBuilder sb2 = new StringBuilder();
            String s3;
            if (n4 != 0) {
                s3 = ",";
            }
            else {
                s3 = " //";
            }
            sb2.append(s3);
            sb2.append(" category-2");
            string2 = sb2.toString();
        }
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(this.header(n2));
        String s4;
        if (n4 != 0) {
            s4 = " // ";
        }
        else {
            s4 = " ";
        }
        sb3.append(s4);
        sb3.append(s);
        sb3.append(string);
        sb3.append(string2);
        observer.parsed(bytes, n2, n3, sb3.toString());
    }
    
    @Override
    public void visitNewarray(final int n, final int n2, final CstType cstType, final ArrayList<Constant> list) {
        String s;
        if (n2 == 1) {
            s = " // ";
        }
        else {
            s = " ";
        }
        final String human = cstType.getClassType().getComponentType().toHuman();
        final ParseObserver observer = this.observer;
        final ByteArray bytes = this.bytes;
        final StringBuilder sb = new StringBuilder();
        sb.append(this.header(n));
        sb.append(s);
        sb.append(human);
        observer.parsed(bytes, n, n2, sb.toString());
    }
    
    @Override
    public void visitNoArgs(final int n, final int n2, final int n3, final Type type) {
        this.observer.parsed(this.bytes, n2, n3, this.header(n2));
    }
    
    @Override
    public void visitSwitch(int i, final int n, final int n2, final SwitchList list, final int n3) {
        final int size = list.size();
        final StringBuffer sb = new StringBuffer(size * 20 + 100);
        sb.append(this.header(n));
        if (n3 != 0) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(" // padding: ");
            sb2.append(Hex.u4(n3));
            sb.append(sb2.toString());
        }
        sb.append('\n');
        for (i = 0; i < size; ++i) {
            sb.append("  ");
            sb.append(Hex.s4(list.getValue(i)));
            sb.append(": ");
            sb.append(Hex.u2(list.getTarget(i)));
            sb.append('\n');
        }
        sb.append("  default: ");
        sb.append(Hex.u2(list.getDefaultTarget()));
        this.observer.parsed(this.bytes, n, n2, sb.toString());
    }
}
