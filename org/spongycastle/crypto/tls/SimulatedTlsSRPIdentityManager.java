package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.agreement.srp.*;
import org.spongycastle.util.*;
import org.spongycastle.crypto.macs.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class SimulatedTlsSRPIdentityManager implements TlsSRPIdentityManager
{
    private static final byte[] PREFIX_PASSWORD;
    private static final byte[] PREFIX_SALT;
    protected SRP6GroupParameters group;
    protected Mac mac;
    protected SRP6VerifierGenerator verifierGenerator;
    
    static {
        PREFIX_PASSWORD = Strings.toByteArray("password");
        PREFIX_SALT = Strings.toByteArray("salt");
    }
    
    public SimulatedTlsSRPIdentityManager(final SRP6GroupParameters group, final SRP6VerifierGenerator verifierGenerator, final Mac mac) {
        this.group = group;
        this.verifierGenerator = verifierGenerator;
        this.mac = mac;
    }
    
    public static SimulatedTlsSRPIdentityManager getRFC5054Default(final SRP6GroupParameters srp6GroupParameters, final byte[] array) {
        final SRP6VerifierGenerator srp6VerifierGenerator = new SRP6VerifierGenerator();
        srp6VerifierGenerator.init(srp6GroupParameters, TlsUtils.createHash((short)2));
        final HMac hMac = new HMac(TlsUtils.createHash((short)2));
        hMac.init(new KeyParameter(array));
        return new SimulatedTlsSRPIdentityManager(srp6GroupParameters, srp6VerifierGenerator, hMac);
    }
    
    @Override
    public TlsSRPLoginParameters getLoginParameters(final byte[] array) {
        final Mac mac = this.mac;
        final byte[] prefix_SALT = SimulatedTlsSRPIdentityManager.PREFIX_SALT;
        mac.update(prefix_SALT, 0, prefix_SALT.length);
        this.mac.update(array, 0, array.length);
        final byte[] array2 = new byte[this.mac.getMacSize()];
        this.mac.doFinal(array2, 0);
        final Mac mac2 = this.mac;
        final byte[] prefix_PASSWORD = SimulatedTlsSRPIdentityManager.PREFIX_PASSWORD;
        mac2.update(prefix_PASSWORD, 0, prefix_PASSWORD.length);
        this.mac.update(array, 0, array.length);
        final byte[] array3 = new byte[this.mac.getMacSize()];
        this.mac.doFinal(array3, 0);
        return new TlsSRPLoginParameters(this.group, this.verifierGenerator.generateVerifier(array2, array, array3), array2);
    }
}
