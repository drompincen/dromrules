package com.example.evaluator;

import java.io.File;
import java.util.List;

/**
 * Builder for creating an Evaluator instance.
 * You must call either fromCsv(...) or withRules(...) before build().
 */
public class EvaluatorBuilder<T> {
    private final Class<T> type;
    private File csvFile;
    private List<Rule<T>> rules;

    private EvaluatorBuilder(Class<T> type) {
        this.type = type;
    }

    /**
     * Start building an Evaluator for the given type.
     */
    public static <T> EvaluatorBuilder<T> builder(Class<T> type) {
        return new EvaluatorBuilder<>(type);
    }

    /**
     * Load rules from the given CSV file.
     */
    public EvaluatorBuilder<T> fromCsv(File csvFile) {
        this.csvFile = csvFile;
        return this;
    }

    /**
     * Use this pre‑built list of Java Rule<T> instances.
     */
    public EvaluatorBuilder<T> withRules(List<Rule<T>> rules) {
        this.rules = rules;
        return this;
    }

    /**
     * Build the Evaluator. Throws if neither source was configured.
     */
    public Evaluator<T> build() throws Exception {
        if (rules != null) {
            return new Evaluator<>(rules);
        }
        if (csvFile != null) {
            // load CSV rules here and pass to the same constructor
            List<Rule<T>> loaded = CsvRuleLoader.load(type, csvFile);
            return new Evaluator<>(loaded);
        }
        throw new IllegalStateException(
                "Must configure rules: call fromCsv(...) or withRules(...) before build()"
        );
    }
}
