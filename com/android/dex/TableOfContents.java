package com.android.dex;

import java.util.*;
import java.io.*;

public final class TableOfContents
{
    public final Section annotationSetRefLists;
    public final Section annotationSets;
    public final Section annotations;
    public final Section annotationsDirectories;
    public int checksum;
    public final Section classDatas;
    public final Section classDefs;
    public final Section codes;
    public int dataOff;
    public int dataSize;
    public final Section debugInfos;
    public final Section encodedArrays;
    public final Section fieldIds;
    public int fileSize;
    public final Section header;
    public int linkOff;
    public int linkSize;
    public final Section mapList;
    public final Section methodIds;
    public final Section protoIds;
    public final Section[] sections;
    public byte[] signature;
    public final Section stringDatas;
    public final Section stringIds;
    public final Section typeIds;
    public final Section typeLists;
    
    public TableOfContents() {
        this.header = new Section(0);
        this.stringIds = new Section(1);
        this.typeIds = new Section(2);
        this.protoIds = new Section(3);
        this.fieldIds = new Section(4);
        this.methodIds = new Section(5);
        this.classDefs = new Section(6);
        this.mapList = new Section(4096);
        this.typeLists = new Section(4097);
        this.annotationSetRefLists = new Section(4098);
        this.annotationSets = new Section(4099);
        this.classDatas = new Section(8192);
        this.codes = new Section(8193);
        this.stringDatas = new Section(8194);
        this.debugInfos = new Section(8195);
        this.annotations = new Section(8196);
        this.encodedArrays = new Section(8197);
        this.annotationsDirectories = new Section(8198);
        this.sections = new Section[] { this.header, this.stringIds, this.typeIds, this.protoIds, this.fieldIds, this.methodIds, this.classDefs, this.mapList, this.typeLists, this.annotationSetRefLists, this.annotationSets, this.classDatas, this.codes, this.stringDatas, this.debugInfos, this.annotations, this.encodedArrays, this.annotationsDirectories };
        this.signature = new byte[20];
    }
    
    private Section getSection(final short n) {
        final Section[] sections = this.sections;
        for (int length = sections.length, i = 0; i < length; ++i) {
            final Section section = sections[i];
            if (section.type == n) {
                return section;
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("No such map item: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void readHeader(final Dex.Section section) throws UnsupportedEncodingException {
        final byte[] byteArray = section.readByteArray(8);
        if (DexFormat.magicToApi(byteArray) != 13) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected magic: ");
            sb.append(Arrays.toString(byteArray));
            throw new DexException(sb.toString());
        }
        this.checksum = section.readInt();
        this.signature = section.readByteArray(20);
        this.fileSize = section.readInt();
        final int int1 = section.readInt();
        if (int1 != 112) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Unexpected header: 0x");
            sb2.append(Integer.toHexString(int1));
            throw new DexException(sb2.toString());
        }
        final int int2 = section.readInt();
        if (int2 != 305419896) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Unexpected endian tag: 0x");
            sb3.append(Integer.toHexString(int2));
            throw new DexException(sb3.toString());
        }
        this.linkSize = section.readInt();
        this.linkOff = section.readInt();
        this.mapList.off = section.readInt();
        if (this.mapList.off == 0) {
            throw new DexException("Cannot merge dex files that do not contain a map");
        }
        this.stringIds.size = section.readInt();
        this.stringIds.off = section.readInt();
        this.typeIds.size = section.readInt();
        this.typeIds.off = section.readInt();
        this.protoIds.size = section.readInt();
        this.protoIds.off = section.readInt();
        this.fieldIds.size = section.readInt();
        this.fieldIds.off = section.readInt();
        this.methodIds.size = section.readInt();
        this.methodIds.off = section.readInt();
        this.classDefs.size = section.readInt();
        this.classDefs.off = section.readInt();
        this.dataSize = section.readInt();
        this.dataOff = section.readInt();
    }
    
    private void readMap(final Dex.Section section) throws IOException {
        final int int1 = section.readInt();
        Section section2 = null;
        for (int i = 0; i < int1; ++i) {
            final short short1 = section.readShort();
            section.readShort();
            final Section section3 = this.getSection(short1);
            final int int2 = section.readInt();
            final int int3 = section.readInt();
            if ((section3.size != 0 && section3.size != int2) || (section3.off != -1 && section3.off != int3)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unexpected map value for 0x");
                sb.append(Integer.toHexString(short1));
                throw new DexException(sb.toString());
            }
            section3.size = int2;
            section3.off = int3;
            if (section2 != null && section2.off > section3.off) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Map is unsorted at ");
                sb2.append(section2);
                sb2.append(", ");
                sb2.append(section3);
                throw new DexException(sb2.toString());
            }
            section2 = section3;
        }
        Arrays.sort(this.sections);
    }
    
    public void computeSizesFromOffsets() {
        int off = this.dataOff + this.dataSize;
        for (int i = this.sections.length - 1; i >= 0; --i) {
            final Section section = this.sections[i];
            if (section.off != -1) {
                if (section.off > off) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Map is unsorted at ");
                    sb.append(section);
                    throw new DexException(sb.toString());
                }
                section.byteCount = off - section.off;
                off = section.off;
            }
        }
    }
    
    public void readFrom(final Dex dex) throws IOException {
        this.readHeader(dex.open(0));
        this.readMap(dex.open(this.mapList.off));
        this.computeSizesFromOffsets();
    }
    
    public void writeHeader(final Dex.Section section) throws IOException {
        section.write(DexFormat.apiToMagic(13).getBytes("UTF-8"));
        section.writeInt(this.checksum);
        section.write(this.signature);
        section.writeInt(this.fileSize);
        section.writeInt(112);
        section.writeInt(305419896);
        section.writeInt(this.linkSize);
        section.writeInt(this.linkOff);
        section.writeInt(this.mapList.off);
        section.writeInt(this.stringIds.size);
        section.writeInt(this.stringIds.off);
        section.writeInt(this.typeIds.size);
        section.writeInt(this.typeIds.off);
        section.writeInt(this.protoIds.size);
        section.writeInt(this.protoIds.off);
        section.writeInt(this.fieldIds.size);
        section.writeInt(this.fieldIds.off);
        section.writeInt(this.methodIds.size);
        section.writeInt(this.methodIds.off);
        section.writeInt(this.classDefs.size);
        section.writeInt(this.classDefs.off);
        section.writeInt(this.dataSize);
        section.writeInt(this.dataOff);
    }
    
    public void writeMap(final Dex.Section section) throws IOException {
        final Section[] sections = this.sections;
        final int length = sections.length;
        int n = 0;
        int n2;
        for (int i = 0; i < length; ++i, n = n2) {
            n2 = n;
            if (sections[i].exists()) {
                n2 = n + 1;
            }
        }
        section.writeInt(n);
        final Section[] sections2 = this.sections;
        for (int length2 = sections2.length, j = 0; j < length2; ++j) {
            final Section section2 = sections2[j];
            if (section2.exists()) {
                section.writeShort(section2.type);
                section.writeShort((short)0);
                section.writeInt(section2.size);
                section.writeInt(section2.off);
            }
        }
    }
    
    public static class Section implements Comparable<Section>
    {
        public int byteCount;
        public int off;
        public int size;
        public final short type;
        
        public Section(final int n) {
            this.size = 0;
            this.off = -1;
            this.byteCount = 0;
            this.type = (short)n;
        }
        
        @Override
        public int compareTo(final Section section) {
            if (this.off == section.off) {
                return 0;
            }
            if (this.off < section.off) {
                return -1;
            }
            return 1;
        }
        
        public boolean exists() {
            return this.size > 0;
        }
        
        @Override
        public String toString() {
            return String.format("Section[type=%#x,off=%#x,size=%#x]", this.type, this.off, this.size);
        }
    }
}
