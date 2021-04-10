package org.spongycastle.crypto.tls;

import java.security.*;
import java.util.*;
import java.io.*;
import org.spongycastle.util.*;

public abstract class DTLSProtocol
{
    protected final SecureRandom secureRandom;
    
    protected DTLSProtocol(final SecureRandom secureRandom) {
        if (secureRandom != null) {
            this.secureRandom = secureRandom;
            return;
        }
        throw new IllegalArgumentException("'secureRandom' cannot be null");
    }
    
    protected static void applyMaxFragmentLengthExtension(final DTLSRecordLayer dtlsRecordLayer, final short n) throws IOException {
        if (n < 0) {
            return;
        }
        if (MaxFragmentLength.isValid(n)) {
            dtlsRecordLayer.setPlaintextLimit(1 << n + 8);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected static short evaluateMaxFragmentLengthExtension(final boolean b, final Hashtable hashtable, final Hashtable hashtable2, final short n) throws IOException {
        final short maxFragmentLengthExtension = TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable2);
        if (maxFragmentLengthExtension >= 0) {
            if (MaxFragmentLength.isValid(maxFragmentLengthExtension)) {
                if (b) {
                    return maxFragmentLengthExtension;
                }
                if (maxFragmentLengthExtension == TlsExtensionsUtils.getMaxFragmentLengthExtension(hashtable)) {
                    return maxFragmentLengthExtension;
                }
            }
            throw new TlsFatalAlert(n);
        }
        return maxFragmentLengthExtension;
    }
    
    protected static byte[] generateCertificate(final Certificate certificate) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        certificate.encode(byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    protected static byte[] generateSupplementalData(final Vector vector) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TlsProtocol.writeSupplementalData(byteArrayOutputStream, vector);
        return byteArrayOutputStream.toByteArray();
    }
    
    protected static void validateSelectedCipherSuite(int encryptionAlgorithm, final short n) throws IOException {
        encryptionAlgorithm = TlsUtils.getEncryptionAlgorithm(encryptionAlgorithm);
        if (encryptionAlgorithm != 1 && encryptionAlgorithm != 2) {
            return;
        }
        throw new TlsFatalAlert(n);
    }
    
    protected void processFinished(final byte[] array, final byte[] array2) throws IOException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final byte[] fully = TlsUtils.readFully(array2.length, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (Arrays.constantTimeAreEqual(array2, fully)) {
            return;
        }
        throw new TlsFatalAlert((short)40);
    }
}
