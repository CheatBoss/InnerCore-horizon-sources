package com.zhekasmirnov.apparatus.ecs.core;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.lang.reflect.*;
import java.util.function.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$EntityComponentRegistry$D9kmXzZHA4EpVCkog86v9b_n0Ng.class })
public class EntityComponentRegistry
{
    private static final EntityComponentRegistry singleton;
    private final Map<String, ComponentType> componentTypeMap;
    
    static {
        singleton = new EntityComponentRegistry();
    }
    
    private EntityComponentRegistry() {
        this.componentTypeMap = new HashMap<String, ComponentType>();
    }
    
    private ComponentType getComponentTypeForClass(final Class<?> p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: monitorenter   
        //     2: ldc             Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponent;.class
        //     4: aload_1        
        //     5: if_acmpeq       211
        //     8: ldc             Ljava/lang/Object;.class
        //    10: aload_1        
        //    11: if_acmpne       17
        //    14: goto            211
        //    17: aload_1        
        //    18: invokevirtual   java/lang/Class.getCanonicalName:()Ljava/lang/String;
        //    21: astore          6
        //    23: aload_0        
        //    24: getfield        com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry.componentTypeMap:Ljava/util/Map;
        //    27: aload           6
        //    29: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    34: checkcast       Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;
        //    37: astore          4
        //    39: aload           4
        //    41: astore          5
        //    43: aload           4
        //    45: ifnonnull       206
        //    48: aconst_null    
        //    49: astore          4
        //    51: aload_1        
        //    52: invokevirtual   java/lang/Class.isInterface:()Z
        //    55: ifne            68
        //    58: aload_0        
        //    59: aload_1        
        //    60: invokevirtual   java/lang/Class.getSuperclass:()Ljava/lang/Class;
        //    63: invokespecial   com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry.getComponentTypeForClass:(Ljava/lang/Class;)Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;
        //    66: astore          4
        //    68: new             Ljava/util/ArrayList;
        //    71: dup            
        //    72: invokespecial   java/util/ArrayList.<init>:()V
        //    75: astore          5
        //    77: aload_1        
        //    78: invokevirtual   java/lang/Class.getInterfaces:()[Ljava/lang/Class;
        //    81: astore          7
        //    83: aload           7
        //    85: arraylength    
        //    86: istore_3       
        //    87: iconst_0       
        //    88: istore_2       
        //    89: iload_2        
        //    90: iload_3        
        //    91: if_icmpge       126
        //    94: aload           7
        //    96: iload_2        
        //    97: aaload         
        //    98: astore          8
        //   100: aload_0        
        //   101: aload           8
        //   103: invokespecial   com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry.getComponentTypeForClass:(Ljava/lang/Class;)Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;
        //   106: astore          9
        //   108: aload           8
        //   110: ifnull          220
        //   113: aload           5
        //   115: aload           9
        //   117: invokeinterface java/util/List.add:(Ljava/lang/Object;)Z
        //   122: pop            
        //   123: goto            220
        //   126: aload_0        
        //   127: getfield        com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry.componentTypeMap:Ljava/util/Map;
        //   130: astore          7
        //   132: new             Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;
        //   135: dup            
        //   136: aload           6
        //   138: aload_1        
        //   139: invokevirtual   java/lang/Class.isInterface:()Z
        //   142: aload           4
        //   144: aload           5
        //   146: aconst_null    
        //   147: invokespecial   com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType.<init>:(Ljava/lang/String;ZLcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;Ljava/util/List;Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$1;)V
        //   150: astore          5
        //   152: aload           5
        //   154: astore          4
        //   156: aload           7
        //   158: aload           6
        //   160: aload           5
        //   162: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   167: pop            
        //   168: aload           4
        //   170: astore          5
        //   172: aload           4
        //   174: invokevirtual   com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType.isInterface:()Z
        //   177: ifne            206
        //   180: aload           4
        //   182: astore          5
        //   184: ldc             Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponent;.class
        //   186: aload_1        
        //   187: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
        //   190: ifeq            206
        //   193: aload           4
        //   195: aload_1        
        //   196: invokestatic    com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry.makeFactoryForClass:(Ljava/lang/Class;)Ljava/util/function/Supplier;
        //   199: invokestatic    com/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType.access$100:(Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;Ljava/util/function/Supplier;)V
        //   202: aload           4
        //   204: astore          5
        //   206: aload_0        
        //   207: monitorexit    
        //   208: aload           5
        //   210: areturn        
        //   211: aload_0        
        //   212: monitorexit    
        //   213: aconst_null    
        //   214: areturn        
        //   215: astore_1       
        //   216: aload_0        
        //   217: monitorexit    
        //   218: aload_1        
        //   219: athrow         
        //   220: iload_2        
        //   221: iconst_1       
        //   222: iadd           
        //   223: istore_2       
        //   224: goto            89
        //    Signature:
        //  (Ljava/lang/Class<*>;)Lcom/zhekasmirnov/apparatus/ecs/core/EntityComponentRegistry$ComponentType;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  17     39     215    220    Any
        //  51     68     215    220    Any
        //  68     87     215    220    Any
        //  100    108    215    220    Any
        //  113    123    215    220    Any
        //  126    152    215    220    Any
        //  156    168    215    220    Any
        //  172    180    215    220    Any
        //  184    202    215    220    Any
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
    
    public static EntityComponentRegistry getSingleton() {
        return EntityComponentRegistry.singleton;
    }
    
    private static Supplier<? extends EntityComponent> makeFactoryForClass(final Class<? extends EntityComponent> clazz) {
        try {
            return (Supplier<? extends EntityComponent>)new -$$Lambda$EntityComponentRegistry$D9kmXzZHA4EpVCkog86v9b_n0Ng(clazz.getConstructor((Class<?>[])new Class[0]), clazz);
        }
        catch (NoSuchMethodException ex) {
            ICLog.e("ERROR", "EntityComponent must declare public constructor with no parameters", ex);
            return null;
        }
        catch (ClassCastException ex2) {
            return null;
        }
    }
    
    public ComponentType getComponentType(final String s) {
        final ComponentType componentType = this.componentTypeMap.get(s);
        if (componentType == null) {
            try {
                return this.getComponentTypeForClass(Class.forName(s));
            }
            catch (ClassNotFoundException ex) {
                return null;
            }
        }
        return componentType;
    }
    
    public EntityComponent newComponent(final Class<? extends EntityComponent> clazz) {
        return this.newComponent(clazz.getCanonicalName());
    }
    
    public EntityComponent newComponent(final String s) {
        final ComponentType componentType = this.getComponentType(s);
        if (componentType != null && componentType.factory != null) {
            return (EntityComponent)componentType.factory.get();
        }
        return null;
    }
    
    public static class ComponentType
    {
        private final List<String> allParentTypeNames;
        private final List<ComponentType> allParentTypes;
        private Supplier<? extends EntityComponent> factory;
        private final List<ComponentType> interfaces;
        private final boolean isInterface;
        private final String name;
        private final ComponentType parent;
        private final EntityFamily singletonEntityFamily;
        
        private ComponentType(final String name, final boolean isInterface, ComponentType parent, final List<ComponentType> interfaces) {
            this.allParentTypes = new ArrayList<ComponentType>();
            this.allParentTypeNames = new ArrayList<String>();
            this.singletonEntityFamily = new EntityFamily();
            this.factory = null;
            this.name = name;
            this.isInterface = isInterface;
            this.parent = parent;
            this.interfaces = interfaces;
            this.singletonEntityFamily.addComponent(name);
            this.singletonEntityFamily.setMutable(false);
            if (isInterface && parent != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("interface component type ");
                sb.append(name);
                sb.append(" got non-null parent type ");
                sb.append(parent.getName());
                sb.append(", all interface parents should be passed in interfaces parameter");
                throw new IllegalArgumentException(sb.toString());
            }
            if (parent != null && parent.isInterface) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("component type ");
                sb2.append(name);
                sb2.append(" got interface type parent ");
                sb2.append(parent.getName());
                sb2.append(" parent type must not be interface");
                throw new IllegalArgumentException(sb2.toString());
            }
            this.allParentTypes.add(this);
            if (parent != null) {
                this.allParentTypes.addAll(parent.allParentTypes);
            }
            final Iterator<ComponentType> iterator = interfaces.iterator();
            while (iterator.hasNext()) {
                parent = iterator.next();
                if (!parent.isInterface) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("component type ");
                    sb3.append(name);
                    sb3.append(" got non-interface type ");
                    sb3.append(parent.getName());
                    sb3.append(" as interface");
                    throw new IllegalArgumentException(sb3.toString());
                }
                this.allParentTypes.addAll(parent.allParentTypes);
            }
            final Iterator<ComponentType> iterator2 = this.allParentTypes.iterator();
            while (iterator2.hasNext()) {
                parent = iterator2.next();
                this.allParentTypeNames.add(parent.getName());
            }
        }
        
        private void setFactory(final Supplier<? extends EntityComponent> factory) {
            this.factory = factory;
        }
        
        public List<String> getAllParentTypeNames() {
            return this.allParentTypeNames;
        }
        
        public List<ComponentType> getAllParentTypes() {
            return this.allParentTypes;
        }
        
        public EntityFamily getEntityFamily() {
            return this.singletonEntityFamily;
        }
        
        public Supplier<? extends EntityComponent> getFactory() {
            return this.factory;
        }
        
        public List<ComponentType> getInterfaces() {
            return this.interfaces;
        }
        
        public String getName() {
            return this.name;
        }
        
        public ComponentType getParent() {
            return this.parent;
        }
        
        public boolean isInterface() {
            return this.isInterface;
        }
    }
}
