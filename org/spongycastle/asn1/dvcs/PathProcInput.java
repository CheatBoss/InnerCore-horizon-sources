package org.spongycastle.asn1.dvcs;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.util.*;

public class PathProcInput extends ASN1Object
{
    private PolicyInformation[] acceptablePolicySet;
    private boolean explicitPolicyReqd;
    private boolean inhibitAnyPolicy;
    private boolean inhibitPolicyMapping;
    
    public PathProcInput(final PolicyInformation[] acceptablePolicySet) {
        this.inhibitPolicyMapping = false;
        this.explicitPolicyReqd = false;
        this.inhibitAnyPolicy = false;
        this.acceptablePolicySet = acceptablePolicySet;
    }
    
    public PathProcInput(final PolicyInformation[] acceptablePolicySet, final boolean inhibitPolicyMapping, final boolean explicitPolicyReqd, final boolean inhibitAnyPolicy) {
        this.inhibitPolicyMapping = false;
        this.explicitPolicyReqd = false;
        this.inhibitAnyPolicy = false;
        this.acceptablePolicySet = acceptablePolicySet;
        this.inhibitPolicyMapping = inhibitPolicyMapping;
        this.explicitPolicyReqd = explicitPolicyReqd;
        this.inhibitAnyPolicy = inhibitAnyPolicy;
    }
    
    private static PolicyInformation[] fromSequence(final ASN1Sequence asn1Sequence) {
        final int size = asn1Sequence.size();
        final PolicyInformation[] array = new PolicyInformation[size];
        for (int i = 0; i != size; ++i) {
            array[i] = PolicyInformation.getInstance(asn1Sequence.getObjectAt(i));
        }
        return array;
    }
    
    public static PathProcInput getInstance(Object o) {
        if (o instanceof PathProcInput) {
            return (PathProcInput)o;
        }
        if (o != null) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(o);
            final PathProcInput pathProcInput = new PathProcInput(fromSequence(ASN1Sequence.getInstance(instance.getObjectAt(0))));
            for (int i = 1; i < instance.size(); ++i) {
                final ASN1Encodable object = instance.getObjectAt(i);
                if (object instanceof ASN1Boolean) {
                    pathProcInput.setInhibitPolicyMapping(ASN1Boolean.getInstance(object).isTrue());
                }
                else if (object instanceof ASN1TaggedObject) {
                    final ASN1TaggedObject instance2 = ASN1TaggedObject.getInstance(object);
                    final int tagNo = instance2.getTagNo();
                    if (tagNo != 0) {
                        if (tagNo != 1) {
                            o = new StringBuilder();
                            ((StringBuilder)o).append("Unknown tag encountered: ");
                            ((StringBuilder)o).append(instance2.getTagNo());
                            throw new IllegalArgumentException(((StringBuilder)o).toString());
                        }
                        pathProcInput.setInhibitAnyPolicy(ASN1Boolean.getInstance(instance2, false).isTrue());
                    }
                    else {
                        pathProcInput.setExplicitPolicyReqd(ASN1Boolean.getInstance(instance2, false).isTrue());
                    }
                }
            }
            return pathProcInput;
        }
        return null;
    }
    
    public static PathProcInput getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        return getInstance(ASN1Sequence.getInstance(asn1TaggedObject, b));
    }
    
    private void setExplicitPolicyReqd(final boolean explicitPolicyReqd) {
        this.explicitPolicyReqd = explicitPolicyReqd;
    }
    
    private void setInhibitAnyPolicy(final boolean inhibitAnyPolicy) {
        this.inhibitAnyPolicy = inhibitAnyPolicy;
    }
    
    private void setInhibitPolicyMapping(final boolean inhibitPolicyMapping) {
        this.inhibitPolicyMapping = inhibitPolicyMapping;
    }
    
    public PolicyInformation[] getAcceptablePolicySet() {
        return this.acceptablePolicySet;
    }
    
    public boolean isExplicitPolicyReqd() {
        return this.explicitPolicyReqd;
    }
    
    public boolean isInhibitAnyPolicy() {
        return this.inhibitAnyPolicy;
    }
    
    public boolean isInhibitPolicyMapping() {
        return this.inhibitPolicyMapping;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        int n = 0;
        while (true) {
            final PolicyInformation[] acceptablePolicySet = this.acceptablePolicySet;
            if (n == acceptablePolicySet.length) {
                break;
            }
            asn1EncodableVector2.add(acceptablePolicySet[n]);
            ++n;
        }
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        final boolean inhibitPolicyMapping = this.inhibitPolicyMapping;
        if (inhibitPolicyMapping) {
            asn1EncodableVector.add(ASN1Boolean.getInstance(inhibitPolicyMapping));
        }
        if (this.explicitPolicyReqd) {
            asn1EncodableVector.add(new DERTaggedObject(false, 0, ASN1Boolean.getInstance(this.explicitPolicyReqd)));
        }
        if (this.inhibitAnyPolicy) {
            asn1EncodableVector.add(new DERTaggedObject(false, 1, ASN1Boolean.getInstance(this.inhibitAnyPolicy)));
        }
        return new DERSequence(asn1EncodableVector);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PathProcInput: {\nacceptablePolicySet: ");
        sb.append(Arrays.asList(this.acceptablePolicySet));
        sb.append("\ninhibitPolicyMapping: ");
        sb.append(this.inhibitPolicyMapping);
        sb.append("\nexplicitPolicyReqd: ");
        sb.append(this.explicitPolicyReqd);
        sb.append("\ninhibitAnyPolicy: ");
        sb.append(this.inhibitAnyPolicy);
        sb.append("\n}\n");
        return sb.toString();
    }
}
