package org.spongycastle.pqc.crypto.newhope;

class Reduce
{
    static final int QInv = 12287;
    static final int RLog = 18;
    static final int RMask = 262143;
    
    static short barrett(final short n) {
        final int n2 = n & 0xFFFF;
        return (short)(n2 - (n2 * 5 >>> 16) * 12289);
    }
    
    static short montgomery(final int n) {
        return (short)((n * 12287 & 0x3FFFF) * 12289 + n >>> 18);
    }
}
