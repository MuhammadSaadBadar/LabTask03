package org.openjfx;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Task extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label fullNameLabel = new Label("Full Name:");
        TextField fullNameField = new TextField();

        Label idLabel = new Label("ID:");
        TextField idField = new TextField();

        Label genderLabel = new Label("Gender:");

        ToggleGroup genderGroup = new ToggleGroup();
        RadioButton maleRadio = new RadioButton("Male");
        maleRadio.setToggleGroup(genderGroup);
        RadioButton femaleRadio = new RadioButton("Female");
        femaleRadio.setToggleGroup(genderGroup);

        HBox genderBox = new HBox(10, maleRadio, femaleRadio);
        genderBox.setAlignment(Pos.CENTER_LEFT);

        Label provinceLabel = new Label("Home Province:");

        ComboBox<String> provinceComboBox = new ComboBox<>();
        provinceComboBox.getItems().addAll("Punjab", "Sindh", "Balochistan", "Khyber Pahkhtunkhwa", "Azad Kashmir");
        provinceComboBox.setPromptText("Select Province");

        Label dobLabel = new Label("DOB:");
        DatePicker dobPicker = new DatePicker();

        Button newRecordButton = new Button("New");
        Button deleteButton = new Button("Delete");
        Button restoreButton = new Button("Restore");
        Button findPrevButton = new Button("Find Prev");
        Button findNextButton = new Button("Find Next");
        Button criteriaButton = new Button("Criteria");
        Button closeButton = new Button("Close");

        newRecordButton.setOnAction(e -> {
            String fullName = fullNameField.getText();
            String id = idField.getText();
            String gender = null;
            if (genderGroup.getSelectedToggle() != null) {
                gender = ((RadioButton) genderGroup.getSelectedToggle()).getText();
            }
            String province = provinceComboBox.getValue();
            String dob = dobPicker.getValue() != null ? dobPicker.getValue().toString() : "";

            if (fullName.isEmpty() || id.isEmpty() || gender == null || province == null || dob.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields before saving.");
                alert.show();
                return;
            }

            File file = new File("Data.txt");
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write("ID: " + id + "\n");
                writer.write("Full Name: " + fullName + "\n");
                writer.write("Gender: " + gender + "\n");
                writer.write("Home Province: " + province + "\n");
                writer.write("DOB: " + dob + "\n");
                writer.write("---------------\n");
            } catch (IOException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error writing to file: " + ex.getMessage());
                alert.show();
            }
        });

        findPrevButton.setOnAction(e -> {
            Stage findStage = new Stage();
            findStage.setTitle("Find Record by ID");

            Label findIdLabel = new Label("Enter ID:");
            TextField findIdField = new TextField();
            Button searchButton = new Button("Search");
            Label resultLabel = new Label();

            searchButton.setOnAction(event -> {
                String searchId = findIdField.getText();

                File file = new File("Data.txt");
                if (file.exists()) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        boolean recordFound = false;
                        StringBuilder result = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            if (line.equals("ID: " + searchId)) {
                                recordFound = true;
                                result.append("Record found:\n").append(line).append("\n");
                                for (int i = 0; i < 4; i++) {
                                    line = reader.readLine();
                                    result.append(line).append("\n");
                                }
                                result.append("---------------\n");
                                break;
                            }
                        }

                        if (!recordFound) {
                            resultLabel.setText("No record found for ID: " + searchId);
                        } else {
                            resultLabel.setText(result.toString());
                        }
                    } catch (IOException ex) {
                        resultLabel.setText("Error reading file: " + ex.getMessage());
                    }
                }
            });

            VBox findLayout = new VBox(10, findIdLabel, findIdField, searchButton, resultLabel);
            findLayout.setPadding(new Insets(0));
            findStage.setScene(new Scene(findLayout, 400, 200));
            findStage.show();
        });

        closeButton.setOnAction(e -> {
            primaryStage.close();
        });

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));
        formGrid.add(fullNameLabel, 0, 0);
        formGrid.add(fullNameField, 1, 0);
        formGrid.add(idLabel, 0, 1);
        formGrid.add(idField, 1, 1);
        formGrid.add(genderLabel, 0, 2);
        formGrid.add(genderBox, 1, 2);
        formGrid.add(provinceLabel, 0, 3);
        formGrid.add(provinceComboBox, 1, 3);
        formGrid.add(dobLabel, 0, 4);
        formGrid.add(dobPicker, 1, 4);

        VBox buttonBox = new VBox(10);
        buttonBox.setPadding(new Insets(10));
        buttonBox.getChildren().addAll(newRecordButton, deleteButton, restoreButton, findPrevButton, findNextButton, criteriaButton, closeButton);

        HBox root = new HBox(20);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(formGrid, buttonBox);

        formGrid.setStyle("-fx-background-color: #F0F0F0; -fx-padding: 10;");
        buttonBox.setStyle("-fx-background-color: #E0E0E0; -fx-padding: 10;");
        root.setStyle("-fx-background-color: #FFFFFF;");

        Scene scene = new Scene(root, 450, 300);
        primaryStage.setTitle("Form Layout with ComboBox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
