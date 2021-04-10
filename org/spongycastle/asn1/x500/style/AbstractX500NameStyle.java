package org.spongycastle.asn1.x500.style;

import java.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.asn1.*;
import java.io.*;

public abstract class AbstractX500NameStyle implements X500NameStyle
{
    private int calcHashCode(final ASN1Encodable asn1Encodable) {
        return IETFUtils.canonicalize(IETFUtils.valueToString(asn1Encodable)).hashCode();
    }
    
    public static Hashtable copyHashTable(final Hashtable hashtable) {
        final Hashtable<Object, Object> hashtable2 = new Hashtable<Object, Object>();
        final Enumeration<Object> keys = hashtable.keys();
        while (keys.hasMoreElements()) {
            final Object nextElement = keys.nextElement();
            hashtable2.put(nextElement, hashtable.get(nextElement));
        }
        return hashtable2;
    }
    
    private boolean foundMatch(final boolean b, final RDN rdn, final RDN[] array) {
        if (b) {
            for (int i = array.length - 1; i >= 0; --i) {
                if (array[i] != null && this.rdnAreEqual(rdn, array[i])) {
                    array[i] = null;
                    return true;
                }
            }
        }
        else {
            for (int j = 0; j != array.length; ++j) {
                if (array[j] != null && this.rdnAreEqual(rdn, array[j])) {
                    array[j] = null;
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean areEqual(final X500Name x500Name, final X500Name x500Name2) {
        final RDN[] rdNs = x500Name.getRDNs();
        final RDN[] rdNs2 = x500Name2.getRDNs();
        if (rdNs.length != rdNs2.length) {
            return false;
        }
        final boolean b = rdNs[0].getFirst() != null && rdNs2[0].getFirst() != null && (rdNs[0].getFirst().getType().equals(rdNs2[0].getFirst().getType()) ^ true);
        for (int i = 0; i != rdNs.length; ++i) {
            if (!this.foundMatch(b, rdNs[i], rdNs2)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int calculateHashCode(final X500Name x500Name) {
        final RDN[] rdNs = x500Name.getRDNs();
        int i = 0;
        int n = 0;
        while (i != rdNs.length) {
            int n3;
            if (rdNs[i].isMultiValued()) {
                final AttributeTypeAndValue[] typesAndValues = rdNs[i].getTypesAndValues();
                int n2 = 0;
                while (true) {
                    n3 = n;
                    if (n2 == typesAndValues.length) {
                        break;
                    }
                    n = (n ^ typesAndValues[n2].getType().hashCode() ^ this.calcHashCode(typesAndValues[n2].getValue()));
                    ++n2;
                }
            }
            else {
                n3 = (n ^ rdNs[i].getFirst().getType().hashCode() ^ this.calcHashCode(rdNs[i].getFirst().getValue()));
            }
            ++i;
            n = n3;
        }
        return n;
    }
    
    protected ASN1Encodable encodeStringValue(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        return new DERUTF8String(s);
    }
    
    protected boolean rdnAreEqual(final RDN rdn, final RDN rdn2) {
        return IETFUtils.rDNAreEqual(rdn, rdn2);
    }
    
    @Override
    public ASN1Encodable stringToValue(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        if (s.length() != 0 && s.charAt(0) == '#') {
            try {
                return IETFUtils.valueFromHexString(s, 1);
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't recode value for oid ");
                sb.append(asn1ObjectIdentifier.getId());
                throw new ASN1ParsingException(sb.toString());
            }
        }
        String substring = s;
        if (s.length() != 0) {
            substring = s;
            if (s.charAt(0) == '\\') {
                substring = s.substring(1);
            }
        }
        return this.encodeStringValue(asn1ObjectIdentifier, substring);
    }
}
