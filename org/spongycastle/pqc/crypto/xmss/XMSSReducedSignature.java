package org.spongycastle.pqc.crypto.xmss;

import java.util.*;
import java.lang.reflect.*;

public class XMSSReducedSignature implements XMSSStoreableObjectInterface
{
    private final List<XMSSNode> authPath;
    private final XMSSParameters params;
    private final WOTSPlusSignature wotsPlusSignature;
    
    protected XMSSReducedSignature(final Builder builder) {
        final XMSSParameters access$000 = builder.params;
        this.params = access$000;
        if (access$000 == null) {
            throw new NullPointerException("params == null");
        }
        final int digestSize = access$000.getDigestSize();
        final int len = this.params.getWOTSPlus().getParams().getLen();
        final int height = this.params.getHeight();
        final byte[] access$2 = builder.reducedSignature;
        final int n = 0;
        if (access$2 != null) {
            if (access$2.length == len * digestSize + height * digestSize) {
                final byte[][] array = new byte[len][];
                int i = 0;
                int n2 = 0;
                while (i < len) {
                    array[i] = XMSSUtil.extractBytesAtOffset(access$2, n2, digestSize);
                    n2 += digestSize;
                    ++i;
                }
                this.wotsPlusSignature = new WOTSPlusSignature(this.params.getWOTSPlus().getParams(), array);
                final ArrayList<XMSSNode> authPath = new ArrayList<XMSSNode>();
                int n3 = n2;
                for (int j = n; j < height; ++j) {
                    authPath.add(new XMSSNode(j, XMSSUtil.extractBytesAtOffset(access$2, n3, digestSize)));
                    n3 += digestSize;
                }
                this.authPath = authPath;
                return;
            }
            throw new IllegalArgumentException("signature has wrong size");
        }
        else {
            WOTSPlusSignature access$3 = builder.wotsPlusSignature;
            if (access$3 == null) {
                access$3 = new WOTSPlusSignature(this.params.getWOTSPlus().getParams(), (byte[][])Array.newInstance(Byte.TYPE, len, digestSize));
            }
            this.wotsPlusSignature = access$3;
            final List access$4 = builder.authPath;
            if (access$4 == null) {
                this.authPath = new ArrayList<XMSSNode>();
                return;
            }
            if (access$4.size() == height) {
                this.authPath = (List<XMSSNode>)access$4;
                return;
            }
            throw new IllegalArgumentException("size of authPath needs to be equal to height of tree");
        }
    }
    
    public List<XMSSNode> getAuthPath() {
        return this.authPath;
    }
    
    public XMSSParameters getParams() {
        return this.params;
    }
    
    public WOTSPlusSignature getWOTSPlusSignature() {
        return this.wotsPlusSignature;
    }
    
    @Override
    public byte[] toByteArray() {
        final int digestSize = this.params.getDigestSize();
        final byte[] array = new byte[this.params.getWOTSPlus().getParams().getLen() * digestSize + this.params.getHeight() * digestSize];
        final byte[][] byteArray = this.wotsPlusSignature.toByteArray();
        final int n = 0;
        int n2 = 0;
        int n3 = 0;
        int i;
        int n4;
        while (true) {
            i = n;
            n4 = n2;
            if (n3 >= byteArray.length) {
                break;
            }
            XMSSUtil.copyBytesAtOffset(array, byteArray[n3], n2);
            n2 += digestSize;
            ++n3;
        }
        while (i < this.authPath.size()) {
            XMSSUtil.copyBytesAtOffset(array, this.authPath.get(i).getValue(), n4);
            n4 += digestSize;
            ++i;
        }
        return array;
    }
    
    public static class Builder
    {
        private List<XMSSNode> authPath;
        private final XMSSParameters params;
        private byte[] reducedSignature;
        private WOTSPlusSignature wotsPlusSignature;
        
        public Builder(final XMSSParameters params) {
            this.wotsPlusSignature = null;
            this.authPath = null;
            this.reducedSignature = null;
            this.params = params;
        }
        
        public XMSSReducedSignature build() {
            return new XMSSReducedSignature(this);
        }
        
        public Builder withAuthPath(final List<XMSSNode> authPath) {
            this.authPath = authPath;
            return this;
        }
        
        public Builder withReducedSignature(final byte[] array) {
            this.reducedSignature = XMSSUtil.cloneArray(array);
            return this;
        }
        
        public Builder withWOTSPlusSignature(final WOTSPlusSignature wotsPlusSignature) {
            this.wotsPlusSignature = wotsPlusSignature;
            return this;
        }
    }
}
