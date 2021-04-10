package com.google.android.gms.internal.measurement;

import java.io.*;

public abstract class zztw<MessageType extends zztw<MessageType, BuilderType>, BuilderType extends zztx<MessageType, BuilderType>> implements zzwt
{
    private static boolean zzbts;
    protected int zzbtr;
    
    public zztw() {
        this.zzbtr = 0;
    }
    
    void zzah(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final zzud zztt() {
        try {
            final zzuk zzam = zzud.zzam(this.zzvu());
            this.zzb(zzam.zzuf());
            return zzam.zzue();
        }
        catch (IOException ex) {
            final String name = this.getClass().getName();
            final StringBuilder sb = new StringBuilder(String.valueOf(name).length() + 62 + "ByteString".length());
            sb.append("Serializing ");
            sb.append(name);
            sb.append(" to a ");
            sb.append("ByteString");
            sb.append(" threw an IOException (should never happen).");
            throw new RuntimeException(sb.toString(), ex);
        }
    }
    
    int zztu() {
        throw new UnsupportedOperationException();
    }
}
