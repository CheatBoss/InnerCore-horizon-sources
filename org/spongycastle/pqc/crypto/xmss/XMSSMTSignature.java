package org.spongycastle.pqc.crypto.xmss;

import java.util.*;

public final class XMSSMTSignature implements XMSSStoreableObjectInterface
{
    private final long index;
    private final XMSSMTParameters params;
    private final byte[] random;
    private final List<XMSSReducedSignature> reducedSignatures;
    
    private XMSSMTSignature(final Builder builder) {
        final XMSSMTParameters access$000 = builder.params;
        this.params = access$000;
        if (access$000 != null) {
            final int digestSize = access$000.getDigestSize();
            final byte[] access$2 = builder.signature;
            if (access$2 != null) {
                final int len = this.params.getWOTSPlus().getParams().getLen();
                final double n = this.params.getHeight();
                Double.isNaN(n);
                final int n2 = (int)Math.ceil(n / 8.0);
                final int n3 = (this.params.getHeight() / this.params.getLayers() + len) * digestSize;
                if (access$2.length != n2 + digestSize + this.params.getLayers() * n3) {
                    throw new IllegalArgumentException("signature has wrong size");
                }
                this.index = XMSSUtil.bytesToXBigEndian(access$2, 0, n2);
                if (!XMSSUtil.isIndexValid(this.params.getHeight(), this.index)) {
                    throw new IllegalArgumentException("index out of bounds");
                }
                final int n4 = n2 + 0;
                this.random = XMSSUtil.extractBytesAtOffset(access$2, n4, digestSize);
                int i = n4 + digestSize;
                this.reducedSignatures = new ArrayList<XMSSReducedSignature>();
                while (i < access$2.length) {
                    this.reducedSignatures.add(new XMSSReducedSignature.Builder(this.params.getXMSSParameters()).withReducedSignature(XMSSUtil.extractBytesAtOffset(access$2, i, n3)).build());
                    i += n3;
                }
            }
            else {
                this.index = builder.index;
                final byte[] access$3 = builder.random;
                if (access$3 != null) {
                    if (access$3.length != digestSize) {
                        throw new IllegalArgumentException("size of random needs to be equal to size of digest");
                    }
                    this.random = access$3;
                }
                else {
                    this.random = new byte[digestSize];
                }
                final List access$4 = builder.reducedSignatures;
                if (access$4 != null) {
                    this.reducedSignatures = (List<XMSSReducedSignature>)access$4;
                    return;
                }
                this.reducedSignatures = new ArrayList<XMSSReducedSignature>();
            }
            return;
        }
        throw new NullPointerException("params == null");
    }
    
    public long getIndex() {
        return this.index;
    }
    
    public byte[] getRandom() {
        return XMSSUtil.cloneArray(this.random);
    }
    
    public List<XMSSReducedSignature> getReducedSignatures() {
        return this.reducedSignatures;
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.params.getDigestSize();
        final int len = this.params.getWOTSPlus().getParams().getLen();
        final double n = this.params.getHeight();
        Double.isNaN(n);
        final int n2 = (int)Math.ceil(n / 8.0);
        final int n3 = (this.params.getHeight() / this.params.getLayers() + len) * digestSize;
        final byte[] array = new byte[n2 + digestSize + this.params.getLayers() * n3];
        XMSSUtil.copyBytesAtOffset(array, XMSSUtil.toBytesBigEndian(this.index, n2), 0);
        final int n4 = n2 + 0;
        XMSSUtil.copyBytesAtOffset(array, this.random, n4);
        int n5 = n4 + digestSize;
        final Iterator<XMSSReducedSignature> iterator = this.reducedSignatures.iterator();
        while (iterator.hasNext()) {
            XMSSUtil.copyBytesAtOffset(array, iterator.next().toByteArray(), n5);
            n5 += n3;
        }
        return array;
    }
    
    public static class Builder
    {
        private long index;
        private final XMSSMTParameters params;
        private byte[] random;
        private List<XMSSReducedSignature> reducedSignatures;
        private byte[] signature;
        
        public Builder(final XMSSMTParameters params) {
            this.index = 0L;
            this.random = null;
            this.reducedSignatures = null;
            this.signature = null;
            this.params = params;
        }
        
        public XMSSMTSignature build() {
            return new XMSSMTSignature(this, null);
        }
        
        public Builder withIndex(final long index) {
            this.index = index;
            return this;
        }
        
        public Builder withRandom(final byte[] array) {
            this.random = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withReducedSignatures(final List<XMSSReducedSignature> reducedSignatures) {
            this.reducedSignatures = reducedSignatures;
            return this;
        }
        
        public Builder withSignature(final byte[] signature) {
            this.signature = signature;
            return this;
        }
    }
}
