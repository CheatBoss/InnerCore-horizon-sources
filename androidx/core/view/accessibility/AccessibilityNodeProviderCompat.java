package androidx.core.view.accessibility;

import android.os.*;
import androidx.annotation.*;
import android.view.accessibility.*;
import java.util.*;

public class AccessibilityNodeProviderCompat
{
    public static final int HOST_VIEW_ID = -1;
    private final Object mProvider;
    
    public AccessibilityNodeProviderCompat() {
        if (Build$VERSION.SDK_INT >= 19) {
            this.mProvider = new AccessibilityNodeProviderApi19(this);
            return;
        }
        if (Build$VERSION.SDK_INT >= 16) {
            this.mProvider = new AccessibilityNodeProviderApi16(this);
            return;
        }
        this.mProvider = null;
    }
    
    public AccessibilityNodeProviderCompat(final Object mProvider) {
        this.mProvider = mProvider;
    }
    
    @Nullable
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(final int n) {
        return null;
    }
    
    @Nullable
    public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(final String s, final int n) {
        return null;
    }
    
    @Nullable
    public AccessibilityNodeInfoCompat findFocus(final int n) {
        return null;
    }
    
    public Object getProvider() {
        return this.mProvider;
    }
    
    public boolean performAction(final int n, final int n2, final Bundle bundle) {
        return false;
    }
    
    @RequiresApi(16)
    static class AccessibilityNodeProviderApi16 extends AccessibilityNodeProvider
    {
        final AccessibilityNodeProviderCompat mCompat;
        
        AccessibilityNodeProviderApi16(final AccessibilityNodeProviderCompat mCompat) {
            this.mCompat = mCompat;
        }
        
        public AccessibilityNodeInfo createAccessibilityNodeInfo(final int n) {
            final AccessibilityNodeInfoCompat accessibilityNodeInfo = this.mCompat.createAccessibilityNodeInfo(n);
            if (accessibilityNodeInfo == null) {
                return null;
            }
            return accessibilityNodeInfo.unwrap();
        }
        
        public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(final String s, int i) {
            final List<AccessibilityNodeInfoCompat> accessibilityNodeInfosByText = this.mCompat.findAccessibilityNodeInfosByText(s, i);
            if (accessibilityNodeInfosByText == null) {
                return null;
            }
            final ArrayList<AccessibilityNodeInfo> list = new ArrayList<AccessibilityNodeInfo>();
            int size;
            for (size = accessibilityNodeInfosByText.size(), i = 0; i < size; ++i) {
                list.add(accessibilityNodeInfosByText.get(i).unwrap());
            }
            return list;
        }
        
        public boolean performAction(final int n, final int n2, final Bundle bundle) {
            return this.mCompat.performAction(n, n2, bundle);
        }
    }
    
    @RequiresApi(19)
    static class AccessibilityNodeProviderApi19 extends AccessibilityNodeProviderApi16
    {
        AccessibilityNodeProviderApi19(final AccessibilityNodeProviderCompat accessibilityNodeProviderCompat) {
            super(accessibilityNodeProviderCompat);
        }
        
        public AccessibilityNodeInfo findFocus(final int n) {
            final AccessibilityNodeInfoCompat focus = this.mCompat.findFocus(n);
            if (focus == null) {
                return null;
            }
            return focus.unwrap();
        }
    }
}
