package com.google.android.gms.common.internal.safeparcel;

import java.util.*;
import android.os.*;

public class SafeParcelReader
{
    public static Bundle createBundle(final Parcel parcel, int size) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final Bundle bundle = parcel.readBundle();
        parcel.setDataPosition(dataPosition + size);
        return bundle;
    }
    
    public static <T extends Parcelable> T createParcelable(final Parcel parcel, int size, final Parcelable$Creator<T> parcelable$Creator) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final Parcelable parcelable = (Parcelable)parcelable$Creator.createFromParcel(parcel);
        parcel.setDataPosition(dataPosition + size);
        return (T)parcelable;
    }
    
    public static String createString(final Parcel parcel, int size) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final String string = parcel.readString();
        parcel.setDataPosition(dataPosition + size);
        return string;
    }
    
    public static <T> T[] createTypedArray(final Parcel parcel, int size, final Parcelable$Creator<T> parcelable$Creator) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final Object[] typedArray = parcel.createTypedArray((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(dataPosition + size);
        return (T[])typedArray;
    }
    
    public static <T> ArrayList<T> createTypedList(final Parcel parcel, int size, final Parcelable$Creator<T> parcelable$Creator) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final ArrayList typedArrayList = parcel.createTypedArrayList((Parcelable$Creator)parcelable$Creator);
        parcel.setDataPosition(dataPosition + size);
        return (ArrayList<T>)typedArrayList;
    }
    
    public static void ensureAtEnd(final Parcel parcel, final int n) {
        if (parcel.dataPosition() == n) {
            return;
        }
        final StringBuilder sb = new StringBuilder(37);
        sb.append("Overread allowed size end=");
        sb.append(n);
        throw new ParseException(sb.toString(), parcel);
    }
    
    public static int getFieldId(final int n) {
        return n & 0xFFFF;
    }
    
    public static boolean readBoolean(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt() != 0;
    }
    
    public static Double readDoubleObject(final Parcel parcel, final int n) {
        final int size = readSize(parcel, n);
        if (size == 0) {
            return null;
        }
        zza(parcel, n, size, 8);
        return parcel.readDouble();
    }
    
    public static Float readFloatObject(final Parcel parcel, final int n) {
        final int size = readSize(parcel, n);
        if (size == 0) {
            return null;
        }
        zza(parcel, n, size, 4);
        return parcel.readFloat();
    }
    
    public static int readHeader(final Parcel parcel) {
        return parcel.readInt();
    }
    
    public static IBinder readIBinder(final Parcel parcel, int size) {
        size = readSize(parcel, size);
        final int dataPosition = parcel.dataPosition();
        if (size == 0) {
            return null;
        }
        final IBinder strongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(dataPosition + size);
        return strongBinder;
    }
    
    public static int readInt(final Parcel parcel, final int n) {
        zza(parcel, n, 4);
        return parcel.readInt();
    }
    
    public static Integer readIntegerObject(final Parcel parcel, final int n) {
        final int size = readSize(parcel, n);
        if (size == 0) {
            return null;
        }
        zza(parcel, n, size, 4);
        return parcel.readInt();
    }
    
    public static long readLong(final Parcel parcel, final int n) {
        zza(parcel, n, 8);
        return parcel.readLong();
    }
    
    public static Long readLongObject(final Parcel parcel, final int n) {
        final int size = readSize(parcel, n);
        if (size == 0) {
            return null;
        }
        zza(parcel, n, size, 8);
        return parcel.readLong();
    }
    
    public static int readSize(final Parcel parcel, final int n) {
        if ((n & 0xFFFF0000) != 0xFFFF0000) {
            return n >> 16 & 0xFFFF;
        }
        return parcel.readInt();
    }
    
    public static void skipUnknownField(final Parcel parcel, int size) {
        size = readSize(parcel, size);
        parcel.setDataPosition(parcel.dataPosition() + size);
    }
    
    public static int validateObjectHeader(final Parcel parcel) {
        final int header = readHeader(parcel);
        final int size = readSize(parcel, header);
        final int dataPosition = parcel.dataPosition();
        if (getFieldId(header) != 20293) {
            final String value = String.valueOf(Integer.toHexString(header));
            String concat;
            if (value.length() != 0) {
                concat = "Expected object header. Got 0x".concat(value);
            }
            else {
                concat = new String("Expected object header. Got 0x");
            }
            throw new ParseException(concat, parcel);
        }
        final int n = size + dataPosition;
        if (n >= dataPosition && n <= parcel.dataSize()) {
            return n;
        }
        final StringBuilder sb = new StringBuilder(54);
        sb.append("Size read is invalid start=");
        sb.append(dataPosition);
        sb.append(" end=");
        sb.append(n);
        throw new ParseException(sb.toString(), parcel);
    }
    
    private static void zza(final Parcel parcel, int size, final int n) {
        size = readSize(parcel, size);
        if (size == n) {
            return;
        }
        final String hexString = Integer.toHexString(size);
        final StringBuilder sb = new StringBuilder(String.valueOf(hexString).length() + 46);
        sb.append("Expected size ");
        sb.append(n);
        sb.append(" got ");
        sb.append(size);
        sb.append(" (0x");
        sb.append(hexString);
        sb.append(")");
        throw new ParseException(sb.toString(), parcel);
    }
    
    private static void zza(final Parcel parcel, final int n, final int n2, final int n3) {
        if (n2 == n3) {
            return;
        }
        final String hexString = Integer.toHexString(n2);
        final StringBuilder sb = new StringBuilder(String.valueOf(hexString).length() + 46);
        sb.append("Expected size ");
        sb.append(n3);
        sb.append(" got ");
        sb.append(n2);
        sb.append(" (0x");
        sb.append(hexString);
        sb.append(")");
        throw new ParseException(sb.toString(), parcel);
    }
    
    public static class ParseException extends RuntimeException
    {
        public ParseException(final String s, final Parcel parcel) {
            final int dataPosition = parcel.dataPosition();
            final int dataSize = parcel.dataSize();
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 41);
            sb.append(s);
            sb.append(" Parcel: pos=");
            sb.append(dataPosition);
            sb.append(" size=");
            sb.append(dataSize);
            super(sb.toString());
        }
    }
}
