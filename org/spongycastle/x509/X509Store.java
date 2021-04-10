package org.spongycastle.x509;

import java.security.*;
import org.spongycastle.util.*;
import java.util.*;

public class X509Store implements Store
{
    private Provider _provider;
    private X509StoreSpi _spi;
    
    private X509Store(final Provider provider, final X509StoreSpi spi) {
        this._provider = provider;
        this._spi = spi;
    }
    
    private static X509Store createStore(final X509Util.Implementation implementation, final X509StoreParameters x509StoreParameters) {
        final X509StoreSpi x509StoreSpi = (X509StoreSpi)implementation.getEngine();
        x509StoreSpi.engineInit(x509StoreParameters);
        return new X509Store(implementation.getProvider(), x509StoreSpi);
    }
    
    public static X509Store getInstance(final String s, final X509StoreParameters x509StoreParameters) throws NoSuchStoreException {
        try {
            return createStore(X509Util.getImplementation("X509Store", s), x509StoreParameters);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new NoSuchStoreException(ex.getMessage());
        }
    }
    
    public static X509Store getInstance(final String s, final X509StoreParameters x509StoreParameters, final String s2) throws NoSuchStoreException, NoSuchProviderException {
        return getInstance(s, x509StoreParameters, X509Util.getProvider(s2));
    }
    
    public static X509Store getInstance(final String s, final X509StoreParameters x509StoreParameters, final Provider provider) throws NoSuchStoreException {
        try {
            return createStore(X509Util.getImplementation("X509Store", s, provider), x509StoreParameters);
        }
        catch (NoSuchAlgorithmException ex) {
            throw new NoSuchStoreException(ex.getMessage());
        }
    }
    
    @Override
    public Collection getMatches(final Selector selector) {
        return this._spi.engineGetMatches(selector);
    }
    
    public Provider getProvider() {
        return this._provider;
    }
}
