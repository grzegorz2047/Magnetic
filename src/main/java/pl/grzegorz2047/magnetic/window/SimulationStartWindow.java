package pl.grzegorz2047.magnetic.window;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import pl.grzegorz2047.magnetic.Simulation;

import java.io.File;
import java.nio.DoubleBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class SimulationStartWindow {

    private boolean started;
    private TextField temperatureField;
    private TextField monteCarloStepsField;
    private TextField spinNetworkSizeField;
    private TextField lineFileField;
    private TextField filenameField;
    private MagnetismChart m;

    public void show() {
        if (started) {
            return;
        }
        started = true;
        GridPane grid = prepareGrid();

        createHeader(grid, "Ustawienia symulacji");
        createLabel(grid, "Wielkość sieci spinów:", 1);
        createLabel(grid, "Testowana temperatura:", 2);
        createLabel(grid, "Liczba kroków Monte Carlo:", 3);
        createLabel(grid, "Od ilu wziac: ", 4);
        createLabel(grid, "Z jakiego pliku: ", 5);


        this.spinNetworkSizeField = createTextField(grid, 1);
        this.temperatureField = createTextField(grid, 2);
        this.monteCarloStepsField = createTextField(grid, 3);
        this.lineFileField = createTextField(grid, 4);
        this.filenameField = createTextField(grid, 5);

        final Text actiontarget = createTextAction(grid);


        Button startBtn = createStartSimulationButton(grid);

        Button loadBtn = createLoadDataButton(grid);


        registerOnClickListenerForStartSimulationButton(actiontarget, startBtn);
        registerOnClickListenerForLoadButton(loadBtn);

        Stage window = new Stage();
        window.setTitle("Symulator magnetyzacji");
        window.setResizable(false);
        window.setWidth(400);
        window.setHeight(400);
        prepareWindowSpaceForWindow(grid, window);
        window.show();
    }

    private Text createTextAction(GridPane grid) {
        final Text actiontarget = new Text();
        grid.add(actiontarget, 0, 6);
        return actiontarget;
    }

    private void prepareWindowSpaceForWindow(GridPane grid, Stage window) {
        Scene windowSpace = new Scene(grid, 300, 300);
        window.setScene(windowSpace);
        windowSpace.getStylesheets().add("WindowSpaceViewProp.css");
    }

    private void registerOnClickListenerForStartSimulationButton(final Text actiontarget, Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                //actiontarget.setText("Simulation params updated!");
                try {
                    Simulation m = new Simulation(Integer.valueOf(spinNetworkSizeField.getText()), Double.valueOf(temperatureField.getText()), Integer.valueOf(monteCarloStepsField.getText()));
                    m.startSimulation();

                } catch (Exception ex) {
                    actiontarget.setText("Niepoprawne wartości!");
                }
            }
        });
    }


    private void registerOnClickListenerForLoadButton(Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                //actiontarget.setText("Simulation params updated!");
                try {
                    if (m == null) {
                        m = new MagnetismChart();
                        m.invoke("temperatura", "średnia magnetyzacja");
                    }

                    double med = 0;
                    List<String> strings = Files.readAllLines(new File(filenameField.getText()).toPath());
                    for (int i = Integer.valueOf(lineFileField.getText()); i < strings.size(); i++) {
                        med += Double.valueOf(strings.get(i));
                    }
                    med = med / (strings.size() - Integer.valueOf(lineFileField.getText()));
                    System.out.println("Wczytana srednia: " + med);
                    m.putMagnetismOnChart(med, Double.valueOf(filenameField.getText()));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private Button createStartSimulationButton(GridPane grid) {
        Button btn = new Button("Nowa symulacja");
        btn.setMinWidth(40);
        HBox hbBtn = new HBox(15);

        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 6);
        return btn;
    }

    private Button createLoadDataButton(GridPane grid) {
        Button btn = new Button("Z pliku");
        btn.setMinWidth(40);
        HBox hbBtn = new HBox(5);
        hbBtn.setMinWidth(40);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 7);
        return btn;
    }

    private TextField createTextField(GridPane grid, int row) {
        TextField temperatureField = new TextField();
        grid.add(temperatureField, 1, row);
        return temperatureField;
    }

    private void createLabel(GridPane grid, String text, int rowIndex) {
        Label userName = new Label(text);
        grid.add(userName, 0, rowIndex);
    }

    private void createHeader(GridPane grid, String headerName) {
        Text scenetitle = new Text(headerName);
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
    }

    private GridPane prepareGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }

}
