package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.modes.*;
import java.io.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.engines.*;

public class DefaultTlsCipherFactory extends AbstractTlsCipherFactory
{
    protected AEADBlockCipher createAEADBlockCipher_AES_CCM() {
        return new CCMBlockCipher(this.createAESEngine());
    }
    
    protected AEADBlockCipher createAEADBlockCipher_AES_GCM() {
        return new GCMBlockCipher(this.createAESEngine());
    }
    
    protected AEADBlockCipher createAEADBlockCipher_AES_OCB() {
        return new OCBBlockCipher(this.createAESEngine(), this.createAESEngine());
    }
    
    protected AEADBlockCipher createAEADBlockCipher_Camellia_GCM() {
        return new GCMBlockCipher(this.createCamelliaEngine());
    }
    
    protected BlockCipher createAESBlockCipher() {
        return new CBCBlockCipher(this.createAESEngine());
    }
    
    protected TlsBlockCipher createAESCipher(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsBlockCipher(tlsContext, this.createAESBlockCipher(), this.createAESBlockCipher(), this.createHMACDigest(n2), this.createHMACDigest(n2), n);
    }
    
    protected BlockCipher createAESEngine() {
        return new AESEngine();
    }
    
    protected BlockCipher createCamelliaBlockCipher() {
        return new CBCBlockCipher(this.createCamelliaEngine());
    }
    
    protected TlsBlockCipher createCamelliaCipher(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsBlockCipher(tlsContext, this.createCamelliaBlockCipher(), this.createCamelliaBlockCipher(), this.createHMACDigest(n2), this.createHMACDigest(n2), n);
    }
    
    protected BlockCipher createCamelliaEngine() {
        return new CamelliaEngine();
    }
    
    protected TlsCipher createChaCha20Poly1305(final TlsContext tlsContext) throws IOException {
        return new Chacha20Poly1305(tlsContext);
    }
    
    @Override
    public TlsCipher createCipher(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        if (n == 0) {
            return this.createNullCipher(tlsContext, n2);
        }
        if (n == 2) {
            return this.createRC4Cipher(tlsContext, 16, n2);
        }
        if (n == 103) {
            return this.createCipher_AES_OCB(tlsContext, 16, 12);
        }
        if (n == 104) {
            return this.createCipher_AES_OCB(tlsContext, 32, 12);
        }
        switch (n) {
            default: {
                throw new TlsFatalAlert((short)80);
            }
            case 21: {
                return this.createChaCha20Poly1305(tlsContext);
            }
            case 20: {
                return this.createCipher_Camellia_GCM(tlsContext, 32, 16);
            }
            case 19: {
                return this.createCipher_Camellia_GCM(tlsContext, 16, 16);
            }
            case 18: {
                return this.createCipher_AES_CCM(tlsContext, 32, 8);
            }
            case 17: {
                return this.createCipher_AES_CCM(tlsContext, 32, 16);
            }
            case 16: {
                return this.createCipher_AES_CCM(tlsContext, 16, 8);
            }
            case 15: {
                return this.createCipher_AES_CCM(tlsContext, 16, 16);
            }
            case 14: {
                return this.createSEEDCipher(tlsContext, n2);
            }
            case 13: {
                return this.createCamelliaCipher(tlsContext, 32, n2);
            }
            case 12: {
                return this.createCamelliaCipher(tlsContext, 16, n2);
            }
            case 11: {
                return this.createCipher_AES_GCM(tlsContext, 32, 16);
            }
            case 10: {
                return this.createCipher_AES_GCM(tlsContext, 16, 16);
            }
            case 9: {
                return this.createAESCipher(tlsContext, 32, n2);
            }
            case 8: {
                return this.createAESCipher(tlsContext, 16, n2);
            }
            case 7: {
                return this.createDESedeCipher(tlsContext, n2);
            }
        }
    }
    
    protected TlsAEADCipher createCipher_AES_CCM(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_AES_CCM(), this.createAEADBlockCipher_AES_CCM(), n, n2);
    }
    
    protected TlsAEADCipher createCipher_AES_GCM(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_AES_GCM(), this.createAEADBlockCipher_AES_GCM(), n, n2);
    }
    
    protected TlsAEADCipher createCipher_AES_OCB(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_AES_OCB(), this.createAEADBlockCipher_AES_OCB(), n, n2, 2);
    }
    
    protected TlsAEADCipher createCipher_Camellia_GCM(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsAEADCipher(tlsContext, this.createAEADBlockCipher_Camellia_GCM(), this.createAEADBlockCipher_Camellia_GCM(), n, n2);
    }
    
    protected BlockCipher createDESedeBlockCipher() {
        return new CBCBlockCipher(new DESedeEngine());
    }
    
    protected TlsBlockCipher createDESedeCipher(final TlsContext tlsContext, final int n) throws IOException {
        return new TlsBlockCipher(tlsContext, this.createDESedeBlockCipher(), this.createDESedeBlockCipher(), this.createHMACDigest(n), this.createHMACDigest(n), 24);
    }
    
    protected Digest createHMACDigest(final int n) throws IOException {
        if (n == 0) {
            return null;
        }
        if (n == 1) {
            return TlsUtils.createHash((short)1);
        }
        if (n == 2) {
            return TlsUtils.createHash((short)2);
        }
        if (n == 3) {
            return TlsUtils.createHash((short)4);
        }
        if (n == 4) {
            return TlsUtils.createHash((short)5);
        }
        if (n == 5) {
            return TlsUtils.createHash((short)6);
        }
        throw new TlsFatalAlert((short)80);
    }
    
    protected TlsNullCipher createNullCipher(final TlsContext tlsContext, final int n) throws IOException {
        return new TlsNullCipher(tlsContext, this.createHMACDigest(n), this.createHMACDigest(n));
    }
    
    protected TlsStreamCipher createRC4Cipher(final TlsContext tlsContext, final int n, final int n2) throws IOException {
        return new TlsStreamCipher(tlsContext, this.createRC4StreamCipher(), this.createRC4StreamCipher(), this.createHMACDigest(n2), this.createHMACDigest(n2), n, false);
    }
    
    protected StreamCipher createRC4StreamCipher() {
        return new RC4Engine();
    }
    
    protected BlockCipher createSEEDBlockCipher() {
        return new CBCBlockCipher(new SEEDEngine());
    }
    
    protected TlsBlockCipher createSEEDCipher(final TlsContext tlsContext, final int n) throws IOException {
        return new TlsBlockCipher(tlsContext, this.createSEEDBlockCipher(), this.createSEEDBlockCipher(), this.createHMACDigest(n), this.createHMACDigest(n), 16);
    }
}
