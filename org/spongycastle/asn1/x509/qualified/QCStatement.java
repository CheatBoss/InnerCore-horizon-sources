package org.spongycastle.asn1.x509.qualified;

import java.util.*;
import org.spongycastle.asn1.*;

public class QCStatement extends ASN1Object implements ETSIQCObjectIdentifiers, RFC3739QCObjectIdentifiers
{
    ASN1ObjectIdentifier qcStatementId;
    ASN1Encodable qcStatementInfo;
    
    public QCStatement(final ASN1ObjectIdentifier qcStatementId) {
        this.qcStatementId = qcStatementId;
        this.qcStatementInfo = null;
    }
    
    public QCStatement(final ASN1ObjectIdentifier qcStatementId, final ASN1Encodable qcStatementInfo) {
        this.qcStatementId = qcStatementId;
        this.qcStatementInfo = qcStatementInfo;
    }
    
    private QCStatement(final ASN1Sequence asn1Sequence) {
        final Enumeration objects = asn1Sequence.getObjects();
        this.qcStatementId = ASN1ObjectIdentifier.getInstance(objects.nextElement());
        if (objects.hasMoreElements()) {
            this.qcStatementInfo = objects.nextElement();
        }
    }
    
    public static QCStatement getInstance(final Object o) {
        if (o instanceof QCStatement) {
            return (QCStatement)o;
        }
        if (o != null) {
            return new QCStatement(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
    public ASN1ObjectIdentifier getStatementId() {
        return this.qcStatementId;
    }
    
    public ASN1Encodable getStatementInfo() {
        return this.qcStatementInfo;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.qcStatementId);
        final ASN1Encodable qcStatementInfo = this.qcStatementInfo;
        if (qcStatementInfo != null) {
            asn1EncodableVector.add(qcStatementInfo);
        }
        return new DERSequence(asn1EncodableVector);
    }
}
