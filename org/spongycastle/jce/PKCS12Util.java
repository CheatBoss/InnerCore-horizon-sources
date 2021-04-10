package org.spongycastle.jce;

import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import java.io.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;

public class PKCS12Util
{
    private static byte[] calculatePbeMac(final ASN1ObjectIdentifier asn1ObjectIdentifier, final byte[] array, final int n, final char[] array2, final byte[] array3, final String s) throws Exception {
        final SecretKeyFactory instance = SecretKeyFactory.getInstance(asn1ObjectIdentifier.getId(), s);
        final PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(array, n);
        final SecretKey generateSecret = instance.generateSecret(new PBEKeySpec(array2));
        final Mac instance2 = Mac.getInstance(asn1ObjectIdentifier.getId(), s);
        instance2.init(generateSecret, pbeParameterSpec);
        instance2.update(array3);
        return instance2.doFinal();
    }
    
    public static byte[] convertToDefiniteLength(final byte[] array) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
        final Pfx instance = Pfx.getInstance(array);
        byteArrayOutputStream.reset();
        derOutputStream.writeObject(instance);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static byte[] convertToDefiniteLength(final byte[] array, final char[] array2, final String s) throws IOException {
        final Pfx instance = Pfx.getInstance(array);
        final ContentInfo authSafe = instance.getAuthSafe();
        final ASN1OctetString instance2 = ASN1OctetString.getInstance(authSafe.getContent());
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DEROutputStream derOutputStream = new DEROutputStream(byteArrayOutputStream);
        derOutputStream.writeObject(new ASN1InputStream(instance2.getOctets()).readObject());
        final ContentInfo contentInfo = new ContentInfo(authSafe.getContentType(), new DEROctetString(byteArrayOutputStream.toByteArray()));
        final MacData macData = instance.getMacData();
        try {
            final int intValue = macData.getIterationCount().intValue();
            final Pfx pfx = new Pfx(contentInfo, new MacData(new DigestInfo(new AlgorithmIdentifier(macData.getMac().getAlgorithmId().getAlgorithm(), DERNull.INSTANCE), calculatePbeMac(macData.getMac().getAlgorithmId().getAlgorithm(), macData.getSalt(), intValue, array2, ASN1OctetString.getInstance(contentInfo.getContent()).getOctets(), s)), macData.getSalt(), intValue));
            byteArrayOutputStream.reset();
            derOutputStream.writeObject(pfx);
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("error constructing MAC: ");
            sb.append(ex.toString());
            throw new IOException(sb.toString());
        }
    }
}
