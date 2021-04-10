package org.spongycastle.asn1.x500.style;

import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;
import java.io.*;
import org.spongycastle.asn1.x500.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class IETFUtils
{
    public static void appendRDN(final StringBuffer sb, final RDN rdn, final Hashtable hashtable) {
        if (rdn.isMultiValued()) {
            final AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
            int i = 0;
            int n = 1;
            while (i != typesAndValues.length) {
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append('+');
                }
                appendTypeAndValue(sb, typesAndValues[i], hashtable);
                ++i;
            }
        }
        else if (rdn.getFirst() != null) {
            appendTypeAndValue(sb, rdn.getFirst(), hashtable);
        }
    }
    
    public static void appendTypeAndValue(final StringBuffer sb, final AttributeTypeAndValue attributeTypeAndValue, final Hashtable hashtable) {
        String id = hashtable.get(attributeTypeAndValue.getType());
        if (id == null) {
            id = attributeTypeAndValue.getType().getId();
        }
        sb.append(id);
        sb.append('=');
        sb.append(valueToString(attributeTypeAndValue.getValue()));
    }
    
    private static boolean atvAreEqual(final AttributeTypeAndValue attributeTypeAndValue, final AttributeTypeAndValue attributeTypeAndValue2) {
        return attributeTypeAndValue == attributeTypeAndValue2 || (attributeTypeAndValue != null && attributeTypeAndValue2 != null && attributeTypeAndValue.getType().equals(attributeTypeAndValue2.getType()) && canonicalize(valueToString(attributeTypeAndValue.getValue())).equals(canonicalize(valueToString(attributeTypeAndValue2.getValue()))));
    }
    
    private static String bytesToString(final byte[] array) {
        final int length = array.length;
        final char[] array2 = new char[length];
        for (int i = 0; i != length; ++i) {
            array2[i] = (char)(array[i] & 0xFF);
        }
        return new String(array2);
    }
    
    public static String canonicalize(String lowerCase) {
        final String lowerCase2 = Strings.toLowerCase(lowerCase);
        final int length = lowerCase2.length();
        int n = 0;
        lowerCase = lowerCase2;
        if (length > 0) {
            lowerCase = lowerCase2;
            if (lowerCase2.charAt(0) == '#') {
                final ASN1Primitive decodeObject = decodeObject(lowerCase2);
                lowerCase = lowerCase2;
                if (decodeObject instanceof ASN1String) {
                    lowerCase = Strings.toLowerCase(((ASN1String)decodeObject).getString());
                }
            }
        }
        String substring = lowerCase;
        if (lowerCase.length() > 1) {
            while (true) {
                final int n2 = n + 1;
                if (n2 >= lowerCase.length() || lowerCase.charAt(n) != '\\' || lowerCase.charAt(n2) != ' ') {
                    break;
                }
                n += 2;
            }
            int n3 = lowerCase.length() - 1;
            while (true) {
                final int n4 = n3 - 1;
                if (n4 <= 0 || lowerCase.charAt(n4) != '\\' || lowerCase.charAt(n3) != ' ') {
                    break;
                }
                n3 -= 2;
            }
            if (n <= 0) {
                substring = lowerCase;
                if (n3 >= lowerCase.length() - 1) {
                    return stripInternalSpaces(substring);
                }
            }
            substring = lowerCase.substring(n, n3 + 1);
        }
        return stripInternalSpaces(substring);
    }
    
    private static int convertHex(final char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        }
        int n;
        if ('a' <= c && c <= 'f') {
            n = c - 'a';
        }
        else {
            n = c - 'A';
        }
        return n + 10;
    }
    
    public static ASN1ObjectIdentifier decodeAttrName(final String s, final Hashtable hashtable) {
        if (Strings.toUpperCase(s).startsWith("OID.")) {
            return new ASN1ObjectIdentifier(s.substring(4));
        }
        if (s.charAt(0) >= '0' && s.charAt(0) <= '9') {
            return new ASN1ObjectIdentifier(s);
        }
        final ASN1ObjectIdentifier asn1ObjectIdentifier = hashtable.get(Strings.toLowerCase(s));
        if (asn1ObjectIdentifier != null) {
            return asn1ObjectIdentifier;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Unknown object id - ");
        sb.append(s);
        sb.append(" - passed to distinguished name");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private static ASN1Primitive decodeObject(final String s) {
        try {
            return ASN1Primitive.fromByteArray(Hex.decode(s.substring(1)));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown encoding in name: ");
            sb.append(ex);
            throw new IllegalStateException(sb.toString());
        }
    }
    
    public static String[] findAttrNamesForOID(final ASN1ObjectIdentifier asn1ObjectIdentifier, final Hashtable hashtable) {
        final Enumeration<Object> elements = hashtable.elements();
        final int n = 0;
        int n2 = 0;
        while (elements.hasMoreElements()) {
            if (asn1ObjectIdentifier.equals(elements.nextElement())) {
                ++n2;
            }
        }
        final String[] array = new String[n2];
        final Enumeration<K> keys = hashtable.keys();
        int n3 = n;
        while (keys.hasMoreElements()) {
            final String s = (String)keys.nextElement();
            if (asn1ObjectIdentifier.equals(hashtable.get(s))) {
                array[n3] = s;
                ++n3;
            }
        }
        return array;
    }
    
    private static boolean isHexDigit(final char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }
    
    public static boolean rDNAreEqual(final RDN rdn, final RDN rdn2) {
        if (!rdn.isMultiValued()) {
            return !rdn2.isMultiValued() && atvAreEqual(rdn.getFirst(), rdn2.getFirst());
        }
        if (!rdn2.isMultiValued()) {
            return false;
        }
        final AttributeTypeAndValue[] typesAndValues = rdn.getTypesAndValues();
        final AttributeTypeAndValue[] typesAndValues2 = rdn2.getTypesAndValues();
        if (typesAndValues.length != typesAndValues2.length) {
            return false;
        }
        for (int i = 0; i != typesAndValues.length; ++i) {
            if (!atvAreEqual(typesAndValues[i], typesAndValues2[i])) {
                return false;
            }
        }
        return true;
    }
    
    public static RDN[] rDNsFromString(String s, final X500NameStyle x500NameStyle) {
        final X500NameTokenizer x500NameTokenizer = new X500NameTokenizer(s);
        final X500NameBuilder x500NameBuilder = new X500NameBuilder(x500NameStyle);
        while (x500NameTokenizer.hasMoreTokens()) {
            s = x500NameTokenizer.nextToken();
            if (s.indexOf(43) > 0) {
                final X500NameTokenizer x500NameTokenizer2 = new X500NameTokenizer(s, '+');
                final X500NameTokenizer x500NameTokenizer3 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                final String nextToken = x500NameTokenizer3.nextToken();
                if (!x500NameTokenizer3.hasMoreTokens()) {
                    throw new IllegalArgumentException("badly formatted directory string");
                }
                s = x500NameTokenizer3.nextToken();
                ASN1ObjectIdentifier asn1ObjectIdentifier = x500NameStyle.attrNameToOID(nextToken.trim());
                if (x500NameTokenizer2.hasMoreTokens()) {
                    final Vector<ASN1ObjectIdentifier> vector = new Vector<ASN1ObjectIdentifier>();
                    final Vector<String> vector2 = new Vector<String>();
                    while (true) {
                        vector.addElement(asn1ObjectIdentifier);
                        vector2.addElement(unescape(s));
                        if (!x500NameTokenizer2.hasMoreTokens()) {
                            x500NameBuilder.addMultiValuedRDN(toOIDArray(vector), toValueArray(vector2));
                            break;
                        }
                        final X500NameTokenizer x500NameTokenizer4 = new X500NameTokenizer(x500NameTokenizer2.nextToken(), '=');
                        final String nextToken2 = x500NameTokenizer4.nextToken();
                        if (!x500NameTokenizer4.hasMoreTokens()) {
                            throw new IllegalArgumentException("badly formatted directory string");
                        }
                        s = x500NameTokenizer4.nextToken();
                        asn1ObjectIdentifier = x500NameStyle.attrNameToOID(nextToken2.trim());
                    }
                }
                else {
                    x500NameBuilder.addRDN(asn1ObjectIdentifier, unescape(s));
                }
            }
            else {
                final X500NameTokenizer x500NameTokenizer5 = new X500NameTokenizer(s, '=');
                s = x500NameTokenizer5.nextToken();
                if (!x500NameTokenizer5.hasMoreTokens()) {
                    throw new IllegalArgumentException("badly formatted directory string");
                }
                x500NameBuilder.addRDN(x500NameStyle.attrNameToOID(s.trim()), unescape(x500NameTokenizer5.nextToken()));
            }
        }
        return x500NameBuilder.build().getRDNs();
    }
    
    public static String stripInternalSpaces(final String s) {
        final StringBuffer sb = new StringBuffer();
        if (s.length() != 0) {
            final char char1 = s.charAt(0);
            sb.append(char1);
            int i = 1;
            char c = char1;
            while (i < s.length()) {
                final char char2 = s.charAt(i);
                if (c != ' ' || char2 != ' ') {
                    sb.append(char2);
                }
                ++i;
                c = char2;
            }
        }
        return sb.toString();
    }
    
    private static ASN1ObjectIdentifier[] toOIDArray(final Vector vector) {
        final int size = vector.size();
        final ASN1ObjectIdentifier[] array = new ASN1ObjectIdentifier[size];
        for (int i = 0; i != size; ++i) {
            array[i] = vector.elementAt(i);
        }
        return array;
    }
    
    private static String[] toValueArray(final Vector vector) {
        final int size = vector.size();
        final String[] array = new String[size];
        for (int i = 0; i != size; ++i) {
            array[i] = vector.elementAt(i);
        }
        return array;
    }
    
    private static String unescape(final String s) {
        if (s.length() != 0 && (s.indexOf(92) >= 0 || s.indexOf(34) >= 0)) {
            final char[] charArray = s.toCharArray();
            final StringBuffer sb = new StringBuffer(s.length());
            int n;
            if (charArray[0] == '\\' && charArray[1] == '#') {
                n = 2;
                sb.append("\\#");
            }
            else {
                n = 0;
            }
            int n2 = 0;
            int n3 = 0;
            int length = 0;
            boolean b = false;
            char c = '\0';
            int n4;
            for (int i = n; i != charArray.length; ++i, n2 = n4) {
                final char c2 = charArray[i];
                if (c2 != ' ') {
                    b = true;
                }
                Label_0246: {
                    if (c2 == '\"') {
                        if (n2 == 0) {
                            n3 ^= 0x1;
                            break Label_0246;
                        }
                    }
                    else {
                        if (c2 == '\\' && n2 == 0 && n3 == 0) {
                            length = sb.length();
                            n4 = 1;
                            continue;
                        }
                        if (c2 == ' ' && n2 == 0 && !b) {
                            n4 = n2;
                            continue;
                        }
                        if (n2 != 0 && isHexDigit(c2)) {
                            if (c != '\0') {
                                sb.append((char)(convertHex(c) * 16 + convertHex(c2)));
                                n4 = 0;
                                c = '\0';
                                continue;
                            }
                            c = c2;
                            n4 = n2;
                            continue;
                        }
                    }
                    sb.append(c2);
                }
                n4 = 0;
            }
            if (sb.length() > 0) {
                while (sb.charAt(sb.length() - 1) == ' ' && length != sb.length() - 1) {
                    sb.setLength(sb.length() - 1);
                }
            }
            return sb.toString();
        }
        return s.trim();
    }
    
    public static ASN1Encodable valueFromHexString(final String s, final int n) throws IOException {
        final int n2 = (s.length() - n) / 2;
        final byte[] array = new byte[n2];
        for (int i = 0; i != n2; ++i) {
            final int n3 = i * 2 + n;
            array[i] = (byte)(convertHex(s.charAt(n3 + 1)) | convertHex(s.charAt(n3)) << 4);
        }
        return ASN1Primitive.fromByteArray(array);
    }
    
    public static String valueToString(final ASN1Encodable asn1Encodable) {
        final StringBuffer sb = new StringBuffer();
        final boolean b = asn1Encodable instanceof ASN1String;
        final int n = 0;
        Label_0157: {
            if (b && !(asn1Encodable instanceof DERUniversalString)) {
                String s2;
                final String s = s2 = ((ASN1String)asn1Encodable).getString();
                if (s.length() > 0) {
                    s2 = s;
                    if (s.charAt(0) == '#') {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("\\");
                        sb2.append(s);
                        s2 = sb2.toString();
                    }
                }
                sb.append(s2);
                break Label_0157;
            }
            try {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("#");
                sb3.append(bytesToString(Hex.encode(asn1Encodable.toASN1Primitive().getEncoded("DER"))));
                sb.append(sb3.toString());
                int length = sb.length();
                final int length2 = sb.length();
                int i = 2;
                if (length2 < 2 || sb.charAt(0) != '\\' || sb.charAt(1) != '#') {
                    i = 0;
                }
                while (i != length) {
                    int n2 = 0;
                    int n3 = 0;
                    Label_0320: {
                        if (sb.charAt(i) != ',' && sb.charAt(i) != '\"' && sb.charAt(i) != '\\' && sb.charAt(i) != '+' && sb.charAt(i) != '=' && sb.charAt(i) != '<' && sb.charAt(i) != '>') {
                            n2 = i;
                            n3 = length;
                            if (sb.charAt(i) != ';') {
                                break Label_0320;
                            }
                        }
                        sb.insert(i, "\\");
                        n2 = i + 1;
                        n3 = length + 1;
                    }
                    i = n2 + 1;
                    length = n3;
                }
                if (sb.length() > 0) {
                    for (int n4 = n; sb.length() > n4 && sb.charAt(n4) == ' '; n4 += 2) {
                        sb.insert(n4, "\\");
                    }
                }
                for (int n5 = sb.length() - 1; n5 >= 0 && sb.charAt(n5) == ' '; --n5) {
                    sb.insert(n5, '\\');
                }
                return sb.toString();
            }
            catch (IOException ex) {
                throw new IllegalArgumentException("Other value has no encoded form");
            }
        }
    }
}
