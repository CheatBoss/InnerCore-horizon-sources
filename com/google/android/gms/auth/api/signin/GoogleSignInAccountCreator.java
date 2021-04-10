package com.google.android.gms.auth.api.signin;

import android.os.*;
import com.google.android.gms.common.internal.safeparcel.*;
import com.google.android.gms.common.api.*;
import android.net.*;
import java.util.*;

public class GoogleSignInAccountCreator implements Parcelable$Creator<GoogleSignInAccount>
{
    public GoogleSignInAccount createFromParcel(final Parcel parcel) {
        final int validateObjectHeader = SafeParcelReader.validateObjectHeader(parcel);
        Object string2;
        Object string = string2 = null;
        Object string4;
        String string3 = (String)(string4 = string2);
        Object string5;
        Object o = string5 = string4;
        Object typedList;
        String string6 = (String)(typedList = string5);
        Object string8;
        Object string7 = string8 = typedList;
        long long1 = 0L;
        int int1 = 0;
        while (parcel.dataPosition() < validateObjectHeader) {
            final int header = SafeParcelReader.readHeader(parcel);
            switch (SafeParcelReader.getFieldId(header)) {
                default: {
                    SafeParcelReader.skipUnknownField(parcel, header);
                    continue;
                }
                case 12: {
                    string8 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 11: {
                    string7 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 10: {
                    typedList = SafeParcelReader.createTypedList(parcel, header, Scope.CREATOR);
                    continue;
                }
                case 9: {
                    string6 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 8: {
                    long1 = SafeParcelReader.readLong(parcel, header);
                    continue;
                }
                case 7: {
                    string5 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 6: {
                    o = SafeParcelReader.createParcelable(parcel, header, (android.os.Parcelable$Creator<Uri>)Uri.CREATOR);
                    continue;
                }
                case 5: {
                    string4 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 4: {
                    string3 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 3: {
                    string2 = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 2: {
                    string = SafeParcelReader.createString(parcel, header);
                    continue;
                }
                case 1: {
                    int1 = SafeParcelReader.readInt(parcel, header);
                    continue;
                }
            }
        }
        SafeParcelReader.ensureAtEnd(parcel, validateObjectHeader);
        return new GoogleSignInAccount(int1, (String)string, (String)string2, string3, (String)string4, (Uri)o, (String)string5, long1, string6, (List<Scope>)typedList, (String)string7, (String)string8);
    }
    
    public GoogleSignInAccount[] newArray(final int n) {
        return new GoogleSignInAccount[n];
    }
}
