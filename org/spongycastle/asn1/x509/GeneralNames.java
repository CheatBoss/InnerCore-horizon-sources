package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

public class GeneralNames extends ASN1Object
{
    private final GeneralName[] names;
    
    private GeneralNames(final ASN1Sequence asn1Sequence) {
        this.names = new GeneralName[asn1Sequence.size()];
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            this.names[i] = GeneralName.getInstance(asn1Sequence.getObjectAt(i));
        }
    }
    
    public GeneralNames(final GeneralName generalName) {
        this.names = new GeneralName[] { generalName };
    }
    
    public GeneralNames(final GeneralName[] names) {
        this.names = names;
    }
    
    public static GeneralNames fromExtensions(final Extensions extensions, final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return getInstance(extensions.getExtensionParsedValue(asn1ObjectIdentifier));
    }
    
    public static GeneralNames getInstance(final Object o) {
        if (o instanceof GeneralNames) {
            return (GeneralNames)o;
        }
        if (o != null) {
            return new GeneralNames(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public static GeneralNames getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    public GeneralName[] getNames() {
        final GeneralName[] names = this.names;
        final GeneralName[] array = new GeneralName[names.length];
        System.arraycopy(names, 0, array, 0, names.length);
        return array;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.names);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("GeneralNames:");
        sb.append(lineSeparator);
        for (int i = 0; i != this.names.length; ++i) {
            sb.append("    ");
            sb.append(this.names[i]);
            sb.append(lineSeparator);
        }
        return sb.toString();
    }
}
