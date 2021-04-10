package org.spongycastle.pqc.crypto.sphincs;

import org.spongycastle.crypto.*;
import java.security.*;

public class SPHINCS256KeyGenerationParameters extends KeyGenerationParameters
{
    private final Digest treeDigest;
    
    public SPHINCS256KeyGenerationParameters(final SecureRandom secureRandom, final Digest treeDigest) {
        super(secureRandom, 8448);
        this.treeDigest = treeDigest;
    }
    
    public Digest getTreeDigest() {
        return this.treeDigest;
    }
}
