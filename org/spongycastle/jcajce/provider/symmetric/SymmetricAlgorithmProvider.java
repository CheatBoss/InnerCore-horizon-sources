package org.spongycastle.jcajce.provider.symmetric;

import org.spongycastle.jcajce.provider.util.*;
import org.spongycastle.jcajce.provider.config.*;

abstract class SymmetricAlgorithmProvider extends AlgorithmProvider
{
    protected void addCMacAlgorithm(final ConfigurableProvider configurableProvider, final String s, String s2, final String s3) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Mac.");
        sb.append(s);
        sb.append("-CMAC");
        configurableProvider.addAlgorithm(sb.toString(), s2);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.Mac.");
        sb2.append(s);
        sb2.append("CMAC");
        s2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("-CMAC");
        configurableProvider.addAlgorithm(s2, sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("KeyGenerator.");
        sb4.append(s);
        sb4.append("-CMAC");
        configurableProvider.addAlgorithm(sb4.toString(), s3);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Alg.Alias.KeyGenerator.");
        sb5.append(s);
        sb5.append("CMAC");
        s2 = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(s);
        sb6.append("-CMAC");
        configurableProvider.addAlgorithm(s2, sb6.toString());
    }
    
    protected void addGMacAlgorithm(final ConfigurableProvider configurableProvider, final String s, String s2, final String s3) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Mac.");
        sb.append(s);
        sb.append("-GMAC");
        configurableProvider.addAlgorithm(sb.toString(), s2);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.Mac.");
        sb2.append(s);
        sb2.append("GMAC");
        s2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(s);
        sb3.append("-GMAC");
        configurableProvider.addAlgorithm(s2, sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("KeyGenerator.");
        sb4.append(s);
        sb4.append("-GMAC");
        configurableProvider.addAlgorithm(sb4.toString(), s3);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Alg.Alias.KeyGenerator.");
        sb5.append(s);
        sb5.append("GMAC");
        s2 = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append(s);
        sb6.append("-GMAC");
        configurableProvider.addAlgorithm(s2, sb6.toString());
    }
    
    protected void addPoly1305Algorithm(final ConfigurableProvider configurableProvider, final String s, String s2, final String s3) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Mac.POLY1305-");
        sb.append(s);
        configurableProvider.addAlgorithm(sb.toString(), s2);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("Alg.Alias.Mac.POLY1305");
        sb2.append(s);
        s2 = sb2.toString();
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("POLY1305-");
        sb3.append(s);
        configurableProvider.addAlgorithm(s2, sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("KeyGenerator.POLY1305-");
        sb4.append(s);
        configurableProvider.addAlgorithm(sb4.toString(), s3);
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("Alg.Alias.KeyGenerator.POLY1305");
        sb5.append(s);
        s2 = sb5.toString();
        final StringBuilder sb6 = new StringBuilder();
        sb6.append("POLY1305-");
        sb6.append(s);
        configurableProvider.addAlgorithm(s2, sb6.toString());
    }
}
