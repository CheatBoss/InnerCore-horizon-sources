package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.asn1.*;
import org.spongycastle.pqc.crypto.xmss.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.digests.*;
import java.security.*;

public class XMSSKeyPairGeneratorSpi extends KeyPairGenerator
{
    private XMSSKeyPairGenerator engine;
    private boolean initialised;
    private XMSSKeyGenerationParameters param;
    private SecureRandom random;
    private ASN1ObjectIdentifier treeDigest;
    
    public XMSSKeyPairGeneratorSpi() {
        super("XMSS");
        this.engine = new XMSSKeyPairGenerator();
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final XMSSKeyGenerationParameters param = new XMSSKeyGenerationParameters(new XMSSParameters(10, new SHA512Digest()), this.random);
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCXMSSPublicKey(this.treeDigest, (XMSSPublicKeyParameters)generateKeyPair.getPublic()), new BCXMSSPrivateKey(this.treeDigest, (XMSSPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        throw new IllegalArgumentException("use AlgorithmParameterSpec");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof XMSSParameterSpec) {
            final XMSSParameterSpec xmssParameterSpec = (XMSSParameterSpec)algorithmParameterSpec;
            Label_0219: {
                XMSSKeyGenerationParameters param;
                if (xmssParameterSpec.getTreeDigest().equals("SHA256")) {
                    this.treeDigest = NISTObjectIdentifiers.id_sha256;
                    param = new XMSSKeyGenerationParameters(new XMSSParameters(xmssParameterSpec.getHeight(), new SHA256Digest()), secureRandom);
                }
                else if (xmssParameterSpec.getTreeDigest().equals("SHA512")) {
                    this.treeDigest = NISTObjectIdentifiers.id_sha512;
                    param = new XMSSKeyGenerationParameters(new XMSSParameters(xmssParameterSpec.getHeight(), new SHA512Digest()), secureRandom);
                }
                else if (xmssParameterSpec.getTreeDigest().equals("SHAKE128")) {
                    this.treeDigest = NISTObjectIdentifiers.id_shake128;
                    param = new XMSSKeyGenerationParameters(new XMSSParameters(xmssParameterSpec.getHeight(), new SHAKEDigest(128)), secureRandom);
                }
                else {
                    if (!xmssParameterSpec.getTreeDigest().equals("SHAKE256")) {
                        break Label_0219;
                    }
                    this.treeDigest = NISTObjectIdentifiers.id_shake256;
                    param = new XMSSKeyGenerationParameters(new XMSSParameters(xmssParameterSpec.getHeight(), new SHAKEDigest(256)), secureRandom);
                }
                this.param = param;
            }
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a XMSSParameterSpec");
    }
}
