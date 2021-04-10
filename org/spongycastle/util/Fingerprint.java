package org.spongycastle.util;

import org.spongycastle.crypto.digests.*;

public class Fingerprint
{
    private static char[] encodingTable;
    private final byte[] fingerprint;
    
    static {
        Fingerprint.encodingTable = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    }
    
    public Fingerprint(final byte[] array) {
        this.fingerprint = calculateFingerprint(array);
    }
    
    public static byte[] calculateFingerprint(byte[] array) {
        final SHA512tDigest sha512tDigest = new SHA512tDigest(160);
        sha512tDigest.update(array, 0, array.length);
        array = new byte[sha512tDigest.getDigestSize()];
        sha512tDigest.doFinal(array, 0);
        return array;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof Fingerprint && Arrays.areEqual(((Fingerprint)o).fingerprint, this.fingerprint));
    }
    
    public byte[] getFingerprint() {
        return Arrays.clone(this.fingerprint);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.fingerprint);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i != this.fingerprint.length; ++i) {
            if (i > 0) {
                sb.append(":");
            }
            sb.append(Fingerprint.encodingTable[this.fingerprint[i] >>> 4 & 0xF]);
            sb.append(Fingerprint.encodingTable[this.fingerprint[i] & 0xF]);
        }
        return sb.toString();
    }
}
