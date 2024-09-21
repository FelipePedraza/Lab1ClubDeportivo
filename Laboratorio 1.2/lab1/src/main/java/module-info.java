module com.lab1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.logging;

    opens com.lab1 to javafx.fxml;
    exports com.lab1;
}
