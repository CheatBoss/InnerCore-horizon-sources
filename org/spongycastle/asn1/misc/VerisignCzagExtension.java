package org.spongycastle.asn1.misc;

import org.spongycastle.asn1.*;

public class VerisignCzagExtension extends DERIA5String
{
    public VerisignCzagExtension(final DERIA5String deria5String) {
        super(deria5String.getString());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("VerisignCzagExtension: ");
        sb.append(this.getString());
        return sb.toString();
    }
}
