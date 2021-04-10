package org.spongycastle.pqc.crypto.xmss;

import java.io.*;

public final class XMSSNode implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final int height;
    private final byte[] value;
    
    protected XMSSNode(final int height, final byte[] value) {
        this.height = height;
        this.value = value;
    }
    
    @Override
    protected XMSSNode clone() {
        return new XMSSNode(this.getHeight(), this.getValue());
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public byte[] getValue() {
        return XMSSUtil.cloneArray(this.value);
    }
}
