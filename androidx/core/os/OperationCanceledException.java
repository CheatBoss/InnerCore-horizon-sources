package androidx.core.os;

import androidx.annotation.*;

public class OperationCanceledException extends RuntimeException
{
    public OperationCanceledException() {
        this((String)null);
    }
    
    public OperationCanceledException(@Nullable String s) {
        if (s == null) {
            s = "The operation has been canceled.";
        }
        super(s);
    }
}
