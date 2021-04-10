package org.spongycastle.jcajce;

import org.spongycastle.crypto.*;

public class PKCS12Key implements PBKDFKey
{
    private final char[] password;
    private final boolean useWrongZeroLengthConversion;
    
    public PKCS12Key(final char[] array) {
        this(array, false);
    }
    
    public PKCS12Key(char[] password, final boolean useWrongZeroLengthConversion) {
        char[] array = password;
        if (password == null) {
            array = new char[0];
        }
        password = new char[array.length];
        this.password = password;
        this.useWrongZeroLengthConversion = useWrongZeroLengthConversion;
        System.arraycopy(array, 0, password, 0, array.length);
    }
    
    @Override
    public String getAlgorithm() {
        return "PKCS12";
    }
    
    @Override
    public byte[] getEncoded() {
        if (this.useWrongZeroLengthConversion && this.password.length == 0) {
            return new byte[2];
        }
        return PBEParametersGenerator.PKCS12PasswordToBytes(this.password);
    }
    
    @Override
    public String getFormat() {
        return "PKCS12";
    }
    
    public char[] getPassword() {
        return this.password;
    }
}
