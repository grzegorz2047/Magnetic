package pl.grzegorz2047.magnetic;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Random;

import static java.lang.Thread.sleep;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class IsignModelSimplified {


    private final int arraySize;
    private final double temperature;
    private final int monteCarloSteps;
    private int nodes[][];
    private static Random random = new Random();


    public IsignModelSimplified(int arraySize, double temperature, int monteCarloSteps) {
        this.arraySize = arraySize;
        this.temperature = temperature;
        this.monteCarloSteps = monteCarloSteps;
    }

    public boolean runModel() {
        nodes = new int[arraySize][arraySize];
        randomizeNodes();

        Group root = new Group();
        Stage secondStage = new Stage();

        Canvas canvas = new Canvas(400, 400);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, 800, 600);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        fillWindow(gc);

        secondStage.setScene(scene);
        secondStage.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= monteCarloSteps; i++) {
                    doMCSStep();
                    if (i % 10 == 0) {
                        System.out.println("Magnetyzacja: " + calculateMagnetism());
                        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                        fillWindow(gc);
                        try {
                            sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Thread.currentThread().interrupt();
                System.exit(0);
            }
        }).start();
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

    private void doMCSStep() {
        for (int i = 0; i < Math.pow(arraySize, 2); i++) {
            touchNode();
        }

    }

    private void invertNode(int i, int j) {
        nodes[i][j] = -nodes[i][j];
    }

    public void fillWindow(GraphicsContext gc) {
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < arraySize; i++) {
                            for (int j = 0; j < arraySize; j++) {
                                int nodeValue = nodes[i][j];
                                if (nodeValue == 1) {
                                    gc.setFill(Color.BLUE);
                                } else {
                                    gc.setFill(Color.BLACK);
                                }
                                gc.fillRect(i * 3, j * 3, 3, 3);

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

        //int ep = -currentNodeState * (leftNeighbour + rightNeighbour + upNeighbour + downNeighbour);
        //int ek = currentNodeState * (leftNeighbour + rightNeighbour + upNeighbour + downNeighbour);
        int deltaE = 2 * currentNodeState * (leftNeighbour + rightNeighbour + upNeighbour + downNeighbour);// To jest to samo co odejmowanie ek i ep
        if (deltaE <= 0) {
            invertNode(x, y);
            return;
        }
        // System.out.println("delta: " + deltaE + " ep: " + ep + "  ek: " + ek);
        double p = Math.exp(-deltaE / temperature);
        double randNow = random.nextDouble() / Integer.MAX_VALUE;

        //System.out.println("p: " + p + "      RandNow: " + randNow);
        if (isRandomlyChosenToInvert(p, randNow)) {
            invertNode(x, y);
        }

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


}
