package com.google.android.gms.common.internal;

import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.*;
import android.accounts.*;
import com.google.android.gms.common.api.*;
import android.os.*;

public class GetServiceRequestCreator implements Parcelable$Creator<GetServiceRequest>
{
    public GetServiceRequest createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Object string = null;
        Object o;
        Object iBinder = o = string;
        Object o2;
        Object bundle = o2 = o;
        Object o4;
        Object o3 = o4 = o2;
        int int1 = 0;
        int int2 = 0;
        int int3 = 0;
        boolean boolean1 = false;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(header)) {
                default: {
                    SafeParcelReader.skipUnknownField(parcel, header);
                    continue;
                }
                case 12: {
                    boolean1 = SafeParcelReader.readBoolean(parcel, header);
                    continue;
                }
                case 11: {
                    o4 = SafeParcelReader.createTypedArray(parcel, header, Feature.CREATOR);
                    continue;
                }
                case 10: {
                    o3 = SafeParcelReader.createTypedArray(parcel, header, Feature.CREATOR);
                    continue;
                }
                case 8: {
                    o2 = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<Account>)Account.CREATOR);
                    continue;
                }
                case 7: {
                    bundle = SafeParcelReader.createBundle(parcel, header);
                    continue;
                }
                case 6: {
                    o = SafeParcelReader.createTypedArray(parcel, header, Scope.CREATOR);
                    continue;
                }
                case 5: {
                    iBinder = SafeParcelReader.readIBinder(parcel, header);
                    continue;
                }
                case 4: {
                    string = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 3: {
                    int3 = SafeParcelReader.readInt(parcel, header);
                    continue;
                }
                case 2: {
                    int2 = SafeParcelReader.readInt(parcel, header);
                    continue;
                }
                case 1: {
                    int1 = SafeParcelReader.readInt(parcel, header);
                    continue;
                }
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new GetServiceRequest(int1, int2, int3, (String)string, (IBinder)iBinder, (Scope[])o, (Bundle)bundle, (Account)o2, (Feature[])o3, (Feature[])o4, boolean1);
    }
    
    public GetServiceRequest[] newArray(final int n) {
        return new GetServiceRequest[n];
    }
}
