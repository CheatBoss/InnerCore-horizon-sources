package org.spongycastle.jcajce.provider.config;

import org.spongycastle.util.*;
import java.util.*;
import java.security.*;

public class ProviderConfigurationPermission extends BasicPermission
{
    private static final int ACCEPTABLE_EC_CURVES = 16;
    private static final String ACCEPTABLE_EC_CURVES_STR = "acceptableeccurves";
    private static final int ADDITIONAL_EC_PARAMETERS = 32;
    private static final String ADDITIONAL_EC_PARAMETERS_STR = "additionalecparameters";
    private static final int ALL = 63;
    private static final String ALL_STR = "all";
    private static final int DH_DEFAULT_PARAMS = 8;
    private static final String DH_DEFAULT_PARAMS_STR = "dhdefaultparams";
    private static final int EC_IMPLICITLY_CA = 2;
    private static final String EC_IMPLICITLY_CA_STR = "ecimplicitlyca";
    private static final int THREAD_LOCAL_DH_DEFAULT_PARAMS = 4;
    private static final String THREAD_LOCAL_DH_DEFAULT_PARAMS_STR = "threadlocaldhdefaultparams";
    private static final int THREAD_LOCAL_EC_IMPLICITLY_CA = 1;
    private static final String THREAD_LOCAL_EC_IMPLICITLY_CA_STR = "threadlocalecimplicitlyca";
    private final String actions;
    private final int permissionMask;
    
    public ProviderConfigurationPermission(final String s) {
        super(s);
        this.actions = "all";
        this.permissionMask = 63;
    }
    
    public ProviderConfigurationPermission(final String s, final String actions) {
        super(s, actions);
        this.actions = actions;
        this.permissionMask = this.calculateMask(actions);
    }
    
    private int calculateMask(final String s) {
        final StringTokenizer stringTokenizer = new StringTokenizer(Strings.toLowerCase(s), " ,");
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            final String nextToken = stringTokenizer.nextToken();
            if (nextToken.equals("threadlocalecimplicitlyca")) {
                n |= 0x1;
            }
            else if (nextToken.equals("ecimplicitlyca")) {
                n |= 0x2;
            }
            else if (nextToken.equals("threadlocaldhdefaultparams")) {
                n |= 0x4;
            }
            else if (nextToken.equals("dhdefaultparams")) {
                n |= 0x8;
            }
            else if (nextToken.equals("acceptableeccurves")) {
                n |= 0x10;
            }
            else if (nextToken.equals("additionalecparameters")) {
                n |= 0x20;
            }
            else {
                if (!nextToken.equals("all")) {
                    continue;
                }
                n |= 0x3F;
            }
        }
        if (n != 0) {
            return n;
        }
        throw new IllegalArgumentException("unknown permissions passed to mask");
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof ProviderConfigurationPermission) {
            final ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)o;
            if (this.permissionMask == providerConfigurationPermission.permissionMask && this.getName().equals(providerConfigurationPermission.getName())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String getActions() {
        return this.actions;
    }
    
    @Override
    public int hashCode() {
        return this.getName().hashCode() + this.permissionMask;
    }
    
    @Override
    public boolean implies(final Permission permission) {
        final boolean b = permission instanceof ProviderConfigurationPermission;
        boolean b2 = false;
        if (!b) {
            return false;
        }
        if (!this.getName().equals(permission.getName())) {
            return false;
        }
        final ProviderConfigurationPermission providerConfigurationPermission = (ProviderConfigurationPermission)permission;
        final int permissionMask = this.permissionMask;
        final int permissionMask2 = providerConfigurationPermission.permissionMask;
        if ((permissionMask & permissionMask2) == permissionMask2) {
            b2 = true;
        }
        return b2;
    }
}
