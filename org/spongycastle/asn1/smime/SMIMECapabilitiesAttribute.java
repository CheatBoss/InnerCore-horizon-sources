package org.spongycastle.asn1.smime;

import org.spongycastle.asn1.cms.*;
import org.spongycastle.asn1.*;

public class SMIMECapabilitiesAttribute extends Attribute
{
    public SMIMECapabilitiesAttribute(final SMIMECapabilityVector smimeCapabilityVector) {
        super(SMIMEAttributes.smimeCapabilities, new DERSet(new DERSequence(smimeCapabilityVector.toASN1EncodableVector())));
    }
}
