package com.android.dx.dex.file;

import com.android.dx.util.*;

public enum ItemType implements ToHuman
{
    TYPE_ANNOTATIONS_DIRECTORY_ITEM(8198, "annotations_directory_item"), 
    TYPE_ANNOTATION_ITEM(8196, "annotation_item"), 
    TYPE_ANNOTATION_SET_ITEM(4099, "annotation_set_item"), 
    TYPE_ANNOTATION_SET_REF_ITEM(-1, "annotation_set_ref_item"), 
    TYPE_ANNOTATION_SET_REF_LIST(4098, "annotation_set_ref_list"), 
    TYPE_CLASS_DATA_ITEM(8192, "class_data_item"), 
    TYPE_CLASS_DEF_ITEM(6, "class_def_item"), 
    TYPE_CODE_ITEM(8193, "code_item"), 
    TYPE_DEBUG_INFO_ITEM(8195, "debug_info_item"), 
    TYPE_ENCODED_ARRAY_ITEM(8197, "encoded_array_item"), 
    TYPE_EXCEPTION_HANDLER_ITEM(-1, "exception_handler_item"), 
    TYPE_FIELD_ID_ITEM(4, "field_id_item"), 
    TYPE_HEADER_ITEM(0, "header_item"), 
    TYPE_MAP_ITEM(-1, "map_item"), 
    TYPE_MAP_LIST(4096, "map_list"), 
    TYPE_METHOD_ID_ITEM(5, "method_id_item"), 
    TYPE_PROTO_ID_ITEM(3, "proto_id_item"), 
    TYPE_STRING_DATA_ITEM(8194, "string_data_item"), 
    TYPE_STRING_ID_ITEM(1, "string_id_item"), 
    TYPE_TYPE_ID_ITEM(2, "type_id_item"), 
    TYPE_TYPE_ITEM(-1, "type_item"), 
    TYPE_TYPE_LIST(4097, "type_list");
    
    private final String humanName;
    private final int mapValue;
    private final String typeName;
    
    private ItemType(final int mapValue, final String typeName) {
        this.mapValue = mapValue;
        this.typeName = typeName;
        substring = typeName;
        if (typeName.endsWith("_item")) {
            substring = typeName.substring(0, typeName.length() - 5);
        }
        this.humanName = substring.replace('_', ' ');
    }
    
    public int getMapValue() {
        return this.mapValue;
    }
    
    public String getTypeName() {
        return this.typeName;
    }
    
    @Override
    public String toHuman() {
        return this.humanName;
    }
}
