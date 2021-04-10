package org.spongycastle.jcajce.spec;

import java.security.spec.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x9.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.*;

public class KTSParameterSpec implements AlgorithmParameterSpec
{
    private final AlgorithmIdentifier kdfAlgorithm;
    private final int keySizeInBits;
    private byte[] otherInfo;
    private final AlgorithmParameterSpec parameterSpec;
    private final String wrappingKeyAlgorithm;
    
    private KTSParameterSpec(final String wrappingKeyAlgorithm, final int keySizeInBits, final AlgorithmParameterSpec parameterSpec, final AlgorithmIdentifier kdfAlgorithm, final byte[] otherInfo) {
        this.wrappingKeyAlgorithm = wrappingKeyAlgorithm;
        this.keySizeInBits = keySizeInBits;
        this.parameterSpec = parameterSpec;
        this.kdfAlgorithm = kdfAlgorithm;
        this.otherInfo = otherInfo;
    }
    
    public AlgorithmIdentifier getKdfAlgorithm() {
        return this.kdfAlgorithm;
    }
    
    public String getKeyAlgorithmName() {
        return this.wrappingKeyAlgorithm;
    }
    
    public int getKeySize() {
        return this.keySizeInBits;
    }
    
    public byte[] getOtherInfo() {
        return Arrays.clone(this.otherInfo);
    }
    
    public AlgorithmParameterSpec getParameterSpec() {
        return this.parameterSpec;
    }
    
    public static final class Builder
    {
        private final String algorithmName;
        private AlgorithmIdentifier kdfAlgorithm;
        private final int keySizeInBits;
        private byte[] otherInfo;
        private AlgorithmParameterSpec parameterSpec;
        
        public Builder(final String s, final int n) {
            this(s, n, null);
        }
        
        public Builder(final String algorithmName, final int keySizeInBits, final byte[] array) {
            this.algorithmName = algorithmName;
            this.keySizeInBits = keySizeInBits;
            this.kdfAlgorithm = new AlgorithmIdentifier(X9ObjectIdentifiers.id_kdf_kdf3, new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256));
            byte[] clone;
            if (array == null) {
                clone = new byte[0];
            }
            else {
                clone = Arrays.clone(array);
            }
            this.otherInfo = clone;
        }
        
        public KTSParameterSpec build() {
            return new KTSParameterSpec(this.algorithmName, this.keySizeInBits, this.parameterSpec, this.kdfAlgorithm, this.otherInfo, null);
        }
        
        public Builder withKdfAlgorithm(final AlgorithmIdentifier kdfAlgorithm) {
            this.kdfAlgorithm = kdfAlgorithm;
            return this;
        }
        
        public Builder withParameterSpec(final AlgorithmParameterSpec parameterSpec) {
            this.parameterSpec = parameterSpec;
            return this;
        }
    }
}
