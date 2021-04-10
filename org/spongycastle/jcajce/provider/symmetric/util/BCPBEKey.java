package org.spongycastle.jcajce.provider.symmetric.util;

import javax.crypto.interfaces.*;
import org.spongycastle.asn1.*;
import javax.crypto.spec.*;
import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.*;

public class BCPBEKey implements PBEKey
{
    String algorithm;
    int digest;
    int ivSize;
    int keySize;
    ASN1ObjectIdentifier oid;
    CipherParameters param;
    PBEKeySpec pbeKeySpec;
    boolean tryWrong;
    int type;
    
    public BCPBEKey(final String algorithm, final ASN1ObjectIdentifier oid, final int type, final int digest, final int keySize, final int ivSize, final PBEKeySpec pbeKeySpec, final CipherParameters param) {
        this.tryWrong = false;
        this.algorithm = algorithm;
        this.oid = oid;
        this.type = type;
        this.digest = digest;
        this.keySize = keySize;
        this.ivSize = ivSize;
        this.pbeKeySpec = pbeKeySpec;
        this.param = param;
    }
    
    @Override
    public String getAlgorithm() {
        return this.algorithm;
    }
    
    int getDigest() {
        return this.digest;
    }
    
    @Override
    public byte[] getEncoded() {
        final CipherParameters param = this.param;
        if (param != null) {
            CipherParameters parameters = param;
            if (param instanceof ParametersWithIV) {
                parameters = ((ParametersWithIV)param).getParameters();
            }
            return ((KeyParameter)parameters).getKey();
        }
        final int type = this.type;
        if (type == 2) {
            return PBEParametersGenerator.PKCS12PasswordToBytes(this.pbeKeySpec.getPassword());
        }
        if (type == 5) {
            return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(this.pbeKeySpec.getPassword());
        }
        return PBEParametersGenerator.PKCS5PasswordToBytes(this.pbeKeySpec.getPassword());
    }
    
    @Override
    public String getFormat() {
        return "RAW";
    }
    
    @Override
    public int getIterationCount() {
        return this.pbeKeySpec.getIterationCount();
    }
    
    public int getIvSize() {
        return this.ivSize;
    }
    
    int getKeySize() {
        return this.keySize;
    }
    
    public ASN1ObjectIdentifier getOID() {
        return this.oid;
    }
    
    public CipherParameters getParam() {
        return this.param;
    }
    
    @Override
    public char[] getPassword() {
        return this.pbeKeySpec.getPassword();
    }
    
    @Override
    public byte[] getSalt() {
        return this.pbeKeySpec.getSalt();
    }
    
    int getType() {
        return this.type;
    }
    
    public void setTryWrongPKCS12Zero(final boolean tryWrong) {
        this.tryWrong = tryWrong;
    }
    
    boolean shouldTryWrongPKCS12() {
        return this.tryWrong;
    }
}
