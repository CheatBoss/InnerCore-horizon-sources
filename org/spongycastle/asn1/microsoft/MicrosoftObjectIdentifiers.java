package org.spongycastle.asn1.microsoft;

import org.spongycastle.asn1.*;

public interface MicrosoftObjectIdentifiers
{
    public static final ASN1ObjectIdentifier microsoft;
    public static final ASN1ObjectIdentifier microsoftAppPolicies = MicrosoftObjectIdentifiers.microsoft.branch("21.10");
    public static final ASN1ObjectIdentifier microsoftCaVersion = MicrosoftObjectIdentifiers.microsoft.branch("21.1");
    public static final ASN1ObjectIdentifier microsoftCertTemplateV1 = (microsoft = new ASN1ObjectIdentifier("1.3.6.1.4.1.311")).branch("20.2");
    public static final ASN1ObjectIdentifier microsoftCertTemplateV2 = MicrosoftObjectIdentifiers.microsoft.branch("21.7");
    public static final ASN1ObjectIdentifier microsoftCrlNextPublish = MicrosoftObjectIdentifiers.microsoft.branch("21.4");
    public static final ASN1ObjectIdentifier microsoftPrevCaCertHash = MicrosoftObjectIdentifiers.microsoft.branch("21.2");
}
