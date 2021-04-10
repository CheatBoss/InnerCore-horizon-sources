package org.spongycastle.pqc.crypto.xmss;

final class WOTSPlusPrivateKeyParameters
{
    private final byte[][] privateKey;
    
    protected WOTSPlusPrivateKeyParameters(final WOTSPlusParameters wotsPlusParameters, final byte[][] array) {
        if (wotsPlusParameters == null) {
            throw new NullPointerException("params == null");
        }
        if (array == null) {
            throw new NullPointerException("privateKey == null");
        }
        if (XMSSUtil.hasNullPointer(array)) {
            throw new NullPointerException("privateKey byte array == null");
        }
        if (array.length == wotsPlusParameters.getLen()) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i].length != wotsPlusParameters.getDigestSize()) {
                    throw new IllegalArgumentException("wrong privateKey format");
                }
            }
            this.privateKey = XMSSUtil.cloneArray(array);
            return;
        }
        throw new IllegalArgumentException("wrong privateKey format");
    }
    
    protected byte[][] toByteArray() {
        return XMSSUtil.cloneArray(this.privateKey);
    }
}
