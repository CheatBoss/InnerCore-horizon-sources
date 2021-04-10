package org.spongycastle.pqc.crypto.xmss;

import java.io.*;
import java.util.*;

class BDSTreeHash implements Serializable
{
    private static final long serialVersionUID = 1L;
    private boolean finished;
    private int height;
    private final int initialHeight;
    private boolean initialized;
    private int nextIndex;
    private XMSSNode tailNode;
    
    BDSTreeHash(final int initialHeight) {
        this.initialHeight = initialHeight;
        this.initialized = false;
        this.finished = false;
    }
    
    int getHeight() {
        if (this.initialized && !this.finished) {
            return this.height;
        }
        return Integer.MAX_VALUE;
    }
    
    int getIndexLeaf() {
        return this.nextIndex;
    }
    
    public XMSSNode getTailNode() {
        return this.tailNode.clone();
    }
    
    void initialize(final int nextIndex) {
        this.tailNode = null;
        this.height = this.initialHeight;
        this.nextIndex = nextIndex;
        this.initialized = true;
        this.finished = false;
    }
    
    boolean isFinished() {
        return this.finished;
    }
    
    boolean isInitialized() {
        return this.initialized;
    }
    
    void setNode(final XMSSNode tailNode) {
        this.tailNode = tailNode;
        final int height = tailNode.getHeight();
        this.height = height;
        if (height == this.initialHeight) {
            this.finished = true;
        }
    }
    
    void update(final Stack<XMSSNode> stack, final WOTSPlus wotsPlus, final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        if (otsHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        if (this.finished || !this.initialized) {
            throw new IllegalStateException("finished or not initialized");
        }
        final OTSHashAddress otsHashAddress2 = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(this.nextIndex).withChainAddress(otsHashAddress.getChainAddress()).withHashAddress(otsHashAddress.getHashAddress())).withKeyAndMask(otsHashAddress.getKeyAndMask())).build();
        final LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(otsHashAddress2.getLayerAddress())).withTreeAddress(otsHashAddress2.getTreeAddress())).withLTreeAddress(this.nextIndex).build();
        final HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(otsHashAddress2.getLayerAddress())).withTreeAddress(otsHashAddress2.getTreeAddress())).withTreeIndex(this.nextIndex).build();
        wotsPlus.importKeys(wotsPlus.getWOTSPlusSecretKey(array2, otsHashAddress2), array);
        XMSSNode lTree = XMSSNodeUtil.lTree(wotsPlus, wotsPlus.getPublicKey(otsHashAddress2), lTreeAddress);
        HashTreeAddress hashTreeAddress2 = hashTreeAddress;
        while (!stack.isEmpty() && stack.peek().getHeight() == lTree.getHeight() && stack.peek().getHeight() != this.initialHeight) {
            final HashTreeAddress hashTreeAddress3 = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight()).withTreeIndex((hashTreeAddress2.getTreeIndex() - 1) / 2)).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
            final XMSSNode randomizeHash = XMSSNodeUtil.randomizeHash(wotsPlus, stack.pop(), lTree, hashTreeAddress3);
            lTree = new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue());
            hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight() + 1).withTreeIndex(hashTreeAddress3.getTreeIndex()).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
        }
        final XMSSNode tailNode = this.tailNode;
        if (tailNode == null) {
            this.tailNode = lTree;
        }
        else if (tailNode.getHeight() == lTree.getHeight()) {
            final HashTreeAddress hashTreeAddress4 = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight()).withTreeIndex((hashTreeAddress2.getTreeIndex() - 1) / 2)).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
            lTree = new XMSSNode(this.tailNode.getHeight() + 1, XMSSNodeUtil.randomizeHash(wotsPlus, this.tailNode, lTree, hashTreeAddress4).getValue());
            this.tailNode = lTree;
            final HashTreeAddress hashTreeAddress5 = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress4.getLayerAddress())).withTreeAddress(hashTreeAddress4.getTreeAddress())).withTreeHeight(hashTreeAddress4.getTreeHeight() + 1).withTreeIndex(hashTreeAddress4.getTreeIndex())).withKeyAndMask(hashTreeAddress4.getKeyAndMask())).build();
        }
        else {
            stack.push(lTree);
        }
        if (this.tailNode.getHeight() == this.initialHeight) {
            this.finished = true;
            return;
        }
        this.height = lTree.getHeight();
        ++this.nextIndex;
    }
}
