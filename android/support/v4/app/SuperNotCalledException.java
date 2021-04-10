package android.support.v4.app;

import android.util.*;

final class SuperNotCalledException extends AndroidRuntimeException
{
    public SuperNotCalledException(final String s) {
        super(s);
    }
}
