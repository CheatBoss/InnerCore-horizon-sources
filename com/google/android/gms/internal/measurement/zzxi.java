package com.google.android.gms.internal.measurement;

import java.io.*;
import java.util.*;

interface zzxi
{
    int getTag();
    
    double readDouble() throws IOException;
    
    float readFloat() throws IOException;
    
    String readString() throws IOException;
    
    void readStringList(final List<String> p0) throws IOException;
    
     <T> T zza(final zzxj<T> p0, final zzuz p1) throws IOException;
    
     <T> void zza(final List<T> p0, final zzxj<T> p1, final zzuz p2) throws IOException;
    
     <K, V> void zza(final Map<K, V> p0, final zzwm<K, V> p1, final zzuz p2) throws IOException;
    
    @Deprecated
     <T> T zzb(final zzxj<T> p0, final zzuz p1) throws IOException;
    
    @Deprecated
     <T> void zzb(final List<T> p0, final zzxj<T> p1, final zzuz p2) throws IOException;
    
    void zzh(final List<Double> p0) throws IOException;
    
    void zzi(final List<Float> p0) throws IOException;
    
    void zzj(final List<Long> p0) throws IOException;
    
    void zzk(final List<Long> p0) throws IOException;
    
    void zzl(final List<Integer> p0) throws IOException;
    
    void zzm(final List<Long> p0) throws IOException;
    
    void zzn(final List<Integer> p0) throws IOException;
    
    void zzo(final List<Boolean> p0) throws IOException;
    
    void zzp(final List<String> p0) throws IOException;
    
    void zzq(final List<zzud> p0) throws IOException;
    
    void zzr(final List<Integer> p0) throws IOException;
    
    void zzs(final List<Integer> p0) throws IOException;
    
    void zzt(final List<Integer> p0) throws IOException;
    
    void zzu(final List<Long> p0) throws IOException;
    
    long zzuh() throws IOException;
    
    long zzui() throws IOException;
    
    int zzuj() throws IOException;
    
    long zzuk() throws IOException;
    
    int zzul() throws IOException;
    
    boolean zzum() throws IOException;
    
    String zzun() throws IOException;
    
    zzud zzuo() throws IOException;
    
    int zzup() throws IOException;
    
    int zzuq() throws IOException;
    
    int zzur() throws IOException;
    
    long zzus() throws IOException;
    
    int zzut() throws IOException;
    
    long zzuu() throws IOException;
    
    void zzv(final List<Integer> p0) throws IOException;
    
    int zzve() throws IOException;
    
    boolean zzvf() throws IOException;
    
    void zzw(final List<Long> p0) throws IOException;
}
