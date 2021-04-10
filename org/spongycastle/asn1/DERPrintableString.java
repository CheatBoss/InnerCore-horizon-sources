package org.spongycastle.asn1;

import org.spongycastle.util.*;
import java.io.*;

public class DERPrintableString extends ASN1Primitive implements ASN1String
{
    private final byte[] string;
    
    public DERPrintableString(final String s) {
        this(s, false);
    }
    
    public DERPrintableString(final String s, final boolean b) {
        if (b && !isPrintableString(s)) {
            throw new IllegalArgumentException("string contains illegal characters");
        }
        this.string = Strings.toByteArray(s);
    }
    
    DERPrintableString(final byte[] string) {
        this.string = string;
    }
    
    public static DERPrintableString getInstance(final Object o) {
        if (o != null && !(o instanceof DERPrintableString)) {
            if (o instanceof byte[]) {
                try {
                    return (DERPrintableString)ASN1Primitive.fromByteArray((byte[])o);
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("encoding error in getInstance: ");
                    sb.append(ex.toString());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (DERPrintableString)o;
    }
    
    public static DERPrintableString getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof DERPrintableString)) {
            return new DERPrintableString(ASN1OctetString.getInstance(object).getOctets());
        }
        return getInstance(object);
    }
    
    public static boolean isPrintableString(final String s) {
        for (int i = s.length() - 1; i >= 0; --i) {
            final char char1 = s.charAt(i);
            if (char1 > '\u007f') {
                return false;
            }
            if ('a' > char1 || char1 > 'z') {
                if ('A' > char1 || char1 > 'Z') {
                    if ('0' > char1 || char1 > '9') {
                        if (char1 != ' ' && char1 != ':' && char1 != '=' && char1 != '?') {
                            switch (char1) {
                                default: {
                                    switch (char1) {
                                        default: {
                                            return false;
                                        }
                                        case 43:
                                        case 44:
                                        case 45:
                                        case 46:
                                        case 47: {
                                            continue;
                                        }
                                    }
                                    break;
                                }
                                case 39:
                                case 40:
                                case 41: {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive instanceof DERPrintableString && Arrays.areEqual(this.string, ((DERPrintableString)asn1Primitive).string);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        asn1OutputStream.writeEncoded(19, this.string);
    }
    
    @Override
    int encodedLength() {
        return StreamUtil.calculateBodyLength(this.string.length) + 1 + this.string.length;
    }
    
    public byte[] getOctets() {
        return Arrays.clone(this.string);
    }
    
    @Override
    public String getString() {
        return Strings.fromByteArray(this.string);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.string);
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
    
    @Override
    public String toString() {
        return this.getString();
    }
}
