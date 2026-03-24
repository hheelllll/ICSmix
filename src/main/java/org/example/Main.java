package org.example;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.CompatibilityHints;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * ICSmix - Advanced iCalendar Merger Tool
 * Author: HE JIALE
 * A modern desktop application to merge multiple .ics files into one.
 */
public class Main extends Application {

    private ListView<File> fileListView;
    private Button btnMerge;
    private Button btnClear;

    // Define UI Constants for styling
    private static final String PRIMARY_COLOR = "#2196F3";
    private static final String SECONDARY_COLOR = "#E0E0E0";
    private static final String TEXT_COLOR_DARK = "#2C3E50";
    private static final String TEXT_COLOR_LIGHT = "#FFFFFF";
    private static final String BACKGROUND_COLOR = "#F4F6F8";

    private static final String BUTTON_STYLE_BASE =
            "-fx-font-size: 13px; -fx-padding: 10 20 10 20; -fx-background-radius: 4px; -fx-cursor: hand; -fx-font-weight: bold;";

    public static void main(String[] args) {
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING, true);
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_UNFOLDING, true);
        CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_VALIDATION, true);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("ICSmix - Advanced iCalendar Merger");

        // --- 1. Header Section ---
        Label lblTitle = new Label("ICSmix");
        lblTitle.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: " + PRIMARY_COLOR + ";");

        Label lblSubtitle = new Label("Merge multiple .ics files smoothly into a single one.");
        lblSubtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #7F8C8D;");

        VBox headerBox = new VBox(5);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10, 0, 15, 0));
        headerBox.getChildren().addAll(lblTitle, lblSubtitle);


        // --- 2. Center Section (File List) ---
        fileListView = new ListView<>();
        fileListView.setPrefHeight(250);
        fileListView.setStyle("-fx-background-color: white; -fx-background-radius: 4px; -fx-border-color: #DCDCDC; -fx-border-radius: 4px;");

        fileListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<File> call(ListView<File> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                            setGraphic(null);
                        } else {
                            setText(item.getName());
                            setStyle("-fx-font-size: 13px; -fx-padding: 8px; -fx-text-fill: " + TEXT_COLOR_DARK + "; -fx-border-color: transparent transparent #EEEEEE transparent;");
                        }
                    }
                };
            }
        });

        Label placeholder = new Label("No files selected. Click 'Add Files' to begin.");
        placeholder.setStyle("-fx-text-fill: #95A5A6; -fx-font-style: italic;");
        fileListView.setPlaceholder(placeholder);


        // --- 3. Right Section (Action Buttons) ---
        Button btnAddFiles = new Button("Add Files...");
        btnAddFiles.setStyle(BUTTON_STYLE_BASE + "-fx-background-color: " + PRIMARY_COLOR + "; -fx-text-fill: " + TEXT_COLOR_LIGHT + ";");

        btnClear = new Button("Clear List");
        btnClear.setStyle(BUTTON_STYLE_BASE + "-fx-background-color: " + SECONDARY_COLOR + "; -fx-text-fill: #333333;");
        btnClear.setDisable(true);

        VBox actionBox = new VBox(15);
        actionBox.setAlignment(Pos.TOP_CENTER);
        actionBox.setPadding(new Insets(0, 0, 0, 15));
        actionBox.getChildren().addAll(btnAddFiles, btnClear);


        // --- 4. Content Area ---
        HBox contentBox = new HBox();
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getChildren().addAll(fileListView, actionBox);
        HBox.setHgrow(fileListView, Priority.ALWAYS);


        // --- 5. Footer Section (Merge Button & Signature) ---
        btnMerge = new Button("Merge and Export...");
        btnMerge.setStyle("-fx-font-size: 15px; -fx-padding: 12 30 12 30; -fx-background-radius: 6px; -fx-cursor: hand; -fx-font-weight: bold; -fx-background-color: #27AE60; -fx-text-fill: white;");
        btnMerge.setDisable(true);

        // 🌟 新增：优雅的作者签名 🌟
        Label lblSignature = new Label("© 2026 Designed & Developed by HE JIALE");
        lblSignature.setStyle("-fx-font-size: 11px; -fx-text-fill: #95A5A6; -fx-font-style: italic;");

        VBox footerBox = new VBox(12); // 设置按钮和签名之间的间距
        footerBox.setAlignment(Pos.CENTER);
        footerBox.setPadding(new Insets(20, 0, 0, 0));
        footerBox.getChildren().addAll(btnMerge, lblSignature); // 把签名加在合并按钮的下方


        // --- 6. Final Root Layout ---
        VBox root = new VBox(0);
        root.setStyle("-fx-background-color: " + BACKGROUND_COLOR + ";");
        root.setPadding(new Insets(25));
        root.getChildren().addAll(headerBox, contentBox, footerBox);


        // --- 7. Event Handlers ---
        btnAddFiles.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select iCalendar Files");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("iCalendar Files (*.ics)", "*.ics"));
            List<File> selectedFiles = fileChooser.showOpenMultipleDialog(primaryStage);

            if (selectedFiles != null) {
                for (File file : selectedFiles) {
                    if (!fileListView.getItems().contains(file)) {
                        fileListView.getItems().add(file);
                    }
                }
                updateButtonStates();
            }
        });

        btnClear.setOnAction(e -> {
            fileListView.getItems().clear();
            updateButtonStates();
        });

        btnMerge.setOnAction(e -> {
            if (fileListView.getItems().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Warning", "Please add at least one .ics file.");
                return;
            }
            performMerge(primaryStage);
        });

        // --- 8. Show Window ---
        Scene scene = new Scene(root, 650, 480);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateButtonStates() {
        boolean hasFiles = !fileListView.getItems().isEmpty();
        btnClear.setDisable(!hasFiles);
        btnMerge.setDisable(!hasFiles);
    }

    private void performMerge(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Merged Calendar");
        fileChooser.setInitialFileName("Merged_Courses.ics");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("iCalendar Files (*.ics)", "*.ics"));
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile == null) {
            return;
        }

        try {
            Calendar mergedCalendar = new Calendar();
            mergedCalendar.getProperties().add(new ProdId("-//ICSmix App//HE JIALE//EN")); // 内部代码里也加上了你的名字
            mergedCalendar.getProperties().add(Version.VERSION_2_0);
            mergedCalendar.getProperties().add(CalScale.GREGORIAN);

            CalendarBuilder builder = new CalendarBuilder();
            int successCount = 0;
            int totalComponents = 0;

            for (File file : fileListView.getItems()) {
                try (InputStream fin = new FileInputStream(file)) {
                    Calendar currentCalendar = builder.build(fin);
                    for (Object componentObj : currentCalendar.getComponents()) {
                        if (componentObj instanceof CalendarComponent) {
                            mergedCalendar.getComponents().add((CalendarComponent) componentObj);
                            totalComponents++;
                        }
                    }
                    successCount++;
                } catch (Exception ex) {
                    System.err.println("Error reading " + file.getName() + ": " + ex.getMessage());
                }
            }

            FileOutputStream fout = new FileOutputStream(saveFile);
            CalendarOutputter outputter = new CalendarOutputter();
            outputter.setValidating(false);
            outputter.output(mergedCalendar, fout);
            fout.close();

            String successMsg = String.format("Successfully merged %d files.\nTotal events combined: %d\nSaved to: %s",
                    successCount, totalComponents, saveFile.getAbsolutePath());
            showAlert(Alert.AlertType.INFORMATION, "Merge Success", successMsg);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Merge Error", "An error occurred during merging:\n" + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.getDialogPane().setStyle("-fx-font-size: 13px;");
        alert.showAndWait();
    }
}