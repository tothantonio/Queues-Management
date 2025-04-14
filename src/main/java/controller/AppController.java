package controller;

import business_logic.SimulationManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import util.SelectionPolicy;

import java.util.ArrayList;

public class AppController {
    private SimulationManager simulationManager;
    private TextArea logArea;
    private TextArea statsArea;

    public void startSimulation(TextField noClientsField, TextField noServersField, TextField timeLimitField,
                                TextField minArrivalField, TextField maxArrivalField, TextField minProcessingField,
                                TextField maxProcessingField, ComboBox<SelectionPolicy> strategyComboBox, Stage primaryStage) {
        logArea = new TextArea();
        logArea.setEditable(false);
        logArea.setPrefHeight(400);

        statsArea = new TextArea();
        statsArea.setEditable(false);
        statsArea.setPrefHeight(200);

        VBox vbox = new VBox(10, logArea, statsArea);
        primaryStage.setScene(new Scene(vbox, 600, 600));
        primaryStage.setTitle("Queue Evolution and Statistics");
        primaryStage.show();

        ArrayList<Integer> input = new ArrayList<>();
        input.add(Integer.parseInt(noClientsField.getText()));
        input.add(Integer.parseInt(noServersField.getText()));
        input.add(Integer.parseInt(timeLimitField.getText()));
        input.add(Integer.parseInt(minArrivalField.getText()));
        input.add(Integer.parseInt(maxArrivalField.getText()));
        input.add(Integer.parseInt(minProcessingField.getText()));
        input.add(Integer.parseInt(maxProcessingField.getText()));

        SelectionPolicy selectedPolicy = strategyComboBox.getValue();
        simulationManager = new SimulationManager(input, selectedPolicy, this::updateLog, this::updateStats);
        Thread simulationThread = new Thread(simulationManager);
        simulationThread.start();
    }

    private void updateLog(String log) {
        Platform.runLater(() -> logArea.appendText(log + "\n"));
    }

    private void updateStats(String stats) {
        Platform.runLater(() -> statsArea.setText(stats));
    }
}