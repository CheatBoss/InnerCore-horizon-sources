package androidx.core.os;

import android.os.*;

@Deprecated
public final class ParcelableCompat
{
    private ParcelableCompat() {
    }
    
    @Deprecated
    public static <T> Parcelable$Creator<T> newCreator(final ParcelableCompatCreatorCallbacks<T> parcelableCompatCreatorCallbacks) {
        return (Parcelable$Creator<T>)new ParcelableCompatCreatorHoneycombMR2((ParcelableCompatCreatorCallbacks<Object>)parcelableCompatCreatorCallbacks);
    }
    
    static class ParcelableCompatCreatorHoneycombMR2<T> implements Parcelable$ClassLoaderCreator<T>
    {
        private final ParcelableCompatCreatorCallbacks<T> mCallbacks;
        
        ParcelableCompatCreatorHoneycombMR2(final ParcelableCompatCreatorCallbacks<T> mCallbacks) {
            this.mCallbacks = mCallbacks;
        }
        
        public T createFromParcel(final Parcel parcel) {
            return this.mCallbacks.createFromParcel(parcel, null);
        }
        
        public T createFromParcel(final Parcel parcel, final ClassLoader classLoader) {
            return this.mCallbacks.createFromParcel(parcel, classLoader);
        }
        
        public T[] newArray(final int n) {
            return this.mCallbacks.newArray(n);
        }
    }
}
