package org.spongycastle.math.raw;

public abstract class Nat384
{
    public static void mul(int[] ext, final int[] array, final int[] array2) {
        Nat192.mul(ext, array, array2);
        Nat192.mul(ext, 6, array, 6, array2, 12);
        final int addToEachOther = Nat192.addToEachOther(array2, 6, array2, 12);
        final int addTo = Nat192.addTo(array2, 18, array2, 12, Nat192.addTo(array2, 0, array2, 6, 0) + addToEachOther);
        final int[] create = Nat192.create();
        final int[] create2 = Nat192.create();
        final boolean b = Nat192.diff(ext, 6, ext, 0, create, 0) != Nat192.diff(array, 6, array, 0, create2, 0);
        ext = Nat192.createExt();
        Nat192.mul(create, create2, ext);
        int n;
        if (b) {
            n = Nat.addTo(12, ext, 0, array2, 6);
        }
        else {
            n = Nat.subFrom(12, ext, 0, array2, 6);
        }
        Nat.addWordAt(24, addToEachOther + addTo + n, array2, 18);
    }
    
    public static void square(int[] ext, final int[] array) {
        Nat192.square(ext, array);
        Nat192.square(ext, 6, array, 12);
        final int addToEachOther = Nat192.addToEachOther(array, 6, array, 12);
        final int addTo = Nat192.addTo(array, 18, array, 12, Nat192.addTo(array, 0, array, 6, 0) + addToEachOther);
        final int[] create = Nat192.create();
        Nat192.diff(ext, 6, ext, 0, create, 0);
        ext = Nat192.createExt();
        Nat192.square(create, ext);
        Nat.addWordAt(24, addToEachOther + addTo + Nat.subFrom(12, ext, 0, array, 6), array, 18);
    }
}
