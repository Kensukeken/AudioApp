# 🎧 Kensukeken Audio Player

![Purple-themed Audio Player Screenshot](screenshot.png)

## 🚀 Overview
A sleek JavaFX audio player with purple aesthetics featuring playlist management, playback controls, and volume adjustment.

## 🛠️ Technical Components

### 🎛️ Core Functionality
```java
public class MyAudioPlayer extends Application {
    private MediaPlayer mediaPlayer;
    private List<String> playlist = new ArrayList<>();
    private int currentSongIndex = 0;
    private boolean isLooping = false;
    // ... UI components
}
```
- **MediaPlayer**: JavaFX's robust audio playback engine
- **Playlist Management**: ArrayList-based track storage
- **State Tracking**: Current index and loop status

### 🎨 UI Components
```java
// Layout setup
VBox root = new VBox(10);
root.setStyle("-fx-background-color: linear-gradient(to bottom, #4b0082, #8a2be2);");

// Control buttons
HBox controlBox = new HBox(10, playButton, stopButton, nextButton, loopButton);
```
- **Main Container**: VBox with 10px spacing and purple gradient
- **Control Panel**: HBox with playback buttons
- **Playlist View**: ListView with custom styling

### 🎚️ Player Controls
```java
private Button createStyledButton(String text) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: #9370db; -fx-text-fill: white;");
    // Hover effects
    return button;
}
```
- **Styled Buttons**: Purple with white text and hover effects
- **Slider Controls**: Progress bar and volume adjustment
- **Dynamic Labels**: Current song display

## 📥 Installation

### Requirements
- Java 17+
- JavaFX 17 SDK

### Build & Run
```bash
# Clone repository
git clone https://github.com/Kensukeken/AudioApp.git

# Build with Gradle
cd AudioApp
./gradlew build

# Run application
./gradlew run
```

## 🎮 Usage Guide

### Playback Controls
| Button | Action | Code Reference |
|--------|--------|----------------|
| ▶️ Play/Pause | Toggle playback | `playButton.setOnAction()` |
| ⏹ Stop | Reset playback | `stopButton.setOnAction()` |
| ⏭ Next | Skip to next track | `nextButton.setOnAction()` |
| 🔄 Loop | Toggle repeat mode | `loopButton.setOnAction()` |

### Playlist Management
```java
addButton.setOnAction(e -> {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Audio Files", "*.mp3", "*.wav", "*.aac")
    );
    // ... file handling
});
```
- Supports MP3, WAV, AAC formats
- Click-to-play from playlist
- Automatic playback of first added song

## 🧑💻 Development

### Project Structure
```
src/main/java/com/myaudioplayer/
├──── MyAudioPlayer.java         # Main application class
├──── AudioController.java        # Audio Controller class
├──module-info.java
resources/com.myaudioplayer
├── audio-view.fxml
```

### Key Methods
1. **playSong()** - Handles media loading and playback
2. **setupEventHandlers()** - Configures all UI interactions
3. **createComponents()** - Initializes UI elements

## 🐛 Troubleshooting

### Common Issues
1. **No audio output**:
    - Verify system volume
    - Check file permissions
    - Ensure supported audio format

2. **UI rendering problems**:
   ```bash
   # Run with JavaFX module path
   ./gradlew run --args="--module-path /path/to/javafx-sdk --add-modules javafx.controls,javafx.media
   ```

💡 **Pro Tip**: Use the `volumeSlider` for precise volume control (0-100 range)  
🐞 Found an issue? Report it on [GitHub Issues](https://github.com/yourusername/kensukeken-audio-player/issues)  
⭐ **Star the project** if you find it useful!
