package org.spongycastle.asn1.bc;

import java.util.function.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.asn1.*;

public class ObjectDataSequence extends ASN1Object implements Iterable<ASN1Encodable>
{
    private final ASN1Encodable[] dataSequence;
    
    private ObjectDataSequence(final ASN1Sequence asn1Sequence) {
        this.dataSequence = new ASN1Encodable[asn1Sequence.size()];
        int n = 0;
        while (true) {
            final ASN1Encodable[] dataSequence = this.dataSequence;
            if (n == dataSequence.length) {
                break;
            }
            dataSequence[n] = ObjectData.getInstance(asn1Sequence.getObjectAt(n));
            ++n;
        }
    }
    
    public ObjectDataSequence(final ObjectData[] array) {
        System.arraycopy(array, 0, this.dataSequence = new ASN1Encodable[array.length], 0, array.length);
    }
    
    public static ObjectDataSequence getInstance(final Object o) {
        if (o instanceof ObjectDataSequence) {
            return (ObjectDataSequence)o;
        }
        if (o != null) {
            return new ObjectDataSequence(ASN1Sequence.getInstance(o));
        }
        return null;
    }
    
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
    
    @Override
    public Iterator<ASN1Encodable> iterator() {
        return new Arrays.Iterator<ASN1Encodable>(this.dataSequence);
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)Iterable-CC.$default$spliterator((java.lang.Iterable)this);
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return new DERSequence(this.dataSequence);
    }
}
