package com.zhekasmirnov.horizon.util;

import org.json.*;
import java.util.*;

public class JSONUtils
{
    public static <T> List<T> toList(final JSONArray array) {
        final List<T> list = new ArrayList<T>();
        for (int i = 0; i < array.length(); ++i) {
            list.add((T)array.opt(i));
        }
        return list;
    }
}
