package com.google.android.gms.internal.measurement;

import java.util.*;

final class zzxn extends zzxm<Object, Object>
{
    zzxn(final int n) {
        super(n, null);
    }
    
    @Override
    public final void zzsm() {
        if (!this.isImmutable()) {
            for (int i = 0; i < this.zzxw(); ++i) {
                final Entry<Object, Object> zzbu = (Entry<Object, Object>)((zzxm<Object, List<Object>>)this).zzbu(i);
                if (zzbu.getKey().zzvy()) {
                    zzbu.setValue(Collections.unmodifiableList((List<?>)zzbu.getValue()));
                }
            }
            for (final Map.Entry<zzvf, V> entry : this.zzxx()) {
                if (entry.getKey().zzvy()) {
                    entry.setValue((V)Collections.unmodifiableList((List<?>)entry.getValue()));
                }
            }
        }
        super.zzsm();
    }
}
