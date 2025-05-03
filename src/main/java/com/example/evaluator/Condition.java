package com.example.evaluator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * A single condition on a field, supporting equality and numeric comparisons.
 */
public class Condition<T> {
    private final String fieldName;
    private final String operator;
    private final String operand;

    public Condition(String fieldName, String expression) {
        this.fieldName = fieldName;
        expression = expression.trim();
        if ("*".equals(expression)) {
            operator = "*";
            operand = null;
        } else if (expression.startsWith(">=")) {
            operator = ">=";
            operand = expression.substring(2);
        } else if (expression.startsWith("<=")) {
            operator = "<=";
            operand = expression.substring(2);
        } else if (expression.startsWith(">")) {
            operator = ">";
            operand = expression.substring(1);
        } else if (expression.startsWith("<")) {
            operator = "<";
            operand = expression.substring(1);
        } else if (expression.startsWith("=")) {
            operator = "==";
            operand = expression.substring(1);
        } else {
            operator = "==";
            operand = expression;
        }
    }

    @SuppressWarnings("unchecked")
    public boolean matches(T item) throws Exception {
        if ("*".equals(operator)) {
            return true;
        }
        Object value = PropertyUtils.getProperty(item, fieldName);
        if (value == null) {
            return false;
        }
        String valStr = value.toString();
        if ("==".equals(operator)) {
            return operand.equals(valStr);
        }
        // numeric comparison
        double fieldVal = Double.parseDouble(valStr);
        double cmpVal = Double.parseDouble(operand);
        switch (operator) {
            case ">":  return fieldVal > cmpVal;
            case "<":  return fieldVal < cmpVal;
            case ">=": return fieldVal >= cmpVal;
            case "<=": return fieldVal <= cmpVal;
            default:    return false;
        }
    }
}

