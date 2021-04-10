package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.util.*;

public final class XMSSPrivateKeyParameters extends AsymmetricKeyParameter implements XMSSStoreableObjectInterface
{
    private final BDS bdsState;
    private final XMSSParameters params;
    private final byte[] publicSeed;
    private final byte[] root;
    private final byte[] secretKeyPRF;
    private final byte[] secretKeySeed;
    
    private XMSSPrivateKeyParameters(final Builder builder) {
        super(true);
        final XMSSParameters access$000 = builder.params;
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
            final int bigEndianToInt = Pack.bigEndianToInt(access$2, 0);
            if (!XMSSUtil.isIndexValid(height, bigEndianToInt)) {
                throw new IllegalArgumentException("index out of bounds");
            }
            this.secretKeySeed = XMSSUtil.extractBytesAtOffset(access$2, 4, digestSize);
            final int n = digestSize + 4;
            this.secretKeyPRF = XMSSUtil.extractBytesAtOffset(access$2, n, digestSize);
            final int n2 = n + digestSize;
            this.publicSeed = XMSSUtil.extractBytesAtOffset(access$2, n2, digestSize);
            final int n3 = n2 + digestSize;
            this.root = XMSSUtil.extractBytesAtOffset(access$2, n3, digestSize);
            final int n4 = n3 + digestSize;
            final byte[] bytesAtOffset = XMSSUtil.extractBytesAtOffset(access$2, n4, access$2.length - n4);
            BDS bdsState = null;
            Label_0187: {
                try {
                    bdsState = (BDS)XMSSUtil.deserialize(bytesAtOffset);
                    break Label_0187;
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
            bdsState.validate();
            if (bdsState.getIndex() == bigEndianToInt) {
                this.bdsState = bdsState;
                return;
            }
            throw new IllegalStateException("serialized BDS has wrong index");
        }
        else {
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
            final BDS access$7 = builder.bdsState;
            if (access$7 != null) {
                this.bdsState = access$7;
                return;
            }
            BDS bdsState2;
            if (builder.index < (1 << this.params.getHeight()) - 2 && access$5 != null && access$3 != null) {
                bdsState2 = new BDS(this.params, access$5, access$3, (OTSHashAddress)new OTSHashAddress.Builder().build(), builder.index);
            }
            else {
                bdsState2 = new BDS(this.params, builder.index);
            }
            this.bdsState = bdsState2;
        }
    }
    
    BDS getBDSState() {
        return this.bdsState;
    }
    
    public int getIndex() {
        return this.bdsState.getIndex();
    }
    
    public XMSSPrivateKeyParameters getNextKey() {
        Builder builder;
        BDS nextState;
        if (this.getIndex() < (1 << this.params.getHeight()) - 1) {
            builder = new Builder(this.params).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root);
            nextState = this.bdsState.getNextState(this.publicSeed, this.secretKeySeed, (OTSHashAddress)new OTSHashAddress.Builder().build());
        }
        else {
            builder = new Builder(this.params).withSecretKeySeed(this.secretKeySeed).withSecretKeyPRF(this.secretKeyPRF).withPublicSeed(this.publicSeed).withRoot(this.root);
            nextState = new BDS(this.params, this.getIndex() + 1);
        }
        return builder.withBDSState(nextState).build();
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
    
    public byte[] getSecretKeyPRF() {
        return XMSSUtil.cloneArray(this.secretKeyPRF);
    }
    
    public byte[] getSecretKeySeed() {
        return XMSSUtil.cloneArray(this.secretKeySeed);
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.params.getDigestSize();
        final int n = digestSize + 4;
        final int n2 = n + digestSize;
        final int n3 = n2 + digestSize;
        final byte[] array = new byte[digestSize + n3];
        Pack.intToBigEndian(this.bdsState.getIndex(), array, 0);
        XMSSUtil.copyBytesAtOffset(array, this.secretKeySeed, 4);
        XMSSUtil.copyBytesAtOffset(array, this.secretKeyPRF, n);
        XMSSUtil.copyBytesAtOffset(array, this.publicSeed, n2);
        XMSSUtil.copyBytesAtOffset(array, this.root, n3);
        try {
            return Arrays.concatenate(array, XMSSUtil.serialize(this.bdsState));
        }
        catch (IOException ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("error serializing bds state: ");
            sb.append(ex.getMessage());
            throw new RuntimeException(sb.toString());
        }
    }
    
    public static class Builder
    {
        private BDS bdsState;
        private int index;
        private final XMSSParameters params;
        private byte[] privateKey;
        private byte[] publicSeed;
        private byte[] root;
        private byte[] secretKeyPRF;
        private byte[] secretKeySeed;
        private XMSSParameters xmss;
        
        public Builder(final XMSSParameters params) {
            this.index = 0;
            this.secretKeySeed = null;
            this.secretKeyPRF = null;
            this.publicSeed = null;
            this.root = null;
            this.bdsState = null;
            this.privateKey = null;
            this.xmss = null;
            this.params = params;
        }
        
        public XMSSPrivateKeyParameters build() {
            return new XMSSPrivateKeyParameters(this, null);
        }
        
        public Builder withBDSState(final BDS bdsState) {
            this.bdsState = bdsState;
            return this;
        }
        
        public Builder withIndex(final int index) {
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
