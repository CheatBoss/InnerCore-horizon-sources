package org.spongycastle.crypto.tls;

import java.util.*;
import org.spongycastle.crypto.agreement.*;
import org.spongycastle.util.*;
import java.math.*;
import java.security.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.asn1.x9.*;
import java.io.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.math.field.*;

public class TlsECCUtils
{
    private static final String[] CURVE_NAMES;
    public static final Integer EXT_ec_point_formats;
    public static final Integer EXT_elliptic_curves;
    
    static {
        EXT_elliptic_curves = Integers.valueOf(10);
        EXT_ec_point_formats = Integers.valueOf(11);
        CURVE_NAMES = new String[] { "sect163k1", "sect163r1", "sect163r2", "sect193r1", "sect193r2", "sect233k1", "sect233r1", "sect239k1", "sect283k1", "sect283r1", "sect409k1", "sect409r1", "sect571k1", "sect571r1", "secp160k1", "secp160r1", "secp160r2", "secp192k1", "secp192r1", "secp224k1", "secp224r1", "secp256k1", "secp256r1", "secp384r1", "secp521r1", "brainpoolP256r1", "brainpoolP384r1", "brainpoolP512r1" };
    }
    
    public static void addSupportedEllipticCurvesExtension(final Hashtable hashtable, final int[] array) throws IOException {
        hashtable.put(TlsECCUtils.EXT_elliptic_curves, createSupportedEllipticCurvesExtension(array));
    }
    
    public static void addSupportedPointFormatsExtension(final Hashtable hashtable, final short[] array) throws IOException {
        hashtable.put(TlsECCUtils.EXT_ec_point_formats, createSupportedPointFormatsExtension(array));
    }
    
    public static boolean areOnSameCurve(final ECDomainParameters ecDomainParameters, final ECDomainParameters ecDomainParameters2) {
        return ecDomainParameters != null && ecDomainParameters.equals(ecDomainParameters2);
    }
    
    public static byte[] calculateECDHBasicAgreement(final ECPublicKeyParameters ecPublicKeyParameters, final ECPrivateKeyParameters ecPrivateKeyParameters) {
        final ECDHBasicAgreement ecdhBasicAgreement = new ECDHBasicAgreement();
        ecdhBasicAgreement.init(ecPrivateKeyParameters);
        return BigIntegers.asUnsignedByteArray(ecdhBasicAgreement.getFieldSize(), ecdhBasicAgreement.calculateAgreement(ecPublicKeyParameters));
    }
    
    private static void checkNamedCurve(final int[] array, final int n) throws IOException {
        if (array == null) {
            return;
        }
        if (Arrays.contains(array, n)) {
            return;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public static boolean containsECCCipherSuites(final int[] array) {
        for (int i = 0; i < array.length; ++i) {
            if (isECCCipherSuite(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static byte[] createSupportedEllipticCurvesExtension(final int[] array) throws IOException {
        if (array != null && array.length >= 1) {
            return TlsUtils.encodeUint16ArrayWithUint16Length(array);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static byte[] createSupportedPointFormatsExtension(final short[] array) throws IOException {
        if (array != null) {
            final short[] append = array;
            if (Arrays.contains(array, (short)0)) {
                return TlsUtils.encodeUint8ArrayWithUint8Length(append);
            }
        }
        final short[] append = Arrays.append(array, (short)0);
        return TlsUtils.encodeUint8ArrayWithUint8Length(append);
    }
    
    public static BigInteger deserializeECFieldElement(int n, final byte[] array) throws IOException {
        n = (n + 7) / 8;
        if (array.length == n) {
            return new BigInteger(1, array);
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static ECPoint deserializeECPoint(final short[] array, final ECCurve ecCurve, final byte[] array2) throws IOException {
        if (array2 == null || array2.length < 1) {
            throw new TlsFatalAlert((short)47);
        }
        short n = 0;
        final byte b = array2[0];
        if (b != 2 && b != 3) {
            if (b != 4) {
                throw new TlsFatalAlert((short)47);
            }
        }
        else if (ECAlgorithms.isF2mCurve(ecCurve)) {
            n = 2;
        }
        else {
            if (!ECAlgorithms.isFpCurve(ecCurve)) {
                throw new TlsFatalAlert((short)47);
            }
            n = 1;
        }
        if (n != 0 && (array == null || !Arrays.contains(array, n))) {
            throw new TlsFatalAlert((short)47);
        }
        return ecCurve.decodePoint(array2);
    }
    
    public static ECPublicKeyParameters deserializeECPublicKey(final short[] array, final ECDomainParameters ecDomainParameters, final byte[] array2) throws IOException {
        try {
            return new ECPublicKeyParameters(deserializeECPoint(array, ecDomainParameters.getCurve(), array2), ecDomainParameters);
        }
        catch (RuntimeException ex) {
            throw new TlsFatalAlert((short)47, ex);
        }
    }
    
    public static AsymmetricCipherKeyPair generateECKeyPair(final SecureRandom secureRandom, final ECDomainParameters ecDomainParameters) {
        final ECKeyPairGenerator ecKeyPairGenerator = new ECKeyPairGenerator();
        ecKeyPairGenerator.init(new ECKeyGenerationParameters(ecDomainParameters, secureRandom));
        return ecKeyPairGenerator.generateKeyPair();
    }
    
    public static ECPrivateKeyParameters generateEphemeralClientKeyExchange(final SecureRandom secureRandom, final short[] array, final ECDomainParameters ecDomainParameters, final OutputStream outputStream) throws IOException {
        final AsymmetricCipherKeyPair generateECKeyPair = generateECKeyPair(secureRandom, ecDomainParameters);
        writeECPoint(array, ((ECPublicKeyParameters)generateECKeyPair.getPublic()).getQ(), outputStream);
        return (ECPrivateKeyParameters)generateECKeyPair.getPrivate();
    }
    
    static ECPrivateKeyParameters generateEphemeralServerKeyExchange(final SecureRandom secureRandom, final int[] array, final short[] array2, final OutputStream outputStream) throws IOException {
        int n = 0;
        Label_0062: {
            if (array == null) {
                n = 23;
            }
            else {
                for (int i = 0; i < array.length; ++i) {
                    final int n2 = array[i];
                    if (NamedCurve.isValid(n2) && isSupportedNamedCurve(n2)) {
                        n = n2;
                        break Label_0062;
                    }
                }
                n = -1;
            }
        }
        ECDomainParameters ecDomainParameters = null;
        if (n >= 0) {
            ecDomainParameters = getParametersForNamedCurve(n);
        }
        else if (Arrays.contains(array, 65281)) {
            ecDomainParameters = getParametersForNamedCurve(23);
        }
        else if (Arrays.contains(array, 65282)) {
            ecDomainParameters = getParametersForNamedCurve(10);
        }
        if (ecDomainParameters != null) {
            if (n < 0) {
                writeExplicitECParameters(array2, ecDomainParameters, outputStream);
            }
            else {
                writeNamedECParameters(n, outputStream);
            }
            return generateEphemeralClientKeyExchange(secureRandom, array2, ecDomainParameters, outputStream);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    public static String getNameOfNamedCurve(final int n) {
        if (isSupportedNamedCurve(n)) {
            return TlsECCUtils.CURVE_NAMES[n - 1];
        }
        return null;
    }
    
    public static ECDomainParameters getParametersForNamedCurve(final int n) {
        final String nameOfNamedCurve = getNameOfNamedCurve(n);
        if (nameOfNamedCurve == null) {
            return null;
        }
        X9ECParameters x9ECParameters;
        if ((x9ECParameters = CustomNamedCurves.getByName(nameOfNamedCurve)) == null && (x9ECParameters = ECNamedCurveTable.getByName(nameOfNamedCurve)) == null) {
            return null;
        }
        return new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
    }
    
    public static int[] getSupportedEllipticCurvesExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsECCUtils.EXT_elliptic_curves);
        if (extensionData == null) {
            return null;
        }
        return readSupportedEllipticCurvesExtension(extensionData);
    }
    
    public static short[] getSupportedPointFormatsExtension(final Hashtable hashtable) throws IOException {
        final byte[] extensionData = TlsUtils.getExtensionData(hashtable, TlsECCUtils.EXT_ec_point_formats);
        if (extensionData == null) {
            return null;
        }
        return readSupportedPointFormatsExtension(extensionData);
    }
    
    public static boolean hasAnySupportedNamedCurves() {
        return TlsECCUtils.CURVE_NAMES.length > 0;
    }
    
    public static boolean isCompressionPreferred(final short[] array, final short n) {
        if (array == null) {
            return false;
        }
        for (int i = 0; i < array.length; ++i) {
            final short n2 = array[i];
            if (n2 == 0) {
                return false;
            }
            if (n2 == n) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isECCCipherSuite(final int n) {
        Label_0474: {
            if (n != 52396) {
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
                                                                                        return false;
                                                                                    }
                                                                                    case 65300:
                                                                                    case 65301: {
                                                                                        break Label_0474;
                                                                                    }
                                                                                }
                                                                                break;
                                                                            }
                                                                            case 65282:
                                                                            case 65283:
                                                                            case 65284:
                                                                            case 65285: {
                                                                                break Label_0474;
                                                                            }
                                                                        }
                                                                        break;
                                                                    }
                                                                    case 52392:
                                                                    case 52393: {
                                                                        break Label_0474;
                                                                    }
                                                                }
                                                                break;
                                                            }
                                                            case 49324:
                                                            case 49325:
                                                            case 49326:
                                                            case 49327: {
                                                                break Label_0474;
                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case 49306:
                                                    case 49307: {
                                                        break Label_0474;
                                                    }
                                                }
                                                break;
                                            }
                                            case 49286:
                                            case 49287:
                                            case 49288:
                                            case 49289:
                                            case 49290:
                                            case 49291:
                                            case 49292:
                                            case 49293: {
                                                break Label_0474;
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
                                    case 49273: {
                                        break Label_0474;
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
                            case 49202:
                            case 49203:
                            case 49204:
                            case 49205:
                            case 49206:
                            case 49207:
                            case 49208:
                            case 49209:
                            case 49210:
                            case 49211: {
                                break Label_0474;
                            }
                        }
                        break;
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
                    case 49177: {
                        break;
                    }
                }
            }
        }
        return true;
    }
    
    public static boolean isSupportedNamedCurve(final int n) {
        return n > 0 && n <= TlsECCUtils.CURVE_NAMES.length;
    }
    
    public static int readECExponent(final int n, final InputStream inputStream) throws IOException {
        final BigInteger ecParameter = readECParameter(inputStream);
        if (ecParameter.bitLength() < 32) {
            final int intValue = ecParameter.intValue();
            if (intValue > 0 && intValue < n) {
                return intValue;
            }
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public static BigInteger readECFieldElement(final int n, final InputStream inputStream) throws IOException {
        return deserializeECFieldElement(n, TlsUtils.readOpaque8(inputStream));
    }
    
    public static BigInteger readECParameter(final InputStream inputStream) throws IOException {
        return new BigInteger(1, TlsUtils.readOpaque8(inputStream));
    }
    
    public static ECDomainParameters readECParameters(final int[] array, final short[] array2, final InputStream inputStream) throws IOException {
        while (true) {
            while (true) {
                try {
                    final short uint8 = TlsUtils.readUint8(inputStream);
                    if (uint8 == 1) {
                        checkNamedCurve(array, 65281);
                        final BigInteger ecParameter = readECParameter(inputStream);
                        final BigInteger ecFieldElement = readECFieldElement(ecParameter.bitLength(), inputStream);
                        final BigInteger ecFieldElement2 = readECFieldElement(ecParameter.bitLength(), inputStream);
                        final byte[] opaque8 = TlsUtils.readOpaque8(inputStream);
                        final BigInteger ecParameter2 = readECParameter(inputStream);
                        final BigInteger ecParameter3 = readECParameter(inputStream);
                        final ECCurve.Fp fp = new ECCurve.Fp(ecParameter, ecFieldElement, ecFieldElement2, ecParameter2, ecParameter3);
                        return new ECDomainParameters(fp, deserializeECPoint(array2, fp, opaque8), ecParameter2, ecParameter3);
                    }
                    if (uint8 != 2) {
                        if (uint8 != 3) {
                            throw new TlsFatalAlert((short)47);
                        }
                        final int uint9 = TlsUtils.readUint16(inputStream);
                        if (NamedCurve.refersToASpecificNamedCurve(uint9)) {
                            checkNamedCurve(array, uint9);
                            return getParametersForNamedCurve(uint9);
                        }
                        throw new TlsFatalAlert((short)47);
                    }
                    else {
                        checkNamedCurve(array, 65282);
                        final int uint10 = TlsUtils.readUint16(inputStream);
                        final short uint11 = TlsUtils.readUint8(inputStream);
                        if (!ECBasisType.isValid(uint11)) {
                            throw new TlsFatalAlert((short)47);
                        }
                        final int ecExponent = readECExponent(uint10, inputStream);
                        if (uint11 == 2) {
                            final int ecExponent2 = readECExponent(uint10, inputStream);
                            final int ecExponent3 = readECExponent(uint10, inputStream);
                            final BigInteger ecFieldElement3 = readECFieldElement(uint10, inputStream);
                            final BigInteger ecFieldElement4 = readECFieldElement(uint10, inputStream);
                            final byte[] opaque9 = TlsUtils.readOpaque8(inputStream);
                            final BigInteger ecParameter4 = readECParameter(inputStream);
                            final BigInteger ecParameter5 = readECParameter(inputStream);
                            ECCurve.F2m f2m;
                            if (uint11 == 2) {
                                f2m = new ECCurve.F2m(uint10, ecExponent, ecExponent2, ecExponent3, ecFieldElement3, ecFieldElement4, ecParameter4, ecParameter5);
                            }
                            else {
                                f2m = new ECCurve.F2m(uint10, ecExponent, ecFieldElement3, ecFieldElement4, ecParameter4, ecParameter5);
                            }
                            return new ECDomainParameters(f2m, deserializeECPoint(array2, f2m, opaque9), ecParameter4, ecParameter5);
                        }
                    }
                }
                catch (RuntimeException ex) {
                    throw new TlsFatalAlert((short)47, ex);
                }
                final int ecExponent2 = -1;
                final int ecExponent3 = -1;
                continue;
            }
        }
    }
    
    public static int[] readSupportedEllipticCurvesExtension(final byte[] array) throws IOException {
        if (array == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final int uint16 = TlsUtils.readUint16(byteArrayInputStream);
        if (uint16 >= 2 && (uint16 & 0x1) == 0x0) {
            final int[] uint16Array = TlsUtils.readUint16Array(uint16 / 2, byteArrayInputStream);
            TlsProtocol.assertEmpty(byteArrayInputStream);
            return uint16Array;
        }
        throw new TlsFatalAlert((short)50);
    }
    
    public static short[] readSupportedPointFormatsExtension(final byte[] array) throws IOException {
        if (array == null) {
            throw new IllegalArgumentException("'extensionData' cannot be null");
        }
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(array);
        final short uint8 = TlsUtils.readUint8(byteArrayInputStream);
        if (uint8 < 1) {
            throw new TlsFatalAlert((short)50);
        }
        final short[] uint8Array = TlsUtils.readUint8Array(uint8, byteArrayInputStream);
        TlsProtocol.assertEmpty(byteArrayInputStream);
        if (Arrays.contains(uint8Array, (short)0)) {
            return uint8Array;
        }
        throw new TlsFatalAlert((short)47);
    }
    
    public static byte[] serializeECFieldElement(final int n, final BigInteger bigInteger) throws IOException {
        return BigIntegers.asUnsignedByteArray((n + 7) / 8, bigInteger);
    }
    
    public static byte[] serializeECPoint(final short[] array, final ECPoint ecPoint) throws IOException {
        final ECCurve curve = ecPoint.getCurve();
        short n;
        if (ECAlgorithms.isFpCurve(curve)) {
            n = 1;
        }
        else {
            if (!ECAlgorithms.isF2mCurve(curve)) {
                final boolean compressionPreferred = false;
                return ecPoint.getEncoded(compressionPreferred);
            }
            n = 2;
        }
        final boolean compressionPreferred = isCompressionPreferred(array, n);
        return ecPoint.getEncoded(compressionPreferred);
    }
    
    public static byte[] serializeECPublicKey(final short[] array, final ECPublicKeyParameters ecPublicKeyParameters) throws IOException {
        return serializeECPoint(array, ecPublicKeyParameters.getQ());
    }
    
    public static ECPublicKeyParameters validateECPublicKey(final ECPublicKeyParameters ecPublicKeyParameters) throws IOException {
        return ecPublicKeyParameters;
    }
    
    public static void writeECExponent(final int n, final OutputStream outputStream) throws IOException {
        writeECParameter(BigInteger.valueOf(n), outputStream);
    }
    
    public static void writeECFieldElement(final int n, final BigInteger bigInteger, final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque8(serializeECFieldElement(n, bigInteger), outputStream);
    }
    
    public static void writeECFieldElement(final ECFieldElement ecFieldElement, final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque8(ecFieldElement.getEncoded(), outputStream);
    }
    
    public static void writeECParameter(final BigInteger bigInteger, final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque8(BigIntegers.asUnsignedByteArray(bigInteger), outputStream);
    }
    
    public static void writeECPoint(final short[] array, final ECPoint ecPoint, final OutputStream outputStream) throws IOException {
        TlsUtils.writeOpaque8(serializeECPoint(array, ecPoint), outputStream);
    }
    
    public static void writeExplicitECParameters(final short[] array, final ECDomainParameters ecDomainParameters, final OutputStream outputStream) throws IOException {
        final ECCurve curve = ecDomainParameters.getCurve();
        if (ECAlgorithms.isFpCurve(curve)) {
            TlsUtils.writeUint8((short)1, outputStream);
            writeECParameter(curve.getField().getCharacteristic(), outputStream);
        }
        else {
            if (!ECAlgorithms.isF2mCurve(curve)) {
                throw new IllegalArgumentException("'ecParameters' not a known curve type");
            }
            final int[] exponentsPresent = ((PolynomialExtensionField)curve.getField()).getMinimalPolynomial().getExponentsPresent();
            TlsUtils.writeUint8((short)2, outputStream);
            final int n = exponentsPresent[exponentsPresent.length - 1];
            TlsUtils.checkUint16(n);
            TlsUtils.writeUint16(n, outputStream);
            int n2;
            if (exponentsPresent.length == 3) {
                TlsUtils.writeUint8((short)1, outputStream);
                n2 = exponentsPresent[1];
            }
            else {
                if (exponentsPresent.length != 5) {
                    throw new IllegalArgumentException("Only trinomial and pentomial curves are supported");
                }
                TlsUtils.writeUint8((short)2, outputStream);
                writeECExponent(exponentsPresent[1], outputStream);
                writeECExponent(exponentsPresent[2], outputStream);
                n2 = exponentsPresent[3];
            }
            writeECExponent(n2, outputStream);
        }
        writeECFieldElement(curve.getA(), outputStream);
        writeECFieldElement(curve.getB(), outputStream);
        TlsUtils.writeOpaque8(serializeECPoint(array, ecDomainParameters.getG()), outputStream);
        writeECParameter(ecDomainParameters.getN(), outputStream);
        writeECParameter(ecDomainParameters.getH(), outputStream);
    }
    
    public static void writeNamedECParameters(final int n, final OutputStream outputStream) throws IOException {
        if (NamedCurve.refersToASpecificNamedCurve(n)) {
            TlsUtils.writeUint8((short)3, outputStream);
            TlsUtils.checkUint16(n);
            TlsUtils.writeUint16(n, outputStream);
            return;
        }
        throw new TlsFatalAlert((short)80);
    }
}
