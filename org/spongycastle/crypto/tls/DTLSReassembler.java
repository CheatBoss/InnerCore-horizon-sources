package org.spongycastle.crypto.tls;

import java.util.*;

class DTLSReassembler
{
    private byte[] body;
    private Vector missing;
    private short msg_type;
    
    DTLSReassembler(final short msg_type, final int n) {
        final Vector<Range> missing = new Vector<Range>();
        this.missing = missing;
        this.msg_type = msg_type;
        this.body = new byte[n];
        missing.addElement(new Range(0, n));
    }
    
    void contributeFragment(final short n, int n2, final byte[] array, final int n3, final int n4, int max) {
        final int n5 = n4 + max;
        if (this.msg_type == n && this.body.length == n2) {
            if (n5 > n2) {
                return;
            }
            int i = 0;
            if (max == 0) {
                if (n4 == 0 && !this.missing.isEmpty() && this.missing.firstElement().getEnd() == 0) {
                    this.missing.removeElementAt(0);
                }
                return;
            }
            while (i < this.missing.size()) {
                final Range range = this.missing.elementAt(i);
                if (range.getStart() >= n5) {
                    return;
                }
                n2 = i;
                if (range.getEnd() > n4) {
                    max = Math.max(range.getStart(), n4);
                    final int min = Math.min(range.getEnd(), n5);
                    System.arraycopy(array, n3 + max - n4, this.body, max, min - max);
                    if (max == range.getStart()) {
                        if (min == range.getEnd()) {
                            this.missing.removeElementAt(i);
                            n2 = i - 1;
                        }
                        else {
                            range.setStart(min);
                            n2 = i;
                        }
                    }
                    else {
                        n2 = i;
                        if (min != range.getEnd()) {
                            final Vector missing = this.missing;
                            final Range range2 = new Range(min, range.getEnd());
                            n2 = i + 1;
                            missing.insertElementAt(range2, n2);
                        }
                        range.setEnd(max);
                    }
                }
                i = n2 + 1;
            }
        }
    }
    
    byte[] getBodyIfComplete() {
        if (this.missing.isEmpty()) {
            return this.body;
        }
        return null;
    }
    
    short getMsgType() {
        return this.msg_type;
    }
    
    void reset() {
        this.missing.removeAllElements();
        this.missing.addElement(new Range(0, this.body.length));
    }
    
    private static class Range
    {
        private int end;
        private int start;
        
        Range(final int start, final int end) {
            this.start = start;
            this.end = end;
        }
        
        public int getEnd() {
            return this.end;
        }
        
        public int getStart() {
            return this.start;
        }
        
        public void setEnd(final int end) {
            this.end = end;
        }
        
        public void setStart(final int start) {
            this.start = start;
        }
    }
}
