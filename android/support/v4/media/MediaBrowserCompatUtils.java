package android.support.v4.media;

import java.util.*;
import android.os.*;

public class MediaBrowserCompatUtils
{
    public static List<MediaBrowserCompat.MediaItem> applyOptions(final List<MediaBrowserCompat.MediaItem> list, final Bundle bundle) {
        final int int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        final int int2 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        if (int1 == -1 && int2 == -1) {
            return list;
        }
        final int n = (int1 - 1) * int2;
        final int n2 = n + int2;
        if (int1 >= 1 && int2 >= 1 && n < list.size()) {
            int size;
            if ((size = n2) > list.size()) {
                size = list.size();
            }
            return list.subList(n, size);
        }
        return null;
    }
    
    public static boolean areSameOptions(final Bundle bundle, final Bundle bundle2) {
        if (bundle != bundle2) {
            if (bundle == null) {
                if (bundle2.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
                    return false;
                }
            }
            else if (bundle2 == null) {
                if (bundle.getInt("android.media.browse.extra.PAGE", -1) != -1 || bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != -1) {
                    return false;
                }
            }
            else {
                if (bundle.getInt("android.media.browse.extra.PAGE", -1) != bundle2.getInt("android.media.browse.extra.PAGE", -1)) {
                    return false;
                }
                if (bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1) != bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean hasDuplicatedItems(final Bundle bundle, final Bundle bundle2) {
        int int1;
        if (bundle == null) {
            int1 = -1;
        }
        else {
            int1 = bundle.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int2;
        if (bundle2 == null) {
            int2 = -1;
        }
        else {
            int2 = bundle2.getInt("android.media.browse.extra.PAGE", -1);
        }
        int int3;
        if (bundle == null) {
            int3 = -1;
        }
        else {
            int3 = bundle.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        int int4;
        if (bundle2 == null) {
            int4 = -1;
        }
        else {
            int4 = bundle2.getInt("android.media.browse.extra.PAGE_SIZE", -1);
        }
        final int n = Integer.MAX_VALUE;
        int n2;
        int n3;
        if (int1 != -1 && int3 != -1) {
            n2 = (int1 - 1) * int3;
            n3 = int3 + n2 - 1;
        }
        else {
            n3 = Integer.MAX_VALUE;
            n2 = 0;
        }
        int n4;
        int n5;
        if (int2 != -1 && int4 != -1) {
            n4 = int4 * (int2 - 1);
            n5 = int4 + n4 - 1;
        }
        else {
            n4 = 0;
            n5 = n;
        }
        return (n2 <= n4 && n4 <= n3) || (n2 <= n5 && n5 <= n3);
    }
}
