package com.example.evaluator;

/**
 * Builder for creating an Evaluator instance. Can be extended to add more configuration options.
 */
public class EvaluatorBuilder<T> {
    private final Class<T> type;

    private EvaluatorBuilder(Class<T> type) {
        this.type = type;
    }

    /**
     * Static factory to start building an Evaluator for the given type.
     */
    public static <T> EvaluatorBuilder<T> builder(Class<T> type) {
        return new EvaluatorBuilder<>(type);
    }

    /**
     * Build the Evaluator.
     */
    public Evaluator<T> build() {
        return new Evaluator<>(type);
    }
}
