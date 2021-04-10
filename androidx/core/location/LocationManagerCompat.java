package androidx.core.location;

import android.location.*;
import androidx.annotation.*;
import android.os.*;

public final class LocationManagerCompat
{
    private LocationManagerCompat() {
    }
    
    public static boolean isLocationEnabled(@NonNull final LocationManager locationManager) {
        if (Build$VERSION.SDK_INT >= 28) {
            return locationManager.isLocationEnabled();
        }
        return locationManager.isProviderEnabled("network") || locationManager.isProviderEnabled("gps");
    }
}
