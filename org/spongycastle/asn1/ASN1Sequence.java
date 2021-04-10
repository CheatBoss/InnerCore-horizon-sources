package org.spongycastle.asn1;

import java.io.*;
import java.util.function.*;
import org.spongycastle.util.*;
import java.util.*;

public abstract class ASN1Sequence extends ASN1Primitive implements Iterable<ASN1Encodable>
{
    protected Vector seq;
    
    protected ASN1Sequence() {
        this.seq = new Vector();
    }
    
    protected ASN1Sequence(final ASN1Encodable asn1Encodable) {
        (this.seq = new Vector()).addElement(asn1Encodable);
    }
    
    protected ASN1Sequence(final ASN1EncodableVector asn1EncodableVector) {
        this.seq = new Vector();
        for (int i = 0; i != asn1EncodableVector.size(); ++i) {
            this.seq.addElement(asn1EncodableVector.get(i));
        }
    }
    
    protected ASN1Sequence(final ASN1Encodable[] array) {
        this.seq = new Vector();
        for (int i = 0; i != array.length; ++i) {
            this.seq.addElement(array[i]);
        }
    }
    
    public static ASN1Sequence getInstance(final Object o) {
        if (o == null || o instanceof ASN1Sequence) {
            return (ASN1Sequence)o;
        }
        if (o instanceof ASN1SequenceParser) {
            return getInstance(((ASN1SequenceParser)o).toASN1Primitive());
        }
        if (o instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[])o));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to construct sequence from byte[]: ");
                sb.append(ex.getMessage());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (o instanceof ASN1Encodable) {
            final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
            if (asn1Primitive instanceof ASN1Sequence) {
                return (ASN1Sequence)asn1Primitive;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unknown object in getInstance: ");
        sb2.append(o.getClass().getName());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static ASN1Sequence getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            if (asn1TaggedObject.isExplicit()) {
                return getInstance(asn1TaggedObject.getObject().toASN1Primitive());
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        }
        else if (asn1TaggedObject.isExplicit()) {
            if (asn1TaggedObject instanceof BERTaggedObject) {
                return new BERSequence(asn1TaggedObject.getObject());
            }
            return new DLSequence(asn1TaggedObject.getObject());
        }
        else {
            if (asn1TaggedObject.getObject() instanceof ASN1Sequence) {
                return (ASN1Sequence)asn1TaggedObject.getObject();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unknown object in getInstance: ");
            sb.append(asn1TaggedObject.getClass().getName());
            throw new IllegalArgumentException(sb.toString());
        }
    }
    
    private ASN1Encodable getNext(final Enumeration enumeration) {
        return enumeration.nextElement();
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        if (!(asn1Primitive instanceof ASN1Sequence)) {
            return false;
        }
        final ASN1Sequence asn1Sequence = (ASN1Sequence)asn1Primitive;
        if (this.size() != asn1Sequence.size()) {
            return false;
        }
        final Enumeration objects = this.getObjects();
        final Enumeration objects2 = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            final ASN1Encodable next = this.getNext(objects);
            final ASN1Encodable next2 = this.getNext(objects2);
            final ASN1Primitive asn1Primitive2 = next.toASN1Primitive();
            final ASN1Primitive asn1Primitive3 = next2.toASN1Primitive();
            if (asn1Primitive2 != asn1Primitive3) {
                if (asn1Primitive2.equals(asn1Primitive3)) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    abstract void encode(final ASN1OutputStream p0) throws IOException;
    
    @Override
    public void forEach(final Consumer<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Could not show original bytecode, likely due to the same error.
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public ASN1Encodable getObjectAt(final int n) {
        return this.seq.elementAt(n);
    }
    
    public Enumeration getObjects() {
        return this.seq.elements();
    }
    
    @Override
    public int hashCode() {
        final Enumeration objects = this.getObjects();
        int size = this.size();
        while (objects.hasMoreElements()) {
            size = (size * 17 ^ this.getNext(objects).hashCode());
        }
        return size;
    }
    
    @Override
    boolean isConstructed() {
        return true;
    }
    
    @Override
    public Iterator<ASN1Encodable> iterator() {
        return new Arrays.Iterator<ASN1Encodable>(this.toArray());
    }
    
    public ASN1SequenceParser parser() {
        return new ASN1SequenceParser() {
            private int index;
            private final int max = ASN1Sequence.this.size();
            
            @Override
            public ASN1Primitive getLoadedObject() {
                return ASN1Sequence.this;
            }
            
            @Override
            public ASN1Encodable readObject() throws IOException {
                final int index = this.index;
                if (index == this.max) {
                    return null;
                }
                final ASN1Sequence this$0 = ASN1Sequence.this;
                this.index = index + 1;
                final ASN1Encodable object = this$0.getObjectAt(index);
                if (object instanceof ASN1Sequence) {
                    return ((ASN1Sequence)object).parser();
                }
                Object parser = object;
                if (object instanceof ASN1Set) {
                    parser = ((ASN1Set)object).parser();
                }
                return (ASN1Encodable)parser;
            }
            
            @Override
            public ASN1Primitive toASN1Primitive() {
                return ASN1Sequence.this;
            }
        };
    }
    
    public int size() {
        return this.seq.size();
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)Iterable-CC.$default$spliterator((java.lang.Iterable)this);
    }
    
    public ASN1Encodable[] toArray() {
        final ASN1Encodable[] array = new ASN1Encodable[this.size()];
        for (int i = 0; i != this.size(); ++i) {
            array[i] = this.getObjectAt(i);
        }
        return array;
    }
    
    @Override
    ASN1Primitive toDERObject() {
        final DERSequence derSequence = new DERSequence();
        derSequence.seq = this.seq;
        return derSequence;
    }
    
    @Override
    ASN1Primitive toDLObject() {
        final DLSequence dlSequence = new DLSequence();
        dlSequence.seq = this.seq;
        return dlSequence;
    }
    
    @Override
    public String toString() {
        return this.seq.toString();
    }
}
