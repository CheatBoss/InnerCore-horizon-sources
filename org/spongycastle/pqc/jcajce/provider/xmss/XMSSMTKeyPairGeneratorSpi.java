package org.spongycastle.pqc.jcajce.provider.xmss;

import org.spongycastle.asn1.*;
import org.spongycastle.pqc.crypto.xmss.*;
import org.spongycastle.crypto.*;
import java.security.spec.*;
import org.spongycastle.pqc.jcajce.spec.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.crypto.digests.*;
import java.security.*;

public class XMSSMTKeyPairGeneratorSpi extends KeyPairGenerator
{
    private XMSSMTKeyPairGenerator engine;
    private boolean initialised;
    private XMSSMTKeyGenerationParameters param;
    private SecureRandom random;
    private ASN1ObjectIdentifier treeDigest;
    
    public XMSSMTKeyPairGeneratorSpi() {
        super("XMSSMT");
        this.engine = new XMSSMTKeyPairGenerator();
        this.random = new SecureRandom();
        this.initialised = false;
    }
    
    @Override
    public KeyPair generateKeyPair() {
        if (!this.initialised) {
            final XMSSMTKeyGenerationParameters param = new XMSSMTKeyGenerationParameters(new XMSSMTParameters(10, 20, new SHA512Digest()), this.random);
            this.param = param;
            this.engine.init(param);
            this.initialised = true;
        }
        final AsymmetricCipherKeyPair generateKeyPair = this.engine.generateKeyPair();
        return new KeyPair(new BCXMSSMTPublicKey(this.treeDigest, (XMSSMTPublicKeyParameters)generateKeyPair.getPublic()), new BCXMSSMTPrivateKey(this.treeDigest, (XMSSMTPrivateKeyParameters)generateKeyPair.getPrivate()));
    }
    
    @Override
    public void initialize(final int n, final SecureRandom secureRandom) {
        throw new IllegalArgumentException("use AlgorithmParameterSpec");
    }
    
    @Override
    public void initialize(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof XMSSMTParameterSpec) {
            final XMSSMTParameterSpec xmssmtParameterSpec = (XMSSMTParameterSpec)algorithmParameterSpec;
            Label_0235: {
                XMSSMTKeyGenerationParameters param;
                if (xmssmtParameterSpec.getTreeDigest().equals("SHA256")) {
                    this.treeDigest = NISTObjectIdentifiers.id_sha256;
                    param = new XMSSMTKeyGenerationParameters(new XMSSMTParameters(xmssmtParameterSpec.getHeight(), xmssmtParameterSpec.getLayers(), new SHA256Digest()), secureRandom);
                }
                else if (xmssmtParameterSpec.getTreeDigest().equals("SHA512")) {
                    this.treeDigest = NISTObjectIdentifiers.id_sha512;
                    param = new XMSSMTKeyGenerationParameters(new XMSSMTParameters(xmssmtParameterSpec.getHeight(), xmssmtParameterSpec.getLayers(), new SHA512Digest()), secureRandom);
                }
                else if (xmssmtParameterSpec.getTreeDigest().equals("SHAKE128")) {
                    this.treeDigest = NISTObjectIdentifiers.id_shake128;
                    param = new XMSSMTKeyGenerationParameters(new XMSSMTParameters(xmssmtParameterSpec.getHeight(), xmssmtParameterSpec.getLayers(), new SHAKEDigest(128)), secureRandom);
                }
                else {
                    if (!xmssmtParameterSpec.getTreeDigest().equals("SHAKE256")) {
                        break Label_0235;
                    }
                    this.treeDigest = NISTObjectIdentifiers.id_shake256;
                    param = new XMSSMTKeyGenerationParameters(new XMSSMTParameters(xmssmtParameterSpec.getHeight(), xmssmtParameterSpec.getLayers(), new SHAKEDigest(256)), secureRandom);
                }
                this.param = param;
            }
            this.engine.init(this.param);
            this.initialised = true;
            return;
        }
        throw new InvalidAlgorithmParameterException("parameter object not a XMSSMTParameterSpec");
    }
}
