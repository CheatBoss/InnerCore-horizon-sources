package com.mojang.minecraftpe.packagesource;

import com.mojang.minecraftpe.*;
import com.mojang.minecraftpe.packagesource.googleplay.*;

public class PackageSourceFactory
{
    static PackageSource createGooglePlayPackageSource(final String s, final PackageSourceListener packageSourceListener) {
        return new ApkXDownloaderClient(MainActivity.mInstance, s, packageSourceListener);
    }
}
