package org.spongycastle.jcajce.provider.asymmetric.dstu;

import java.security.*;
import org.spongycastle.asn1.*;
import java.io.*;

public class SignatureSpiLe extends SignatureSpi
{
    @Override
    protected byte[] engineSign() throws SignatureException {
        final byte[] octets = ASN1OctetString.getInstance(super.engineSign()).getOctets();
        this.reverseBytes(octets);
        try {
            return new DEROctetString(octets).getEncoded();
        }
        catch (Exception ex) {
            throw new SignatureException(ex.toString());
        }
    }
    
    @Override
    protected boolean engineVerify(byte[] octets) throws SignatureException {
        try {
            octets = ((ASN1OctetString)ASN1Primitive.fromByteArray(octets)).getOctets();
            this.reverseBytes(octets);
            try {
                return super.engineVerify(new DEROctetString(octets).getEncoded());
            }
            catch (Exception ex) {
                throw new SignatureException(ex.toString());
            }
            catch (SignatureException ex2) {
                throw ex2;
            }
        }
        catch (IOException ex3) {
            throw new SignatureException("error decoding signature bytes.");
        }
    }
    
    void reverseBytes(final byte[] array) {
        for (int i = 0; i < array.length / 2; ++i) {
            final byte b = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = b;
        }
    }
}
