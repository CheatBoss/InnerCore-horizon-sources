package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.text.*;
import java.io.*;
import java.util.*;

public class SkeinParameters implements CipherParameters
{
    public static final int PARAM_TYPE_CONFIG = 4;
    public static final int PARAM_TYPE_KEY = 0;
    public static final int PARAM_TYPE_KEY_IDENTIFIER = 16;
    public static final int PARAM_TYPE_MESSAGE = 48;
    public static final int PARAM_TYPE_NONCE = 20;
    public static final int PARAM_TYPE_OUTPUT = 63;
    public static final int PARAM_TYPE_PERSONALISATION = 8;
    public static final int PARAM_TYPE_PUBLIC_KEY = 12;
    private Hashtable parameters;
    
    public SkeinParameters() {
        this(new Hashtable());
    }
    
    private SkeinParameters(final Hashtable parameters) {
        this.parameters = parameters;
    }
    
    public byte[] getKey() {
        return this.parameters.get(Integers.valueOf(0));
    }
    
    public byte[] getKeyIdentifier() {
        return this.parameters.get(Integers.valueOf(16));
    }
    
    public byte[] getNonce() {
        return this.parameters.get(Integers.valueOf(20));
    }
    
    public Hashtable getParameters() {
        return this.parameters;
    }
    
    public byte[] getPersonalisation() {
        return this.parameters.get(Integers.valueOf(8));
    }
    
    public byte[] getPublicKey() {
        return this.parameters.get(Integers.valueOf(12));
    }
    
    public static class Builder
    {
        private Hashtable parameters;
        
        public Builder() {
            this.parameters = new Hashtable();
        }
        
        public Builder(final Hashtable hashtable) {
            this.parameters = new Hashtable();
            final Enumeration<Integer> keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                final Integer n = keys.nextElement();
                this.parameters.put(n, hashtable.get(n));
            }
        }
        
        public Builder(final SkeinParameters skeinParameters) {
            this.parameters = new Hashtable();
            final Enumeration<Integer> keys = skeinParameters.parameters.keys();
            while (keys.hasMoreElements()) {
                final Integer n = keys.nextElement();
                this.parameters.put(n, skeinParameters.parameters.get(n));
            }
        }
        
        public SkeinParameters build() {
            return new SkeinParameters(this.parameters, null);
        }
        
        public Builder set(final int n, final byte[] array) {
            if (array == null) {
                throw new IllegalArgumentException("Parameter value must not be null.");
            }
            if (n != 0 && (n <= 4 || n >= 63 || n == 48)) {
                throw new IllegalArgumentException("Parameter types must be in the range 0,5..47,49..62.");
            }
            if (n != 4) {
                this.parameters.put(Integers.valueOf(n), array);
                return this;
            }
            throw new IllegalArgumentException("Parameter type 4 is reserved for internal use.");
        }
        
        public Builder setKey(final byte[] array) {
            return this.set(0, array);
        }
        
        public Builder setKeyIdentifier(final byte[] array) {
            return this.set(16, array);
        }
        
        public Builder setNonce(final byte[] array) {
            return this.set(20, array);
        }
        
        public Builder setPersonalisation(final Date date, final String s, final String s2) {
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD").format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(s);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(s2);
                outputStreamWriter.close();
                return this.set(8, byteArrayOutputStream.toByteArray());
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Byte I/O failed: ");
                sb.append(ex);
                throw new IllegalStateException(sb.toString());
            }
        }
        
        public Builder setPersonalisation(final Date date, final Locale locale, final String s, final String s2) {
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(byteArrayOutputStream, "UTF-8");
                outputStreamWriter.write(new SimpleDateFormat("YYYYMMDD", locale).format(date));
                outputStreamWriter.write(" ");
                outputStreamWriter.write(s);
                outputStreamWriter.write(" ");
                outputStreamWriter.write(s2);
                outputStreamWriter.close();
                return this.set(8, byteArrayOutputStream.toByteArray());
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Byte I/O failed: ");
                sb.append(ex);
                throw new IllegalStateException(sb.toString());
            }
        }
        
        public Builder setPersonalisation(final byte[] array) {
            return this.set(8, array);
        }
        
        public Builder setPublicKey(final byte[] array) {
            return this.set(12, array);
        }
    }
}
