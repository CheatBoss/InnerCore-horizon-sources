package org.spongycastle.asn1.smime;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;

public interface SMIMEAttributes
{
    public static final ASN1ObjectIdentifier encrypKeyPref = PKCSObjectIdentifiers.id_aa_encrypKeyPref;
    public static final ASN1ObjectIdentifier smimeCapabilities = PKCSObjectIdentifiers.pkcs_9_at_smimeCapabilities;
}
