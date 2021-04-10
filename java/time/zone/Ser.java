package java.time.zone;

import java.io.*;
import java.time.*;

final class Ser implements Externalizable
{
    private static final long serialVersionUID = -8885321777449118786L;
    static final byte ZRULES = 1;
    static final byte ZOT = 2;
    static final byte ZOTRULE = 3;
    private byte type;
    private Object object;
    
    public Ser() {
    }
    
    Ser(final byte type, final Object object) {
        this.type = type;
        this.object = object;
    }
    
    @Override
    public void writeExternal(final ObjectOutput objectOutput) throws IOException {
        writeInternal(this.type, this.object, objectOutput);
    }
    
    static void write(final Object o, final DataOutput dataOutput) throws IOException {
        writeInternal((byte)1, o, dataOutput);
    }
    
    private static void writeInternal(final byte b, final Object o, final DataOutput dataOutput) throws IOException {
        dataOutput.writeByte(b);
        switch (b) {
            case 1: {
                ((ZoneRules)o).writeExternal(dataOutput);
                break;
            }
            case 2: {
                ((ZoneOffsetTransition)o).writeExternal(dataOutput);
                break;
            }
            case 3: {
                ((ZoneOffsetTransitionRule)o).writeExternal(dataOutput);
                break;
            }
            default: {
                throw new InvalidClassException("Unknown serialized type");
            }
        }
    }
    
    @Override
    public void readExternal(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        this.type = objectInput.readByte();
        this.object = readInternal(this.type, objectInput);
    }
    
    static Object read(final DataInput dataInput) throws IOException, ClassNotFoundException {
        return readInternal(dataInput.readByte(), dataInput);
    }
    
    private static Object readInternal(final byte b, final DataInput dataInput) throws IOException, ClassNotFoundException {
        switch (b) {
            case 1: {
                return ZoneRules.readExternal(dataInput);
            }
            case 2: {
                return ZoneOffsetTransition.readExternal(dataInput);
            }
            case 3: {
                return ZoneOffsetTransitionRule.readExternal(dataInput);
            }
            default: {
                throw new StreamCorruptedException("Unknown serialized type");
            }
        }
    }
    
    private Object readResolve() {
        return this.object;
    }
    
    static void writeOffset(final ZoneOffset zoneOffset, final DataOutput dataOutput) throws IOException {
        final int totalSeconds = zoneOffset.getTotalSeconds();
        final int n = (totalSeconds % 900 == 0) ? (totalSeconds / 900) : 127;
        dataOutput.writeByte(n);
        if (n == 127) {
            dataOutput.writeInt(totalSeconds);
        }
    }
    
    static ZoneOffset readOffset(final DataInput dataInput) throws IOException {
        final byte byte1 = dataInput.readByte();
        return (byte1 == 127) ? ZoneOffset.ofTotalSeconds(dataInput.readInt()) : ZoneOffset.ofTotalSeconds(byte1 * 900);
    }
    
    static void writeEpochSec(final long n, final DataOutput dataOutput) throws IOException {
        if (n >= -4575744000L && n < 10413792000L && n % 900L == 0L) {
            final int n2 = (int)((n + 4575744000L) / 900L);
            dataOutput.writeByte(n2 >>> 16 & 0xFF);
            dataOutput.writeByte(n2 >>> 8 & 0xFF);
            dataOutput.writeByte(n2 & 0xFF);
        }
        else {
            dataOutput.writeByte(255);
            dataOutput.writeLong(n);
        }
    }
    
    static long readEpochSec(final DataInput dataInput) throws IOException {
        final int n = dataInput.readByte() & 0xFF;
        if (n == 255) {
            return dataInput.readLong();
        }
        return ((n << 16) + ((dataInput.readByte() & 0xFF) << 8) + (dataInput.readByte() & 0xFF)) * 900L - 4575744000L;
    }
}
