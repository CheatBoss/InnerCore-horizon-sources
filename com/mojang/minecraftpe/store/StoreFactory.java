package com.mojang.minecraftpe.store;

import com.mojang.minecraftpe.*;
import com.mojang.minecraftpe.store.amazonappstore.*;
import android.content.*;
import com.mojang.minecraftpe.store.googleplay.*;

public class StoreFactory
{
    static Store createAmazonAppStore(final StoreListener storeListener, final boolean b) {
        return new AmazonAppStore((Context)MainActivity.mInstance, storeListener, b);
    }
    
    static Store createGooglePlayStore(final String s, final StoreListener storeListener) {
        return new GooglePlayStore(MainActivity.mInstance, s, storeListener);
    }
}
