package com.example.javarules;

import com.example.evaluator.Action;
import com.example.evaluator.Condition;
import com.example.evaluator.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A generic builder for creating Rule<T> instances via reflection on T's getters/setters.
 */
public class RuleBuilder<T> {
    private final Class<T> type;
    private final Map<String, String> conditions = new HashMap<>();
    private final Map<String, Operator> operators = new HashMap<>();
    private final Map<String, String> actions = new HashMap<>();
    private final Map<String, Boolean> copyFlags = new HashMap<>();

    private RuleBuilder(Class<T> type) {
        this.type = type;
    }

    /**
     * Start building a Rule<T> for the given class.
     */
    public static <T> RuleBuilder<T> rule(Class<T> type) {
        return new RuleBuilder<>(type);
    }

    public RuleBuilder<T> when(String field, Operator operator, String value) {
        conditions.put(field, value);
        operators.put(field, operator);
        return this;
    }

    public RuleBuilder<T> thenSet(String field, String value) {
        actions.put(field, value);
        copyFlags.put(field, false);
        return this;
    }

    public RuleBuilder<T> thenCopy(String field, String sourceField) {
        actions.put(field, sourceField);
        copyFlags.put(field, true);
        return this;
    }

    public Rule<T> build() {
        List<Condition<T>> builtConditions = new ArrayList<>();
        List<Action<T>> builtActions = new ArrayList<>();

        // Build conditions using reflection
        for (Map.Entry<String, String> e : conditions.entrySet()) {
            String field = e.getKey();
            String rawValue = e.getValue();
            Operator op = operators.get(field);
            var getter = findGetter(field);

            builtConditions.add(new Condition<T>(field, rawValue) {
                @Override
                public boolean matches(T item) {
                    try {
                        Object val = getter.invoke(item);
                        String input = val == null ? null : val.toString();
                        if (op == Operator.ANY) return true;
                        if (input == null) return false;
                        return switch (op) {
                            case EQUALS -> input.equals(rawValue);
                            case GREATER_THAN -> Double.parseDouble(input) > Double.parseDouble(rawValue);
                            case LESS_THAN -> Double.parseDouble(input) < Double.parseDouble(rawValue);
                            default -> false;
                        };
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        // Build actions using reflection
        for (Map.Entry<String, String> e : actions.entrySet()) {
            String field = e.getKey();
            String rawValue = e.getValue();
            boolean isCopy = copyFlags.get(field);
            var setter = findSetter(field);

            builtActions.add(new Action<T>(field, rawValue) {
                @Override
                public void apply(T item) {
                    try {
                        Object arg;
                        if (isCopy) {
                            var sourceGetter = findGetter(rawValue);
                            arg = sourceGetter.invoke(item);
                        } else {
                            arg = convert(rawValue, setter.getParameterTypes()[0]);
                        }
                        setter.invoke(item, arg);
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

        return new Rule<>(builtConditions, builtActions);
    }

    private java.lang.reflect.Method findGetter(String field) {
        String cap = Character.toUpperCase(field.charAt(0)) + field.substring(1);
        try {
            return type.getMethod("get" + cap);
        } catch (NoSuchMethodException e1) {
            try {
                return type.getMethod("is" + cap);
            } catch (NoSuchMethodException e2) {
                throw new IllegalArgumentException("No getter for " + field);
            }
        }
    }

    private java.lang.reflect.Method findSetter(String field) {
        String cap = Character.toUpperCase(field.charAt(0)) + field.substring(1);
        for (var m : type.getMethods()) {
            if (m.getName().equals("set" + cap) && m.getParameterCount() == 1) {
                return m;
            }
        }
        throw new IllegalArgumentException("No setter for " + field);
    }

    private Object convert(String raw, Class<?> target) {
        if (target == String.class) return raw;
        if (target == int.class || target == Integer.class) return Integer.parseInt(raw);
        if (target == long.class || target == Long.class) return Long.parseLong(raw);
        if (target == double.class || target == Double.class) return Double.parseDouble(raw);
        if (target == boolean.class || target == Boolean.class) return Boolean.parseBoolean(raw);
        throw new IllegalArgumentException("Cannot convert '" + raw + "' to " + target);
    }
}