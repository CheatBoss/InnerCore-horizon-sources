package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dx.util.*;

public final class HeaderItem extends IndexedItem
{
    @Override
    public void addContents(final DexFile dexFile) {
    }
    
    @Override
    public ItemType itemType() {
        return ItemType.TYPE_HEADER_ITEM;
    }
    
    @Override
    public int writeSize() {
        return 112;
    }
    
    @Override
    public void writeTo(final DexFile dexFile, final AnnotatedOutput annotatedOutput) {
        final int fileOffset = dexFile.getMap().getFileOffset();
        final Section firstDataSection = dexFile.getFirstDataSection();
        final Section lastDataSection = dexFile.getLastDataSection();
        final int fileOffset2 = firstDataSection.getFileOffset();
        final int n = lastDataSection.getFileOffset() + lastDataSection.writeSize() - fileOffset2;
        final String magic = dexFile.getDexOptions().getMagic();
        if (annotatedOutput.annotates()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("magic: ");
            sb.append(new CstString(magic).toQuoted());
            annotatedOutput.annotate(8, sb.toString());
            annotatedOutput.annotate(4, "checksum");
            annotatedOutput.annotate(20, "signature");
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("file_size:       ");
            sb2.append(Hex.u4(dexFile.getFileSize()));
            annotatedOutput.annotate(4, sb2.toString());
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("header_size:     ");
            sb3.append(Hex.u4(112));
            annotatedOutput.annotate(4, sb3.toString());
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("endian_tag:      ");
            sb4.append(Hex.u4(305419896));
            annotatedOutput.annotate(4, sb4.toString());
            annotatedOutput.annotate(4, "link_size:       0");
            annotatedOutput.annotate(4, "link_off:        0");
            final StringBuilder sb5 = new StringBuilder();
            sb5.append("map_off:         ");
            sb5.append(Hex.u4(fileOffset));
            annotatedOutput.annotate(4, sb5.toString());
        }
        for (int i = 0; i < 8; ++i) {
            annotatedOutput.writeByte(magic.charAt(i));
        }
        annotatedOutput.writeZeroes(24);
        annotatedOutput.writeInt(dexFile.getFileSize());
        annotatedOutput.writeInt(112);
        annotatedOutput.writeInt(305419896);
        annotatedOutput.writeZeroes(8);
        annotatedOutput.writeInt(fileOffset);
        dexFile.getStringIds().writeHeaderPart(annotatedOutput);
        dexFile.getTypeIds().writeHeaderPart(annotatedOutput);
        dexFile.getProtoIds().writeHeaderPart(annotatedOutput);
        dexFile.getFieldIds().writeHeaderPart(annotatedOutput);
        dexFile.getMethodIds().writeHeaderPart(annotatedOutput);
        dexFile.getClassDefs().writeHeaderPart(annotatedOutput);
        if (annotatedOutput.annotates()) {
            final StringBuilder sb6 = new StringBuilder();
            sb6.append("data_size:       ");
            sb6.append(Hex.u4(n));
            annotatedOutput.annotate(4, sb6.toString());
            final StringBuilder sb7 = new StringBuilder();
            sb7.append("data_off:        ");
            sb7.append(Hex.u4(fileOffset2));
            annotatedOutput.annotate(4, sb7.toString());
        }
        annotatedOutput.writeInt(n);
        annotatedOutput.writeInt(fileOffset2);
    }
}
