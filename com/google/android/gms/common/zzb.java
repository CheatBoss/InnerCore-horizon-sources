package com.google.android.gms.common;

import java.util.*;

final class zzb extends CertData
{
    private final byte[] zzbd;
    
    zzb(final byte[] zzbd) {
        super(Arrays.copyOfRange(zzbd, 0, 25));
        this.zzbd = zzbd;
    }
    
    @Override
    final byte[] getBytes() {
        return this.zzbd;
    }
}
