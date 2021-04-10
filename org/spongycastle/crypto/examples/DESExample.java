package org.spongycastle.crypto.examples;

import org.spongycastle.crypto.paddings.*;
import java.security.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.crypto.params.*;
import java.io.*;
import org.spongycastle.crypto.engines.*;
import org.spongycastle.crypto.modes.*;
import org.spongycastle.crypto.*;

public class DESExample
{
    private PaddedBufferedBlockCipher cipher;
    private boolean encrypt;
    private BufferedInputStream in;
    private byte[] key;
    private BufferedOutputStream out;
    
    public DESExample() {
        this.encrypt = true;
        this.cipher = null;
        this.in = null;
        this.out = null;
        this.key = null;
    }
    
    public DESExample(String s, final String s2, final String s3, final boolean encrypt) {
        this.encrypt = true;
        this.cipher = null;
        this.in = null;
        this.out = null;
        this.key = null;
        this.encrypt = encrypt;
        try {
            this.in = new BufferedInputStream(new FileInputStream(s));
        }
        catch (FileNotFoundException ex) {
            final PrintStream err = System.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("Input file not found [");
            sb.append(s);
            sb.append("]");
            err.println(sb.toString());
            System.exit(1);
        }
        try {
            this.out = new BufferedOutputStream(new FileOutputStream(s2));
        }
        catch (IOException ex2) {
            final PrintStream err2 = System.err;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Output file not created [");
            sb2.append(s2);
            sb2.append("]");
            err2.println(sb2.toString());
            System.exit(1);
        }
        PrintStream printStream;
        StringBuilder sb3;
        String s4;
        if (encrypt) {
            try {
                try {
                    s = (String)new SecureRandom();
                    try {
                        ((SecureRandom)s).setSeed("www.bouncycastle.org".getBytes());
                    }
                    catch (Exception ex3) {}
                }
                catch (IOException ex4) {}
            }
            catch (Exception ex5) {
                s = null;
            }
            System.err.println("Hmmm, no SHA1PRNG, you need the Sun implementation");
            System.exit(1);
            final KeyGenerationParameters keyGenerationParameters = new KeyGenerationParameters((SecureRandom)s, 192);
            final DESedeKeyGenerator deSedeKeyGenerator = new DESedeKeyGenerator();
            deSedeKeyGenerator.init(keyGenerationParameters);
            this.key = deSedeKeyGenerator.generateKey();
            final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(s3));
            final byte[] encode = Hex.encode(this.key);
            bufferedOutputStream.write(encode, 0, encode.length);
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            return;
            printStream = System.err;
            sb3 = new StringBuilder();
            s4 = "Could not decryption create key file [";
        }
        else {
            try {
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(s3));
                final int available = bufferedInputStream.available();
                final byte[] array = new byte[available];
                bufferedInputStream.read(array, 0, available);
                this.key = Hex.decode(array);
                return;
            }
            catch (IOException ex6) {
                printStream = System.err;
                sb3 = new StringBuilder();
                s4 = "Decryption key file not found, or not valid [";
            }
        }
        sb3.append(s4);
        sb3.append(s3);
        sb3.append("]");
        printStream.println(sb3.toString());
        System.exit(1);
    }
    
    public static void main(final String[] array) {
        if (array.length < 2) {
            final DESExample desExample = new DESExample();
            final PrintStream err = System.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("Usage: java ");
            sb.append(desExample.getClass().getName());
            sb.append(" infile outfile [keyfile]");
            err.println(sb.toString());
            System.exit(1);
        }
        boolean b = false;
        final String s = array[0];
        final String s2 = array[1];
        String s3;
        if (array.length > 2) {
            s3 = array[2];
        }
        else {
            s3 = "deskey.dat";
            b = true;
        }
        new DESExample(s, s2, s3, b).process();
    }
    
    private void performDecrypt(byte[] decode) {
        this.cipher.init(false, new KeyParameter(decode));
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.in));
        decode = null;
        try {
            while (true) {
                final String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                decode = Hex.decode(line);
                final byte[] array = new byte[this.cipher.getOutputSize(decode.length)];
                final int processBytes = this.cipher.processBytes(decode, 0, decode.length, array, 0);
                decode = array;
                if (processBytes <= 0) {
                    continue;
                }
                this.out.write(array, 0, processBytes);
                decode = array;
            }
            try {
                final int doFinal = this.cipher.doFinal(decode, 0);
                if (doFinal > 0) {
                    this.out.write(decode, 0, doFinal);
                }
            }
            catch (CryptoException ex2) {}
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void performEncrypt(byte[] encode) {
        this.cipher.init(true, new KeyParameter(encode));
        final int outputSize = this.cipher.getOutputSize(47);
        encode = new byte[47];
        final byte[] array = new byte[outputSize];
        try {
            while (true) {
                final int read = this.in.read(encode, 0, 47);
                if (read <= 0) {
                    break;
                }
                final int processBytes = this.cipher.processBytes(encode, 0, read, array, 0);
                if (processBytes <= 0) {
                    continue;
                }
                final byte[] encode2 = Hex.encode(array, 0, processBytes);
                this.out.write(encode2, 0, encode2.length);
                this.out.write(10);
            }
            try {
                final int doFinal = this.cipher.doFinal(array, 0);
                if (doFinal > 0) {
                    encode = Hex.encode(array, 0, doFinal);
                    this.out.write(encode, 0, encode.length);
                    this.out.write(10);
                }
            }
            catch (CryptoException ex2) {}
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void process() {
        this.cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new DESedeEngine()));
        if (this.encrypt) {
            this.performEncrypt(this.key);
        }
        else {
            this.performDecrypt(this.key);
        }
        try {
            this.in.close();
            this.out.flush();
            this.out.close();
        }
        catch (IOException ex) {
            final PrintStream err = System.err;
            final StringBuilder sb = new StringBuilder();
            sb.append("exception closing resources: ");
            sb.append(ex.getMessage());
            err.println(sb.toString());
        }
    }
}
