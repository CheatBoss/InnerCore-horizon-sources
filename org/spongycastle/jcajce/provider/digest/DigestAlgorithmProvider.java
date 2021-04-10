package org.spongycastle.jcajce.provider.digest;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.*;

abstract class DigestAlgorithmProvider extends AlgorithmProvider
{
    protected void addHMACAlgorithm(final ConfigurableProvider configurableProvider, final String s, final String s2, final String s3) {
        final StringBuilder sb = new StringBuilder();
        sb.append("HMAC");
        sb.append(s);
        final String string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Mac.");
        sb2.append(string);
        configurableProvider.addAlgorithm(sb2.toString(), s2);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Alg.Alias.Mac.HMAC-");
        sb3.append(s);
        configurableProvider.addAlgorithm(sb3.toString(), string);
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Alg.Alias.Mac.HMAC/");
        sb4.append(s);
        configurableProvider.addAlgorithm(sb4.toString(), string);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("KeyGenerator.");
        sb5.append(string);
        configurableProvider.addAlgorithm(sb5.toString(), s3);
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("Alg.Alias.KeyGenerator.HMAC-");
        sb6.append(s);
        configurableProvider.addAlgorithm(sb6.toString(), string);
        final StringBuilder sb7 = new StringBuilder();
        sb7.append("Alg.Alias.KeyGenerator.HMAC/");
        sb7.append(s);
        configurableProvider.addAlgorithm(sb7.toString(), string);
    }
    
    protected void addHMACAlias(final ConfigurableProvider configurableProvider, String string, final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final StringBuilder sb = new StringBuilder();
        sb.append("HMAC");
        sb.append(string);
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.Mac.");
        sb2.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb2.toString(), string);
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Alg.Alias.KeyGenerator.");
        sb3.append(asn1ObjectIdentifier);
        configurableProvider.addAlgorithm(sb3.toString(), string);
    }
}
