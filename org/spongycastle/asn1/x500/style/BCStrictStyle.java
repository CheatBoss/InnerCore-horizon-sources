package org.spongycastle.asn1.x500.style;

import org.spongycastle.asn1.x500.*;

public class BCStrictStyle extends BCStyle
{
    public static final X500NameStyle INSTANCE;
    
    static {
        INSTANCE = new BCStrictStyle();
    }
    
    @Override
    public boolean areEqual(final X500Name x500Name, final X500Name x500Name2) {
        final RDN[] rdNs = x500Name.getRDNs();
        final RDN[] rdNs2 = x500Name2.getRDNs();
        if (rdNs.length != rdNs2.length) {
            return false;
        }
        for (int i = 0; i != rdNs.length; ++i) {
            if (!this.rdnAreEqual(rdNs[i], rdNs2[i])) {
                return false;
            }
        }
        return true;
    }
}
