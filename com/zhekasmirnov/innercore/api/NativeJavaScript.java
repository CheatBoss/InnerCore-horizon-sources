package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.horizon.runtime.logger.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import org.mozilla.javascript.*;

public class NativeJavaScript
{
    private static final int CALL_TYPE_BASIC = 1;
    private static final int CALL_TYPE_COMPLEX = 2;
    private static final int TYPE_BYTE = 0;
    private static final int TYPE_DOUBLE = 3;
    private static final int TYPE_INT = 1;
    private static final int TYPE_LONG = 2;
    private static final int TYPE_STRING = 4;
    
    public static Callable getFunction(final String s, final String s2) {
        final long functionHandle = getFunctionHandle(s, s2);
        if (functionHandle != 0L) {
            final String functionSignature = getFunctionSignature(functionHandle);
            switch (getFunctionCallType(functionHandle)) {
                case 2: {
                    return (Callable)new ComplexCaller(functionHandle, s, s2, functionSignature);
                }
                case 1: {
                    return (Callable)new BasicCaller(functionHandle, s, s2, functionSignature);
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to wrap native function ");
        sb.append(s);
        sb.append("::");
        sb.append(s2);
        sb.append(" for some reason");
        Logger.error("NativeJavaScript", sb.toString());
        return null;
    }
    
    private static native int getFunctionCallType(final long p0);
    
    private static native long getFunctionHandle(final String p0, final String p1);
    
    private static native String[] getFunctionListForModule(final String p0);
    
    private static native String getFunctionSignature(final long p0);
    
    public static boolean injectNativeModule(final String s, final ScriptableObject scriptableObject) {
        final String[] functionListForModule = getFunctionListForModule(s);
        int i = 0;
        if (functionListForModule != null && functionListForModule.length > 0) {
            while (i < functionListForModule.length) {
                final String s2 = functionListForModule[i];
                final Callable function = getFunction(s, s2);
                if (function != null) {
                    scriptableObject.put(s2, (Scriptable)scriptableObject, (Object)function);
                }
                ++i;
            }
            return true;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("failed to import native module: ");
        sb.append(s);
        Logger.error("NativeJavaScript", sb.toString());
        return false;
    }
    
    private static native long invokeBasicParameterFunction(final long p0, final byte[] p1);
    
    private static native long invokeComplexParameterFunction(final long p0, final byte[] p1);
    
    private static int parseComplexParameters(final DataOutputStream dataOutputStream, final ScriptableObject scriptableObject, final String s) throws IOException {
        int n = 0;
        final Object[] allIds = scriptableObject.getAllIds();
        for (int length = allIds.length, i = 0; i < length; ++i) {
            final Object o = allIds[i];
            final Object value = scriptableObject.get(o);
            if (value instanceof ScriptableObject) {
                final ScriptableObject scriptableObject2 = (ScriptableObject)value;
                final StringBuilder sb = new StringBuilder();
                sb.append(s);
                sb.append(o.toString());
                sb.append(".");
                n += parseComplexParameters(dataOutputStream, scriptableObject2, sb.toString());
            }
            else {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append(o.toString());
                writeValue(dataOutputStream, sb2.toString(), value);
                ++n;
            }
        }
        return n;
    }
    
    public static byte[] parseComplexParameters(final ScriptableObject scriptableObject) {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeInt(-1);
            final int complexParameters = parseComplexParameters(dataOutputStream, scriptableObject, "");
            final byte[] byteArray = byteArrayOutputStream.toByteArray();
            byteArray[0] = (byte)(complexParameters >>> 24 & 0xFF);
            byteArray[1] = (byte)(complexParameters >>> 16 & 0xFF);
            byteArray[2] = (byte)(complexParameters >>> 8 & 0xFF);
            byteArray[3] = (byte)(complexParameters >>> 0 & 0xFF);
            return byteArray;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static native double unwrapDoubleResult(final long p0);
    
    private static native long unwrapLongResult(final long p0);
    
    private static native Object unwrapObjectResult(final long p0);
    
    public static Object unwrapResult(final long n, final String s) {
        if (s == null || s.length() == 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid native caller signature: ");
            sb.append(s);
            throw new RuntimeException(sb.toString());
        }
        final char char1 = s.charAt(0);
        if (char1 == 'F') {
            return unwrapDoubleResult(n);
        }
        if (char1 == 'I') {
            return unwrapLongResult(n);
        }
        if (char1 == 'O') {
            return unwrapObjectResult(n);
        }
        if (char1 == 'S') {
            return unwrapStringResult(n);
        }
        if (char1 != 'V') {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid native function signature: ");
            sb2.append(s);
            throw new RuntimeException(sb2.toString());
        }
        return null;
    }
    
    private static native String unwrapStringResult(final long p0);
    
    public static ScriptableObject wrapNativeModule(final String s) {
        final ScriptableObject scriptableObject = new ScriptableObject() {
            public String getClassName() {
                final StringBuilder sb = new StringBuilder();
                sb.append("NativeJSModule_");
                sb.append(s);
                return sb.toString();
            }
        };
        if (injectNativeModule(s, scriptableObject)) {
            return scriptableObject;
        }
        return null;
    }
    
    private static void writeValue(final DataOutputStream dataOutputStream, final String s, final Object o) throws IOException {
        dataOutputStream.writeUTF(s);
        if (o instanceof Number) {
            final Number n = (Number)o;
            if (n instanceof Long) {
                dataOutputStream.writeByte(2);
                dataOutputStream.writeLong(n.longValue());
            }
            else if (!(n instanceof Double) && !(n instanceof Float)) {
                if (n instanceof Byte) {
                    dataOutputStream.writeByte(0);
                    dataOutputStream.writeByte(n.byteValue());
                }
                else {
                    dataOutputStream.writeByte(1);
                    dataOutputStream.writeInt(n.intValue());
                }
            }
            else {
                dataOutputStream.writeByte(3);
                dataOutputStream.writeDouble(n.doubleValue());
            }
            return;
        }
        if (o instanceof CharSequence) {
            dataOutputStream.writeByte(4);
            dataOutputStream.writeUTF(o.toString());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid parameter passed for key ");
        sb.append(s);
        sb.append(": ");
        sb.append(o);
        sb.append(", it will be replaced with zero byte");
        Logger.error("NativeJavaScript", sb.toString());
        dataOutputStream.writeByte(0);
        dataOutputStream.writeByte(0);
    }
    
    public static class BasicCaller extends ScriptableFunctionImpl
    {
        public final int argsSize;
        public final long handle;
        public final String module;
        public final String name;
        public final String params;
        public final String signature;
        
        public BasicCaller(final long handle, final String module, final String name, final String signature) {
            this.handle = handle;
            this.module = module;
            this.name = name;
            this.signature = signature;
            final int n = signature.indexOf(40) + 1;
            final int index = signature.indexOf(41);
            if (n != 0 && index != -1) {
                this.params = signature.substring(n, index);
                int argsSize = 0;
                for (int i = 0; i < this.params.length(); ++i) {
                    final char char1 = this.params.charAt(i);
                    int n2 = argsSize;
                    if (char1 != 'F') {
                        n2 = argsSize;
                        if (char1 != 'I') {
                            int n3 = argsSize;
                            if (char1 != 'L') {
                                int n4 = argsSize;
                                n3 = argsSize;
                                switch (char1) {
                                    default: {
                                        continue;
                                    }
                                    case 66:
                                    case 67: {
                                        while ((n4 + 4) % 4 != 0) {
                                            ++n4;
                                        }
                                        argsSize = n4 + 1;
                                        continue;
                                    }
                                    case 68: {
                                        break;
                                    }
                                }
                            }
                            while ((n3 + 4) % 8 != 0) {
                                ++n3;
                            }
                            argsSize = n3 + 8;
                            continue;
                        }
                    }
                    while ((n2 + 4) % 4 != 0) {
                        ++n2;
                    }
                    argsSize = n2 + 4;
                }
                this.argsSize = argsSize;
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("invalid native function signature: ");
            sb.append(signature);
            throw new RuntimeException(sb.toString());
        }
        
        public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
            throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.copyTypes(TypeTransformer.java:311)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.fixTypes(TypeTransformer.java:226)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:207)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
        }
    }
    
    public static class ComplexCaller extends ScriptableFunctionImpl
    {
        public final long handle;
        public final String module;
        public final String name;
        public final String signature;
        
        public ComplexCaller(final long handle, final String module, final String name, final String signature) {
            this.handle = handle;
            this.module = module;
            this.name = name;
            this.signature = signature;
        }
        
        public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
            if (array.length == 1 && array[0] instanceof ScriptableObject) {
                return NativeJavaScript.unwrapResult(invokeComplexParameterFunction(this.handle, NativeJavaScript.parseComplexParameters((ScriptableObject)array[0])), this.signature);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("complex native function ");
            sb.append(this.module);
            sb.append("::");
            sb.append(this.name);
            sb.append(" => ");
            sb.append(this.signature);
            sb.append(" must receive exactly one javascript object as parameter");
            throw new IllegalArgumentException(sb.toString());
        }
    }
}
