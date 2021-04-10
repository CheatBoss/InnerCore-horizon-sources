package com.zhekasmirnov.innercore.api.runtime;

import com.zhekasmirnov.innercore.mod.executable.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.mozilla.javascript.*;

public class Callback
{
    private static HashMap<String, ArrayList<CallbackFunction>> callbacks;
    
    static {
        Callback.callbacks = new HashMap<String, ArrayList<CallbackFunction>>();
    }
    
    public static void addCallback(final String s, final Function function, final int n) {
        if (!Callback.callbacks.containsKey(s)) {
            Callback.callbacks.put(s, new ArrayList<CallbackFunction>());
        }
        final CallbackFunction callbackFunction = new CallbackFunction(function, n);
        final ArrayList<CallbackFunction> list = Callback.callbacks.get(s);
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).priority < n) {
                list.add(i, callbackFunction);
                return;
            }
        }
        list.add(callbackFunction);
    }
    
    public static List<Runnable> getCallbackAsRunnableList(final String s, final Object[] array) {
        final ArrayList<Callback$1> list = (ArrayList<Callback$1>)new ArrayList<Runnable>();
        final ArrayList<CallbackFunction> list2 = Callback.callbacks.get(s);
        if (list2 != null) {
            final Iterator<CallbackFunction> iterator = list2.iterator();
            while (iterator.hasNext()) {
                list.add(new Runnable() {
                    final /* synthetic */ CallbackFunction val$func0 = iterator.next();
                    
                    @Override
                    public void run() {
                        final Scriptable parentScope = this.val$func0.function.getParentScope();
                        this.val$func0.function.call(Compiler.assureContextForCurrentThread(), parentScope, parentScope, array);
                    }
                });
            }
        }
        return (List<Runnable>)list;
    }
    
    public static Throwable invokeAPICallback(final String s, final Object... array) {
        try {
            invokeAPICallbackUnsafe(s, array);
            return null;
        }
        catch (Throwable t) {
            final StringBuilder sb = new StringBuilder();
            sb.append("error occurred while calling callback ");
            sb.append(s);
            ICLog.e("INNERCORE-CALLBACK", sb.toString(), t);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Non-Fatal error occurred in callback ");
            sb2.append(s);
            DialogHelper.reportNonFatalError(sb2.toString(), t);
            return t;
        }
    }
    
    public static void invokeAPICallbackUnsafe(final String s, final Object[] array) {
        invokeCallbackV(s, array);
    }
    
    public static void invokeCallback(final String s, final Object... array) {
        invokeCallbackV(s, array);
    }
    
    public static void invokeCallbackV(final String s, final Object[] array) {
        final Context assureContextForCurrentThread = Compiler.assureContextForCurrentThread();
        final ArrayList<CallbackFunction> list = Callback.callbacks.get(s);
        if (list != null) {
            for (final CallbackFunction callbackFunction : list) {
                final Scriptable parentScope = callbackFunction.function.getParentScope();
                callbackFunction.function.call(assureContextForCurrentThread, parentScope, parentScope, array);
            }
        }
    }
    
    private static class CallbackFunction
    {
        public final Function function;
        public final int priority;
        
        public CallbackFunction(final Function function, final int priority) {
            this.function = function;
            this.priority = priority;
        }
    }
}
