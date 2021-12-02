package org.example.nlo.task;

import java.util.Objects;
import java.util.random.RandomGenerator;

public class Interval {

    private final double min;
    private final double max;
    private final boolean minClose;
    private final boolean maxClose;

    private Interval(double min, boolean minClose, double max, boolean maxClose) {
        if (min > max || min == max && (!minClose || !maxClose)) {
            throw new IllegalArgumentException("Interval isn't correct!");
        }

        this.min = min;
        this.max = max;

        this.minClose = minClose;
        this.maxClose = maxClose;
    }

    public static Interval open(double min, double max) {
        return new Interval(min, false, max, false);
    }

    public static Interval close(double min, double max) {
        return new Interval(min, true, max, true);
    }

    public static Interval openClose(double min, double max) {
        return new Interval(min, false, max, true);
    }

    public static Interval closeOpen(double min, double max) {
        return new Interval(min, true, max, false);
    }

    public double min() {
        return min;
    }

    public double max() {
        return max;
    }

    public boolean isBelong(double x) {
        return checkMin(x) && checkMax(x);
    }

    private boolean checkMax(double x) {
        return x < max || isMaxBound(x);
    }

    private boolean isMaxBound(double x) {
        return x == max && maxClose;
    }

    private boolean checkMin(double x) {
        return x > min || isMinBound(x);
    }

    private boolean isMinBound(double x) {
        return x == min && minClose;
    }

    public double inclusiveMin() {
        return minClose ? min() : Math.nextUp(min());
    }

    public double exclusiveMax() {
        return maxClose ? Math.nextDown(max()) : max();
    }

    public double generateIntervalValue() {
        double origin = inclusiveMin();
        if (origin == Double.NEGATIVE_INFINITY) {
            origin = -Double.MAX_VALUE;
        }

        double bound = exclusiveMax();
        if (bound == Double.POSITIVE_INFINITY) {
            bound = Double.MAX_VALUE;
        }

        return RandomGenerator.getDefault().nextDouble(origin, bound);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Interval interval = (Interval) o;
        return Double.compare(interval.min, min) == 0 && Double.compare(interval.max, max) == 0 && minClose == interval.minClose && maxClose == interval.maxClose;
    }

    @Override
    public int hashCode() {
        return Objects.hash(min, max, minClose, maxClose);
    }
}
