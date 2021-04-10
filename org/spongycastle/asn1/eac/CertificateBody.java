package org.spongycastle.asn1.eac;

import java.io.*;
import org.spongycastle.asn1.*;

public class CertificateBody extends ASN1Object
{
    private static final int CAR = 2;
    private static final int CEfD = 32;
    private static final int CExD = 64;
    private static final int CHA = 16;
    private static final int CHR = 8;
    private static final int CPI = 1;
    private static final int PK = 4;
    public static final int profileType = 127;
    public static final int requestType = 13;
    private DERApplicationSpecific certificateEffectiveDate;
    private DERApplicationSpecific certificateExpirationDate;
    private CertificateHolderAuthorization certificateHolderAuthorization;
    private DERApplicationSpecific certificateHolderReference;
    private DERApplicationSpecific certificateProfileIdentifier;
    private int certificateType;
    private DERApplicationSpecific certificationAuthorityReference;
    private PublicKeyDataObject publicKey;
    ASN1InputStream seq;
    
    private CertificateBody(final ASN1ApplicationSpecific iso7816CertificateBody) throws IOException {
        this.certificateType = 0;
        this.setIso7816CertificateBody(iso7816CertificateBody);
    }
    
    public CertificateBody(final DERApplicationSpecific certificateProfileIdentifier, final CertificationAuthorityReference certificationAuthorityReference, final PublicKeyDataObject publicKey, final CertificateHolderReference certificateHolderReference, final CertificateHolderAuthorization certificateHolderAuthorization, final PackedDate packedDate, final PackedDate packedDate2) {
        this.certificateType = 0;
        this.setCertificateProfileIdentifier(certificateProfileIdentifier);
        this.setCertificationAuthorityReference(new DERApplicationSpecific(2, certificationAuthorityReference.getEncoded()));
        this.setPublicKey(publicKey);
        this.setCertificateHolderReference(new DERApplicationSpecific(32, certificateHolderReference.getEncoded()));
        this.setCertificateHolderAuthorization(certificateHolderAuthorization);
        try {
            this.setCertificateEffectiveDate(new DERApplicationSpecific(false, 37, new DEROctetString(packedDate.getEncoding())));
            this.setCertificateExpirationDate(new DERApplicationSpecific(false, 36, new DEROctetString(packedDate2.getEncoding())));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to encode dates: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    public static CertificateBody getInstance(final Object o) throws IOException {
        if (o instanceof CertificateBody) {
            return (CertificateBody)o;
        }
        if (o != null) {
            return new CertificateBody(ASN1ApplicationSpecific.getInstance(o));
        }
        return null;
    }
    
    private ASN1Primitive profileToASN1Object() throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certificateProfileIdentifier);
        asn1EncodableVector.add(this.certificationAuthorityReference);
        asn1EncodableVector.add(new DERApplicationSpecific(false, 73, this.publicKey));
        asn1EncodableVector.add(this.certificateHolderReference);
        asn1EncodableVector.add(this.certificateHolderAuthorization);
        asn1EncodableVector.add(this.certificateEffectiveDate);
        asn1EncodableVector.add(this.certificateExpirationDate);
        return new DERApplicationSpecific(78, asn1EncodableVector);
    }
    
    private ASN1Primitive requestToASN1Object() throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.certificateProfileIdentifier);
        asn1EncodableVector.add(new DERApplicationSpecific(false, 73, this.publicKey));
        asn1EncodableVector.add(this.certificateHolderReference);
        return new DERApplicationSpecific(78, asn1EncodableVector);
    }
    
    private void setCertificateEffectiveDate(final DERApplicationSpecific certificateEffectiveDate) throws IllegalArgumentException {
        if (certificateEffectiveDate.getApplicationTag() == 37) {
            this.certificateEffectiveDate = certificateEffectiveDate;
            this.certificateType |= 0x20;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Not an Iso7816Tags.APPLICATION_EFFECTIVE_DATE tag :");
        sb.append(EACTags.encodeTag(certificateEffectiveDate));
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void setCertificateExpirationDate(final DERApplicationSpecific certificateExpirationDate) throws IllegalArgumentException {
        if (certificateExpirationDate.getApplicationTag() == 36) {
            this.certificateExpirationDate = certificateExpirationDate;
            this.certificateType |= 0x40;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.APPLICATION_EXPIRATION_DATE tag");
    }
    
    private void setCertificateHolderAuthorization(final CertificateHolderAuthorization certificateHolderAuthorization) {
        this.certificateHolderAuthorization = certificateHolderAuthorization;
        this.certificateType |= 0x10;
    }
    
    private void setCertificateHolderReference(final DERApplicationSpecific certificateHolderReference) throws IllegalArgumentException {
        if (certificateHolderReference.getApplicationTag() == 32) {
            this.certificateHolderReference = certificateHolderReference;
            this.certificateType |= 0x8;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.CARDHOLDER_NAME tag");
    }
    
    private void setCertificateProfileIdentifier(final DERApplicationSpecific certificateProfileIdentifier) throws IllegalArgumentException {
        if (certificateProfileIdentifier.getApplicationTag() == 41) {
            this.certificateProfileIdentifier = certificateProfileIdentifier;
            this.certificateType |= 0x1;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Not an Iso7816Tags.INTERCHANGE_PROFILE tag :");
        sb.append(EACTags.encodeTag(certificateProfileIdentifier));
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void setCertificationAuthorityReference(final DERApplicationSpecific certificationAuthorityReference) throws IllegalArgumentException {
        if (certificationAuthorityReference.getApplicationTag() == 2) {
            this.certificationAuthorityReference = certificationAuthorityReference;
            this.certificateType |= 0x2;
            return;
        }
        throw new IllegalArgumentException("Not an Iso7816Tags.ISSUER_IDENTIFICATION_NUMBER tag");
    }
    
    private void setIso7816CertificateBody(final ASN1ApplicationSpecific asn1ApplicationSpecific) throws IOException {
        if (asn1ApplicationSpecific.getApplicationTag() != 78) {
            throw new IOException("Bad tag : not an iso7816 CERTIFICATE_CONTENT_TEMPLATE");
        }
        final ASN1InputStream asn1InputStream = new ASN1InputStream(asn1ApplicationSpecific.getContents());
        while (true) {
            final ASN1Primitive object = asn1InputStream.readObject();
            if (object == null) {
                asn1InputStream.close();
                return;
            }
            if (!(object instanceof DERApplicationSpecific)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Not a valid iso7816 content : not a DERApplicationSpecific Object :");
                sb.append(EACTags.encodeTag(asn1ApplicationSpecific));
                sb.append(((DERApplicationSpecific)object).getClass());
                throw new IOException(sb.toString());
            }
            final DERApplicationSpecific certificationAuthorityReference = (DERApplicationSpecific)object;
            final int applicationTag = certificationAuthorityReference.getApplicationTag();
            if (applicationTag != 2) {
                if (applicationTag != 32) {
                    if (applicationTag != 41) {
                        if (applicationTag != 73) {
                            if (applicationTag != 76) {
                                if (applicationTag != 36) {
                                    if (applicationTag != 37) {
                                        this.certificateType = 0;
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("Not a valid iso7816 DERApplicationSpecific tag ");
                                        sb2.append(certificationAuthorityReference.getApplicationTag());
                                        throw new IOException(sb2.toString());
                                    }
                                    this.setCertificateEffectiveDate(certificationAuthorityReference);
                                }
                                else {
                                    this.setCertificateExpirationDate(certificationAuthorityReference);
                                }
                            }
                            else {
                                this.setCertificateHolderAuthorization(new CertificateHolderAuthorization(certificationAuthorityReference));
                            }
                        }
                        else {
                            this.setPublicKey(PublicKeyDataObject.getInstance(certificationAuthorityReference.getObject(16)));
                        }
                    }
                    else {
                        this.setCertificateProfileIdentifier(certificationAuthorityReference);
                    }
                }
                else {
                    this.setCertificateHolderReference(certificationAuthorityReference);
                }
            }
            else {
                this.setCertificationAuthorityReference(certificationAuthorityReference);
            }
        }
    }
    
    private void setPublicKey(final PublicKeyDataObject publicKeyDataObject) {
        this.publicKey = PublicKeyDataObject.getInstance(publicKeyDataObject);
        this.certificateType |= 0x4;
    }
    
    public PackedDate getCertificateEffectiveDate() {
        if ((this.certificateType & 0x20) == 0x20) {
            return new PackedDate(this.certificateEffectiveDate.getContents());
        }
        return null;
    }
    
    public PackedDate getCertificateExpirationDate() throws IOException {
        if ((this.certificateType & 0x40) == 0x40) {
            return new PackedDate(this.certificateExpirationDate.getContents());
        }
        throw new IOException("certificate Expiration Date not set");
    }
    
    public CertificateHolderAuthorization getCertificateHolderAuthorization() throws IOException {
        if ((this.certificateType & 0x10) == 0x10) {
            return this.certificateHolderAuthorization;
        }
        throw new IOException("Certificate Holder Authorisation not set");
    }
    
    public CertificateHolderReference getCertificateHolderReference() {
        return new CertificateHolderReference(this.certificateHolderReference.getContents());
    }
    
    public DERApplicationSpecific getCertificateProfileIdentifier() {
        return this.certificateProfileIdentifier;
    }
    
    public int getCertificateType() {
        return this.certificateType;
    }
    
    public CertificationAuthorityReference getCertificationAuthorityReference() throws IOException {
        if ((this.certificateType & 0x2) == 0x2) {
            return new CertificationAuthorityReference(this.certificationAuthorityReference.getContents());
        }
        throw new IOException("Certification authority reference not set");
    }
    
    public PublicKeyDataObject getPublicKey() {
        return this.publicKey;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1Primitive requestToASN1Object = null;
        try {
            if (this.certificateType == 127) {
                return this.profileToASN1Object();
            }
            if (this.certificateType == 13) {
                requestToASN1Object = this.requestToASN1Object();
            }
            return requestToASN1Object;
        }
        catch (IOException ex) {
            return null;
        }
    }
}
