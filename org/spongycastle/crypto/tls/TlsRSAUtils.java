package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.encodings.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.*;

public class TlsRSAUtils
{
    public static byte[] generateEncryptedPreMasterSecret(final TlsContext tlsContext, final RSAKeyParameters rsaKeyParameters, final OutputStream outputStream) throws IOException {
        final byte[] array = new byte[48];
        tlsContext.getSecureRandom().nextBytes(array);
        TlsUtils.writeVersion(tlsContext.getClientVersion(), array, 0);
        final PKCS1Encoding pkcs1Encoding = new PKCS1Encoding(new RSABlindedEngine());
        pkcs1Encoding.init(true, new ParametersWithRandom(rsaKeyParameters, tlsContext.getSecureRandom()));
        try {
            final byte[] processBlock = pkcs1Encoding.processBlock(array, 0, 48);
            if (TlsUtils.isSSL(tlsContext)) {
                outputStream.write(processBlock);
                return array;
            }
            TlsUtils.writeOpaque16(processBlock, outputStream);
            return array;
        }
        catch (InvalidCipherTextException ex) {
            throw new TlsFatalAlert((short)80, ex);
        }
    }
    
    public static byte[] safeDecryptPreMasterSecret(final TlsContext tlsContext, final RSAKeyParameters rsaKeyParameters, final byte[] array) {
        final ProtocolVersion clientVersion = tlsContext.getClientVersion();
        final byte[] array2 = new byte[48];
        tlsContext.getSecureRandom().nextBytes(array2);
        final byte[] clone = Arrays.clone(array2);
        int i = 0;
        byte[] processBlock;
        try {
            final PKCS1Encoding pkcs1Encoding = new PKCS1Encoding(new RSABlindedEngine(), array2);
            pkcs1Encoding.init(false, new ParametersWithRandom(rsaKeyParameters, tlsContext.getSecureRandom()));
            processBlock = pkcs1Encoding.processBlock(array, 0, array.length);
        }
        catch (Exception ex) {
            processBlock = clone;
        }
        final int n = (clientVersion.getMajorVersion() ^ (processBlock[0] & 0xFF)) | (clientVersion.getMinorVersion() ^ (processBlock[1] & 0xFF));
        final int n2 = n | n >> 1;
        final int n3 = n2 | n2 >> 2;
        final int n4 = ~(((n3 | n3 >> 4) & 0x1) - 1);
        while (i < 48) {
            processBlock[i] = (byte)((processBlock[i] & ~n4) | (array2[i] & n4));
            ++i;
        }
        return processBlock;
    }
}
