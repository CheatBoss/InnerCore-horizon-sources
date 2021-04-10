package org.spongycastle.asn1;

import java.io.*;
import java.util.function.*;
import org.spongycastle.util.*;
import java.util.*;

public abstract class ASN1Set extends ASN1Primitive implements Iterable<ASN1Encodable>
{
    private boolean isSorted;
    private Vector set;
    
    protected ASN1Set() {
        this.set = new Vector();
        this.isSorted = false;
    }
    
    protected ASN1Set(final ASN1Encodable asn1Encodable) {
        final Vector<ASN1Encodable> set = new Vector<ASN1Encodable>();
        this.set = set;
        this.isSorted = false;
        set.addElement(asn1Encodable);
    }
    
    protected ASN1Set(final ASN1EncodableVector asn1EncodableVector, final boolean b) {
        this.set = new Vector();
        int i = 0;
        this.isSorted = false;
        while (i != asn1EncodableVector.size()) {
            this.set.addElement(asn1EncodableVector.get(i));
            ++i;
        }
        if (b) {
            this.sort();
        }
    }
    
    protected ASN1Set(final ASN1Encodable[] array, final boolean b) {
        this.set = new Vector();
        int i = 0;
        this.isSorted = false;
        while (i != array.length) {
            this.set.addElement(array[i]);
            ++i;
        }
        if (b) {
            this.sort();
        }
    }
    
    private byte[] getDEREncoded(final ASN1Encodable asn1Encodable) {
        try {
            return asn1Encodable.toASN1Primitive().getEncoded("DER");
        }
        catch (IOException ex) {
            throw new IllegalArgumentException("cannot encode object added to SET");
        }
    }
    
    public static ASN1Set getInstance(final Object o) {
        if (o == null || o instanceof ASN1Set) {
            return (ASN1Set)o;
        }
        if (o instanceof ASN1SetParser) {
            return getInstance(((ASN1SetParser)o).toASN1Primitive());
        }
        if (o instanceof byte[]) {
            try {
                return getInstance(ASN1Primitive.fromByteArray((byte[])o));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("failed to construct set from byte[]: ");
                sb.append(ex.getMessage());
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (o instanceof ASN1Encodable) {
            final ASN1Primitive asn1Primitive = ((ASN1Encodable)o).toASN1Primitive();
            if (asn1Primitive instanceof ASN1Set) {
                return (ASN1Set)asn1Primitive;
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unknown object in getInstance: ");
        sb2.append(o.getClass().getName());
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static ASN1Set getInstance(final ASN1TaggedObject asn1TaggedObject, final boolean b) {
        if (b) {
            if (asn1TaggedObject.isExplicit()) {
                return (ASN1Set)asn1TaggedObject.getObject();
            }
            throw new IllegalArgumentException("object implicit - explicit expected.");
        }
        else if (asn1TaggedObject.isExplicit()) {
            if (asn1TaggedObject instanceof BERTaggedObject) {
                return new BERSet(asn1TaggedObject.getObject());
            }
            return new DLSet(asn1TaggedObject.getObject());
        }
        else {
            if (asn1TaggedObject.getObject() instanceof ASN1Set) {
                return (ASN1Set)asn1TaggedObject.getObject();
            }
            if (!(asn1TaggedObject.getObject() instanceof ASN1Sequence)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("unknown object in getInstance: ");
                sb.append(asn1TaggedObject.getClass().getName());
                throw new IllegalArgumentException(sb.toString());
            }
            final ASN1Sequence asn1Sequence = (ASN1Sequence)asn1TaggedObject.getObject();
            if (asn1TaggedObject instanceof BERTaggedObject) {
                return new BERSet(asn1Sequence.toArray());
            }
            return new DLSet(asn1Sequence.toArray());
        }
    }
    
    private ASN1Encodable getNext(final Enumeration enumeration) {
        ASN1Encodable instance;
        if ((instance = enumeration.nextElement()) == null) {
            instance = DERNull.INSTANCE;
        }
        return instance;
    }
    
    private boolean lessThanOrEqual(final byte[] array, final byte[] array2) {
        final int min = Math.min(array.length, array2.length);
        final boolean b = false;
        boolean b2 = false;
        for (int i = 0; i != min; ++i) {
            if (array[i] != array2[i]) {
                if ((array[i] & 0xFF) < (array2[i] & 0xFF)) {
                    b2 = true;
                }
                return b2;
            }
        }
        boolean b3 = b;
        if (min == array.length) {
            b3 = true;
        }
        return b3;
    }
    
    @Override
    boolean asn1Equals(final ASN1Primitive asn1Primitive) {
        if (!(asn1Primitive instanceof ASN1Set)) {
            return false;
        }
        final ASN1Set set = (ASN1Set)asn1Primitive;
        if (this.size() != set.size()) {
            return false;
        }
        final Enumeration objects = this.getObjects();
        final Enumeration objects2 = set.getObjects();
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
        return this.set.elementAt(n);
    }
    
    public Enumeration getObjects() {
        return this.set.elements();
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
    
    public ASN1SetParser parser() {
        return new ASN1SetParser() {
            private int index;
            private final int max = ASN1Set.this.size();
            
            @Override
            public ASN1Primitive getLoadedObject() {
                return ASN1Set.this;
            }
            
            @Override
            public ASN1Encodable readObject() throws IOException {
                final int index = this.index;
                if (index == this.max) {
                    return null;
                }
                final ASN1Set this$0 = ASN1Set.this;
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
                return ASN1Set.this;
            }
        };
    }
    
    public int size() {
        return this.set.size();
    }
    
    protected void sort() {
        if (!this.isSorted) {
            this.isSorted = true;
            if (this.set.size() > 1) {
                int n = this.set.size() - 1;
                int i = 1;
                while (i != 0) {
                    final Vector set = this.set;
                    int j = 0;
                    byte[] derEncoded = this.getDEREncoded(set.elementAt(0));
                    int n2 = 0;
                    i = 0;
                    while (j != n) {
                        final Vector set2 = this.set;
                        final int n3 = j + 1;
                        final byte[] derEncoded2 = this.getDEREncoded(set2.elementAt(n3));
                        if (this.lessThanOrEqual(derEncoded, derEncoded2)) {
                            derEncoded = derEncoded2;
                        }
                        else {
                            final Object element = this.set.elementAt(j);
                            final Vector set3 = this.set;
                            set3.setElementAt(set3.elementAt(n3), j);
                            this.set.setElementAt(element, n3);
                            i = 1;
                            n2 = j;
                        }
                        j = n3;
                    }
                    n = n2;
                }
            }
        }
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
        if (this.isSorted) {
            final DERSet set = new DERSet();
            set.set = this.set;
            return set;
        }
        final Vector<Object> set2 = new Vector<Object>();
        for (int i = 0; i != this.set.size(); ++i) {
            set2.addElement(this.set.elementAt(i));
        }
        final DERSet set3 = new DERSet();
        set3.set = set2;
        set3.sort();
        return set3;
    }
    
    @Override
    ASN1Primitive toDLObject() {
        final DLSet set = new DLSet();
        set.set = this.set;
        return set;
    }
    
    @Override
    public String toString() {
        return this.set.toString();
    }
}
