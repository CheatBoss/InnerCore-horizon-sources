package java.time.chrono;

import java.io.*;

final class Ser implements Externalizable
{
    private static final long serialVersionUID = -6103370247208168577L;
    static final byte CHRONO_TYPE = 1;
    static final byte CHRONO_LOCAL_DATE_TIME_TYPE = 2;
    static final byte CHRONO_ZONE_DATE_TIME_TYPE = 3;
    static final byte JAPANESE_DATE_TYPE = 4;
    static final byte JAPANESE_ERA_TYPE = 5;
    static final byte HIJRAH_DATE_TYPE = 6;
    static final byte MINGUO_DATE_TYPE = 7;
    static final byte THAIBUDDHIST_DATE_TYPE = 8;
    static final byte CHRONO_PERIOD_TYPE = 9;
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
    
    private static void writeInternal(final byte b, final Object o, final ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(b);
        switch (b) {
            case 1: {
                ((AbstractChronology)o).writeExternal(objectOutput);
                break;
            }
            case 2: {
                ((ChronoLocalDateTimeImpl)o).writeExternal(objectOutput);
                break;
            }
            case 3: {
                ((ChronoZonedDateTimeImpl)o).writeExternal(objectOutput);
                break;
            }
            case 4: {
                ((JapaneseDate)o).writeExternal(objectOutput);
                break;
            }
            case 5: {
                ((JapaneseEra)o).writeExternal(objectOutput);
                break;
            }
            case 6: {
                ((HijrahDate)o).writeExternal(objectOutput);
                break;
            }
            case 7: {
                ((MinguoDate)o).writeExternal(objectOutput);
                break;
            }
            case 8: {
                ((ThaiBuddhistDate)o).writeExternal(objectOutput);
                break;
            }
            case 9: {
                ((ChronoPeriodImpl)o).writeExternal(objectOutput);
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
    
    static Object read(final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        return readInternal(objectInput.readByte(), objectInput);
    }
    
    private static Object readInternal(final byte b, final ObjectInput objectInput) throws IOException, ClassNotFoundException {
        switch (b) {
            case 1: {
                return AbstractChronology.readExternal(objectInput);
            }
            case 2: {
                return ChronoLocalDateTimeImpl.readExternal(objectInput);
            }
            case 3: {
                return ChronoZonedDateTimeImpl.readExternal(objectInput);
            }
            case 4: {
                return JapaneseDate.readExternal(objectInput);
            }
            case 5: {
                return JapaneseEra.readExternal(objectInput);
            }
            case 6: {
                return HijrahDate.readExternal(objectInput);
            }
            case 7: {
                return MinguoDate.readExternal(objectInput);
            }
            case 8: {
                return ThaiBuddhistDate.readExternal(objectInput);
            }
            case 9: {
                return ChronoPeriodImpl.readExternal(objectInput);
            }
            default: {
                throw new StreamCorruptedException("Unknown serialized type");
            }
        }
    }
    
    private Object readResolve() {
        return this.object;
    }
}
