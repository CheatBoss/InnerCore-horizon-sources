package org.spongycastle.jcajce.provider.symmetric.util;

import javax.crypto.spec.*;
import java.security.spec.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.crypto.params.*;
import java.security.*;
import javax.crypto.*;
import org.spongycastle.crypto.*;

public class BaseStreamCipher extends BaseWrapCipher implements PBE
{
    private Class[] availableSpecs;
    private StreamCipher cipher;
    private int digest;
    private int ivLength;
    private ParametersWithIV ivParam;
    private int keySizeInBits;
    private String pbeAlgorithm;
    private PBEParameterSpec pbeSpec;
    
    protected BaseStreamCipher(final StreamCipher streamCipher, final int n) {
        this(streamCipher, n, -1, -1);
    }
    
    protected BaseStreamCipher(final StreamCipher cipher, final int ivLength, final int keySizeInBits, final int digest) {
        this.availableSpecs = new Class[] { RC2ParameterSpec.class, RC5ParameterSpec.class, IvParameterSpec.class, PBEParameterSpec.class };
        this.ivLength = 0;
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.cipher = cipher;
        this.ivLength = ivLength;
        this.keySizeInBits = keySizeInBits;
        this.digest = digest;
    }
    
    @Override
    protected int engineDoFinal(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        if (n3 + n2 <= array2.length) {
            if (n2 != 0) {
                this.cipher.processBytes(array, n, n2, array2, n3);
            }
            this.cipher.reset();
            return n2;
        }
        throw new ShortBufferException("output buffer too short for input.");
    }
    
    @Override
    protected byte[] engineDoFinal(byte[] engineUpdate, final int n, final int n2) {
        if (n2 != 0) {
            engineUpdate = this.engineUpdate(engineUpdate, n, n2);
            this.cipher.reset();
            return engineUpdate;
        }
        this.cipher.reset();
        return new byte[0];
    }
    
    @Override
    protected int engineGetBlockSize() {
        return 0;
    }
    
    @Override
    protected byte[] engineGetIV() {
        final ParametersWithIV ivParam = this.ivParam;
        if (ivParam != null) {
            return ivParam.getIV();
        }
        return null;
    }
    
    @Override
    protected int engineGetKeySize(final Key key) {
        return key.getEncoded().length * 8;
    }
    
    @Override
    protected int engineGetOutputSize(final int n) {
        return n;
    }
    
    @Override
    protected AlgorithmParameters engineGetParameters() {
        if (this.engineParams == null && this.pbeSpec != null) {
            try {
                final AlgorithmParameters parametersInstance = this.createParametersInstance(this.pbeAlgorithm);
                parametersInstance.init(this.pbeSpec);
                return parametersInstance;
            }
            catch (Exception ex) {
                return null;
            }
        }
        return this.engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameters engineParams, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        AlgorithmParameterSpec parameterSpec = null;
        final AlgorithmParameterSpec algorithmParameterSpec = null;
        if (engineParams != null) {
            int n2 = 0;
            while (true) {
                final Class[] availableSpecs = this.availableSpecs;
                parameterSpec = algorithmParameterSpec;
                if (n2 != availableSpecs.length) {
                    try {
                        parameterSpec = engineParams.getParameterSpec((Class<AlgorithmParameterSpec>)availableSpecs[n2]);
                    }
                    catch (Exception ex) {
                        ++n2;
                        continue;
                    }
                    break;
                }
                break;
            }
            if (parameterSpec == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("can't handle parameter ");
                sb.append(engineParams.toString());
                throw new InvalidAlgorithmParameterException(sb.toString());
            }
        }
        this.engineInit(n, key, parameterSpec, secureRandom);
        this.engineParams = engineParams;
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        try {
            this.engineInit(n, key, (AlgorithmParameterSpec)null, secureRandom);
        }
        catch (InvalidAlgorithmParameterException ex) {
            throw new InvalidKeyException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineInit(final int n, final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        this.pbeSpec = null;
        this.pbeAlgorithm = null;
        this.engineParams = null;
        if (key instanceof SecretKey) {
            CipherParameters pbeParameters = null;
            Label_0351: {
                if (key instanceof PKCS12Key) {
                    final PKCS12Key pkcs12Key = (PKCS12Key)key;
                    final PBEParameterSpec pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                    this.pbeSpec = pbeSpec;
                    if (pkcs12Key instanceof PKCS12KeyWithParameters && pbeSpec == null) {
                        final PKCS12KeyWithParameters pkcs12KeyWithParameters = (PKCS12KeyWithParameters)pkcs12Key;
                        this.pbeSpec = new PBEParameterSpec(pkcs12KeyWithParameters.getSalt(), pkcs12KeyWithParameters.getIterationCount());
                    }
                    pbeParameters = Util.makePBEParameters(pkcs12Key.getEncoded(), 2, this.digest, this.keySizeInBits, this.ivLength * 8, this.pbeSpec, this.cipher.getAlgorithmName());
                }
                else {
                    CipherParameters cipherParameters2;
                    if (key instanceof BCPBEKey) {
                        final BCPBEKey bcpbeKey = (BCPBEKey)key;
                        String pbeAlgorithm;
                        if (bcpbeKey.getOID() != null) {
                            pbeAlgorithm = bcpbeKey.getOID().getId();
                        }
                        else {
                            pbeAlgorithm = bcpbeKey.getAlgorithm();
                        }
                        this.pbeAlgorithm = pbeAlgorithm;
                        CipherParameters cipherParameters;
                        if (bcpbeKey.getParam() != null) {
                            cipherParameters = bcpbeKey.getParam();
                            this.pbeSpec = new PBEParameterSpec(bcpbeKey.getSalt(), bcpbeKey.getIterationCount());
                        }
                        else {
                            if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                                throw new InvalidAlgorithmParameterException("PBE requires PBE parameters to be set.");
                            }
                            cipherParameters = Util.makePBEParameters(bcpbeKey, algorithmParameterSpec, this.cipher.getAlgorithmName());
                            this.pbeSpec = (PBEParameterSpec)algorithmParameterSpec;
                        }
                        cipherParameters2 = cipherParameters;
                        if (bcpbeKey.getIvSize() != 0) {
                            this.ivParam = (ParametersWithIV)cipherParameters;
                            cipherParameters2 = cipherParameters;
                        }
                    }
                    else if (algorithmParameterSpec == null) {
                        if (this.digest > 0) {
                            throw new InvalidKeyException("Algorithm requires a PBE key");
                        }
                        cipherParameters2 = new KeyParameter(key.getEncoded());
                    }
                    else {
                        if (algorithmParameterSpec instanceof IvParameterSpec) {
                            pbeParameters = new ParametersWithIV(new KeyParameter(key.getEncoded()), ((IvParameterSpec)algorithmParameterSpec).getIV());
                            this.ivParam = (ParametersWithIV)pbeParameters;
                            break Label_0351;
                        }
                        throw new InvalidAlgorithmParameterException("unknown parameter type.");
                    }
                    pbeParameters = cipherParameters2;
                }
            }
            ParametersWithIV parametersWithIV = (ParametersWithIV)pbeParameters;
            if (this.ivLength != 0) {
                parametersWithIV = (ParametersWithIV)pbeParameters;
                if (!(pbeParameters instanceof ParametersWithIV)) {
                    SecureRandom secureRandom2;
                    if ((secureRandom2 = secureRandom) == null) {
                        secureRandom2 = new SecureRandom();
                    }
                    if (n != 1 && n != 3) {
                        throw new InvalidAlgorithmParameterException("no IV set when one expected");
                    }
                    final byte[] array = new byte[this.ivLength];
                    secureRandom2.nextBytes(array);
                    parametersWithIV = new ParametersWithIV(pbeParameters, array);
                    this.ivParam = parametersWithIV;
                }
            }
            Label_0516: {
                if (n == 1) {
                    break Label_0516;
                }
                Label_0504: {
                    if (n == 2) {
                        break Label_0504;
                    }
                    if (n == 3) {
                        break Label_0516;
                    }
                    if (n == 4) {
                        break Label_0504;
                    }
                    try {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown opmode ");
                        sb.append(n);
                        sb.append(" passed");
                        throw new InvalidParameterException(sb.toString());
                        this.cipher.init(false, parametersWithIV);
                        return;
                        this.cipher.init(true, parametersWithIV);
                        return;
                    }
                    catch (Exception ex) {
                        throw new InvalidKeyException(ex.getMessage());
                    }
                }
            }
            throw new InvalidAlgorithmParameterException("unknown parameter type.");
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Key for algorithm ");
        sb2.append(key.getAlgorithm());
        sb2.append(" not suitable for symmetric enryption.");
        throw new InvalidKeyException(sb2.toString());
    }
    
    @Override
    protected void engineSetMode(final String s) throws NoSuchAlgorithmException {
        if (s.equalsIgnoreCase("ECB")) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("can't support mode ");
        sb.append(s);
        throw new NoSuchAlgorithmException(sb.toString());
    }
    
    @Override
    protected void engineSetPadding(final String s) throws NoSuchPaddingException {
        if (s.equalsIgnoreCase("NoPadding")) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Padding ");
        sb.append(s);
        sb.append(" unknown.");
        throw new NoSuchPaddingException(sb.toString());
    }
    
    @Override
    protected int engineUpdate(final byte[] array, final int n, final int n2, final byte[] array2, final int n3) throws ShortBufferException {
        if (n3 + n2 <= array2.length) {
            try {
                this.cipher.processBytes(array, n, n2, array2, n3);
                return n2;
            }
            catch (DataLengthException ex) {
                throw new IllegalStateException(ex.getMessage());
            }
        }
        throw new ShortBufferException("output buffer too short for input.");
    }
    
    @Override
    protected byte[] engineUpdate(final byte[] array, final int n, final int n2) {
        final byte[] array2 = new byte[n2];
        this.cipher.processBytes(array, n, n2, array2, 0);
        return array2;
    }
}
