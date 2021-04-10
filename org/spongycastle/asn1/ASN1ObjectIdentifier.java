package org.spongycastle.asn1;

import java.util.concurrent.*;
import java.math.*;
import org.spongycastle.util.*;
import java.io.*;

public class ASN1ObjectIdentifier extends ASN1Primitive
{
    private static final long LONG_LIMIT = 72057594037927808L;
    private static final ConcurrentMap<OidHandle, ASN1ObjectIdentifier> pool;
    private byte[] body;
    private final String identifier;
    
    static {
        pool = new ConcurrentHashMap<OidHandle, ASN1ObjectIdentifier>();
    }
    
    public ASN1ObjectIdentifier(final String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("'identifier' cannot be null");
        }
        if (isValidIdentifier(identifier)) {
            this.identifier = identifier;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("string ");
        sb.append(identifier);
        sb.append(" not an OID");
        throw new IllegalArgumentException(sb.toString());
    }
    
    ASN1ObjectIdentifier(final ASN1ObjectIdentifier asn1ObjectIdentifier, final String s) {
        if (isValidBranchID(s, 0)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(asn1ObjectIdentifier.getId());
            sb.append(".");
            sb.append(s);
            this.identifier = sb.toString();
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("string ");
        sb2.append(s);
        sb2.append(" not a valid OID branch");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    ASN1ObjectIdentifier(final byte[] array) {
        final StringBuffer sb = new StringBuffer();
        int i = 0;
        long n = 0L;
        BigInteger shiftLeft = null;
        int n2 = 1;
        while (i != array.length) {
            final int n3 = array[i] & 0xFF;
            if (n <= 72057594037927808L) {
                long n4 = n + (n3 & 0x7F);
                if ((n3 & 0x80) == 0x0) {
                    if (n2 != 0) {
                        if (n4 < 40L) {
                            sb.append('0');
                        }
                        else if (n4 < 80L) {
                            sb.append('1');
                            n4 -= 40L;
                        }
                        else {
                            sb.append('2');
                            n4 -= 80L;
                        }
                        n2 = 0;
                    }
                    sb.append('.');
                    sb.append(n4);
                    n = 0L;
                }
                else {
                    n = n4 << 7;
                }
            }
            else {
                BigInteger value;
                if ((value = shiftLeft) == null) {
                    value = BigInteger.valueOf(n);
                }
                BigInteger bigInteger = value.or(BigInteger.valueOf(n3 & 0x7F));
                if ((n3 & 0x80) == 0x0) {
                    if (n2 != 0) {
                        sb.append('2');
                        bigInteger = bigInteger.subtract(BigInteger.valueOf(80L));
                        n2 = 0;
                    }
                    sb.append('.');
                    sb.append(bigInteger);
                    n = 0L;
                    shiftLeft = null;
                }
                else {
                    shiftLeft = bigInteger.shiftLeft(7);
                }
            }
            ++i;
        }
        this.identifier = sb.toString();
        this.body = Arrays.clone(array);
    }
    
    private void doOutput(final ByteArrayOutputStream byteArrayOutputStream) {
        final OIDTokenizer oidTokenizer = new OIDTokenizer(this.identifier);
        final int n = Integer.parseInt(oidTokenizer.nextToken()) * 40;
        final String nextToken = oidTokenizer.nextToken();
        if (nextToken.length() <= 18) {
            this.writeField(byteArrayOutputStream, n + Long.parseLong(nextToken));
        }
        else {
            this.writeField(byteArrayOutputStream, new BigInteger(nextToken).add(BigInteger.valueOf(n)));
        }
        while (oidTokenizer.hasMoreTokens()) {
            final String nextToken2 = oidTokenizer.nextToken();
            if (nextToken2.length() <= 18) {
                this.writeField(byteArrayOutputStream, Long.parseLong(nextToken2));
            }
            else {
                this.writeField(byteArrayOutputStream, new BigInteger(nextToken2));
            }
        }
    }
    
    static ASN1ObjectIdentifier fromOctetString(final byte[] array) {
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        if ((asn1ObjectIdentifier = ASN1ObjectIdentifier.pool.get(new OidHandle(array))) == null) {
            asn1ObjectIdentifier = new ASN1ObjectIdentifier(array);
        }
        return asn1ObjectIdentifier;
    }
    
    private byte[] getBody() {
        synchronized (this) {
            if (this.body == null) {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                this.doOutput(byteArrayOutputStream);
                this.body = byteArrayOutputStream.toByteArray();
            }
            return this.body;
        }
    }
    
    public static ASN1ObjectIdentifier getInstance(final Object o) {
        if (o != null && !(o instanceof ASN1ObjectIdentifier)) {
            if (o instanceof ASN1Encodable) {
                final ASN1Encodable asn1Encodable = (ASN1Encodable)o;
                if (asn1Encodable.toASN1Primitive() instanceof ASN1ObjectIdentifier) {
                    return (ASN1ObjectIdentifier)asn1Encodable.toASN1Primitive();
                }
            }
            if (o instanceof byte[]) {
                final byte[] array = (byte[])o;
                try {
                    return (ASN1ObjectIdentifier)ASN1Primitive.fromByteArray(array);
                }
                catch (IOException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to construct object identifier from byte[]: ");
                    sb.append(ex.getMessage());
                    throw new IllegalArgumentException(sb.toString());
                }
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("illegal object in getInstance: ");
            sb2.append(o.getClass().getName());
            throw new IllegalArgumentException(sb2.toString());
        }
        return (ASN1ObjectIdentifier)o;
    }
    
    public static ASN1ObjectIdentifier getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        final ASN1Primitive object = asn1TaggedObject.getObject();
        if (!b && !(object instanceof ASN1ObjectIdentifier)) {
            return fromOctetString(ASN1OctetString.getInstance(asn1TaggedObject.getObject()).getOctets());
        }
        return getInstance(object);
    }
    
    private static boolean isValidBranchID(final String s, final int n) {
        int length = s.length();
        char char1;
        boolean b;
        do {
            b = false;
            while (true) {
                --length;
                if (length < n) {
                    return b;
                }
                char1 = s.charAt(length);
                if ('0' > char1 || char1 > '9') {
                    break;
                }
                b = true;
            }
        } while (char1 == '.' && b);
        return false;
    }
    
    private static boolean isValidIdentifier(final String s) {
        if (s.length() >= 3) {
            if (s.charAt(1) != '.') {
                return false;
            }
            final char char1 = s.charAt(0);
            if (char1 >= '0') {
                return char1 <= '2' && isValidBranchID(s, 2);
            }
        }
        return false;
    }
    
    private void writeField(final ByteArrayOutputStream byteArrayOutputStream, long n) {
        final byte[] array = new byte[9];
        final byte b = (byte)((int)n & 0x7F);
        int n2 = 8;
        array[8] = b;
        while (n >= 128L) {
            n >>= 7;
            --n2;
            array[n2] = (byte)(((int)n & 0x7F) | 0x80);
        }
        byteArrayOutputStream.write(array, n2, 9 - n2);
    }
    
    private void writeField(final ByteArrayOutputStream byteArrayOutputStream, BigInteger shiftRight) {
        final int n = (shiftRight.bitLength() + 6) / 7;
        if (n == 0) {
            byteArrayOutputStream.write(0);
            return;
        }
        final byte[] array = new byte[n];
        int i;
        int n2;
        for (n2 = (i = n - 1); i >= 0; --i) {
            array[i] = (byte)((shiftRight.intValue() & 0x7F) | 0x80);
            shiftRight = shiftRight.shiftRight(7);
        }
        array[n2] &= 0x7F;
        byteArrayOutputStream.write(array, 0, n);
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        return asn1Primitive == this || (asn1Primitive instanceof ASN1ObjectIdentifier && this.identifier.equals(((ASN1ObjectIdentifier)asn1Primitive).identifier));
    }
    
    public ASN1ObjectIdentifier branch(final String s) {
        return new ASN1ObjectIdentifier(this, s);
    }
    
    @Override
    void encode(final ASN1OutputStream asn1OutputStream) throws IOException {
        final byte[] body = this.getBody();
        asn1OutputStream.write(6);
        asn1OutputStream.writeLength(body.length);
        asn1OutputStream.write(body);
    }
    
    @Override
    int encodedLength() throws IOException {
        final int length = this.getBody().length;
        return StreamUtil.calculateBodyLength(length) + 1 + length;
    }
    
    public String getId() {
        return this.identifier;
    }
    
    @Override
    public int hashCode() {
        return this.identifier.hashCode();
    }
    
    public ASN1ObjectIdentifier intern() {
        final OidHandle oidHandle = new OidHandle(this.getBody());
        ASN1ObjectIdentifier asn1ObjectIdentifier = ASN1ObjectIdentifier.pool.get(oidHandle);
        if (asn1ObjectIdentifier == null && (asn1ObjectIdentifier = ASN1ObjectIdentifier.pool.putIfAbsent(oidHandle, this)) == null) {
            return this;
        }
        return asn1ObjectIdentifier;
    }
    
    @Override
    boolean isConstructed() {
        return false;
    }
    
    public boolean on(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        final String id = this.getId();
        final String id2 = asn1ObjectIdentifier.getId();
        return id.length() > id2.length() && id.charAt(id2.length()) == '.' && id.startsWith(id2);
    }
    
    @Override
    public String toString() {
        return this.getId();
    }
    
    private static class OidHandle
    {
        private final byte[] enc;
        private final int key;
        
        OidHandle(final byte[] enc) {
            this.key = Arrays.hashCode(enc);
            this.enc = enc;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o instanceof OidHandle && Arrays.areEqual(this.enc, ((OidHandle)o).enc);
        }
        
        @Override
        public int hashCode() {
            return this.key;
        }
    }
}
