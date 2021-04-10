package org.spongycastle.crypto.util;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import java.io.*;

class DerUtil
{
    static ASN1OctetString getOctetString(final byte[] array) {
        if (array == null) {
            return new DEROctetString(new byte[0]);
        }
        return new DEROctetString(Arrays.clone(array));
    }
    
    static byte[] toByteArray(final ASN1Primitive asn1Primitive) {
        try {
            return asn1Primitive.getEncoded();
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Cannot get encoding: ");
            sb.append(ex.getMessage());
            throw new IllegalStateException(sb.toString()) {
                @Override
                public Throwable getCause() {
                    return ex;
                }
            };
        }
    }
}
