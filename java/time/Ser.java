package java.time;

import java.io.*;

final class Ser implements Externalizable
{
    private static final long serialVersionUID = -7683839454370182990L;
    static final byte DURATION_TYPE = 1;
    static final byte INSTANT_TYPE = 2;
    static final byte LOCAL_DATE_TYPE = 3;
    static final byte LOCAL_TIME_TYPE = 4;
    static final byte LOCAL_DATE_TIME_TYPE = 5;
    static final byte ZONE_DATE_TIME_TYPE = 6;
    static final byte ZONE_REGION_TYPE = 7;
    static final byte ZONE_OFFSET_TYPE = 8;
    static final byte OFFSET_TIME_TYPE = 9;
    static final byte OFFSET_DATE_TIME_TYPE = 10;
    static final byte YEAR_TYPE = 11;
    static final byte YEAR_MONTH_TYPE = 12;
    static final byte MONTH_DAY_TYPE = 13;
    static final byte PERIOD_TYPE = 14;
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
    
    static void writeInternal(final byte b, final Object o, final ObjectOutput objectOutput) throws IOException {
        objectOutput.writeByte(b);
        switch (b) {
            case 1: {
                ((Duration)o).writeExternal(objectOutput);
                break;
            }
            case 2: {
                ((Instant)o).writeExternal(objectOutput);
                break;
            }
            case 3: {
                ((LocalDate)o).writeExternal(objectOutput);
                break;
            }
            case 5: {
                ((LocalDateTime)o).writeExternal(objectOutput);
                break;
            }
            case 4: {
                ((LocalTime)o).writeExternal(objectOutput);
                break;
            }
            case 7: {
                ((ZoneRegion)o).writeExternal(objectOutput);
                break;
            }
            case 8: {
                ((ZoneOffset)o).writeExternal(objectOutput);
                break;
            }
            case 6: {
                ((ZonedDateTime)o).writeExternal(objectOutput);
                break;
            }
            case 9: {
                ((OffsetTime)o).writeExternal(objectOutput);
                break;
            }
            case 10: {
                ((OffsetDateTime)o).writeExternal(objectOutput);
                break;
            }
            case 11: {
                ((Year)o).writeExternal(objectOutput);
                break;
            }
            case 12: {
                ((YearMonth)o).writeExternal(objectOutput);
                break;
            }
            case 13: {
                ((MonthDay)o).writeExternal(objectOutput);
                break;
            }
            case 14: {
                ((Period)o).writeExternal(objectOutput);
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
                return Duration.readExternal(objectInput);
            }
            case 2: {
                return Instant.readExternal(objectInput);
            }
            case 3: {
                return LocalDate.readExternal(objectInput);
            }
            case 5: {
                return LocalDateTime.readExternal(objectInput);
            }
            case 4: {
                return LocalTime.readExternal(objectInput);
            }
            case 6: {
                return ZonedDateTime.readExternal(objectInput);
            }
            case 8: {
                return ZoneOffset.readExternal(objectInput);
            }
            case 7: {
                return ZoneRegion.readExternal(objectInput);
            }
            case 9: {
                return OffsetTime.readExternal(objectInput);
            }
            case 10: {
                return OffsetDateTime.readExternal(objectInput);
            }
            case 11: {
                return Year.readExternal(objectInput);
            }
            case 12: {
                return YearMonth.readExternal(objectInput);
            }
            case 13: {
                return MonthDay.readExternal(objectInput);
            }
            case 14: {
                return Period.readExternal(objectInput);
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
