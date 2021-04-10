package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import java.util.*;
import java.io.*;

public class TlsSRTPUtils
{
    public static final Integer EXT_use_srtp;
    
    static {
        EXT_use_srtp = Integers.valueOf(14);
    }
    
    public static void addUseSRTPExtension(final Hashtable hashtable, final UseSRTPData useSRTPData) throws IOException {
        hashtable.put(TlsSRTPUtils.EXT_use_srtp, createUseSRTPExtension(useSRTPData));
    }
    
    public static byte[] createUseSRTPExtension(final UseSRTPData useSRTPData) throws IOException {
        if (useSRTPData != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            TlsUtils.writeUint16ArrayWithUint16Length(useSRTPData.getProtectionProfiles(), byteArrayOutputStream);
            TlsUtils.writeOpaque8(useSRTPData.getMki(), byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        throw new IllegalArgumentException("'useSRTPData' cannot be null");
    }
    
    public static UseSRTPData getUseSRTPExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsSRTPUtils.EXT_use_srtp);
        if (extensionData == null) {
            return null;
        }
        return readUseSRTPExtension(extensionData);
    }
    
    public static UseSRTPData readUseSRTPExtension(final byte[] array) throws IOException {
        if (array == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (uint16 >= 2 && (uint16 & 0x1) == 0x0) {
            final int[] uint16Array = TlsUtils.readUint16Array(uint16 / 2, byteArrayInputStream);
            final byte[] opaque8 = TlsUtils.readOpaque8(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return new UseSRTPData(uint16Array, opaque8);
        }
        throw new TlsFatalAlert((short)50);
    }
}
