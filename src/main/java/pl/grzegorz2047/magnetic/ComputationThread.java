package pl.grzegorz2047.magnetic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.window.MainChart;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 28.04.2017.
 */
public class ComputationThread extends Thread {

    private final IsingModelSimplified ising;
    private final Canvas canvas;
    private final Stage secondStage;

    public ComputationThread(IsingModelSimplified ising, Canvas canvas, Stage secondStage) {
        this.ising = ising;
        this.canvas = canvas;
        this.secondStage = secondStage;
    }

    public void run() {
        MainChart chart = new MainChart(this.secondStage);
        chart.invoke();
        for (int i = 0; i <= ising.getMonteCarloSteps(); i++) {
            ising.doMCSStep();
            if (i % 100 == 0) {
                System.out.println("Magnetyzacja: " + ising.calculateMagnetism());

                chart.putMagnetismOnChart(ising.calculateMagnetism(), i);
                ising.fillWindow(canvas);

/*                try {

                    sleep(100);
                } catch (InterruptedException e) {
                    System.exit(0);
                }*/
            }
        }

    }


}
