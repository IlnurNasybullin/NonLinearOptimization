package org.example.nlo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.example.nlo.task.Extremum;
import org.example.nlo.task.Gradients;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.nlo.task.GlobalParameters.*;

public class App extends Application {

    public static final int MAX_ITER = 100_000;
    private static final SimpleDoubleProperty currentX = new SimpleDoubleProperty();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Non Linear Optimization");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        lineChart.setTitle("Profit from machine tools");

        double gradientX = Gradients.gradientX(function, Extremum.MAX, MAX_ITER, intervals());
        long grX = (long) gradientX;
        double fx1 = function.value(grX);
        double fx2 = function.value(grX + 1);
        double fx = Math.max(fx1, fx2);
        if (fx1 < fx2) {
            grX++;
        }

        int xi = 0;
        List<XYChart.Data<Number, Number>> data = IntStream.rangeClosed(xi, (int) grX)
                .mapToObj(x -> Map.entry((Number) x, (Number) function.value(x)))
                .map(entry -> new XYChart.Data<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().addAll(data);
        lineChart.setCreateSymbols(true);

        double fv = fx;
        xi = (int) (grX + 1);
        while (fv > 0) {
            fv = function.value(xi);
            series.getData().add(new XYChart.Data<>(xi, fv));
            xi += 1;
        }

        series.setName("profit function");

        XYChart.Series<Number, Number> maxPoint =
                new XYChart.Series<>();
        maxPoint.getData().add(new XYChart.Data<>(grX, fx));
        maxPoint.setName("Extremum value");

        XYChart.Series<Number, Number> currentPoint = new XYChart.Series<>();
        double current = currentX.get();
        currentPoint.getData().add(new XYChart.Data<>(current, function.value(current)));
        currentPoint.setName("Current value");

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
        lineChart.getData().add(maxPoint);
        lineChart.getData().add(currentPoint);

        series.getData().forEach(d -> d.getNode().setVisible(false));

        maxPoint.getData()
                .forEach(d -> d.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: green, white"));

        currentPoint.getData()
                .forEach(d -> d.getNode().lookup(".chart-line-symbol").setStyle("-fx-background-color: blue, white"));

        Platform.runLater(() -> {
            lineChart.lookup(".default-color1.chart-legend-item-symbol")
                    .setStyle("-fx-background-color: green, white;");
            lineChart.lookup(".default-color2.chart-legend-item-symbol")
                    .setStyle("-fx-background-color: blue, white;");
        });

        stage.setScene(scene);
        stage.show();

        System.out.printf("Current x value: %.2f\n", currentX.get());
        System.out.printf("Current profit (f(x)) value: %.2f bln\n", function.value(current));
        System.out.printf("Extremum x value: %d\n", grX);
        System.out.printf("Extremum profit (f(x)) value: %.2f bln\n", fx);
    }

    public static void main(String[] args) {
        Arguments arguments = Arguments.of(args);
        a.set(arguments.a());
        b.set(arguments.b());
        currentX.set(arguments.currentX());

        launch(args);
    }
}
