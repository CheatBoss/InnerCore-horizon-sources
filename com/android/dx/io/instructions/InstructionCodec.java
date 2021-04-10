package com.android.dx.io.instructions;

import java.io.*;
import com.android.dx.io.*;
import com.android.dx.util.*;
import com.android.dex.*;

public enum InstructionCodec
{
    FORMAT_00X {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, n, 0, null, 0, 0L);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(decodedInstruction.getOpcodeUnit());
        }
    }, 
    FORMAT_10T {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, byte0(n), 0, null, codeInput.cursor() - 1 + (byte)byte1(n), 0L);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getTargetByte(codeOutput.cursor())));
        }
    }, 
    FORMAT_10X {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new ZeroRegisterDecodedInstruction(this, byte0(n), 0, null, 0, byte1(n));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(decodedInstruction.getOpcodeUnit());
        }
    }, 
    FORMAT_11N {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new OneRegisterDecodedInstruction(this, byte0(n), 0, null, 0, nibble3(n) << 28 >> 28, nibble2(n));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcodeUnit(), makeByte(decodedInstruction.getA(), decodedInstruction.getLiteralNibble())));
        }
    }, 
    FORMAT_11X {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new OneRegisterDecodedInstruction(this, byte0(n), 0, null, 0, 0L, byte1(n));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()));
        }
    }, 
    FORMAT_12X {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, byte0(n), 0, null, 0, 0L, nibble2(n), nibble3(n));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcodeUnit(), makeByte(decodedInstruction.getA(), decodedInstruction.getB())));
        }
    }, 
    FORMAT_20BC {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new ZeroRegisterDecodedInstruction(this, access$201, codeInput.read(), IndexType.VARIES, 0, access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getLiteralByte()), decodedInstruction.getIndexUnit());
        }
    }, 
    FORMAT_20T {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int cursor = codeInput.cursor();
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new ZeroRegisterDecodedInstruction(this, access$201, 0, null, cursor - 1 + (short)codeInput.read(), access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(decodedInstruction.getOpcodeUnit(), decodedInstruction.getTargetUnit(codeOutput.cursor()));
        }
    }, 
    FORMAT_21C {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, codeInput.read(), OpcodeInfo.getIndexType(access$201), 0, 0L, access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), decodedInstruction.getIndexUnit());
        }
    }, 
    FORMAT_21H {
        @Override
        public DecodedInstruction decode(int n, final CodeInput codeInput) throws EOFException {
            final int access$100 = byte0(n);
            final int access$101 = byte1(n);
            final long n2 = (short)codeInput.read();
            if (access$100 == 21) {
                n = 16;
            }
            else {
                n = 48;
            }
            return new OneRegisterDecodedInstruction(this, access$100, 0, null, 0, n2 << n, access$101);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final int opcode = decodedInstruction.getOpcode();
            int n;
            if (opcode == 21) {
                n = 16;
            }
            else {
                n = 48;
            }
            codeOutput.write(codeUnit(opcode, decodedInstruction.getA()), (short)(decodedInstruction.getLiteral() >> n));
        }
    }, 
    FORMAT_21S {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, 0, null, 0, (short)codeInput.read(), access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), decodedInstruction.getLiteralUnit());
        }
    }, 
    FORMAT_21T {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int cursor = codeInput.cursor();
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, 0, null, cursor - 1 + (short)codeInput.read(), 0L, access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), decodedInstruction.getTargetUnit(codeOutput.cursor()));
        }
    }, 
    FORMAT_22B {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            final int read = codeInput.read();
            return new TwoRegisterDecodedInstruction(this, access$201, 0, null, 0, (byte)byte1(read), access$200, byte0(read));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), codeUnit(decodedInstruction.getB(), decodedInstruction.getLiteralByte()));
        }
    }, 
    FORMAT_22C {
        @Override
        public DecodedInstruction decode(int access$400, final CodeInput codeInput) throws EOFException {
            final int access$401 = byte0(access$400);
            final int access$402 = nibble2(access$400);
            access$400 = nibble3(access$400);
            return new TwoRegisterDecodedInstruction(this, access$401, codeInput.read(), OpcodeInfo.getIndexType(access$401), 0, 0L, access$402, access$400);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), makeByte(decodedInstruction.getA(), decodedInstruction.getB())), decodedInstruction.getIndexUnit());
        }
    }, 
    FORMAT_22CS {
        @Override
        public DecodedInstruction decode(int access$400, final CodeInput codeInput) throws EOFException {
            final int access$401 = byte0(access$400);
            final int access$402 = nibble2(access$400);
            access$400 = nibble3(access$400);
            return new TwoRegisterDecodedInstruction(this, access$401, codeInput.read(), IndexType.FIELD_OFFSET, 0, 0L, access$402, access$400);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), makeByte(decodedInstruction.getA(), decodedInstruction.getB())), decodedInstruction.getIndexUnit());
        }
    }, 
    FORMAT_22S {
        @Override
        public DecodedInstruction decode(int access$400, final CodeInput codeInput) throws EOFException {
            final int access$401 = byte0(access$400);
            final int access$402 = nibble2(access$400);
            access$400 = nibble3(access$400);
            return new TwoRegisterDecodedInstruction(this, access$401, 0, null, 0, (short)codeInput.read(), access$402, access$400);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), makeByte(decodedInstruction.getA(), decodedInstruction.getB())), decodedInstruction.getLiteralUnit());
        }
    }, 
    FORMAT_22T {
        @Override
        public DecodedInstruction decode(int access$400, final CodeInput codeInput) throws EOFException {
            final int cursor = codeInput.cursor();
            final int access$401 = byte0(access$400);
            final int access$402 = nibble2(access$400);
            access$400 = nibble3(access$400);
            return new TwoRegisterDecodedInstruction(this, access$401, 0, null, cursor - 1 + (short)codeInput.read(), 0L, access$402, access$400);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), makeByte(decodedInstruction.getA(), decodedInstruction.getB())), decodedInstruction.getTargetUnit(codeOutput.cursor()));
        }
    }, 
    FORMAT_22X {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return new TwoRegisterDecodedInstruction(this, byte0(n), 0, null, 0, 0L, byte1(n), codeInput.read());
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), decodedInstruction.getBUnit());
        }
    }, 
    FORMAT_23X {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            final int read = codeInput.read();
            return new ThreeRegisterDecodedInstruction(this, access$201, 0, null, 0, 0L, access$200, byte0(read), byte1(read));
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), codeUnit(decodedInstruction.getB(), decodedInstruction.getC()));
        }
    }, 
    FORMAT_30T {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int cursor = codeInput.cursor();
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new ZeroRegisterDecodedInstruction(this, access$201, 0, null, cursor - 1 + codeInput.readInt(), access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final int target = decodedInstruction.getTarget(codeOutput.cursor());
            codeOutput.write(decodedInstruction.getOpcodeUnit(), unit0(target), unit1(target));
        }
    }, 
    FORMAT_31C {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, codeInput.readInt(), OpcodeInfo.getIndexType(access$201), 0, 0L, access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final int index = decodedInstruction.getIndex();
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), unit0(index), unit1(index));
        }
    }, 
    FORMAT_31I {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, 0, null, 0, codeInput.readInt(), access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final int literalInt = decodedInstruction.getLiteralInt();
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), unit0(literalInt), unit1(literalInt));
        }
    }, 
    FORMAT_31T {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int n = codeInput.cursor() - 1;
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            final int n2 = n + codeInput.readInt();
            switch (access$201) {
                case 43:
                case 44: {
                    codeInput.setBaseAddress(n2, n);
                    break;
                }
            }
            return new OneRegisterDecodedInstruction(this, access$201, 0, null, n2, 0L, access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final int target = decodedInstruction.getTarget(codeOutput.cursor());
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), unit0(target), unit1(target));
        }
    }, 
    FORMAT_32X {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new TwoRegisterDecodedInstruction(this, access$201, 0, null, 0, access$200, codeInput.read(), codeInput.read());
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            codeOutput.write(decodedInstruction.getOpcodeUnit(), decodedInstruction.getAUnit(), decodedInstruction.getBUnit());
        }
    }, 
    FORMAT_35C {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterList(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterList(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_35MI {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterList(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterList(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_35MS {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterList(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterList(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_3RC {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterRange(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterRange(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_3RMI {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterRange(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterRange(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_3RMS {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            return decodeRegisterRange(this, n, codeInput);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            encodeRegisterRange(decodedInstruction, codeOutput);
        }
    }, 
    FORMAT_51L {
        @Override
        public DecodedInstruction decode(int access$200, final CodeInput codeInput) throws EOFException {
            final int access$201 = byte0(access$200);
            access$200 = byte1(access$200);
            return new OneRegisterDecodedInstruction(this, access$201, 0, null, 0, codeInput.readLong(), access$200);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final long literal = decodedInstruction.getLiteral();
            codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getA()), unit0(literal), unit1(literal), unit2(literal), unit3(literal));
        }
    }, 
    FORMAT_FILL_ARRAY_DATA_PAYLOAD {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            final int read = codeInput.read();
            final int int1 = codeInput.readInt();
            final int n2 = 0;
            final int n3 = 0;
            int i = 0;
            if (read == 4) {
                final int[] array = new int[int1];
                for (int j = n3; j < int1; ++j) {
                    array[j] = codeInput.readInt();
                }
                return new FillArrayDataPayloadDecodedInstruction(this, n, array);
            }
            if (read == 8) {
                final long[] array2 = new long[int1];
                for (int k = n2; k < int1; ++k) {
                    array2[k] = codeInput.readLong();
                }
                return new FillArrayDataPayloadDecodedInstruction(this, n, array2);
            }
            switch (read) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("bogus element_width: ");
                    sb.append(Hex.u2(read));
                    throw new DexException(sb.toString());
                }
                case 2: {
                    final short[] array3 = new short[int1];
                    while (i < int1) {
                        array3[i] = (short)codeInput.read();
                        ++i;
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this, n, array3);
                }
                case 1: {
                    final byte[] array4 = new byte[int1];
                    int l = 0;
                    boolean b = true;
                    int read2 = 0;
                    while (l < int1) {
                        if (b) {
                            read2 = codeInput.read();
                        }
                        array4[l] = (byte)(read2 & 0xFF);
                        final int n4 = read2 >> 8;
                        ++l;
                        b = !b;
                        read2 = n4;
                    }
                    return new FillArrayDataPayloadDecodedInstruction(this, n, array4);
                }
            }
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final FillArrayDataPayloadDecodedInstruction fillArrayDataPayloadDecodedInstruction = (FillArrayDataPayloadDecodedInstruction)decodedInstruction;
            final short elementWidthUnit = fillArrayDataPayloadDecodedInstruction.getElementWidthUnit();
            final Object data = fillArrayDataPayloadDecodedInstruction.getData();
            codeOutput.write(fillArrayDataPayloadDecodedInstruction.getOpcodeUnit());
            codeOutput.write(elementWidthUnit);
            codeOutput.writeInt(fillArrayDataPayloadDecodedInstruction.getSize());
            if (elementWidthUnit == 4) {
                codeOutput.write((int[])data);
                return;
            }
            if (elementWidthUnit == 8) {
                codeOutput.write((long[])data);
                return;
            }
            switch (elementWidthUnit) {
                default: {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("bogus element_width: ");
                    sb.append(Hex.u2(elementWidthUnit));
                    throw new DexException(sb.toString());
                }
                case 2: {
                    codeOutput.write((short[])data);
                }
                case 1: {
                    codeOutput.write((byte[])data);
                }
            }
        }
    }, 
    FORMAT_PACKED_SWITCH_PAYLOAD {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            final int baseAddressForCursor = codeInput.baseAddressForCursor();
            final int read = codeInput.read();
            final int int1 = codeInput.readInt();
            final int[] array = new int[read];
            for (int i = 0; i < read; ++i) {
                array[i] = codeInput.readInt() + (baseAddressForCursor - 1);
            }
            return new PackedSwitchPayloadDecodedInstruction(this, n, int1, array);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final PackedSwitchPayloadDecodedInstruction packedSwitchPayloadDecodedInstruction = (PackedSwitchPayloadDecodedInstruction)decodedInstruction;
            final int[] targets = packedSwitchPayloadDecodedInstruction.getTargets();
            final int baseAddressForCursor = codeOutput.baseAddressForCursor();
            codeOutput.write(packedSwitchPayloadDecodedInstruction.getOpcodeUnit());
            codeOutput.write(asUnsignedUnit(targets.length));
            codeOutput.writeInt(packedSwitchPayloadDecodedInstruction.getFirstKey());
            for (int length = targets.length, i = 0; i < length; ++i) {
                codeOutput.writeInt(targets[i] - baseAddressForCursor);
            }
        }
    }, 
    FORMAT_SPARSE_SWITCH_PAYLOAD {
        @Override
        public DecodedInstruction decode(final int n, final CodeInput codeInput) throws EOFException {
            final int baseAddressForCursor = codeInput.baseAddressForCursor();
            final int read = codeInput.read();
            final int[] array = new int[read];
            final int[] array2 = new int[read];
            final int n2 = 0;
            for (int i = 0; i < read; ++i) {
                array[i] = codeInput.readInt();
            }
            for (int j = n2; j < read; ++j) {
                array2[j] = codeInput.readInt() + (baseAddressForCursor - 1);
            }
            return new SparseSwitchPayloadDecodedInstruction(this, n, array, array2);
        }
        
        @Override
        public void encode(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
            final SparseSwitchPayloadDecodedInstruction sparseSwitchPayloadDecodedInstruction = (SparseSwitchPayloadDecodedInstruction)decodedInstruction;
            final int[] keys = sparseSwitchPayloadDecodedInstruction.getKeys();
            final int[] targets = sparseSwitchPayloadDecodedInstruction.getTargets();
            final int baseAddressForCursor = codeOutput.baseAddressForCursor();
            codeOutput.write(sparseSwitchPayloadDecodedInstruction.getOpcodeUnit());
            codeOutput.write(asUnsignedUnit(targets.length));
            final int length = keys.length;
            final int n = 0;
            for (int i = 0; i < length; ++i) {
                codeOutput.writeInt(keys[i]);
            }
            for (int length2 = targets.length, j = n; j < length2; ++j) {
                codeOutput.writeInt(targets[j] - baseAddressForCursor);
            }
        }
    };
    
    private static short asUnsignedUnit(final int n) {
        if ((0xFFFF0000 & n) != 0x0) {
            throw new IllegalArgumentException("bogus unsigned code unit");
        }
        return (short)n;
    }
    
    private static int byte0(final int n) {
        return n & 0xFF;
    }
    
    private static int byte1(final int n) {
        return n >> 8 & 0xFF;
    }
    
    private static int byte2(final int n) {
        return n >> 16 & 0xFF;
    }
    
    private static int byte3(final int n) {
        return n >>> 24;
    }
    
    private static short codeUnit(final int n, final int n2) {
        if ((n & 0xFFFFFF00) != 0x0) {
            throw new IllegalArgumentException("bogus lowByte");
        }
        if ((n2 & 0xFFFFFF00) != 0x0) {
            throw new IllegalArgumentException("bogus highByte");
        }
        return (short)(n2 << 8 | n);
    }
    
    private static short codeUnit(final int n, final int n2, final int n3, final int n4) {
        if ((n & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus nibble0");
        }
        if ((n2 & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus nibble1");
        }
        if ((n3 & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus nibble2");
        }
        if ((n4 & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus nibble3");
        }
        return (short)(n2 << 4 | n | n3 << 8 | n4 << 12);
    }
    
    private static DecodedInstruction decodeRegisterList(final InstructionCodec instructionCodec, int nibble3, final CodeInput codeInput) throws EOFException {
        final int byte0 = byte0(nibble3);
        final int nibble4 = nibble2(nibble3);
        nibble3 = nibble3(nibble3);
        final int read = codeInput.read();
        final int read2 = codeInput.read();
        final int nibble5 = nibble0(read2);
        final int nibble6 = nibble1(read2);
        final int nibble7 = nibble2(read2);
        final int nibble8 = nibble3(read2);
        final IndexType indexType = OpcodeInfo.getIndexType(byte0);
        switch (nibble3) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("bogus registerCount: ");
                sb.append(Hex.uNibble(nibble3));
                throw new DexException(sb.toString());
            }
            case 5: {
                return new FiveRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L, nibble5, nibble6, nibble7, nibble8, nibble4);
            }
            case 4: {
                return new FourRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L, nibble5, nibble6, nibble7, nibble8);
            }
            case 3: {
                return new ThreeRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L, nibble5, nibble6, nibble7);
            }
            case 2: {
                return new TwoRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L, nibble5, nibble6);
            }
            case 1: {
                return new OneRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L, nibble5);
            }
            case 0: {
                return new ZeroRegisterDecodedInstruction(instructionCodec, byte0, read, indexType, 0, 0L);
            }
        }
    }
    
    private static DecodedInstruction decodeRegisterRange(final InstructionCodec instructionCodec, int byte1, final CodeInput codeInput) throws EOFException {
        final int byte2 = byte0(byte1);
        byte1 = byte1(byte1);
        return new RegisterRangeDecodedInstruction(instructionCodec, byte2, codeInput.read(), OpcodeInfo.getIndexType(byte2), 0, 0L, codeInput.read(), byte1);
    }
    
    private static void encodeRegisterList(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
        codeOutput.write(codeUnit(decodedInstruction.getOpcode(), makeByte(decodedInstruction.getE(), decodedInstruction.getRegisterCount())), decodedInstruction.getIndexUnit(), codeUnit(decodedInstruction.getA(), decodedInstruction.getB(), decodedInstruction.getC(), decodedInstruction.getD()));
    }
    
    private static void encodeRegisterRange(final DecodedInstruction decodedInstruction, final CodeOutput codeOutput) {
        codeOutput.write(codeUnit(decodedInstruction.getOpcode(), decodedInstruction.getRegisterCount()), decodedInstruction.getIndexUnit(), decodedInstruction.getAUnit());
    }
    
    private static int makeByte(final int n, final int n2) {
        if ((n & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus lowNibble");
        }
        if ((n2 & 0xFFFFFFF0) != 0x0) {
            throw new IllegalArgumentException("bogus highNibble");
        }
        return n2 << 4 | n;
    }
    
    private static int nibble0(final int n) {
        return n & 0xF;
    }
    
    private static int nibble1(final int n) {
        return n >> 4 & 0xF;
    }
    
    private static int nibble2(final int n) {
        return n >> 8 & 0xF;
    }
    
    private static int nibble3(final int n) {
        return n >> 12 & 0xF;
    }
    
    private static short unit0(final int n) {
        return (short)n;
    }
    
    private static short unit0(final long n) {
        return (short)n;
    }
    
    private static short unit1(final int n) {
        return (short)(n >> 16);
    }
    
    private static short unit1(final long n) {
        return (short)(n >> 16);
    }
    
    private static short unit2(final long n) {
        return (short)(n >> 32);
    }
    
    private static short unit3(final long n) {
        return (short)(n >> 48);
    }
    
    public abstract DecodedInstruction decode(final int p0, final CodeInput p1) throws EOFException;
    
    public abstract void encode(final DecodedInstruction p0, final CodeOutput p1);
}
