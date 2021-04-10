package org.spongycastle.jcajce.provider.util;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.*;

public abstract class AsymmetricAlgorithmProvider extends AlgorithmProvider
{
    protected void addSignatureAlgorithm(final ConfigurableProvider configurableProvider, String string, final String s, final String s2, final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append("WITH");
        sb.append(s);
        final String string2 = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append("with");
        sb2.append(s);
        final String string3 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(string);
        sb3.append("With");
        sb3.append(s);
        final String string4 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(string);
        sb4.append("/");
        sb4.append(s);
        string = sb4.toString();
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Signature.");
        sb5.append(string2);
        configurableProvider.addAlgorithm(sb5.toString(), s2);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Alg.Alias.Signature.");
        sb6.append(string3);
        configurableProvider.addAlgorithm(sb6.toString(), string2);
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("Alg.Alias.Signature.");
        sb7.append(string4);
        configurableProvider.addAlgorithm(sb7.toString(), string2);
        final StringBuilder sb8 = new StringBuilder();
        sb8.append("Alg.Alias.Signature.");
        sb8.append(string);
        configurableProvider.addAlgorithm(sb8.toString(), string2);
        final StringBuilder sb9 = new StringBuilder();
        sb9.append("Alg.Alias.Signature.");
        sb9.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb9.toString(), string2);
        final StringBuilder sb10 = new StringBuilder();
        sb10.append("Alg.Alias.Signature.OID.");
        sb10.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb10.toString(), string2);
    }
    
    protected void registerOid(final ConfigurableProvider configurableProvider, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s, final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alg.Alias.KeyFactory.");
        sb.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb.toString(), s);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.KeyPairGenerator.");
        sb2.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb2.toString(), s);
        configurableProvider.addKeyInfoConverter(asn1ObjectIdentifier, asymmetricKeyInfoConverter);
    }
    
    protected void registerOidAlgorithmParameterGenerator(final ConfigurableProvider configurableProvider, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alg.Alias.AlgorithmParameterGenerator.");
        sb.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb.toString(), s);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.AlgorithmParameters.");
        sb2.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb2.toString(), s);
    }
    
    protected void registerOidAlgorithmParameters(final ConfigurableProvider configurableProvider, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Alg.Alias.AlgorithmParameters.");
        sb.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb.toString(), s);
    }
}
