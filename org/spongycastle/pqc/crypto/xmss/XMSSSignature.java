package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.util.*;

public final class XMSSSignature extends XMSSReducedSignature implements XMSSStoreableObjectInterface
{
    private final int index;
    private final byte[] random;
    
    private XMSSSignature(final Builder builder) {
        super((XMSSReducedSignature.Builder)builder);
        this.index = builder.index;
        final int digestSize = this.getParams().getDigestSize();
        final byte[] access$100 = builder.random;
        if (access$100 == null) {
            this.random = new byte[digestSize];
            return;
        }
        if (access$100.length == digestSize) {
            this.random = access$100;
            return;
        }
        throw new IllegalArgumentException("size of random needs to be equal to size of digest");
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.getParams().getDigestSize();
        int n = digestSize + 4;
        final byte[] array = new byte[this.getParams().getWOTSPlus().getParams().getLen() * digestSize + n + this.getParams().getHeight() * digestSize];
        final int index = this.index;
        final int n2 = 0;
        Pack.intToBigEndian(index, array, 0);
        XMSSUtil.copyBytesAtOffset(array, this.random, 4);
        final byte[][] byteArray = this.getWOTSPlusSignature().toByteArray();
        int n3 = 0;
        int n4;
        int i;
        while (true) {
            n4 = n;
            i = n2;
            if (n3 >= byteArray.length) {
                break;
            }
            XMSSUtil.copyBytesAtOffset(array, byteArray[n3], n);
            n += digestSize;
            ++n3;
        }
        while (i < this.getAuthPath().size()) {
            XMSSUtil.copyBytesAtOffset(array, this.getAuthPath().get(i).getValue(), n4);
            n4 += digestSize;
            ++i;
        }
        return array;
    }
    
    public static class Builder extends XMSSReducedSignature.Builder
    {
        private int index;
        private final XMSSParameters params;
        private byte[] random;
        
        public Builder(final XMSSParameters params) {
            super(params);
            this.index = 0;
            this.random = null;
            this.params = params;
        }
        
        public XMSSSignature build() {
            return new XMSSSignature(this, null);
        }
        
        public Builder withIndex(final int index) {
            this.index = index;
            return this;
        }
        
        public Builder withRandom(final byte[] array) {
            this.random = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withSignature(final byte[] array) {
            if (array != null) {
                final int digestSize = this.params.getDigestSize();
                final int len = this.params.getWOTSPlus().getParams().getLen();
                final int height = this.params.getHeight();
                this.index = Pack.bigEndianToInt(array, 0);
                this.random = XMSSUtil.extractBytesAtOffset(array, 4, digestSize);
                ((XMSSReducedSignature.Builder)this).withReducedSignature(XMSSUtil.extractBytesAtOffset(array, digestSize + 4, len * digestSize + height * digestSize));
                return this;
            }
            throw new NullPointerException("signature == null");
        }
    }
}
