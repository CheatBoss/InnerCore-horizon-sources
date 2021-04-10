package org.spongycastle.pqc.jcajce.provider.newhope;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.pqc.crypto.newhope.*;
import org.spongycastle.pqc.crypto.*;
import org.spongycastle.util.*;
import javax.crypto.*;
import java.security.spec.*;
import java.security.*;

public class KeyAgreementSpi extends BaseAgreementSpi
{
    private NHAgreement agreement;
    private NHExchangePairGenerator exchangePairGenerator;
    private BCNHPublicKey otherPartyKey;
    private byte[] shared;
    
    public KeyAgreementSpi() {
        super("NH", null);
    }
    
    @Override
    protected byte[] calcSecret() {
        return this.engineGenerateSecret();
    }
    
    @Override
    protected Key engineDoPhase(final Key key, final boolean b) throws InvalidKeyException, IllegalStateException {
        if (!b) {
            throw new IllegalStateException("NewHope can only be between two parties.");
        }
        final BCNHPublicKey otherPartyKey = (BCNHPublicKey)key;
        this.otherPartyKey = otherPartyKey;
        final NHExchangePairGenerator exchangePairGenerator = this.exchangePairGenerator;
        if (exchangePairGenerator != null) {
            final ExchangePair generateExchange = exchangePairGenerator.generateExchange((AsymmetricKeyParameter)otherPartyKey.getKeyParams());
            this.shared = generateExchange.getSharedValue();
            return new BCNHPublicKey((NHPublicKeyParameters)generateExchange.getPublicKey());
        }
        this.shared = this.agreement.calculateAgreement(otherPartyKey.getKeyParams());
        return null;
    }
    
    @Override
    protected int engineGenerateSecret(final byte[] array, final int n) throws IllegalStateException, ShortBufferException {
        final byte[] shared = this.shared;
        System.arraycopy(shared, 0, array, n, shared.length);
        Arrays.fill(this.shared, (byte)0);
        return this.shared.length;
    }
    
    @Override
    protected byte[] engineGenerateSecret() throws IllegalStateException {
        final byte[] clone = Arrays.clone(this.shared);
        Arrays.fill(this.shared, (byte)0);
        return clone;
    }
    
    @Override
    protected void engineInit(final Key key, final SecureRandom secureRandom) throws InvalidKeyException {
        if (key != null) {
            (this.agreement = new NHAgreement()).init(((BCNHPrivateKey)key).getKeyParams());
            return;
        }
        this.exchangePairGenerator = new NHExchangePairGenerator(secureRandom);
    }
    
    @Override
    protected void engineInit(final Key key, final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("NewHope does not require parameters");
    }
}
