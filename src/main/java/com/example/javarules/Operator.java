package com.example.javarules;

/**
 * Represents comparison operators used in rule conditions
 */
public enum Operator {
    EQUALS,
    GREATER_THAN,
    LESS_THAN,
    ANY;

    public static Operator fromString(String value) {
        if (value.equals("*")) {
            return ANY;
        } else if (value.startsWith(">")) {
            return GREATER_THAN;
        } else if (value.startsWith("<")) {
            return LESS_THAN;
        } else {
            return EQUALS;
        }
    }

    public String toString() {
        switch (this) {
            case EQUALS:
                return "=";
            case GREATER_THAN:
                return ">";
            case LESS_THAN:
                return "<";
            case ANY:
                return "*";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }
}