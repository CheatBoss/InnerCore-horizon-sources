package com.zhekasmirnov.apparatus.ecs.core.data;

import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import java.util.*;
import com.zhekasmirnov.apparatus.util.*;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.*;
import org.json.*;

public class SharedData implements OnDataChangedListener, DataVerifier
{
    public static final int FLAG_NETWORK = 2;
    public static final int FLAG_SAVE = 1;
    private static final String NETWORK_NULL = "$null$";
    private final Map<String, Field> fieldMap;
    private SyncedNetworkData networkData;
    
    public SharedData() {
        this.fieldMap = new HashMap<String, Field>();
        this.networkData = null;
    }
    
    private void onFieldChanged(final Field field) {
        if (this.networkData != null) {
            Object access$300;
            if ((access$300 = field.getSerialized()) == null) {
                access$300 = "$null$";
            }
            this.networkData.putObject(field.name, access$300);
        }
    }
    
    public Field addDefine(final String s, final FieldType fieldType) {
        if (this.fieldMap.containsKey(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("field ");
            sb.append(s);
            sb.append(" already defined");
            throw new IllegalArgumentException(sb.toString());
        }
        return this.fieldMap.put(s, new Field(s, fieldType));
    }
    
    @Override
    public void onChanged(final SyncedNetworkData syncedNetworkData, final String s, final boolean b) {
        if (b) {
            final Field field = this.fieldMap.get(s);
            if (field != null && (field.flags & 0x2) != 0x0) {
                Object object;
                if ("$null$".equals(object = syncedNetworkData.getObject(s))) {
                    object = null;
                }
                field.putSerialized(object);
            }
        }
    }
    
    public boolean setField(final String s, final Object o) {
        final Field field = this.fieldMap.get(s);
        if (field == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("field ");
            sb.append(s);
            sb.append(" was not defined");
            throw new IllegalArgumentException(sb.toString());
        }
        return field.put(o);
    }
    
    public void setNetworkData(final SyncedNetworkData networkData) {
        (this.networkData = networkData).addOnDataChangedListener((SyncedNetworkData.OnDataChangedListener)this);
        this.networkData.setGlobalVerifier((SyncedNetworkData.DataVerifier)this);
    }
    
    @Override
    public Object verify(final String s, final Object o) {
        final Field field = this.fieldMap.get(s);
        if (field == null || (field.flags & 0x2) == 0x0) {
            return false;
        }
        if (field.type.verify(o, true)) {
            return o;
        }
        return null;
    }
    
    public class Field
    {
        private int flags;
        private FieldListener listener;
        private final String name;
        private final FieldType type;
        private FieldValidator validator;
        private Object value;
        
        public Field(final String name, final FieldType type) {
            this.flags = 0;
            this.name = name;
            this.type = type;
            this.value = type.getDefaultValue();
        }
        
        private Object getSerialized() {
            return this.type.serialize(this.value);
        }
        
        private boolean put(final Object value) {
            if (this.type.verify(value, false)) {
                if (!Java8BackComp.equals(value, this.value)) {
                    this.setValue(value);
                }
                return true;
            }
            return false;
        }
        
        private boolean putSerialized(Object access$000) {
            access$000 = this.type.deserialize(access$000);
            if (this.validator != null) {
                synchronized (this) {
                    this.setValue(this.validator.validate(access$000, this.value));
                    return true;
                }
            }
            this.setValue(access$000);
            return true;
        }
        
        private void setValue(final Object value) {
            final Object value2 = this.value;
            this.value = value;
            if (this.listener != null) {
                this.listener.onChanged(this, this.value, value2);
            }
        }
        
        public Field setDefaultValue(final Object value) {
            this.value = value;
            return this;
        }
        
        public Field setFlags(final int flags) {
            this.flags = flags;
            return this;
        }
        
        public Field setListener(final FieldListener listener) {
            this.listener = listener;
            return this;
        }
        
        public Field setValidator(final FieldValidator validator) {
            this.validator = validator;
            return this;
        }
    }
    
    public interface FieldListener
    {
        void onChanged(final Field p0, final Object p1, final Object p2);
    }
    
    public enum FieldType
    {
        ANY((Object)null), 
        BOOLEAN((Object)false), 
        JSON((Object)new JSONObject()), 
        NUMBER((Object)0), 
        SERIALIZABLE((Object)null), 
        STRING((Object)null);
        
        private final Object defaultValue;
        
        private FieldType(final Object defaultValue) {
            this.defaultValue = defaultValue;
        }
        
        private Object deserialize(Object scriptableFromJson) {
            if (scriptableFromJson == null) {
                return null;
            }
            switch (this) {
                default: {
                    return scriptableFromJson;
                }
                case SERIALIZABLE: {
                    try {
                        scriptableFromJson = ScriptableSerializer.scriptableFromJson(ScriptableSerializer.stringToJson((String)scriptableFromJson));
                        return scriptableFromJson;
                    }
                    catch (JSONException ex) {
                        return null;
                    }
                }
                case JSON: {
                    try {
                        return new JSONObject(scriptableFromJson.toString());
                    }
                    catch (JSONException ex2) {
                        return null;
                    }
                    break;
                }
            }
        }
        
        private Object serialize(final Object o) {
            if (o instanceof Number || o instanceof CharSequence) {
                return o;
            }
            if (o instanceof Boolean) {
                return o;
            }
            if (o instanceof JSONObject) {
                return ((JSONObject)o).toString();
            }
            final StringBuilder sb = new StringBuilder();
            sb.append(ScriptableSerializer.scriptableToJson(o, null));
            sb.append("");
            return sb.toString();
        }
        
        private boolean verify(final Object o, final boolean b) {
            final int n = SharedData$1.$SwitchMap$com$zhekasmirnov$apparatus$ecs$core$data$SharedData$FieldType[this.ordinal()];
            boolean b2 = true;
            switch (n) {
                default: {
                    return true;
                }
                case 5: {
                    if (b) {
                        if (o instanceof String) {
                            return true;
                        }
                        b2 = false;
                    }
                    return b2;
                }
                case 4: {
                    if (b) {
                        return o instanceof String;
                    }
                    return o instanceof JSONObject;
                }
                case 3: {
                    return o instanceof Boolean;
                }
                case 2: {
                    return o instanceof CharSequence;
                }
                case 1: {
                    return o instanceof Number;
                }
            }
        }
        
        public Object getDefaultValue() {
            return this.defaultValue;
        }
    }
    
    public interface FieldValidator
    {
        Object validate(final Object p0, final Object p1);
    }
}
