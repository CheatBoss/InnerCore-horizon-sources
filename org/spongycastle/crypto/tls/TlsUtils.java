package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.util.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.io.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;

public class TlsUtils
{
    public static final byte[] EMPTY_BYTES;
    public static final int[] EMPTY_INTS;
    public static final long[] EMPTY_LONGS;
    public static final short[] EMPTY_SHORTS;
    public static final Integer EXT_signature_algorithms;
    static final byte[][] SSL3_CONST;
    static final byte[] SSL_CLIENT;
    static final byte[] SSL_SERVER;
    
    static {
        EMPTY_BYTES = new byte[0];
        EMPTY_SHORTS = new short[0];
        EMPTY_INTS = new int[0];
        EMPTY_LONGS = new long[0];
        EXT_signature_algorithms = Integers.valueOf(13);
        SSL_CLIENT = new byte[] { 67, 76, 78, 84 };
        SSL_SERVER = new byte[] { 83, 82, 86, 82 };
        SSL3_CONST = genSSL3Const();
    }
    
    public static byte[] PRF(final TlsContext tlsContext, final byte[] array, final String s, byte[] array2, final int n) {
        if (tlsContext.getServerVersion().isSSL()) {
            throw new IllegalStateException("No PRF available for SSLv3 session");
        }
        final byte[] byteArray = Strings.toByteArray(s);
        final byte[] concat = concat(byteArray, array2);
        final int prfAlgorithm = tlsContext.getSecurityParameters().getPrfAlgorithm();
        if (prfAlgorithm == 0) {
            return PRF_legacy(array, byteArray, concat, n);
        }
        final Digest prfHash = createPRFHash(prfAlgorithm);
        array2 = new byte[n];
        hmac_hash(prfHash, array, concat, array2);
        return array2;
    }
    
    public static byte[] PRF_legacy(final byte[] array, final String s, final byte[] array2, final int n) {
        final byte[] byteArray = Strings.toByteArray(s);
        return PRF_legacy(array, byteArray, concat(byteArray, array2), n);
    }
    
    static byte[] PRF_legacy(byte[] array, byte[] array2, final byte[] array3, final int n) {
        final int n2 = (array.length + 1) / 2;
        array2 = new byte[n2];
        final byte[] array4 = new byte[n2];
        int i = 0;
        System.arraycopy(array, 0, array2, 0, n2);
        System.arraycopy(array, array.length - n2, array4, 0, n2);
        array = new byte[n];
        final byte[] array5 = new byte[n];
        hmac_hash(createHash((short)1), array2, array3, array);
        hmac_hash(createHash((short)2), array4, array3, array5);
        while (i < n) {
            array[i] ^= array5[i];
            ++i;
        }
        return array;
    }
    
    public static void addSignatureAlgorithmsExtension(final Hashtable hashtable, final Vector vector) throws IOException {
        hashtable.put(TlsUtils.EXT_signature_algorithms, createSignatureAlgorithmsExtension(vector));
    }
    
    static byte[] calculateKeyBlock(final TlsContext tlsContext, final int n) {
        final SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        final byte[] masterSecret = securityParameters.getMasterSecret();
        final byte[] concat = concat(securityParameters.getServerRandom(), securityParameters.getClientRandom());
        if (isSSL(tlsContext)) {
            return calculateKeyBlock_SSL(masterSecret, concat, n);
        }
        return PRF(tlsContext, masterSecret, "key expansion", concat, n);
    }
    
    static byte[] calculateKeyBlock_SSL(final byte[] array, final byte[] array2, final int n) {
        final Digest hash = createHash((short)1);
        final Digest hash2 = createHash((short)2);
        final int digestSize = hash.getDigestSize();
        final int digestSize2 = hash2.getDigestSize();
        final byte[] array3 = new byte[digestSize2];
        final byte[] array4 = new byte[n + digestSize];
        for (int i = 0, n2 = 0; i < n; i += digestSize, ++n2) {
            final byte[] array5 = TlsUtils.SSL3_CONST[n2];
            hash2.update(array5, 0, array5.length);
            hash2.update(array, 0, array.length);
            hash2.update(array2, 0, array2.length);
            hash2.doFinal(array3, 0);
            hash.update(array, 0, array.length);
            hash.update(array3, 0, digestSize2);
            hash.doFinal(array4, i);
        }
        return Arrays.copyOfRange(array4, 0, n);
    }
    
    static byte[] calculateMasterSecret(final TlsContext tlsContext, final byte[] array) {
        final SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        byte[] array2;
        if (securityParameters.extendedMasterSecret) {
            array2 = securityParameters.getSessionHash();
        }
        else {
            array2 = concat(securityParameters.getClientRandom(), securityParameters.getServerRandom());
        }
        if (isSSL(tlsContext)) {
            return calculateMasterSecret_SSL(array, array2);
        }
        String s;
        if (securityParameters.extendedMasterSecret) {
            s = "extended master secret";
        }
        else {
            s = "master secret";
        }
        return PRF(tlsContext, array, s, array2, 48);
    }
    
    static byte[] calculateMasterSecret_SSL(final byte[] array, final byte[] array2) {
        final Digest hash = createHash((short)1);
        final Digest hash2 = createHash((short)2);
        final int digestSize = hash.getDigestSize();
        final int digestSize2 = hash2.getDigestSize();
        final byte[] array3 = new byte[digestSize2];
        final byte[] array4 = new byte[digestSize * 3];
        int i = 0;
        int n = 0;
        while (i < 3) {
            final byte[] array5 = TlsUtils.SSL3_CONST[i];
            hash2.update(array5, 0, array5.length);
            hash2.update(array, 0, array.length);
            hash2.update(array2, 0, array2.length);
            hash2.doFinal(array3, 0);
            hash.update(array, 0, array.length);
            hash.update(array3, 0, digestSize2);
            hash.doFinal(array4, n);
            n += digestSize;
            ++i;
        }
        return array4;
    }
    
    static byte[] calculateVerifyData(final TlsContext tlsContext, final String s, final byte[] array) {
        if (isSSL(tlsContext)) {
            return array;
        }
        final SecurityParameters securityParameters = tlsContext.getSecurityParameters();
        return PRF(tlsContext, securityParameters.getMasterSecret(), s, array, securityParameters.getVerifyDataLength());
    }
    
    public static void checkUint16(final int n) throws IOException {
        if (isValidUint16(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint16(final long n) throws IOException {
        if (isValidUint16(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint24(final int n) throws IOException {
        if (isValidUint24(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint24(final long n) throws IOException {
        if (isValidUint24(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint32(final long n) throws IOException {
        if (isValidUint32(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint48(final long n) throws IOException {
        if (isValidUint48(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint64(final long n) throws IOException {
        if (isValidUint64(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint8(final int n) throws IOException {
        if (isValidUint8(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint8(final long n) throws IOException {
        if (isValidUint8(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static void checkUint8(final short n) throws IOException {
        if (isValidUint8(n)) {
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static Digest cloneHash(final short n, final Digest digest) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 6: {
                return new SHA512Digest((SHA512Digest)digest);
            }
            case 5: {
                return new SHA384Digest((SHA384Digest)digest);
            }
            case 4: {
                return new SHA256Digest((SHA256Digest)digest);
            }
            case 3: {
                return new SHA224Digest((SHA224Digest)digest);
            }
            case 2: {
                return new SHA1Digest((SHA1Digest)digest);
            }
            case 1: {
                return new MD5Digest((MD5Digest)digest);
            }
        }
    }
    
    public static Digest clonePRFHash(final int n, final Digest digest) {
        if (n != 0) {
            return cloneHash(getHashAlgorithmForPRFAlgorithm(n), digest);
        }
        return new CombinedHash((CombinedHash)digest);
    }
    
    static byte[] concat(final byte[] array, final byte[] array2) {
        final byte[] array3 = new byte[array.length + array2.length];
        System.arraycopy(array, 0, array3, 0, array.length);
        System.arraycopy(array2, 0, array3, array.length, array2.length);
        return array3;
    }
    
    public static Digest createHash(final SignatureAndHashAlgorithm signatureAndHashAlgorithm) {
        if (signatureAndHashAlgorithm == null) {
            return new CombinedHash();
        }
        return createHash(signatureAndHashAlgorithm.getHash());
    }
    
    public static Digest createHash(final short n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 6: {
                return new SHA512Digest();
            }
            case 5: {
                return new SHA384Digest();
            }
            case 4: {
                return new SHA256Digest();
            }
            case 3: {
                return new SHA224Digest();
            }
            case 2: {
                return new SHA1Digest();
            }
            case 1: {
                return new MD5Digest();
            }
        }
    }
    
    public static Digest createPRFHash(final int n) {
        if (n != 0) {
            return createHash(getHashAlgorithmForPRFAlgorithm(n));
        }
        return new CombinedHash();
    }
    
    public static byte[] createSignatureAlgorithmsExtension(final Vector vector) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        encodeSupportedSignatureAlgorithms(vector, false, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
    
    public static TlsSigner createTlsSigner(final short n) {
        if (n == 1) {
            return new TlsRSASigner();
        }
        if (n == 2) {
            return new TlsDSSSigner();
        }
        if (n == 64) {
            return new TlsECDSASigner();
        }
        throw new IllegalArgumentException("'clientCertificateType' is not a type with signing capability");
    }
    
    public static byte[] encodeOpaque8(final byte[] array) throws IOException {
        checkUint8(array.length);
        return Arrays.prepend(array, (byte)array.length);
    }
    
    public static void encodeSupportedSignatureAlgorithms(final Vector vector, final boolean b, final OutputStream outputStream) throws IOException {
        if (vector != null && vector.size() >= 1 && vector.size() < 32768) {
            final int n = vector.size() * 2;
            checkUint16(n);
            writeUint16(n, outputStream);
            for (int i = 0; i < vector.size(); ++i) {
                final SignatureAndHashAlgorithm signatureAndHashAlgorithm = vector.elementAt(i);
                if (!b && signatureAndHashAlgorithm.getSignature() == 0) {
                    throw new IllegalArgumentException("SignatureAlgorithm.anonymous MUST NOT appear in the signature_algorithms extension");
                }
                signatureAndHashAlgorithm.encode(outputStream);
            }
            return;
        }
        throw new IllegalArgumentException("'supportedSignatureAlgorithms' must have length from 1 to (2^15 - 1)");
    }
    
    public static byte[] encodeUint16ArrayWithUint16Length(final int[] array) throws IOException {
        final byte[] array2 = new byte[array.length * 2 + 2];
        writeUint16ArrayWithUint16Length(array, array2, 0);
        return array2;
    }
    
    public static byte[] encodeUint8ArrayWithUint8Length(final short[] array) throws IOException {
        final byte[] array2 = new byte[array.length + 1];
        writeUint8ArrayWithUint8Length(array, array2, 0);
        return array2;
    }
    
    private static byte[][] genSSL3Const() {
        final byte[][] array = new byte[10][];
        int n;
        for (int i = 0; i < 10; i = n) {
            n = i + 1;
            final byte[] array2 = new byte[n];
            Arrays.fill(array2, (byte)(i + 65));
            array[i] = array2;
        }
        return array;
    }
    
    public static Vector getAllSignatureAlgorithms() {
        final Vector<Short> vector = new Vector<Short>(4);
        vector.addElement(Shorts.valueOf((short)0));
        vector.addElement(Shorts.valueOf((short)1));
        vector.addElement(Shorts.valueOf((short)2));
        vector.addElement(Shorts.valueOf((short)3));
        return vector;
    }
    
    public static int getCipherType(int encryptionAlgorithm) throws IOException {
        encryptionAlgorithm = getEncryptionAlgorithm(encryptionAlgorithm);
        if (encryptionAlgorithm != 103 && encryptionAlgorithm != 104) {
            switch (encryptionAlgorithm) {
                default: {
                    throw new TlsFatalAlert((short)80);
                }
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 12:
                case 13:
                case 14: {
                    return 1;
                }
                case 0:
                case 1:
                case 2: {
                    return 0;
                }
                case 10:
                case 11:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 20:
                case 21: {
                    break;
                }
            }
        }
        return 2;
    }
    
    static short getClientCertificateType(final Certificate certificate, final Certificate certificate2) throws IOException {
        if (certificate.isEmpty()) {
            return -1;
        }
        final org.spongycastle.asn1.x509.Certificate certificate3 = certificate.getCertificateAt(0);
        final SubjectPublicKeyInfo subjectPublicKeyInfo = certificate3.getSubjectPublicKeyInfo();
        try {
            final AsymmetricKeyParameter key = PublicKeyFactory.createKey(subjectPublicKeyInfo);
            if (key.isPrivate()) {
                throw new TlsFatalAlert((short)80);
            }
            if (key instanceof RSAKeyParameters) {
                validateKeyUsage(certificate3, 128);
                return 1;
            }
            if (key instanceof DSAPublicKeyParameters) {
                validateKeyUsage(certificate3, 128);
                return 2;
            }
            if (key instanceof ECPublicKeyParameters) {
                validateKeyUsage(certificate3, 128);
                return 64;
            }
            throw new TlsFatalAlert((short)43);
        }
        catch (Exception ex) {
            throw new TlsFatalAlert((short)43, ex);
        }
    }
    
    public static Vector getDefaultDSSSignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short)2, (short)2));
    }
    
    public static Vector getDefaultECDSASignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short)2, (short)3));
    }
    
    public static Vector getDefaultRSASignatureAlgorithms() {
        return vectorOfOne(new SignatureAndHashAlgorithm((short)2, (short)1));
    }
    
    public static Vector getDefaultSupportedSignatureAlgorithms() {
        final Vector<SignatureAndHashAlgorithm> vector = new Vector<SignatureAndHashAlgorithm>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 5; ++j) {
                vector.addElement(new SignatureAndHashAlgorithm((new short[] { 2, 3, 4, 5, 6 })[j], (new short[] { 1, 2, 3 })[i]));
            }
        }
        return vector;
    }
    
    public static int getEncryptionAlgorithm(final int n) throws IOException {
        if (n != 1 && n != 2) {
            if (n != 4 && n != 5) {
                Label_1208: {
                    if (n != 10 && n != 13 && n != 16 && n != 19 && n != 22) {
                        if (n == 24) {
                            return 2;
                        }
                        if (n != 27) {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            switch (n) {
                                                                default: {
                                                                    switch (n) {
                                                                        default: {
                                                                            switch (n) {
                                                                                default: {
                                                                                    switch (n) {
                                                                                        default: {
                                                                                            throw new TlsFatalAlert((short)80);
                                                                                        }
                                                                                        case 65297:
                                                                                        case 65299:
                                                                                        case 65301: {
                                                                                            return 104;
                                                                                        }
                                                                                        case 65296:
                                                                                        case 65298:
                                                                                        case 65300: {
                                                                                            return 103;
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                                case 65281:
                                                                                case 65283:
                                                                                case 65285: {
                                                                                    return 104;
                                                                                }
                                                                                case 65280:
                                                                                case 65282:
                                                                                case 65284: {
                                                                                    return 103;
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                        case 52392:
                                                                        case 52393:
                                                                        case 52394:
                                                                        case 52395:
                                                                        case 52396:
                                                                        case 52397:
                                                                        case 52398: {
                                                                            return 21;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 49313:
                                                                case 49315:
                                                                case 49321:
                                                                case 49323:
                                                                case 49327: {
                                                                    return 18;
                                                                }
                                                                case 49312:
                                                                case 49314:
                                                                case 49320:
                                                                case 49322:
                                                                case 49326: {
                                                                    return 16;
                                                                }
                                                                case 49309:
                                                                case 49311:
                                                                case 49317:
                                                                case 49319:
                                                                case 49325: {
                                                                    return 17;
                                                                }
                                                                case 49308:
                                                                case 49310:
                                                                case 49316:
                                                                case 49318:
                                                                case 49324: {
                                                                    return 15;
                                                                }
                                                                case 49275:
                                                                case 49277:
                                                                case 49279:
                                                                case 49281:
                                                                case 49283:
                                                                case 49285:
                                                                case 49287:
                                                                case 49289:
                                                                case 49291:
                                                                case 49293:
                                                                case 49295:
                                                                case 49297:
                                                                case 49299: {
                                                                    return 20;
                                                                }
                                                                case 49274:
                                                                case 49276:
                                                                case 49278:
                                                                case 49280:
                                                                case 49282:
                                                                case 49284:
                                                                case 49286:
                                                                case 49288:
                                                                case 49290:
                                                                case 49292:
                                                                case 49294:
                                                                case 49296:
                                                                case 49298: {
                                                                    return 19;
                                                                }
                                                                case 49266:
                                                                case 49268:
                                                                case 49270:
                                                                case 49272:
                                                                case 49300:
                                                                case 49302:
                                                                case 49304:
                                                                case 49306: {
                                                                    return 12;
                                                                }
                                                                case 49267:
                                                                case 49269:
                                                                case 49271:
                                                                case 49273:
                                                                case 49301:
                                                                case 49303:
                                                                case 49305:
                                                                case 49307: {
                                                                    return 13;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 49210:
                                                        case 49211: {
                                                            return 0;
                                                        }
                                                        case 49196:
                                                        case 49198:
                                                        case 49200:
                                                        case 49202: {
                                                            return 11;
                                                        }
                                                        case 49195:
                                                        case 49197:
                                                        case 49199:
                                                        case 49201: {
                                                            return 10;
                                                        }
                                                        case 49157:
                                                        case 49162:
                                                        case 49167:
                                                        case 49172:
                                                        case 49177:
                                                        case 49184:
                                                        case 49185:
                                                        case 49186:
                                                        case 49188:
                                                        case 49190:
                                                        case 49192:
                                                        case 49194:
                                                        case 49206:
                                                        case 49208: {
                                                            return 9;
                                                        }
                                                        case 49156:
                                                        case 49161:
                                                        case 49166:
                                                        case 49171:
                                                        case 49176:
                                                        case 49181:
                                                        case 49182:
                                                        case 49183:
                                                        case 49187:
                                                        case 49189:
                                                        case 49191:
                                                        case 49193:
                                                        case 49205:
                                                        case 49207: {
                                                            return 8;
                                                        }
                                                        case 49155:
                                                        case 49160:
                                                        case 49165:
                                                        case 49170:
                                                        case 49175:
                                                        case 49178:
                                                        case 49179:
                                                        case 49180:
                                                        case 49204: {
                                                            break Label_1208;
                                                        }
                                                        case 49154:
                                                        case 49159:
                                                        case 49164:
                                                        case 49169:
                                                        case 49174:
                                                        case 49203: {
                                                            return 2;
                                                        }
                                                        case 49153:
                                                        case 49158:
                                                        case 49163:
                                                        case 49168:
                                                        case 49173:
                                                        case 49209: {
                                                            return 0;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 104:
                                                case 105:
                                                case 106:
                                                case 107:
                                                case 109: {
                                                    return 9;
                                                }
                                                case 103:
                                                case 108: {
                                                    return 8;
                                                }
                                            }
                                            break;
                                        }
                                        case 65:
                                        case 66:
                                        case 67:
                                        case 68:
                                        case 69:
                                        case 70: {
                                            return 12;
                                        }
                                        case 59: {
                                            return 0;
                                        }
                                        case 53:
                                        case 54:
                                        case 55:
                                        case 56:
                                        case 57:
                                        case 58:
                                        case 61: {
                                            return 9;
                                        }
                                        case 47:
                                        case 48:
                                        case 49:
                                        case 50:
                                        case 51:
                                        case 52:
                                        case 60:
                                        case 62:
                                        case 63:
                                        case 64: {
                                            return 8;
                                        }
                                        case 44:
                                        case 45:
                                        case 46: {
                                            return 0;
                                        }
                                    }
                                    break;
                                }
                                case 186:
                                case 187:
                                case 188:
                                case 189:
                                case 190:
                                case 191: {
                                    return 12;
                                }
                                case 176:
                                case 177:
                                case 180:
                                case 181:
                                case 184:
                                case 185: {
                                    return 0;
                                }
                                case 157:
                                case 159:
                                case 161:
                                case 163:
                                case 165:
                                case 167:
                                case 169:
                                case 171:
                                case 173: {
                                    return 11;
                                }
                                case 156:
                                case 158:
                                case 160:
                                case 162:
                                case 164:
                                case 166:
                                case 168:
                                case 170:
                                case 172: {
                                    return 10;
                                }
                                case 150:
                                case 151:
                                case 152:
                                case 153:
                                case 154:
                                case 155: {
                                    return 14;
                                }
                                case 141:
                                case 145:
                                case 149:
                                case 175:
                                case 179:
                                case 183: {
                                    return 9;
                                }
                                case 140:
                                case 144:
                                case 148:
                                case 174:
                                case 178:
                                case 182: {
                                    return 8;
                                }
                                case 132:
                                case 133:
                                case 134:
                                case 135:
                                case 136:
                                case 137:
                                case 192:
                                case 193:
                                case 194:
                                case 195:
                                case 196:
                                case 197: {
                                    return 13;
                                }
                                case 139:
                                case 143:
                                case 147: {
                                    break;
                                }
                                case 138:
                                case 142:
                                case 146: {
                                    return 2;
                                }
                            }
                        }
                    }
                }
                return 7;
            }
            return 2;
        }
        return 0;
    }
    
    public static byte[] getExtensionData(final Hashtable hashtable, final Integer n) {
        if (hashtable == null) {
            return null;
        }
        return hashtable.get(n);
    }
    
    public static short getHashAlgorithmForPRFAlgorithm(final int n) {
        if (n == 0) {
            throw new IllegalArgumentException("legacy PRF not a valid algorithm");
        }
        if (n == 1) {
            return 4;
        }
        if (n == 2) {
            return 5;
        }
        throw new IllegalArgumentException("unknown PRFAlgorithm");
    }
    
    public static int getKeyExchangeAlgorithm(final int n) throws IOException {
        if (n != 1 && n != 2 && n != 4 && n != 5 && n != 10) {
            if (n != 13) {
                if (n != 16) {
                    if (n != 19) {
                        if (n != 22) {
                            Label_1194: {
                                if (n != 24 && n != 27) {
                                    switch (n) {
                                        default: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            switch (n) {
                                                                default: {
                                                                    switch (n) {
                                                                        default: {
                                                                            switch (n) {
                                                                                default: {
                                                                                    switch (n) {
                                                                                        default: {
                                                                                            switch (n) {
                                                                                                default: {
                                                                                                    throw new TlsFatalAlert((short)80);
                                                                                                }
                                                                                                case 65300:
                                                                                                case 65301: {
                                                                                                    return 24;
                                                                                                }
                                                                                                case 65298:
                                                                                                case 65299: {
                                                                                                    return 14;
                                                                                                }
                                                                                                case 65296:
                                                                                                case 65297: {
                                                                                                    return 13;
                                                                                                }
                                                                                            }
                                                                                            break;
                                                                                        }
                                                                                        case 65284:
                                                                                        case 65285: {
                                                                                            return 17;
                                                                                        }
                                                                                        case 65282:
                                                                                        case 65283: {
                                                                                            return 19;
                                                                                        }
                                                                                        case 65280:
                                                                                        case 65281: {
                                                                                            return 5;
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                                case 52393: {
                                                                                    return 17;
                                                                                }
                                                                                case 52392: {
                                                                                    return 19;
                                                                                }
                                                                                case 52396: {
                                                                                    return 24;
                                                                                }
                                                                                case 52398: {
                                                                                    return 15;
                                                                                }
                                                                                case 52397: {
                                                                                    return 14;
                                                                                }
                                                                                case 52395: {
                                                                                    return 13;
                                                                                }
                                                                                case 52394: {
                                                                                    return 5;
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                        case 49272:
                                                                        case 49273:
                                                                        case 49292:
                                                                        case 49293: {
                                                                            return 18;
                                                                        }
                                                                        case 49268:
                                                                        case 49269:
                                                                        case 49288:
                                                                        case 49289: {
                                                                            return 16;
                                                                        }
                                                                        case 49266:
                                                                        case 49267:
                                                                        case 49286:
                                                                        case 49287:
                                                                        case 49324:
                                                                        case 49325:
                                                                        case 49326:
                                                                        case 49327: {
                                                                            return 17;
                                                                        }
                                                                        case 49270:
                                                                        case 49271:
                                                                        case 49290:
                                                                        case 49291: {
                                                                            return 19;
                                                                        }
                                                                        case 49306:
                                                                        case 49307: {
                                                                            return 24;
                                                                        }
                                                                        case 49298:
                                                                        case 49299:
                                                                        case 49304:
                                                                        case 49305: {
                                                                            return 15;
                                                                        }
                                                                        case 49296:
                                                                        case 49297:
                                                                        case 49302:
                                                                        case 49303:
                                                                        case 49318:
                                                                        case 49319:
                                                                        case 49322:
                                                                        case 49323: {
                                                                            return 14;
                                                                        }
                                                                        case 49294:
                                                                        case 49295:
                                                                        case 49300:
                                                                        case 49301:
                                                                        case 49316:
                                                                        case 49317:
                                                                        case 49320:
                                                                        case 49321: {
                                                                            return 13;
                                                                        }
                                                                        case 49284:
                                                                        case 49285: {
                                                                            break Label_1194;
                                                                        }
                                                                        case 49276:
                                                                        case 49277:
                                                                        case 49310:
                                                                        case 49311:
                                                                        case 49314:
                                                                        case 49315: {
                                                                            return 5;
                                                                        }
                                                                        case 49280:
                                                                        case 49281: {
                                                                            return 3;
                                                                        }
                                                                        case 49278:
                                                                        case 49279: {
                                                                            return 9;
                                                                        }
                                                                        case 49282:
                                                                        case 49283: {
                                                                            return 7;
                                                                        }
                                                                        case 49274:
                                                                        case 49275:
                                                                        case 49308:
                                                                        case 49309:
                                                                        case 49312:
                                                                        case 49313: {
                                                                            return 1;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 49180:
                                                                case 49183:
                                                                case 49186: {
                                                                    return 22;
                                                                }
                                                                case 49179:
                                                                case 49182:
                                                                case 49185: {
                                                                    return 23;
                                                                }
                                                                case 49178:
                                                                case 49181:
                                                                case 49184: {
                                                                    return 21;
                                                                }
                                                                case 49173:
                                                                case 49174:
                                                                case 49175:
                                                                case 49176:
                                                                case 49177: {
                                                                    return 20;
                                                                }
                                                                case 49163:
                                                                case 49164:
                                                                case 49165:
                                                                case 49166:
                                                                case 49167:
                                                                case 49193:
                                                                case 49194:
                                                                case 49201:
                                                                case 49202: {
                                                                    return 18;
                                                                }
                                                                case 49153:
                                                                case 49154:
                                                                case 49155:
                                                                case 49156:
                                                                case 49157:
                                                                case 49189:
                                                                case 49190:
                                                                case 49197:
                                                                case 49198: {
                                                                    return 16;
                                                                }
                                                                case 49158:
                                                                case 49159:
                                                                case 49160:
                                                                case 49161:
                                                                case 49162:
                                                                case 49187:
                                                                case 49188:
                                                                case 49195:
                                                                case 49196: {
                                                                    return 17;
                                                                }
                                                                case 49168:
                                                                case 49169:
                                                                case 49170:
                                                                case 49171:
                                                                case 49172:
                                                                case 49191:
                                                                case 49192:
                                                                case 49199:
                                                                case 49200: {
                                                                    return 19;
                                                                }
                                                                case 49203:
                                                                case 49204:
                                                                case 49205:
                                                                case 49206:
                                                                case 49207:
                                                                case 49208:
                                                                case 49209:
                                                                case 49210:
                                                                case 49211: {
                                                                    return 24;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 108:
                                                        case 109: {
                                                            break Label_1194;
                                                        }
                                                        case 103:
                                                        case 107: {
                                                            return 5;
                                                        }
                                                        case 106: {
                                                            return 3;
                                                        }
                                                        case 105: {
                                                            return 9;
                                                        }
                                                        case 104: {
                                                            return 7;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 46: {
                                                    return 15;
                                                }
                                                case 45: {
                                                    return 14;
                                                }
                                                case 44: {
                                                    return 13;
                                                }
                                                case 52:
                                                case 58:
                                                case 70: {
                                                    break Label_1194;
                                                }
                                                case 51:
                                                case 57:
                                                case 69: {
                                                    return 5;
                                                }
                                                case 50:
                                                case 56:
                                                case 64:
                                                case 68: {
                                                    return 3;
                                                }
                                                case 49:
                                                case 55:
                                                case 63:
                                                case 67: {
                                                    return 9;
                                                }
                                                case 48:
                                                case 54:
                                                case 62:
                                                case 66: {
                                                    return 7;
                                                }
                                                case 47:
                                                case 53:
                                                case 59:
                                                case 60:
                                                case 61:
                                                case 65: {
                                                    return 1;
                                                }
                                            }
                                            break;
                                        }
                                        case 146:
                                        case 147:
                                        case 148:
                                        case 149:
                                        case 172:
                                        case 173:
                                        case 182:
                                        case 183:
                                        case 184:
                                        case 185: {
                                            return 15;
                                        }
                                        case 142:
                                        case 143:
                                        case 144:
                                        case 145:
                                        case 170:
                                        case 171:
                                        case 178:
                                        case 179:
                                        case 180:
                                        case 181: {
                                            return 14;
                                        }
                                        case 138:
                                        case 139:
                                        case 140:
                                        case 141:
                                        case 168:
                                        case 169:
                                        case 174:
                                        case 175:
                                        case 176:
                                        case 177: {
                                            return 13;
                                        }
                                        case 137:
                                        case 155:
                                        case 166:
                                        case 167:
                                        case 191:
                                        case 197: {
                                            break;
                                        }
                                        case 136:
                                        case 154:
                                        case 158:
                                        case 159:
                                        case 190:
                                        case 196: {
                                            return 5;
                                        }
                                        case 135:
                                        case 153:
                                        case 162:
                                        case 163:
                                        case 189:
                                        case 195: {
                                            return 3;
                                        }
                                        case 134:
                                        case 152:
                                        case 160:
                                        case 161:
                                        case 188:
                                        case 194: {
                                            return 9;
                                        }
                                        case 133:
                                        case 151:
                                        case 164:
                                        case 165:
                                        case 187:
                                        case 193: {
                                            return 7;
                                        }
                                        case 132:
                                        case 150:
                                        case 156:
                                        case 157:
                                        case 186:
                                        case 192: {
                                            return 1;
                                        }
                                    }
                                }
                            }
                            return 11;
                        }
                        return 5;
                    }
                    return 3;
                }
                return 9;
            }
            return 7;
        }
        return 1;
    }
    
    public static int getMACAlgorithm(final int n) throws IOException {
        if (n != 1) {
            Label_1164: {
                if (n != 2) {
                    if (n == 4) {
                        return 1;
                    }
                    if (n != 5 && n != 10 && n != 13 && n != 16 && n != 19 && n != 22) {
                        if (n == 24) {
                            return 1;
                        }
                        if (n != 27) {
                            switch (n) {
                                default: {
                                    switch (n) {
                                        default: {
                                            switch (n) {
                                                default: {
                                                    switch (n) {
                                                        default: {
                                                            switch (n) {
                                                                default: {
                                                                    switch (n) {
                                                                        default: {
                                                                            switch (n) {
                                                                                default: {
                                                                                    switch (n) {
                                                                                        default: {
                                                                                            throw new TlsFatalAlert((short)80);
                                                                                        }
                                                                                        case 65296:
                                                                                        case 65297:
                                                                                        case 65298:
                                                                                        case 65299:
                                                                                        case 65300:
                                                                                        case 65301: {
                                                                                            return 0;
                                                                                        }
                                                                                    }
                                                                                    break;
                                                                                }
                                                                                case 65280:
                                                                                case 65281:
                                                                                case 65282:
                                                                                case 65283:
                                                                                case 65284:
                                                                                case 65285: {
                                                                                    return 0;
                                                                                }
                                                                            }
                                                                            break;
                                                                        }
                                                                        case 52392:
                                                                        case 52393:
                                                                        case 52394:
                                                                        case 52395:
                                                                        case 52396:
                                                                        case 52397:
                                                                        case 52398: {
                                                                            return 0;
                                                                        }
                                                                    }
                                                                    break;
                                                                }
                                                                case 49267:
                                                                case 49269:
                                                                case 49271:
                                                                case 49273:
                                                                case 49301:
                                                                case 49303:
                                                                case 49305:
                                                                case 49307: {
                                                                    return 4;
                                                                }
                                                                case 49266:
                                                                case 49268:
                                                                case 49270:
                                                                case 49272:
                                                                case 49300:
                                                                case 49302:
                                                                case 49304:
                                                                case 49306: {
                                                                    return 3;
                                                                }
                                                                case 49274:
                                                                case 49275:
                                                                case 49276:
                                                                case 49277:
                                                                case 49278:
                                                                case 49279:
                                                                case 49280:
                                                                case 49281:
                                                                case 49282:
                                                                case 49283:
                                                                case 49284:
                                                                case 49285:
                                                                case 49286:
                                                                case 49287:
                                                                case 49288:
                                                                case 49289:
                                                                case 49290:
                                                                case 49291:
                                                                case 49292:
                                                                case 49293:
                                                                case 49294:
                                                                case 49295:
                                                                case 49296:
                                                                case 49297:
                                                                case 49298:
                                                                case 49299:
                                                                case 49308:
                                                                case 49309:
                                                                case 49310:
                                                                case 49311:
                                                                case 49312:
                                                                case 49313:
                                                                case 49314:
                                                                case 49315:
                                                                case 49316:
                                                                case 49317:
                                                                case 49318:
                                                                case 49319:
                                                                case 49320:
                                                                case 49321:
                                                                case 49322:
                                                                case 49323:
                                                                case 49324:
                                                                case 49325:
                                                                case 49326:
                                                                case 49327: {
                                                                    return 0;
                                                                }
                                                            }
                                                            break;
                                                        }
                                                        case 49188:
                                                        case 49190:
                                                        case 49192:
                                                        case 49194:
                                                        case 49208:
                                                        case 49211: {
                                                            return 4;
                                                        }
                                                        case 49187:
                                                        case 49189:
                                                        case 49191:
                                                        case 49193:
                                                        case 49207:
                                                        case 49210: {
                                                            return 3;
                                                        }
                                                        case 49195:
                                                        case 49196:
                                                        case 49197:
                                                        case 49198:
                                                        case 49199:
                                                        case 49200:
                                                        case 49201:
                                                        case 49202: {
                                                            return 0;
                                                        }
                                                        case 49153:
                                                        case 49154:
                                                        case 49155:
                                                        case 49156:
                                                        case 49157:
                                                        case 49158:
                                                        case 49159:
                                                        case 49160:
                                                        case 49161:
                                                        case 49162:
                                                        case 49163:
                                                        case 49164:
                                                        case 49165:
                                                        case 49166:
                                                        case 49167:
                                                        case 49168:
                                                        case 49169:
                                                        case 49170:
                                                        case 49171:
                                                        case 49172:
                                                        case 49173:
                                                        case 49174:
                                                        case 49175:
                                                        case 49176:
                                                        case 49177:
                                                        case 49178:
                                                        case 49179:
                                                        case 49180:
                                                        case 49181:
                                                        case 49182:
                                                        case 49183:
                                                        case 49184:
                                                        case 49185:
                                                        case 49186:
                                                        case 49203:
                                                        case 49204:
                                                        case 49205:
                                                        case 49206:
                                                        case 49209: {
                                                            break Label_1164;
                                                        }
                                                    }
                                                    break;
                                                }
                                                case 103:
                                                case 104:
                                                case 105:
                                                case 106:
                                                case 107:
                                                case 108:
                                                case 109: {
                                                    return 3;
                                                }
                                            }
                                            break;
                                        }
                                        case 59:
                                        case 60:
                                        case 61:
                                        case 62:
                                        case 63:
                                        case 64: {
                                            return 3;
                                        }
                                        case 44:
                                        case 45:
                                        case 46:
                                        case 47:
                                        case 48:
                                        case 49:
                                        case 50:
                                        case 51:
                                        case 52:
                                        case 53:
                                        case 54:
                                        case 55:
                                        case 56:
                                        case 57:
                                        case 58:
                                        case 65:
                                        case 66:
                                        case 67:
                                        case 68:
                                        case 69:
                                        case 70: {
                                            break Label_1164;
                                        }
                                    }
                                    break;
                                }
                                case 175:
                                case 177:
                                case 179:
                                case 181:
                                case 183:
                                case 185: {
                                    return 4;
                                }
                                case 174:
                                case 176:
                                case 178:
                                case 180:
                                case 182:
                                case 184:
                                case 186:
                                case 187:
                                case 188:
                                case 189:
                                case 190:
                                case 191:
                                case 192:
                                case 193:
                                case 194:
                                case 195:
                                case 196:
                                case 197: {
                                    return 3;
                                }
                                case 156:
                                case 157:
                                case 158:
                                case 159:
                                case 160:
                                case 161:
                                case 162:
                                case 163:
                                case 164:
                                case 165:
                                case 166:
                                case 167:
                                case 168:
                                case 169:
                                case 170:
                                case 171:
                                case 172:
                                case 173: {
                                    return 0;
                                }
                                case 132:
                                case 133:
                                case 134:
                                case 135:
                                case 136:
                                case 137:
                                case 138:
                                case 139:
                                case 140:
                                case 141:
                                case 142:
                                case 143:
                                case 144:
                                case 145:
                                case 146:
                                case 147:
                                case 148:
                                case 149:
                                case 150:
                                case 151:
                                case 152:
                                case 153:
                                case 154:
                                case 155: {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            return 2;
        }
        return 1;
    }
    
    public static ProtocolVersion getMinimumVersion(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        switch (n) {
                            default: {
                                switch (n) {
                                    default: {
                                        switch (n) {
                                            default: {
                                                switch (n) {
                                                    default: {
                                                        switch (n) {
                                                            default: {
                                                                switch (n) {
                                                                    default: {
                                                                        switch (n) {
                                                                            default: {
                                                                                switch (n) {
                                                                                    default: {
                                                                                        return ProtocolVersion.SSLv3;
                                                                                    }
                                                                                    case 65296:
                                                                                    case 65297:
                                                                                    case 65298:
                                                                                    case 65299:
                                                                                    case 65300:
                                                                                    case 65301: {
                                                                                        return ProtocolVersion.TLSv12;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 65280:
                                                                            case 65281:
                                                                            case 65282:
                                                                            case 65283:
                                                                            case 65284:
                                                                            case 65285: {
                                                                                return ProtocolVersion.TLSv12;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 52392:
                                                                    case 52393:
                                                                    case 52394:
                                                                    case 52395:
                                                                    case 52396:
                                                                    case 52397:
                                                                    case 52398: {
                                                                        return ProtocolVersion.TLSv12;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 49308:
                                                            case 49309:
                                                            case 49310:
                                                            case 49311:
                                                            case 49312:
                                                            case 49313:
                                                            case 49314:
                                                            case 49315:
                                                            case 49316:
                                                            case 49317:
                                                            case 49318:
                                                            case 49319:
                                                            case 49320:
                                                            case 49321:
                                                            case 49322:
                                                            case 49323:
                                                            case 49324:
                                                            case 49325:
                                                            case 49326:
                                                            case 49327: {
                                                                return ProtocolVersion.TLSv12;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 49266:
                                                    case 49267:
                                                    case 49268:
                                                    case 49269:
                                                    case 49270:
                                                    case 49271:
                                                    case 49272:
                                                    case 49273:
                                                    case 49274:
                                                    case 49275:
                                                    case 49276:
                                                    case 49277:
                                                    case 49278:
                                                    case 49279:
                                                    case 49280:
                                                    case 49281:
                                                    case 49282:
                                                    case 49283:
                                                    case 49284:
                                                    case 49285:
                                                    case 49286:
                                                    case 49287:
                                                    case 49288:
                                                    case 49289:
                                                    case 49290:
                                                    case 49291:
                                                    case 49292:
                                                    case 49293:
                                                    case 49294:
                                                    case 49295:
                                                    case 49296:
                                                    case 49297:
                                                    case 49298:
                                                    case 49299: {
                                                        return ProtocolVersion.TLSv12;
                                                    }
                                                }
                                                break;
                                            }
                                            case 49187:
                                            case 49188:
                                            case 49189:
                                            case 49190:
                                            case 49191:
                                            case 49192:
                                            case 49193:
                                            case 49194:
                                            case 49195:
                                            case 49196:
                                            case 49197:
                                            case 49198:
                                            case 49199:
                                            case 49200:
                                            case 49201:
                                            case 49202: {
                                                return ProtocolVersion.TLSv12;
                                            }
                                        }
                                        break;
                                    }
                                    case 186:
                                    case 187:
                                    case 188:
                                    case 189:
                                    case 190:
                                    case 191:
                                    case 192:
                                    case 193:
                                    case 194:
                                    case 195:
                                    case 196:
                                    case 197: {
                                        return ProtocolVersion.TLSv12;
                                    }
                                }
                                break;
                            }
                            case 156:
                            case 157:
                            case 158:
                            case 159:
                            case 160:
                            case 161:
                            case 162:
                            case 163:
                            case 164:
                            case 165:
                            case 166:
                            case 167:
                            case 168:
                            case 169:
                            case 170:
                            case 171:
                            case 172:
                            case 173: {
                                return ProtocolVersion.TLSv12;
                            }
                        }
                        break;
                    }
                    case 103:
                    case 104:
                    case 105:
                    case 106:
                    case 107:
                    case 108:
                    case 109: {
                        return ProtocolVersion.TLSv12;
                    }
                }
                break;
            }
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64: {
                return ProtocolVersion.TLSv12;
            }
        }
    }
    
    public static ASN1ObjectIdentifier getOIDForHashAlgorithm(final short n) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("unknown HashAlgorithm");
            }
            case 6: {
                return NISTObjectIdentifiers.id_sha512;
            }
            case 5: {
                return NISTObjectIdentifiers.id_sha384;
            }
            case 4: {
                return NISTObjectIdentifiers.id_sha256;
            }
            case 3: {
                return NISTObjectIdentifiers.id_sha224;
            }
            case 2: {
                return X509ObjectIdentifiers.id_SHA1;
            }
            case 1: {
                return PKCSObjectIdentifiers.md5;
            }
        }
    }
    
    public static Vector getSignatureAlgorithmsExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = getExtensionData(hashtable, TlsUtils.EXT_signature_algorithms);
        if (extensionData == null) {
            return null;
        }
        return readSignatureAlgorithmsExtension(extensionData);
    }
    
    public static SignatureAndHashAlgorithm getSignatureAndHashAlgorithm(final TlsContext tlsContext, final TlsSignerCredentials tlsSignerCredentials) throws IOException {
        if (!isTLSv12(tlsContext)) {
            return null;
        }
        final SignatureAndHashAlgorithm signatureAndHashAlgorithm = tlsSignerCredentials.getSignatureAndHashAlgorithm();
        if (signatureAndHashAlgorithm != null) {
            return signatureAndHashAlgorithm;
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static Vector getUsableSignatureAlgorithms(final Vector vector) {
        if (vector == null) {
            return getAllSignatureAlgorithms();
        }
        final Vector<Short> vector2 = new Vector<Short>(4);
        int i = 0;
        vector2.addElement(Shorts.valueOf((short)0));
        while (i < vector.size()) {
            final Short value = Shorts.valueOf(vector.elementAt(i).getSignature());
            if (!vector2.contains(value)) {
                vector2.addElement(value);
            }
            ++i;
        }
        return vector2;
    }
    
    public static boolean hasExpectedEmptyExtensionData(final Hashtable hashtable, final Integer n, final short n2) throws IOException {
        final byte[] extensionData = getExtensionData(hashtable, n);
        if (extensionData == null) {
            return false;
        }
        if (extensionData.length == 0) {
            return true;
        }
        throw new TlsFatalAlert(n2);
    }
    
    public static boolean hasSigningCapability(final short n) {
        return n == 1 || n == 2 || n == 64;
    }
    
    static void hmac_hash(final Digest digest, byte[] array, final byte[] array2, final byte[] array3) {
        final HMac hMac = new HMac(digest);
        hMac.init(new KeyParameter(array));
        final int digestSize = digest.getDigestSize();
        final int n = (array3.length + digestSize - 1) / digestSize;
        final int macSize = hMac.getMacSize();
        array = new byte[macSize];
        final byte[] array4 = new byte[hMac.getMacSize()];
        byte[] array5 = array2;
        for (int i = 0; i < n; ++i, array5 = array) {
            hMac.update(array5, 0, array5.length);
            hMac.doFinal(array, 0);
            hMac.update(array, 0, macSize);
            hMac.update(array2, 0, array2.length);
            hMac.doFinal(array4, 0);
            final int n2 = digestSize * i;
            System.arraycopy(array4, 0, array3, n2, Math.min(digestSize, array3.length - n2));
        }
    }
    
    public static TlsSession importSession(final byte[] array, final SessionParameters sessionParameters) {
        return new TlsSessionImpl(array, sessionParameters);
    }
    
    public static boolean isAEADCipherSuite(final int n) throws IOException {
        return 2 == getCipherType(n);
    }
    
    public static boolean isBlockCipherSuite(final int n) throws IOException {
        return 1 == getCipherType(n);
    }
    
    public static boolean isSSL(final TlsContext tlsContext) {
        return tlsContext.getServerVersion().isSSL();
    }
    
    public static boolean isSignatureAlgorithmsExtensionAllowed(final ProtocolVersion protocolVersion) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }
    
    public static boolean isStreamCipherSuite(final int n) throws IOException {
        return getCipherType(n) == 0;
    }
    
    public static boolean isTLSv11(final ProtocolVersion protocolVersion) {
        return ProtocolVersion.TLSv11.isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }
    
    public static boolean isTLSv11(final TlsContext tlsContext) {
        return isTLSv11(tlsContext.getServerVersion());
    }
    
    public static boolean isTLSv12(final ProtocolVersion protocolVersion) {
        return ProtocolVersion.TLSv12.isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }
    
    public static boolean isTLSv12(final TlsContext tlsContext) {
        return isTLSv12(tlsContext.getServerVersion());
    }
    
    public static boolean isValidCipherSuiteForSignatureAlgorithms(int keyExchangeAlgorithm, final Vector vector) {
        try {
            keyExchangeAlgorithm = getKeyExchangeAlgorithm(keyExchangeAlgorithm);
            while (true) {
                Label_0099: {
                    if (keyExchangeAlgorithm == 3 || keyExchangeAlgorithm == 4) {
                        break Label_0099;
                    }
                    Label_0091: {
                        if (keyExchangeAlgorithm != 5 && keyExchangeAlgorithm != 6) {
                            if (keyExchangeAlgorithm != 11 && keyExchangeAlgorithm != 12) {
                                if (keyExchangeAlgorithm == 17) {
                                    final Short n = Shorts.valueOf((short)3);
                                    return vector.contains(n);
                                }
                                if (keyExchangeAlgorithm == 19) {
                                    break Label_0091;
                                }
                                if (keyExchangeAlgorithm != 20) {
                                    if (keyExchangeAlgorithm == 22) {
                                        break Label_0099;
                                    }
                                    if (keyExchangeAlgorithm != 23) {
                                        return true;
                                    }
                                    break Label_0091;
                                }
                            }
                            final short n2 = 0;
                            break Label_0083;
                        }
                    }
                    Short n = Shorts.valueOf((short)1);
                    return vector.contains(n);
                    short n2 = 0;
                    n = Shorts.valueOf(n2);
                    return vector.contains(n);
                }
                final short n2 = 2;
                continue;
            }
        }
        catch (IOException ex) {
            return true;
        }
    }
    
    public static boolean isValidCipherSuiteForVersion(final int n, final ProtocolVersion protocolVersion) {
        return getMinimumVersion(n).isEqualOrEarlierVersionOf(protocolVersion.getEquivalentTLSVersion());
    }
    
    public static boolean isValidUint16(final int n) {
        return (0xFFFF & n) == n;
    }
    
    public static boolean isValidUint16(final long n) {
        return (n & 0xFFFFL) == n;
    }
    
    public static boolean isValidUint24(final int n) {
        return (0xFFFFFF & n) == n;
    }
    
    public static boolean isValidUint24(final long n) {
        return (n & 0xFFFFFFL) == n;
    }
    
    public static boolean isValidUint32(final long n) {
        return (n & 0xFFFFFFFFL) == n;
    }
    
    public static boolean isValidUint48(final long n) {
        return (n & 0xFFFFFFFFFFFFL) == n;
    }
    
    public static boolean isValidUint64(final long n) {
        return true;
    }
    
    public static boolean isValidUint8(final int n) {
        return (n & 0xFF) == n;
    }
    
    public static boolean isValidUint8(final long n) {
        return (n & 0xFFL) == n;
    }
    
    public static boolean isValidUint8(final short n) {
        return (n & 0xFF) == n;
    }
    
    public static Vector parseSupportedSignatureAlgorithms(final boolean b, final InputStream inputStream) throws IOException {
        final int uint16 = readUint16(inputStream);
        if (uint16 >= 2 && (uint16 & 0x1) == 0x0) {
            final int n = uint16 / 2;
            final Vector vector = new Vector<SignatureAndHashAlgorithm>(n);
            for (int i = 0; i < n; ++i) {
                final SignatureAndHashAlgorithm parse = SignatureAndHashAlgorithm.parse(inputStream);
                if (!b && parse.getSignature() == 0) {
                    throw new TlsFatalAlert((short)47);
                }
                vector.addElement(parse);
            }
            return vector;
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static ASN1Primitive readASN1Object(final byte[] array) throws IOException {
        final ASN1InputStream asn1InputStream = new ASN1InputStream(array);
        final ASN1Primitive object = asn1InputStream.readObject();
        if (object == null) {
            throw new TlsFatalAlert((short)50);
        }
        if (asn1InputStream.readObject() == null) {
            return object;
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static byte[] readAllOrNothing(final int n, final InputStream inputStream) throws IOException {
        if (n < 1) {
            return TlsUtils.EMPTY_BYTES;
        }
        final byte[] array = new byte[n];
        final int fully = Streams.readFully(inputStream, array);
        if (fully == 0) {
            return null;
        }
        if (fully == n) {
            return array;
        }
        throw new EOFException();
    }
    
    public static ASN1Primitive readDERObject(final byte[] array) throws IOException {
        final ASN1Primitive asn1Object = readASN1Object(array);
        if (Arrays.areEqual(asn1Object.getEncoded("DER"), array)) {
            return asn1Object;
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static void readFully(final byte[] array, final InputStream inputStream) throws IOException {
        final int length = array.length;
        if (length <= 0) {
            return;
        }
        if (length == Streams.readFully(inputStream, array)) {
            return;
        }
        throw new EOFException();
    }
    
    public static byte[] readFully(final int n, final InputStream inputStream) throws IOException {
        if (n < 1) {
            return TlsUtils.EMPTY_BYTES;
        }
        final byte[] array = new byte[n];
        if (n == Streams.readFully(inputStream, array)) {
            return array;
        }
        throw new EOFException();
    }
    
    public static byte[] readOpaque16(final InputStream inputStream) throws IOException {
        return readFully(readUint16(inputStream), inputStream);
    }
    
    public static byte[] readOpaque24(final InputStream inputStream) throws IOException {
        return readFully(readUint24(inputStream), inputStream);
    }
    
    public static byte[] readOpaque8(final InputStream inputStream) throws IOException {
        return readFully(readUint8(inputStream), inputStream);
    }
    
    public static Vector readSignatureAlgorithmsExtension(final byte[] array) throws IOException {
        if (array != null) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
            final Vector supportedSignatureAlgorithms = parseSupportedSignatureAlgorithms(false, byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return supportedSignatureAlgorithms;
        }
        throw new IllegalArgumentException("'extensionData' cannot be null");
    }
    
    public static int readUint16(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if (read2 >= 0) {
            return read2 | read << 8;
        }
        throw new EOFException();
    }
    
    public static int readUint16(final byte[] array, final int n) {
        return (array[n + 1] & 0xFF) | (array[n] & 0xFF) << 8;
    }
    
    public static int[] readUint16Array(final int n, final InputStream inputStream) throws IOException {
        final int[] array = new int[n];
        for (int i = 0; i < n; ++i) {
            array[i] = readUint16(inputStream);
        }
        return array;
    }
    
    public static int readUint24(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        if (read3 >= 0) {
            return read3 | (read << 16 | read2 << 8);
        }
        throw new EOFException();
    }
    
    public static int readUint24(final byte[] array, int n) {
        final byte b = array[n];
        ++n;
        return (array[n + 1] & 0xFF) | ((b & 0xFF) << 16 | (array[n] & 0xFF) << 8);
    }
    
    public static long readUint32(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        final int read3 = inputStream.read();
        final int read4 = inputStream.read();
        if (read4 >= 0) {
            return (long)(read4 | (read << 24 | read2 << 16 | read3 << 8)) & 0xFFFFFFFFL;
        }
        throw new EOFException();
    }
    
    public static long readUint32(final byte[] array, int n) {
        final byte b = array[n];
        final int n2 = n + 1;
        n = array[n2];
        final int n3 = n2 + 1;
        return (long)((array[n3 + 1] & 0xFF) | ((b & 0xFF) << 24 | (n & 0xFF) << 16 | (array[n3] & 0xFF) << 8)) & 0xFFFFFFFFL;
    }
    
    public static long readUint48(final InputStream inputStream) throws IOException {
        return ((long)readUint24(inputStream) & 0xFFFFFFFFL) << 24 | ((long)readUint24(inputStream) & 0xFFFFFFFFL);
    }
    
    public static long readUint48(final byte[] array, int uint24) {
        final int uint25 = readUint24(array, uint24);
        uint24 = readUint24(array, uint24 + 3);
        return ((long)uint24 & 0xFFFFFFFFL) | ((long)uint25 & 0xFFFFFFFFL) << 24;
    }
    
    public static short readUint8(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        if (read >= 0) {
            return (short)read;
        }
        throw new EOFException();
    }
    
    public static short readUint8(final byte[] array, final int n) {
        return (short)(array[n] & 0xFF);
    }
    
    public static short[] readUint8Array(final int n, final InputStream inputStream) throws IOException {
        final short[] array = new short[n];
        for (int i = 0; i < n; ++i) {
            array[i] = readUint8(inputStream);
        }
        return array;
    }
    
    public static ProtocolVersion readVersion(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if (read2 >= 0) {
            return ProtocolVersion.get(read, read2);
        }
        throw new EOFException();
    }
    
    public static ProtocolVersion readVersion(final byte[] array, final int n) throws IOException {
        return ProtocolVersion.get(array[n] & 0xFF, array[n + 1] & 0xFF);
    }
    
    public static int readVersionRaw(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        final int read2 = inputStream.read();
        if (read2 >= 0) {
            return read2 | read << 8;
        }
        throw new EOFException();
    }
    
    public static int readVersionRaw(final byte[] array, final int n) throws IOException {
        return array[n + 1] | array[n] << 8;
    }
    
    static void trackHashAlgorithms(final TlsHandshakeHash tlsHandshakeHash, final Vector vector) {
        if (vector != null) {
            for (int i = 0; i < vector.size(); ++i) {
                final short hash = vector.elementAt(i).getHash();
                if (!HashAlgorithm.isPrivate(hash)) {
                    tlsHandshakeHash.trackHashAlgorithm(hash);
                }
            }
        }
    }
    
    static void validateKeyUsage(final org.spongycastle.asn1.x509.Certificate certificate, final int n) throws IOException {
        final Extensions extensions = certificate.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final KeyUsage fromExtensions = KeyUsage.fromExtensions(extensions);
            if (fromExtensions != null) {
                if ((fromExtensions.getBytes()[0] & 0xFF & n) == n) {
                    return;
                }
                throw new TlsFatalAlert((short)46);
            }
        }
    }
    
    private static Vector vectorOfOne(final Object o) {
        final Vector<Object> vector = new Vector<Object>(1);
        vector.addElement(o);
        return vector;
    }
    
    public static void verifySupportedSignatureAlgorithm(final Vector vector, final SignatureAndHashAlgorithm signatureAndHashAlgorithm) throws IOException {
        if (vector == null || vector.size() < 1 || vector.size() >= 32768) {
            throw new IllegalArgumentException("'supportedSignatureAlgorithms' must have length from 1 to (2^15 - 1)");
        }
        if (signatureAndHashAlgorithm != null) {
            if (signatureAndHashAlgorithm.getSignature() != 0) {
                for (int i = 0; i < vector.size(); ++i) {
                    final SignatureAndHashAlgorithm signatureAndHashAlgorithm2 = vector.elementAt(i);
                    if (signatureAndHashAlgorithm2.getHash() == signatureAndHashAlgorithm.getHash() && signatureAndHashAlgorithm2.getSignature() == signatureAndHashAlgorithm.getSignature()) {
                        return;
                    }
                }
            }
            throw new TlsFatalAlert((short)47);
        }
        throw new IllegalArgumentException("'signatureAlgorithm' cannot be null");
    }
    
    public static void writeGMTUnixTime(final byte[] array, final int n) {
        final int n2 = (int)(System.currentTimeMillis() / 1000L);
        array[n] = (byte)(n2 >>> 24);
        array[n + 1] = (byte)(n2 >>> 16);
        array[n + 2] = (byte)(n2 >>> 8);
        array[n + 3] = (byte)n2;
    }
    
    public static void writeOpaque16(final byte[] array, final OutputStream outputStream) throws IOException {
        checkUint16(array.length);
        writeUint16(array.length, outputStream);
        outputStream.write(array);
    }
    
    public static void writeOpaque24(final byte[] array, final OutputStream outputStream) throws IOException {
        checkUint24(array.length);
        writeUint24(array.length, outputStream);
        outputStream.write(array);
    }
    
    public static void writeOpaque8(final byte[] array, final OutputStream outputStream) throws IOException {
        checkUint8(array.length);
        writeUint8(array.length, outputStream);
        outputStream.write(array);
    }
    
    public static void writeUint16(final int n, final OutputStream outputStream) throws IOException {
        outputStream.write(n >>> 8);
        outputStream.write(n);
    }
    
    public static void writeUint16(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 8);
        array[n2 + 1] = (byte)n;
    }
    
    public static void writeUint16Array(final int[] array, final OutputStream outputStream) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            writeUint16(array[i], outputStream);
        }
    }
    
    public static void writeUint16Array(final int[] array, final byte[] array2, int i) throws IOException {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            writeUint16(array[i], array2, n2);
            n2 += 2;
        }
    }
    
    public static void writeUint16ArrayWithUint16Length(final int[] array, final OutputStream outputStream) throws IOException {
        final int n = array.length * 2;
        checkUint16(n);
        writeUint16(n, outputStream);
        writeUint16Array(array, outputStream);
    }
    
    public static void writeUint16ArrayWithUint16Length(final int[] array, final byte[] array2, final int n) throws IOException {
        final int n2 = array.length * 2;
        checkUint16(n2);
        writeUint16(n2, array2, n);
        writeUint16Array(array, array2, n + 2);
    }
    
    public static void writeUint24(final int n, final OutputStream outputStream) throws IOException {
        outputStream.write((byte)(n >>> 16));
        outputStream.write((byte)(n >>> 8));
        outputStream.write((byte)n);
    }
    
    public static void writeUint24(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 16);
        array[n2 + 1] = (byte)(n >>> 8);
        array[n2 + 2] = (byte)n;
    }
    
    public static void writeUint32(final long n, final OutputStream outputStream) throws IOException {
        outputStream.write((byte)(n >>> 24));
        outputStream.write((byte)(n >>> 16));
        outputStream.write((byte)(n >>> 8));
        outputStream.write((byte)n);
    }
    
    public static void writeUint32(final long n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 24);
        array[n2 + 1] = (byte)(n >>> 16);
        array[n2 + 2] = (byte)(n >>> 8);
        array[n2 + 3] = (byte)n;
    }
    
    public static void writeUint48(final long n, final OutputStream outputStream) throws IOException {
        outputStream.write((byte)(n >>> 40));
        outputStream.write((byte)(n >>> 32));
        outputStream.write((byte)(n >>> 24));
        outputStream.write((byte)(n >>> 16));
        outputStream.write((byte)(n >>> 8));
        outputStream.write((byte)n);
    }
    
    public static void writeUint48(final long n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 40);
        array[n2 + 1] = (byte)(n >>> 32);
        array[n2 + 2] = (byte)(n >>> 24);
        array[n2 + 3] = (byte)(n >>> 16);
        array[n2 + 4] = (byte)(n >>> 8);
        array[n2 + 5] = (byte)n;
    }
    
    public static void writeUint64(final long n, final OutputStream outputStream) throws IOException {
        outputStream.write((byte)(n >>> 56));
        outputStream.write((byte)(n >>> 48));
        outputStream.write((byte)(n >>> 40));
        outputStream.write((byte)(n >>> 32));
        outputStream.write((byte)(n >>> 24));
        outputStream.write((byte)(n >>> 16));
        outputStream.write((byte)(n >>> 8));
        outputStream.write((byte)n);
    }
    
    public static void writeUint64(final long n, final byte[] array, final int n2) {
        array[n2] = (byte)(n >>> 56);
        array[n2 + 1] = (byte)(n >>> 48);
        array[n2 + 2] = (byte)(n >>> 40);
        array[n2 + 3] = (byte)(n >>> 32);
        array[n2 + 4] = (byte)(n >>> 24);
        array[n2 + 5] = (byte)(n >>> 16);
        array[n2 + 6] = (byte)(n >>> 8);
        array[n2 + 7] = (byte)n;
    }
    
    public static void writeUint8(final int n, final OutputStream outputStream) throws IOException {
        outputStream.write(n);
    }
    
    public static void writeUint8(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
    }
    
    public static void writeUint8(final short n, final OutputStream outputStream) throws IOException {
        outputStream.write(n);
    }
    
    public static void writeUint8(final short n, final byte[] array, final int n2) {
        array[n2] = (byte)n;
    }
    
    public static void writeUint8Array(final short[] array, final OutputStream outputStream) throws IOException {
        for (int i = 0; i < array.length; ++i) {
            writeUint8(array[i], outputStream);
        }
    }
    
    public static void writeUint8Array(final short[] array, final byte[] array2, int i) throws IOException {
        final int n = 0;
        int n2 = i;
        for (i = n; i < array.length; ++i) {
            writeUint8(array[i], array2, n2);
            ++n2;
        }
    }
    
    public static void writeUint8ArrayWithUint8Length(final short[] array, final OutputStream outputStream) throws IOException {
        checkUint8(array.length);
        writeUint8(array.length, outputStream);
        writeUint8Array(array, outputStream);
    }
    
    public static void writeUint8ArrayWithUint8Length(final short[] array, final byte[] array2, final int n) throws IOException {
        checkUint8(array.length);
        writeUint8(array.length, array2, n);
        writeUint8Array(array, array2, n + 1);
    }
    
    public static void writeVersion(final ProtocolVersion protocolVersion, final OutputStream outputStream) throws IOException {
        outputStream.write(protocolVersion.getMajorVersion());
        outputStream.write(protocolVersion.getMinorVersion());
    }
    
    public static void writeVersion(final ProtocolVersion protocolVersion, final byte[] array, final int n) {
        array[n] = (byte)protocolVersion.getMajorVersion();
        array[n + 1] = (byte)protocolVersion.getMinorVersion();
    }
}
