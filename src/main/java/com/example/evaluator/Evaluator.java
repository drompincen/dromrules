package com.example.evaluator;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Generic evaluator that processes a collection of objects against rules defined in a CSV file.
 */
public class Evaluator<T> {
    private final Class<T> type;

    Evaluator(Class<T> type) {
        this.type = type;
    }

    /**
     * Process each item in the collection against rules in the CSV file.
     * If a rule matches, its action is applied to the item.
     *
     * Usage: Evaluator.builder(Car.class).build().process(cars, new File("rules.csv"));
     */
    public void process(Collection<T> items, File csvFile) throws Exception {
        List<Rule<T>> rules = CsvRuleLoader.load(type, csvFile);
        for (T item : items) {
            for (Rule<T> rule : rules) {
                if (rule.matches(item)) {
                    rule.apply(item);
                }
            }
        }
    }
}
