package org.spongycastle.pqc.jcajce.provider.sphincs;

import org.spongycastle.asn1.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.pqc.crypto.sphincs.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.crypto.digests.*;
import java.security.*;

public class Sphincs256KeyPairGeneratorSpi extends KeyPairGenerator
{
    SPHINCS256KeyPairGenerator engine;
    boolean initialised;
    SPHINCS256KeyGenerationParameters param;
    SecureRandom random;
    ASN1ObjectIdentifier treeDigest;
    
    public Sphincs256KeyPairGeneratorSpi() {
        super("SPHINCS256");
        this.treeDigest = NISTObjectIdentifiers.id_sha512_256;
        this.engine = new SPHINCS256KeyPairGenerator();
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final SPHINCS256KeyGenerationParameters param = new SPHINCS256KeyGenerationParameters(this.random, new SHA512tDigest(256));
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCSphincs256PublicKey(this.treeDigest, (SPHINCSPublicKeyParameters)generateKeyPair.getPublic()), new BCSphincs256PrivateKey(this.treeDigest, (SPHINCSPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        throw new IllegalArgumentException("use AlgorithmParameterSpec");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof SPHINCS256KeyGenParameterSpec) {
            final SPHINCS256KeyGenParameterSpec sphincs256KeyGenParameterSpec = (SPHINCS256KeyGenParameterSpec)algorithmParameterSpec;
            Label_0099: {
                SPHINCS256KeyGenerationParameters param;
                if (sphincs256KeyGenParameterSpec.getTreeDigest().equals("SHA512-256")) {
                    this.treeDigest = NISTObjectIdentifiers.id_sha512_256;
                    param = new SPHINCS256KeyGenerationParameters(secureRandom, new SHA512tDigest(256));
                }
                else {
                    if (!sphincs256KeyGenParameterSpec.getTreeDigest().equals("SHA3-256")) {
                        break Label_0099;
                    }
                    this.treeDigest = NISTObjectIdentifiers.id_sha3_256;
                    param = new SPHINCS256KeyGenerationParameters(secureRandom, new SHA3Digest(256));
                }
                this.param = param;
            }
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a SPHINCS256KeyGenParameterSpec");
    }
}
