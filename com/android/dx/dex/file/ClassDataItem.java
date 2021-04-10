package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import java.io.*;
import java.util.*;
import com.android.dx.util.*;

public final class ClassDataItem extends OffsettedItem
{
    private final ArrayList<EncodedMethod> directMethods;
    private byte[] encodedForm;
    private final ArrayList<EncodedField> instanceFields;
    private final ArrayList<EncodedField> staticFields;
    private final HashMap<EncodedField, Constant> staticValues;
    private CstArray staticValuesConstant;
    private final CstType thisClass;
    private final ArrayList<EncodedMethod> virtualMethods;
    
    public ClassDataItem(final CstType thisClass) {
        super(1, -1);
        if (thisClass == null) {
            throw new NullPointerException("thisClass == null");
        }
        this.thisClass = thisClass;
        this.staticFields = new ArrayList<EncodedField>(20);
        this.staticValues = new HashMap<EncodedField, Constant>(40);
        this.instanceFields = new ArrayList<EncodedField>(20);
        this.directMethods = new ArrayList<EncodedMethod>(20);
        this.virtualMethods = new ArrayList<EncodedMethod>(20);
        this.staticValuesConstant = null;
    }
    
    private static void encodeList(final DexFile dexFile, final AnnotatedOutput annotatedOutput, final String s, final ArrayList<? extends EncodedMember> list) {
        final int size = list.size();
        int encode = 0;
        if (size == 0) {
            return;
        }
        final boolean annotates = annotatedOutput.annotates();
        int i = 0;
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append("  ");
            sb.append(s);
            sb.append(":");
            annotatedOutput.annotate(0, sb.toString());
        }
        while (i < size) {
            encode = ((EncodedMember)list.get(i)).encode(dexFile, annotatedOutput, encode, i);
            ++i;
        }
    }
    
    private void encodeOutput(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final boolean annotates = annotatedOutput.annotates();
        if (annotates) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.offsetString());
            sb.append(" class data for ");
            sb.append(this.thisClass.toHuman());
            annotatedOutput.annotate(0, sb.toString());
        }
        encodeSize(dexFile, annotatedOutput, "static_fields", this.staticFields.size());
        encodeSize(dexFile, annotatedOutput, "instance_fields", this.instanceFields.size());
        encodeSize(dexFile, annotatedOutput, "direct_methods", this.directMethods.size());
        encodeSize(dexFile, annotatedOutput, "virtual_methods", this.virtualMethods.size());
        encodeList(dexFile, annotatedOutput, "static_fields", this.staticFields);
        encodeList(dexFile, annotatedOutput, "instance_fields", this.instanceFields);
        encodeList(dexFile, annotatedOutput, "direct_methods", this.directMethods);
        encodeList(dexFile, annotatedOutput, "virtual_methods", this.virtualMethods);
        if (annotates) {
            annotatedOutput.endAnnotation();
        }
    }
    
    private static void encodeSize(final DexFile dexFile, final AnnotatedOutput annotatedOutput, final String s, final int n) {
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("_size:");
            annotatedOutput.annotate(String.format("  %-21s %08x", sb.toString(), n));
        }
        annotatedOutput.writeUleb128(n);
    }
    
    private CstArray makeStaticValuesConstant() {
        Collections.sort(this.staticFields);
        int i;
        for (i = this.staticFields.size(); i > 0; --i) {
            final Constant constant = this.staticValues.get(this.staticFields.get(i - 1));
            if (constant instanceof CstLiteralBits) {
                if (((CstLiteralBits)constant).getLongBits() != 0L) {
                    break;
                }
            }
            else if (constant != null) {
                break;
            }
        }
        if (i == 0) {
            return null;
        }
        final CstArray.List list = new CstArray.List(i);
        for (int j = 0; j < i; ++j) {
            final EncodedField encodedField = this.staticFields.get(j);
            Constant zero;
            if ((zero = this.staticValues.get(encodedField)) == null) {
                zero = Zeroes.zeroFor(encodedField.getRef().getType());
            }
            list.set(j, zero);
        }
        list.setImmutable();
        return new CstArray(list);
    }
    
    @Override
    public void addContents(final DexFile dexFile) {
        if (!this.staticFields.isEmpty()) {
            this.getStaticValuesConstant();
            final Iterator<EncodedField> iterator = this.staticFields.iterator();
            while (iterator.hasNext()) {
                iterator.next().addContents(dexFile);
            }
        }
        if (!this.instanceFields.isEmpty()) {
            Collections.sort(this.instanceFields);
            final Iterator<EncodedField> iterator2 = this.instanceFields.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().addContents(dexFile);
            }
        }
        if (!this.directMethods.isEmpty()) {
            Collections.sort(this.directMethods);
            final Iterator<EncodedMethod> iterator3 = this.directMethods.iterator();
            while (iterator3.hasNext()) {
                iterator3.next().addContents(dexFile);
            }
        }
        if (!this.virtualMethods.isEmpty()) {
            Collections.sort(this.virtualMethods);
            final Iterator<EncodedMethod> iterator4 = this.virtualMethods.iterator();
            while (iterator4.hasNext()) {
                iterator4.next().addContents(dexFile);
            }
        }
    }
    
    public void addDirectMethod(final EncodedMethod encodedMethod) {
        if (encodedMethod == null) {
            throw new NullPointerException("method == null");
        }
        this.directMethods.add(encodedMethod);
    }
    
    public void addInstanceField(final EncodedField encodedField) {
        if (encodedField == null) {
            throw new NullPointerException("field == null");
        }
        this.instanceFields.add(encodedField);
    }
    
    public void addStaticField(final EncodedField encodedField, final Constant constant) {
        if (encodedField == null) {
            throw new NullPointerException("field == null");
        }
        if (this.staticValuesConstant != null) {
            throw new UnsupportedOperationException("static fields already sorted");
        }
        this.staticFields.add(encodedField);
        this.staticValues.put(encodedField, constant);
    }
    
    public void addVirtualMethod(final EncodedMethod encodedMethod) {
        if (encodedMethod == null) {
            throw new NullPointerException("method == null");
        }
        this.virtualMethods.add(encodedMethod);
    }
    
    public void debugPrint(final Writer writer, final boolean b) {
        final PrintWriter printWriter = Writers.printWriterFor(writer);
        final int size = this.staticFields.size();
        final int n = 0;
        for (int i = 0; i < size; ++i) {
            final StringBuilder sb = new StringBuilder();
            sb.append("  sfields[");
            sb.append(i);
            sb.append("]: ");
            sb.append(this.staticFields.get(i));
            printWriter.println(sb.toString());
        }
        for (int size2 = this.instanceFields.size(), j = 0; j < size2; ++j) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("  ifields[");
            sb2.append(j);
            sb2.append("]: ");
            sb2.append(this.instanceFields.get(j));
            printWriter.println(sb2.toString());
        }
        for (int size3 = this.directMethods.size(), k = 0; k < size3; ++k) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("  dmeths[");
            sb3.append(k);
            sb3.append("]:");
            printWriter.println(sb3.toString());
            this.directMethods.get(k).debugPrint(printWriter, b);
        }
        for (int size4 = this.virtualMethods.size(), l = n; l < size4; ++l) {
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("  vmeths[");
            sb4.append(l);
            sb4.append("]:");
            printWriter.println(sb4.toString());
            this.virtualMethods.get(l).debugPrint(printWriter, b);
        }
    }
    
    public ArrayList<EncodedMethod> getMethods() {
        final ArrayList<Object> list = (ArrayList<Object>)new ArrayList<EncodedMethod>(this.directMethods.size() + this.virtualMethods.size());
        list.addAll(this.directMethods);
        list.addAll(this.virtualMethods);
        return (ArrayList<EncodedMethod>)list;
    }
    
    public CstArray getStaticValuesConstant() {
        if (this.staticValuesConstant == null && this.staticFields.size() != 0) {
            this.staticValuesConstant = this.makeStaticValuesConstant();
        }
        return this.staticValuesConstant;
    }
    
    public boolean isEmpty() {
        return this.staticFields.isEmpty() && this.instanceFields.isEmpty() && this.directMethods.isEmpty() && this.virtualMethods.isEmpty();
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_CLASS_DATA_ITEM;
    }
    
    @Override
    protected void place0(final Section section, final int n) {
        final ByteArrayAnnotatedOutput byteArrayAnnotatedOutput = new ByteArrayAnnotatedOutput();
        this.encodeOutput(section.getFile(), byteArrayAnnotatedOutput);
        this.encodedForm = byteArrayAnnotatedOutput.toByteArray();
        this.setWriteSize(this.encodedForm.length);
    }
    
    @Override
    public String toHuman() {
        return this.toString();
    }
    
    public void writeTo0(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        if (annotatedOutput.annotates()) {
            this.encodeOutput(dexFile, annotatedOutput);
            return;
        }
        annotatedOutput.write(this.encodedForm);
    }
}
