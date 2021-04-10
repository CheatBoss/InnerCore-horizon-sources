package org.spongycastle.math.raw;

public abstract class Nat512
{
    public static void mul(int[] ext, final int[] array, final int[] array2) {
        Nat256.mul(ext, array, array2);
        Nat256.mul(ext, 8, array, 8, array2, 16);
        final int addToEachOther = Nat256.addToEachOther(array2, 8, array2, 16);
        final int addTo = Nat256.addTo(array2, 24, array2, 16, Nat256.addTo(array2, 0, array2, 8, 0) + addToEachOther);
        final int[] create = Nat256.create();
        final int[] create2 = Nat256.create();
        final boolean b = Nat256.diff(ext, 8, ext, 0, create, 0) != Nat256.diff(array, 8, array, 0, create2, 0);
        ext = Nat256.createExt();
        Nat256.mul(create, create2, ext);
        int n;
        if (b) {
            n = Nat.addTo(16, ext, 0, array2, 8);
        }
        else {
            n = Nat.subFrom(16, ext, 0, array2, 8);
        }
        Nat.addWordAt(32, addToEachOther + addTo + n, array2, 24);
    }
    
    public static void square(int[] ext, final int[] array) {
        Nat256.square(ext, array);
        Nat256.square(ext, 8, array, 16);
        final int addToEachOther = Nat256.addToEachOther(array, 8, array, 16);
        final int addTo = Nat256.addTo(array, 24, array, 16, Nat256.addTo(array, 0, array, 8, 0) + addToEachOther);
        final int[] create = Nat256.create();
        Nat256.diff(ext, 8, ext, 0, create, 0);
        ext = Nat256.createExt();
        Nat256.square(create, ext);
        Nat.addWordAt(32, addToEachOther + addTo + Nat.subFrom(16, ext, 0, array, 8), array, 24);
    }
}
