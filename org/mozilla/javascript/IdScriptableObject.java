package org.mozilla.javascript;

import java.io.*;

public abstract class IdScriptableObject extends ScriptableObject implements IdFunctionCall
{
    private transient PrototypeValues prototypeValues;
    
    public IdScriptableObject() {
    }
    
    public IdScriptableObject(final Scriptable scriptable, final Scriptable scriptable2) {
        super(scriptable, scriptable2);
    }
    
    private ScriptableObject getBuiltInDescriptor(final String s) {
        Scriptable parentScope;
        if ((parentScope = this.getParentScope()) == null) {
            parentScope = this;
        }
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0) {
            return ScriptableObject.buildDataDescriptor(parentScope, this.getInstanceIdValue(0xFFFF & instanceIdInfo), instanceIdInfo >>> 16);
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                return ScriptableObject.buildDataDescriptor(parentScope, this.prototypeValues.get(id), this.prototypeValues.getAttributes(id));
            }
        }
        return null;
    }
    
    protected static EcmaError incompatibleCallError(final IdFunctionObject idFunctionObject) {
        throw ScriptRuntime.typeError1("msg.incompat.call", idFunctionObject.getFunctionName());
    }
    
    protected static int instanceIdInfo(final int n, final int n2) {
        return n << 16 | n2;
    }
    
    private IdFunctionObject newIdFunction(final Object o, final int n, final String s, final int n2, final Scriptable scriptable) {
        final IdFunctionObject idFunctionObject = new IdFunctionObject(this, o, n, s, n2, scriptable);
        if (this.isSealed()) {
            idFunctionObject.sealObject();
        }
        return idFunctionObject;
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        final int int1 = objectInputStream.readInt();
        if (int1 != 0) {
            this.activatePrototypeMap(int1);
        }
    }
    
    private void writeObject(final ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        int maxId = 0;
        if (this.prototypeValues != null) {
            maxId = this.prototypeValues.getMaxId();
        }
        objectOutputStream.writeInt(maxId);
    }
    
    public final void activatePrototypeMap(final int n) {
        final PrototypeValues prototypeValues = new PrototypeValues(this, n);
        synchronized (this) {
            if (this.prototypeValues != null) {
                throw new IllegalStateException();
            }
            this.prototypeValues = prototypeValues;
        }
    }
    
    protected void addIdFunctionProperty(final Scriptable scriptable, final Object o, final int n, final String s, final int n2) {
        this.newIdFunction(o, n, s, n2, ScriptableObject.getTopLevelScope(scriptable)).addAsProperty(scriptable);
    }
    
    protected final Object defaultGet(final String s) {
        return super.get(s, this);
    }
    
    protected final boolean defaultHas(final String s) {
        return super.has(s, this);
    }
    
    protected final void defaultPut(final String s, final Object o) {
        super.put(s, this, o);
    }
    
    @Override
    public void defineOwnProperty(final Context context, final Object o, final ScriptableObject scriptableObject) {
        if (o instanceof String) {
            final String s = (String)o;
            final int instanceIdInfo = this.findInstanceIdInfo(s);
            if (instanceIdInfo != 0) {
                final int n = 0xFFFF & instanceIdInfo;
                if (!this.isAccessorDescriptor(scriptableObject)) {
                    this.checkPropertyDefinition(scriptableObject);
                    this.checkPropertyChange(s, this.getOwnPropertyDescriptor(context, o), scriptableObject);
                    final int n2 = instanceIdInfo >>> 16;
                    final Object property = ScriptableObject.getProperty(scriptableObject, "value");
                    if (property != IdScriptableObject.NOT_FOUND && (n2 & 0x1) == 0x0 && !this.sameValue(property, this.getInstanceIdValue(n))) {
                        this.setInstanceIdValue(n, property);
                    }
                    this.setAttributes(s, this.applyDescriptorToAttributeBitset(n2, scriptableObject));
                    return;
                }
                this.delete(n);
            }
            if (this.prototypeValues != null) {
                final int id = this.prototypeValues.findId(s);
                if (id != 0) {
                    if (!this.isAccessorDescriptor(scriptableObject)) {
                        this.checkPropertyDefinition(scriptableObject);
                        this.checkPropertyChange(s, this.getOwnPropertyDescriptor(context, o), scriptableObject);
                        final int attributes = this.prototypeValues.getAttributes(id);
                        final Object property2 = ScriptableObject.getProperty(scriptableObject, "value");
                        if (property2 != IdScriptableObject.NOT_FOUND && (attributes & 0x1) == 0x0 && !this.sameValue(property2, this.prototypeValues.get(id))) {
                            this.prototypeValues.set(id, this, property2);
                        }
                        this.prototypeValues.setAttributes(id, this.applyDescriptorToAttributeBitset(attributes, scriptableObject));
                        return;
                    }
                    this.prototypeValues.delete(id);
                }
            }
        }
        super.defineOwnProperty(context, o, scriptableObject);
    }
    
    @Override
    public void delete(final String s) {
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0 && !this.isSealed()) {
            if ((instanceIdInfo >>> 16 & 0x4) == 0x0) {
                this.setInstanceIdValue(0xFFFF & instanceIdInfo, IdScriptableObject.NOT_FOUND);
            }
            return;
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                if (!this.isSealed()) {
                    this.prototypeValues.delete(id);
                }
                return;
            }
        }
        super.delete(s);
    }
    
    @Override
    public Object execIdCall(final IdFunctionObject idFunctionObject, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        throw idFunctionObject.unknown();
    }
    
    public final IdFunctionObject exportAsJSClass(final int n, final Scriptable parentScope, final boolean b) {
        if (parentScope != this && parentScope != null) {
            this.setParentScope(parentScope);
            this.setPrototype(ScriptableObject.getObjectPrototype(parentScope));
        }
        this.activatePrototypeMap(n);
        final IdFunctionObject precachedConstructor = this.prototypeValues.createPrecachedConstructor();
        if (b) {
            this.sealObject();
        }
        this.fillConstructorProperties(precachedConstructor);
        if (b) {
            precachedConstructor.sealObject();
        }
        precachedConstructor.exportAsScopeProperty();
        return precachedConstructor;
    }
    
    protected void fillConstructorProperties(final IdFunctionObject idFunctionObject) {
    }
    
    protected int findInstanceIdInfo(final String s) {
        return 0;
    }
    
    protected int findPrototypeId(final String s) {
        throw new IllegalStateException(s);
    }
    
    @Override
    public Object get(final String s, final Scriptable scriptable) {
        final Object value = super.get(s, scriptable);
        if (value != IdScriptableObject.NOT_FOUND) {
            return value;
        }
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0) {
            final Object instanceIdValue = this.getInstanceIdValue(0xFFFF & instanceIdInfo);
            if (instanceIdValue != IdScriptableObject.NOT_FOUND) {
                return instanceIdValue;
            }
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                final Object value2 = this.prototypeValues.get(id);
                if (value2 != IdScriptableObject.NOT_FOUND) {
                    return value2;
                }
            }
        }
        return IdScriptableObject.NOT_FOUND;
    }
    
    @Override
    public int getAttributes(final String s) {
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0) {
            return instanceIdInfo >>> 16;
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                return this.prototypeValues.getAttributes(id);
            }
        }
        return super.getAttributes(s);
    }
    
    @Override
    Object[] getIds(final boolean b) {
        Object[] array2;
        final Object[] array = array2 = super.getIds(b);
        if (this.prototypeValues != null) {
            array2 = this.prototypeValues.getNames(b, array);
        }
        int i = this.getMaxInstanceId();
        Object[] array3 = array2;
        if (i != 0) {
            int n = 0;
            Object[] array4 = null;
            while (i != 0) {
                final String instanceIdName = this.getInstanceIdName(i);
                final int instanceIdInfo = this.findInstanceIdInfo(instanceIdName);
                int n2 = n;
                Object[] array5 = array4;
                Label_0157: {
                    if (instanceIdInfo != 0) {
                        final int n3 = instanceIdInfo >>> 16;
                        if ((n3 & 0x4) == 0x0 && IdScriptableObject.NOT_FOUND == this.getInstanceIdValue(i)) {
                            n2 = n;
                            array5 = array4;
                        }
                        else {
                            if (!b) {
                                n2 = n;
                                array5 = array4;
                                if ((n3 & 0x2) != 0x0) {
                                    break Label_0157;
                                }
                            }
                            if (n == 0) {
                                array4 = new Object[i];
                            }
                            array4[n] = instanceIdName;
                            n2 = n + 1;
                            array5 = array4;
                        }
                    }
                }
                --i;
                n = n2;
                array4 = array5;
            }
            array3 = array2;
            if (n != 0) {
                if (array2.length == 0 && array4.length == n) {
                    return array4;
                }
                array3 = new Object[array2.length + n];
                System.arraycopy(array2, 0, array3, 0, array2.length);
                System.arraycopy(array4, 0, array3, array2.length, n);
            }
        }
        return array3;
    }
    
    protected String getInstanceIdName(final int n) {
        throw new IllegalArgumentException(String.valueOf(n));
    }
    
    protected Object getInstanceIdValue(final int n) {
        throw new IllegalStateException(String.valueOf(n));
    }
    
    protected int getMaxInstanceId() {
        return 0;
    }
    
    @Override
    protected ScriptableObject getOwnPropertyDescriptor(final Context context, final Object o) {
        ScriptableObject scriptableObject2;
        final ScriptableObject scriptableObject = scriptableObject2 = super.getOwnPropertyDescriptor(context, o);
        if (scriptableObject == null) {
            scriptableObject2 = scriptableObject;
            if (o instanceof String) {
                scriptableObject2 = this.getBuiltInDescriptor((String)o);
            }
        }
        return scriptableObject2;
    }
    
    @Override
    public boolean has(final String s, final Scriptable scriptable) {
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0) {
            return (instanceIdInfo >>> 16 & 0x4) != 0x0 || IdScriptableObject.NOT_FOUND != this.getInstanceIdValue(0xFFFF & instanceIdInfo);
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                return this.prototypeValues.has(id);
            }
        }
        return super.has(s, scriptable);
    }
    
    public final boolean hasPrototypeMap() {
        return this.prototypeValues != null;
    }
    
    public final void initPrototypeConstructor(final IdFunctionObject idFunctionObject) {
        final int constructorId = this.prototypeValues.constructorId;
        if (constructorId == 0) {
            throw new IllegalStateException();
        }
        if (idFunctionObject.methodId() != constructorId) {
            throw new IllegalArgumentException();
        }
        if (this.isSealed()) {
            idFunctionObject.sealObject();
        }
        this.prototypeValues.initValue(constructorId, "constructor", idFunctionObject, 2);
    }
    
    protected void initPrototypeId(final int n) {
        throw new IllegalStateException(String.valueOf(n));
    }
    
    public final void initPrototypeMethod(final Object o, final int n, final String s, final int n2) {
        this.prototypeValues.initValue(n, s, this.newIdFunction(o, n, s, n2, ScriptableObject.getTopLevelScope(this)), 2);
    }
    
    public final void initPrototypeValue(final int n, final String s, final Object o, final int n2) {
        this.prototypeValues.initValue(n, s, o, n2);
    }
    
    @Override
    public void put(final String s, final Scriptable scriptable, final Object o) {
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo == 0) {
            if (this.prototypeValues != null) {
                final int id = this.prototypeValues.findId(s);
                if (id != 0) {
                    if (scriptable == this && this.isSealed()) {
                        throw Context.reportRuntimeError1("msg.modify.sealed", s);
                    }
                    this.prototypeValues.set(id, scriptable, o);
                    return;
                }
            }
            super.put(s, scriptable, o);
            return;
        }
        if (scriptable == this && this.isSealed()) {
            throw Context.reportRuntimeError1("msg.modify.sealed", s);
        }
        if ((instanceIdInfo >>> 16 & 0x1) == 0x0) {
            if (scriptable == this) {
                this.setInstanceIdValue(0xFFFF & instanceIdInfo, o);
                return;
            }
            scriptable.put(s, scriptable, o);
        }
    }
    
    @Override
    public void setAttributes(final String s, final int n) {
        ScriptableObject.checkValidAttributes(n);
        final int instanceIdInfo = this.findInstanceIdInfo(s);
        if (instanceIdInfo != 0) {
            if (n != instanceIdInfo >>> 16) {
                this.setInstanceIdAttributes(0xFFFF & instanceIdInfo, n);
            }
            return;
        }
        if (this.prototypeValues != null) {
            final int id = this.prototypeValues.findId(s);
            if (id != 0) {
                this.prototypeValues.setAttributes(id, n);
                return;
            }
        }
        super.setAttributes(s, n);
    }
    
    protected void setInstanceIdAttributes(final int n, final int n2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Changing attributes not supported for ");
        sb.append(this.getClassName());
        sb.append(" ");
        sb.append(this.getInstanceIdName(n));
        sb.append(" property");
        throw ScriptRuntime.constructError("InternalError", sb.toString());
    }
    
    protected void setInstanceIdValue(final int n, final Object o) {
        throw new IllegalStateException(String.valueOf(n));
    }
    
    private static final class PrototypeValues implements Serializable
    {
        private static final int NAME_SLOT = 1;
        private static final int SLOT_SPAN = 2;
        static final long serialVersionUID = 3038645279153854371L;
        private short[] attributeArray;
        private IdFunctionObject constructor;
        private short constructorAttrs;
        int constructorId;
        private int maxId;
        private IdScriptableObject obj;
        private Object[] valueArray;
        
        PrototypeValues(final IdScriptableObject obj, final int maxId) {
            if (obj == null) {
                throw new IllegalArgumentException();
            }
            if (maxId < 1) {
                throw new IllegalArgumentException();
            }
            this.obj = obj;
            this.maxId = maxId;
        }
        
        private Object ensureId(final int p0) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     1: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.valueArray:[Ljava/lang/Object;
            //     4: astore          4
            //     6: aload           4
            //     8: astore_3       
            //     9: aload           4
            //    11: ifnonnull       65
            //    14: aload_0        
            //    15: monitorenter   
            //    16: aload_0        
            //    17: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.valueArray:[Ljava/lang/Object;
            //    20: astore          4
            //    22: aload           4
            //    24: astore_3       
            //    25: aload           4
            //    27: ifnonnull       55
            //    30: aload_0        
            //    31: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.maxId:I
            //    34: iconst_2       
            //    35: imul           
            //    36: anewarray       Ljava/lang/Object;
            //    39: astore_3       
            //    40: aload_0        
            //    41: aload_3        
            //    42: putfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.valueArray:[Ljava/lang/Object;
            //    45: aload_0        
            //    46: aload_0        
            //    47: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.maxId:I
            //    50: newarray        S
            //    52: putfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.attributeArray:[S
            //    55: aload_0        
            //    56: monitorexit    
            //    57: goto            65
            //    60: astore_3       
            //    61: aload_0        
            //    62: monitorexit    
            //    63: aload_3        
            //    64: athrow         
            //    65: iload_1        
            //    66: iconst_1       
            //    67: isub           
            //    68: iconst_2       
            //    69: imul           
            //    70: istore_2       
            //    71: aload_3        
            //    72: iload_2        
            //    73: aaload         
            //    74: astore          5
            //    76: aload           5
            //    78: astore          4
            //    80: aload           5
            //    82: ifnonnull       193
            //    85: iload_1        
            //    86: aload_0        
            //    87: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.constructorId:I
            //    90: if_icmpne       119
            //    93: aload_0        
            //    94: aload_0        
            //    95: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.constructorId:I
            //    98: ldc             "constructor"
            //   100: aload_0        
            //   101: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.constructor:Lorg/mozilla/javascript/IdFunctionObject;
            //   104: aload_0        
            //   105: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.constructorAttrs:S
            //   108: invokespecial   org/mozilla/javascript/IdScriptableObject$PrototypeValues.initSlot:(ILjava/lang/String;Ljava/lang/Object;I)V
            //   111: aload_0        
            //   112: aconst_null    
            //   113: putfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.constructor:Lorg/mozilla/javascript/IdFunctionObject;
            //   116: goto            127
            //   119: aload_0        
            //   120: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.obj:Lorg/mozilla/javascript/IdScriptableObject;
            //   123: iload_1        
            //   124: invokevirtual   org/mozilla/javascript/IdScriptableObject.initPrototypeId:(I)V
            //   127: aload_3        
            //   128: iload_2        
            //   129: aaload         
            //   130: astore_3       
            //   131: aload_3        
            //   132: astore          4
            //   134: aload_3        
            //   135: ifnonnull       193
            //   138: new             Ljava/lang/StringBuilder;
            //   141: dup            
            //   142: invokespecial   java/lang/StringBuilder.<init>:()V
            //   145: astore_3       
            //   146: aload_3        
            //   147: aload_0        
            //   148: getfield        org/mozilla/javascript/IdScriptableObject$PrototypeValues.obj:Lorg/mozilla/javascript/IdScriptableObject;
            //   151: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //   154: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   157: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   160: pop            
            //   161: aload_3        
            //   162: ldc             ".initPrototypeId(int id) "
            //   164: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   167: pop            
            //   168: aload_3        
            //   169: ldc             "did not initialize id="
            //   171: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   174: pop            
            //   175: aload_3        
            //   176: iload_1        
            //   177: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
            //   180: pop            
            //   181: new             Ljava/lang/IllegalStateException;
            //   184: dup            
            //   185: aload_3        
            //   186: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   189: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
            //   192: athrow         
            //   193: aload           4
            //   195: areturn        
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ----
            //  16     22     60     65     Any
            //  30     55     60     65     Any
            //  55     57     60     65     Any
            //  61     63     60     65     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        private void initSlot(final int n, final String s, final Object o, final int n2) {
            final Object[] valueArray = this.valueArray;
            if (valueArray == null) {
                throw new IllegalStateException();
            }
            Object null_VALUE;
            if ((null_VALUE = o) == null) {
                null_VALUE = UniqueTag.NULL_VALUE;
            }
            final int n3 = (n - 1) * 2;
            // monitorenter(this)
            Label_0077: {
                if (valueArray[n3] != null) {
                    break Label_0077;
                }
                valueArray[n3] = null_VALUE;
                valueArray[n3 + 1] = s;
                try {
                    this.attributeArray[n - 1] = (short)n2;
                    Label_0099: {
                        return;
                    }
                    // iftrue(Label_0099:, s.equals(valueArray[n3 + 1]))
                    throw new IllegalStateException();
                }
                finally {
                }
                // monitorexit(this)
            }
        }
        
        final IdFunctionObject createPrecachedConstructor() {
            if (this.constructorId != 0) {
                throw new IllegalStateException();
            }
            this.constructorId = this.obj.findPrototypeId("constructor");
            if (this.constructorId == 0) {
                throw new IllegalStateException("No id for constructor property");
            }
            this.obj.initPrototypeId(this.constructorId);
            if (this.constructor == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append(this.obj.getClass().getName());
                sb.append(".initPrototypeId() did not ");
                sb.append("initialize id=");
                sb.append(this.constructorId);
                throw new IllegalStateException(sb.toString());
            }
            this.constructor.initFunction(this.obj.getClassName(), ScriptableObject.getTopLevelScope(this.obj));
            this.constructor.markAsConstructor(this.obj);
            return this.constructor;
        }
        
        final void delete(final int n) {
            this.ensureId(n);
            if ((this.attributeArray[n - 1] & 0x4) == 0x0) {
                synchronized (this) {
                    this.valueArray[(n - 1) * 2] = Scriptable.NOT_FOUND;
                    this.attributeArray[n - 1] = 0;
                }
            }
        }
        
        final int findId(final String s) {
            return this.obj.findPrototypeId(s);
        }
        
        final Object get(final int n) {
            Object ensureId;
            if ((ensureId = this.ensureId(n)) == UniqueTag.NULL_VALUE) {
                ensureId = null;
            }
            return ensureId;
        }
        
        final int getAttributes(final int n) {
            this.ensureId(n);
            return this.attributeArray[n - 1];
        }
        
        final int getMaxId() {
            return this.maxId;
        }
        
        final Object[] getNames(final boolean b, Object[] array) {
            int n = 0;
            Object[] array2 = null;
            int n2;
            Object[] array3;
            for (int i = 1; i <= this.maxId; ++i, n = n2, array2 = array3) {
                final Object ensureId = this.ensureId(i);
                if (!b) {
                    n2 = n;
                    array3 = array2;
                    if ((this.attributeArray[i - 1] & 0x2) != 0x0) {
                        continue;
                    }
                }
                n2 = n;
                array3 = array2;
                if (ensureId != Scriptable.NOT_FOUND) {
                    final String s = (String)this.valueArray[(i - 1) * 2 + 1];
                    if ((array3 = array2) == null) {
                        array3 = new Object[this.maxId];
                    }
                    array3[n] = s;
                    n2 = n + 1;
                }
            }
            if (n == 0) {
                return array;
            }
            if (array != null && array.length != 0) {
                final int length = array.length;
                final Object[] array4 = new Object[length + n];
                System.arraycopy(array, 0, array4, 0, length);
                System.arraycopy(array2, 0, array4, length, n);
                return array4;
            }
            array = array2;
            if (n != array2.length) {
                array = new Object[n];
                System.arraycopy(array2, 0, array, 0, n);
            }
            return array;
        }
        
        final boolean has(final int n) {
            final Object[] valueArray = this.valueArray;
            if (valueArray == null) {
                return true;
            }
            final Object o = valueArray[(n - 1) * 2];
            return o == null || o != Scriptable.NOT_FOUND;
        }
        
        final void initValue(final int n, final String s, final Object o, final int n2) {
            if (1 > n || n > this.maxId) {
                throw new IllegalArgumentException();
            }
            if (s == null) {
                throw new IllegalArgumentException();
            }
            if (o == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            }
            ScriptableObject.checkValidAttributes(n2);
            if (this.obj.findPrototypeId(s) != n) {
                throw new IllegalArgumentException(s);
            }
            if (n != this.constructorId) {
                this.initSlot(n, s, o, n2);
                return;
            }
            if (!(o instanceof IdFunctionObject)) {
                throw new IllegalArgumentException("consructor should be initialized with IdFunctionObject");
            }
            this.constructor = (IdFunctionObject)o;
            this.constructorAttrs = (short)n2;
        }
        
        final void set(final int n, final Scriptable scriptable, final Object o) {
            if (o == Scriptable.NOT_FOUND) {
                throw new IllegalArgumentException();
            }
            this.ensureId(n);
            if ((this.attributeArray[n - 1] & 0x1) == 0x0) {
                if (scriptable == this.obj) {
                    Object null_VALUE;
                    if ((null_VALUE = o) == null) {
                        null_VALUE = UniqueTag.NULL_VALUE;
                    }
                    synchronized (this) {
                        this.valueArray[(n - 1) * 2] = null_VALUE;
                        return;
                    }
                }
                scriptable.put((String)this.valueArray[(n - 1) * 2 + 1], scriptable, o);
            }
        }
        
        final void setAttributes(final int n, final int n2) {
            ScriptableObject.checkValidAttributes(n2);
            this.ensureId(n);
            synchronized (this) {
                this.attributeArray[n - 1] = (short)n2;
            }
        }
    }
}
