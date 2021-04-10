package com.zhekasmirnov.innercore.api;

import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;

public class NativeExceptionHandler
{
    public static ICNativeException buildException(final String s, String s2) {
        final ArrayList<StackTraceElement> list = new ArrayList<StackTraceElement>();
        if (s != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("[signal info: ");
            sb.append(s);
            sb.append("]");
            list.add(new StackTraceElement(sb.toString(), "", "", -2));
        }
        if (s2 != null) {
            final String[] split = s2.split("\n");
            for (int length = split.length, i = 0; i < length; ++i) {
                s2 = split[i];
                list.add(new StackTraceElement("", s2.substring(s2.indexOf(95)), "", -2));
            }
        }
        final ICNativeException ex = new ICNativeException("exception was caught in native code");
        ex.setStackTrace((StackTraceElement[])list.toArray());
        return ex;
    }
    
    public static void handle(final String s, final String s2) {
        new Thread(new Runnable() {
            final /* synthetic */ ICNativeException val$exception = buildException(s, s2);
            
            @Override
            public void run() {
                ICLog.e("ERROR", "native crash handled: ", this.val$exception);
                throw this.val$exception;
            }
        }).start();
    }
    
    public static class ICNativeException extends RuntimeException
    {
        public ICNativeException(final String s) {
            super(s);
        }
    }
}
