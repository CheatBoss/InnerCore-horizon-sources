package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.x500.*;
import java.io.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class GeneralName extends ASN1Object implements ASN1Choice
{
    public static final int dNSName = 2;
    public static final int directoryName = 4;
    public static final int ediPartyName = 5;
    public static final int iPAddress = 7;
    public static final int otherName = 0;
    public static final int registeredID = 8;
    public static final int rfc822Name = 1;
    public static final int uniformResourceIdentifier = 6;
    public static final int x400Address = 3;
    private ASN1Encodable obj;
    private int tag;
    
    public GeneralName(final int tag, final String s) {
        this.tag = tag;
        if (tag == 1 || tag == 2 || tag == 6) {
            this.obj = new DERIA5String(s);
            return;
        }
        if (tag == 8) {
            this.obj = new ASN1ObjectIdentifier(s);
            return;
        }
        if (tag == 4) {
            this.obj = new X500Name(s);
            return;
        }
        if (tag != 7) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't process String for tag: ");
            sb.append(tag);
            throw new IllegalArgumentException(sb.toString());
        }
        final byte[] generalNameEncoding = this.toGeneralNameEncoding(s);
        if (generalNameEncoding != null) {
            this.obj = new DEROctetString(generalNameEncoding);
            return;
        }
        throw new IllegalArgumentException("IP Address is invalid");
    }
    
    public GeneralName(final int tag, final ASN1Encodable obj) {
        this.obj = obj;
        this.tag = tag;
    }
    
    public GeneralName(final X500Name obj) {
        this.obj = obj;
        this.tag = 4;
    }
    
    public GeneralName(final X509Name x509Name) {
        this.obj = X500Name.getInstance(x509Name);
        this.tag = 4;
    }
    
    private void copyInts(final int[] array, final byte[] array2, final int n) {
        for (int i = 0; i != array.length; ++i) {
            final int n2 = i * 2;
            array2[n2 + n] = (byte)(array[i] >> 8);
            array2[n2 + 1 + n] = (byte)array[i];
        }
    }
    
    public static GeneralName getInstance(final Object o) {
        if (o != null && !(o instanceof GeneralName)) {
            if (o instanceof ASN1TaggedObject) {
                final ASN1TaggedObject asn1TaggedObject = (ASN1TaggedObject)o;
                final int tagNo = asn1TaggedObject.getTagNo();
                switch (tagNo) {
                    case 8: {
                        return new GeneralName(tagNo, ASN1ObjectIdentifier.getInstance(asn1TaggedObject, false));
                    }
                    case 7: {
                        return new GeneralName(tagNo, ASN1OctetString.getInstance(asn1TaggedObject, false));
                    }
                    case 6: {
                        return new GeneralName(tagNo, DERIA5String.getInstance(asn1TaggedObject, false));
                    }
                    case 5: {
                        return new GeneralName(tagNo, ASN1Sequence.getInstance(asn1TaggedObject, false));
                    }
                    case 4: {
                        return new GeneralName(tagNo, X500Name.getInstance(asn1TaggedObject, true));
                    }
                    case 3: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag: ");
                        sb.append(tagNo);
                        throw new IllegalArgumentException(sb.toString());
                    }
                    case 2: {
                        return new GeneralName(tagNo, DERIA5String.getInstance(asn1TaggedObject, false));
                    }
                    case 1: {
                        return new GeneralName(tagNo, DERIA5String.getInstance(asn1TaggedObject, false));
                    }
                    case 0: {
                        return new GeneralName(tagNo, ASN1Sequence.getInstance(asn1TaggedObject, false));
                    }
                }
            }
            if (o instanceof byte[]) {
                try {
                    return getInstance(ASN1Primitive.fromByteArray((byte[])o));
                }
                catch (IOException ex) {
                    throw new IllegalArgumentException("unable to parse encoded general name");
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("unknown object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (GeneralName)o;
    }
    
    public static GeneralName getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1TaggedObject.getInstance(asn1TaggedObject, true));
    }
    
    private void parseIPv4(final String s, final byte[] array, final int n) {
        final StringTokenizer stringTokenizer = new StringTokenizer(s, "./");
        int n2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            array[n2 + n] = (byte)Integer.parseInt(stringTokenizer.nextToken());
            ++n2;
        }
    }
    
    private void parseIPv4Mask(final String s, final byte[] array, final int n) {
        for (int int1 = Integer.parseInt(s), i = 0; i != int1; ++i) {
            final int n2 = i / 8 + n;
            array[n2] |= (byte)(1 << 7 - i % 8);
        }
    }
    
    private int[] parseIPv6(String nextToken) {
        final StringTokenizer stringTokenizer = new StringTokenizer(nextToken, ":", true);
        final int[] array = new int[8];
        if (nextToken.charAt(0) == ':' && nextToken.charAt(1) == ':') {
            stringTokenizer.nextToken();
        }
        int n = 0;
        int i = -1;
        while (stringTokenizer.hasMoreTokens()) {
            nextToken = stringTokenizer.nextToken();
            if (nextToken.equals(":")) {
                array[n] = 0;
                i = n;
                ++n;
            }
            else if (nextToken.indexOf(46) < 0) {
                array[n] = Integer.parseInt(nextToken, 16);
                if (stringTokenizer.hasMoreTokens()) {
                    stringTokenizer.nextToken();
                }
                ++n;
            }
            else {
                final StringTokenizer stringTokenizer2 = new StringTokenizer(nextToken, ".");
                final int n2 = n + 1;
                array[n] = (Integer.parseInt(stringTokenizer2.nextToken()) << 8 | Integer.parseInt(stringTokenizer2.nextToken()));
                n = n2 + 1;
                array[n2] = (Integer.parseInt(stringTokenizer2.nextToken()) | Integer.parseInt(stringTokenizer2.nextToken()) << 8);
            }
        }
        if (n != 8) {
            final int n3 = n - i;
            final int n4 = 8 - n3;
            System.arraycopy(array, i, array, n4, n3);
            while (i != n4) {
                array[i] = 0;
                ++i;
            }
        }
        return array;
    }
    
    private int[] parseMask(final String s) {
        final int[] array = new int[8];
        for (int int1 = Integer.parseInt(s), i = 0; i != int1; ++i) {
            final int n = i / 16;
            array[n] |= 1 << 15 - i % 16;
        }
        return array;
    }
    
    private byte[] toGeneralNameEncoding(String s) {
        if (!IPAddress.isValidIPv6WithNetmask(s) && !IPAddress.isValidIPv6(s)) {
            if (!IPAddress.isValidIPv4WithNetmask(s) && !IPAddress.isValidIPv4(s)) {
                return null;
            }
            final int index = s.indexOf(47);
            if (index < 0) {
                final byte[] array = new byte[4];
                this.parseIPv4(s, array, 0);
                return array;
            }
            final byte[] array2 = new byte[8];
            this.parseIPv4(s.substring(0, index), array2, 0);
            s = s.substring(index + 1);
            if (s.indexOf(46) > 0) {
                this.parseIPv4(s, array2, 4);
                return array2;
            }
            this.parseIPv4Mask(s, array2, 4);
            return array2;
        }
        else {
            final int index2 = s.indexOf(47);
            if (index2 < 0) {
                final byte[] array3 = new byte[16];
                this.copyInts(this.parseIPv6(s), array3, 0);
                return array3;
            }
            final byte[] array4 = new byte[32];
            this.copyInts(this.parseIPv6(s.substring(0, index2)), array4, 0);
            s = s.substring(index2 + 1);
            int[] array5;
            if (s.indexOf(58) > 0) {
                array5 = this.parseIPv6(s);
            }
            else {
                array5 = this.parseMask(s);
            }
            this.copyInts(array5, array4, 16);
            return array4;
        }
    }
    
    public ASN1Encodable getName() {
        return this.obj;
    }
    
    public int getTagNo() {
        return this.tag;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        if (this.tag == 4) {
            return new DERTaggedObject(true, this.tag, this.obj);
        }
        return new DERTaggedObject(false, this.tag, this.obj);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append(this.tag);
        sb.append(": ");
        final int tag = this.tag;
        String s = null;
        Label_0086: {
            if (tag != 1 && tag != 2) {
                if (tag == 4) {
                    s = X500Name.getInstance(this.obj).toString();
                    break Label_0086;
                }
                if (tag != 6) {
                    s = this.obj.toString();
                    break Label_0086;
                }
            }
            s = DERIA5String.getInstance(this.obj).getString();
        }
        sb.append(s);
        return sb.toString();
    }
}
