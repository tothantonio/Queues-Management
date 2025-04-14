package application;

import controller.AppController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.SelectionPolicy;

import java.util.Objects;

public class App extends Application {
    private final AppController appController = new AppController();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simulation Manager");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setAlignment(Pos.CENTER);
        grid.setId("root");

        Label noClientsLabel = new Label("Number of Clients:");
        TextField noClientsField = new TextField();
        noClientsLabel.getStyleClass().add("label");
        noClientsField.getStyleClass().add("text-field");
        grid.add(noClientsLabel, 0, 0);
        grid.add(noClientsField, 1, 0);

        Label noServersLabel = new Label("Number of Queues:");
        TextField noServersField = new TextField();
        noServersLabel.getStyleClass().add("label");
        noServersField.getStyleClass().add("text-field");
        grid.add(noServersLabel, 0, 1);
        grid.add(noServersField, 1, 1);

        Label timeLimitLabel = new Label("Time Limit:");
        TextField timeLimitField = new TextField();
        timeLimitLabel.getStyleClass().add("label");
        timeLimitField.getStyleClass().add("text-field");
        grid.add(timeLimitLabel, 0, 2);
        grid.add(timeLimitField, 1, 2);

        Label minArrivalLabel = new Label("Min Arrival Time:");
        TextField minArrivalField = new TextField();
        minArrivalLabel.getStyleClass().add("label");
        minArrivalField.getStyleClass().add("text-field");
        grid.add(minArrivalLabel, 0, 3);
        grid.add(minArrivalField, 1, 3);

        Label maxArrivalLabel = new Label("Max Arrival Time:");
        TextField maxArrivalField = new TextField();
        maxArrivalLabel.getStyleClass().add("label");
        maxArrivalField.getStyleClass().add("text-field");
        grid.add(maxArrivalLabel, 0, 4);
        grid.add(maxArrivalField, 1, 4);

        Label minProcessingLabel = new Label("Min Processing Time:");
        TextField minProcessingField = new TextField();
        minProcessingLabel.getStyleClass().add("label");
        minProcessingField.getStyleClass().add("text-field");
        grid.add(minProcessingLabel, 0, 5);
        grid.add(minProcessingField, 1, 5);

        Label maxProcessingLabel = new Label("Max Processing Time:");
        TextField maxProcessingField = new TextField();
        maxProcessingLabel.getStyleClass().add("label");
        maxProcessingField.getStyleClass().add("text-field");
        grid.add(maxProcessingLabel, 0, 6);
        grid.add(maxProcessingField, 1, 6);

        Label strategyLabel = new Label("Selection Strategy:");
        ComboBox<SelectionPolicy> strategyComboBox = new ComboBox<>();
        strategyComboBox.getItems().addAll(SelectionPolicy.SHORTEST_QUEUE, SelectionPolicy.SHORTEST_TIME);
        strategyComboBox.setValue(SelectionPolicy.SHORTEST_QUEUE);
        strategyLabel.getStyleClass().add("label");
        grid.add(strategyLabel, 0, 7);
        grid.add(strategyComboBox, 1, 7);

        Button startButton = new Button("Start Simulation");
        startButton.getStyleClass().add("button");
        grid.add(startButton, 1, 8);

        startButton.setOnAction(event -> appController.startSimulation(noClientsField, noServersField, timeLimitField,
                minArrivalField, maxArrivalField, minProcessingField, maxProcessingField, strategyComboBox, primaryStage));

        Scene scene = new Scene(grid, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/application.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}