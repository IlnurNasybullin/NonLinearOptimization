package org.example.nlo.task;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.Map;

public final class GlobalParameters {

    public static final SimpleDoubleProperty a = new SimpleDoubleProperty(null, "a");
    public static final SimpleDoubleProperty b = new SimpleDoubleProperty(null, "b");

    public static final SimpleDoubleProperty P = new SimpleDoubleProperty(null, "P");
    public static final SimpleDoubleProperty C_f = new SimpleDoubleProperty(null, "C_f");

    public static final SimpleDoubleProperty K_1 = new SimpleDoubleProperty(null, "K_1");
    public static final SimpleDoubleProperty K_2 = new SimpleDoubleProperty(null, "K_2");

    public static final SimpleDoubleProperty T = new SimpleDoubleProperty(null, "T");
    public static final SimpleDoubleProperty S = new SimpleDoubleProperty(null, "S");

    public static final SimpleDoubleProperty X_T = new SimpleDoubleProperty(null, "X_T");
    public static final SimpleDoubleProperty X_S = new SimpleDoubleProperty(null, "X_S");

    public static final SimpleDoubleProperty X_1 = new SimpleDoubleProperty(null, "X_1");
    public static final SimpleDoubleProperty X_2 = new SimpleDoubleProperty(null, "X_2");

    public static final MathFunction C_v = x -> K_1.get() * x + K_2.get() * x * x;
    public static final MathFunction P_x = x -> P.get() * x;
    public static final MathFunction X_tx = x -> x <= X_T.get() ? 0 : T.get() * (x - X_T.get());
    public static final MathFunction X_sx = x -> x < X_S.get() ? 0 : S.get();

    public static final MathFunction function = x -> P_x.value(x) - C_f.get() - C_v.value(x) -
                                                                    X_sx.value(x) - X_tx.value(x);

    static {
        P.bind(a.multiply(2)
                .add(b.multiply(3))
                .add(60));

        C_f.bind(b.multiply(20)
                .add(a.multiply(-10))
                .add(300));

        K_1.bind(a.multiply(0.5)
                .add(6));

        K_2.bind(b.multiply(0.01)
                .add(0.1));

        T.bind(a.add(b)
                .add(25));

        S.bind(a.multiply(-20)
                .add(b.multiply(-10))
                .add(500));

        X_T.bind(a.multiply(10)
                .add(b.multiply(5))
                .add(62));

        X_S.bind(a.multiply(15)
                .add(b.multiply(15))
                .add(30));

        X_1.bind(Bindings.min(X_T, X_S));
        X_2.bind(Bindings.max(X_T, X_S));
    }

    private GlobalParameters() {}

    public static Map<Double, Interval> intervals() {
        double min1 = 0;
        double max1 = X_1.get();

        Interval interval1 = X_1.isEqualTo(X_T).get() ? Interval.close(min1, max1) : Interval.closeOpen(min1, max1);

        double max2 = X_2.get();

        Interval interval2 = X_2.isEqualTo(X_S).get() ? Interval.open(max1, max2) : Interval.closeOpen(max1, max2);
        Interval interval3 = Interval.closeOpen(max2, Double.POSITIVE_INFINITY);

        return Map.of(interval1.generateIntervalValue(), interval1,
                        interval2.generateIntervalValue(), interval2,
                        interval3.generateIntervalValue(), interval3);
    }
}
