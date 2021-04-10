package org.spongycastle.asn1.gm;

import java.math.*;
import org.spongycastle.math.ec.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;
import java.util.*;

public class GMNamedCurves
{
    static final Hashtable curves;
    static final Hashtable names;
    static final Hashtable objIds;
    static X9ECParametersHolder sm2p256v1;
    static X9ECParametersHolder wapip192v1;
    
    static {
        GMNamedCurves.sm2p256v1 = new X9ECParametersHolder() {
            @Override
            protected X9ECParameters createParameters() {
                final BigInteger access$000 = fromHex("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFF");
                final BigInteger access$2 = fromHex("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF00000000FFFFFFFFFFFFFFFC");
                final BigInteger access$3 = fromHex("28E9FA9E9D9F5E344D5A9E4BCF6509A7F39789F515AB8F92DDBCBD414D940E93");
                final BigInteger access$4 = fromHex("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123");
                final BigInteger value = BigInteger.valueOf(1L);
                final ECCurve access$5 = configureCurve(new ECCurve.Fp(access$000, access$2, access$3, access$4, value));
                return new X9ECParameters(access$5, new X9ECPoint(access$5, Hex.decode("0432C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0")), access$4, value, null);
            }
        };
        GMNamedCurves.wapip192v1 = new X9ECParametersHolder() {
            @Override
            protected X9ECParameters createParameters() {
                final BigInteger access$000 = fromHex("BDB6F4FE3E8B1D9E0DA8C0D46F4C318CEFE4AFE3B6B8551F");
                final BigInteger access$2 = fromHex("BB8E5E8FBC115E139FE6A814FE48AAA6F0ADA1AA5DF91985");
                final BigInteger access$3 = fromHex("1854BEBDC31B21B7AEFC80AB0ECD10D5B1B3308E6DBF11C1");
                final BigInteger access$4 = fromHex("BDB6F4FE3E8B1D9E0DA8C0D40FC962195DFAE76F56564677");
                final BigInteger value = BigInteger.valueOf(1L);
                final ECCurve access$5 = configureCurve(new ECCurve.Fp(access$000, access$2, access$3, access$4, value));
                return new X9ECParameters(access$5, new X9ECPoint(access$5, Hex.decode("044AD5F7048DE709AD51236DE65E4D4B482C836DC6E410664002BB3A02D4AAADACAE24817A4CA3A1B014B5270432DB27D2")), access$4, value, null);
            }
        };
        objIds = new Hashtable();
        curves = new Hashtable();
        names = new Hashtable();
        defineCurve("wapip192v1", GMObjectIdentifiers.wapip192v1, GMNamedCurves.wapip192v1);
        defineCurve("sm2p256v1", GMObjectIdentifiers.sm2p256v1, GMNamedCurves.sm2p256v1);
    }
    
    private static ECCurve configureCurve(final ECCurve ecCurve) {
        return ecCurve;
    }
    
    static void defineCurve(final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier, final X9ECParametersHolder x9ECParametersHolder) {
        GMNamedCurves.objIds.put(Strings.toLowerCase(s), asn1ObjectIdentifier);
        GMNamedCurves.names.put(asn1ObjectIdentifier, s);
        GMNamedCurves.curves.put(asn1ObjectIdentifier, x9ECParametersHolder);
    }
    
    private static BigInteger fromHex(final String s) {
        return new BigInteger(1, Hex.decode(s));
    }
    
    public static X9ECParameters getByName(final String s) {
        final ASN1ObjectIdentifier oid = getOID(s);
        if (oid == null) {
            return null;
        }
        return getByOID(oid);
    }
    
    public static X9ECParameters getByOID(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final X9ECParametersHolder x9ECParametersHolder = GMNamedCurves.curves.get(asn1ObjectIdentifier);
        if (x9ECParametersHolder == null) {
            return null;
        }
        return x9ECParametersHolder.getParameters();
    }
    
    public static String getName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return GMNamedCurves.names.get(asn1ObjectIdentifier);
    }
    
    public static Enumeration getNames() {
        return GMNamedCurves.names.elements();
    }
    
    public static ASN1ObjectIdentifier getOID(final String s) {
        return GMNamedCurves.objIds.get(Strings.toLowerCase(s));
    }
}
