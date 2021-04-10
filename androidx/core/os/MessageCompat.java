package androidx.core.os;

import androidx.annotation.*;
import android.os.*;
import android.annotation.*;

public final class MessageCompat
{
    private static boolean sTryIsAsynchronous;
    private static boolean sTrySetAsynchronous;
    
    static {
        MessageCompat.sTrySetAsynchronous = true;
        MessageCompat.sTryIsAsynchronous = true;
    }
    
    private MessageCompat() {
    }
    
    @SuppressLint({ "NewApi" })
    public static boolean isAsynchronous(@NonNull final Message message) {
        if (Build$VERSION.SDK_INT >= 22) {
            return message.isAsynchronous();
        }
        if (MessageCompat.sTryIsAsynchronous && Build$VERSION.SDK_INT >= 16) {
            try {
                return message.isAsynchronous();
            }
            catch (NoSuchMethodError noSuchMethodError) {
                MessageCompat.sTryIsAsynchronous = false;
            }
        }
        return false;
    }
    
    @SuppressLint({ "NewApi" })
    public static void setAsynchronous(@NonNull final Message message, final boolean b) {
        if (Build$VERSION.SDK_INT >= 22) {
            message.setAsynchronous(b);
            return;
        }
        if (MessageCompat.sTrySetAsynchronous && Build$VERSION.SDK_INT >= 16) {
            try {
                message.setAsynchronous(b);
            }
            catch (NoSuchMethodError noSuchMethodError) {
                MessageCompat.sTrySetAsynchronous = false;
            }
        }
    }
}
