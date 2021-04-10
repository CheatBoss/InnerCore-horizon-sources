package com.google.android.gms.common.internal.safeparcel;

import android.os.*;
import java.util.*;

public class SafeParcelWriter
{
    public static int beginObjectHeader(final Parcel parcel) {
        return zza(parcel, 20293);
    }
    
    public static void finishObjectHeader(final Parcel parcel, final int n) {
        zzb(parcel, n);
    }
    
    public static void writeBoolean(final Parcel parcel, final int n, final boolean b) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: \r\n\tat com.googlecode.dex2jar.ir.ts.NewTransformer.transform(NewTransformer.java:134)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:148)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    public static void writeBundle(final Parcel parcel, int zza, final Bundle bundle, final boolean b) {
        if (bundle == null) {
            if (b) {
                zzb(parcel, zza, 0);
            }
            return;
        }
        zza = zza(parcel, zza);
        parcel.writeBundle(bundle);
        zzb(parcel, zza);
    }
    
    public static void writeDoubleObject(final Parcel parcel, final int n, final Double n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 8);
        parcel.writeDouble((double)n2);
    }
    
    public static void writeFloatObject(final Parcel parcel, final int n, final Float n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 4);
        parcel.writeFloat((float)n2);
    }
    
    public static void writeIBinder(final Parcel parcel, int zza, final IBinder binder, final boolean b) {
        if (binder == null) {
            if (b) {
                zzb(parcel, zza, 0);
            }
            return;
        }
        zza = zza(parcel, zza);
        parcel.writeStrongBinder(binder);
        zzb(parcel, zza);
    }
    
    public static void writeInt(final Parcel parcel, final int n, final int n2) {
        zzb(parcel, n, 4);
        parcel.writeInt(n2);
    }
    
    public static void writeIntegerObject(final Parcel parcel, final int n, final Integer n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 4);
        parcel.writeInt((int)n2);
    }
    
    public static void writeLong(final Parcel parcel, final int n, final long n2) {
        zzb(parcel, n, 8);
        parcel.writeLong(n2);
    }
    
    public static void writeLongObject(final Parcel parcel, final int n, final Long n2, final boolean b) {
        if (n2 == null) {
            if (b) {
                zzb(parcel, n, 0);
            }
            return;
        }
        zzb(parcel, n, 8);
        parcel.writeLong((long)n2);
    }
    
    public static void writeParcelable(final Parcel parcel, int zza, final Parcelable parcelable, final int n, final boolean b) {
        if (parcelable == null) {
            if (b) {
                zzb(parcel, zza, 0);
            }
            return;
        }
        zza = zza(parcel, zza);
        parcelable.writeToParcel(parcel, n);
        zzb(parcel, zza);
    }
    
    public static void writeString(final Parcel parcel, int zza, final String s, final boolean b) {
        if (s == null) {
            if (b) {
                zzb(parcel, zza, 0);
            }
            return;
        }
        zza = zza(parcel, zza);
        parcel.writeString(s);
        zzb(parcel, zza);
    }
    
    public static <T extends Parcelable> void writeTypedArray(final Parcel parcel, int i, final T[] array, final int n, final boolean b) {
        if (array == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zza = zza(parcel, i);
        final int length = array.length;
        parcel.writeInt(length);
        Parcelable parcelable;
        for (i = 0; i < length; ++i) {
            parcelable = array[i];
            if (parcelable == null) {
                parcel.writeInt(0);
            }
            else {
                zza(parcel, parcelable, n);
            }
        }
        zzb(parcel, zza);
    }
    
    public static <T extends Parcelable> void writeTypedList(final Parcel parcel, int i, final List<T> list, final boolean b) {
        if (list == null) {
            if (b) {
                zzb(parcel, i, 0);
            }
            return;
        }
        final int zza = zza(parcel, i);
        final int size = list.size();
        parcel.writeInt(size);
        Parcelable parcelable;
        for (i = 0; i < size; ++i) {
            parcelable = list.get(i);
            if (parcelable == null) {
                parcel.writeInt(0);
            }
            else {
                zza(parcel, parcelable, 0);
            }
        }
        zzb(parcel, zza);
    }
    
    private static int zza(final Parcel parcel, final int n) {
        parcel.writeInt(n | 0xFFFF0000);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }
    
    private static <T extends Parcelable> void zza(final Parcel parcel, final T t, int dataPosition) {
        final int dataPosition2 = parcel.dataPosition();
        parcel.writeInt(1);
        final int dataPosition3 = parcel.dataPosition();
        t.writeToParcel(parcel, dataPosition);
        dataPosition = parcel.dataPosition();
        parcel.setDataPosition(dataPosition2);
        parcel.writeInt(dataPosition - dataPosition3);
        parcel.setDataPosition(dataPosition);
    }
    
    private static void zzb(final Parcel parcel, final int n) {
        final int dataPosition = parcel.dataPosition();
        parcel.setDataPosition(n - 4);
        parcel.writeInt(dataPosition - n);
        parcel.setDataPosition(dataPosition);
    }
    
    private static void zzb(final Parcel parcel, final int n, final int n2) {
        if (n2 >= 65535) {
            parcel.writeInt(n | 0xFFFF0000);
            parcel.writeInt(n2);
            return;
        }
        parcel.writeInt(n | n2 << 16);
    }
}
