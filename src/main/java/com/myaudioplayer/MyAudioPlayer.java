package com.myaudioplayer;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MyAudioPlayer extends Application {

    private MediaPlayer mediaPlayer;
    private List<String> playlist = new ArrayList<>();
    private int currentSongIndex = 0;
    private boolean isLooping = false;

    private Label currentSongLabel;
    private Slider progressSlider;
    private Slider volumeSlider;
    private Button playButton, stopButton, nextButton, loopButton, addButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create UI components
        createComponents();

        // Layout setup
        VBox root = new VBox(10); // Reduced spacing between elements
        root.setPadding(new Insets(15));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #4b0082, #8a2be2);");

        // Current song display
        currentSongLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        HBox songInfoBox = new HBox(currentSongLabel);
        songInfoBox.setAlignment(Pos.CENTER);
        songInfoBox.setPadding(new Insets(0, 0, 10, 0));

        // Progress slider
        progressSlider.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progressSlider, Priority.ALWAYS);

        // Control buttons
        HBox controlBox = new HBox(10, playButton, stopButton, nextButton, loopButton);
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(10, 0, 10, 0));

        // Volume control
        Label volumeLabel = new Label("Volume:");
        volumeLabel.setStyle("-fx-text-fill: white;");
        HBox volumeBox = new HBox(10, volumeLabel, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        // Playlist
        Label playlistLabel = new Label("Playlist:");
        playlistLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        ListView<String> playlistView = new ListView<>();
        playlistView.setStyle("-fx-control-inner-background: #e6e6fa; -fx-text-fill: #4b0082;");
        playlistView.setPrefHeight(200);
        VBox.setVgrow(playlistView, Priority.ALWAYS);

        // Add song button
        HBox addBox = new HBox(addButton);
        addBox.setAlignment(Pos.CENTER_RIGHT);
        addBox.setPadding(new Insets(10, 0, 0, 0));

        // Add all components to root
        root.getChildren().addAll(
                songInfoBox,
                progressSlider,
                controlBox,
                volumeBox,
                playlistLabel,
                playlistView,
                addBox
        );

        // Set up event handlers
        setupEventHandlers(playlistView);

        // Create scene and show stage
        Scene scene = new Scene(root, 400, 500);
        primaryStage.setTitle("Kensukeken Audio Player");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createComponents() {
        currentSongLabel = new Label("No song selected");

        progressSlider = new Slider();
        progressSlider.setStyle("-fx-control-inner-background: #e6e6fa;");

        volumeSlider = new Slider(0, 100, 50);
        volumeSlider.setStyle("-fx-control-inner-background: #e6e6fa;");

        // Create buttons with purple style
        playButton = createStyledButton("Play");
        stopButton = createStyledButton("Stop");
        nextButton = createStyledButton("Next");
        loopButton = createStyledButton("Loop: Off");
        addButton = createStyledButton("Add Song");
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: #9370db; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #8a2be2; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80px;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #9370db; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 80px;"));
        return button;
    }

    private void setupEventHandlers(ListView<String> playlistView) {
        // Play button action
        playButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                    mediaPlayer.pause();
                    playButton.setText("Play");
                } else {
                    mediaPlayer.play();
                    playButton.setText("Pause");
                }
            } else if (!playlist.isEmpty()) {
                playSong(playlist.get(currentSongIndex), playlistView);
            }
        });

        // Stop button action
        stopButton.setOnAction(e -> {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                playButton.setText("Play");
            }
        });

        // Next button action
        nextButton.setOnAction(e -> {
            if (!playlist.isEmpty()) {
                currentSongIndex = (currentSongIndex + 1) % playlist.size();
                playSong(playlist.get(currentSongIndex), playlistView);
            }
        });

        // Loop button action
        loopButton.setOnAction(e -> {
            isLooping = !isLooping;
            loopButton.setText(isLooping ? "Loop: On" : "Loop: Off");
            if (mediaPlayer != null) {
                if (isLooping) {
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                } else {
                    mediaPlayer.setCycleCount(1);
                }
            }
        });

        // Add song button action
        addButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.aac")
            );
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                String filePath = selectedFile.toURI().toString();
                playlist.add(filePath);
                playlistView.getItems().add(selectedFile.getName());

                // If it's the first song added, play it automatically
                if (playlist.size() == 1) {
                    currentSongIndex = 0;
                    playSong(filePath, playlistView);
                }
            }
        });

        // Playlist selection action
        playlistView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int selectedIndex = playlistView.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0 && selectedIndex < playlist.size()) {
                    currentSongIndex = selectedIndex;
                    playSong(playlist.get(selectedIndex), playlistView);
                }
            }
        });

        // Volume slider action
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newVal.doubleValue() / 100);
            }
        });
    }

    private void playSong(String filePath, ListView<String> playlistView) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        try {
            Media media = new Media(filePath);
            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnReady(() -> {
                try {
                    // Decode URL-encoded file path
                    String decodedPath = URLDecoder.decode(
                            media.getSource().replaceFirst("file:/", ""),
                            StandardCharsets.UTF_8.toString()
                    );
                    String songName = new File(decodedPath).getName();
                    currentSongLabel.setText("Now Playing: " + songName);
                    playlistView.getSelectionModel().select(currentSongIndex);
                    playButton.setText("Pause");
                    mediaPlayer.play();
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100);

                    if (isLooping) {
                        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    }
                } catch (Exception e) {
                    currentSongLabel.setText("Error displaying song name");
                }
            });

            mediaPlayer.currentTimeProperty().addListener((obs, oldVal, newVal) -> {
                if (!progressSlider.isValueChanging()) {
                    progressSlider.setValue(newVal.toSeconds() / mediaPlayer.getTotalDuration().toSeconds() * 100);
                }
            });

            progressSlider.valueChangingProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal) {
                    mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progressSlider.getValue() / 100));
                }
            });

            mediaPlayer.setOnEndOfMedia(() -> {
                if (!isLooping && !playlist.isEmpty()) {
                    currentSongIndex = (currentSongIndex + 1) % playlist.size();
                    playSong(playlist.get(currentSongIndex), playlistView);
                }
            });

        } catch (Exception e) {
            currentSongLabel.setText("Error playing: " + e.getMessage());
        }
    }
}