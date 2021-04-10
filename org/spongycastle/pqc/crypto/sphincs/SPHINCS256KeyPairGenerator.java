package org.spongycastle.pqc.crypto.sphincs;

import java.security.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class SPHINCS256KeyPairGenerator implements AsymmetricCipherKeyPairGenerator
{
    private SecureRandom random;
    private Digest treeDigest;
    
    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final Tree.leafaddr leafaddr = new Tree.leafaddr();
        final byte[] array = new byte[1088];
        this.random.nextBytes(array);
        final byte[] array2 = new byte[1056];
        System.arraycopy(array, 32, array2, 0, 1024);
        leafaddr.level = 11;
        leafaddr.subtree = 0L;
        leafaddr.subleaf = 0L;
        Tree.treehash(new HashFunctions(this.treeDigest), array2, 1024, 5, array, leafaddr, array2, 0);
        return new AsymmetricCipherKeyPair(new SPHINCSPublicKeyParameters(array2), new SPHINCSPrivateKeyParameters(array));
    }
    
    @Override
    public void init(final KeyGenerationParameters keyGenerationParameters) {
        this.random = keyGenerationParameters.getRandom();
        this.treeDigest = ((SPHINCS256KeyGenerationParameters)keyGenerationParameters).getTreeDigest();
    }
}
