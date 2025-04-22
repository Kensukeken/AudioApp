module com.myaudioplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.myaudioplayer to javafx.fxml;
    exports com.myaudioplayer;
}