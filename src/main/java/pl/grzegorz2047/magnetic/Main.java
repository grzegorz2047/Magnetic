package pl.grzegorz2047.magnetic;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.window.ConfigurationWindow;
import pl.grzegorz2047.magnetic.window.MainChart;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) {
        ConfigurationWindow configurationWindow = new ConfigurationWindow();
        configurationWindow.show();
        IsignModelSimplified model = new IsignModelSimplified(50, 2.4, 50000);
        model.runModel();
        //new MainChart(stage).invoke();
    }

    public static void main(String[] args) {
        launch(args);
    }

}