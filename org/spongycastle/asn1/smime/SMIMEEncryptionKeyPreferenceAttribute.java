package org.spongycastle.asn1.smime;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.cms.*;

public class SMIMEEncryptionKeyPreferenceAttribute extends Attribute
{
    public SMIMEEncryptionKeyPreferenceAttribute(final ASN1OctetString asn1OctetString) {
        super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 2, asn1OctetString)));
    }
    
    public SMIMEEncryptionKeyPreferenceAttribute(final IssuerAndSerialNumber issuerAndSerialNumber) {
        super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 0, issuerAndSerialNumber)));
    }
    
    public SMIMEEncryptionKeyPreferenceAttribute(final RecipientKeyIdentifier recipientKeyIdentifier) {
        super(SMIMEAttributes.encrypKeyPref, new DERSet(new DERTaggedObject(false, 1, recipientKeyIdentifier)));
    }
}
