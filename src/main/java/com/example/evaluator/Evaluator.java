package com.example.evaluator;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Generic evaluator that processes a collection of objects against rules.
 * Rules may come from a CSV (via CsvRuleLoader) or be supplied directly.
 */
public class Evaluator<T> {
    private final List<Rule<T>> rules;

    /**
     * Constructor: always supply a list of rules.
     */
    public Evaluator(List<Rule<T>> rules) {
        this.rules = rules;
    }

    /**
     * Process each item in the collection against the loaded rules.
     * Mutates each item in place by applying the first matching rule.
     */
    public void process(Collection<T> items) {
        for (T item : items) {
            for (Rule<T> rule : rules) {
                try {
                    if (rule.matches(item)) {
                        rule.apply(item);
                        break; // only first‑match
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error applying rule", e);
                }
            }
        }
    }

    /**
     * Evaluate a single item (mutates and returns it).
     */
    public T evaluate(T item) {
        for (Rule<T> rule : rules) {
            try {
                if (rule.matches(item)) {
                    rule.apply(item);
                    break;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return item;
    }
}