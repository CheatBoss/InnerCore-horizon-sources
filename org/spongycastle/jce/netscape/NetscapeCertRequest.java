package org.spongycastle.jce.netscape;

import org.spongycastle.asn1.x509.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import java.io.*;
import java.security.*;

public class NetscapeCertRequest extends ASN1Object
{
    String challenge;
    DERBitString content;
    AlgorithmIdentifier keyAlg;
    PublicKey pubkey;
    AlgorithmIdentifier sigAlg;
    byte[] sigBits;
    
    public NetscapeCertRequest(final String challenge, final AlgorithmIdentifier sigAlg, final PublicKey pubkey) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        this.challenge = challenge;
        this.sigAlg = sigAlg;
        this.pubkey = pubkey;
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.getKeySpec());
        asn1EncodableVector.add(new DERIA5String(challenge));
        try {
            this.content = new DERBitString(new DERSequence(asn1EncodableVector));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception encoding key: ");
            sb.append(ex.toString());
            throw new InvalidKeySpecException(sb.toString());
        }
    }
    
    public NetscapeCertRequest(ASN1Sequence asn1Sequence) {
        try {
            if (asn1Sequence.size() != 3) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid SPKAC (size):");
                sb.append(asn1Sequence.size());
                throw new IllegalArgumentException(sb.toString());
            }
            this.sigAlg = AlgorithmIdentifier.getInstance(asn1Sequence.getObjectAt(1));
            this.sigBits = ((DERBitString)asn1Sequence.getObjectAt(2)).getOctets();
            asn1Sequence = (ASN1Sequence)asn1Sequence.getObjectAt(0);
            if (asn1Sequence.size() == 2) {
                this.challenge = ((DERIA5String)asn1Sequence.getObjectAt(1)).getString();
                this.content = new DERBitString(asn1Sequence);
                final SubjectPublicKeyInfo instance = SubjectPublicKeyInfo.getInstance(asn1Sequence.getObjectAt(0));
                final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new DERBitString(instance).getBytes());
                final AlgorithmIdentifier algorithm = instance.getAlgorithm();
                this.keyAlg = algorithm;
                this.pubkey = KeyFactory.getInstance(algorithm.getAlgorithm().getId(), "SC").generatePublic(x509EncodedKeySpec);
                return;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid PKAC (len): ");
            sb2.append(asn1Sequence.size());
            throw new IllegalArgumentException(sb2.toString());
        }
        catch (Exception ex) {
            throw new IllegalArgumentException(ex.toString());
        }
    }
    
    public NetscapeCertRequest(final byte[] array) throws IOException {
        this(getReq(array));
    }
    
    private ASN1Primitive getKeySpec() throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchProviderException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(this.pubkey.getEncoded());
            byteArrayOutputStream.close();
            return new ASN1InputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
        }
        catch (IOException ex) {
            throw new InvalidKeySpecException(ex.getMessage());
        }
    }
    
    private static ASN1Sequence getReq(final byte[] array) throws IOException {
        return ASN1Sequence.getInstance(new ASN1InputStream(new ByteArrayInputStream(array)).readObject());
    }
    
    public String getChallenge() {
        return this.challenge;
    }
    
    public AlgorithmIdentifier getKeyAlgorithm() {
        return this.keyAlg;
    }
    
    public PublicKey getPublicKey() {
        return this.pubkey;
    }
    
    public AlgorithmIdentifier getSigningAlgorithm() {
        return this.sigAlg;
    }
    
    public void setChallenge(final String challenge) {
        this.challenge = challenge;
    }
    
    public void setKeyAlgorithm(final AlgorithmIdentifier keyAlg) {
        this.keyAlg = keyAlg;
    }
    
    public void setPublicKey(final PublicKey pubkey) {
        this.pubkey = pubkey;
    }
    
    public void setSigningAlgorithm(final AlgorithmIdentifier sigAlg) {
        this.sigAlg = sigAlg;
    }
    
    public void sign(final PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
        this.sign(privateKey, null);
    }
    
    public void sign(final PrivateKey privateKey, final SecureRandom secureRandom) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException, InvalidKeySpecException {
        final Signature instance = Signature.getInstance(this.sigAlg.getAlgorithm().getId(), "SC");
        if (secureRandom != null) {
            instance.initSign(privateKey, secureRandom);
        }
        else {
            instance.initSign(privateKey);
        }
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(this.getKeySpec());
        asn1EncodableVector.add(new DERIA5String(this.challenge));
        try {
            instance.update(new DERSequence(asn1EncodableVector).getEncoded("DER"));
            this.sigBits = instance.sign();
        }
        catch (IOException ex) {
            throw new SignatureException(ex.getMessage());
        }
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
        try {
            asn1EncodableVector2.add(this.getKeySpec());
        }
        catch (Exception ex) {}
        asn1EncodableVector2.add(new DERIA5String(this.challenge));
        asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
        asn1EncodableVector.add(this.sigAlg);
        asn1EncodableVector.add(new DERBitString(this.sigBits));
        return new DERSequence(asn1EncodableVector);
    }
    
    public boolean verify(final String s) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, NoSuchProviderException {
        if (!s.equals(this.challenge)) {
            return false;
        }
        final Signature instance = Signature.getInstance(this.sigAlg.getAlgorithm().getId(), "SC");
        instance.initVerify(this.pubkey);
        instance.update(this.content.getBytes());
        return instance.verify(this.sigBits);
    }
}
