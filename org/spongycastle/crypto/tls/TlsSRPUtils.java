package org.spongycastle.crypto.tls;

import java.util.*;
import java.math.*;
import java.io.*;
import org.spongycastle.util.*;

public class TlsSRPUtils
{
    public static final Integer EXT_SRP;
    
    static {
        EXT_SRP = Integers.valueOf(12);
    }
    
    public static void addSRPExtension(final Hashtable hashtable, final byte[] array) throws IOException {
        hashtable.put(TlsSRPUtils.EXT_SRP, createSRPExtension(array));
    }
    
    public static byte[] createSRPExtension(final byte[] array) throws IOException {
        if (array != null) {
            return TlsUtils.encodeOpaque8(array);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static byte[] getSRPExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsSRPUtils.EXT_SRP);
        if (extensionData == null) {
            return null;
        }
        return readSRPExtension(extensionData);
    }
    
    public static boolean isSRPCipherSuite(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 49178:
            case 49179:
            case 49180:
            case 49181:
            case 49182:
            case 49183:
            case 49184:
            case 49185:
            case 49186: {
                return true;
            }
        }
    }
    
    public static byte[] readSRPExtension(final byte[] array) throws IOException {
        if (array != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return opaque8;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static BigInteger readSRPParameter(final InputStream inputStream) throws IOException {
        return new BigInteger(1, TlsUtils.readOpaque16(inputStream));
    }
    
    public static void writeSRPParameter(final BigInteger bigInteger, final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque16(BigIntegers.asUnsignedByteArray(bigInteger), outputStream);
    }
}
