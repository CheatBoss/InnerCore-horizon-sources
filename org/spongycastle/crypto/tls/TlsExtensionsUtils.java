package org.spongycastle.crypto.tls;

import org.spongycastle.util.*;
import java.util.*;
import java.io.*;

public class TlsExtensionsUtils
{
    public static final Integer EXT_encrypt_then_mac;
    public static final Integer EXT_extended_master_secret;
    public static final Integer EXT_heartbeat;
    public static final Integer EXT_max_fragment_length;
    public static final Integer EXT_padding;
    public static final Integer EXT_server_name;
    public static final Integer EXT_status_request;
    public static final Integer EXT_truncated_hmac;
    
    static {
        EXT_encrypt_then_mac = Integers.valueOf(22);
        EXT_extended_master_secret = Integers.valueOf(23);
        EXT_heartbeat = Integers.valueOf(15);
        EXT_max_fragment_length = Integers.valueOf(1);
        EXT_padding = Integers.valueOf(21);
        EXT_server_name = Integers.valueOf(0);
        EXT_status_request = Integers.valueOf(5);
        EXT_truncated_hmac = Integers.valueOf(4);
    }
    
    public static void addEncryptThenMACExtension(final Hashtable hashtable) {
        hashtable.put(TlsExtensionsUtils.EXT_encrypt_then_mac, createEncryptThenMACExtension());
    }
    
    public static void addExtendedMasterSecretExtension(final Hashtable hashtable) {
        hashtable.put(TlsExtensionsUtils.EXT_extended_master_secret, createExtendedMasterSecretExtension());
    }
    
    public static void addHeartbeatExtension(final Hashtable hashtable, final HeartbeatExtension heartbeatExtension) throws IOException {
        hashtable.put(TlsExtensionsUtils.EXT_heartbeat, createHeartbeatExtension(heartbeatExtension));
    }
    
    public static void addMaxFragmentLengthExtension(final Hashtable hashtable, final short n) throws IOException {
        hashtable.put(TlsExtensionsUtils.EXT_max_fragment_length, createMaxFragmentLengthExtension(n));
    }
    
    public static void addPaddingExtension(final Hashtable hashtable, final int n) throws IOException {
        hashtable.put(TlsExtensionsUtils.EXT_padding, createPaddingExtension(n));
    }
    
    public static void addServerNameExtension(final Hashtable hashtable, final ServerNameList list) throws IOException {
        hashtable.put(TlsExtensionsUtils.EXT_server_name, createServerNameExtension(list));
    }
    
    public static void addStatusRequestExtension(final Hashtable hashtable, final CertificateStatusRequest certificateStatusRequest) throws IOException {
        hashtable.put(TlsExtensionsUtils.EXT_status_request, createStatusRequestExtension(certificateStatusRequest));
    }
    
    public static void addTruncatedHMacExtension(final Hashtable hashtable) {
        hashtable.put(TlsExtensionsUtils.EXT_truncated_hmac, createTruncatedHMacExtension());
    }
    
    public static byte[] createEmptyExtensionData() {
        return TlsUtils.EMPTY_BYTES;
    }
    
    public static byte[] createEncryptThenMACExtension() {
        return createEmptyExtensionData();
    }
    
    public static byte[] createExtendedMasterSecretExtension() {
        return createEmptyExtensionData();
    }
    
    public static byte[] createHeartbeatExtension(final HeartbeatExtension heartbeatExtension) throws IOException {
        if (heartbeatExtension != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            heartbeatExtension.encode(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static byte[] createMaxFragmentLengthExtension(final short n) throws IOException {
        TlsUtils.checkUint8(n);
        final byte[] array = { 0 };
        TlsUtils.writeUint8(n, array, 0);
        return array;
    }
    
    public static byte[] createPaddingExtension(final int n) throws IOException {
        TlsUtils.checkUint16(n);
        return new byte[n];
    }
    
    public static byte[] createServerNameExtension(final ServerNameList list) throws IOException {
        if (list != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            list.encode(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static byte[] createStatusRequestExtension(final CertificateStatusRequest certificateStatusRequest) throws IOException {
        if (certificateStatusRequest != null) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            certificateStatusRequest.encode(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static byte[] createTruncatedHMacExtension() {
        return createEmptyExtensionData();
    }
    
    public static Hashtable ensureExtensionsInitialised(final Hashtable hashtable) {
        Hashtable hashtable2 = hashtable;
        if (hashtable == null) {
            hashtable2 = new Hashtable();
        }
        return hashtable2;
    }
    
    public static HeartbeatExtension getHeartbeatExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_heartbeat);
        if (extensionData == null) {
            return null;
        }
        return readHeartbeatExtension(extensionData);
    }
    
    public static short getMaxFragmentLengthExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_max_fragment_length);
        if (extensionData == null) {
            return -1;
        }
        return readMaxFragmentLengthExtension(extensionData);
    }
    
    public static int getPaddingExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_padding);
        if (extensionData == null) {
            return -1;
        }
        return readPaddingExtension(extensionData);
    }
    
    public static ServerNameList getServerNameExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_server_name);
        if (extensionData == null) {
            return null;
        }
        return readServerNameExtension(extensionData);
    }
    
    public static CertificateStatusRequest getStatusRequestExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_status_request);
        if (extensionData == null) {
            return null;
        }
        return readStatusRequestExtension(extensionData);
    }
    
    public static boolean hasEncryptThenMACExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_encrypt_then_mac);
        return extensionData != null && readEncryptThenMACExtension(extensionData);
    }
    
    public static boolean hasExtendedMasterSecretExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_extended_master_secret);
        return extensionData != null && readExtendedMasterSecretExtension(extensionData);
    }
    
    public static boolean hasTruncatedHMacExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsExtensionsUtils.EXT_truncated_hmac);
        return extensionData != null && readTruncatedHMacExtension(extensionData);
    }
    
    private static boolean readEmptyExtensionData(final byte[] array) throws IOException {
        if (array == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        if (array.length == 0) {
            return true;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public static boolean readEncryptThenMACExtension(final byte[] array) throws IOException {
        return readEmptyExtensionData(array);
    }
    
    public static boolean readExtendedMasterSecretExtension(final byte[] array) throws IOException {
        return readEmptyExtensionData(array);
    }
    
    public static HeartbeatExtension readHeartbeatExtension(final byte[] array) throws IOException {
        if (array != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final HeartbeatExtension parse = HeartbeatExtension.parse(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return parse;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static short readMaxFragmentLengthExtension(final byte[] array) throws IOException {
        if (array == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        if (array.length == 1) {
            return TlsUtils.readUint8(array, 0);
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static int readPaddingExtension(final byte[] array) throws IOException {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i] != 0) {
                    throw new TlsFatalAlert((short)47);
                }
            }
            return array.length;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static ServerNameList readServerNameExtension(final byte[] array) throws IOException {
        if (array != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final ServerNameList parse = ServerNameList.parse(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return parse;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static CertificateStatusRequest readStatusRequestExtension(final byte[] array) throws IOException {
        if (array != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final CertificateStatusRequest parse = CertificateStatusRequest.parse(byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return parse;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static boolean readTruncatedHMacExtension(final byte[] array) throws IOException {
        return readEmptyExtensionData(array);
    }
}
