package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.params.*;

public final class XMSSPublicKeyParameters extends AsymmetricKeyParameter implements XMSSStoreableObjectInterface
{
    private final XMSSParameters params;
    private final byte[] publicSeed;
    private final byte[] root;
    
    private XMSSPublicKeyParameters(final Builder builder) {
        super(false);
        final XMSSParameters access$000 = builder.params;
        this.params = access$000;
        if (access$000 != null) {
            final int digestSize = access$000.getDigestSize();
            final byte[] access$2 = builder.publicKey;
            byte[] bytesAtOffset;
            if (access$2 != null) {
                if (access$2.length != digestSize + digestSize) {
                    throw new IllegalArgumentException("public key has wrong size");
                }
                this.root = XMSSUtil.extractBytesAtOffset(access$2, 0, digestSize);
                bytesAtOffset = XMSSUtil.extractBytesAtOffset(access$2, digestSize + 0, digestSize);
            }
            else {
                byte[] access$3 = builder.root;
                if (access$3 != null) {
                    if (access$3.length != digestSize) {
                        throw new IllegalArgumentException("length of root must be equal to length of digest");
                    }
                }
                else {
                    access$3 = new byte[digestSize];
                }
                this.root = access$3;
                final byte[] access$4 = builder.publicSeed;
                if (access$4 != null) {
                    if (access$4.length == digestSize) {
                        this.publicSeed = access$4;
                        return;
                    }
                    throw new IllegalArgumentException("length of publicSeed must be equal to length of digest");
                }
                else {
                    bytesAtOffset = new byte[digestSize];
                }
            }
            this.publicSeed = bytesAtOffset;
            return;
        }
        throw new NullPointerException("params == null");
    }
    
    public XMSSParameters getParameters() {
        return this.params;
    }
    
    public byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }
    
    public byte[] getRoot() {
        return XMSSUtil.cloneArray(this.root);
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.params.getDigestSize();
        final byte[] array = new byte[digestSize + digestSize];
        XMSSUtil.copyBytesAtOffset(array, this.root, 0);
        XMSSUtil.copyBytesAtOffset(array, this.publicSeed, digestSize + 0);
        return array;
    }
    
    public static class Builder
    {
        private final XMSSParameters params;
        private byte[] publicKey;
        private byte[] publicSeed;
        private byte[] root;
        
        public Builder(final XMSSParameters params) {
            this.root = null;
            this.publicSeed = null;
            this.publicKey = null;
            this.params = params;
        }
        
        public XMSSPublicKeyParameters build() {
            return new XMSSPublicKeyParameters(this, null);
        }
        
        public Builder withPublicKey(final byte[] array) {
            this.publicKey = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withPublicSeed(final byte[] array) {
            this.publicSeed = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withRoot(final byte[] array) {
            this.root = XMSSUtil.cloneArray(array);
            return this;
        }
    }
}
