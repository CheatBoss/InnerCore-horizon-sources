package org.spongycastle.asn1.x509;

import java.util.*;
import org.spongycastle.asn1.*;

public class IetfAttrSyntax extends ASN1Object
{
    public static final int VALUE_OCTETS = 1;
    public static final int VALUE_OID = 2;
    public static final int VALUE_UTF8 = 3;
    GeneralNames policyAuthority;
    int valueChoice;
    Vector values;
    
    private IetfAttrSyntax(final ASN1Sequence asn1Sequence) {
        this.policyAuthority = null;
        this.values = new Vector();
        this.valueChoice = -1;
        int n = 0;
        Label_0081: {
            GeneralNames policyAuthority;
            if (asn1Sequence.getObjectAt(0) instanceof ASN1TaggedObject) {
                policyAuthority = GeneralNames.getInstance((ASN1TaggedObject)asn1Sequence.getObjectAt(0), false);
            }
            else {
                if (asn1Sequence.size() != 2) {
                    break Label_0081;
                }
                policyAuthority = GeneralNames.getInstance(asn1Sequence.getObjectAt(0));
            }
            this.policyAuthority = policyAuthority;
            n = 1;
        }
        if (asn1Sequence.getObjectAt(n) instanceof ASN1Sequence) {
            final Enumeration objects = ((ASN1Sequence)asn1Sequence.getObjectAt(n)).getObjects();
            while (objects.hasMoreElements()) {
                final ASN1Primitive asn1Primitive = objects.nextElement();
                int valueChoice;
                if (asn1Primitive instanceof ASN1ObjectIdentifier) {
                    valueChoice = 2;
                }
                else if (asn1Primitive instanceof DERUTF8String) {
                    valueChoice = 3;
                }
                else {
                    if (!(asn1Primitive instanceof DEROctetString)) {
                        throw new IllegalArgumentException("Bad value type encoding IetfAttrSyntax");
                    }
                    valueChoice = 1;
                }
                if (this.valueChoice < 0) {
                    this.valueChoice = valueChoice;
                }
                if (valueChoice != this.valueChoice) {
                    throw new IllegalArgumentException("Mix of value types in IetfAttrSyntax");
                }
                this.values.addElement(asn1Primitive);
            }
            return;
        }
        throw new IllegalArgumentException("Non-IetfAttrSyntax encoding");
    }
    
    public static IetfAttrSyntax getInstance(final Object o) {
        if (o instanceof IetfAttrSyntax) {
            return (IetfAttrSyntax)o;
        }
        if (o != null) {
            return new IetfAttrSyntax(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public GeneralNames getPolicyAuthority() {
        return this.policyAuthority;
    }
    
    public int getValueType() {
        return this.valueChoice;
    }
    
    public Object[] getValues() {
        final int valueType = this.getValueType();
        final int n = 0;
        final int n2 = 0;
        int i = 0;
        if (valueType == 1) {
            final int size = this.values.size();
            final ASN1OctetString[] array = new ASN1OctetString[size];
            while (i != size) {
                array[i] = this.values.elementAt(i);
                ++i;
            }
            return array;
        }
        if (this.getValueType() == 2) {
            final int size2 = this.values.size();
            final ASN1ObjectIdentifier[] array2 = new ASN1ObjectIdentifier[size2];
            for (int j = n; j != size2; ++j) {
                array2[j] = (ASN1ObjectIdentifier)this.values.elementAt(j);
            }
            return array2;
        }
        final int size3 = this.values.size();
        final DERUTF8String[] array3 = new DERUTF8String[size3];
        for (int k = n2; k != size3; ++k) {
            array3[k] = (DERUTF8String)this.values.elementAt(k);
        }
        return array3;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.policyAuthority != null) {
            asn1EncodableVector.add(new DERTaggedObject(0, this.policyAuthority));
        }
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        final Enumeration<ASN1Encodable> elements = this.values.elements();
        while (elements.hasMoreElements()) {
            asn1EncodableVector2.add(elements.nextElement());
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        return new DERSequence(asn1EncodableVector);
    }
}
