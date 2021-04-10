package com.android.dx.cf.direct;

import com.android.dx.util.*;
import com.android.dx.rop.cst.*;
import com.android.dx.cf.iface.*;
import com.android.dex.util.*;

abstract class MemberListParser
{
    private final AttributeFactory attributeFactory;
    private final DirectClassFile cf;
    private final CstType definer;
    private int endOffset;
    private ParseObserver observer;
    private final int offset;
    
    public MemberListParser(final DirectClassFile cf, final CstType definer, final int offset, final AttributeFactory attributeFactory) {
        if (cf == null) {
            throw new NullPointerException("cf == null");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }
        if (attributeFactory == null) {
            throw new NullPointerException("attributeFactory == null");
        }
        this.cf = cf;
        this.definer = definer;
        this.offset = offset;
        this.attributeFactory = attributeFactory;
        this.endOffset = -1;
    }
    
    private void parse() {
        final int attributeContext = this.getAttributeContext();
        final int count = this.getCount();
        final int offset = this.offset;
        final ByteArray bytes = this.cf.getBytes();
        final ConstantPool constantPool = this.cf.getConstantPool();
        if (this.observer != null) {
            final ParseObserver observer = this.observer;
            final int offset2 = this.offset;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.humanName());
            sb.append("s_count: ");
            sb.append(Hex.u2(count));
            observer.parsed(bytes, offset2, 2, sb.toString());
        }
        int endOffset = offset + 2;
        int i = 0;
        while (i < count) {
            Label_0785: {
                try {
                    final int unsignedShort = bytes.getUnsignedShort(endOffset);
                    final int unsignedShort2 = bytes.getUnsignedShort(endOffset + 2);
                    final int unsignedShort3 = bytes.getUnsignedShort(endOffset + 4);
                    final CstString cstString = (CstString)constantPool.get(unsignedShort2);
                    final CstString cstString2 = (CstString)constantPool.get(unsignedShort3);
                    if (this.observer != null) {
                        try {
                            this.observer.startParsingMember(bytes, endOffset, cstString.getString(), cstString2.getString());
                            final ParseObserver observer2 = this.observer;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("\n");
                            sb2.append(this.humanName());
                            sb2.append("s[");
                            sb2.append(i);
                            sb2.append("]:\n");
                            observer2.parsed(bytes, endOffset, 0, sb2.toString());
                            this.observer.changeIndent(1);
                            final ParseObserver observer3 = this.observer;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("access_flags: ");
                            sb3.append(this.humanAccessFlags(unsignedShort));
                            observer3.parsed(bytes, endOffset, 2, sb3.toString());
                            final ParseObserver observer4 = this.observer;
                            final StringBuilder sb4 = new StringBuilder();
                            try {
                                sb4.append("name: ");
                                sb4.append(cstString.toHuman());
                                observer4.parsed(bytes, endOffset + 2, 2, sb4.toString());
                                final ParseObserver observer5 = this.observer;
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append("descriptor: ");
                                sb5.append(cstString2.toHuman());
                                observer5.parsed(bytes, endOffset + 4, 2, sb5.toString());
                            }
                            catch (RuntimeException bytes) {}
                            catch (ParseException bytes) {}
                        }
                        catch (RuntimeException bytes) {
                            goto Label_0709;
                        }
                        catch (ParseException bytes) {
                            break Label_0785;
                        }
                    }
                    try {
                        final AttributeListParser attributeListParser = new AttributeListParser(this.cf, attributeContext, endOffset + 6, this.attributeFactory);
                        attributeListParser.setObserver(this.observer);
                        endOffset = attributeListParser.getEndOffset();
                        try {
                            final StdAttributeList list = attributeListParser.getList();
                            list.setImmutable();
                            final Member set = this.set(i, unsignedShort, new CstNat(cstString, cstString2), list);
                            if (this.observer != null) {
                                final ParseObserver observer6 = this.observer;
                                try {
                                    observer6.changeIndent(-1);
                                    final ParseObserver observer7 = this.observer;
                                    final StringBuilder sb6 = new StringBuilder();
                                    sb6.append("end ");
                                    sb6.append(this.humanName());
                                    sb6.append("s[");
                                    sb6.append(i);
                                    sb6.append("]\n");
                                    observer7.parsed(bytes, endOffset, 0, sb6.toString());
                                    this.observer.endParsingMember(bytes, endOffset, cstString.getString(), cstString2.getString(), set);
                                }
                                catch (RuntimeException bytes) {
                                    goto Label_0709;
                                }
                                catch (ParseException bytes) {
                                    break Label_0785;
                                }
                            }
                            ++i;
                        }
                        catch (RuntimeException bytes) {}
                        catch (ParseException bytes) {}
                    }
                    catch (RuntimeException ex) {}
                    catch (ParseException ex2) {}
                }
                catch (RuntimeException ex3) {}
                catch (ParseException ex4) {}
            }
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("...while parsing ");
            sb7.append(this.humanName());
            sb7.append("s[");
            sb7.append(i);
            sb7.append("]");
            ((ExceptionWithContext)bytes).addContext(sb7.toString());
            throw bytes;
        }
        this.endOffset = endOffset;
    }
    
    protected abstract int getAttributeContext();
    
    protected final int getCount() {
        return this.cf.getBytes().getUnsignedShort(this.offset);
    }
    
    protected final CstType getDefiner() {
        return this.definer;
    }
    
    public int getEndOffset() {
        this.parseIfNecessary();
        return this.endOffset;
    }
    
    protected abstract String humanAccessFlags(final int p0);
    
    protected abstract String humanName();
    
    protected final void parseIfNecessary() {
        if (this.endOffset < 0) {
            this.parse();
        }
    }
    
    protected abstract Member set(final int p0, final int p1, final CstNat p2, final AttributeList p3);
    
    public final void setObserver(final ParseObserver observer) {
        this.observer = observer;
    }
}
