package org.spongycastle.crypto.agreement.kdf;

import org.spongycastle.crypto.generators.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.crypto.*;

public class ECDHKEKGenerator implements DigestDerivationFunction
{
    private ASN1ObjectIdentifier algorithm;
    private DigestDerivationFunction kdf;
    private int keySize;
    private byte[] z;
    
    public ECDHKEKGenerator(final Digest digest) {
        this.kdf = new KDF2BytesGenerator(digest);
    }
    
    @Override
    public int generateBytes(final byte[] array, final int n, final int n2) throws DataLengthException, IllegalArgumentException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        asn1EncodableVector.add(new AlgorithmIdentifier(this.algorithm, DERNull.INSTANCE));
        asn1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(Pack.intToBigEndian(this.keySize))));
        try {
            this.kdf.init(new KDFParameters(this.z, new DERSequence(asn1EncodableVector).getEncoded("DER")));
            return this.kdf.generateBytes(array, n, n2);
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unable to initialise kdf: ");
            sb.append(ex.getMessage());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    @Override
    public Digest getDigest() {
        return this.kdf.getDigest();
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        final DHKDFParameters dhkdfParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dhkdfParameters.getAlgorithm();
        this.keySize = dhkdfParameters.getKeySize();
        this.z = dhkdfParameters.getZ();
    }
}
