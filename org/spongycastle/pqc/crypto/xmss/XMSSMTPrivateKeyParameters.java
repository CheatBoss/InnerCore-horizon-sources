package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.util.*;

public final class XMSSMTPrivateKeyParameters extends AsymmetricKeyParameter implements XMSSStoreableObjectInterface
{
    private final BDSStateMap bdsState;
    private final long index;
    private final XMSSMTParameters params;
    private final byte[] publicSeed;
    private final byte[] root;
    private final byte[] secretKeyPRF;
    private final byte[] secretKeySeed;
    
    private XMSSMTPrivateKeyParameters(final Builder builder) {
        super(true);
        final XMSSMTParameters access$000 = builder.params;
        this.params = access$000;
        if (access$000 == null) {
            throw new NullPointerException("params == null");
        }
        final int digestSize = access$000.getDigestSize();
        final byte[] access$2 = builder.privateKey;
        if (access$2 != null) {
            if (builder.xmss == null) {
                throw new NullPointerException("xmss == null");
            }
            final int height = this.params.getHeight();
            final int n = (height + 7) / 8;
            final long bytesToXBigEndian = XMSSUtil.bytesToXBigEndian(access$2, 0, n);
            this.index = bytesToXBigEndian;
            if (XMSSUtil.isIndexValid(height, bytesToXBigEndian)) {
                final int n2 = n + 0;
                this.secretKeySeed = XMSSUtil.extractBytesAtOffset(access$2, n2, digestSize);
                final int n3 = n2 + digestSize;
                this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(access$2, n3, digestSize);
                final int n4 = n3 + digestSize;
                this.publicSeed = XMSSUtil.extractBytesAtOffset(access$2, n4, digestSize);
                final int n5 = n4 + digestSize;
                this.root = XMSSUtil.extractBytesAtOffset(access$2, n5, digestSize);
                final int n6 = n5 + digestSize;
                final byte[] bytesAtOffset = XMSSUtil.extractBytesAtOffset(access$2, n6, access$2.length - n6);
                BDSStateMap bdsState = null;
                Label_0199: {
                    try {
                        bdsState = (BDSStateMap)XMSSUtil.deserialize(bytesAtOffset);
                        break Label_0199;
                    }
                    catch (ClassNotFoundException ex) {
                        ex.printStackTrace();
                    }
                    catch (IOException ex2) {
                        ex2.printStackTrace();
                    }
                    bdsState = null;
                }
                bdsState.setXMSS(builder.xmss);
                this.bdsState = bdsState;
                return;
            }
            throw new IllegalArgumentException("index out of bounds");
        }
        else {
            this.index = builder.index;
            final byte[] access$3 = builder.secretKeySeed;
            if (access$3 != null) {
                if (access$3.length != digestSize) {
                    throw new IllegalArgumentException("size of secretKeySeed needs to be equal size of digest");
                }
                this.secretKeySeed = access$3;
            }
            else {
                this.secretKeySeed = new byte[digestSize];
            }
            byte[] access$4 = builder.secretKeyPRF;
            if (access$4 != null) {
                if (access$4.length != digestSize) {
                    throw new IllegalArgumentException("size of secretKeyPRF needs to be equal size of digest");
                }
            }
            else {
                access$4 = new byte[digestSize];
            }
            this.secretKeyPRF = access$4;
            final byte[] access$5 = builder.publicSeed;
            if (access$5 != null) {
                if (access$5.length != digestSize) {
                    throw new IllegalArgumentException("size of publicSeed needs to be equal size of digest");
                }
                this.publicSeed = access$5;
            }
            else {
                this.publicSeed = new byte[digestSize];
            }
            final byte[] access$6 = builder.root;
            if (access$6 != null) {
                if (access$6.length != digestSize) {
                    throw new IllegalArgumentException("size of root needs to be equal size of digest");
                }
                this.root = access$6;
            }
            else {
                this.root = new byte[digestSize];
            }
            final BDSStateMap access$7 = builder.bdsState;
            if (access$7 != null) {
                this.bdsState = access$7;
                return;
            }
            if (XMSSUtil.isIndexValid(this.params.getHeight(), builder.index) && access$5 != null && access$3 != null) {
                this.bdsState = new BDSStateMap(this.params, builder.index, access$5, access$3);
                return;
            }
            this.bdsState = new BDSStateMap();
        }
    }
    
    BDSStateMap getBDSState() {
        return this.bdsState;
    }
    
    public long getIndex() {
        return this.index;
    }
    
    public XMSSMTPrivateKeyParameters getNextKey() {
        return new Builder(this.params).withIndex(this.index + 1L).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root).withBDSState(new BDSStateMap(this.bdsState, this.params, this.getIndex(), this.publicSeed, this.secretKeySeed)).build();
    }
    
    public XMSSMTParameters getParameters() {
        return this.params;
    }
    
    public byte[] getPublicSeed() {
        return XMSSUtil.cloneArray(this.publicSeed);
    }
    
    public byte[] getRoot() {
        return XMSSUtil.cloneArray(this.root);
    }
    
    public byte[] getSecretKeyPRF() {
        return XMSSUtil.cloneArray(this.secretKeyPRF);
    }
    
    public byte[] getSecretKeySeed() {
        return XMSSUtil.cloneArray(this.secretKeySeed);
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.params.getDigestSize();
        final int n = (this.params.getHeight() + 7) / 8;
        final byte[] array = new byte[n + digestSize + digestSize + digestSize + digestSize];
        XMSSUtil.copyBytesAtOffset(array, XMSSUtil.toBytesBigEndian(this.index, n), 0);
        final int n2 = n + 0;
        XMSSUtil.copyBytesAtOffset(array, this.secretKeySeed, n2);
        final int n3 = n2 + digestSize;
        XMSSUtil.copyBytesAtOffset(array, this.secretKeyPRF, n3);
        final int n4 = n3 + digestSize;
        XMSSUtil.copyBytesAtOffset(array, this.publicSeed, n4);
        XMSSUtil.copyBytesAtOffset(array, this.root, n4 + digestSize);
        try {
            return Arrays.concatenate(array, XMSSUtil.serialize(this.bdsState));
        }
        catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("error serializing bds state");
        }
    }
    
    public static class Builder
    {
        private BDSStateMap bdsState;
        private long index;
        private final XMSSMTParameters params;
        private byte[] privateKey;
        private byte[] publicSeed;
        private byte[] root;
        private byte[] secretKeyPRF;
        private byte[] secretKeySeed;
        private XMSSParameters xmss;
        
        public Builder(final XMSSMTParameters params) {
            this.index = 0L;
            this.secretKeySeed = null;
            this.secretKeyPRF = null;
            this.publicSeed = null;
            this.root = null;
            this.bdsState = null;
            this.privateKey = null;
            this.xmss = null;
            this.params = params;
        }
        
        public XMSSMTPrivateKeyParameters build() {
            return new XMSSMTPrivateKeyParameters(this, null);
        }
        
        public Builder withBDSState(final BDSStateMap bdsState) {
            this.bdsState = bdsState;
            return this;
        }
        
        public Builder withIndex(final long index) {
            this.index = index;
            return this;
        }
        
        public Builder withPrivateKey(final byte[] array, final XMSSParameters xmss) {
            this.privateKey = XMSSUtil.cloneArray(array);
            this.xmss = xmss;
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
        
        public Builder withSecretKeyPRF(final byte[] array) {
            this.secretKeyPRF = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withSecretKeySeed(final byte[] array) {
            this.secretKeySeed = XMSSUtil.cloneArray(array);
            return this;
        }
    }
}
