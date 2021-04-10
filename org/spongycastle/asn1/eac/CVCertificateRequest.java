package org.spongycastle.asn1.eac;

import java.io.*;
import java.util.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class CVCertificateRequest extends ASN1Object
{
    private static final int bodyValid = 1;
    private static final int signValid = 2;
    private CertificateBody certificateBody;
    private byte[] innerSignature;
    private final ASN1ApplicationSpecific original;
    private byte[] outerSignature;
    
    private CVCertificateRequest(final ASN1ApplicationSpecific original) throws IOException {
        this.innerSignature = null;
        this.outerSignature = null;
        this.original = original;
        if (original.isConstructed() && original.getApplicationTag() == 7) {
            final ASN1Sequence instance = ASN1Sequence.getInstance(original.getObject(16));
            this.initCertBody(ASN1ApplicationSpecific.getInstance(instance.getObjectAt(0)));
            this.outerSignature = ASN1ApplicationSpecific.getInstance(instance.getObjectAt(instance.size() - 1)).getContents();
            return;
        }
        this.initCertBody(original);
    }
    
    public static CVCertificateRequest getInstance(final Object o) {
        if (o instanceof CVCertificateRequest) {
            return (CVCertificateRequest)o;
        }
        if (o != null) {
            try {
                return new CVCertificateRequest(ASN1ApplicationSpecific.getInstance(o));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unable to parse data: ");
                sb.append(ex.getMessage());
                throw new ASN1ParsingException(sb.toString(), ex);
            }
        }
        return null;
    }
    
    private void initCertBody(final ASN1ApplicationSpecific asn1ApplicationSpecific) throws IOException {
        if (asn1ApplicationSpecific.getApplicationTag() != 33) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not a CARDHOLDER_CERTIFICATE in request:");
            sb.append(asn1ApplicationSpecific.getApplicationTag());
            throw new IOException(sb.toString());
        }
        int n = 0;
        final Enumeration objects = ASN1Sequence.getInstance(asn1ApplicationSpecific.getObject(16)).getObjects();
        while (objects.hasMoreElements()) {
            final ASN1ApplicationSpecific instance = ASN1ApplicationSpecific.getInstance(objects.nextElement());
            final int applicationTag = instance.getApplicationTag();
            if (applicationTag != 55) {
                if (applicationTag != 78) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid tag, not an CV Certificate Request element:");
                    sb2.append(instance.getApplicationTag());
                    throw new IOException(sb2.toString());
                }
                this.certificateBody = CertificateBody.getInstance(instance);
                n |= 0x1;
            }
            else {
                this.innerSignature = instance.getContents();
                n |= 0x2;
            }
        }
        if ((n & 0x3) != 0x0) {
            return;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Invalid CARDHOLDER_CERTIFICATE in request:");
        sb3.append(asn1ApplicationSpecific.getApplicationTag());
        throw new IOException(sb3.toString());
    }
    
    public CertificateBody getCertificateBody() {
        return this.certificateBody;
    }
    
    public byte[] getInnerSignature() {
        return Arrays.clone(this.innerSignature);
    }
    
    public byte[] getOuterSignature() {
        return Arrays.clone(this.outerSignature);
    }
    
    public PublicKeyDataObject getPublicKey() {
        return this.certificateBody.getPublicKey();
    }
    
    public boolean hasOuterSignature() {
        return this.outerSignature != null;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1ApplicationSpecific original = this.original;
        if (original != null) {
            return original;
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certificateBody);
        try {
            asn1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.innerSignature)));
            return new DERApplicationSpecific(33, asn1EncodableVector);
        }
        catch (IOException ex) {
            throw new IllegalStateException("unable to convert signature!");
        }
    }
}
