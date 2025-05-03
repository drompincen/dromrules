package com.example.evaluator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * Applies a single action by setting a field to a specified value.
 */
public class Action<T> {
    private final String fieldName;
    private final String value;

    public Action(String fieldName, String value) {
        this.fieldName = fieldName;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public void apply(T item) throws Exception {
        Object toSet;
        if (value.startsWith("$")) {
            // dynamic: get property from item
            String refField = value.substring(1);
            toSet = PropertyUtils.getProperty(item, refField);
        } else {
            Class<?> type = PropertyUtils.getPropertyType(item, fieldName);
            String v = value;
            if (type == String.class) {
                toSet = v;
            } else if (type == int.class || type == Integer.class) {
                toSet = Integer.valueOf(v);
            } else if (type == long.class || type == Long.class) {
                toSet = Long.valueOf(v);
            } else if (type == double.class || type == Double.class) {
                toSet = Double.valueOf(v);
            } else if (type == float.class || type == Float.class) {
                toSet = Float.valueOf(v);
            } else {
                toSet = v;
            }
        }
        PropertyUtils.setProperty(item, fieldName, toSet);
    }
}