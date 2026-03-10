package com.codexarcadesuite;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class CodexArcadeSuiteApp extends Application
{
    private static final String APP_PATH_PROPERTY = "jpackage.app-path";

    private static final SuiteEntry[] ENTRIES = {
            new SuiteEntry("CodexCommand", "Missile defense.", "CodexCommand.exe",
                    "https://github.com/ferret99gt/CodexCommand"),
            new SuiteEntry("Codexroids", "Vector asteroid survival.", "Codexroids.exe",
                    "https://github.com/ferret99gt/Codexroids"),
            new SuiteEntry("Codextris", "Falling blocks.", "Codextris.exe",
                    "https://github.com/ferret99gt/Codextris"),
            new SuiteEntry("CodexMan", "Maze chase.", "CodexMan.exe",
                    "https://github.com/ferret99gt/CodexMan"),
            new SuiteEntry("Codexaga", "Arcade shooter.", "Codexaga.exe",
                    "https://github.com/ferret99gt/Codexaga")
    };

    @Override
    public void start(Stage stage)
    {
        VBox root = new VBox(18);
        root.setPadding(new Insets(24));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #09152a, #030916);");

        Label title = new Label("Codex Arcade Suite");
        title.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: #8fe2ff;");

        Label subtitle = new Label("Five standalone Codex arcade projects, one shared runtime.");
        subtitle.setStyle("-fx-font-size: 15px; -fx-text-fill: #b9d7ff;");

        root.getChildren().addAll(title, subtitle);
        for (SuiteEntry entry : ENTRIES)
        {
            root.getChildren().add(createLauncherCard(entry));
        }

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);
        scrollPane.setStyle("-fx-background: #030916; -fx-background-color: #030916;");

        Scene scene = new Scene(scrollPane, 560, 520, Color.BLACK);
        stage.setTitle("Codex Arcade Suite");
        stage.setScene(scene);
        stage.setMinWidth(460);
        stage.setMinHeight(360);
        stage.setResizable(true);
        stage.show();
    }

    private HBox createLauncherCard(SuiteEntry entry)
    {
        Label nameLabel = new Label(entry.name());
        nameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Label descLabel = new Label(entry.description());
        descLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #99b4db;");

        VBox textColumn = new VBox(4, nameLabel, descLabel);

        Button playButton = new Button("Play");
        playButton.setStyle("-fx-background-color: #2f9bff; -fx-text-fill: white; -fx-font-weight: bold;");
        playButton.setOnAction(event -> launch(entry.executableName()));

        Button repoButton = new Button("Repo");
        repoButton.setStyle("-fx-background-color: #203959; -fx-text-fill: #d8ebff; -fx-font-weight: bold;");
        repoButton.setOnAction(event -> getHostServices().showDocument(entry.repoUrl()));

        HBox buttons = new HBox(10, playButton, repoButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox card = new HBox(14, textColumn, spacer, buttons);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(14));
        card.setStyle("-fx-background-color: rgba(17, 34, 68, 0.88);"
                + "-fx-background-radius: 14; -fx-border-color: rgba(100, 170, 255, 0.35);"
                + "-fx-border-radius: 14;");
        return card;
    }

    private void launch(String executableName)
    {
        Path launcherDirectory = resolveLauncherDirectory();
        Path executable = launcherDirectory.resolve(executableName);
        if (!Files.exists(executable))
        {
            showError("Launcher not found", "Could not find " + executableName + " next to the suite launcher.");
            return;
        }

        try
        {
            new ProcessBuilder(executable.toString())
                    .directory(launcherDirectory.toFile())
                    .start();
        }
        catch (IOException ex)
        {
            showError("Launch failed", ex.getMessage());
        }
    }

    private Path resolveLauncherDirectory()
    {
        String appPath = System.getProperty(APP_PATH_PROPERTY);
        if (appPath != null && !appPath.isBlank())
        {
            Path path = Path.of(appPath).toAbsolutePath();
            if (path.getParent() != null)
            {
                return path.getParent();
            }
        }
        return Path.of(System.getProperty("user.dir", ".")).toAbsolutePath();
    }

    private void showError(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    private record SuiteEntry(String name, String description, String executableName, String repoUrl)
    {
    }
}
