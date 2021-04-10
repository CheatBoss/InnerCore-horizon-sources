package org.mozilla.javascript.typedarrays;

import org.mozilla.javascript.*;
import java.util.stream.*;
import java.util.*;
import java.util.function.*;
import java.lang.reflect.*;

public abstract class NativeTypedArrayView<T> extends NativeArrayBufferView implements List<T>, RandomAccess, ExternalArrayData
{
    private static final int Id_BYTES_PER_ELEMENT = 11;
    private static final int Id_constructor = 1;
    private static final int Id_get = 2;
    private static final int Id_length = 10;
    private static final int Id_set = 3;
    private static final int Id_subarray = 4;
    private static final int MAX_INSTANCE_ID = 11;
    protected static final int MAX_PROTOTYPE_ID = 4;
    protected final int length;
    
    protected NativeTypedArrayView() {
        this.length = 0;
    }
    
    protected NativeTypedArrayView(final NativeArrayBuffer nativeArrayBuffer, final int n, final int length, final int n2) {
        super(nativeArrayBuffer, n, n2);
        this.length = length;
    }
    
    private NativeTypedArrayView js_constructor(final Context context, final Scriptable scriptable, final Object[] array) {
        final int n = 0;
        int i = 0;
        if (!NativeArrayBufferView.isArg(array, 0)) {
            return this.construct(NativeArrayBuffer.EMPTY_BUFFER, 0, 0);
        }
        if (array[0] instanceof Number || array[0] instanceof String) {
            final int int32 = ScriptRuntime.toInt32(array[0]);
            return this.construct(this.makeArrayBuffer(context, scriptable, this.getBytesPerElement() * int32), 0, int32);
        }
        if (array[0] instanceof NativeTypedArrayView) {
            final NativeTypedArrayView nativeTypedArrayView = (NativeTypedArrayView)array[0];
            final NativeTypedArrayView construct = this.construct(this.makeArrayBuffer(context, scriptable, nativeTypedArrayView.length * this.getBytesPerElement()), 0, nativeTypedArrayView.length);
            while (i < nativeTypedArrayView.length) {
                construct.js_set(i, nativeTypedArrayView.js_get(i));
                ++i;
            }
            return construct;
        }
        if (array[0] instanceof NativeArrayBuffer) {
            final NativeArrayBuffer nativeArrayBuffer = (NativeArrayBuffer)array[0];
            int int33 = n;
            if (NativeArrayBufferView.isArg(array, 1)) {
                int33 = ScriptRuntime.toInt32(array[1]);
            }
            int n2;
            if (NativeArrayBufferView.isArg(array, 2)) {
                n2 = ScriptRuntime.toInt32(array[2]) * this.getBytesPerElement();
            }
            else {
                n2 = nativeArrayBuffer.getLength() - int33;
            }
            if (int33 < 0 || int33 > nativeArrayBuffer.buffer.length) {
                throw ScriptRuntime.constructError("RangeError", "offset out of range");
            }
            if (n2 < 0 || int33 + n2 > nativeArrayBuffer.buffer.length) {
                throw ScriptRuntime.constructError("RangeError", "length out of range");
            }
            if (int33 % this.getBytesPerElement() != 0) {
                throw ScriptRuntime.constructError("RangeError", "offset must be a multiple of the byte size");
            }
            if (n2 % this.getBytesPerElement() != 0) {
                throw ScriptRuntime.constructError("RangeError", "offset and buffer must be a multiple of the byte size");
            }
            return this.construct(nativeArrayBuffer, int33, n2 / this.getBytesPerElement());
        }
        else {
            if (array[0] instanceof NativeArray) {
                final List list = (List)array[0];
                final NativeTypedArrayView construct2 = this.construct(this.makeArrayBuffer(context, scriptable, list.size() * this.getBytesPerElement()), 0, list.size());
                int n3 = 0;
                final Iterator<Object> iterator = list.iterator();
                while (iterator.hasNext()) {
                    construct2.js_set(n3, iterator.next());
                    ++n3;
                }
                return construct2;
            }
            throw ScriptRuntime.constructError("Error", "invalid argument");
        }
    }
    
    private Object js_subarray(final Context context, final Scriptable scriptable, int n, int max) {
        if (n < 0) {
            n += this.length;
        }
        if (max < 0) {
            max += this.length;
        }
        n = Math.max(0, n);
        max = Math.max(0, Math.min(this.length, max) - n);
        n = Math.min(this.getBytesPerElement() * n, this.arrayBuffer.getLength());
        return context.newObject(scriptable, this.getClassName(), new Object[] { this.arrayBuffer, n, max });
    }
    
    private NativeArrayBuffer makeArrayBuffer(final Context context, final Scriptable scriptable, final int n) {
        return (NativeArrayBuffer)context.newObject(scriptable, "ArrayBuffer", new Object[] { n });
    }
    
    private void setRange(final NativeArray nativeArray, int n) {
        if (n > this.length) {
            throw ScriptRuntime.constructError("RangeError", "offset out of range");
        }
        if (nativeArray.size() + n > this.length) {
            throw ScriptRuntime.constructError("RangeError", "offset + length out of range");
        }
        final Iterator iterator = nativeArray.iterator();
        while (iterator.hasNext()) {
            this.js_set(n, iterator.next());
            ++n;
        }
    }
    
    private void setRange(final NativeTypedArrayView nativeTypedArrayView, final int n) {
        if (n >= this.length) {
            throw ScriptRuntime.constructError("RangeError", "offset out of range");
        }
        if (nativeTypedArrayView.length > this.length - n) {
            throw ScriptRuntime.constructError("RangeError", "source array too long");
        }
        final NativeArrayBuffer arrayBuffer = nativeTypedArrayView.arrayBuffer;
        final NativeArrayBuffer arrayBuffer2 = this.arrayBuffer;
        int i = 0;
        final int n2 = 0;
        if (arrayBuffer == arrayBuffer2) {
            final Object[] array = new Object[nativeTypedArrayView.length];
            for (int j = 0; j < nativeTypedArrayView.length; ++j) {
                array[j] = nativeTypedArrayView.js_get(j);
            }
            for (int k = n2; k < nativeTypedArrayView.length; ++k) {
                this.js_set(k + n, array[k]);
            }
            return;
        }
        while (i < nativeTypedArrayView.length) {
            this.js_set(i + n, nativeTypedArrayView.js_get(i));
            ++i;
        }
    }
    
    @Override
    public void add(final int n, final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean add(final T t) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int n, final Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> collection) {
        throw new UnsupportedOperationException();
    }
    
    protected boolean checkIndex(final int n) {
        return n < 0 || n >= this.length;
    }
    
    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }
    
    protected abstract NativeTypedArrayView construct(final NativeArrayBuffer p0, final int p1, final int p2);
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) >= 0;
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        final Iterator<?> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (!this.contains(iterator.next())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void delete(final int n) {
    }
    
    @Override
    public boolean equals(final Object o) {
    Label_0018_Outer:
        while (true) {
        Label_0018:
            while (true) {
                Label_0058: {
                    try {
                        final NativeTypedArrayView nativeTypedArrayView = (NativeTypedArrayView)o;
                        if (this.length != nativeTypedArrayView.length) {
                            return false;
                        }
                        break Label_0058;
                        while (true) {
                            return false;
                            Label_0046: {
                                final int n = n + 1;
                            }
                            break Label_0018;
                            Label_0053:
                            return true;
                            continue Label_0018_Outer;
                        }
                    }
                    // iftrue(Label_0046:, this.js_get(n).equals(nativeTypedArrayView.js_get(n)))
                    // iftrue(Label_0053:, n >= this.length)
                    catch (ClassCastException ex) {
                        return false;
                    }
                }
                final int n = 0;
                continue Label_0018;
            }
        }
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (!idFunctionObject.hasTag(this.getClassName())) {
            return super.execIdCall(idFunctionObject, context, scriptable, scriptable2, array);
        }
        final int methodId = idFunctionObject.methodId();
        switch (methodId) {
            default: {
                throw new IllegalArgumentException(String.valueOf(methodId));
            }
            case 4: {
                if (array.length > 0) {
                    final NativeTypedArrayView realThis = this.realThis(scriptable2, idFunctionObject);
                    final int int32 = ScriptRuntime.toInt32(array[0]);
                    int n;
                    if (NativeArrayBufferView.isArg(array, 1)) {
                        n = ScriptRuntime.toInt32(array[1]);
                    }
                    else {
                        n = realThis.length;
                    }
                    return realThis.js_subarray(context, scriptable, int32, n);
                }
                throw ScriptRuntime.constructError("Error", "invalid arguments");
            }
            case 3: {
                if (array.length > 0) {
                    final NativeTypedArrayView realThis2 = this.realThis(scriptable2, idFunctionObject);
                    if (array[0] instanceof NativeTypedArrayView) {
                        int int33;
                        if (NativeArrayBufferView.isArg(array, 1)) {
                            int33 = ScriptRuntime.toInt32(array[1]);
                        }
                        else {
                            int33 = 0;
                        }
                        realThis2.setRange((NativeTypedArrayView)array[0], int33);
                        return Undefined.instance;
                    }
                    if (array[0] instanceof NativeArray) {
                        int int34;
                        if (NativeArrayBufferView.isArg(array, 1)) {
                            int34 = ScriptRuntime.toInt32(array[1]);
                        }
                        else {
                            int34 = 0;
                        }
                        realThis2.setRange((NativeArray)array[0], int34);
                        return Undefined.instance;
                    }
                    if (array[0] instanceof Scriptable) {
                        return Undefined.instance;
                    }
                    if (NativeArrayBufferView.isArg(array, 2)) {
                        return realThis2.js_set(ScriptRuntime.toInt32(array[0]), array[1]);
                    }
                }
                throw ScriptRuntime.constructError("Error", "invalid arguments");
            }
            case 2: {
                if (array.length > 0) {
                    return this.realThis(scriptable2, idFunctionObject).js_get(ScriptRuntime.toInt32(array[0]));
                }
                throw ScriptRuntime.constructError("Error", "invalid arguments");
            }
            case 1: {
                return this.js_constructor(context, scriptable, array);
            }
        }
    }
    
    @Override
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
        idFunctionObject.put("BYTES_PER_ELEMENT", idFunctionObject, ScriptRuntime.wrapInt(this.getBytesPerElement()));
    }
    
    @Override
    protected int findInstanceIdInfo(final String s) {
        int n = 0;
        String s2 = null;
        final int length = s.length();
        if (length == 6) {
            s2 = "length";
            n = 10;
        }
        else if (length == 17) {
            s2 = "BYTES_PER_ELEMENT";
            n = 11;
        }
        int n2 = n;
        if (s2 != null) {
            n2 = n;
            if (s2 != s) {
                n2 = n;
                if (!s2.equals(s)) {
                    n2 = 0;
                }
            }
        }
        if (n2 == 0) {
            return super.findInstanceIdInfo(s);
        }
        return IdScriptableObject.instanceIdInfo(5, n2);
    }
    
    @Override
    protected int findPrototypeId(final String s) {
        final boolean b = false;
        final String s2 = null;
        final int length = s.length();
        int n;
        String s3;
        if (length == 3) {
            final char char1 = s.charAt(0);
            if (char1 == 'g') {
                n = (b ? 1 : 0);
                s3 = s2;
                if (s.charAt(2) == 't') {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (s.charAt(1) == 'e') {
                        return 2;
                    }
                }
            }
            else {
                n = (b ? 1 : 0);
                s3 = s2;
                if (char1 == 's') {
                    n = (b ? 1 : 0);
                    s3 = s2;
                    if (s.charAt(2) == 't') {
                        n = (b ? 1 : 0);
                        s3 = s2;
                        if (s.charAt(1) == 'e') {
                            return 3;
                        }
                    }
                }
            }
        }
        else if (length == 8) {
            s3 = "subarray";
            n = 4;
        }
        else {
            n = (b ? 1 : 0);
            s3 = s2;
            if (length == 11) {
                s3 = "constructor";
                n = 1;
            }
        }
        int n2 = n;
        if (s3 != null) {
            n2 = n;
            if (s3 != s) {
                n2 = n;
                if (!s3.equals(s)) {
                    n2 = 0;
                }
            }
        }
        return n2;
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
    public Object get(final int n, final Scriptable scriptable) {
        return this.js_get(n);
    }
    
    @Override
    public Object getArrayElement(final int n) {
        return this.js_get(n);
    }
    
    @Override
    public int getArrayLength() {
        return this.length;
    }
    
    public abstract int getBytesPerElement();
    
    @Override
    public Object[] getIds() {
        final Object[] array = new Object[this.length];
        for (int i = 0; i < this.length; ++i) {
            array[i] = i;
        }
        return array;
    }
    
    @Override
    protected String getInstanceIdName(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdName(n);
            }
            case 11: {
                return "BYTES_PER_ELEMENT";
            }
            case 10: {
                return "length";
            }
        }
    }
    
    @Override
    protected Object getInstanceIdValue(final int n) {
        switch (n) {
            default: {
                return super.getInstanceIdValue(n);
            }
            case 11: {
                return ScriptRuntime.wrapInt(this.getBytesPerElement());
            }
            case 10: {
                return ScriptRuntime.wrapInt(this.length);
            }
        }
    }
    
    @Override
    protected int getMaxInstanceId() {
        return 11;
    }
    
    @Override
    public boolean has(final int n, final Scriptable scriptable) {
        return n > 0 && n < this.length;
    }
    
    @Override
    public int hashCode() {
        int n = 0;
        for (int i = 0; i < this.length; ++i) {
            n += this.js_get(i).hashCode();
        }
        return 0;
    }
    
    @Override
    public int indexOf(final Object o) {
        for (int i = 0; i < this.length; ++i) {
            if (o.equals(this.js_get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    protected void initPrototypeId(final int n) {
        int n2 = 0;
        String s = null;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 4: {
                n2 = 2;
                s = "subarray";
                break;
            }
            case 3: {
                n2 = 2;
                s = "set";
                break;
            }
            case 2: {
                n2 = 1;
                s = "get";
                break;
            }
            case 1: {
                n2 = 1;
                s = "constructor";
                break;
            }
        }
        this.initPrototypeMethod(this.getClassName(), n, s, n2);
    }
    
    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new NativeTypedArrayIterator<T>(this, 0);
    }
    
    protected abstract Object js_get(final int p0);
    
    protected abstract Object js_set(final int p0, final Object p1);
    
    @Override
    public int lastIndexOf(final Object o) {
        for (int i = this.length - 1; i >= 0; --i) {
            if (o.equals(this.js_get(i))) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public ListIterator<T> listIterator() {
        return new NativeTypedArrayIterator<T>(this, 0);
    }
    
    @Override
    public ListIterator<T> listIterator(final int n) {
        if (this.checkIndex(n)) {
            throw new IndexOutOfBoundsException();
        }
        return new NativeTypedArrayIterator<T>(this, n);
    }
    
    @Override
    public Stream<Object> parallelStream() {
        return (Stream<Object>)Collection-CC.$default$parallelStream((Collection)this);
    }
    
    @Override
    public void put(final int n, final Scriptable scriptable, final Object o) {
        this.js_set(n, o);
    }
    
    protected abstract NativeTypedArrayView realThis(final Scriptable p0, final IdFunctionObject p1);
    
    @Override
    public T remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean remove(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeIf(final Predicate<?> p0) {
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
    public void replaceAll(final UnaryOperator<Object> unaryOperator) {
        List-CC.$default$replaceAll((List)this, (UnaryOperator)unaryOperator);
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void setArrayElement(final int n, final Object o) {
        this.js_set(n, o);
    }
    
    @Override
    public int size() {
        return this.length;
    }
    
    @Override
    public void sort(final Comparator<?> p0) {
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
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)List-CC.$default$spliterator((List)this);
    }
    
    @Override
    public Stream<Object> stream() {
        return (Stream<Object>)Collection-CC.$default$stream((Collection)this);
    }
    
    @Override
    public List<T> subList(final int n, final int n2) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        final Object[] array = new Object[this.length];
        for (int i = 0; i < this.length; ++i) {
            array[i] = this.js_get(i);
        }
        return array;
    }
    
    public <T> T[] toArray(final IntFunction<T[]> intFunction) {
        return (T[])Collection-CC.$default$toArray((Collection)this, (IntFunction)intFunction);
    }
    
    @Override
    public <U> U[] toArray(U[] array) {
        if (array.length < this.length) {
            array = (U[])Array.newInstance(array.getClass().getComponentType(), this.length);
        }
        int i = 0;
        while (i < this.length) {
            try {
                array[i] = (U)this.js_get(i);
                ++i;
                continue;
            }
            catch (ClassCastException ex) {
                throw new ArrayStoreException();
            }
            break;
        }
        return array;
    }
}
