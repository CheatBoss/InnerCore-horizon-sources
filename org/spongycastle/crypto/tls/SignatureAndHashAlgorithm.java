package org.spongycastle.crypto.tls;

import java.io.*;

public class SignatureAndHashAlgorithm
{
    protected short hash;
    protected short signature;
    
    public SignatureAndHashAlgorithm(final short hash, final short signature) {
        if (!TlsUtils.isValidUint8(hash)) {
            throw new IllegalArgumentException("'hash' should be a uint8");
        }
        if (!TlsUtils.isValidUint8(signature)) {
            throw new IllegalArgumentException("'signature' should be a uint8");
        }
        if (signature != 0) {
            this.hash = hash;
            this.signature = signature;
            return;
        }
        throw new IllegalArgumentException("'signature' MUST NOT be \"anonymous\"");
    }
    
    public static SignatureAndHashAlgorithm parse(final InputStream inputStream) throws IOException {
        return new SignatureAndHashAlgorithm(TlsUtils.readUint8(inputStream), TlsUtils.readUint8(inputStream));
    }
    
    public void encode(final OutputStream outputStream) throws IOException {
        TlsUtils.writeUint8(this.getHash(), outputStream);
        TlsUtils.writeUint8(this.getSignature(), outputStream);
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof SignatureAndHashAlgorithm;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final SignatureAndHashAlgorithm signatureAndHashAlgorithm = (SignatureAndHashAlgorithm)o;
        boolean b3 = b2;
        if (signatureAndHashAlgorithm.getHash() == this.getHash()) {
            b3 = b2;
            if (signatureAndHashAlgorithm.getSignature() == this.getSignature()) {
                b3 = true;
            }
        }
        return b3;
    }
    
    public short getHash() {
        return this.hash;
    }
    
    public short getSignature() {
        return this.signature;
    }
    
    @Override
    public int hashCode() {
        return this.getHash() << 16 | this.getSignature();
    }
}
