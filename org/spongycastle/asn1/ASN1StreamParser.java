package org.spongycastle.asn1;

import java.io.*;

public class ASN1StreamParser
{
    private final InputStream _in;
    private final int _limit;
    private final byte[][] tmpBuffers;
    
    public ASN1StreamParser(final InputStream inputStream) {
        this(inputStream, StreamUtil.findLimit(inputStream));
    }
    
    public ASN1StreamParser(final InputStream in, final int limit) {
        this._in = in;
        this._limit = limit;
        this.tmpBuffers = new byte[11][];
    }
    
    public ASN1StreamParser(final byte[] array) {
        this(new ByteArrayInputStream(array), array.length);
    }
    
    private void set00Check(final boolean eofOn00) {
        final InputStream in = this._in;
        if (in instanceof IndefiniteLengthInputStream) {
            ((IndefiniteLengthInputStream)in).setEofOn00(eofOn00);
        }
    }
    
    ASN1Encodable readImplicit(final boolean b, final int n) throws IOException {
        if (!(this._in instanceof IndefiniteLengthInputStream)) {
            if (b) {
                if (n == 4) {
                    return new BEROctetStringParser(this);
                }
                if (n == 16) {
                    return new DERSequenceParser(this);
                }
                if (n == 17) {
                    return new DERSetParser(this);
                }
            }
            else {
                if (n == 4) {
                    return new DEROctetStringParser((DefiniteLengthInputStream)this._in);
                }
                if (n == 16) {
                    throw new ASN1Exception("sets must use constructed encoding (see X.690 8.11.1/8.12.1)");
                }
                if (n == 17) {
                    throw new ASN1Exception("sequences must use constructed encoding (see X.690 8.9.1/8.10.1)");
                }
            }
            throw new ASN1Exception("implicit tagging not implemented");
        }
        if (b) {
            return this.readIndef(n);
        }
        throw new IOException("indefinite-length primitive encoding encountered");
    }
    
    ASN1Encodable readIndef(final int n) throws IOException {
        if (n == 4) {
            return new BEROctetStringParser(this);
        }
        if (n == 8) {
            return new DERExternalParser(this);
        }
        if (n == 16) {
            return new BERSequenceParser(this);
        }
        if (n == 17) {
            return new BERSetParser(this);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("unknown BER object encountered: 0x");
        sb.append(Integer.toHexString(n));
        throw new ASN1Exception(sb.toString());
    }
    
    public ASN1Encodable readObject() throws IOException {
        final int read = this._in.read();
        if (read == -1) {
            return null;
        }
        boolean b = false;
        this.set00Check(false);
        final int tagNumber = ASN1InputStream.readTagNumber(this._in, read);
        if ((read & 0x20) != 0x0) {
            b = true;
        }
        final int length = ASN1InputStream.readLength(this._in, this._limit);
        if (length < 0) {
            if (!b) {
                throw new IOException("indefinite-length primitive encoding encountered");
            }
            final ASN1StreamParser asn1StreamParser = new ASN1StreamParser(new IndefiniteLengthInputStream(this._in, this._limit), this._limit);
            if ((read & 0x40) != 0x0) {
                return new BERApplicationSpecificParser(tagNumber, asn1StreamParser);
            }
            if ((read & 0x80) != 0x0) {
                return new BERTaggedObjectParser(true, tagNumber, asn1StreamParser);
            }
            return asn1StreamParser.readIndef(tagNumber);
        }
        else {
            final DefiniteLengthInputStream definiteLengthInputStream = new DefiniteLengthInputStream(this._in, length);
            if ((read & 0x40) != 0x0) {
                return new DERApplicationSpecific(b, tagNumber, definiteLengthInputStream.toByteArray());
            }
            if ((read & 0x80) != 0x0) {
                return new BERTaggedObjectParser(b, tagNumber, new ASN1StreamParser(definiteLengthInputStream));
            }
            if (!b) {
                if (tagNumber != 4) {
                    try {
                        return ASN1InputStream.createPrimitiveDERObject(tagNumber, definiteLengthInputStream, this.tmpBuffers);
                    }
                    catch (IllegalArgumentException ex) {
                        throw new ASN1Exception("corrupted stream detected", ex);
                    }
                }
                return new DEROctetStringParser(definiteLengthInputStream);
            }
            if (tagNumber == 4) {
                return new BEROctetStringParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            if (tagNumber == 8) {
                return new DERExternalParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            if (tagNumber == 16) {
                return new DERSequenceParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            if (tagNumber == 17) {
                return new DERSetParser(new ASN1StreamParser(definiteLengthInputStream));
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown tag ");
            sb.append(tagNumber);
            sb.append(" encountered");
            throw new IOException(sb.toString());
        }
    }
    
    ASN1Primitive readTaggedObject(final boolean b, final int n) throws IOException {
        if (!b) {
            return new DERTaggedObject(false, n, new DEROctetString(((DefiniteLengthInputStream)this._in).toByteArray()));
        }
        final ASN1EncodableVector vector = this.readVector();
        if (this._in instanceof IndefiniteLengthInputStream) {
            if (vector.size() == 1) {
                return new BERTaggedObject(true, n, vector.get(0));
            }
            return new BERTaggedObject(false, n, BERFactory.createSequence(vector));
        }
        else {
            if (vector.size() == 1) {
                return new DERTaggedObject(true, n, vector.get(0));
            }
            return new DERTaggedObject(false, n, DERFactory.createSequence(vector));
        }
    }
    
    ASN1EncodableVector readVector() throws IOException {
        final ASN1EncodableVector asn1EncodableVector = new ASN1EncodableVector();
        while (true) {
            final ASN1Encodable object = this.readObject();
            if (object == null) {
                break;
            }
            ASN1Primitive asn1Primitive;
            if (object instanceof InMemoryRepresentable) {
                asn1Primitive = ((InMemoryRepresentable)object).getLoadedObject();
            }
            else {
                asn1Primitive = object.toASN1Primitive();
            }
            asn1EncodableVector.add(asn1Primitive);
        }
        return asn1EncodableVector;
    }
}
