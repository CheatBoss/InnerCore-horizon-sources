package org.spongycastle.jcajce.provider.asymmetric.rsa;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.asn1.x509.*;
import javax.crypto.spec.*;
import java.io.*;
import org.spongycastle.jcajce.util.*;
import java.security.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;

public abstract class AlgorithmParametersSpi extends java.security.AlgorithmParametersSpi
{
    @Override
    protected AlgorithmParameterSpec engineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != null) {
            return this.localEngineGetParameterSpec(clazz);
        }
        throw new NullPointerException("argument to getParameterSpec must not be null");
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(final Class p0) throws InvalidParameterSpecException;
    
    public static class OAEP extends AlgorithmParametersSpi
    {
        OAEPParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() {
            final RSAESOAEPparams rsaesoaePparams = new RSAESOAEPparams(new AlgorithmIdentifier(DigestFactory.getOID(this.currentSpec.getDigestAlgorithm()), DERNull.INSTANCE), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(DigestFactory.getOID(((MGF1ParameterSpec)this.currentSpec.getMGFParameters()).getDigestAlgorithm()), DERNull.INSTANCE)), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_pSpecified, new DEROctetString(((PSource.PSpecified)this.currentSpec.getPSource()).getValue())));
            try {
                return rsaesoaePparams.getEncoded("DER");
            }
            catch (IOException ex) {
                throw new RuntimeException("Error encoding OAEPParameters");
            }
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) {
            if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
                return null;
            }
            return this.engineGetEncoded();
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof OAEPParameterSpec) {
                this.currentSpec = (OAEPParameterSpec)algorithmParameterSpec;
                return;
            }
            throw new InvalidParameterSpecException("OAEPParameterSpec required to initialise an OAEP algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final RSAESOAEPparams instance = RSAESOAEPparams.getInstance(array);
                if (instance.getMaskGenAlgorithm().getAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1)) {
                    this.currentSpec = new OAEPParameterSpec(MessageDigestUtils.getDigestName(instance.getHashAlgorithm().getAlgorithm()), OAEPParameterSpec.DEFAULT.getMGFAlgorithm(), new MGF1ParameterSpec(MessageDigestUtils.getDigestName(AlgorithmIdentifier.getInstance(instance.getMaskGenAlgorithm().getParameters()).getAlgorithm())), new PSource.PSpecified(ASN1OctetString.getInstance(instance.getPSourceAlgorithm().getParameters()).getOctets()));
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown mask generation function: ");
                sb.append(instance.getMaskGenAlgorithm().getAlgorithm());
                throw new IOException(sb.toString());
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
            catch (ClassCastException ex2) {
                throw new IOException("Not a valid OAEP Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (!s.equalsIgnoreCase("X.509") && !s.equalsIgnoreCase("ASN.1")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown parameter format ");
                sb.append(s);
                throw new IOException(sb.toString());
            }
            this.engineInit(array);
        }
        
        @Override
        protected String engineToString() {
            return "OAEP Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz != OAEPParameterSpec.class && clazz != AlgorithmParameterSpec.class) {
                throw new InvalidParameterSpecException("unknown parameter spec passed to OAEP parameters object.");
            }
            return this.currentSpec;
        }
    }
    
    public static class PSS extends AlgorithmParametersSpi
    {
        PSSParameterSpec currentSpec;
        
        @Override
        protected byte[] engineGetEncoded() throws IOException {
            final PSSParameterSpec currentSpec = this.currentSpec;
            return new RSASSAPSSparams(new AlgorithmIdentifier(DigestFactory.getOID(currentSpec.getDigestAlgorithm()), DERNull.INSTANCE), new AlgorithmIdentifier(PKCSObjectIdentifiers.id_mgf1, new AlgorithmIdentifier(DigestFactory.getOID(((MGF1ParameterSpec)currentSpec.getMGFParameters()).getDigestAlgorithm()), DERNull.INSTANCE)), new ASN1Integer(currentSpec.getSaltLength()), new ASN1Integer(currentSpec.getTrailerField())).getEncoded("DER");
        }
        
        @Override
        protected byte[] engineGetEncoded(final String s) throws IOException {
            if (!s.equalsIgnoreCase("X.509") && !s.equalsIgnoreCase("ASN.1")) {
                return null;
            }
            return this.engineGetEncoded();
        }
        
        @Override
        protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec) throws InvalidParameterSpecException {
            if (algorithmParameterSpec instanceof PSSParameterSpec) {
                this.currentSpec = (PSSParameterSpec)algorithmParameterSpec;
                return;
            }
            throw new InvalidParameterSpecException("PSSParameterSpec required to initialise an PSS algorithm parameters object");
        }
        
        @Override
        protected void engineInit(final byte[] array) throws IOException {
            try {
                final RSASSAPSSparams instance = RSASSAPSSparams.getInstance(array);
                if (instance.getMaskGenAlgorithm().getAlgorithm().equals(PKCSObjectIdentifiers.id_mgf1)) {
                    this.currentSpec = new PSSParameterSpec(MessageDigestUtils.getDigestName(instance.getHashAlgorithm().getAlgorithm()), PSSParameterSpec.DEFAULT.getMGFAlgorithm(), new MGF1ParameterSpec(MessageDigestUtils.getDigestName(AlgorithmIdentifier.getInstance(instance.getMaskGenAlgorithm().getParameters()).getAlgorithm())), instance.getSaltLength().intValue(), instance.getTrailerField().intValue());
                    return;
                }
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown mask generation function: ");
                sb.append(instance.getMaskGenAlgorithm().getAlgorithm());
                throw new IOException(sb.toString());
            }
            catch (ArrayIndexOutOfBoundsException ex) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
            catch (ClassCastException ex2) {
                throw new IOException("Not a valid PSS Parameter encoding.");
            }
        }
        
        @Override
        protected void engineInit(final byte[] array, final String s) throws IOException {
            if (!this.isASN1FormatString(s) && !s.equalsIgnoreCase("X.509")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unknown parameter format ");
                sb.append(s);
                throw new IOException(sb.toString());
            }
            this.engineInit(array);
        }
        
        @Override
        protected String engineToString() {
            return "PSS Parameters";
        }
        
        @Override
        protected AlgorithmParameterSpec localEngineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
            if (clazz == PSSParameterSpec.class) {
                final PSSParameterSpec currentSpec = this.currentSpec;
                if (currentSpec != null) {
                    return currentSpec;
                }
            }
            throw new InvalidParameterSpecException("unknown parameter spec passed to PSS parameters object.");
        }
    }
}
