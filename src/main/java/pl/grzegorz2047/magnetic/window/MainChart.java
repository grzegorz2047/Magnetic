package pl.grzegorz2047.magnetic.window;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */

public class MainChart {
    private Stage stage;
    private XYChart.Series series;

    public MainChart(Stage stage) {
        this.stage = stage;
    }

    public void invoke() {
        series = new XYChart.Series();
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Magnetyzacja");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        stage.setTitle("Wykres liniowy");
                        series.setName("Przebieg");
                        //defining the axes

                        lineChart.setTitle("Simulator");
                        //defining a series


                        Scene scene = new Scene(lineChart, 800, 600);
                        lineChart.getData().add(series);

                        stage.setScene(scene);
                        stage.show();

                    }
                }
        );
    }

    public void putMagnetismOnChart(double magnetism, int mcsNumber) {
        series.getData().add(new XYChart.Data(mcsNumber, magnetism));
    }
}
