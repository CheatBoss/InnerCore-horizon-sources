package org.spongycastle.asn1.eac;

import java.util.*;

public class BidirectionalMap extends Hashtable
{
    private static final long serialVersionUID = -7457289971962812909L;
    Hashtable reverseMap;
    
    public BidirectionalMap() {
        this.reverseMap = new Hashtable();
    }
    
    public Object getReverse(final Object o) {
        return this.reverseMap.get(o);
    }
    
    @Override
    public Object put(final Object o, final Object o2) {
        this.reverseMap.put(o2, o);
        return super.put(o, o2);
    }
}
