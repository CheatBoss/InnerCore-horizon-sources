package org.spongycastle.pqc.jcajce.provider;

import org.spongycastle.jcajce.provider.config.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.pkcs.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;
import java.security.*;
import org.spongycastle.jcajce.provider.util.*;
import java.util.*;

public class BouncyCastlePQCProvider extends Provider implements ConfigurableProvider
{
    private static final String[] ALGORITHMS;
    private static final String ALGORITHM_PACKAGE = "org.spongycastle.pqc.jcajce.provider.";
    public static final ProviderConfiguration CONFIGURATION;
    public static String PROVIDER_NAME = "BCPQC";
    private static String info = "BouncyCastle Post-Quantum Security Provider v1.58";
    private static final Map keyInfoConverters;
    
    static {
        keyInfoConverters = new HashMap();
        ALGORITHMS = new String[] { "Rainbow", "McEliece", "SPHINCS", "NH", "XMSS" };
    }
    
    public BouncyCastlePQCProvider() {
        super(BouncyCastlePQCProvider.PROVIDER_NAME, 1.58, BouncyCastlePQCProvider.info);
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction() {
            @Override
            public Object run() {
                BouncyCastlePQCProvider.this.setup();
                return null;
            }
        });
    }
    
    private static AsymmetricKeyInfoConverter getAsymmetricKeyInfoConverter(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        synchronized (BouncyCastlePQCProvider.keyInfoConverters) {
            return BouncyCastlePQCProvider.keyInfoConverters.get(asn1ObjectIdentifier);
        }
    }
    
    public static PrivateKey getPrivateKey(final PrivateKeyInfo privateKeyInfo) throws IOException {
        final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(privateKeyInfo.getPrivateKeyAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePrivate(privateKeyInfo);
    }
    
    public static PublicKey getPublicKey(final SubjectPublicKeyInfo subjectPublicKeyInfo) throws IOException {
        final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter = getAsymmetricKeyInfoConverter(subjectPublicKeyInfo.getAlgorithm().getAlgorithm());
        if (asymmetricKeyInfoConverter == null) {
            return null;
        }
        return asymmetricKeyInfoConverter.generatePublic(subjectPublicKeyInfo);
    }
    
    private void loadAlgorithms(final String s, final String[] array) {
        for (int i = 0; i != array.length; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(array[i]);
            sb.append("$Mappings");
            final Class loadClass = loadClass(BouncyCastlePQCProvider.class, sb.toString());
            if (loadClass != null) {
                try {
                    loadClass.newInstance().configure(this);
                }
                catch (Exception ex) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("cannot create instance of ");
                    sb2.append(s);
                    sb2.append(array[i]);
                    sb2.append("$Mappings : ");
                    sb2.append(ex);
                    throw new InternalError(sb2.toString());
                }
            }
        }
    }
    
    static Class loadClass(Class clazz, final String s) {
        try {
            final ClassLoader classLoader = clazz.getClassLoader();
            if (classLoader != null) {
                return classLoader.loadClass(s);
            }
            clazz = AccessController.doPrivileged((PrivilegedAction<Class>)new PrivilegedAction() {
                @Override
                public Object run() {
                    try {
                        return Class.forName(s);
                    }
                    catch (Exception ex) {
                        return null;
                    }
                }
            });
            return clazz;
        }
        catch (ClassNotFoundException ex) {
            return null;
        }
    }
    
    private void setup() {
        this.loadAlgorithms("org.spongycastle.pqc.jcajce.provider.", BouncyCastlePQCProvider.ALGORITHMS);
    }
    
    @Override
    public void addAlgorithm(final String s, final String s2) {
        if (!this.containsKey(s)) {
            this.put(s, s2);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("duplicate provider key (");
        sb.append(s);
        sb.append(") found");
        throw new IllegalStateException(sb.toString());
    }
    
    @Override
    public void addAlgorithm(final String s, final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".");
        sb.append(s2);
        if (this.containsKey(sb.toString())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(s);
            sb2.append(".");
            sb2.append(asn1ObjectIdentifier);
            this.addAlgorithm(sb2.toString(), s2);
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(s);
            sb3.append(".OID.");
            sb3.append(asn1ObjectIdentifier);
            this.addAlgorithm(sb3.toString(), s2);
            return;
        }
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("primary key (");
        sb4.append(s);
        sb4.append(".");
        sb4.append(s2);
        sb4.append(") not found");
        throw new IllegalStateException(sb4.toString());
    }
    
    @Override
    public void addAttributes(final String s, final Map<String, String> map) {
        for (final String s2 : map.keySet()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" ");
            sb.append(s2);
            final String string = sb.toString();
            if (this.containsKey(string)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("duplicate provider attribute key (");
                sb2.append(string);
                sb2.append(") found");
                throw new IllegalStateException(sb2.toString());
            }
            this.put(string, map.get(s2));
        }
    }
    
    @Override
    public void addKeyInfoConverter(final ASN1ObjectIdentifier asn1ObjectIdentifier, final AsymmetricKeyInfoConverter asymmetricKeyInfoConverter) {
        synchronized (BouncyCastlePQCProvider.keyInfoConverters) {
            BouncyCastlePQCProvider.keyInfoConverters.put(asn1ObjectIdentifier, asymmetricKeyInfoConverter);
        }
    }
    
    @Override
    public boolean hasAlgorithm(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(".");
        sb.append(s2);
        if (!this.containsKey(sb.toString())) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.");
            sb2.append(s);
            sb2.append(".");
            sb2.append(s2);
            if (!this.containsKey(sb2.toString())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void setParameter(final String s, final Object o) {
        synchronized (BouncyCastlePQCProvider.CONFIGURATION) {
        }
        // monitorexit(BouncyCastlePQCProvider.CONFIGURATION)
    }
}
