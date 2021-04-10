package org.spongycastle.jce.spec;

import java.security.spec.*;
import org.spongycastle.jce.interfaces.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.cryptopro.*;

public class GOST3410ParameterSpec implements AlgorithmParameterSpec, GOST3410Params
{
    private String digestParamSetOID;
    private String encryptionParamSetOID;
    private String keyParamSetOID;
    private GOST3410PublicKeyParameterSetSpec keyParameters;
    
    public GOST3410ParameterSpec(final String s) {
        this(s, CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId(), null);
    }
    
    public GOST3410ParameterSpec(final String s, final String s2) {
        this(s, s2, null);
    }
    
    public GOST3410ParameterSpec(String id, final String digestParamSetOID, final String encryptionParamSetOID) {
        GOST3410ParamSetParameters gost3410ParamSetParameters;
        try {
            gost3410ParamSetParameters = GOST3410NamedParameters.getByOID(new ASN1ObjectIdentifier(id));
        }
        catch (IllegalArgumentException ex) {
            final ASN1ObjectIdentifier oid = GOST3410NamedParameters.getOID(id);
            if (oid != null) {
                id = oid.getId();
                gost3410ParamSetParameters = GOST3410NamedParameters.getByOID(oid);
            }
            else {
                gost3410ParamSetParameters = null;
            }
        }
        if (gost3410ParamSetParameters != null) {
            this.keyParameters = new GOST3410PublicKeyParameterSetSpec(gost3410ParamSetParameters.getP(), gost3410ParamSetParameters.getQ(), gost3410ParamSetParameters.getA());
            this.keyParamSetOID = id;
            this.digestParamSetOID = digestParamSetOID;
            this.encryptionParamSetOID = encryptionParamSetOID;
            return;
        }
        throw new IllegalArgumentException("no key parameter set for passed in name/OID.");
    }
    
    public GOST3410ParameterSpec(final GOST3410PublicKeyParameterSetSpec keyParameters) {
        this.keyParameters = keyParameters;
        this.digestParamSetOID = CryptoProObjectIdentifiers.gostR3411_94_CryptoProParamSet.getId();
        this.encryptionParamSetOID = null;
    }
    
    public static GOST3410ParameterSpec fromPublicKeyAlg(final GOST3410PublicKeyAlgParameters gost3410PublicKeyAlgParameters) {
        if (gost3410PublicKeyAlgParameters.getEncryptionParamSet() != null) {
            return new GOST3410ParameterSpec(gost3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), gost3410PublicKeyAlgParameters.getDigestParamSet().getId(), gost3410PublicKeyAlgParameters.getEncryptionParamSet().getId());
        }
        return new GOST3410ParameterSpec(gost3410PublicKeyAlgParameters.getPublicKeyParamSet().getId(), gost3410PublicKeyAlgParameters.getDigestParamSet().getId());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof GOST3410ParameterSpec) {
            final GOST3410ParameterSpec gost3410ParameterSpec = (GOST3410ParameterSpec)o;
            if (this.keyParameters.equals(gost3410ParameterSpec.keyParameters) && this.digestParamSetOID.equals(gost3410ParameterSpec.digestParamSetOID)) {
                final String encryptionParamSetOID = this.encryptionParamSetOID;
                final String encryptionParamSetOID2 = gost3410ParameterSpec.encryptionParamSetOID;
                if (encryptionParamSetOID == encryptionParamSetOID2 || (encryptionParamSetOID != null && encryptionParamSetOID.equals(encryptionParamSetOID2))) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public String getDigestParamSetOID() {
        return this.digestParamSetOID;
    }
    
    @Override
    public String getEncryptionParamSetOID() {
        return this.encryptionParamSetOID;
    }
    
    @Override
    public String getPublicKeyParamSetOID() {
        return this.keyParamSetOID;
    }
    
    @Override
    public GOST3410PublicKeyParameterSetSpec getPublicKeyParameters() {
        return this.keyParameters;
    }
    
    @Override
    public int hashCode() {
        final int hashCode = this.keyParameters.hashCode();
        final int hashCode2 = this.digestParamSetOID.hashCode();
        final String encryptionParamSetOID = this.encryptionParamSetOID;
        int hashCode3;
        if (encryptionParamSetOID != null) {
            hashCode3 = encryptionParamSetOID.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        return hashCode ^ hashCode2 ^ hashCode3;
    }
}
