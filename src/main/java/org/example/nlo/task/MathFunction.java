package org.example.nlo.task;

import java.util.function.DoubleUnaryOperator;

@FunctionalInterface
public interface MathFunction extends DoubleUnaryOperator {

    double DX = 0.0001;

    default double minX() {
        return Double.NEGATIVE_INFINITY;
    }

    default double maxX() {
        return Double.POSITIVE_INFINITY;
    }

    default double value(double x) {
        return applyAsDouble(x);
    }

    default double derivative(double x) {
        return derivative(x, DX);
    }

    default double derivative(double x, double dx) {
        return (value(x + dx) - value(x)) / dx;
    }
}
