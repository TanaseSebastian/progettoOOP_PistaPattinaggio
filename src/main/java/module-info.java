module org.example.progettooop_pistapattinaggio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires java.desktop;
    requires com.google.zxing;
    requires com.google.zxing.javase;

    opens org.example.progettooop_pistapattinaggio to javafx.fxml;
    exports org.example.progettooop_pistapattinaggio;
}