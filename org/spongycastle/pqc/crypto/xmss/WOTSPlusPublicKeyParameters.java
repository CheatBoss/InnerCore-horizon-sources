package org.spongycastle.pqc.crypto.xmss;

final class WOTSPlusPublicKeyParameters
{
    private final byte[][] publicKey;
    
    protected WOTSPlusPublicKeyParameters(final WOTSPlusParameters wotsPlusParameters, final byte[][] array) {
        if (wotsPlusParameters == null) {
            throw new NullPointerException("params == null");
        }
        if (array == null) {
            throw new NullPointerException("publicKey == null");
        }
        if (XMSSUtil.hasNullPointer(array)) {
            throw new NullPointerException("publicKey byte array == null");
        }
        if (array.length == wotsPlusParameters.getLen()) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i].length != wotsPlusParameters.getDigestSize()) {
                    throw new IllegalArgumentException("wrong publicKey format");
                }
            }
            this.publicKey = XMSSUtil.cloneArray(array);
            return;
        }
        throw new IllegalArgumentException("wrong publicKey size");
    }
    
    protected byte[][] toByteArray() {
        return XMSSUtil.cloneArray(this.publicKey);
    }
}
