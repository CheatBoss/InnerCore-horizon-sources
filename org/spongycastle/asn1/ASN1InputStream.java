package org.spongycastle.asn1;

import org.spongycastle.util.io.*;
import java.io.*;

public class ASN1InputStream extends FilterInputStream implements BERTags
{
    private final boolean lazyEvaluate;
    private final int limit;
    private final byte[][] tmpBuffers;
    
    public ASN1InputStream(final InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }
    
    public ASN1InputStream(final InputStream inputStream, final int n) {
        this(inputStream, n, false);
    }
    
    public ASN1InputStream(final InputStream inputStream, final int limit, final boolean lazyEvaluate) {
        super(inputStream);
        this.limit = limit;
        this.lazyEvaluate = lazyEvaluate;
        this.tmpBuffers = new byte[11][];
    }
    
    public ASN1InputStream(final InputStream inputStream, final boolean b) {
        this(inputStream, StreamUtil.findLimit(inputStream), b);
    }
    
    public ASN1InputStream(final byte[] array) {
        this(new ByteArrayInputStream(array), array.length);
    }
    
    public ASN1InputStream(final byte[] array, final boolean b) {
        this(new ByteArrayInputStream(array), array.length, b);
    }
    
    static ASN1Primitive createPrimitiveDERObject(final int n, final DefiniteLengthInputStream definiteLengthInputStream, final byte[][] array) throws IOException {
        if (n == 10) {
            return ASN1Enumerated.fromOctetString(getBuffer(definiteLengthInputStream, array));
        }
        if (n == 12) {
            return new DERUTF8String(definiteLengthInputStream.toByteArray());
        }
        if (n == 30) {
            return new DERBMPString(getBMPCharBuffer(definiteLengthInputStream));
        }
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("unknown tag ");
                        sb.append(n);
                        sb.append(" encountered");
                        throw new IOException(sb.toString());
                    }
                    case 28: {
                        return new DERUniversalString(definiteLengthInputStream.toByteArray());
                    }
                    case 27: {
                        return new DERGeneralString(definiteLengthInputStream.toByteArray());
                    }
                    case 26: {
                        return new DERVisibleString(definiteLengthInputStream.toByteArray());
                    }
                    case 25: {
                        return new DERGraphicString(definiteLengthInputStream.toByteArray());
                    }
                    case 24: {
                        return new ASN1GeneralizedTime(definiteLengthInputStream.toByteArray());
                    }
                    case 23: {
                        return new ASN1UTCTime(definiteLengthInputStream.toByteArray());
                    }
                    case 22: {
                        return new DERIA5String(definiteLengthInputStream.toByteArray());
                    }
                    case 21: {
                        return new DERVideotexString(definiteLengthInputStream.toByteArray());
                    }
                    case 20: {
                        return new DERT61String(definiteLengthInputStream.toByteArray());
                    }
                    case 19: {
                        return new DERPrintableString(definiteLengthInputStream.toByteArray());
                    }
                    case 18: {
                        return new DERNumericString(definiteLengthInputStream.toByteArray());
                    }
                }
                break;
            }
            case 6: {
                return ASN1ObjectIdentifier.fromOctetString(getBuffer(definiteLengthInputStream, array));
            }
            case 5: {
                return DERNull.INSTANCE;
            }
            case 4: {
                return new DEROctetString(definiteLengthInputStream.toByteArray());
            }
            case 3: {
                return ASN1BitString.fromInputStream(definiteLengthInputStream.getRemaining(), definiteLengthInputStream);
            }
            case 2: {
                return new ASN1Integer(definiteLengthInputStream.toByteArray(), false);
            }
            case 1: {
                return ASN1Boolean.fromOctetString(getBuffer(definiteLengthInputStream, array));
            }
        }
    }
    
    private static char[] getBMPCharBuffer(final DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        final int n = definiteLengthInputStream.getRemaining() / 2;
        final char[] array = new char[n];
        for (int i = 0; i < n; ++i) {
            final int read = definiteLengthInputStream.read();
            if (read < 0) {
                return array;
            }
            final int read2 = definiteLengthInputStream.read();
            if (read2 < 0) {
                return array;
            }
            array[i] = (char)(read << 8 | (read2 & 0xFF));
        }
        return array;
    }
    
    private static byte[] getBuffer(final DefiniteLengthInputStream definiteLengthInputStream, final byte[][] array) throws IOException {
        final int remaining = definiteLengthInputStream.getRemaining();
        if (definiteLengthInputStream.getRemaining() < array.length) {
            byte[] array2;
            if ((array2 = array[remaining]) == null) {
                array2 = new byte[remaining];
                array[remaining] = array2;
            }
            Streams.readFully(definiteLengthInputStream, array2);
            return array2;
        }
        return definiteLengthInputStream.toByteArray();
    }
    
    static int readLength(final InputStream inputStream, final int n) throws IOException {
        final int read = inputStream.read();
        if (read < 0) {
            throw new EOFException("EOF found when length expected");
        }
        if (read == 128) {
            return -1;
        }
        if (read <= 127) {
            return read;
        }
        final int n2 = read & 0x7F;
        if (n2 > 4) {
            final StringBuilder sb = new StringBuilder();
            sb.append("DER length more than 4 bytes: ");
            sb.append(n2);
            throw new IOException(sb.toString());
        }
        int i = 0;
        int n3 = 0;
        while (i < n2) {
            final int read2 = inputStream.read();
            if (read2 < 0) {
                throw new EOFException("EOF found reading length");
            }
            n3 = (n3 << 8) + read2;
            ++i;
        }
        if (n3 < 0) {
            throw new IOException("corrupted stream - negative length found");
        }
        if (n3 < n) {
            return n3;
        }
        throw new IOException("corrupted stream - out of bounds length found");
    }
    
    static int readTagNumber(final InputStream inputStream, int n) throws IOException {
        n &= 0x1F;
        if (n != 31) {
            return n;
        }
        int n2 = 0;
        n = inputStream.read();
        if ((n & 0x7F) == 0x0) {
            throw new IOException("corrupted stream - invalid high tag number found");
        }
        while (n >= 0 && (n & 0x80) != 0x0) {
            n2 = (n2 | (n & 0x7F)) << 7;
            n = inputStream.read();
        }
        if (n >= 0) {
            return (n & 0x7F) | n2;
        }
        throw new EOFException("EOF found inside tag value.");
    }
    
    ASN1EncodableVector buildDEREncodableVector(final DefiniteLengthInputStream definiteLengthInputStream) throws IOException {
        return new ASN1InputStream(definiteLengthInputStream).buildEncodableVector();
    }
    
    ASN1EncodableVector buildEncodableVector() throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        while (true) {
            final ASN1Primitive object = this.readObject();
            if (object == null) {
                break;
            }
            asn1EncodableVector.add(object);
        }
        return asn1EncodableVector;
    }
    
    protected ASN1Primitive buildObject(int i, int size, final int n) throws IOException {
        final int n2 = 0;
        final boolean b = (i & 0x20) != 0x0;
        final DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this, n);
        if ((i & 0x40) != 0x0) {
            return new DERApplicationSpecific(b, size, definiteLengthInputStream.toByteArray());
        }
        if ((i & 0x80) != 0x0) {
            return new ASN1StreamParser(definiteLengthInputStream).readTaggedObject(b, size);
        }
        if (!b) {
            return createPrimitiveDERObject(size, definiteLengthInputStream, this.tmpBuffers);
        }
        if (size == 4) {
            final ASN1EncodableVector buildDEREncodableVector = this.buildDEREncodableVector(definiteLengthInputStream);
            size = buildDEREncodableVector.size();
            final ASN1OctetString[] array = new ASN1OctetString[size];
            for (i = n2; i != size; ++i) {
                array[i] = (ASN1OctetString)buildDEREncodableVector.get(i);
            }
            return new BEROctetString(array);
        }
        if (size == 8) {
            return new DERExternal(this.buildDEREncodableVector(definiteLengthInputStream));
        }
        if (size != 16) {
            if (size == 17) {
                return DERFactory.createSet(this.buildDEREncodableVector(definiteLengthInputStream));
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown tag ");
            sb.append(size);
            sb.append(" encountered");
            throw new IOException(sb.toString());
        }
        else {
            if (this.lazyEvaluate) {
                return new LazyEncodedSequence(definiteLengthInputStream.toByteArray());
            }
            return DERFactory.createSequence(this.buildDEREncodableVector(definiteLengthInputStream));
        }
    }
    
    int getLimit() {
        return this.limit;
    }
    
    protected void readFully(final byte[] array) throws IOException {
        if (Streams.readFully(this, array) == array.length) {
            return;
        }
        throw new EOFException("EOF encountered in middle of object");
    }
    
    protected int readLength() throws IOException {
        return readLength(this, this.limit);
    }
    
    public ASN1Primitive readObject() throws IOException {
        final int read = this.read();
        if (read <= 0) {
            if (read != 0) {
                return null;
            }
            throw new IOException("unexpected end-of-contents marker");
        }
        else {
            final int tagNumber = readTagNumber(this, read);
            final boolean b = (read & 0x20) != 0x0;
            final int length = this.readLength();
            if (length < 0) {
                if (!b) {
                    throw new IOException("indefinite-length primitive encoding encountered");
                }
                final ASN1StreamParser asn1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this, this.limit), this.limit);
                if ((read & 0x40) != 0x0) {
                    return new BERApplicationSpecificParser(tagNumber, asn1StreamParser).getLoadedObject();
                }
                if ((read & 0x80) != 0x0) {
                    return new BERTaggedObjectParser(true, tagNumber, asn1StreamParser).getLoadedObject();
                }
                if (tagNumber == 4) {
                    return new BEROctetStringParser(asn1StreamParser).getLoadedObject();
                }
                if (tagNumber == 8) {
                    return new DERExternalParser(asn1StreamParser).getLoadedObject();
                }
                if (tagNumber == 16) {
                    return new BERSequenceParser(asn1StreamParser).getLoadedObject();
                }
                if (tagNumber == 17) {
                    return new BERSetParser(asn1StreamParser).getLoadedObject();
                }
                throw new IOException("unknown BER object encountered");
            }
            else {
                try {
                    return this.buildObject(read, tagNumber, length);
                }
                catch (IllegalArgumentException ex) {
                    throw new ASN1Exception("corrupted stream detected", ex);
                }
            }
        }
    }
}
