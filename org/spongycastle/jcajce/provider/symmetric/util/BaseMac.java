package org.spongycastle.jcajce.provider.symmetric.util;

import java.util.*;
import java.security.spec.*;
import org.spongycastle.jcajce.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import org.spongycastle.crypto.macs.*;
import java.security.*;
import org.spongycastle.crypto.*;
import javax.crypto.spec.*;
import org.spongycastle.jcajce.spec.*;
import org.spongycastle.crypto.params.*;

public class BaseMac extends MacSpi implements PBE
{
    private static final Class gcmSpecClass;
    private int keySize;
    private Mac macEngine;
    private int pbeHash;
    private int scheme;
    
    static {
        gcmSpecClass = ClassUtil.loadClass(BaseMac.class, "javax.crypto.spec.GCMParameterSpec");
    }
    
    protected BaseMac(final Mac macEngine) {
        this.scheme = 2;
        this.pbeHash = 1;
        this.keySize = 160;
        this.macEngine = macEngine;
    }
    
    protected BaseMac(final Mac macEngine, final int scheme, final int pbeHash, final int keySize) {
        this.scheme = 2;
        this.pbeHash = 1;
        this.keySize = 160;
        this.macEngine = macEngine;
        this.scheme = scheme;
        this.pbeHash = pbeHash;
        this.keySize = keySize;
    }
    
    private static Hashtable copyMap(final Map map) {
        final Hashtable<Object, Object> hashtable = new Hashtable<Object, Object>();
        for (final Object next : map.keySet()) {
            hashtable.put(next, map.get(next));
        }
        return hashtable;
    }
    
    @Override
    protected byte[] engineDoFinal() {
        final byte[] array = new byte[this.engineGetMacLength()];
        this.macEngine.doFinal(array, 0);
        return array;
    }
    
    @Override
    protected int engineGetMacLength() {
        return this.macEngine.getMacSize();
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (key != null) {
            if (key instanceof PKCS12Key) {
                try {
                    final SecretKey secretKey = (SecretKey)key;
                    try {
                        PBEParameterSpec pbeParameterSpec = (PBEParameterSpec)algorithmParameterSpec;
                        if (secretKey instanceof PBEKey && (pbeParameterSpec = pbeParameterSpec) == null) {
                            final PBEKey pbeKey = (PBEKey)secretKey;
                            pbeParameterSpec = new PBEParameterSpec(pbeKey.getSalt(), pbeKey.getIterationCount());
                        }
                        final int n = 1;
                        final boolean startsWith = this.macEngine.getAlgorithmName().startsWith("GOST");
                        int n2 = 256;
                        int n3 = 0;
                        Label_0318: {
                            if (startsWith) {
                                n3 = 6;
                            }
                            else {
                                final Mac macEngine = this.macEngine;
                                n3 = n;
                                if (macEngine instanceof HMac) {
                                    n3 = n;
                                    if (!macEngine.getAlgorithmName().startsWith("SHA-1")) {
                                        if (this.macEngine.getAlgorithmName().startsWith("SHA-224")) {
                                            n3 = 7;
                                            n2 = 224;
                                            break Label_0318;
                                        }
                                        if (this.macEngine.getAlgorithmName().startsWith("SHA-256")) {
                                            n3 = 4;
                                            break Label_0318;
                                        }
                                        if (this.macEngine.getAlgorithmName().startsWith("SHA-384")) {
                                            n3 = 8;
                                            n2 = 384;
                                            break Label_0318;
                                        }
                                        if (this.macEngine.getAlgorithmName().startsWith("SHA-512")) {
                                            n3 = 9;
                                            n2 = 512;
                                            break Label_0318;
                                        }
                                        if (!this.macEngine.getAlgorithmName().startsWith("RIPEMD160")) {
                                            final StringBuilder sb = new StringBuilder();
                                            sb.append("no PKCS12 mapping for HMAC: ");
                                            sb.append(this.macEngine.getAlgorithmName());
                                            throw new InvalidAlgorithmParameterException(sb.toString());
                                        }
                                        n3 = 2;
                                    }
                                }
                                n2 = 160;
                            }
                        }
                        final CipherParameters cipherParameters = Util.makePBEMacParameters(secretKey, 2, n3, n2, pbeParameterSpec);
                    }
                    catch (Exception ex2) {
                        throw new InvalidAlgorithmParameterException("PKCS12 requires a PBEParameterSpec");
                    }
                }
                catch (Exception ex3) {
                    throw new InvalidKeyException("PKCS12 requires a SecretKey/PBEKey");
                }
            }
            CipherParameters cipherParameters;
            if (key instanceof BCPBEKey) {
                final BCPBEKey bcpbeKey = (BCPBEKey)key;
                if (bcpbeKey.getParam() != null) {
                    cipherParameters = bcpbeKey.getParam();
                }
                else {
                    if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                        throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                    }
                    cipherParameters = Util.makePBEMacParameters(bcpbeKey, algorithmParameterSpec);
                }
            }
            else {
                if (algorithmParameterSpec instanceof PBEParameterSpec) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("inappropriate parameter type: ");
                    sb2.append(algorithmParameterSpec.getClass().getName());
                    throw new InvalidAlgorithmParameterException(sb2.toString());
                }
                cipherParameters = new KeyParameter(key.getEncoded());
            }
            KeyParameter keyParameter;
            if (cipherParameters instanceof ParametersWithIV) {
                keyParameter = (KeyParameter)((ParametersWithIV)cipherParameters).getParameters();
            }
            else {
                keyParameter = (KeyParameter)cipherParameters;
            }
            Label_0748: {
                CipherParameters build;
                if (algorithmParameterSpec instanceof AEADParameterSpec) {
                    final AEADParameterSpec aeadParameterSpec = (AEADParameterSpec)algorithmParameterSpec;
                    build = new AEADParameters(keyParameter, aeadParameterSpec.getMacSizeInBits(), aeadParameterSpec.getNonce(), aeadParameterSpec.getAssociatedData());
                }
                else if (algorithmParameterSpec instanceof IvParameterSpec) {
                    build = new ParametersWithIV(keyParameter, ((IvParameterSpec)algorithmParameterSpec).getIV());
                }
                else if (algorithmParameterSpec instanceof RC2ParameterSpec) {
                    final byte[] key2 = keyParameter.getKey();
                    final RC2ParameterSpec rc2ParameterSpec = (RC2ParameterSpec)algorithmParameterSpec;
                    build = new ParametersWithIV(new RC2Parameters(key2, rc2ParameterSpec.getEffectiveKeyBits()), rc2ParameterSpec.getIV());
                }
                else if (algorithmParameterSpec instanceof SkeinParameterSpec) {
                    build = new SkeinParameters.Builder(copyMap(((SkeinParameterSpec)algorithmParameterSpec).getParameters())).setKey(keyParameter.getKey()).build();
                }
                else if (algorithmParameterSpec == null) {
                    build = new KeyParameter(key.getEncoded());
                }
                else {
                    final Class gcmSpecClass = BaseMac.gcmSpecClass;
                    if (gcmSpecClass != null && gcmSpecClass.isAssignableFrom(algorithmParameterSpec.getClass())) {
                        try {
                            build = new AEADParameters(keyParameter, (int)BaseMac.gcmSpecClass.getDeclaredMethod("getTLen", (Class[])new Class[0]).invoke(algorithmParameterSpec, new Object[0]), (byte[])BaseMac.gcmSpecClass.getDeclaredMethod("getIV", (Class[])new Class[0]).invoke(algorithmParameterSpec, new Object[0]));
                            break Label_0748;
                        }
                        catch (Exception ex4) {
                            throw new InvalidAlgorithmParameterException("Cannot process GCMParameterSpec.");
                        }
                    }
                    if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                        break Label_0748;
                    }
                    build = cipherParameters;
                }
                try {
                    this.macEngine.init(build);
                    return;
                }
                catch (Exception ex) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("cannot initialize MAC: ");
                    sb3.append(ex.getMessage());
                    throw new InvalidAlgorithmParameterException(sb3.toString());
                }
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("unknown parameter type: ");
            sb4.append(algorithmParameterSpec.getClass().getName());
            throw new InvalidAlgorithmParameterException(sb4.toString());
        }
        throw new InvalidKeyException("key is null");
    }
    
    @Override
    protected void engineReset() {
        this.macEngine.reset();
    }
    
    @Override
    protected void engineUpdate(final byte b) {
        this.macEngine.update(b);
    }
    
    @Override
    protected void engineUpdate(final byte[] array, final int n, final int n2) {
        this.macEngine.update(array, n, n2);
    }
}
