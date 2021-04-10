package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.symmetric.util.*;
import org.spongycastle.asn1.cms.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.util.*;

class GcmSpecUtil
{
    static final Class gcmSpecClass;
    
    static {
        gcmSpecClass = ClassUtil.loadClass(GcmSpecUtil.class, "javax.crypto.spec.GCMParameterSpec");
    }
    
    static GCMParameters extractGcmParameters(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
        try {
            return new GCMParameters((byte[])GcmSpecUtil.gcmSpecClass.getDeclaredMethod("getIV", (Class[])new Class[0]).invoke(algorithmParameterSpec, new Object[0]), (int)GcmSpecUtil.gcmSpecClass.getDeclaredMethod("getTLen", (Class[])new Class[0]).invoke(algorithmParameterSpec, new Object[0]) / 8);
        }
        catch (Exception ex) {
            throw new InvalidParameterSpecException("Cannot process GCMParameterSpec");
        }
    }
    
    static AlgorithmParameterSpec extractGcmSpec(final ASN1Primitive asn1Primitive) throws InvalidParameterSpecException {
        try {
            final GCMParameters instance = GCMParameters.getInstance(asn1Primitive);
            return GcmSpecUtil.gcmSpecClass.getConstructor(Integer.TYPE, byte[].class).newInstance(Integers.valueOf(instance.getIcvLen() * 8), instance.getNonce());
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Construction failed: ");
            sb.append(ex.getMessage());
            throw new InvalidParameterSpecException(sb.toString());
        }
        catch (NoSuchMethodException ex2) {
            throw new InvalidParameterSpecException("No constructor found!");
        }
    }
    
    static boolean gcmSpecExists() {
        return GcmSpecUtil.gcmSpecClass != null;
    }
    
    static boolean isGcmSpec(final Class clazz) {
        return GcmSpecUtil.gcmSpecClass == clazz;
    }
    
    static boolean isGcmSpec(final AlgorithmParameterSpec algorithmParameterSpec) {
        final Class gcmSpecClass = GcmSpecUtil.gcmSpecClass;
        return gcmSpecClass != null && gcmSpecClass.isInstance(algorithmParameterSpec);
    }
}
