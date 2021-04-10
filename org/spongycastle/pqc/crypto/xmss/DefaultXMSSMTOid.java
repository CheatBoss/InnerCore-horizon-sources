package org.spongycastle.pqc.crypto.xmss;

import java.util.*;

public final class DefaultXMSSMTOid implements XMSSOid
{
    private static final Map<String, DefaultXMSSMTOid> oidLookupTable;
    private final int oid;
    private final String stringRepresentation;
    
    static {
        final HashMap<String, DefaultXMSSMTOid> hashMap = new HashMap<String, DefaultXMSSMTOid>();
        hashMap.put(createKey("SHA-256", 32, 16, 67, 20, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H20_D2"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 20, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H20_D4"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H40_D2"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H40_D4"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 40, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H40_D8"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 8), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H60_D3"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 6), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H60_D6"));
        hashMap.put(createKey("SHA-256", 32, 16, 67, 60, 12), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-256_W16_H60_D12"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 20, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H20_D2"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 20, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H20_D4"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 40, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H40_D2"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 40, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H40_D4"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 40, 8), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H40_D8"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 60, 3), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H60_D3"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 60, 6), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H60_D6"));
        hashMap.put(createKey("SHA2-512", 64, 16, 131, 60, 12), new DefaultXMSSMTOid(16777217, "XMSSMT_SHA2-512_W16_H60_D12"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 20, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H20_D2"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 20, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H20_D4"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H40_D2"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H40_D4"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 40, 8), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H40_D8"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 3), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H60_D3"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 6), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H60_D6"));
        hashMap.put(createKey("SHAKE128", 32, 16, 67, 60, 12), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE128_W16_H60_D12"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 20, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H20_D2"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 20, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H20_D4"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 2), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H40_D2"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 4), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H40_D4"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 40, 8), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H40_D8"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 3), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H60_D3"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 6), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H60_D6"));
        hashMap.put(createKey("SHAKE256", 64, 16, 131, 60, 12), new DefaultXMSSMTOid(16777217, "XMSSMT_SHAKE256_W16_H60_D12"));
        oidLookupTable = Collections.unmodifiableMap((Map<?, ?>)hashMap);
    }
    
    private DefaultXMSSMTOid(final int oid, final String stringRepresentation) {
        this.oid = oid;
        this.stringRepresentation = stringRepresentation;
    }
    
    private static String createKey(final String s, final int n, final int n2, final int n3, final int n4, final int n5) {
        if (s != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("-");
            sb.append(n);
            sb.append("-");
            sb.append(n2);
            sb.append("-");
            sb.append(n3);
            sb.append("-");
            sb.append(n4);
            sb.append("-");
            sb.append(n5);
            return sb.toString();
        }
        throw new NullPointerException("algorithmName == null");
    }
    
    public static DefaultXMSSMTOid lookup(final String s, final int n, final int n2, final int n3, final int n4, final int n5) {
        if (s != null) {
            return DefaultXMSSMTOid.oidLookupTable.get(createKey(s, n, n2, n3, n4, n5));
        }
        throw new NullPointerException("algorithmName == null");
    }
    
    @Override
    public int getOid() {
        return this.oid;
    }
    
    @Override
    public String toString() {
        return this.stringRepresentation;
    }
}
