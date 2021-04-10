package org.spongycastle.asn1.misc;

import org.spongycastle.asn1.*;

public class NetscapeRevocationURL extends DERIA5String
{
    public NetscapeRevocationURL(final DERIA5String deria5String) {
        super(deria5String.getString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("NetscapeRevocationURL: ");
        sb.append(this.getString());
        return sb.toString();
    }
}
