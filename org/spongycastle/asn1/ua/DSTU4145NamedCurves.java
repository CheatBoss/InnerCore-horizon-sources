package org.spongycastle.asn1.ua;

import java.math.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.math.ec.*;

public class DSTU4145NamedCurves
{
    private static final BigInteger ONE;
    private static final BigInteger ZERO;
    static final String oidBase;
    static final ASN1ObjectIdentifier[] oids;
    public static final ECDomainParameters[] params;
    
    static {
        ZERO = BigInteger.valueOf(0L);
        ONE = BigInteger.valueOf(1L);
        params = new ECDomainParameters[10];
        oids = new ASN1ObjectIdentifier[10];
        final StringBuilder sb = new StringBuilder();
        sb.append(UAObjectIdentifiers.dstu4145le.getId());
        sb.append(".2.");
        oidBase = sb.toString();
        final BigInteger[] array = { new BigInteger("400000000000000000002BEC12BE2262D39BCF14D", 16), new BigInteger("3FFFFFFFFFFFFFFFFFFFFFB12EBCC7D7F29FF7701F", 16), new BigInteger("800000000000000000000189B4E67606E3825BB2831", 16), new BigInteger("3FFFFFFFFFFFFFFFFFFFFFFB981960435FE5AB64236EF", 16), new BigInteger("40000000000000000000000069A779CAC1DABC6788F7474F", 16), new BigInteger("1000000000000000000000000000013E974E72F8A6922031D2603CFE0D7", 16), new BigInteger("800000000000000000000000000000006759213AF182E987D3E17714907D470D", 16), new BigInteger("3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC079C2F3825DA70D390FBBA588D4604022B7B7", 16), new BigInteger("40000000000000000000000000000000000000000000009C300B75A3FA824F22428FD28CE8812245EF44049B2D49", 16), new BigInteger("3FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFBA3175458009A8C0A724F02F81AA8A1FCBAF80D90C7A95110504CF", 16) };
        final BigInteger[] array2 = { BigInteger.valueOf(2L), BigInteger.valueOf(2L), BigInteger.valueOf(4L), BigInteger.valueOf(2L), BigInteger.valueOf(2L), BigInteger.valueOf(2L), BigInteger.valueOf(4L), BigInteger.valueOf(2L), BigInteger.valueOf(2L), BigInteger.valueOf(2L) };
        final ECCurve.F2m[] array3 = { new ECCurve.F2m(163, 3, 6, 7, DSTU4145NamedCurves.ONE, new BigInteger("5FF6108462A2DC8210AB403925E638A19C1455D21", 16), array[0], array2[0]), new ECCurve.F2m(167, 6, DSTU4145NamedCurves.ONE, new BigInteger("6EE3CEEB230811759F20518A0930F1A4315A827DAC", 16), array[1], array2[1]), new ECCurve.F2m(173, 1, 2, 10, DSTU4145NamedCurves.ZERO, new BigInteger("108576C80499DB2FC16EDDF6853BBB278F6B6FB437D9", 16), array[2], array2[2]), new ECCurve.F2m(179, 1, 2, 4, DSTU4145NamedCurves.ONE, new BigInteger("4A6E0856526436F2F88DD07A341E32D04184572BEB710", 16), array[3], array2[3]), new ECCurve.F2m(191, 9, DSTU4145NamedCurves.ONE, new BigInteger("7BC86E2102902EC4D5890E8B6B4981ff27E0482750FEFC03", 16), array[4], array2[4]), new ECCurve.F2m(233, 1, 4, 9, DSTU4145NamedCurves.ONE, new BigInteger("06973B15095675534C7CF7E64A21BD54EF5DD3B8A0326AA936ECE454D2C", 16), array[5], array2[5]), new ECCurve.F2m(257, 12, DSTU4145NamedCurves.ZERO, new BigInteger("1CEF494720115657E18F938D7A7942394FF9425C1458C57861F9EEA6ADBE3BE10", 16), array[6], array2[6]), new ECCurve.F2m(307, 2, 4, 8, DSTU4145NamedCurves.ONE, new BigInteger("393C7F7D53666B5054B5E6C6D3DE94F4296C0C599E2E2E241050DF18B6090BDC90186904968BB", 16), array[7], array2[7]), new ECCurve.F2m(367, 21, DSTU4145NamedCurves.ONE, new BigInteger("43FC8AD242B0B7A6F3D1627AD5654447556B47BF6AA4A64B0C2AFE42CADAB8F93D92394C79A79755437B56995136", 16), array[8], array2[8]), new ECCurve.F2m(431, 1, 3, 5, DSTU4145NamedCurves.ONE, new BigInteger("03CE10490F6A708FC26DFE8C3D27C4F94E690134D5BFF988D8D28AAEAEDE975936C66BAC536B18AE2DC312CA493117DAA469C640CAF3", 16), array[9], array2[9]) };
        final ECPoint point = array3[0].createPoint(new BigInteger("2E2F85F5DD74CE983A5C4237229DAF8A3F35823BE", 16), new BigInteger("3826F008A8C51D7B95284D9D03FF0E00CE2CD723A", 16));
        final ECPoint point2 = array3[1].createPoint(new BigInteger("7A1F6653786A68192803910A3D30B2A2018B21CD54", 16), new BigInteger("5F49EB26781C0EC6B8909156D98ED435E45FD59918", 16));
        final ECPoint point3 = array3[2].createPoint(new BigInteger("4D41A619BCC6EADF0448FA22FAD567A9181D37389CA", 16), new BigInteger("10B51CC12849B234C75E6DD2028BF7FF5C1CE0D991A1", 16));
        final ECPoint point4 = array3[3].createPoint(new BigInteger("6BA06FE51464B2BD26DC57F48819BA9954667022C7D03", 16), new BigInteger("25FBC363582DCEC065080CA8287AAFF09788A66DC3A9E", 16));
        final ECPoint point5 = array3[4].createPoint(new BigInteger("714114B762F2FF4A7912A6D2AC58B9B5C2FCFE76DAEB7129", 16), new BigInteger("29C41E568B77C617EFE5902F11DB96FA9613CD8D03DB08DA", 16));
        final ECPoint point6 = array3[5].createPoint(new BigInteger("3FCDA526B6CDF83BA1118DF35B3C31761D3545F32728D003EEB25EFE96", 16), new BigInteger("9CA8B57A934C54DEEDA9E54A7BBAD95E3B2E91C54D32BE0B9DF96D8D35", 16));
        final ECPoint point7 = array3[6].createPoint(new BigInteger("02A29EF207D0E9B6C55CD260B306C7E007AC491CA1B10C62334A9E8DCD8D20FB7", 16), new BigInteger("10686D41FF744D4449FCCF6D8EEA03102E6812C93A9D60B978B702CF156D814EF", 16));
        final ECPoint point8 = array3[7].createPoint(new BigInteger("216EE8B189D291A0224984C1E92F1D16BF75CCD825A087A239B276D3167743C52C02D6E7232AA", 16), new BigInteger("5D9306BACD22B7FAEB09D2E049C6E2866C5D1677762A8F2F2DC9A11C7F7BE8340AB2237C7F2A0", 16));
        final ECPoint point9 = array3[8].createPoint(new BigInteger("324A6EDDD512F08C49A99AE0D3F961197A76413E7BE81A400CA681E09639B5FE12E59A109F78BF4A373541B3B9A1", 16), new BigInteger("1AB597A5B4477F59E39539007C7F977D1A567B92B043A49C6B61984C3FE3481AAF454CD41BA1F051626442B3C10", 16));
        final ECPoint point10 = array3[9].createPoint(new BigInteger("1A62BA79D98133A16BBAE7ED9A8E03C32E0824D57AEF72F88986874E5AAE49C27BED49A2A95058068426C2171E99FD3B43C5947C857D", 16), new BigInteger("70B5E1E14031C1F70BBEFE96BDDE66F451754B4CA5F48DA241F331AA396B8D1839A855C1769B1EA14BA53308B5E2723724E090E02DB9", 16));
        int n = 0;
        while (true) {
            final ECDomainParameters[] params2 = DSTU4145NamedCurves.params;
            if (n >= params2.length) {
                break;
            }
            params2[n] = new ECDomainParameters(array3[n], (new ECPoint[] { point, point2, point3, point4, point5, point6, point7, point8, point9, point10 })[n], array[n], array2[n]);
            ++n;
        }
        int n2 = 0;
        while (true) {
            final ASN1ObjectIdentifier[] oids2 = DSTU4145NamedCurves.oids;
            if (n2 >= oids2.length) {
                break;
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(DSTU4145NamedCurves.oidBase);
            sb2.append(n2);
            oids2[n2] = new ASN1ObjectIdentifier(sb2.toString());
            ++n2;
        }
    }
    
    public static ECDomainParameters getByOID(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final String id = asn1ObjectIdentifier.getId();
        if (id.startsWith(DSTU4145NamedCurves.oidBase)) {
            return DSTU4145NamedCurves.params[Integer.parseInt(id.substring(id.length() - 1))];
        }
        return null;
    }
    
    public static ASN1ObjectIdentifier[] getOIDs() {
        return DSTU4145NamedCurves.oids;
    }
}
