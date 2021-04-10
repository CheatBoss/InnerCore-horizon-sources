package org.spongycastle.asn1.x509;

import java.io.*;
import org.spongycastle.asn1.*;

public class X509DefaultEntryConverter extends X509NameEntryConverter
{
    @Override
    public ASN1Primitive getConvertedValue(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        if (s.length() != 0 && s.charAt(0) == '#') {
            try {
                return this.convertHexEncoded(s, 1);
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't recode value for oid ");
                sb.append(asn1ObjectIdentifier.getId());
                throw new RuntimeException(sb.toString());
            }
        }
        String substring = s;
        if (s.length() != 0) {
            substring = s;
            if (s.charAt(0) == '\\') {
                substring = s.substring(1);
            }
        }
        if (asn1ObjectIdentifier.equals(X509Name.EmailAddress) || asn1ObjectIdentifier.equals(X509Name.DC)) {
            return new DERIA5String(substring);
        }
        if (asn1ObjectIdentifier.equals(X509Name.DATE_OF_BIRTH)) {
            return new DERGeneralizedTime(substring);
        }
        if (!asn1ObjectIdentifier.equals(X509Name.C) && !asn1ObjectIdentifier.equals(X509Name.SN) && !asn1ObjectIdentifier.equals(X509Name.DN_QUALIFIER) && !asn1ObjectIdentifier.equals(X509Name.TELEPHONE_NUMBER)) {
            return new DERUTF8String(substring);
        }
        return new DERPrintableString(substring);
    }
}
