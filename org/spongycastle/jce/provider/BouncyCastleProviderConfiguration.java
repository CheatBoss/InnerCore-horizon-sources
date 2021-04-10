package org.spongycastle.jce.provider;

import java.security.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.jcajce.provider.config.*;
import java.util.*;
import javax.crypto.spec.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;

class BouncyCastleProviderConfiguration implements ProviderConfiguration
{
    private static Permission BC_ADDITIONAL_EC_CURVE_PERMISSION;
    private static Permission BC_DH_LOCAL_PERMISSION;
    private static Permission BC_DH_PERMISSION;
    private static Permission BC_EC_CURVE_PERMISSION;
    private static Permission BC_EC_LOCAL_PERMISSION;
    private static Permission BC_EC_PERMISSION;
    private volatile Set acceptableNamedCurves;
    private volatile Map additionalECParameters;
    private volatile Object dhDefaultParams;
    private ThreadLocal dhThreadSpec;
    private volatile ECParameterSpec ecImplicitCaParams;
    private ThreadLocal ecThreadSpec;
    
    static {
        BouncyCastleProviderConfiguration.BC_EC_LOCAL_PERMISSION = new ProviderConfigurationPermission("SC", "threadLocalEcImplicitlyCa");
        BouncyCastleProviderConfiguration.BC_EC_PERMISSION = new ProviderConfigurationPermission("SC", "ecImplicitlyCa");
        BouncyCastleProviderConfiguration.BC_DH_LOCAL_PERMISSION = new ProviderConfigurationPermission("SC", "threadLocalDhDefaultParams");
        BouncyCastleProviderConfiguration.BC_DH_PERMISSION = new ProviderConfigurationPermission("SC", "DhDefaultParams");
        BouncyCastleProviderConfiguration.BC_EC_CURVE_PERMISSION = new ProviderConfigurationPermission("SC", "acceptableEcCurves");
        BouncyCastleProviderConfiguration.BC_ADDITIONAL_EC_CURVE_PERMISSION = new ProviderConfigurationPermission("SC", "additionalEcParameters");
    }
    
    BouncyCastleProviderConfiguration() {
        this.ecThreadSpec = new ThreadLocal();
        this.dhThreadSpec = new ThreadLocal();
        this.acceptableNamedCurves = new HashSet();
        this.additionalECParameters = new HashMap();
    }
    
    @Override
    public Set getAcceptableNamedCurves() {
        return Collections.unmodifiableSet((Set<?>)this.acceptableNamedCurves);
    }
    
    @Override
    public Map getAdditionalECParameters() {
        return Collections.unmodifiableMap((Map<?, ?>)this.additionalECParameters);
    }
    
    @Override
    public DHParameterSpec getDHDefaultParameters(final int n) {
        Object o;
        if ((o = this.dhThreadSpec.get()) == null) {
            o = this.dhDefaultParams;
        }
        if (o instanceof DHParameterSpec) {
            final DHParameterSpec dhParameterSpec = (DHParameterSpec)o;
            if (dhParameterSpec.getP().bitLength() == n) {
                return dhParameterSpec;
            }
        }
        else if (o instanceof DHParameterSpec[]) {
            final DHParameterSpec[] array = (DHParameterSpec[])o;
            for (int i = 0; i != array.length; ++i) {
                if (array[i].getP().bitLength() == n) {
                    return array[i];
                }
            }
        }
        return null;
    }
    
    @Override
    public ECParameterSpec getEcImplicitlyCa() {
        final ECParameterSpec ecParameterSpec = this.ecThreadSpec.get();
        if (ecParameterSpec != null) {
            return ecParameterSpec;
        }
        return this.ecImplicitCaParams;
    }
    
    void setParameter(final String s, final Object dhDefaultParams) {
        final SecurityManager securityManager = System.getSecurityManager();
        if (s.equals("threadLocalEcImplicitlyCa")) {
            if (securityManager != null) {
                securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_EC_LOCAL_PERMISSION);
            }
            ECParameterSpec convertSpec;
            if (!(dhDefaultParams instanceof ECParameterSpec) && dhDefaultParams != null) {
                convertSpec = EC5Util.convertSpec((java.security.spec.ECParameterSpec)dhDefaultParams, false);
            }
            else {
                convertSpec = (ECParameterSpec)dhDefaultParams;
            }
            if (convertSpec == null) {
                this.ecThreadSpec.remove();
                return;
            }
            this.ecThreadSpec.set(convertSpec);
        }
        else if (s.equals("ecImplicitlyCa")) {
            if (securityManager != null) {
                securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_EC_PERMISSION);
            }
            if (!(dhDefaultParams instanceof ECParameterSpec) && dhDefaultParams != null) {
                this.ecImplicitCaParams = EC5Util.convertSpec((java.security.spec.ECParameterSpec)dhDefaultParams, false);
                return;
            }
            this.ecImplicitCaParams = (ECParameterSpec)dhDefaultParams;
        }
        else if (s.equals("threadLocalDhDefaultParams")) {
            if (securityManager != null) {
                securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_DH_LOCAL_PERMISSION);
            }
            if (!(dhDefaultParams instanceof DHParameterSpec) && !(dhDefaultParams instanceof DHParameterSpec[]) && dhDefaultParams != null) {
                throw new IllegalArgumentException("not a valid DHParameterSpec");
            }
            if (dhDefaultParams == null) {
                this.dhThreadSpec.remove();
                return;
            }
            this.dhThreadSpec.set(dhDefaultParams);
        }
        else if (s.equals("DhDefaultParams")) {
            if (securityManager != null) {
                securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_DH_PERMISSION);
            }
            if (!(dhDefaultParams instanceof DHParameterSpec) && !(dhDefaultParams instanceof DHParameterSpec[]) && dhDefaultParams != null) {
                throw new IllegalArgumentException("not a valid DHParameterSpec or DHParameterSpec[]");
            }
            this.dhDefaultParams = dhDefaultParams;
        }
        else {
            if (s.equals("acceptableEcCurves")) {
                if (securityManager != null) {
                    securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_EC_CURVE_PERMISSION);
                }
                this.acceptableNamedCurves = (Set)dhDefaultParams;
                return;
            }
            if (s.equals("additionalEcParameters")) {
                if (securityManager != null) {
                    securityManager.checkPermission(BouncyCastleProviderConfiguration.BC_ADDITIONAL_EC_CURVE_PERMISSION);
                }
                this.additionalECParameters = (Map)dhDefaultParams;
            }
        }
    }
}
