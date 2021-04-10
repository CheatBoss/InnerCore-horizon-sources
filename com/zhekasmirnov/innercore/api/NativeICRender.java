package com.zhekasmirnov.innercore.api;

import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import java.util.*;

public class NativeICRender
{
    public static final int MODE_EXCLUDE = 1;
    public static final int MODE_INCLUDE = 0;
    private static HashMap<String, Group> activeGroups;
    private static int groupUUID;
    
    static {
        NativeICRender.groupUUID = 0;
        NativeICRender.activeGroups = new HashMap<String, Group>();
    }
    
    public static native void addToConditionOperatorAnd(final long p0, final long p1);
    
    public static native void addToConditionOperatorOr(final long p0, final long p1);
    
    public static native void collisionShapeAddEntry(final long p0, final long p1);
    
    public static native void collisionShapeEntryAddBox(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6);
    
    public static native void collisionShapeEntrySetCondition(final long p0, final long p1);
    
    public static native long conditionRandomSetAxisEnabled(final long p0, final int p1, final boolean p2);
    
    public static native long constructCollisionShape();
    
    public static native long constructCollisionShapeEntry();
    
    public static native long constructICRender();
    
    public static native long constructICRenderGroup();
    
    @JSStaticFunction
    public static Group getGroup(final String s) {
        if (NativeICRender.activeGroups.containsKey(s)) {
            return NativeICRender.activeGroups.get(s);
        }
        final Group group = new Group(s);
        NativeICRender.activeGroups.put(s, group);
        return group;
    }
    
    @JSStaticFunction
    public static Group getUnnamedGroup() {
        final StringBuilder sb = new StringBuilder();
        sb.append("_anonymous");
        final int groupUUID = NativeICRender.groupUUID;
        NativeICRender.groupUUID = groupUUID + 1;
        sb.append(groupUUID);
        return getGroup(sb.toString());
    }
    
    public static native long icRenderAddEntry(final long p0);
    
    public static native void icRenderEntrySetModel(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void icRenderEntrySetupCondition(final long p0, final long p1);
    
    public static native void icRenderGroupAddBlock(final long p0, final int p1, final int p2);
    
    public static native long newConditionBlock(final int p0, final int p1, final int p2, final long p3, final boolean p4);
    
    public static native long newConditionOperatorAnd();
    
    public static native long newConditionOperatorNot(final long p0);
    
    public static native long newConditionOperatorOr();
    
    public static native long newConditionRandom(final int p0, final int p1, final int p2);
    
    public static class AND extends CONDITION
    {
        private CONDITION[] conditions;
        
        public AND(final CONDITION... conditions) {
            super(NativeICRender.newConditionOperatorAnd());
            this.conditions = conditions;
            if (conditions.length < 2) {
                throw new IllegalArgumentException("ICRender AND condition got less than 2 parameters, it is useless");
            }
            for (int length = conditions.length, i = 0; i < length; ++i) {
                NativeICRender.addToConditionOperatorAnd(this.ptr, conditions[i].ptr);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("AND [\n");
            final CONDITION[] conditions = this.conditions;
            for (int length = conditions.length, i = 0; i < length; ++i) {
                final String[] split = conditions[i].toString().split("\n");
                for (int length2 = split.length, j = 0; j < length2; ++j) {
                    final String s = split[j];
                    sb.append("  ");
                    sb.append(s);
                    sb.append("\n");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    public static class BLOCK extends CONDITION
    {
        private Group group;
        private boolean mode;
        private int x;
        private int y;
        private int z;
        
        public BLOCK(final int x, final int y, final int z, final Group group, final boolean mode) {
            super(NativeICRender.newConditionBlock(x, y, z, group.ptr, mode));
            this.x = x;
            this.y = y;
            this.z = z;
            this.mode = mode;
            this.group = group;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("BLOCK [");
            sb.append(this.x);
            sb.append(" ");
            sb.append(this.y);
            sb.append(" ");
            sb.append(this.z);
            String s;
            if (this.mode) {
                s = " !";
            }
            else {
                s = " ";
            }
            sb.append(s);
            sb.append(this.group.getName());
            sb.append("]");
            return sb.toString();
        }
    }
    
    private static class CONDITION
    {
        protected final long ptr;
        
        private CONDITION(final long ptr) {
            this.ptr = ptr;
        }
    }
    
    public static class CollisionEntry
    {
        private long ptr;
        
        private CollisionEntry() {
            this.ptr = NativeICRender.constructCollisionShapeEntry();
        }
        
        public CollisionEntry addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
            NativeICRender.collisionShapeEntryAddBox(this.ptr, n, n2, n3, n4, n5, n6);
            return this;
        }
        
        public CollisionEntry setCondition(final CONDITION condition) {
            NativeICRender.collisionShapeEntrySetCondition(this.ptr, condition.ptr);
            return this;
        }
    }
    
    public static class CollisionShape
    {
        private long ptr;
        
        public CollisionShape() {
            this.ptr = NativeICRender.constructCollisionShape();
        }
        
        public CollisionEntry addEntry() {
            final CollisionEntry collisionEntry = new CollisionEntry();
            NativeICRender.collisionShapeAddEntry(this.ptr, collisionEntry.ptr);
            return collisionEntry;
        }
        
        public long getPtr() {
            return this.ptr;
        }
    }
    
    public static class Group implements IIdIterator
    {
        private String name;
        private long ptr;
        
        private Group(final String name) {
            this.ptr = NativeICRender.constructICRenderGroup();
            this.name = name;
        }
        
        public void add(final int n, final int n2) {
            NativeIdMapping.iterateMetadata(n, n2, (NativeIdMapping.IIdIterator)this);
        }
        
        public String getName() {
            return this.name;
        }
        
        @Override
        public void onIdDataIterated(final int n, final int n2) {
            NativeICRender.icRenderGroupAddBlock(this.ptr, n, n2);
        }
    }
    
    public static class Model
    {
        private List<RenderEntry> entries;
        private long ptr;
        
        public Model() {
            this.entries = new ArrayList<RenderEntry>();
            this.ptr = NativeICRender.constructICRender();
        }
        
        public Model(final NativeBlockModel model) {
            this();
            this.addEntry().setModel(model);
        }
        
        public RenderEntry addEntry() {
            final RenderEntry renderEntry = new RenderEntry(this);
            this.entries.add(renderEntry);
            return renderEntry;
        }
        
        public RenderEntry addEntry(final NativeBlockModel model) {
            return this.addEntry().setModel(model);
        }
        
        public RenderEntry addEntry(final NativeRenderMesh mesh) {
            return this.addEntry().setMesh(mesh);
        }
        
        public GuiBlockModel buildGuiModel(final boolean b) {
            final GuiBlockModel.Builder builder = new GuiBlockModel.Builder();
            for (final RenderEntry renderEntry : this.entries) {
                if (renderEntry.blockModel != null && renderEntry.condition == null) {
                    builder.add(renderEntry.blockModel.guiModelBuilder);
                }
            }
            return builder.build(b);
        }
        
        public long getPtr() {
            return this.ptr;
        }
    }
    
    public static class NOT extends CONDITION
    {
        private CONDITION condition;
        
        public NOT(final CONDITION condition) {
            super(NativeICRender.newConditionOperatorNot(condition.ptr));
            this.condition = condition;
        }
        
        @Override
        public String toString() {
            final String string = this.condition.toString();
            final String[] split = string.split("\n");
            if (split.length > 1) {
                final StringBuilder sb = new StringBuilder();
                sb.append("NOT [\n");
                for (int length = split.length, i = 0; i < length; ++i) {
                    final String s = split[i];
                    sb.append("  ");
                    sb.append(s);
                    sb.append("\n");
                }
                sb.append("]");
                return sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("NOT [");
            sb2.append(string);
            sb2.append("]");
            return sb2.toString();
        }
    }
    
    public static class OR extends CONDITION
    {
        private CONDITION[] conditions;
        
        public OR(final CONDITION... conditions) {
            super(NativeICRender.newConditionOperatorOr());
            this.conditions = conditions;
            if (conditions.length < 2) {
                throw new IllegalArgumentException("ICRender OR condition got less than 2 parameters, it is useless");
            }
            for (int length = conditions.length, i = 0; i < length; ++i) {
                NativeICRender.addToConditionOperatorOr(this.ptr, conditions[i].ptr);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("OR [\n");
            final CONDITION[] conditions = this.conditions;
            for (int length = conditions.length, i = 0; i < length; ++i) {
                final String[] split = conditions[i].toString().split("\n");
                for (int length2 = split.length, j = 0; j < length2; ++j) {
                    final String s = split[j];
                    sb.append("  ");
                    sb.append(s);
                    sb.append("\n");
                }
            }
            sb.append("]");
            return sb.toString();
        }
    }
    
    public static class RANDOM extends CONDITION
    {
        private int max;
        private int seed;
        private int value;
        
        public RANDOM(final int n) {
            this(0, n);
        }
        
        public RANDOM(final int n, final int n2) {
            this(n, n2, 131071);
        }
        
        public RANDOM(final int value, final int max, final int seed) {
            super(NativeICRender.newConditionRandom(value, max, seed));
            this.value = value;
            this.max = max;
            this.seed = seed;
        }
        
        public RANDOM setAxisEnabled(final int n, final boolean b) {
            NativeICRender.conditionRandomSetAxisEnabled(this.ptr, n, b);
            return this;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("RANDOM [value=");
            sb.append(this.value);
            sb.append(" max=");
            sb.append(this.max);
            sb.append(" seed=");
            sb.append(this.seed);
            sb.append("]");
            return sb.toString();
        }
    }
    
    public static class RenderEntry
    {
        private NativeBlockModel blockModel;
        private CONDITION condition;
        private Model icRender;
        private long ptr;
        
        private RenderEntry(final Model icRender) {
            this.blockModel = null;
            this.condition = null;
            this.ptr = NativeICRender.icRenderAddEntry(icRender.ptr);
            this.icRender = icRender;
        }
        
        public RenderEntry asCondition(final int n, final int n2, final int n3, final int n4, final int n5, final int n6) {
            final Group unnamedGroup = NativeICRender.getUnnamedGroup();
            unnamedGroup.add(n4, n5);
            return this.asCondition(n, n2, n3, unnamedGroup, n6);
        }
        
        public RenderEntry asCondition(final int n, final int n2, final int n3, final Group group, final int n4) {
            this.setCondition(new BLOCK(n, n2, n3, group, n4 != 0));
            return this;
        }
        
        public RenderEntry asCondition(final int n, final int n2, final int n3, final String s, final int n4) {
            return this.asCondition(n, n2, n3, NativeICRender.getGroup(s), n4);
        }
        
        public Model getParent() {
            return this.icRender;
        }
        
        public RenderEntry setCondition(final CONDITION condition) {
            NativeICRender.icRenderEntrySetupCondition(this.ptr, condition.ptr);
            this.condition = condition;
            return this;
        }
        
        public RenderEntry setMesh(final NativeRenderMesh nativeRenderMesh) {
            return this.setModel(new NativeBlockModel(nativeRenderMesh));
        }
        
        public RenderEntry setModel(final int n, final int n2, final int n3, final NativeBlockModel blockModel) {
            NativeICRender.icRenderEntrySetModel(this.ptr, n, n2, n3, blockModel.pointer);
            this.blockModel = blockModel;
            return this;
        }
        
        public RenderEntry setModel(final NativeBlockModel nativeBlockModel) {
            return this.setModel(0, 0, 0, nativeBlockModel);
        }
    }
}
