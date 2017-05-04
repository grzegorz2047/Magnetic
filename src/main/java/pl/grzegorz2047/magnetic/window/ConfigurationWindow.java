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
import pl.grzegorz2047.magnetic.IsingModelSimplified;

/**
 * Plik stworzony przez grzegorz2047 27.04.2017.
 */
public class ConfigurationWindow {

    private boolean started;
    private TextField temperatureField;


    public void show() {
        if (started) {
            return;
        }
        started = true;
        GridPane grid = prepareGrid();

        createText(grid);

        createLabel(grid, "Network size:", 1);

        createTextField(grid, 1);

        createLabel(grid, "Temperature:", 2);

        this.temperatureField = createTextField(grid, 2);

        final Text actiontarget = createTextAction(grid);

        Button btn = createButton(grid);

        registerButtonListener(actiontarget, btn);

        createAndShowScene(grid);
    }

    private Text createTextAction(GridPane grid) {
        final Text actiontarget = new Text();
        grid.add(actiontarget, 1, 6);
        return actiontarget;
    }

    private void createAndShowScene(GridPane grid) {
        Stage secondStage = new Stage();
        Scene scene = new Scene(grid, 300, 275);
        secondStage.setScene(scene);
        scene.getStylesheets().add("SecondStage.css");
        secondStage.show();
    }

    private void registerButtonListener(final Text actiontarget, Button btn) {
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
                actiontarget.setFill(Color.FIREBRICK);
                //actiontarget.setText("Simulation params updated!");
                try {
                    IsingModelSimplified m =new IsingModelSimplified(50, Double.valueOf(temperatureField.getText()), 50000);
                    m.runModel();
                } catch (Exception ex) {
                    System.out.println("Not valid number given as temperature");
                }
            }
        });
    }

    private Button createButton(GridPane grid) {
        Button btn = new Button("Start new simulation");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_CENTER);
        hbBtn.getChildren().add(btn);
        grid.add(hbBtn, 1, 4);
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

    private void createText(GridPane grid) {
        Text scenetitle = new Text("Simulation properties");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
    }

    private GridPane prepareGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        return grid;
    }

}
