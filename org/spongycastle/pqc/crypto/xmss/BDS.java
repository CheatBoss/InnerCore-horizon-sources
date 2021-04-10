package org.spongycastle.pqc.crypto.xmss;

import java.io.*;
import java.util.*;

public final class BDS implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<XMSSNode> authenticationPath;
    private int index;
    private int k;
    private Map<Integer, XMSSNode> keep;
    private Map<Integer, LinkedList<XMSSNode>> retain;
    private XMSSNode root;
    private Stack<XMSSNode> stack;
    private final List<BDSTreeHash> treeHashInstances;
    private final int treeHeight;
    private boolean used;
    private transient WOTSPlus wotsPlus;
    
    private BDS(final BDS bds, final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        this.wotsPlus = bds.wotsPlus;
        this.treeHeight = bds.treeHeight;
        this.k = bds.k;
        this.root = bds.root;
        this.authenticationPath = new ArrayList<XMSSNode>(bds.authenticationPath);
        this.retain = bds.retain;
        this.stack = (Stack<XMSSNode>)bds.stack.clone();
        this.treeHashInstances = bds.treeHashInstances;
        this.keep = new TreeMap<Integer, XMSSNode>(bds.keep);
        this.index = bds.index;
        this.nextAuthenticationPath(array, array2, otsHashAddress);
        bds.used = true;
    }
    
    private BDS(final WOTSPlus wotsPlus, int i, int k) {
        this.wotsPlus = wotsPlus;
        this.treeHeight = i;
        this.k = k;
        if (k <= i && k >= 2) {
            k = i - k;
            if (k % 2 == 0) {
                this.authenticationPath = new ArrayList<XMSSNode>();
                this.retain = new TreeMap<Integer, LinkedList<XMSSNode>>();
                this.stack = new Stack<XMSSNode>();
                this.treeHashInstances = new ArrayList<BDSTreeHash>();
                for (i = 0; i < k; ++i) {
                    this.treeHashInstances.add(new BDSTreeHash(i));
                }
                this.keep = new TreeMap<Integer, XMSSNode>();
                this.index = 0;
                this.used = false;
                return;
            }
        }
        throw new IllegalArgumentException("illegal value for BDS parameter k");
    }
    
    BDS(final XMSSParameters xmssParameters, final int index) {
        this(xmssParameters.getWOTSPlus(), xmssParameters.getHeight(), xmssParameters.getK());
        this.index = index;
        this.used = true;
    }
    
    BDS(final XMSSParameters xmssParameters, final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        this(xmssParameters.getWOTSPlus(), xmssParameters.getHeight(), xmssParameters.getK());
        this.initialize(array, array2, otsHashAddress);
    }
    
    BDS(final XMSSParameters xmssParameters, final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress, final int n) {
        this(xmssParameters.getWOTSPlus(), xmssParameters.getHeight(), xmssParameters.getK());
        this.initialize(array, array2, otsHashAddress);
        while (this.index < n) {
            this.nextAuthenticationPath(array, array2, otsHashAddress);
            this.used = false;
        }
    }
    
    private BDSTreeHash getBDSTreeHashInstanceForUpdate() {
        final Iterator<BDSTreeHash> iterator = this.treeHashInstances.iterator();
        BDSTreeHash bdsTreeHash = null;
        while (iterator.hasNext()) {
            final BDSTreeHash bdsTreeHash2 = iterator.next();
            if (!bdsTreeHash2.isFinished()) {
                if (!bdsTreeHash2.isInitialized()) {
                    continue;
                }
                if (bdsTreeHash != null) {
                    if (bdsTreeHash2.getHeight() >= bdsTreeHash.getHeight()) {
                        if (bdsTreeHash2.getHeight() != bdsTreeHash.getHeight() || bdsTreeHash2.getIndexLeaf() >= bdsTreeHash.getIndexLeaf()) {
                            continue;
                        }
                    }
                }
                bdsTreeHash = bdsTreeHash2;
            }
        }
        return bdsTreeHash;
    }
    
    private void initialize(final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        if (otsHashAddress != null) {
            LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).build();
            final HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).build();
            int i = 0;
            OTSHashAddress otsHashAddress2 = otsHashAddress;
            HashTreeAddress hashTreeAddress2 = hashTreeAddress;
            while (i < 1 << this.treeHeight) {
                otsHashAddress2 = (OTSHashAddress)((OTSHashAddress.Builder)((OTSHashAddress.Builder)((OTSHashAddress.Builder)new OTSHashAddress.Builder().withLayerAddress(otsHashAddress2.getLayerAddress())).withTreeAddress(otsHashAddress2.getTreeAddress())).withOTSAddress(i).withChainAddress(otsHashAddress2.getChainAddress()).withHashAddress(otsHashAddress2.getHashAddress()).withKeyAndMask(otsHashAddress2.getKeyAndMask())).build();
                final WOTSPlus wotsPlus = this.wotsPlus;
                wotsPlus.importKeys(wotsPlus.getWOTSPlusSecretKey(array2, otsHashAddress2), array);
                final WOTSPlusPublicKeyParameters publicKey = this.wotsPlus.getPublicKey(otsHashAddress2);
                final LTreeAddress lTreeAddress2 = (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(i).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex())).withKeyAndMask(lTreeAddress.getKeyAndMask())).build();
                XMSSNode lTree = XMSSNodeUtil.lTree(this.wotsPlus, publicKey, lTreeAddress2);
                hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeIndex(i).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                while (!this.stack.isEmpty() && this.stack.peek().getHeight() == lTree.getHeight()) {
                    final int n = (int)Math.floor(i / (1 << lTree.getHeight()));
                    if (n == 1) {
                        this.authenticationPath.add(lTree.clone());
                    }
                    if (n == 3 && lTree.getHeight() < this.treeHeight - this.k) {
                        this.treeHashInstances.get(lTree.getHeight()).setNode(lTree.clone());
                    }
                    if (n >= 3 && (n & 0x1) == 0x1 && lTree.getHeight() >= this.treeHeight - this.k && lTree.getHeight() <= this.treeHeight - 2) {
                        if (this.retain.get(lTree.getHeight()) == null) {
                            final LinkedList<XMSSNode> list = new LinkedList<XMSSNode>();
                            list.add(lTree.clone());
                            this.retain.put(lTree.getHeight(), list);
                        }
                        else {
                            this.retain.get(lTree.getHeight()).add(lTree.clone());
                        }
                    }
                    final HashTreeAddress hashTreeAddress3 = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress2.getLayerAddress())).withTreeAddress(hashTreeAddress2.getTreeAddress())).withTreeHeight(hashTreeAddress2.getTreeHeight()).withTreeIndex((hashTreeAddress2.getTreeIndex() - 1) / 2)).withKeyAndMask(hashTreeAddress2.getKeyAndMask())).build();
                    final XMSSNode randomizeHash = XMSSNodeUtil.randomizeHash(this.wotsPlus, this.stack.pop(), lTree, hashTreeAddress3);
                    lTree = new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue());
                    hashTreeAddress2 = (HashTreeAddress)((HashTreeAddress.Builder)((HashTreeAddress.Builder)((HashTreeAddress.Builder)new HashTreeAddress.Builder().withLayerAddress(hashTreeAddress3.getLayerAddress())).withTreeAddress(hashTreeAddress3.getTreeAddress())).withTreeHeight(hashTreeAddress3.getTreeHeight() + 1).withTreeIndex(hashTreeAddress3.getTreeIndex()).withKeyAndMask(hashTreeAddress3.getKeyAndMask())).build();
                }
                this.stack.push(lTree);
                ++i;
                lTreeAddress = lTreeAddress2;
            }
            this.root = this.stack.pop();
            return;
        }
        throw new NullPointerException("otsHashAddress == null");
    }
    
    private void nextAuthenticationPath(final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        if (otsHashAddress == null) {
            throw new NullPointerException("otsHashAddress == null");
        }
        if (this.used) {
            throw new IllegalStateException("index already used");
        }
        if (this.index <= (1 << this.treeHeight) - 2) {
            final LTreeAddress lTreeAddress = (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).build();
            final HashTreeAddress hashTreeAddress = (HashTreeAddress)((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).build();
            final int calculateTau = XMSSUtil.calculateTau(this.index, this.treeHeight);
            if ((this.index >> calculateTau + 1 & 0x1) == 0x0 && calculateTau < this.treeHeight - 1) {
                this.keep.put(calculateTau, this.authenticationPath.get(calculateTau).clone());
            }
            final int n = 0;
            OTSHashAddress otsHashAddress2;
            int i;
            if (calculateTau == 0) {
                otsHashAddress2 = (OTSHashAddress)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((OTSHashAddress.Builder)((XMSSAddress.Builder<OTSHashAddress.Builder>)((XMSSAddress.Builder<OTSHashAddress.Builder>)new OTSHashAddress.Builder()).withLayerAddress(otsHashAddress.getLayerAddress())).withTreeAddress(otsHashAddress.getTreeAddress())).withOTSAddress(this.index).withChainAddress(otsHashAddress.getChainAddress()).withHashAddress(otsHashAddress.getHashAddress())).withKeyAndMask(otsHashAddress.getKeyAndMask())).build();
                final WOTSPlus wotsPlus = this.wotsPlus;
                wotsPlus.importKeys(wotsPlus.getWOTSPlusSecretKey(array2, otsHashAddress2), array);
                this.authenticationPath.set(0, XMSSNodeUtil.lTree(this.wotsPlus, this.wotsPlus.getPublicKey(otsHashAddress2), (LTreeAddress)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((LTreeAddress.Builder)((XMSSAddress.Builder<LTreeAddress.Builder>)((XMSSAddress.Builder<LTreeAddress.Builder>)new LTreeAddress.Builder()).withLayerAddress(lTreeAddress.getLayerAddress())).withTreeAddress(lTreeAddress.getTreeAddress())).withLTreeAddress(this.index).withTreeHeight(lTreeAddress.getTreeHeight()).withTreeIndex(lTreeAddress.getTreeIndex())).withKeyAndMask(lTreeAddress.getKeyAndMask())).build()));
                i = n;
            }
            else {
                final HashTreeAddress.Builder builder = ((XMSSAddress.Builder<HashTreeAddress.Builder>)((XMSSAddress.Builder<HashTreeAddress.Builder>)new HashTreeAddress.Builder()).withLayerAddress(hashTreeAddress.getLayerAddress())).withTreeAddress(hashTreeAddress.getTreeAddress());
                final int n2 = calculateTau - 1;
                final XMSSNode randomizeHash = XMSSNodeUtil.randomizeHash(this.wotsPlus, this.authenticationPath.get(n2), this.keep.get(n2), ((HashTreeAddress.Builder)((XMSSAddress.Builder<HashTreeAddress.Builder>)builder.withTreeHeight(n2).withTreeIndex(this.index >> calculateTau)).withKeyAndMask(hashTreeAddress.getKeyAndMask())).build());
                this.authenticationPath.set(calculateTau, new XMSSNode(randomizeHash.getHeight() + 1, randomizeHash.getValue()));
                this.keep.remove(n2);
                for (int j = 0; j < calculateTau; ++j) {
                    List<XMSSNode> list;
                    XMSSNode xmssNode;
                    if (j < this.treeHeight - this.k) {
                        list = this.authenticationPath;
                        xmssNode = this.treeHashInstances.get(j).getTailNode();
                    }
                    else {
                        list = this.authenticationPath;
                        xmssNode = this.retain.get(j).removeFirst();
                    }
                    list.set(j, xmssNode);
                }
                final int min = Math.min(calculateTau, this.treeHeight - this.k);
                int n3 = 0;
                while (true) {
                    i = n;
                    otsHashAddress2 = otsHashAddress;
                    if (n3 >= min) {
                        break;
                    }
                    final int n4 = this.index + 1 + (1 << n3) * 3;
                    if (n4 < 1 << this.treeHeight) {
                        this.treeHashInstances.get(n3).initialize(n4);
                    }
                    ++n3;
                }
            }
            while (i < this.treeHeight - this.k >> 1) {
                final BDSTreeHash bdsTreeHashInstanceForUpdate = this.getBDSTreeHashInstanceForUpdate();
                if (bdsTreeHashInstanceForUpdate != null) {
                    bdsTreeHashInstanceForUpdate.update(this.stack, this.wotsPlus, array, array2, otsHashAddress2);
                }
                ++i;
            }
            ++this.index;
            return;
        }
        throw new IllegalStateException("index out of bounds");
    }
    
    protected List<XMSSNode> getAuthenticationPath() {
        final ArrayList<XMSSNode> list = new ArrayList<XMSSNode>();
        final Iterator<XMSSNode> iterator = this.authenticationPath.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().clone());
        }
        return list;
    }
    
    protected int getIndex() {
        return this.index;
    }
    
    public BDS getNextState(final byte[] array, final byte[] array2, final OTSHashAddress otsHashAddress) {
        return new BDS(this, array, array2, otsHashAddress);
    }
    
    protected XMSSNode getRoot() {
        return this.root.clone();
    }
    
    protected int getTreeHeight() {
        return this.treeHeight;
    }
    
    boolean isUsed() {
        return this.used;
    }
    
    protected void setXMSS(final XMSSParameters xmssParameters) {
        if (this.treeHeight == xmssParameters.getHeight()) {
            this.wotsPlus = xmssParameters.getWOTSPlus();
            return;
        }
        throw new IllegalStateException("wrong height");
    }
    
    protected void validate() {
        if (this.authenticationPath == null) {
            throw new IllegalStateException("authenticationPath == null");
        }
        if (this.retain == null) {
            throw new IllegalStateException("retain == null");
        }
        if (this.stack == null) {
            throw new IllegalStateException("stack == null");
        }
        if (this.treeHashInstances == null) {
            throw new IllegalStateException("treeHashInstances == null");
        }
        if (this.keep == null) {
            throw new IllegalStateException("keep == null");
        }
        if (XMSSUtil.isIndexValid(this.treeHeight, this.index)) {
            return;
        }
        throw new IllegalStateException("index in BDS state out of bounds");
    }
}
