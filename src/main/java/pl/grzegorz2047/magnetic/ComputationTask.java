package pl.grzegorz2047.magnetic;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.window.MagnetismChart;

import java.io.File;
import java.io.FileWriter;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 28.04.2017.
 */
public class ComputationTask extends Task {

    private final Simulation ising;

    public ComputationTask(Simulation ising) {
        this.ising = ising;
    }

    @Override
    protected Object call() throws Exception {
        File file = new File(String.valueOf(ising.getTemperature()));
        FileWriter fileWriter = new FileWriter(file);
        for (int i = 1; i <= ising.getMonteCarloSteps(); i++) {
            ising.doMCSStep();
            fileWriter.write(String.valueOf(ising.calculateMagnetism()));
            fileWriter.write(System.lineSeparator());
            if (i % 500 == 0) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ising.renderVisualisation();
                    }
                });
                if (isCancelled()) {
                    return false;
                }
            }

        }
        fileWriter.flush();
        fileWriter.close();

        return true;
    }

}
