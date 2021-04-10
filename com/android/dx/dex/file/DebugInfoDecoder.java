package com.android.dx.dex.file;

import com.android.dx.rop.cst.*;
import com.android.dex.*;
import com.android.dx.rop.type.*;
import com.android.dex.util.*;
import java.util.*;
import java.io.*;
import com.android.dx.dex.code.*;

public class DebugInfoDecoder
{
    private int address;
    private final int codesize;
    private final Prototype desc;
    private final byte[] encoded;
    private final DexFile file;
    private final boolean isStatic;
    private final LocalEntry[] lastEntryForReg;
    private int line;
    private final ArrayList<LocalEntry> locals;
    private final ArrayList<PositionEntry> positions;
    private final int regSize;
    private final int thisStringIdx;
    
    DebugInfoDecoder(final byte[] encoded, int index, int regSize, final boolean isStatic, final CstMethodRef cstMethodRef, final DexFile file) {
        this.line = 1;
        this.address = 0;
        if (encoded == null) {
            throw new NullPointerException("encoded == null");
        }
        this.encoded = encoded;
        this.isStatic = isStatic;
        this.desc = cstMethodRef.getPrototype();
        this.file = file;
        this.regSize = regSize;
        this.positions = new ArrayList<PositionEntry>();
        this.locals = new ArrayList<LocalEntry>();
        this.codesize = index;
        this.lastEntryForReg = new LocalEntry[regSize];
        index = -1;
        try {
            regSize = (index = file.getStringIds().indexOf(new CstString("this")));
        }
        catch (IllegalArgumentException ex) {}
        this.thisStringIdx = index;
    }
    
    private void decode0() throws IOException {
        final ByteArrayByteInput byteArrayByteInput = new ByteArrayByteInput(this.encoded);
        this.line = Leb128.readUnsignedLeb128(byteArrayByteInput);
        final int unsignedLeb128 = Leb128.readUnsignedLeb128(byteArrayByteInput);
        final StdTypeList parameterTypes = this.desc.getParameterTypes();
        final int paramBase = this.getParamBase();
        if (unsignedLeb128 != parameterTypes.size()) {
            throw new RuntimeException("Mismatch between parameters_size and prototype");
        }
        int n = paramBase;
        if (!this.isStatic) {
            final LocalEntry localEntry = new LocalEntry(0, true, paramBase, this.thisStringIdx, 0, 0);
            this.locals.add(localEntry);
            this.lastEntryForReg[paramBase] = localEntry;
            n = paramBase + 1;
        }
        final int n2 = 0;
        int n3 = n;
        for (int i = n2; i < unsignedLeb128; ++i) {
            final Type type = parameterTypes.getType(i);
            final int stringIndex = this.readStringIndex(byteArrayByteInput);
            LocalEntry localEntry2;
            if (stringIndex == -1) {
                localEntry2 = new LocalEntry(0, true, n3, -1, 0, 0);
            }
            else {
                localEntry2 = new LocalEntry(0, true, n3, stringIndex, 0, 0);
            }
            this.locals.add(localEntry2);
            this.lastEntryForReg[n3] = localEntry2;
            n3 += type.getCategory();
        }
        while (true) {
            final int n4 = byteArrayByteInput.readByte() & 0xFF;
            switch (n4) {
                default: {
                    if (n4 < 10) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Invalid extended opcode encountered ");
                        sb.append(n4);
                        throw new RuntimeException(sb.toString());
                    }
                    final int n5 = n4 - 10;
                    this.address += n5 / 15;
                    this.line += n5 % 15 - 4;
                    this.positions.add(new PositionEntry(this.address, this.line));
                    continue;
                }
                case 9: {
                    continue;
                }
                case 8: {
                    continue;
                }
                case 7: {
                    continue;
                }
                case 6: {
                    final int unsignedLeb129 = Leb128.readUnsignedLeb128(byteArrayByteInput);
                    try {
                        final LocalEntry localEntry3 = this.lastEntryForReg[unsignedLeb129];
                        if (localEntry3.isStart) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("nonsensical RESTART_LOCAL on live register v");
                            sb2.append(unsignedLeb129);
                            throw new RuntimeException(sb2.toString());
                        }
                        final LocalEntry localEntry4 = new LocalEntry(this.address, true, unsignedLeb129, localEntry3.nameIndex, localEntry3.typeIndex, 0);
                        this.locals.add(localEntry4);
                        this.lastEntryForReg[unsignedLeb129] = localEntry4;
                        continue;
                    }
                    catch (NullPointerException ex) {
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("Encountered RESTART_LOCAL on new v");
                        sb3.append(unsignedLeb129);
                        throw new RuntimeException(sb3.toString());
                    }
                }
                case 5: {
                    final int unsignedLeb130 = Leb128.readUnsignedLeb128(byteArrayByteInput);
                    try {
                        final LocalEntry localEntry5 = this.lastEntryForReg[unsignedLeb130];
                        if (!localEntry5.isStart) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("nonsensical END_LOCAL on dead register v");
                            sb4.append(unsignedLeb130);
                            throw new RuntimeException(sb4.toString());
                        }
                        final LocalEntry localEntry6 = new LocalEntry(this.address, false, unsignedLeb130, localEntry5.nameIndex, localEntry5.typeIndex, localEntry5.signatureIndex);
                        this.locals.add(localEntry6);
                        this.lastEntryForReg[unsignedLeb130] = localEntry6;
                        continue;
                    }
                    catch (NullPointerException ex2) {
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("Encountered END_LOCAL on new v");
                        sb5.append(unsignedLeb130);
                        throw new RuntimeException(sb5.toString());
                    }
                }
                case 4: {
                    final int unsignedLeb131 = Leb128.readUnsignedLeb128(byteArrayByteInput);
                    final LocalEntry localEntry7 = new LocalEntry(this.address, true, unsignedLeb131, this.readStringIndex(byteArrayByteInput), this.readStringIndex(byteArrayByteInput), this.readStringIndex(byteArrayByteInput));
                    this.locals.add(localEntry7);
                    this.lastEntryForReg[unsignedLeb131] = localEntry7;
                    continue;
                }
                case 3: {
                    final int unsignedLeb132 = Leb128.readUnsignedLeb128(byteArrayByteInput);
                    final LocalEntry localEntry8 = new LocalEntry(this.address, true, unsignedLeb132, this.readStringIndex(byteArrayByteInput), this.readStringIndex(byteArrayByteInput), 0);
                    this.locals.add(localEntry8);
                    this.lastEntryForReg[unsignedLeb132] = localEntry8;
                    continue;
                }
                case 2: {
                    this.line += Leb128.readSignedLeb128(byteArrayByteInput);
                    continue;
                }
                case 1: {
                    this.address += Leb128.readUnsignedLeb128(byteArrayByteInput);
                    continue;
                }
                case 0: {}
            }
        }
    }
    
    private int getParamBase() {
        return this.regSize - this.desc.getParameterTypes().getWordCount() - ((this.isStatic ^ true) ? 1 : 0);
    }
    
    private int readStringIndex(final ByteInput byteInput) throws IOException {
        return Leb128.readUnsignedLeb128(byteInput) - 1;
    }
    
    public static void validateEncode(final byte[] array, final DexFile dexFile, final CstMethodRef cstMethodRef, DalvCode insns, final boolean b) {
        final PositionList positions = insns.getPositions();
        final LocalList locals = insns.getLocals();
        insns = (DalvCode)insns.getInsns();
        final int codeSize = ((DalvInsnList)insns).codeSize();
        final int registersSize = ((DalvInsnList)insns).getRegistersSize();
        try {
            validateEncode0(array, codeSize, registersSize, b, cstMethodRef, dexFile, positions, locals);
        }
        catch (RuntimeException ex) {
            System.err.println("instructions:");
            ((DalvInsnList)insns).debugPrint(System.err, "  ", true);
            System.err.println("local list:");
            locals.debugPrint(System.err, "  ");
            final StringBuilder sb = new StringBuilder();
            sb.append("while processing ");
            sb.append(cstMethodRef.toHuman());
            throw ExceptionWithContext.withContext(ex, sb.toString());
        }
    }
    
    private static void validateEncode0(final byte[] array, int i, int address, final boolean b, final CstMethodRef cstMethodRef, final DexFile dexFile, final PositionList list, final LocalList list2) {
        final DebugInfoDecoder debugInfoDecoder = new DebugInfoDecoder(array, i, address, b, cstMethodRef, dexFile);
        debugInfoDecoder.decode();
        final List<PositionEntry> positionList = debugInfoDecoder.getPositionList();
        if (positionList.size() != list.size()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Decoded positions table not same size was ");
            sb.append(positionList.size());
            sb.append(" expected ");
            sb.append(list.size());
            throw new RuntimeException(sb.toString());
        }
    Label_0113:
        for (final PositionEntry positionEntry : positionList) {
            address = 0;
            i = list.size() - 1;
            while (true) {
                while (i >= 0) {
                    final PositionList.Entry value = list.get(i);
                    if (positionEntry.line == value.getPosition().getLine() && positionEntry.address == value.getAddress()) {
                        i = 1;
                        if (i == 0) {
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Could not match position entry: ");
                            sb2.append(positionEntry.address);
                            sb2.append(", ");
                            sb2.append(positionEntry.line);
                            throw new RuntimeException(sb2.toString());
                        }
                        continue Label_0113;
                    }
                    else {
                        --i;
                    }
                }
                i = address;
                continue;
            }
        }
        final List<LocalEntry> locals = debugInfoDecoder.getLocals();
        final int thisStringIdx = debugInfoDecoder.thisStringIdx;
        int size = locals.size();
        final int paramBase = debugInfoDecoder.getParamBase();
        final int n = 0;
        LocalEntry localEntry;
        int nameIndex;
        int n2;
        LocalEntry localEntry2;
        for (i = 0; i < size; ++i, size = address) {
            localEntry = locals.get(i);
            nameIndex = localEntry.nameIndex;
            if (nameIndex >= 0) {
                address = size;
                if (nameIndex != thisStringIdx) {
                    continue;
                }
            }
            n2 = i + 1;
            while (true) {
                address = size;
                if (n2 >= size) {
                    break;
                }
                localEntry2 = locals.get(n2);
                if (localEntry2.address != 0) {
                    address = size;
                    break;
                }
                if (localEntry.reg == localEntry2.reg && localEntry2.isStart) {
                    locals.set(i, localEntry2);
                    locals.remove(n2);
                    address = size - 1;
                    break;
                }
                ++n2;
            }
        }
        final int size2 = list2.size();
        i = 0;
        final int n3 = 0;
        while (true) {
            for (int j = n; j < size2; ++j) {
                final LocalList.Entry value2 = list2.get(j);
                address = i;
                if (value2.getDisposition() != LocalList.Disposition.END_REPLACED) {
                    LocalEntry localEntry3;
                    do {
                        localEntry3 = locals.get(address);
                        if (localEntry3.nameIndex >= 0) {
                            i = address;
                            break;
                        }
                        i = address + 1;
                    } while ((address = i) < size);
                    address = localEntry3.address;
                    if (localEntry3.reg != value2.getRegister()) {
                        final PrintStream err = System.err;
                        final StringBuilder sb3 = new StringBuilder();
                        sb3.append("local register mismatch at orig ");
                        sb3.append(j);
                        sb3.append(" / decoded ");
                        sb3.append(i);
                        err.println(sb3.toString());
                        i = 1;
                    }
                    else if (localEntry3.isStart != value2.isStart()) {
                        final PrintStream err2 = System.err;
                        final StringBuilder sb4 = new StringBuilder();
                        sb4.append("local start/end mismatch at orig ");
                        sb4.append(j);
                        sb4.append(" / decoded ");
                        sb4.append(i);
                        err2.println(sb4.toString());
                        i = 1;
                    }
                    else {
                        if (address == value2.getAddress() || (address == 0 && localEntry3.reg >= paramBase)) {
                            ++i;
                            continue;
                        }
                        final PrintStream err3 = System.err;
                        final StringBuilder sb5 = new StringBuilder();
                        sb5.append("local address mismatch at orig ");
                        sb5.append(j);
                        sb5.append(" / decoded ");
                        sb5.append(i);
                        err3.println(sb5.toString());
                        i = 1;
                    }
                    if (i != 0) {
                        System.err.println("decoded locals:");
                        for (final LocalEntry localEntry4 : locals) {
                            final PrintStream err4 = System.err;
                            final StringBuilder sb6 = new StringBuilder();
                            sb6.append("  ");
                            sb6.append(localEntry4);
                            err4.println(sb6.toString());
                        }
                        throw new RuntimeException("local table problem");
                    }
                    return;
                }
            }
            i = n3;
            continue;
        }
    }
    
    public void decode() {
        try {
            this.decode0();
        }
        catch (Exception ex) {
            throw ExceptionWithContext.withContext(ex, "...while decoding debug info");
        }
    }
    
    public List<LocalEntry> getLocals() {
        return this.locals;
    }
    
    public List<PositionEntry> getPositionList() {
        return this.positions;
    }
    
    private static class LocalEntry
    {
        public int address;
        public boolean isStart;
        public int nameIndex;
        public int reg;
        public int signatureIndex;
        public int typeIndex;
        
        public LocalEntry(final int address, final boolean isStart, final int reg, final int nameIndex, final int typeIndex, final int signatureIndex) {
            this.address = address;
            this.isStart = isStart;
            this.reg = reg;
            this.nameIndex = nameIndex;
            this.typeIndex = typeIndex;
            this.signatureIndex = signatureIndex;
        }
        
        @Override
        public String toString() {
            final int address = this.address;
            String s;
            if (this.isStart) {
                s = "start";
            }
            else {
                s = "end";
            }
            return String.format("[%x %s v%d %04x %04x %04x]", address, s, this.reg, this.nameIndex, this.typeIndex, this.signatureIndex);
        }
    }
    
    private static class PositionEntry
    {
        public int address;
        public int line;
        
        public PositionEntry(final int address, final int line) {
            this.address = address;
            this.line = line;
        }
    }
}
