package org.spongycastle.asn1.x509;

import java.math.*;
import org.spongycastle.asn1.*;

public class PolicyConstraints extends ASN1Object
{
    private BigInteger inhibitPolicyMapping;
    private BigInteger requireExplicitPolicyMapping;
    
    public PolicyConstraints(final BigInteger requireExplicitPolicyMapping, final BigInteger inhibitPolicyMapping) {
        this.requireExplicitPolicyMapping = requireExplicitPolicyMapping;
        this.inhibitPolicyMapping = inhibitPolicyMapping;
    }
    
    private PolicyConstraints(final ASN1Sequence asn1Sequence) {
        for (int i = 0; i != asn1Sequence.size(); ++i) {
            final ASN1TaggedObject instance = ASN1TaggedObject.getInstance(asn1Sequence.getObjectAt(i));
            if (instance.getTagNo() == 0) {
                this.requireExplicitPolicyMapping = ASN1Integer.getInstance(instance, false).getValue();
            }
            else {
                if (instance.getTagNo() != 1) {
                    throw new IllegalArgumentException("Unknown tag encountered.");
                }
                this.inhibitPolicyMapping = ASN1Integer.getInstance(instance, false).getValue();
            }
        }
    }
    
    public static PolicyConstraints fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.policyConstraints));
    }
    
    public static PolicyConstraints getInstance(final Object o) {
        if (o instanceof PolicyConstraints) {
            return (PolicyConstraints)o;
        }
        if (o != null) {
            return new PolicyConstraints(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public BigInteger getInhibitPolicyMapping() {
        return this.inhibitPolicyMapping;
    }
    
    public BigInteger getRequireExplicitPolicyMapping() {
        return this.requireExplicitPolicyMapping;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        if (this.requireExplicitPolicyMapping != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, new ASN1Integer(this.requireExplicitPolicyMapping)));
        }
        if (this.inhibitPolicyMapping != null) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, new ASN1Integer(this.inhibitPolicyMapping)));
        }
        return new DERSequence(asn1EncodableVector);
    }
}
