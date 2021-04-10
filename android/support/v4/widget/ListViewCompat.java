package android.support.v4.widget;

import android.widget.*;
import android.support.annotation.*;
import android.os.*;

public final class ListViewCompat
{
    private ListViewCompat() {
    }
    
    public static void scrollListBy(@NonNull final ListView listView, final int n) {
        if (Build$VERSION.SDK_INT >= 19) {
            ListViewCompatKitKat.scrollListBy(listView, n);
            return;
        }
        ListViewCompatDonut.scrollListBy(listView, n);
    }
}
