package org.spongycastle.x509;

import java.security.*;
import java.io.*;
import org.spongycastle.x509.util.*;
import java.util.*;

public class X509StreamParser implements StreamParser
{
    private Provider _provider;
    private X509StreamParserSpi _spi;
    
    private X509StreamParser(final Provider provider, final X509StreamParserSpi spi) {
        this._provider = provider;
        this._spi = spi;
    }
    
    private static X509StreamParser createParser(final X509Util.Implementation implementation) {
        return new X509StreamParser(implementation.getProvider(), (X509StreamParserSpi)implementation.getEngine());
    }
    
    public static X509StreamParser getInstance(final String s) throws NoSuchParserException {
        try {
            return createParser(X509Util.getImplementation("X509StreamParser", s));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new NoSuchParserException(ex.getMessage());
        }
    }
    
    public static X509StreamParser getInstance(final String s, final String s2) throws NoSuchParserException, NoSuchProviderException {
        return getInstance(s, X509Util.getProvider(s2));
    }
    
    public static X509StreamParser getInstance(final String s, final Provider provider) throws NoSuchParserException {
        try {
            return createParser(X509Util.getImplementation("X509StreamParser", s, provider));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new NoSuchParserException(ex.getMessage());
        }
    }
    
    public Provider getProvider() {
        return this._provider;
    }
    
    public void init(final InputStream inputStream) {
        this._spi.engineInit(inputStream);
    }
    
    public void init(final byte[] array) {
        this._spi.engineInit(new ByteArrayInputStream(array));
    }
    
    @Override
    public Object read() throws StreamParsingException {
        return this._spi.engineRead();
    }
    
    @Override
    public Collection readAll() throws StreamParsingException {
        return this._spi.engineReadAll();
    }
}
