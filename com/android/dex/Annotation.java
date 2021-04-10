package com.android.dex;

public final class Annotation implements Comparable<Annotation>
{
    private final Dex dex;
    private final EncodedValue encodedAnnotation;
    private final byte visibility;
    
    public Annotation(final Dex dex, final byte visibility, final EncodedValue encodedAnnotation) {
        this.dex = dex;
        this.visibility = visibility;
        this.encodedAnnotation = encodedAnnotation;
    }
    
    @Override
    public int compareTo(final Annotation annotation) {
        return this.encodedAnnotation.compareTo(annotation.encodedAnnotation);
    }
    
    public EncodedValueReader getReader() {
        return new EncodedValueReader(this.encodedAnnotation, 29);
    }
    
    public int getTypeIndex() {
        final EncodedValueReader reader = this.getReader();
        reader.readAnnotation();
        return reader.getAnnotationType();
    }
    
    public byte getVisibility() {
        return this.visibility;
    }
    
    @Override
    public String toString() {
        StringBuilder sb;
        if (this.dex == null) {
            sb = new StringBuilder();
            sb.append(this.visibility);
            sb.append(" ");
            sb.append(this.getTypeIndex());
        }
        else {
            sb = new StringBuilder();
            sb.append(this.visibility);
            sb.append(" ");
            sb.append(this.dex.typeNames().get(this.getTypeIndex()));
        }
        return sb.toString();
    }
    
    public void writeTo(final Dex.Section section) {
        section.writeByte(this.visibility);
        this.encodedAnnotation.writeTo(section);
    }
}
