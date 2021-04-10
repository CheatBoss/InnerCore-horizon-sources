package com.mojang.minecraftpe.packagesource;

public class NativePackageSourceListener implements PackageSourceListener
{
    long mPackageSourceListener;
    
    public NativePackageSourceListener() {
        this.mPackageSourceListener = 0L;
    }
    
    public native void nativeOnDownloadProgress(final long p0, final long p1, final long p2, final float p3, final long p4);
    
    public native void nativeOnDownloadStarted(final long p0);
    
    public native void nativeOnDownloadStateChanged(final long p0, final boolean p1, final boolean p2, final boolean p3, final boolean p4, final boolean p5, final int p6, final int p7);
    
    public native void nativeOnMountStateChanged(final long p0, final String p1, final int p2);
    
    @Override
    public void onDownloadProgress(final long n, final long n2, final float n3, final long n4) {
        this.nativeOnDownloadProgress(this.mPackageSourceListener, n, n2, n3, n4);
    }
    
    @Override
    public void onDownloadStarted() {
        this.nativeOnDownloadStarted(this.mPackageSourceListener);
    }
    
    @Override
    public void onDownloadStateChanged(final boolean b, final boolean b2, final boolean b3, final boolean b4, final boolean b5, final int n, final int n2) {
        this.nativeOnDownloadStateChanged(this.mPackageSourceListener, b, b2, b3, b4, b5, n, n2);
    }
    
    @Override
    public void onMountStateChanged(final String s, final int n) {
        this.nativeOnMountStateChanged(this.mPackageSourceListener, s, n);
    }
    
    public void setListener(final long mPackageSourceListener) {
        this.mPackageSourceListener = mPackageSourceListener;
    }
}
