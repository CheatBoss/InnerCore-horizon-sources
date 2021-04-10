package org.spongycastle.crypto.parsers;

import org.spongycastle.crypto.*;
import java.io.*;
import org.spongycastle.util.io.*;
import org.spongycastle.crypto.params.*;

public class ECIESPublicKeyParser implements KeyParser
{
    private ECDomainParameters ecParams;
    
    public ECIESPublicKeyParser(final ECDomainParameters ecParams) {
        this.ecParams = ecParams;
    }
    
    @Override
    public AsymmetricKeyParameter readKey(final InputStream inputStream) throws IOException {
        final int read = inputStream.read();
        if (read != 0) {
            byte[] array;
            if (read != 2 && read != 3) {
                if (read != 4 && read != 6 && read != 7) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Sender's public key has invalid point encoding 0x");
                    sb.append(Integer.toString(read, 16));
                    throw new IOException(sb.toString());
                }
                array = new byte[(this.ecParams.getCurve().getFieldSize() + 7) / 8 * 2 + 1];
            }
            else {
                array = new byte[(this.ecParams.getCurve().getFieldSize() + 7) / 8 + 1];
            }
            array[0] = (byte)read;
            Streams.readFully(inputStream, array, 1, array.length - 1);
            return new ECPublicKeyParameters(this.ecParams.getCurve().decodePoint(array), this.ecParams);
        }
        throw new IOException("Sender's public key invalid.");
    }
}
