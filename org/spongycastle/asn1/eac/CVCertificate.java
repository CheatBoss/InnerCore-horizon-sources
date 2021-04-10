package org.spongycastle.asn1.eac;

import java.io.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;

public class CVCertificate extends ASN1Object
{
    private static int bodyValid = 1;
    private static int signValid = 2;
    private CertificateBody certificateBody;
    private byte[] signature;
    private int valid;
    
    private CVCertificate(final ASN1ApplicationSpecific privateData) throws IOException {
        this.setPrivateData(privateData);
    }
    
    public CVCertificate(final ASN1InputStream asn1InputStream) throws IOException {
        this.initFrom(asn1InputStream);
    }
    
    public CVCertificate(final CertificateBody certificateBody, final byte[] array) throws IOException {
        this.certificateBody = certificateBody;
        this.signature = Arrays.clone(array);
        final int valid = this.valid | CVCertificate.bodyValid;
        this.valid = valid;
        this.valid = (valid | CVCertificate.signValid);
    }
    
    public static CVCertificate getInstance(final Object o) {
        if (o instanceof CVCertificate) {
            return (CVCertificate)o;
        }
        if (o != null) {
            try {
                return new CVCertificate(ASN1ApplicationSpecific.getInstance(o));
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
    
    private void initFrom(final ASN1InputStream asn1InputStream) throws IOException {
        while (true) {
            final ASN1Primitive object = asn1InputStream.readObject();
            if (object == null) {
                return;
            }
            if (!(object instanceof DERApplicationSpecific)) {
                throw new IOException("Invalid Input Stream for creating an Iso7816CertificateStructure");
            }
            this.setPrivateData((ASN1ApplicationSpecific)object);
        }
    }
    
    private void setPrivateData(final ASN1ApplicationSpecific asn1ApplicationSpecific) throws IOException {
        this.valid = 0;
        if (asn1ApplicationSpecific.getApplicationTag() != 33) {
            final StringBuilder sb = new StringBuilder();
            sb.append("not a CARDHOLDER_CERTIFICATE :");
            sb.append(asn1ApplicationSpecific.getApplicationTag());
            throw new IOException(sb.toString());
        }
        final ASN1InputStream asn1InputStream = new ASN1InputStream(asn1ApplicationSpecific.getContents());
        while (true) {
            final ASN1Primitive object = asn1InputStream.readObject();
            if (object != null) {
                if (!(object instanceof DERApplicationSpecific)) {
                    throw new IOException("Invalid Object, not an Iso7816CertificateStructure");
                }
                final DERApplicationSpecific derApplicationSpecific = (DERApplicationSpecific)object;
                final int applicationTag = derApplicationSpecific.getApplicationTag();
                int n;
                int n2;
                if (applicationTag != 55) {
                    if (applicationTag != 78) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Invalid tag, not an Iso7816CertificateStructure :");
                        sb2.append(derApplicationSpecific.getApplicationTag());
                        throw new IOException(sb2.toString());
                    }
                    this.certificateBody = CertificateBody.getInstance(derApplicationSpecific);
                    n = this.valid;
                    n2 = CVCertificate.bodyValid;
                }
                else {
                    this.signature = derApplicationSpecific.getContents();
                    n = this.valid;
                    n2 = CVCertificate.signValid;
                }
                this.valid = (n | n2);
            }
            else {
                asn1InputStream.close();
                if (this.valid == (CVCertificate.signValid | CVCertificate.bodyValid)) {
                    return;
                }
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("invalid CARDHOLDER_CERTIFICATE :");
                sb3.append(asn1ApplicationSpecific.getApplicationTag());
                throw new IOException(sb3.toString());
            }
        }
    }
    
    public CertificationAuthorityReference getAuthorityReference() throws IOException {
        return this.certificateBody.getCertificationAuthorityReference();
    }
    
    public CertificateBody getBody() {
        return this.certificateBody;
    }
    
    public int getCertificateType() {
        return this.certificateBody.getCertificateType();
    }
    
    public PackedDate getEffectiveDate() throws IOException {
        return this.certificateBody.getCertificateEffectiveDate();
    }
    
    public PackedDate getExpirationDate() throws IOException {
        return this.certificateBody.getCertificateExpirationDate();
    }
    
    public ASN1ObjectIdentifier getHolderAuthorization() throws IOException {
        return this.certificateBody.getCertificateHolderAuthorization().getOid();
    }
    
    public Flags getHolderAuthorizationRights() throws IOException {
        return new Flags(this.certificateBody.getCertificateHolderAuthorization().getAccessRights() & 0x1F);
    }
    
    public int getHolderAuthorizationRole() throws IOException {
        return this.certificateBody.getCertificateHolderAuthorization().getAccessRights() & 0xC0;
    }
    
    public CertificateHolderReference getHolderReference() throws IOException {
        return this.certificateBody.getCertificateHolderReference();
    }
    
    public int getRole() throws IOException {
        return this.certificateBody.getCertificateHolderAuthorization().getAccessRights();
    }
    
    public byte[] getSignature() {
        return Arrays.clone(this.signature);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certificateBody);
        try {
            asn1EncodableVector.add(new DERApplicationSpecific(false, 55, new DEROctetString(this.signature)));
            return new DERApplicationSpecific(33, asn1EncodableVector);
        }
        catch (IOException ex) {
            throw new IllegalStateException("unable to convert signature!");
        }
    }
}
