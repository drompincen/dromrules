package com.example.evaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A rule consisting of multiple conditions and one or more actions.
 */
public class Rule<T> {
    private final List<Condition<T>> conditions;
    private final List<Action<T>> actions;

    public Rule(List<Condition<T>> conditions, List<Action<T>> actions) {
        this.conditions = conditions;
        this.actions = actions;
    }

    public static <T> Rule<T> fromMaps(Class<T> type,
                                       Map<String, String> condMap,
                                       Map<String, String> actionMap) {
        List<Condition<T>> conds = new ArrayList<>();
        for (Map.Entry<String, String> e : condMap.entrySet()) {
            conds.add(new Condition<>(e.getKey(), e.getValue()));
        }
        List<Action<T>> acts = new ArrayList<>();
        for (Map.Entry<String, String> e : actionMap.entrySet()) {
            acts.add(new Action<>(e.getKey(), e.getValue()));
        }
        return new Rule<>(conds, acts);
    }

    public boolean matches(T item) throws Exception {
        for (Condition<T> c : conditions) {
            if (!c.matches(item)) {
                return false;
            }
        }
        return true;
    }

    public void apply(T item) throws Exception {
        for (Action<T> a : actions) {
            a.apply(item);
        }
    }
}