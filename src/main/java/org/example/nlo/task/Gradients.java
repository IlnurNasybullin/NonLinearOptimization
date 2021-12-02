package org.example.nlo.task;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import static org.example.nlo.task.MathFunction.DX;

public final class Gradients {

    public static final double EPSILON = 0.000000001;
    public static final double K = 0.001;

    private Gradients() {}

    public static double gradientX(MathFunction function, Extremum extremum, int maxIter,
                                   Map<Double, Interval> intervals) {
        Stream<Map.Entry<Double, Double>> stream = intervals.entrySet()
                .stream()
                .mapToDouble(interval -> gradientX(function, extremum, maxIter, interval.getKey(), interval.getValue()))
                .mapToObj(x -> Map.entry(x, function.value(x)));

        Comparator<Map.Entry<Double, Double>> comparator = Map.Entry.comparingByValue();
        return extremum == Extremum.MAX ?
                stream.max(comparator).orElseThrow().getKey() :
                stream.min(comparator).orElseThrow().getKey();
    }

    public static double gradientX(MathFunction function, Extremum extremum, int maxIter, double startX,
                                   Interval interval) {
        return gradientX(function, extremum, maxIter, startX, interval, K, DX, EPSILON);
    }

    public static double gradientX(MathFunction function, Extremum extremum, int maxIter, double startX,
                                   Interval interval, double k, double dx, double epsilon) {
        double a = Math.abs(k);
        if (extremum == Extremum.MIN) {
            a = -a;
        }

        double x = startX;
        double df = function.derivative(x, dx);
        int i = 0;

        while (i < maxIter && Math.abs(df) >= epsilon) {
            x += a * df;
            if (!interval.isBelong(x)) {
                break;
            }
            df = function.derivative(x, dx);
            i++;
        }

        double min = interval.min();
        if (min == Double.NEGATIVE_INFINITY) {
            min = -Double.MAX_VALUE;
        }

        double max = interval.max();
        if (max == Double.POSITIVE_INFINITY) {
            max = Double.MAX_VALUE;
        }

        Stream<Map.Entry<Double, Double>> stream = DoubleStream.of(min, x, max)
                .mapToObj(v -> Map.entry(v, function.value(v)))
                .filter(v -> Double.isFinite(v.getValue()));

        Comparator<Map.Entry<Double, Double>> comparator = Map.Entry.comparingByValue();
        Optional<Map.Entry<Double, Double>> val = extremum == Extremum.MIN ?
                stream.min(comparator) :
                stream.max(comparator);

        return val.orElseThrow().getKey();
    }

}
