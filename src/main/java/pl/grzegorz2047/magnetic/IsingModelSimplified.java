package pl.grzegorz2047.magnetic;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.window.MainChart;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class IsingModelSimplified {


    private final int arraySize;
    private double temperature;
    private final int monteCarloSteps;
    private int nodes[][];
    private static Random random = new Random();
    private final int SIZE = 3;
    private ComputationThread computationThread;
    private Stage secondStage;
    private Canvas canvas;

    public IsingModelSimplified(int arraySize, double temperature, int monteCarloSteps) {
        this.arraySize = arraySize;
        this.temperature = temperature;
        this.monteCarloSteps = monteCarloSteps;
    }

    public boolean runModel() {
        nodes = new int[arraySize][arraySize];
        randomizeNodes();

        Group root = new Group();
        secondStage = new Stage();

        canvas = new Canvas(800, 700);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 700);

        secondStage.setScene(scene);
        secondStage.show();
        computationThread = new ComputationThread(this, canvas, secondStage);
        computationThread.start();
        return true;
    }

    private void randomizeNodes() {
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                boolean out = random.nextBoolean();
                nodes[i][j] = out ? 1 : -1;
            }
        }
    }


    public double calculateMagnetism() {
        double magnetism;
        double sum = 0;
        for (int i = 0; i < arraySize; i++) {
            for (int j = 0; j < arraySize; j++) {
                sum += nodes[i][j];
            }
        }
        //System.out.println("Suma: " + sum + " wszystkie: " + Math.pow(arraySize, 2));
        magnetism = Math.abs(sum) / Math.pow(arraySize, 2);
        return magnetism;
    }

    public void doMCSStep() {
        for (int i = 0; i < Math.pow(arraySize, 2); i++) {
            touchNode();
        }

    }

    private void invertNode(int i, int j) {
        nodes[i][j] = -nodes[i][j];
    }

    public void fillWindow(Canvas canvas) {
        
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < arraySize; i++) {
                            for (int j = 0; j < arraySize; j++) {
                                int nodeValue = nodes[i][j];
                                if (nodeValue == 1) {
                                    gc.setFill(Color.WHITE);
                                } else {
                                    gc.setFill(Color.BLACK);
                                }

                                gc.fillRect(i * SIZE, j * SIZE, SIZE, SIZE);

                            }

                        }
                    }
                }
        );
    }

    private void touchNode() {
        int x = random.nextInt(arraySize);
        int y = random.nextInt(arraySize);
        int currentNodeState = nodes[x][y];

        int downNeighbour;
        int upNeighbour;
        int leftNeighbour;
        int rightNeighbour;

        if (isNeighbourOnTheRight(x)) {
            rightNeighbour = nodes[x + 1][y];
        } else {
            rightNeighbour = nodes[0][y];
        }

        if (isNeighbourOnTheLeft(x)) {
            leftNeighbour = nodes[x - 1][y];
        } else {
            leftNeighbour = nodes[arraySize - 1][y];
        }

        if (isNeighbourInTheBottom(y)) {
            downNeighbour = nodes[x][y + 1];
        } else {
            downNeighbour = nodes[x][0];
        }

        if (isNeighbourOnTheTop(y)) {
            upNeighbour = nodes[x][y - 1];
        } else {
            upNeighbour = nodes[x][arraySize - 1];
        }

        int ep = -currentNodeState * (leftNeighbour + rightNeighbour + upNeighbour + downNeighbour);
        int ek = currentNodeState * (leftNeighbour + rightNeighbour + upNeighbour + downNeighbour);
        double deltaE = ek - ep;
        if (deltaE <= 0) {
            invertNode(x, y);
            return;
        }
        // System.out.println("delta: " + deltaE + " ep: " + ep + "  ek: " + ek);
        double var = -deltaE / temperature;
        double p = Math.exp(var);
        double randNow = random.nextDouble();

        //System.out.println("p: " + p + "      RandNow: " + randNow);
        if (isRandomlyChosenToInvert(p, randNow)) {
            invertNode(x, y);
        }

    }

    public void stopModel() {
        computationThread.interrupt();

    }

    private boolean isRandomlyChosenToInvert(double p, double randNow) {
        return randNow <= p;
    }

    private boolean isCurrentEnergyLowerThanFutureEnergy(int ek, int ep) {
        return ek <= ep;
    }

    private int currentEnergy(int currentNodeState, int ei) {
        return ei;
    }

    private boolean isNeighbourOnTheTop(int y) {
        return (y - 1) != -1;
    }

    private boolean isNeighbourInTheBottom(int y) {
        return (y + 1) != arraySize;
    }

    private boolean isNeighbourOnTheLeft(int x) {
        return (x - 1) != -1;
    }

    private boolean isNeighbourOnTheRight(int x) {
        return (x + 1) != arraySize;
    }


    public int getArraySize() {
        return arraySize;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMonteCarloSteps() {
        return monteCarloSteps;
    }

    public int[][] getNodes() {
        return nodes;
    }

    public static Random getRandom() {
        return random;
    }

    public int getSIZE() {
        return SIZE;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}
