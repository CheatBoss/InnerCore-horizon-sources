package org.spongycastle.pqc.crypto.xmss;

import org.spongycastle.util.*;

public abstract class XMSSAddress
{
    private final int keyAndMask;
    private final int layerAddress;
    private final long treeAddress;
    private final int type;
    
    protected XMSSAddress(final Builder builder) {
        this.layerAddress = builder.layerAddress;
        this.treeAddress = builder.treeAddress;
        this.type = builder.type;
        this.keyAndMask = builder.keyAndMask;
    }
    
    public final int getKeyAndMask() {
        return this.keyAndMask;
    }
    
    protected final int getLayerAddress() {
        return this.layerAddress;
    }
    
    protected final long getTreeAddress() {
        return this.treeAddress;
    }
    
    public final int getType() {
        return this.type;
    }
    
    protected byte[] toByteArray() {
        final byte[] array = new byte[32];
        Pack.intToBigEndian(this.layerAddress, array, 0);
        Pack.longToBigEndian(this.treeAddress, array, 4);
        Pack.intToBigEndian(this.type, array, 12);
        Pack.intToBigEndian(this.keyAndMask, array, 28);
        return array;
    }
    
    protected abstract static class Builder<T extends Builder>
    {
        private int keyAndMask;
        private int layerAddress;
        private long treeAddress;
        private final int type;
        
        protected Builder(final int type) {
            this.layerAddress = 0;
            this.treeAddress = 0L;
            this.keyAndMask = 0;
            this.type = type;
        }
        
        protected abstract XMSSAddress build();
        
        protected abstract T getThis();
        
        protected T withKeyAndMask(final int keyAndMask) {
            this.keyAndMask = keyAndMask;
            return this.getThis();
        }
        
        protected T withLayerAddress(final int layerAddress) {
            this.layerAddress = layerAddress;
            return this.getThis();
        }
        
        protected T withTreeAddress(final long treeAddress) {
            this.treeAddress = treeAddress;
            return this.getThis();
        }
    }
}
