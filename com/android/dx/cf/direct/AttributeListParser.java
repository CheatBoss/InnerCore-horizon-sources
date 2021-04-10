package com.android.dx.cf.direct;

import com.android.dx.util.*;
import com.android.dx.cf.iface.*;

final class AttributeListParser
{
    private final AttributeFactory attributeFactory;
    private final DirectClassFile cf;
    private final int context;
    private int endOffset;
    private final StdAttributeList list;
    private ParseObserver observer;
    private final int offset;
    
    public AttributeListParser(final DirectClassFile cf, final int context, final int offset, final AttributeFactory attributeFactory) {
        if (cf == null) {
            throw new NullPointerException("cf == null");
        }
        if (attributeFactory == null) {
            throw new NullPointerException("attributeFactory == null");
        }
        final int unsignedShort = cf.getBytes().getUnsignedShort(offset);
        this.cf = cf;
        this.context = context;
        this.offset = offset;
        this.attributeFactory = attributeFactory;
        this.list = new StdAttributeList(unsignedShort);
        this.endOffset = -1;
    }
    
    private void parse() {
        final int size = this.list.size();
        final int offset = this.offset;
        final ByteArray bytes = this.cf.getBytes();
        if (this.observer != null) {
            final ParseObserver observer = this.observer;
            final int offset2 = this.offset;
            final StringBuilder sb = new StringBuilder();
            sb.append("attributes_count: ");
            sb.append(Hex.u2(size));
            observer.parsed(bytes, offset2, 2, sb.toString());
        }
        int endOffset = offset + 2;
        int i = 0;
        while (i < size) {
            try {
                if (this.observer != null) {
                    final ParseObserver observer2 = this.observer;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("\nattributes[");
                    sb2.append(i);
                    sb2.append("]:\n");
                    observer2.parsed(bytes, endOffset, 0, sb2.toString());
                    this.observer.changeIndent(1);
                }
                final Attribute parse = this.attributeFactory.parse(this.cf, this.context, endOffset, this.observer);
                endOffset += parse.byteLength();
                this.list.set(i, parse);
                if (this.observer != null) {
                    this.observer.changeIndent(-1);
                    final ParseObserver observer3 = this.observer;
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("end attributes[");
                    sb3.append(i);
                    sb3.append("]\n");
                    observer3.parsed(bytes, endOffset, 0, sb3.toString());
                }
                ++i;
                continue;
            }
            catch (RuntimeException ex2) {
                final ParseException ex = new ParseException(ex2);
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("...while parsing attributes[");
                sb4.append(i);
                sb4.append("]");
                ex.addContext(sb4.toString());
                throw ex;
            }
            catch (ParseException ex3) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("...while parsing attributes[");
                sb5.append(i);
                sb5.append("]");
                ex3.addContext(sb5.toString());
                throw ex3;
            }
            break;
        }
        this.endOffset = endOffset;
    }
    
    private void parseIfNecessary() {
        if (this.endOffset < 0) {
            this.parse();
        }
    }
    
    public int getEndOffset() {
        this.parseIfNecessary();
        return this.endOffset;
    }
    
    public StdAttributeList getList() {
        this.parseIfNecessary();
        return this.list;
    }
    
    public void setObserver(final ParseObserver observer) {
        this.observer = observer;
    }
}
