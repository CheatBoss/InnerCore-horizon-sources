package org.spongycastle.crypto.agreement.kdf;

import org.spongycastle.util.*;
import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.crypto.*;

public class DHKEKGenerator implements DerivationFunction
{
    private ASN1ObjectIdentifier algorithm;
    private final Digest digest;
    private int keySize;
    private byte[] partyAInfo;
    private byte[] z;
    
    public DHKEKGenerator(final Digest digest) {
        this.digest = digest;
    }
    
    @Override
    public int generateBytes(final byte[] array, int n, int i) throws DataLengthException, IllegalArgumentException {
        if (array.length - i < n) {
            throw new OutputLengthException("output buffer too small");
        }
        final long n2 = i;
        final int digestSize = this.digest.getDigestSize();
        if (n2 <= 8589934591L) {
            final long n3 = digestSize;
            final int n4 = (int)((n2 + n3 - 1L) / n3);
            final byte[] array2 = new byte[this.digest.getDigestSize()];
            final int n5 = 0;
            final int n6 = 1;
            int n7 = n;
            int n8 = i;
            n = n4;
            int n9 = n6;
            i = n5;
            while (i < n) {
                final Digest digest = this.digest;
                final byte[] z = this.z;
                digest.update(z, 0, z.length);
                final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
                final ASN1EncodableVector asn1EncodableVector2 = new ASN1EncodableVector();
                asn1EncodableVector2.add(this.algorithm);
                asn1EncodableVector2.add(new DEROctetString(Pack.intToBigEndian(n9)));
                asn1EncodableVector.add(new DERSequence(asn1EncodableVector2));
                if (this.partyAInfo != null) {
                    asn1EncodableVector.add(new DERTaggedObject(true, 0, new DEROctetString(this.partyAInfo)));
                }
                asn1EncodableVector.add(new DERTaggedObject(true, 2, new DEROctetString(Pack.intToBigEndian(this.keySize))));
                try {
                    final byte[] encoded = new DERSequence(asn1EncodableVector).getEncoded("DER");
                    this.digest.update(encoded, 0, encoded.length);
                    this.digest.doFinal(array2, 0);
                    if (n8 > digestSize) {
                        System.arraycopy(array2, 0, array, n7, digestSize);
                        n7 += digestSize;
                        n8 -= digestSize;
                    }
                    else {
                        System.arraycopy(array2, 0, array, n7, n8);
                    }
                    ++n9;
                    ++i;
                    continue;
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("unable to encode parameter info: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
                break;
            }
            this.digest.reset();
            return (int)n2;
        }
        throw new IllegalArgumentException("Output length too large");
    }
    
    public Digest getDigest() {
        return this.digest;
    }
    
    @Override
    public void init(final DerivationParameters derivationParameters) {
        final DHKDFParameters dhkdfParameters = (DHKDFParameters)derivationParameters;
        this.algorithm = dhkdfParameters.getAlgorithm();
        this.keySize = dhkdfParameters.getKeySize();
        this.z = dhkdfParameters.getZ();
        this.partyAInfo = dhkdfParameters.getExtraInfo();
    }
}
