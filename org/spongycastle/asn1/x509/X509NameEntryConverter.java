package org.spongycastle.asn1.x509;

import org.spongycastle.util.*;
import java.io.*;
import org.spongycastle.asn1.*;

public abstract class X509NameEntryConverter
{
    protected boolean canBePrintable(final String s) {
        return DERPrintableString.isPrintableString(s);
    }
    
    protected ASN1Primitive convertHexEncoded(String lowerCase, final int n) throws IOException {
        lowerCase = Strings.toLowerCase(lowerCase);
        final int n2 = (lowerCase.length() - n) / 2;
        final byte[] array = new byte[n2];
        for (int i = 0; i != n2; ++i) {
            final int n3 = i * 2 + n;
            final char char1 = lowerCase.charAt(n3);
            final char char2 = lowerCase.charAt(n3 + 1);
            if (char1 < 'a') {
                array[i] = (byte)(char1 - '0' << 4);
            }
            else {
                array[i] = (byte)(char1 - 'a' + 10 << 4);
            }
            if (char2 < 'a') {
                array[i] |= (byte)(char2 - '0');
            }
            else {
                array[i] |= (byte)(char2 - 'a' + 10);
            }
        }
        return new ASN1InputStream(array).readObject();
    }
    
    public abstract ASN1Primitive getConvertedValue(final ASN1ObjectIdentifier p0, final String p1);
}
