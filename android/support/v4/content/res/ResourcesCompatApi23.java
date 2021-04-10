package android.support.v4.content.res;

import android.content.res.*;

class ResourcesCompatApi23
{
    public static int getColor(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        return resources.getColor(n, resources$Theme);
    }
    
    public static ColorStateList getColorStateList(final Resources resources, final int n, final Resources$Theme resources$Theme) throws Resources$NotFoundException {
        return resources.getColorStateList(n, resources$Theme);
    }
}
