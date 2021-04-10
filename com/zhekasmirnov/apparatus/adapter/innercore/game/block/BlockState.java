package com.zhekasmirnov.apparatus.adapter.innercore.game.block;

import com.zhekasmirnov.apparatus.minecraft.enums.*;
import java.util.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;

public class BlockState
{
    public final int data;
    public final int id;
    private Map<String, Integer> namedStates;
    private int[] rawStates;
    public final int runtimeId;
    private Map<Integer, Integer> states;
    
    public BlockState(final int id, final int data) {
        this.rawStates = null;
        this.states = null;
        this.namedStates = null;
        this.id = id;
        this.data = data;
        this.runtimeId = -1;
    }
    
    public BlockState(final int id, final Map<?, Integer> map) {
        this.rawStates = null;
        this.states = null;
        this.namedStates = null;
        this.id = id;
        this.rawStates = new int[map.size() * 2];
        int n = 0;
        for (final Map.Entry<?, Integer> entry : map.entrySet()) {
            final Object key = entry.getKey();
            int n2 = -1;
            if (key instanceof Integer) {
                n2 = (int)key;
            }
            else if (key instanceof CharSequence) {
                n2 = GameEnums.getInt(GameEnums.getSingleton().getEnum("block_states", key.toString()), -1);
            }
            if (n2 == -1) {
                continue;
            }
            final int[] rawStates = this.rawStates;
            final int n3 = n + 1;
            rawStates[n] = n2;
            this.rawStates[n3] = entry.getValue();
            n = n3 + 1;
        }
        this.runtimeId = runtimeIdByStates(id, this.rawStates);
        this.data = getDataFromRuntimeId(this.runtimeId);
    }
    
    public BlockState(final int n, final Scriptable scriptable) {
        this(n, scriptableToStateMap(scriptable));
    }
    
    private BlockState(final int id, final int[] rawStates) {
        this.rawStates = null;
        this.states = null;
        this.namedStates = null;
        this.id = id;
        this.rawStates = rawStates;
        this.runtimeId = runtimeIdByStates(id, rawStates);
        this.data = getDataFromRuntimeId(this.runtimeId);
    }
    
    public BlockState(final long n) {
        this.rawStates = null;
        this.states = null;
        this.namedStates = null;
        final long n2 = n & -1L;
        this.id = ((int)(n2 >> 16) & 0xFFFF);
        this.data = (int)(n2 & 0xFFFFL);
        this.runtimeId = (int)(n >> 32);
    }
    
    private static native int[] getAllStatesFromId(final int p0);
    
    public static native int getDataFromRuntimeId(final int p0);
    
    public static native int getIdFromRuntimeId(final int p0);
    
    private static native int getStateFromId(final int p0, final int p1);
    
    public static native int runtimeIdByStates(final int p0, final int[] p1);
    
    private static Map<Integer, Integer> scriptableToStateMap(final Scriptable scriptable) {
        final HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
        final Object[] ids = scriptable.getIds();
        for (int length = ids.length, i = 0; i < length; ++i) {
            final String string = ids[i].toString();
            int n;
            if ((n = GameEnums.getInt(GameEnums.getSingleton().getEnum("block_states", string), -1)) == -1) {
                try {
                    n = Integer.parseInt(string);
                }
                catch (NumberFormatException ex) {
                    continue;
                }
            }
            final Object value = scriptable.get(string, scriptable);
            int intValue;
            if (value instanceof Number) {
                intValue = ((Number)value).intValue();
            }
            else {
                intValue = 0;
            }
            hashMap.put(n, intValue);
        }
        return hashMap;
    }
    
    public BlockState addState(final int n, final int n2) {
        final HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>(this.getStates());
        hashMap.put(n, n2);
        return new BlockState(this.id, hashMap);
    }
    
    public BlockState addStates(final Map<?, Integer> map) {
        final HashMap<Object, Object> hashMap = new HashMap<Object, Object>(this.getStates());
        hashMap.putAll(map);
        return new BlockState(this.id, (Map<?, Integer>)hashMap);
    }
    
    public BlockState addStates(final Scriptable scriptable) {
        return this.addStates(scriptableToStateMap(scriptable));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final BlockState blockState = (BlockState)o;
        return this.id == blockState.id && this.data == blockState.data && this.runtimeId == blockState.runtimeId && this.getStates().equals(blockState.getStates());
    }
    
    public int getData() {
        return this.data;
    }
    
    public int getId() {
        return this.id;
    }
    
    public Map<String, Integer> getNamedStates() {
        if (this.namedStates == null) {
            this.namedStates = new HashMap<String, Integer>();
            for (final Map.Entry<Integer, Integer> entry : this.getStates().entrySet()) {
                this.namedStates.put(GameEnums.getSingleton().getKeyForEnum("block_states", entry.getKey()), entry.getValue());
            }
        }
        return this.namedStates;
    }
    
    public ScriptableObject getNamedStatesScriptable() {
        final Map<String, Integer> namedStates = this.getNamedStates();
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        for (final Map.Entry<String, Integer> entry : namedStates.entrySet()) {
            empty.put((String)entry.getKey(), (Scriptable)empty, (Object)entry.getValue());
        }
        return empty;
    }
    
    public int getRuntimeId() {
        return this.runtimeId;
    }
    
    public int getState(final int n) {
        if (this.runtimeId > 0) {
            return getStateFromId(this.runtimeId, n);
        }
        return -1;
    }
    
    public Map<Integer, Integer> getStates() {
        if (this.states == null) {
            this.states = new HashMap<Integer, Integer>();
            if (this.runtimeId > 0) {
                int[] rawStates;
                if (this.rawStates != null) {
                    rawStates = this.rawStates;
                }
                else {
                    rawStates = getAllStatesFromId(this.runtimeId);
                }
                this.rawStates = rawStates;
                for (int i = 0; i < this.rawStates.length / 2; ++i) {
                    this.states.put(this.rawStates[i * 2], this.rawStates[i * 2 + 1]);
                }
            }
        }
        return this.states;
    }
    
    public ScriptableObject getStatesScriptable() {
        final Map<Integer, Integer> states = this.getStates();
        final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
        for (final Map.Entry<Integer, Integer> entry : states.entrySet()) {
            empty.put(entry.getKey().toString(), (Scriptable)empty, (Object)entry.getValue());
        }
        return empty;
    }
    
    public boolean hasState(final int n) {
        return this.getState(n) != -1;
    }
    
    public boolean isValidState() {
        return this.runtimeId != -1;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BlockState{id=");
        sb.append(this.id);
        sb.append(", data=");
        sb.append(this.data);
        sb.append(", runtimeId=");
        sb.append(this.runtimeId);
        sb.append(", states=");
        sb.append(this.getNamedStates());
        sb.append("}");
        return sb.toString();
    }
}
